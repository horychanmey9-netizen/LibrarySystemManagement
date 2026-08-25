<template>

  <div class="books-page">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="page-header">

      <div>

        <h1>
          Book Management
        </h1>

        <p>
          Manage all library books
        </p>

      </div>


      <!-- ADD BOOK -->

      <button
        class="add-btn"
        type="button"
        @click="openAddBook"
      >

        <i class="bi bi-plus-lg"></i>

        <span>
          Add Book
        </span>

      </button>

    </div>


    <!-- =====================================================
         SEARCH & FILTER
    ====================================================== -->

    <div class="filter-box">

      <!-- SEARCH -->

      <div class="search-box">

        <i
          class="bi bi-search search-icon"
        ></i>

        <input
          v-model="search"
          type="text"
          placeholder="Search books by title, author or ISBN..."
        />

      </div>


      <!-- CATEGORY -->

      <select
        v-model="selectedCategory"
      >

        <option value="">
          All Categories
        </option>

        <option
          v-for="category in categories"
          :key="category.id"
          :value="category.name"
        >

          {{ category.name }}

        </option>

      </select>


      <!-- STATUS -->

      <select
        v-model="selectedStatus"
      >

        <option value="">
          All Status
        </option>

        <option value="Available">
          Available
        </option>

        <option value="Borrowed">
          Borrowed
        </option>

        <option value="Overdue">
          Overdue
        </option>

      </select>


      <!-- RESET -->

      <button
        class="reset-btn"
        type="button"
        @click="resetFilters"
      >

        <i class="bi bi-arrow-clockwise"></i>

        Reset

      </button>

    </div>


    <!-- =====================================================
         TABLE
    ====================================================== -->

    <div class="table-container">

      <table>

        <!-- =================================================
             HEADER
        ================================================== -->

        <thead>

          <tr>

            <th>
              #
            </th>

            <th>
              BOOK
            </th>

            <th>
              AUTHOR
            </th>

            <th>
              CATEGORY
            </th>

            <th>
              ISBN
            </th>

            <th>
              QUANTITY
            </th>

            <th>
              STATUS
            </th>

            <th>
              ACTION
            </th>

          </tr>

        </thead>


        <!-- =================================================
             BODY
        ================================================== -->

        <tbody>

          <tr
            v-for="(book, index) in filteredBooks"
            :key="book.id"
          >

            <!-- NUMBER -->

            <td>

              {{ index + 1 }}

            </td>


            <!-- =================================================
                 BOOK
            ================================================== -->

            <td>

              <div class="book-info">

                <!-- BOOK IMAGE -->

                <div class="book-cover">

                  <img
                    v-if="book.image"
                    :src="getBookImage(book.image)"
                    :alt="book.title"
                    @error="handleImageError"
                  />

                  <i
                    v-else
                    class="bi bi-book"
                  ></i>

                </div>


                <!-- BOOK TEXT -->

                <div class="book-text">

                  <strong>
                    {{ book.title || "-" }}
                  </strong>

                  <small
                    v-if="book.description"
                    :title="book.description"
                  >

                    {{ book.description }}

                  </small>

                  <small
                    v-else
                    class="no-description"
                  >

                    No description

                  </small>

                </div>

              </div>

            </td>


            <!-- =================================================
                 AUTHOR
            ================================================== -->

            <td>

              {{ book.author || "-" }}

            </td>


            <!-- =================================================
                 CATEGORY
            ================================================== -->

            <td>

              <span class="category">

                {{ getCategoryName(book) }}

              </span>

            </td>


            <!-- =================================================
                 ISBN
            ================================================== -->

            <td>

              {{ book.isbn || "-" }}

            </td>


            <!-- =================================================
                 QUANTITY
            ================================================== -->

            <td>

              <span
                class="quantity"
                :class="getQuantityClass(book)"
              >

                {{ getQuantity(book) }}

              </span>

            </td>


            <!-- =================================================
                 STATUS
            ================================================== -->

            <td>

              <span
                class="status"
                :class="getStatusClass(book.status)"
              >

                {{ formatStatus(book.status) }}

              </span>

            </td>


            <!-- =================================================
                 ACTION
            ================================================== -->

            <td>

              <!-- EDIT -->

              <button
                class="edit-btn"
                type="button"
                title="Edit Book"
                @click="editBook(book.id)"
              >

                <i class="bi bi-pencil"></i>

              </button>


              <!-- DELETE -->

              <button
                class="delete-btn"
                type="button"
                title="Delete Book"
                @click="deleteBook(book.id)"
              >

                <i class="bi bi-trash"></i>

              </button>

            </td>

          </tr>


          <!-- =================================================
               NO RESULT
          ================================================== -->

          <tr
            v-if="filteredBooks.length === 0"
          >

            <td
              colspan="8"
              class="no-result"
            >

              <i
                class="bi bi-book no-result-icon"
              ></i>

              <p>
                No books found.
              </p>

              <small
                v-if="hasFilters"
              >
                Try changing your search or filters.
              </small>

            </td>

          </tr>

        </tbody>

      </table>

    </div>


    <!-- =====================================================
         PAGINATION / RESULT
    ====================================================== -->

    <div class="pagination">

      <span>

        Showing
        <strong>
          {{ filteredBooks.length }}
        </strong>
        of
        <strong>
          {{ books.length }}
        </strong>
        books

      </span>
    </div>

  </div>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from "vue";

