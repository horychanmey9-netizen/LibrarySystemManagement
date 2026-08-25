<template>

  <div class="books-page">

    <!-- =====================================================
         LOADING
    ====================================================== -->

    <div
      v-if="loading"
      class="loading-container"
    >

      <i class="bi bi-arrow-repeat loading-icon"></i>

      <p>Loading books...</p>

    </div>


    <!-- =====================================================
         ERROR
    ====================================================== -->

    <div
      v-else-if="errorMessage"
      class="error-container"
    >

      <i class="bi bi-exclamation-circle"></i>

      <p>{{ errorMessage }}</p>

      <button
        type="button"
        @click="fetchBooks"
      >
        Try Again
      </button>

    </div>


    <!-- =====================================================
         BOOK TABLE
    ====================================================== -->

    <BookTable
      v-else-if="currentView === 'list'"
      :books="books"
      @add-book="openAddBook"
      @edit-book="openEditBook"
      @delete-book="deleteBook"
    />


    <!-- =====================================================
         ADD BOOK
    ====================================================== -->

    <AddBook
      v-else-if="currentView === 'add'"
      @close="closeAddBook"
      @saved="handleBookSaved"
    />


    <!-- =====================================================
         EDIT BOOK
    ====================================================== -->

    <EditBook
      v-else-if="currentView === 'edit'"
      :book="selectedBook"
      @close="closeEditBook"
      @updated="handleBookUpdated"
    />

  </div>

</template>


<script setup>

import {
  ref,
  onMounted
} from "vue";


// =====================================================
// COMPONENTS
// =====================================================

import BookTable
  from "../../components/books/BookTable.vue";

import AddBook
  from "../../components/admin/AddBook.vue";

import EditBook
  from "../../components/books/EditBook.vue";


// =====================================================
// SERVICE
// =====================================================

import {
  getBooks,
  createBook,
  updateBook,
  deleteBookById
} from "../../service/bookService";


// =====================================================
// CURRENT VIEW
// =====================================================

const currentView = ref("list");


// =====================================================
// BOOKS
// =====================================================

const books = ref([]);


// =====================================================
// SELECTED BOOK
// =====================================================

const selectedBook = ref(null);


// =====================================================
// LOADING
// =====================================================

const loading = ref(false);


// =====================================================
// ERROR
// =====================================================

const errorMessage = ref("");


// =====================================================
// FETCH BOOKS
// =====================================================

async function fetchBooks() {

  loading.value = true;

  errorMessage.value = "";

  try {

    const response = await getBooks();

    console.log(
      "BOOK API RESPONSE:",
      response
    );


    // ===================================================
    // BACKEND RESPONSE
    //
    // { 
    //   status: 200,
    //   data: [...]
    // }
    // ===================================================

    let bookData = [];


    if (
      response &&
      Array.isArray(response.data)
    ) {

      bookData = response.data;

    }

    // If backend directly returns array
    else if (
      Array.isArray(response)
    ) {

      bookData = response;

    }


    // ===================================================
    // NORMALIZE BOOK DATA
    // ===================================================

    books.value = bookData.map(book => ({

      id: book.id,

      title: book.title || "",

      description:
        book.description || "",

      author:
        book.author || "",

      isbn:
        book.isbn || "",

      // Backend uses qty
      quantity:
        book.qty ?? 0,

      // Keep original qty too
      qty:
        book.qty ?? 0,

      status:
        book.status || "",

      pages:
        book.pages ?? null,

      language:
        book.language || "",

      image:
        book.image || "",

      // Backend Category object
      category:
        book.category || null,

      // Useful for edit/filter
      categoryId:
        book.category?.id ?? null,

      categoryName:
        book.category?.name || ""

    }));


    console.log(
      "BOOKS:",
      books.value
    );

  } catch (error) {

    console.error(
      "Failed to fetch books:",
      error
    );

    errorMessage.value =
      error.response?.data?.msg ||
      "Failed to load books.";

  } finally {

    loading.value = false;

  }

}


// =====================================================
// OPEN ADD BOOK
// =====================================================

function openAddBook() {

  console.log(
    "Opening Add Book..."
  );

  currentView.value = "add";

}


// =====================================================
// CLOSE ADD BOOK
// =====================================================

function closeAddBook() {

  currentView.value = "list";

}


// =====================================================
// HANDLE BOOK SAVED
// =====================================================

async function handleBookSaved(newBook) {

  console.log(
    "New book received:",
    newBook
  );


  currentView.value = "list";


  // Fetch from backend again
  // so database becomes the source of truth.

  await fetchBooks();

}


// =====================================================
// OPEN EDIT BOOK
// =====================================================

function openEditBook(bookId) {

  console.log(
    "Edit book ID:",
    bookId
  );


  const book =
    books.value.find(
      item =>
        Number(item.id) ===
        Number(bookId)
    );


  if (!book) {

    window.alert(
      "Book not found."
    );

    return;

  }


  // Copy book
  selectedBook.value = {
    ...book
  };


  console.log(
    "Selected book:",
    selectedBook.value
  );


  currentView.value = "edit";

}


// =====================================================
// CLOSE EDIT BOOK
// =====================================================

function closeEditBook() {

  selectedBook.value = null;

  currentView.value = "list";

}


// =====================================================
// HANDLE BOOK UPDATED
// =====================================================

async function handleBookUpdated(updatedBook) {

  console.log(
    "Updated book:",
    updatedBook
  );


  selectedBook.value = null;

  currentView.value = "list";


  // Reload real data from backend

  await fetchBooks();

}


// =====================================================
// DELETE BOOK
// =====================================================

async function deleteBook(bookId) {

  const book = books.value.find(
    item => Number(item.id) === Number(bookId)
  );

  if (!book) {
    alert("Book not found.");
    return;
  }


  const confirmed = window.confirm(
    `Are you sure you want to delete "${book.title}"?`
  );

  if (!confirmed) {
    return;
  }


  try {

    await deleteBookById(bookId);


    // Remove from frontend after backend success
    books.value = books.value.filter(
      item => Number(item.id) !== Number(bookId)
    );


    alert("Book deleted successfully.");


  } catch (error) {

    console.error(
      "Delete book error:",
      error
    );

    alert(
      error?.message ||
      "Failed to delete book."
    );

  }

}


// =====================================================
// ON MOUNTED
// =====================================================

onMounted(() => {

  fetchBooks();

});

</script>


<style scoped>

.books-page {

  min-height:
    calc(100vh - 70px);

  padding: 30px;

  background:
    #f8fafc;

  box-sizing:
    border-box;

}


/* =====================================================
   LOADING
===================================================== */

.loading-container {

  min-height: 400px;

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  color: #667085;

}


.loading-icon {

  font-size: 32px;

  margin-bottom: 10px;

  animation:
    spin 1s linear infinite;

}


@keyframes spin {

  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }

}


/* =====================================================
   ERROR
===================================================== */

.error-container {

  min-height: 400px;

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  color: #d93636;

}


.error-container i {

  font-size: 35px;

  margin-bottom: 10px;

}


.error-container button {

  margin-top: 10px;

  padding: 10px 18px;

  border: none;

  border-radius: 7px;

  background: #5b3df5;

  color: white;

  cursor: pointer;

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 768px) {

  .books-page {

    padding: 20px;

  }

}

</style>