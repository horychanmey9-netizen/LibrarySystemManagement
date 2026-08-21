<script setup>
import BorrowCard from '@/components/user/BorrowCard.vue'

const borrowings = [
  {
    id: 1,
    bookId: 1,
    title: 'The Great Gatsby',
    author: 'F. Scott Fitzgerald',
    category: 'Fiction',
    image:
      'https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg',
    borrowedDate: '2026-08-01',
    dueDate: '2026-08-15',
    status: 'Borrowed'
  },

  {
    id: 2,
    bookId: 2,
    title: 'Atomic Habits',
    author: 'James Clear',
    category: 'Self Help',
    image:
      'https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg',
    borrowedDate: '2026-08-05',
    dueDate: '2026-08-20',
    status: 'Borrowed'
  }
]
</script>


<template>
  <div class="min-h-screen bg-[#f8faff] p-6">

    <!-- Header -->
    <div class="mb-6">

      <h1 class="text-2xl font-bold text-gray-800">
        My Borrowings
      </h1>

      <p class="mt-1 text-sm text-gray-500">
        Books you have currently borrowed
      </p>

    </div>


    <!-- Borrowing List -->
    <div
      v-if="borrowings.length"
      class="grid gap-4"
    >

      <BorrowCard
        v-for="book in borrowings"
        :key="book.id"
        :book="book"
      />

    </div>


    <!-- Empty State -->
    <div
      v-else
      class="rounded-xl bg-white p-10 text-center shadow-sm"
    >

      <i
        class="bi bi-book text-5xl text-gray-300"
      ></i>

      <h2 class="mt-4 text-lg font-semibold text-gray-700">
        No Borrowings
      </h2>

      <p class="mt-1 text-sm text-gray-400">
        You haven't borrowed any books yet.
      </p>

      <router-link
        to="/browse-books"
        class="mt-5 inline-block rounded-lg bg-blue-600 px-5 py-2 text-sm font-medium text-white transition hover:bg-blue-700"
      >
        Browse Books
      </router-link>

    </div>

  </div>
</template>