import {
  getCategories
} from "../../service/categoryService";


// =====================================================
// PROPS
// =====================================================

const props = defineProps({

  books: {

    type: Array,

    default: () => []

  }

});


// =====================================================
// EVENTS
// =====================================================

const emit = defineEmits([

  "add-book",

  "edit-book",

  "delete-book"

]);


// =====================================================
// SEARCH
// =====================================================

const search = ref("");


// =====================================================
// FILTER
// =====================================================

const selectedCategory =
  ref("");

const selectedStatus =
  ref("");


// =====================================================
// CATEGORIES
// =====================================================

const categories =
  ref([]);


// =====================================================
// FETCH CATEGORIES
// =====================================================

async function fetchCategories() {

  try {

    const response =
      await getCategories();

    console.log(
      "CATEGORY API RESPONSE:",
      response
    );


    // =================================================
    // RESPONSE:
    //
    // {
    //   status: 200,
    //   data: [...]
    // }
    // =================================================

    if (
      response &&
      Array.isArray(response.data)
    ) {

      categories.value =
        response.data;

    }

    // =================================================
    // DIRECT ARRAY
    // =================================================

    else if (
      Array.isArray(response)
    ) {

      categories.value =
        response;

    }

    else {

      categories.value = [];

    }


    console.log(
      "CATEGORIES:",
      categories.value
    );

  } catch (error) {

    console.error(
      "Failed to fetch categories:",
      error
    );

    categories.value = [];

  }

}


// =====================================================
// ON MOUNTED
// =====================================================

onMounted(() => {

  fetchCategories();

});


// =====================================================
// GET CATEGORY NAME
// =====================================================

function getCategoryName(book) {

  // ===================================================
  // CASE 1
  //
  // category:
  // {
  //   id: 1,
  //   name: "Programming"
  // }
  // ===================================================

  if (
    book.category &&
    typeof book.category === "object"
  ) {

    return (
      book.category.name ||
      "-"
    );

  }


  // ===================================================
  // CASE 2
  //
  // category: "Programming"
  // ===================================================

  if (
    typeof book.category === "string"
  ) {

    return book.category;

  }


  // ===================================================
  // CASE 3
  //
  // categoryName: "Programming"
  // ===================================================

  if (
    book.categoryName
  ) {

    return book.categoryName;

  }


  return "-";

}


// =====================================================
// GET QUANTITY
//
// Backend:
// qty
//
// Frontend:
// quantity
// =====================================================

function getQuantity(book) {

  if (
    book.quantity !== undefined &&
    book.quantity !== null
  ) {

    return book.quantity;

  }


  if (
    book.qty !== undefined &&
    book.qty !== null
  ) {

    return book.qty;

  }


  return 0;

}


// =====================================================
// QUANTITY CLASS
// =====================================================

