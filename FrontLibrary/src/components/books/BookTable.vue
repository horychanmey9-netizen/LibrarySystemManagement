<template>
  <div class="books-page">

    <!-- ================= HEADER ================= -->
    <div class="page-header">

      <div>
        <h1>Book Management</h1>
        <p>Manage all library books</p>
      </div>

      <!-- Add Book -->
      <button
        class="add-btn"
        @click="openAddBook"
      >
        <i class="bi bi-plus-lg"></i>
        <span>Add Book</span>
      </button>

    </div>


    <!-- ================= SEARCH & FILTER ================= -->
    <div class="filter-box">

      <!-- Search -->
      <div class="search-box">

        <i class="bi bi-search search-icon"></i>

        <input
          v-model="search"
          type="text"
          placeholder="Search books by title, author or ISBN..."
        />

      </div>


      <!-- Category -->
      <select v-model="selectedCategory">

        <option value="">
          All Categories
        </option>

        <option
          v-for="category in categories"
          :key="category"
          :value="category"
        >
          {{ category }}
        </option>

      </select>


      <!-- Status -->
      <select v-model="selectedStatus">

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


      <!-- Reset -->
      <button
        class="reset-btn"
        @click="resetFilters"
      >
        <i class="bi bi-arrow-clockwise"></i>
        Reset
      </button>

    </div>


    <!-- ================= TABLE ================= -->
    <div class="table-container">

      <table>

        <!-- Table Header -->
        <thead>
          <tr>
            <th>#</th>
            <th>BOOK</th>
            <th>AUTHOR</th>
            <th>CATEGORY</th>
            <th>ISBN</th>
            <th>QUANTITY</th>
            <th>STATUS</th>
            <th>ACTION</th>
          </tr>
        </thead>


        <!-- Table Body -->
        <tbody>

          <tr
            v-for="(book, index) in filteredBooks"
            :key="book.id"
          >

            <!-- Number -->
            <td>
              {{ index + 1 }}
            </td>


            <!-- Book -->
            <td>

              <div class="book-info">

                <div class="book-cover">

                  <i class="bi bi-book"></i>

                </div>

                <div class="book-text">

                  <strong>
                    {{ book.title }}
                  </strong>

                  <small>
                    {{ book.description }}
                  </small>

                </div>

              </div>

            </td>


            <!-- Author -->
            <td>
              {{ book.author }}
            </td>


            <!-- Category -->
            <td>

              <span class="category">
                {{ book.category }}
              </span>

            </td>


            <!-- ISBN -->
            <td>
              {{ book.isbn || "-" }}
            </td>


            <!-- Quantity -->
            <td>
              {{ book.quantity }}
            </td>


            <!-- Status -->
            <td>

              <span
                class="status"
                :class="getStatusClass(book.status)"
              >
                {{ book.status }}
              </span>

            </td>


            <!-- Action -->
            <td>

              <!-- Edit -->
              <button
                class="edit-btn"
                @click="editBook(book)"
                title="Edit Book"
              >
                <i class="bi bi-pencil"></i>
              </button>


              <!-- Delete -->
              <button
                class="delete-btn"
                @click="deleteBook(book.id)"
                title="Delete Book"
              >
                <i class="bi bi-trash"></i>
              </button>

            </td>

          </tr>


          <!-- No Result -->
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

            </td>

          </tr>

        </tbody>

      </table>

    </div>


    <!-- ================= PAGINATION ================= -->
    <div class="pagination">

      <span>
        Showing {{ filteredBooks.length }}
        of {{ books.length }} books
      </span>


      <div class="pagination-buttons">

        <button
          title="Previous"
        >
          <i class="bi bi-chevron-left"></i>
        </button>


        <button class="active">
          1
        </button>


        <button>
          2
        </button>


        <button>
          3
        </button>


        <button
          title="Next"
        >
          <i class="bi bi-chevron-right"></i>
        </button>

      </div>

    </div>

  </div>
</template>


<script setup>

import {
  ref,
  computed
} from "vue";


// =====================================================
// EVENTS
// =====================================================

const emit = defineEmits([
  "add-book"
]);


