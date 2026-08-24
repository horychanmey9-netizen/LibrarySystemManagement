<template>
  <div class="books-page">

    <!-- =========================
         BOOK TABLE
    ========================== -->
    <BookTable
      v-if="!showAddBook"
      @add-book="openAddBook"
    />

    <!-- =========================
         ADD BOOK FORM
    ========================== -->
    <AddBook
      v-else
      @close="closeAddBook"
      @saved="handleBookSaved"
    />

  </div>
</template>


<script setup>

import { ref } from "vue";


// IMPORTANT:
// BookTable is inside:
// src/components/admin/books/BookTable.vue

import BookTable from "../../components/books/BookTable.vue";

// AddBook is inside:
// src/components/admin/AddBook.vue

import AddBook from "../../components/admin/AddBook.vue";


// =====================================================
// STATE
// =====================================================

const showAddBook = ref(false);


// =====================================================
// OPEN ADD BOOK
// =====================================================

function openAddBook() {
  showAddBook.value = true;
}


// =====================================================
// CLOSE ADD BOOK
// =====================================================

function closeAddBook() {
  showAddBook.value = false;
}


// =====================================================
// BOOK SAVED
// =====================================================

function handleBookSaved() {
  showAddBook.value = false;
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