function getQuantityClass(book) {

  const quantity =
    Number(
      getQuantity(book)
    );


  if (quantity <= 0) {

    return "quantity-empty";

  }


  if (quantity <= 2) {

    return "quantity-low";

  }


  return "quantity-normal";

}


// =====================================================
// FORMAT STATUS
//
// AVAILABLE
// → Available
//
// BORROWED
// → Borrowed
//
// OVERDUE
// → Overdue
// =====================================================

function formatStatus(status) {

  if (!status) {

    return "-";

  }


  const value =
    String(status)
      .toLowerCase()
      .trim();


  return (
    value.charAt(0).toUpperCase() +
    value.slice(1)
  );

}


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass(status) {

  const value =
    String(status || "")
      .toLowerCase()
      .trim();


  if (
    value === "available"
  ) {

    return "available";

  }


  if (
    value === "borrowed"
  ) {

    return "borrowed";

  }


  if (
    value === "overdue"
  ) {

    return "overdue";

  }


  return "unknown";

}


// =====================================================
// BOOK IMAGE
// =====================================================

function getBookImage(image) {

  if (!image) {

    return "";

  }


  // Already complete URL

  if (
    image.startsWith("http://") ||
    image.startsWith("https://")
  ) {

    return image;

  }


  // Backend upload path

  return `http://localhost:8080/uploads/${image}`;

}


// =====================================================
// IMAGE ERROR
// =====================================================

function handleImageError(event) {

  event.target.style.display =
    "none";

}


// =====================================================
// FILTERED BOOKS
// =====================================================

const filteredBooks =
  computed(() => {

    return props.books.filter(
      (book) => {

        // =================================================
        // SEARCH TEXT
        // =================================================

        const searchText =
          search.value
            .toLowerCase()
            .trim();


        // =================================================
        // TITLE
        // =================================================

        const title =
          String(
            book.title || ""
          )
            .toLowerCase();


        // =================================================
        // AUTHOR
        // =================================================

        const author =
          String(
            book.author || ""
          )
            .toLowerCase();


        // =================================================
        // ISBN
        // =================================================

        const isbn =
          String(
            book.isbn || ""
          )
            .toLowerCase();


        // =================================================
        // SEARCH MATCH
        // =================================================

        const matchesSearch =

          title.includes(
            searchText
          )

          ||

          author.includes(
            searchText
          )

          ||

          isbn.includes(
            searchText
          );


        // =================================================
        // CATEGORY
        // =================================================

        const bookCategory =
          getCategoryName(book);


        const matchesCategory =

          selectedCategory.value === ""

          ||

          bookCategory ===
            selectedCategory.value;


        // =================================================
        // STATUS
        // =================================================

        const bookStatus =
          String(
            book.status || ""
          )
            .toLowerCase()
            .trim();


        const selectedStatusValue =
          String(
            selectedStatus.value || ""
          )
            .toLowerCase()
            .trim();


        const matchesStatus =

          selectedStatus.value === ""

          ||

          bookStatus ===
            selectedStatusValue;


        // =================================================
        // RETURN
        // =================================================

        return (

          matchesSearch &&

          matchesCategory &&

          matchesStatus

        );

      }
    );

  });


// =====================================================
// HAS FILTERS
// =====================================================

const hasFilters =
  computed(() => {

    return (

      search.value.trim() !== ""

      ||

      selectedCategory.value !== ""

      ||

      selectedStatus.value !== ""

    );

  });


// =====================================================
// RESET FILTERS
// =====================================================

function resetFilters() {

  search.value = "";

  selectedCategory.value = "";

  selectedStatus.value = "";

}


// =====================================================
// ADD BOOK
// =====================================================

function openAddBook() {

  console.log(
    "Opening Add Book..."
  );

  emit(
    "add-book"
  );

}


// =====================================================
// EDIT BOOK
// =====================================================

function editBook(bookId) {

  console.log(
    "Edit book ID:",
    bookId
  );

  emit(
    "edit-book",
    bookId
  );

}


// =====================================================
// DELETE BOOK
// =====================================================

function deleteBook(bookId) {

  console.log(
    "Delete book ID:",
    bookId
  );

  emit(
    "delete-book",
    bookId
  );

}

