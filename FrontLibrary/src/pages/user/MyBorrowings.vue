<script setup>

import {
  ref,
  onMounted
} from "vue";

import BorrowCard
  from "../../components/user/BorrowCard.vue";

import {
  getMyBorrowings
} from "../../service/borrowingService.js";


// =====================================================
// DATA
// =====================================================

const borrowings = ref([]);

const loading = ref(false);

const error = ref("");


// =====================================================
// EXTRACT DATA
// =====================================================

function extractBorrowings(response) {

  console.log(
    "MY BORROWINGS RAW RESPONSE:",
    response
  );


  // -----------------------------------------------
  // Case 1
  // response = []
  // -----------------------------------------------

  if (Array.isArray(response)) {

    return response;

  }


  // -----------------------------------------------
  // Case 2
  // response = {
  //   data: []
  // }
  // -----------------------------------------------

  if (Array.isArray(response?.data)) {

    return response.data;

  }


  // -----------------------------------------------
  // Case 3
  // response = {
  //   data: {
  //      data: []
  //   }
  // }
  // -----------------------------------------------

  if (
    Array.isArray(
      response?.data?.data
    )
  ) {

    return response.data.data;

  }


  // -----------------------------------------------
  // Case 4
  // response = {
  //   result: []
  // }
  // -----------------------------------------------

  if (
    Array.isArray(
      response?.result
    )
  ) {

    return response.result;

  }


  // -----------------------------------------------
  // Nothing
  // -----------------------------------------------

  return [];

}


// =====================================================
// FORMAT BORROWING
// =====================================================

function formatBorrowing(item) {

  return {

    id:
      item?.id,

    bookId:
      item?.bookId ??
      item?.book?.id ??
      null,


    title:
      item?.title ??
      item?.bookTitle ??
      item?.book?.title ??
      "Unknown Book",


    author:
      item?.author ??
      item?.bookAuthor ??
      item?.book?.author ??
      "Unknown Author",


    category:
      item?.category ??
      item?.categoryName ??
      item?.book?.category?.name ??
      item?.book?.category ??
      "",


    image:
      item?.image ??
      item?.bookImage ??
      item?.book?.image ??
      "",


    borrowedDate:
      item?.borrowedDate ??
      item?.borrowDate ??
      item?.borrowingDate ??
      "",


    dueDate:
      item?.dueDate ??
      "",


    status:
      item?.status ??
      "BORROWED"

  };

}


// =====================================================
// FETCH MY BORROWINGS
// =====================================================

async function fetchMyBorrowings() {

  loading.value = true;

  error.value = "";


  try {

    const response =
      await getMyBorrowings();


    console.log(
      "MY BORROWINGS RESPONSE:",
      response
    );


    const data =
      extractBorrowings(
        response
      );


    borrowings.value =
      data.map(
        formatBorrowing
      );


    console.log(
      "MY BORROWINGS:",
      borrowings.value
    );


  }
  catch (err) {

    console.error(
      "FETCH MY BORROWINGS ERROR:",
      err
    );


    error.value =
      err?.message ||
      "Failed to load your borrowings.";


    borrowings.value = [];

  }
  finally {

    loading.value = false;

  }

}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDate(date) {

  if (!date) {

    return "-";

  }


  return date;

}


// =====================================================
// LOAD
// =====================================================

onMounted(() => {

  fetchMyBorrowings();

});

</script>


<template>

  <div
    class="min-h-screen bg-[#f8faff] p-6"
  >

    <!-- =================================================
         HEADER
    ================================================== -->

    <div class="mb-6">

      <h1
        class="text-2xl font-bold text-gray-800"
      >
        My Borrowings
      </h1>


      <p
        class="mt-1 text-sm text-gray-500"
      >
        Books you have currently borrowed
      </p>

    </div>


    <!-- =================================================
         LOADING
    ================================================== -->

    <div
      v-if="loading"
      class="rounded-xl bg-white p-10 text-center shadow-sm"
    >

      <i
        class="bi bi-arrow-repeat animate-spin text-3xl text-blue-600"
      ></i>


      <p
        class="mt-3 text-sm text-gray-500"
      >
        Loading your borrowings...
      </p>

    </div>


    <!-- =================================================
         ERROR
    ================================================== -->

    <div
      v-else-if="error"
      class="rounded-xl border border-red-200 bg-red-50 p-5 text-center"
    >

      <i
        class="bi bi-exclamation-circle text-2xl text-red-500"
      ></i>


      <p
        class="mt-2 text-sm text-red-600"
      >
        {{ error }}
      </p>


      <button
        type="button"
        @click="fetchMyBorrowings"
        class="mt-4 rounded-lg bg-red-500 px-4 py-2 text-sm font-medium text-white hover:bg-red-600"
      >
        Try Again
      </button>

    </div>


    <!-- =================================================
         BORROWINGS
    ================================================== -->

    <div
      v-else-if="borrowings.length > 0"
      class="grid gap-4"
    >

      <BorrowCard
        v-for="borrowing in borrowings"
        :key="borrowing.id"
        :book="borrowing"
      />

    </div>


    <!-- =================================================
         EMPTY
    ================================================== -->

    <div
      v-else
      class="rounded-xl bg-white p-10 text-center shadow-sm"
    >

      <i
        class="bi bi-book text-5xl text-gray-300"
      ></i>


      <h2
        class="mt-4 text-lg font-semibold text-gray-700"
      >
        No Borrowings
      </h2>


      <p
        class="mt-1 text-sm text-gray-400"
      >
        You haven't borrowed any books yet.
      </p>


      <router-link
        to="/user/browse-books"
        class="mt-5 inline-block rounded-lg bg-blue-600 px-5 py-2 text-sm font-medium text-white transition hover:bg-blue-700"
      >
        Browse Books
      </router-link>

    </div>

  </div>

</template>