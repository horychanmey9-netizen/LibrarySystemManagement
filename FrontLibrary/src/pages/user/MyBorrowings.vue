
<script setup>
import { ref, computed, onMounted } from "vue";

import BorrowCard from "../../components/user/BorrowCard.vue";

import {
  getMyBorrowings
} from "../../service/borrowingService.js";

const borrowings = ref([]);
const loading = ref(false);
const error = ref("");


function extractBorrowings(response) {

  if (Array.isArray(response)) {
    return response;
  }

  if (Array.isArray(response?.data)) {
    return response.data;
  }

  if (Array.isArray(response?.data?.data)) {
    return response.data.data;
  }

  if (Array.isArray(response?.result)) {
    return response.result;
  }

  return [];
}


function formatBorrowing(item) {

  return {
    id: item?.id,

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
      extractBorrowings(response);

    borrowings.value =
      data.map(formatBorrowing);

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


const activeBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status === "BORROWED"
  ).length;

});


const returnedBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status === "RETURNED"
  ).length;

});


const overdueBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status === "OVERDUE"
  ).length;

});


function formatDate(date) {

  if (!date) {
    return "-";
  }

  try {

    return new Date(date).toLocaleDateString(
      "en-US",
      {
        year: "numeric",
        month: "short",
        day: "numeric"
      }
    );

  }
  catch {

    return date;

  }

}


function getStatusClass(status) {

  const value =
    status?.toUpperCase();

  if (value === "RETURNED") {
    return "returned";
  }

  if (value === "OVERDUE") {
    return "overdue";
  }

  return "borrowed";

}


onMounted(() => {

  fetchMyBorrowings();

});
</script>


<template>

  <div class="my-borrowings-page">

    <!-- HEADER -->

    <div class="page-header">

      <div class="header-left">

        <div class="header-icon">
          <i class="bi bi-journal-bookmark-fill"></i>
        </div>

        <div>
          <h1>My Borrowings</h1>

          <p>
            Manage and track all your borrowed books
          </p>
        </div>

      </div>


      <button
        type="button"
        class="refresh-btn"
        :disabled="loading"
        @click="fetchMyBorrowings"
      >
        <i
          class="bi bi-arrow-clockwise"
          :class="{ spinning: loading }"
        ></i>

        Refresh
      </button>

    </div>


    <!-- STATISTICS -->

    <div class="statistics-grid">

      <div class="stat-card">

        <div>

          <p>Active Borrowings</p>

          <h2>
            {{ activeBorrowings }}
          </h2>

        </div>

        <div class="stat-icon active-icon">
          <i class="bi bi-book-fill"></i>
        </div>

      </div>


      <div class="stat-card">

        <div>

          <p>Returned Books</p>

          <h2>
            {{ returnedBorrowings }}
          </h2>

        </div>

        <div class="stat-icon returned-icon">
          <i class="bi bi-check-circle-fill"></i>
        </div>

      </div>


      <div class="stat-card">

        <div>

          <p>Overdue Books</p>

          <h2>
            {{ overdueBorrowings }}
          </h2>

        </div>

        <div class="stat-icon overdue-icon">
          <i class="bi bi-exclamation-circle-fill"></i>
        </div>

      </div>

    </div>


    <!-- LOADING -->

    <div
      v-if="loading"
      class="state-container"
    >

      <div class="state-content">

        <div class="loading-icon">
          <i class="bi bi-arrow-repeat spinning"></i>
        </div>

        <h3>
          Loading Borrowings
        </h3>

        <p>
          Please wait while we load your borrowed books...
        </p>

      </div>

    </div>


    <!-- ERROR -->

    <div
      v-else-if="error"
      class="state-container error-state"
    >

      <div class="state-content">

        <div class="error-icon">
          <i class="bi bi-exclamation-triangle-fill"></i>
        </div>

        <h3>
          Unable to Load Borrowings
        </h3>

        <p>
          {{ error }}
        </p>

        <button
          type="button"
          class="retry-btn"
          @click="fetchMyBorrowings"
        >

          <i class="bi bi-arrow-clockwise"></i>

          Try Again

        </button>

      </div>

    </div>


    <!-- BORROWINGS -->

    <div
      v-else-if="borrowings.length > 0"
      class="borrowings-section"
    >

      <div class="section-header">

        <div>

          <h2>
            Borrowed Books
          </h2>

          <p>
            {{ borrowings.length }}
            book{{ borrowings.length > 1 ? "s" : "" }}
            found
          </p>

        </div>

      </div>


      <div class="borrowings-list">
  <div
    v-for="borrowing in borrowings"
    :key="borrowing.id"
    class="borrowing-wrapper"
  >
    <div class="book-detail-center">
      <BorrowCard
        :book="borrowing"
      />
    </div>

    <div class="borrowing-footer">
      ...
    </div>
  </div>