</script>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.books-page {
  width: 100%;
  min-height: calc(100vh - 70px);

  padding: 30px;
  box-sizing: border-box;

  background: #f8f9fc;

  font-family: Arial, sans-serif;
}


/* =====================================================
   HEADER
===================================================== */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;

  margin-bottom: 25px;
}

.page-header h1 {
  margin: 0;

  font-size: 28px;
  font-weight: 700;

  color: #172033;
}

.page-header p {
  margin: 6px 0 0;

  color: #7b8497;
  font-size: 14px;
}


/* =====================================================
   ADD BUTTON
===================================================== */

.add-btn {
  display: flex;
  align-items: center;
  justify-content: center;

  gap: 8px;

  background: #5b3df5;
  color: white;

  border: none;

  padding: 12px 20px;

  border-radius: 8px;

  cursor: pointer;

  font-size: 14px;
  font-weight: 600;

  transition: 0.2s;
}

.add-btn:hover {
  background: #4930d5;
}

.add-btn i {
  font-size: 16px;
}


/* =====================================================
   FILTER BOX
===================================================== */

.filter-box {
  display: flex;
  gap: 12px;

  margin-bottom: 20px;

  background: white;

  padding: 18px;

  border-radius: 10px;

  border: 1px solid #e5e8ef;

  box-sizing: border-box;
}


/* =====================================================
   SEARCH
===================================================== */

.search-box {
  flex: 1;

  position: relative;

  min-width: 200px;
}

.search-box input {
  width: 100%;

  box-sizing: border-box;

  padding: 12px 15px 12px 40px;

  border: 1px solid #dfe3eb;

  border-radius: 8px;

  outline: none;

  font-size: 14px;

  color: #333b4f;

  background: white;
}

.search-box input::placeholder {
  color: #a0a6b2;
}

.search-box input:focus {
  border-color: #5b3df5;

  box-shadow:
    0 0 0 3px
    rgba(91, 61, 245, 0.08);
}

.search-icon {
  position: absolute;

  left: 14px;
  top: 50%;

  transform: translateY(-50%);

  font-size: 16px;

  color: #8a92a3;

  pointer-events: none;
}


/* =====================================================
   SELECT
===================================================== */

select {
  width: 180px;

  padding: 12px;

  border: 1px solid #dfe3eb;

  border-radius: 8px;

  background: white;

  cursor: pointer;

  outline: none;

  color: #333b4f;

  font-size: 14px;
}

select:focus {
  border-color: #5b3df5;

  box-shadow:
    0 0 0 3px
    rgba(91, 61, 245, 0.08);
}


/* =====================================================
   RESET
===================================================== */

.reset-btn {
  display: flex;

  align-items: center;
  justify-content: center;

  gap: 7px;

  padding: 12px 18px;

  border: 1px solid #dfe3eb;

  background: white;

  border-radius: 8px;

  cursor: pointer;

  color: #667085;

  white-space: nowrap;

  font-size: 14px;

  transition: 0.2s;
}

.reset-btn:hover {
  background: #f1f2f6;

  border-color: #d4d8e1;
}

.reset-btn i {
  font-size: 15px;
}


/* =====================================================
   TABLE CONTAINER
   VERTICAL + HORIZONTAL SCROLL
===================================================== */

.table-container {
  width: 100%;

  background: white;

  border-radius: 10px;

  border: 1px solid #e5e8ef;

  box-sizing: border-box;

  /*
   * Main scroll
   */
  max-height: 600px;

  overflow-y: auto;
  overflow-x: auto;

  /*
   * Smooth scrolling
   */
  scroll-behavior: smooth;
}


/* =====================================================
   SCROLLBAR
===================================================== */

.table-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.table-container::-webkit-scrollbar-track {
  background: #f5f6f8;

  border-radius: 10px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #c7cad4;

  border-radius: 10px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #aeb2be;
}


/* Firefox */

.table-container {
  scrollbar-width: thin;
  scrollbar-color: #c7cad4 #f5f6f8;
}


/* =====================================================
   TABLE
===================================================== */

table {
  width: 100%;

  min-width: 1100px;

  border-collapse: separate;

  border-spacing: 0;
}


