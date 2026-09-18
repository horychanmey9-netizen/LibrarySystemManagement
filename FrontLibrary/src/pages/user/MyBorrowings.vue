
<script setup>

import {
  ref,
  computed,
  onMounted
} from "vue";

import BorrowCard
  from "../../components/user/BorrowCard.vue";

import {
  getMyBorrowings,
  returnBook
} from "../../service/borrowingService.js";


// =====================================================
// STATE
// =====================================================

const borrowings = ref([]);

const loading = ref(false);

const error = ref("");

const activeMenu = ref("ALL");


// =====================================================
// SHOW MORE
// =====================================================

const displayLimit = ref(16);

const loadAmount = 16;


// =====================================================
// MENU
// =====================================================

const menus = [

  {
    label: "All",
    value: "ALL"
  },

  {
    label: "Returned",
    value: "RETURNED"
  },

  {
    label: "Pending",
    value: "PENDING"
  },

  {
    label: "Borrowed",
    value: "BORROWED"
  },

  {
    label: "Not Accept",
    value: "REJECTED"
  },

  {
    label: "Overdue",
    value: "OVERDUE"
  }

];


// =====================================================
// EXTRACT BORROWINGS
// =====================================================

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


// =====================================================
// FORMAT BORROWING
// =====================================================

function formatBorrowing(item) {

  console.log(
    "BORROWING ITEM:",
    item
  );


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
      "",

    // =================================================
    // IMAGE
    // =================================================

    image:
      item?.image ??
      item?.bookImage ??
      item?.book?.image ??
      item?.book?.coverImage ??
      item?.coverImage ??
      "",

    borrowedDate:
      item?.borrowedDate ??
      item?.borrowDate ??
      item?.borrowingDate ??
      "",

    dueDate:
      item?.dueDate ??
      "",

    returnedDate:
      item?.returnedDate ??
      item?.returnDate ??
      "",

    status:
      item?.status ??
      "BORROWED"

  };

}


// =====================================================
// GET BORROWING DATE
// =====================================================

function getBorrowingTime(borrowing) {

  const date =
    borrowing?.borrowedDate ||
    borrowing?.borrowDate ||
    borrowing?.borrowingDate;

  if (!date) {

    return 0;

  }


  const time =
    new Date(date).getTime();

  return Number.isNaN(time)
    ? 0
    : time;

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
      extractBorrowings(response);


    const formattedData =
      data.map(formatBorrowing);


    // =================================================
    // NEWEST FIRST
    // =================================================

    formattedData.sort(
      (a, b) =>
        getBorrowingTime(b) -
        getBorrowingTime(a)
    );


    borrowings.value =
      formattedData;


    // Reset Show More
    displayLimit.value = 16;


    console.log(
      "MY BORROWINGS NEWEST FIRST:",
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
// FILTER BY MENU
// =====================================================

const filteredBorrowings = computed(() => {

  let result = [];


  // ===================================================
  // ALL
  // ===================================================

  if (activeMenu.value === "ALL") {

    result = [
      ...borrowings.value
    ];

  }

  else {

    result =
      borrowings.value.filter(
        borrowing =>
          borrowing.status?.toUpperCase() ===
          activeMenu.value
      );

  }


  // ===================================================
  // NEWEST FIRST
  // ===================================================

  result.sort(
    (a, b) =>
      getBorrowingTime(b) -
      getBorrowingTime(a)
  );


  return result;

});


// =====================================================
// VISIBLE BORROWINGS
// =====================================================

const visibleBorrowings = computed(() => {

  return filteredBorrowings.value.slice(
    0,
    displayLimit.value
  );

});


// =====================================================
// SHOW MORE AVAILABLE
// =====================================================

const hasMoreBorrowings = computed(() => {

  return (
    displayLimit.value <
    filteredBorrowings.value.length
  );

});


// =====================================================
// SHOW MORE
// =====================================================

function showMore() {

  displayLimit.value += loadAmount;

}


// =====================================================
// CHANGE MENU
// =====================================================

function changeMenu(value) {

  activeMenu.value = value;


  // Reset to first 16
  displayLimit.value = 16;

}


// =====================================================
// RETURN A BOOK
// =====================================================

async function handleReturn(book) {

  try {

    await returnBook(book.id);


    // =================================================
    // BACKEND SHOULD CHANGE STATUS
    // TO RETURN_REQUESTED
    // =================================================

    await fetchMyBorrowings();

  }

  catch (err) {

    console.error(
      "Return request error:",
      err
    );


    error.value =
      err?.message ||
      "Failed to submit return request.";

  }

}


// =====================================================
// STATISTICS
// =====================================================

const activeBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status?.toUpperCase() ===
      "BORROWED"
  ).length;

});


const returnedBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status?.toUpperCase() ===
      "RETURNED"
  ).length;

});


const overdueBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status?.toUpperCase() ===
      "OVERDUE"
  ).length;

});


// =====================================================
// FORMAT DATE
// =====================================================

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


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass(status) {

  const value =
    status?.toUpperCase();


  if (value === "RETURNED") {

    return "returned";

  }


  if (value === "OVERDUE") {

    return "overdue";

  }


  if (value === "PENDING") {

    return "pending";

  }


  if (value === "REJECTED") {

    return "rejected";

  }


  if (value === "RETURN_REQUESTED") {

    return "return-requested";

  }


  return "borrowed";

}


// =====================================================
// LOAD DATA
// =====================================================

onMounted(() => {

  fetchMyBorrowings();

});

</script>


<template>

  <div class="my-borrowings-page">


    <!-- =================================================
         HEADER
    ================================================== -->

    <div class="page-header">

      <div class="header-left">

        <div class="header-icon">

          <i class="bi bi-journal-bookmark-fill"></i>

        </div>

        <div>

          <h1>

            My Borrowings

          </h1>

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



    <!-- =================================================
         STATISTICS
    ================================================== -->

    <div class="statistics-grid">


      <!-- ACTIVE -->

      <div class="stat-card">

        <div>

          <p>

            Active Borrowings

          </p>

          <h2>

            {{ activeBorrowings }}

          </h2>

        </div>

        <div class="stat-icon active-icon">

          <i class="bi bi-book-fill"></i>

        </div>

      </div>



      <!-- RETURNED -->

      <div class="stat-card">

        <div>

          <p>

            Returned Books

          </p>

          <h2>

            {{ returnedBorrowings }}

          </h2>

        </div>

        <div class="stat-icon returned-icon">

          <i class="bi bi-check-circle-fill"></i>

        </div>

      </div>



      <!-- OVERDUE -->

      <div class="stat-card">

        <div>

          <p>

            Overdue Books

          </p>

          <h2>

            {{ overdueBorrowings }}

          </h2>

        </div>

        <div class="stat-icon overdue-icon">

          <i class="bi bi-exclamation-circle-fill"></i>

        </div>

      </div>


    </div>



    <!-- =================================================
         MENU FILTER
    ================================================== -->

    <div class="menu-card">

      <div class="menu-header">

        <h2>

          Borrowing History

        </h2>

        <p>

          Filter your borrowing records

        </p>

      </div>


      <div class="menu-list">

        <button
          v-for="menu in menus"
          :key="menu.value"
          type="button"
          class="menu-btn"
          :class="{
            active:
              activeMenu === menu.value
          }"
          @click="changeMenu(menu.value)"
        >

          <span>

            {{ menu.label }}

          </span>

        </button>

      </div>

    </div>



    <!-- =================================================
         LOADING
    ================================================== -->

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



    <!-- =================================================
         ERROR
    ================================================== -->

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



    <!-- =================================================
         BORROWINGS
    ================================================== -->

    <div
      v-else-if="
        filteredBorrowings.length > 0
      "
      class="borrowings-section"
    >


      <!-- =================================================
           SECTION HEADER
      ================================================== -->

      <div class="section-header">

        <div>

          <h2>

            {{
              activeMenu === "ALL"
                ? "All Borrowings"
                : menus.find(
                    menu =>
                      menu.value ===
                      activeMenu
                  )?.label
            }}

          </h2>

          <p>

            Showing

            {{ visibleBorrowings.length }}

            of

            {{ filteredBorrowings.length }}

            book{{ filteredBorrowings.length > 1 ? "s" : "" }}

          </p>

        </div>

      </div>



      <!-- =================================================
           BORROWING LIST
           NEWEST FIRST
      ================================================== -->

      <div class="borrowings-list">

        <div
          v-for="borrowing in visibleBorrowings"
          :key="borrowing.id"
          class="borrowing-wrapper"
        >

          <BorrowCard
            :book="borrowing"
            :hide-return-button="
              borrowing.status?.toUpperCase() === 'REJECTED' ||
              borrowing.status?.toUpperCase() !== 'BORROWED'
            "
            @return="handleReturn"
          />

        </div>

      </div>



      <!-- =================================================
           SHOW MORE
      ================================================== -->

      <div
        v-if="hasMoreBorrowings"
        class="show-more-container"
      >

        <button
          type="button"
          class="show-more-btn"
          @click="showMore"
        >

          <span>

            Show More

          </span>

          <i class="bi bi-chevron-down"></i>

        </button>

        <p>

          Showing
          {{ visibleBorrowings.length }}
          of
          {{ filteredBorrowings.length }}
          borrowings

        </p>

      </div>



      <!-- =================================================
           ALL LOADED
      ================================================== -->

      <div
        v-else-if="
          filteredBorrowings.length > 16
        "
        class="all-loaded"
      >

        <i class="bi bi-check-circle"></i>

        <span>

          All borrowings loaded

        </span>

      </div>


    </div>



    <!-- =================================================
         EMPTY
    ================================================== -->

    <div
      v-else
      class="state-container empty-state"
    >

      <div class="state-content">

        <div class="empty-icon">

          <i class="bi bi-journal-x"></i>

        </div>

        <h2>

          No
          {{
            activeMenu === "ALL"
              ? "Borrowings"
              : menus.find(
                  menu =>
                    menu.value ===
                    activeMenu
                )?.label
          }}

        </h2>

        <p>

          No books found in this category.

        </p>

        <router-link
          v-if="activeMenu === 'ALL'"
          to="/user/browse-books"
          class="browse-btn"
        >

          <i class="bi bi-search"></i>

          Browse Books

        </router-link>

        <button
          v-else
          type="button"
          class="browse-btn"
          @click="changeMenu('ALL')"
        >

          <i class="bi bi-arrow-left"></i>

          View All Borrowings

        </button>

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
  padding: 15px 80px;
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

  box-shadow:
    0 5px 15px
    rgba(37, 99, 235, 0.2);

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
   MENU FILTER