</div>

    </div>


    <!-- EMPTY -->

    <div
      v-else
      class="state-container empty-state"
    >

      <div class="state-content">

        <div class="empty-icon">
          <i class="bi bi-journal-x"></i>
        </div>

        <h2>
          No Borrowings Yet
        </h2>

        <p>
          You haven't borrowed any books yet.
          Explore our library and find your next favorite book.
        </p>

        <router-link
          to="/user/browse-books"
          class="browse-btn"
        >

          <i class="bi bi-search"></i>

          Browse Books

        </router-link>

      </div>

    </div>

  </div>

</template>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.my-borrowings-page {
  min-height: 100vh;
  padding:
    1.5rem
    2rem;
  background: #f8faff;
}


/* =====================================================
   HEADER
===================================================== */

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
  margin-bottom: 30px;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 15px;
}

.header-icon {
  width: 52px;
  height: 52px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 14px;

  background: #2563eb;
  color: white;

  font-size: 22px;

  box-shadow: 0 5px 15px rgba(37, 99, 235, 0.2);
}

.page-header h1 {
  margin: 0;

  font-size: 28px;
  font-weight: 700;

  color: #1f2937;
}

.page-header p {
  margin: 5px 0 0;

  font-size: 14px;

  color: #6b7280;
}

.borrowing-wrapper {
  width: 100%;
  overflow: hidden;
  background: white;
  border-radius: 16px;
  border: 1px solid #edf0f5;
  box-shadow: 0 3px 10px rgba(0, 0, 0, 0.03);
  transition: 0.25s ease;
}

.book-detail-center {
  width: 100%;
  max-width: 650px;
  margin: 0 auto;
  padding: 18px 20px;
}

.book-detail-center :deep(.borrow-card) {
  width: 100%;
  max-width: 650px;
  margin: 0 auto;
}


/* =====================================================
   BUTTON
===================================================== */

.refresh-btn,
.retry-btn,
.browse-btn {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 8px;

  border: none;
  border-radius: 10px;

  padding: 11px 18px;

  font-size: 14px;
  font-weight: 600;

  cursor: pointer;

  transition: 0.25s ease;
}

.refresh-btn {
  background: white;

  border: 1px solid #e5e7eb;

  color: #4b5563;
}

.refresh-btn:hover {
  color: #2563eb;

  background: #f8fafc;
}

.refresh-btn:disabled {
  opacity: 0.6;

  cursor: not-allowed;
}


/* =====================================================
   STATISTICS
===================================================== */

.statistics-grid {
  display: grid;

  grid-template-columns:
    repeat(3, minmax(0, 1fr));

  gap: 20px;

  margin-bottom: 30px;
}

.stat-card {
  display: flex;

  align-items: center;
  justify-content: space-between;

  padding: 22px;

  background: white;

  border-radius: 16px;

  border: 1px solid #eef0f4;

  box-shadow:
    0 3px 10px
    rgba(0, 0, 0, 0.03);

  transition: 0.25s ease;
}

.stat-card:hover {
  transform: translateY(-3px);

  box-shadow:
    0 10px 25px
    rgba(0, 0, 0, 0.08);
}

.stat-card p {
  margin: 0;

  font-size: 14px;

  color: #6b7280;
}

.stat-card h2 {
  margin: 8px 0 0;

  font-size: 30px;

  color: #1f2937;
}

.stat-icon {
  width: 52px;
  height: 52px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 14px;

  font-size: 21px;
}

.active-icon {
  background: #eff6ff;
  color: #2563eb;
}

.returned-icon {
  background: #ecfdf5;
  color: #16a34a;
}

.overdue-icon {
  background: #fef2f2;
  color: #ef4444;
}


/* =====================================================
   BORROWINGS
===================================================== */

.borrowings-section {
  width: 100%;
}

.section-header {
  margin-bottom: 18px;
}

.section-header h2 {
  margin: 0;

  font-size: 20px;

  color: #1f2937;
}

.section-header p {
  margin: 5px 0 0;

  font-size: 14px;

  color: #9ca3af;
}

.borrowings-list {
  display: grid;

  gap: 20px;
}