/* =====================================================
   TABLE HEADER
===================================================== */

thead th {
  position: sticky;

  top: 0;

  z-index: 10;

  text-align: left;

  padding: 15px;

  font-size: 12px;

  font-weight: 700;

  color: #7c8497;

  background: #fafbfc;

  white-space: nowrap;

  border-bottom: 1px solid #e5e8ef;
}


/*
 * Make the first header corner rounded
 */

thead th:first-child {
  border-top-left-radius: 10px;
}


/*
 * Make the last header corner rounded
 */

thead th:last-child {
  border-top-right-radius: 10px;
}


/* =====================================================
   TABLE BODY
===================================================== */

td {
  padding: 15px;

  border-top: 1px solid #edf0f5;

  color: #333b4f;

  font-size: 14px;

  white-space: nowrap;

  background: white;
}


/* =====================================================
   ROW
===================================================== */

tbody tr {
  transition: background 0.15s;
}

tbody tr:hover td {
  background: #fafbff;
}


/* =====================================================
   BOOK INFO
===================================================== */

.book-info {
  display: flex;

  align-items: center;

  gap: 12px;

  min-width: 280px;
}


/* =====================================================
   BOOK COVER
===================================================== */

.book-cover {
  width: 38px;
  height: 50px;

  display: flex;

  align-items: center;
  justify-content: center;

  background: #f0efff;

  border-radius: 5px;

  flex-shrink: 0;

  overflow: hidden;
}

.book-cover img {
  width: 100%;
  height: 100%;

  object-fit: cover;
}

.book-cover i {
  color: #5b3df5;

  font-size: 20px;
}


/* =====================================================
   BOOK TEXT
===================================================== */

.book-text {
  min-width: 0;
}

.book-text strong {
  display: block;

  max-width: 280px;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  color: #172033;

  font-size: 14px;
}

.book-text small {
  display: block;

  margin-top: 4px;

  max-width: 280px;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  color: #8a92a3;

  font-size: 12px;
}

.book-text .no-description {
  color: #b0b5c0;

  font-style: italic;
}


/* =====================================================
   CATEGORY
===================================================== */

.category {
  display: inline-block;

  padding: 5px 9px;

  background: #eeeeff;

  color: #5941e8;

  border-radius: 5px;

  font-size: 12px;

  font-weight: 500;
}


/* =====================================================
   QUANTITY
===================================================== */

.quantity {
  display: inline-flex;

  align-items: center;
  justify-content: center;

  min-width: 32px;

  padding: 4px 8px;

  border-radius: 5px;

  font-size: 13px;

  font-weight: 600;

  box-sizing: border-box;
}

.quantity-normal {
  background: #e5f8ed;

  color: #16864a;
}

.quantity-low {
  background: #fff3d8;

  color: #b77900;
}

.quantity-empty {
  background: #ffe5e5;

  color: #d93636;
}


/* =====================================================
   STATUS
===================================================== */

.status {
  display: inline-block;

  padding: 5px 10px;

  border-radius: 5px;

  font-size: 12px;

  font-weight: 600;
}

.available {
  background: #e5f8ed;

  color: #16864a;
}

.borrowed {
  background: #fff3d8;

  color: #b77900;
}

.overdue {
  background: #ffe5e5;

  color: #d93636;
}

.unknown {
  background: #eef0f3;

  color: #667085;
}


/* =====================================================
   ACTION BUTTONS
===================================================== */

.edit-btn,
.delete-btn {
  width: 34px;
  height: 34px;

  border: none;

  border-radius: 5px;

  cursor: pointer;

  margin-right: 5px;

  display: inline-flex;

  align-items: center;
  justify-content: center;

  transition: 0.2s;
}

.edit-btn {
  background: #eeeaff;

  color: #5941e8;
}

.delete-btn {
  background: #ffe7e7;

  color: #d93636;
}

.edit-btn:hover {
  background: #ddd7ff;
}

.delete-btn:hover {
  background: #ffd4d4;
}

.edit-btn i,
.delete-btn i {
  font-size: 15px;
}


/* =====================================================
   NO RESULT
===================================================== */

.no-result {
  text-align: center;

  padding: 50px 40px;

  color: #888;
}

