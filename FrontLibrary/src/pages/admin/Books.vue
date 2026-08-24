<template>
  <div class="books-page">

    <!-- =========================
         BOOK TABLE
    ========================== -->
    <BookTable
      v-if="currentView === 'list'"
      :books="books"
      @add-book="openAddBook"
      @edit-book="openEditBook"
      @delete-book="deleteBook"
    />

    <!-- =========================
         ADD BOOK
    ========================== -->
    <AddBook
      v-else-if="currentView === 'add'"
      @close="closeAddBook"
      @saved="handleBookSaved"
    />

    <!-- =========================
         EDIT BOOK
    ========================== -->
    <EditBook
      v-else-if="currentView === 'edit'"
      :book="selectedBook"
      @close="closeEditBook"
      @updated="handleBookUpdated"
    />

  </div>
</template>


<script setup>
import { ref } from "vue";

import BookTable from "../../components/books/BookTable.vue";
import AddBook from "../../components/admin/AddBook.vue";
import EditBook from "../../components/books/EditBook.vue";


// =====================================================
// CURRENT VIEW
// =====================================================

const currentView = ref("list");


// =====================================================
// BOOKS DATA
// =====================================================

const books = ref([
  {
    id: 1,
    title: "Clean Code",
    description: "A Handbook of Agile Software Craftsmanship",
    author: "Robert C. Martin",
    category: "Programming",
    isbn: "978-0132350884",
    quantity: 5,
    status: "Available"
  },

  {
    id: 2,
    title: "Java Programming",
    description: "Comprehensive Guide",
    author: "John Smith",
    category: "Programming",
    isbn: "978-0321573513",
    quantity: 3,
    status: "Borrowed"
  },

  {
    id: 3,
    title: "Database System Concepts",
    description: "Database Management",
    author: "Abraham Silberschatz",
    category: "Database",
    isbn: "978-0078022159",
    quantity: 4,
    status: "Available"
  },

  {
    id: 4,
    title: "Computer Networking",
    description: "A Top-Down Approach",
    author: "James F. Kurose",
    category: "Networking",
    isbn: "978-0133594140",
    quantity: 2,
    status: "Borrowed"
  },

  {
    id: 5,
    title: "Web Development",
    description: "Modern Web Development",
    author: "David Miller",
    category: "Web Development",
    isbn: "978-1492052203",
    quantity: 6,
    status: "Available"
  },

  {
    id: 6,
    title: "Cyber Security",
    description: "Introduction to Security",
    author: "William Stallings",
    category: "Security",
    isbn: "978-0134091305",
    quantity: 2,
    status: "Overdue"
  }
]);


// =====================================================
// SELECTED BOOK
// =====================================================

const selectedBook = ref(null);


// =====================================================
// ADD BOOK
// =====================================================

function openAddBook() {
  currentView.value = "add";
}


function closeAddBook() {
  currentView.value = "list";
}


function handleBookSaved(newBook) {
  if (newBook) {
    books.value.push({
      ...newBook,
      id: newBook.id || Date.now()
    });
  }

  currentView.value = "list";
}


// =====================================================
// EDIT BOOK
// =====================================================

function openEditBook(bookId) {
  console.log("Edit book ID:", bookId);

  const book = books.value.find(
    item => item.id === Number(bookId)
  );

  if (!book) {
    alert("Book not found.");
    return;
  }

  selectedBook.value = book;

  currentView.value = "edit";
}


function closeEditBook() {
  selectedBook.value = null;

  currentView.value = "list";
}


function handleBookUpdated(updatedBook) {
  if (!updatedBook) {
    currentView.value = "list";
    return;
  }

  const index = books.value.findIndex(
    item => item.id === updatedBook.id
  );

  if (index !== -1) {
    books.value[index] = {
      ...updatedBook
    };
  }

  selectedBook.value = null;

  currentView.value = "list";
}


// =====================================================
// DELETE BOOK
// =====================================================

function deleteBook(bookId) {
  const confirmDelete = window.confirm(
    "Are you sure you want to delete this book?"
  );

  if (!confirmDelete) {
    return;
  }

  books.value = books.value.filter(
    book => book.id !== Number(bookId)
  );
}
</script>


<style scoped>

.books-page {
  min-height: calc(100vh - 70px);

  padding: 30px;

  background: #f8fafc;

  box-sizing: border-box;
}

</style>