.borrowing-wrapper {
  overflow: hidden;

  background: white;

  border-radius: 16px;

  border: 1px solid #edf0f5;

  box-shadow:
    0 3px 10px
    rgba(0, 0, 0, 0.03);

  transition: 0.25s ease;
}

.borrowing-wrapper:hover {
  box-shadow:
    0 10px 30px
    rgba(0, 0, 0, 0.07);
}


/* =====================================================
   BORROWING FOOTER
===================================================== */

.borrowing-footer {
  display: grid;

  grid-template-columns:
    1fr 1fr auto;

  gap: 25px;

  align-items: center;

  padding: 18px 22px;

  border-top: 1px solid #f0f1f3;

  background: #fafbfc;
}

.date-info {
  display: flex;

  align-items: center;

  gap: 12px;
}

.date-icon {
  width: 40px;
  height: 40px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 10px;

  font-size: 17px;
}

.borrow-date-icon {
  background: #eff6ff;
  color: #2563eb;
}

.due-date-icon {
  background: #fff7ed;
  color: #f97316;
}

.date-info span {
  display: block;

  font-size: 12px;

  color: #9ca3af;
}

.date-info strong {
  display: block;

  margin-top: 3px;

  font-size: 14px;

  color: #374151;
}

.status-container {
  display: flex;

  justify-content: flex-end;
}

.status-badge {
  display: inline-flex;

  align-items: center;

  gap: 7px;

  padding: 7px 13px;

  border-radius: 30px;

  font-size: 12px;
  font-weight: 600;

  border: 1px solid transparent;
}

.status-dot {
  width: 7px;
  height: 7px;

  border-radius: 50%;

  background: currentColor;
}

.borrowed {
  background: #eff6ff;

  border-color: #dbeafe;

  color: #2563eb;
}

.returned {
  background: #ecfdf5;

  border-color: #bbf7d0;

  color: #16a34a;
}

.overdue {
  background: #fef2f2;

  border-color: #fecaca;

  color: #ef4444;
}


/* =====================================================
   STATES
===================================================== */

.state-container {
  min-height: 400px;

  display: flex;

  align-items: center;
  justify-content: center;

  padding: 40px;

  background: white;

  border-radius: 16px;

  border: 1px solid #edf0f5;

  box-shadow:
    0 3px 10px
    rgba(0, 0, 0, 0.03);
}

.state-content {
  max-width: 500px;

  text-align: center;
}

.state-content h2,
.state-content h3 {
  margin: 20px 0 0;

  color: #374151;
}

.state-content p {
  margin: 10px 0 0;

  color: #9ca3af;

  font-size: 14px;

  line-height: 1.6;
}


/* =====================================================
   LOADING
===================================================== */

.loading-icon,
.error-icon,
.empty-icon {
  width: 80px;
  height: 80px;

  margin: auto;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  font-size: 34px;
}

.loading-icon {
  background: #eff6ff;

  color: #2563eb;
}

.error-icon {
  background: #fef2f2;

  color: #ef4444;
}

.empty-icon {
  width: 100px;
  height: 100px;

  background: #eff6ff;

  color: #93c5fd;

  font-size: 48px;
}

.spinning {
  animation: spin 1s linear infinite;
}


/* =====================================================
   ERROR BUTTON
===================================================== */

.retry-btn {
  margin-top: 22px;

  background: #ef4444;

  color: white;
}

.retry-btn:hover {
  background: #dc2626;
}


/* =====================================================
   BROWSE BUTTON
===================================================== */

.browse-btn {
  margin-top: 24px;

  text-decoration: none;

  background: #2563eb;

  color: white;
}

.browse-btn:hover {
  background: #1d4ed8;

  transform: translateY(-1px);
}


/* =====================================================
   ANIMATION
===================================================== */

@keyframes spin {

  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 992px) {

  .statistics-grid {
    grid-template-columns:
      repeat(2, minmax(0, 1fr));
  }

}


@media (max-width: 768px) {

  .my-borrowings-page {
    padding: 20px;
  }

  .page-header {
    align-items: flex-start;

    flex-direction: column;
  }

  .statistics-grid {
    grid-template-columns: 1fr;
  }

  .borrowing-footer {
    grid-template-columns: 1fr;

    gap: 15px;
  }

  .status-container {
    justify-content: flex-start;
  }

}


@media (max-width: 480px) {

  .my-borrowings-page {
    padding: 15px;
  }

  .page-header h1 {
    font-size: 23px;
  }

  .state-container {
    min-height: 350px;

    padding: 25px;
  }

}

</style>