.no-result-icon {
  display: block;

  margin: 0 auto 10px;

  font-size: 35px;

  color: #9ca3af;
}

.no-result p {
  margin: 0;

  font-size: 15px;

  color: #667085;
}

.no-result small {
  display: block;

  margin-top: 6px;

  color: #a0a6b2;
}


/* =====================================================
   PAGINATION / RESULT
===================================================== */

.pagination {
  display: flex;

  justify-content: space-between;

  align-items: center;

  padding: 18px 5px;

  color: #777;

  font-size: 14px;
}

.pagination strong {
  color: #333b4f;
}


/* =====================================================
   PAGINATION BUTTONS
===================================================== */

.pagination-buttons {
  display: flex;

  align-items: center;
}

.pagination button {
  width: 35px;
  height: 35px;

  margin-left: 5px;

  border: 1px solid #ddd;

  background: white;

  border-radius: 6px;

  cursor: pointer;

  transition: 0.2s;
}

.pagination button:hover:not(:disabled) {
  background: #f3f4f6;
}

.pagination button.active {
  background: #5b3df5;

  color: white;

  border-color: #5b3df5;
}

.pagination button:disabled {
  cursor: not-allowed;

  opacity: 0.5;
}


/* =====================================================
   RESPONSIVE - TABLET
===================================================== */

@media (max-width: 1100px) {

  .filter-box {
    flex-wrap: wrap;
  }

  .search-box {
    flex: 1 1 100%;
  }

  select {
    flex: 1;
  }

}


/* =====================================================
   RESPONSIVE - MOBILE
===================================================== */

@media (max-width: 900px) {

  .books-page {
    padding: 20px;
  }


  /* HEADER */

  .page-header {
    flex-direction: column;

    align-items: flex-start;

    gap: 15px;
  }

  .page-header h1 {
    font-size: 24px;
  }

  .add-btn {
    width: 100%;
  }


  /* FILTER */

  .filter-box {
    flex-direction: column;

    padding: 15px;
  }

  .search-box {
    width: 100%;

    min-width: 0;
  }

  select {
    width: 100%;
  }

  .reset-btn {
    width: 100%;
  }


  /* TABLE */

  .table-container {
    max-height: 500px;

    overflow-y: auto;
    overflow-x: auto;
  }

  table {
    min-width: 1100px;
  }

}


/* =====================================================
   RESPONSIVE - SMALL MOBILE
===================================================== */

@media (max-width: 600px) {

  .books-page {
    padding: 15px;
  }


  .page-header h1 {
    font-size: 22px;
  }

  .page-header p {
    font-size: 13px;
  }


  .filter-box {
    padding: 12px;
  }


  .table-container {
    max-height: 450px;

    border-radius: 8px;
  }


  th {
    padding: 12px;

    font-size: 11px;
  }


  td {
    padding: 12px;

    font-size: 13px;
  }


  .book-info {
    min-width: 250px;
  }


  .book-text strong {
    max-width: 230px;
  }


  .book-text small {
    max-width: 230px;
  }


  .pagination {
    flex-direction: column;

    align-items: flex-start;

    gap: 15px;
  }

}


/* =====================================================
   ACCESSIBILITY
===================================================== */

button:focus-visible,
input:focus-visible,
select:focus-visible {
  outline: 2px solid #5b3df5;

  outline-offset: 2px;
}


/* =====================================================
   SMOOTH SCROLL
===================================================== */

.table-container {
  scroll-behavior: smooth;
}


/* =====================================================
   VERTICAL SCROLLBAR - EDGE/CHROME
===================================================== */

.table-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.table-container::-webkit-scrollbar-track {
  background: #f5f6f8;

  border-radius: 10px;
}

.table-container::-webkit-scrollbar-thumb {
  background: #c7cad4;

  border-radius: 10px;
}

.table-container::-webkit-scrollbar-thumb:hover {
  background: #aeb2be;
}


/* =====================================================
   FIREFOX SCROLLBAR
===================================================== */

.table-container {
  scrollbar-width: thin;

  scrollbar-color:
    #c7cad4
    #f5f6f8;
}

</style>