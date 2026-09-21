```vue
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
  { label: "All", value: "ALL" },
  { label: "Returned", value: "RETURNED" },
  { label: "Pending", value: "PENDING" },
  { label: "Borrowed", value: "BORROWED" },
  { label: "Not Accept", value: "REJECTED" },
  { label: "Overdue", value: "OVERDUE" }
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

  console.log("BORROWING ITEM:", item);

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
// FETCH
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

    formattedData.sort(
      (a, b) =>
        getBorrowingTime(b) -
        getBorrowingTime(a)
    );

    borrowings.value =
      formattedData;

    displayLimit.value = 16;

    console.log(
      "MY BORROWINGS NEWEST FIRST:",
      borrowings.value
    );

  } catch (err) {

    console.error(
      "FETCH MY BORROWINGS ERROR:",
      err
    );

    error.value =
      err?.message ||
      "Failed to load your borrowings.";

    borrowings.value = [];

  } finally {

    loading.value = false;
  }
}


// =====================================================
// FILTER
// =====================================================

const filteredBorrowings = computed(() => {

  let result = [];

  if (activeMenu.value === "ALL") {

    result = [
      ...borrowings.value
    ];

  } else {

    result =
      borrowings.value.filter(
        borrowing =>
          borrowing.status
            ?.toUpperCase() ===
          activeMenu.value
      );
  }

  result.sort(
    (a, b) =>
      getBorrowingTime(b) -
      getBorrowingTime(a)
  );

  return result;
});


// =====================================================
// VISIBLE
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

  displayLimit.value +=
    loadAmount;
}


// =====================================================
// CHANGE MENU
// =====================================================

function changeMenu(value) {

  activeMenu.value =
    value;

  displayLimit.value =
    16;
}


// =====================================================
// RETURN
// =====================================================

async function handleReturn(book) {

  try {

    await returnBook(book.id);

    await fetchMyBorrowings();

  } catch (err) {

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
      borrowing.status
        ?.toUpperCase() ===
      "BORROWED"
  ).length;

});


const returnedBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status
        ?.toUpperCase() ===
      "RETURNED"
  ).length;

});


const overdueBorrowings = computed(() => {

  return borrowings.value.filter(
    borrowing =>
      borrowing.status
        ?.toUpperCase() ===
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

    return new Date(date)
      .toLocaleDateString(
        "en-US",
        {
          year: "numeric",
          month: "short",
          day: "numeric"
        }
      );

  } catch {

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
// MOUNT
// =====================================================

onMounted(() => {

  fetchMyBorrowings();

});

</script>


<template>

  <div class="my-borrowings-page">

    <!-- =================================================
         PAGE HEADER
    ================================================== -->

    <div class="page-header">

      <div>

        <h1>
          My Borrowings
        </h1>

        <p class="hidden sm:flex">
          Manage your borrowed books
        </p>

      </div>

    </div>


    <!-- =================================================
         STATISTICS
    ================================================== -->

    <div class="statistics-grid">

      <!-- ACTIVE BORROWINGS -->

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

          <i class="bi bi-book"></i>

        </div>

      </div>


      <!-- RETURNED BOOKS -->

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

          <i class="bi bi-check-circle"></i>

        </div>

      </div>


      <!-- OVERDUE BOOKS -->

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

          <i class="bi bi-exclamation-circle"></i>

        </div>

      </div>

    </div>


    <!-- =================================================
         MENU
         KEEP ORIGINAL DESIGN
    ================================================== -->

    <div class="menu-card">

      <button
        v-for="menu in menus"
        :key="menu.value"
        class="menu-button"
        :class="{
          active:
            activeMenu === menu.value
        }"
        @click="
          changeMenu(menu.value)
        "
      >

        {{ menu.label }}

      </button>

    </div>


    <!-- =================================================
         LOADING
    ================================================== -->

    <div
      v-if="loading"
      class="loading-state"
    >

      Loading...

    </div>


    <!-- =================================================
         ERROR
    ================================================== -->

    <div
      v-else-if="error"
      class="error-state"
    >

      {{ error }}

    </div>


    <!-- =================================================
         EMPTY
    ================================================== -->

    <div
      v-else-if="
        visibleBorrowings.length === 0
      "
      class="empty-state"
    >

      <i class="bi bi-book"></i>

      <p>
        No borrowings found.
      </p>

    </div>


    <!-- =================================================
         BORROWINGS
    ================================================== -->

    <div
      v-else
      class="borrowings-list"
    >

      <BorrowCard
        v-for="borrowing in visibleBorrowings"
        :key="borrowing.id"
        :book="borrowing"
        @return="handleReturn"
      />

    </div>


    <!-- =================================================
         SHOW MORE
    ================================================== -->

    <div
      v-if="hasMoreBorrowings"
      class="show-more-wrapper"
    >

      <button
        class="show-more-button"
        @click="showMore"
      >

        Show More

        <i class="bi bi-chevron-down"></i>

      </button>

    </div>

  </div>

</template>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.my-borrowings-page {
  width: 100%;
  padding: 20px 90px;
}


/* =====================================================
   PAGE HEADER
===================================================== */

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: #1f2937;
}

.page-header p {
  margin: 6px 0 0;
  color: #6b7280;
  font-size: 14px;
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

  min-width: 0;

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

  transform:
    translateY(-3px);

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

  min-width: 52px;

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
   MENU
   ORIGINAL STYLE
===================================================== */

.menu-card {

  display: flex;

  gap: 8px;

  margin-bottom: 24px;

  padding: 6px;

  background: white;

  border: 1px solid #eef0f4;

  border-radius: 12px;

  overflow-x: auto;

  scrollbar-width: none;
}


.menu-card::-webkit-scrollbar {
  display: none;
}


.menu-button {

  border: none;

  background: transparent;

  padding: 9px 16px;

  border-radius: 8px;

  font-size: 13px;

  color: #6b7280;

  cursor: pointer;

  white-space: nowrap;

  transition: 0.2s ease;
}


.menu-button:hover {

  background: #f3f4f6;

  color: #374151;
}


.menu-button.active {

  background: #2563eb;

  color: white;
}


/* =====================================================
   BORROWINGS
===================================================== */

.borrowings-list {

  display: flex;

  flex-direction: column;

  gap: 16px;
}


/* =====================================================
   LOADING
===================================================== */

.loading-state {

  padding: 40px;

  text-align: center;

  color: #6b7280;
}


/* =====================================================
   ERROR
===================================================== */

.error-state {

  padding: 20px;

  text-align: center;

  color: #dc2626;

  background: #fef2f2;

  border-radius: 12px;
}


/* =====================================================
   EMPTY
===================================================== */

.empty-state {

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  padding: 60px 20px;

  color: #9ca3af;

  text-align: center;
}


.empty-state i {

  font-size: 42px;

  margin-bottom: 10px;
}


.empty-state p {

  margin: 0;

  font-size: 14px;
}


/* =====================================================
   SHOW MORE
===================================================== */

.show-more-wrapper {

  display: flex;

  justify-content: center;

  margin-top: 24px;
}


.show-more-button {

  display: flex;

  align-items: center;

  gap: 8px;

  border: none;

  background: #2563eb;

  color: white;

  padding: 10px 18px;

  border-radius: 10px;

  font-size: 13px;

  cursor: pointer;

  transition: 0.2s ease;
}


.show-more-button:hover {

  background: #1d4ed8;
}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 992px) {

  .my-borrowings-page {

    padding: 15px 40px;
  }


  .statistics-grid {

    grid-template-columns:
      repeat(3, minmax(0, 1fr));

    gap: 12px;
  }


  .stat-card {

    padding: 15px;
  }


  .stat-card p {

    font-size: 12px;
  }


  .stat-card h2 {

    font-size: 25px;
  }


  .stat-icon {

    width: 42px;

    height: 42px;

    min-width: 42px;

    border-radius: 11px;

    font-size: 17px;
  }


  /*
   * IMPORTANT:
   * Menu remains exactly the original style.
   */
}


/* =====================================================
   MOBILE
   ONLY STATISTICS IS CHANGED
   NO SCROLL
===================================================== */

@media (max-width: 768px) {

  .my-borrowings-page {

    padding: 12px;
  }


  .page-header {

    margin-bottom: 16px;
  }


  .page-header h1 {

    font-size: 22px;
  }


  .page-header p {

    font-size: 12px;
  }


  /* =================================================
     SMALL STATISTICS
     3 CARDS IN ONE ROW
     NO HORIZONTAL SCROLL
  ================================================== */

  .statistics-grid {

    display: grid;

    grid-template-columns:
      repeat(3, minmax(0, 1fr));

    gap: 7px;

    margin-bottom: 22px;

    overflow: visible;

    padding: 0;
  }


  .stat-card {

    min-width: 0;

    width: 100%;

    padding: 9px 7px;

    border-radius: 10px;

    box-shadow:
      0 2px 7px
      rgba(0, 0, 0, 0.04);
  }


  .stat-card:hover {

    transform: none;

    box-shadow:
      0 2px 7px
      rgba(0, 0, 0, 0.04);
  }


  .stat-card p {

    font-size: 8px;

    line-height: 1.2;

    white-space: nowrap;
  }


  .stat-card h2 {

    margin-top: 3px;

    font-size: 18px;

    line-height: 1;
  }


  .stat-icon {

    width: 28px;

    height: 28px;

    min-width: 28px;

    border-radius: 7px;

    font-size: 11px;
  }


  /* =================================================
     MENU
     KEEP OLD DESIGN
  ================================================== */

  .menu-card {

    display: flex;

    gap: 8px;

    margin-bottom: 24px;

    padding: 6px;

    background: white;

    border: 1px solid #eef0f4;

    border-radius: 12px;

    overflow-x: auto;

    scrollbar-width: none;
  }


  .menu-card::-webkit-scrollbar {
    display: none;
  }


  .menu-button {

    border: none;

    background: transparent;

    padding: 9px 16px;

    border-radius: 8px;

    font-size: 13px;

    color: #6b7280;

    cursor: pointer;

    white-space: nowrap;

    transition: 0.2s ease;
  }


  .menu-button:hover {

    background: #f3f4f6;

    color: #374151;
  }


  .menu-button.active {

    background: #2563eb;

    color: white;
  }


  /* =================================================
     BORROWINGS
  ================================================== */

  .borrowings-list {

    gap: 12px;
  }

}


/* =====================================================
   SMALL MOBILE
===================================================== */

@media (max-width: 480px) {

  .my-borrowings-page {

    padding: 10px;
  }


  /* =================================================
     STATISTICS
     EVEN SMALLER
     NO SCROLL
  ================================================== */

  .statistics-grid {

    grid-template-columns:
      repeat(3, minmax(0, 1fr));

    gap: 5px;

    margin-bottom: 22px;

    overflow: visible;
  }


  .stat-card {

    padding: 8px 5px;

    border-radius: 9px;
  }


  .stat-card p {

    font-size: 7px;

    letter-spacing: -0.1px;
  }


  .stat-card h2 {

    font-size: 16px;

    margin-top: 3px;
  }


  .stat-icon {

    width: 24px;

    height: 24px;

    min-width: 24px;

    border-radius: 6px;

    font-size: 10px;
  }


  /*
   * MENU IS STILL ORIGINAL SIZE
   */

  .menu-card {

    gap: 8px;

    margin-bottom: 24px;

    padding: 6px;

    overflow-x: auto;

    scrollbar-width: none;
  }


  .menu-card::-webkit-scrollbar {
    display: none;
  }


  .menu-button {

    padding: 9px 16px;

    font-size: 13px;
  }

}

</style>
```