===================================================== */

.menu-card {

  width: 100%;

  margin-bottom: 30px;

  padding: 20px;

  background: white;

  border-radius: 16px;

  border: 1px solid #edf0f5;

  box-shadow:
    0 3px 10px
    rgba(0, 0, 0, 0.03);

}


.menu-header {

  margin-bottom: 18px;

}


.menu-header h2 {

  margin: 0;

  font-size: 20px;

  font-weight: 700;

  color: #1f2937;

}


.menu-header p {

  margin: 5px 0 0;

  font-size: 14px;

  color: #9ca3af;

}


.menu-list {

  display: flex;

  align-items: center;

  gap: 10px;

  flex-wrap: wrap;

}


.menu-btn {

  display: inline-flex;

  align-items: center;

  justify-content: center;

  padding: 11px 20px;

  border: 1px solid #e5e7eb;

  border-radius: 10px;

  background: #f8fafc;

  color: #4b5563;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.25s ease;

}


.menu-btn:hover {

  border-color: #2563eb;

  color: #2563eb;

  background: #eff6ff;

}


.menu-btn.active {

  background: #2563eb;

  border-color: #2563eb;

  color: white;

  box-shadow:
    0 4px 12px
    rgba(37, 99, 235, 0.2);

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

  width: 100%;

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
   SHOW MORE
===================================================== */

.show-more-container {

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  margin-top: 30px;

  padding: 10px 0 20px;

}


.show-more-btn {

  display: inline-flex;

  align-items: center;

  justify-content: center;

  gap: 10px;

  min-width: 150px;

  padding: 12px 22px;

  border: 1px solid #2563eb;

  border-radius: 10px;

  background: white;

  color: #2563eb;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.25s ease;

}


.show-more-btn:hover {

  background: #2563eb;

  color: white;

  box-shadow:
    0 5px 15px
    rgba(37, 99, 235, 0.2);

  transform: translateY(-1px);

}


.show-more-btn i {

  font-size: 12px;

}


.show-more-container p {

  margin: 10px 0 0;

  font-size: 13px;

  color: #9ca3af;

}


.all-loaded {

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 8px;

  margin-top: 25px;

  padding: 15px;

  color: #9ca3af;

  font-size: 13px;

}


.all-loaded i {

  color: #16a34a;

  font-size: 16px;

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
   STATE ICONS
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


/* =====================================================
   ANIMATION
===================================================== */

.spinning {

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
   ERROR BUTTON
===================================================== */

.retry-btn {

  margin-top: 22px;

  border: none;

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

  border: none;

  background: #2563eb;

  color: white;

}


.browse-btn:hover {

  background: #1d4ed8;

  transform: translateY(-1px);

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


  .menu-list {

    display: grid;

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

  }


  .menu-btn {

    width: 100%;

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


  .menu-card {

    padding: 15px;

  }


  .menu-list {

    grid-template-columns: 1fr;

  }


  .show-more-btn {

    width: 100%;

    max-width: 250px;

  }

}

</style>