// =====================================================
// SEARCH
// =====================================================

const search = ref("");


// =====================================================
// FILTER
// =====================================================

const selectedCategory = ref("");

const selectedStatus = ref("");


// =====================================================
// CATEGORIES
// =====================================================

const categories = [
  "Programming",
  "Database",
  "Networking",
  "Security",
  "Web Development",
  "Business",
  "Other"
];


// =====================================================
// BOOKS DATA
// =====================================================

const books = ref([

  {
    id: 1,
    title: "Clean Code",
    description:
      "A Handbook of Agile Software Craftsmanship",
    author: "Robert C. Martin",
    category: "Programming",
    isbn: "978-0132350884",
    quantity: 5,
    status: "Available"
  },

  {
    id: 2,
    title: "Java Programming",
    description:
      "Comprehensive Guide",
    author: "John Smith",
    category: "Programming",
    isbn: "978-0321573513",
    quantity: 3,
    status: "Borrowed"
  },

  {
    id: 3,
    title: "Database System Concepts",
    description:
      "Database Management",
    author: "Abraham Silberschatz",
    category: "Database",
    isbn: "978-0078022159",
    quantity: 4,
    status: "Available"
  },

  {
    id: 4,
    title: "Computer Networking",
    description:
      "A Top-Down Approach",
    author: "James F. Kurose",
    category: "Networking",
    isbn: "978-0133594140",
    quantity: 2,
    status: "Borrowed"
  },

  {
    id: 5,
    title: "Web Development",
    description:
      "Modern Web Development",
    author: "David Miller",
    category: "Web Development",
    isbn: "978-1492052203",
    quantity: 6,
    status: "Available"
  },

  {
    id: 6,
    title: "Cyber Security",
    description:
      "Introduction to Security",
    author: "William Stallings",
    category: "Security",
    isbn: "978-0134091305",
    quantity: 2,
    status: "Overdue"
  }

]);


// =====================================================
// FILTER BOOKS
// =====================================================

const filteredBooks = computed(() => {

  return books.value.filter((book) => {

    const searchText =
      search.value
        .toLowerCase()
        .trim();


    // Search
    const matchesSearch =

      book.title
        .toLowerCase()
        .includes(searchText)

      ||

      book.author
        .toLowerCase()
        .includes(searchText)

      ||

      (book.isbn || "")
        .toLowerCase()
        .includes(searchText);


    // Category
    const matchesCategory =

      selectedCategory.value === ""

      ||

      book.category ===
        selectedCategory.value;


    // Status
    const matchesStatus =

      selectedStatus.value === ""

      ||

      book.status ===
        selectedStatus.value;


    return (
      matchesSearch &&
      matchesCategory &&
      matchesStatus
    );

  });

});


// =====================================================
// RESET FILTER
// =====================================================

function resetFilters() {

  search.value = "";

  selectedCategory.value = "";

  selectedStatus.value = "";

}


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass(status) {

  if (status === "Available") {
    return "available";
  }

  if (status === "Borrowed") {
    return "borrowed";
  }

  if (status === "Overdue") {
    return "overdue";
  }

  return "";

}


// =====================================================
// ADD BOOK
// =====================================================

function openAddBook() {

  emit("add-book");

}


// =====================================================
// EDIT BOOK
// =====================================================

function editBook(book) {

  console.log(
    "Edit book:",
    book
  );

  // Later:
  // Open EditBook form
}


// =====================================================
// DELETE BOOK
// =====================================================

function deleteBook(id) {

  const confirmDelete =
    window.confirm(
      "Are you sure you want to delete this book?"
    );


  if (!confirmDelete) {
    return;
  }


  books.value =
    books.value.filter(
      (book) =>
        book.id !== id
    );

}

</script>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.books-page {

  width: 100%;

  min-height:
    calc(100vh - 70px);

  padding: 30px;

  box-sizing: border-box;

  background: #f8f9fc;

  font-family:
    Arial,
    sans-serif;

}


/* =====================================================
   HEADER
===================================================== */

.page-header {

  display: flex;

  justify-content:
    space-between;

  align-items:
    center;

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

  padding:
    12px
    15px
    12px
    40px;

  border:
    1px solid
    #dfe3eb;

  border-radius: 8px;

  outline: none;

  font-size: 14px;

}


.search-box input:focus {

  border-color: #5b3df5;

}


.search-icon {

  position: absolute;

  left: 14px;

  top: 50%;

  transform:
    translateY(-50%);

  font-size: 16px;

  color: #8a92a3;

}


/* =====================================================
   SELECT
===================================================== */

select {

  width: 180px;

  padding: 12px;

  border:
    1px solid
    #dfe3eb;

  border-radius: 8px;

  background: white;

  cursor: pointer;

  outline: none;

}


select:focus {

  border-color: #5b3df5;

}


/* =====================================================
   RESET
===================================================== */

.reset-btn {

  display: flex;

  align-items: center;

  gap: 7px;

  padding: 12px 18px;

  border:
    1px solid
    #dfe3eb;

  background: white;

  border-radius: 8px;

  cursor: pointer;

  color: #667085;

}


.reset-btn:hover {

  background: #f1f2f6;

}


.reset-btn i {

  font-size: 15px;

}


/* =====================================================
   TABLE
===================================================== */

.table-container {

  background: white;

  border-radius: 10px;

  overflow-x: auto;

  border:
    1px solid
    #e5e8ef;

}


table {

  width: 100%;

  min-width: 1000px;

  border-collapse:
    collapse;

}


th {

  text-align: left;

  padding: 15px;

  font-size: 12px;

  color: #7c8497;

  background: #fafbfc;

  white-space: nowrap;

}


td {

  padding: 15px;

  border-top:
    1px solid
    #edf0f5;

  color: #333b4f;

  font-size: 14px;

  white-space: nowrap;

}


/* =====================================================
   BOOK INFO
===================================================== */

.book-info {

  display: flex;

  align-items: center;

  gap: 12px;

}


.book-cover {

  width: 38px;

  height: 50px;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #f0efff;

  border-radius: 5px;

  flex-shrink: 0;

}


.book-cover i {

  color: #5b3df5;

  font-size: 20px;

}


.book-text strong {

  display: block;

}


.book-text small {

  color: #8a92a3;

  display: block;

  margin-top: 4px;

  max-width: 280px;

  overflow: hidden;

  text-overflow: ellipsis;

}


/* =====================================================
   CATEGORY
===================================================== */

.category {

  padding: 5px 9px;

  background: #eeeeff;

  color: #5941e8;

  border-radius: 5px;

  font-size: 12px;

}


/* =====================================================
   STATUS
===================================================== */

.status {

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


/* =====================================================
   ACTIONS
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


/* =====================================================
   NO RESULT
===================================================== */

.no-result {

  text-align: center;

  padding: 40px;

  color: #888;

}


.no-result-icon {

  display: block;

  margin-bottom: 8px;

  font-size: 30px;

  color: #9ca3af;

}


.no-result p {

  margin: 0;

}


/* =====================================================
   PAGINATION
===================================================== */

.pagination {

  display: flex;

  justify-content:
    space-between;

  align-items:
    center;

  padding: 18px 5px;

  color: #777;

  font-size: 14px;

}


.pagination-buttons {

  display: flex;

  align-items: center;

}


.pagination button {

  width: 35px;

  height: 35px;

  margin-left: 5px;

  border:
    1px solid
    #ddd;

  background: white;

  border-radius: 6px;

  cursor: pointer;

}


.pagination button:hover {

  background: #f3f4f6;

}


.pagination button.active {

  background: #5b3df5;

  color: white;

  border-color:
    #5b3df5;

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 900px) {

  .filter-box {

    flex-direction: column;

  }


  select {

    width: 100%;

  }


  .page-header {

    flex-direction: column;

    align-items: flex-start;

    gap: 15px;

  }


  .add-btn {

    width: 100%;

    justify-content:
      center;

  }

}


@media (max-width: 768px) {

  .books-page {

    padding: 20px;

  }


  .pagination {

    flex-direction: column;

    align-items: flex-start;

    gap: 15px;

  }

}

</style>