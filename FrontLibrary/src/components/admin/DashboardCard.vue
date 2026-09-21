```vue
<template>
  <div class="stats-grid">

    <div
      v-for="stat in stats"
      :key="stat.title"
      class="stat-card"
    >

      <!-- =================================================
           ICON
      ================================================== -->
      <div
        class="stat-icon"
        :class="stat.color"
      >
        <i :class="stat.icon"></i>
      </div>


      <!-- =================================================
           CONTENT
      ================================================== -->
      <div class="stat-content">

        <p>
          {{ stat.title }}
        </p>

        <h2>
          {{ stat.loading ? "..." : stat.value }}
        </h2>

        <span
          class="live-status"
          :class="stat.loading ? 'loading' : ''"
        >
          <i
            :class="
              stat.loading
                ? 'bi bi-arrow-repeat spinning'
                : 'bi bi-check-circle'
            "
          ></i>

          {{ stat.loading ? "Loading..." : "Live data" }}
        </span>

      </div>

    </div>

  </div>
</template>


<script setup>

import {
  ref,
  onMounted
} from "vue";


import {
  getBooks
} from "../../service/bookService.js";


import {
  getUsers
} from "../../service/userService.js";


import {
  getBorrowings
} from "../../service/borrowingService.js";


// =====================================================
// API
// =====================================================

const FINE_API_URL =
  "http://localhost:8080/api/fine";


// =====================================================
// STATS
// =====================================================

const stats = ref([

  {
    title: "Total Books",
    value: "0",
    icon: "bi bi-book",
    color: "purple",
    loading: true
  },

  {
    title: "Total Users",
    value: "0",
    icon: "bi bi-people",
    color: "blue",
    loading: true
  },

  {
    title: "Borrowed Books",
    value: "0",
    icon: "bi bi-journal-bookmark",
    color: "orange",
    loading: true
  },

  {
    title: "Overdue Books",
    value: "0",
    icon: "bi bi-exclamation-triangle",
    color: "red",
    loading: true
  },

  {
    title: "Total Fines",
    value: "$0.00",
    icon: "bi bi-cash-coin",
    color: "green",
    loading: true
  }

]);


// =====================================================
// FORMAT NUMBER
// =====================================================

function formatNumber(value) {

  const number =
    Number(value) || 0;

  return number.toLocaleString("en-US");

}


// =====================================================
// FORMAT MONEY
// =====================================================

function formatMoney(value) {

  const amount =
    Number(value) || 0;

  return `$${amount.toLocaleString(
    "en-US",
    {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }
  )}`;

}


// =====================================================
// GET FINE SUMMARY
// =====================================================

async function fetchFineSummary() {

  const token =
    sessionStorage.getItem("token");

  if (!token) {
    throw new Error(
      "Authentication token not found."
    );
  }


  const response =
    await fetch(
      `${FINE_API_URL}/getsummary`,
      {
        method: "GET",

        headers: {
          Authorization:
            `Bearer ${token}`,

          "Content-Type":
            "application/json"
        }
      }
    );


  let result = null;


  try {

    result =
      await response.json();

  } catch {

    result = null;

  }


  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch fine summary (${response.status})`
    );

  }


  console.log(
    "FINE SUMMARY RESPONSE:",
    result
  );


  return (
    result?.data ??
    result
  );

}


// =====================================================
// GET TOTAL BOOKS
// =====================================================

async function fetchTotalBooks() {

  try {

    const response =
      await getBooks();


    /*
     * bookService.js already returns
     * the array from result.data
     */

    const books =
      Array.isArray(response)
        ? response
        : [];


    /*
     * Total Books = total copies
     *
     * Example:
     * Book A qty = 5
     * Book B qty = 3
     * Book C qty = 2
     *
     * Total Books = 10
     */

    const totalBooks =
      books.reduce(
        (total, book) => {

          const qty =
            Number(
              book?.qty ??
              book?.quantity ??
              0
            );

          return total + qty;

        },
        0
      );


    stats.value[0].value =
      formatNumber(totalBooks);


    console.log(
      "TOTAL BOOKS:",
      totalBooks
    );


  } catch (error) {

    console.error(
      "FETCH TOTAL BOOKS ERROR:",
      error
    );

    stats.value[0].value =
      "0";

  } finally {

    stats.value[0].loading =
      false;

  }

}


// =====================================================
// GET TOTAL USERS
// =====================================================

async function fetchTotalUsers() {

  try {

    const response =
      await getUsers();


    /*
     * userService.js returns result.data
     */

    const users =
      Array.isArray(response)
        ? response
        : [];


    const totalUsers =
      users.length;


    stats.value[1].value =
      formatNumber(totalUsers);


    console.log(
      "TOTAL USERS:",
      totalUsers
    );


  } catch (error) {

    console.error(
      "FETCH TOTAL USERS ERROR:",
      error
    );

    stats.value[1].value =
      "0";

  } finally {

    stats.value[1].loading =
      false;

  }

}


// =====================================================
// GET BORROWED + OVERDUE
// =====================================================

async function fetchBorrowingStats() {

  try {

    const response =
      await getBorrowings();


    /*
     * borrowingService.js returns
     * result.data
     */

    const borrowings =
      Array.isArray(response)
        ? response
        : [];


    // ================================================
    // BORROWED BOOKS
    // ================================================

    const borrowedBooks =
      borrowings.filter(
        borrowing =>
          borrowing?.status
            ?.toUpperCase() ===
          "BORROWED"
      ).length;


    // ================================================
    // OVERDUE BOOKS
    // ================================================

    const overdueBooks =
      borrowings.filter(
        borrowing =>
          borrowing?.status
            ?.toUpperCase() ===
          "OVERDUE"
      ).length;


    stats.value[2].value =
      formatNumber(
        borrowedBooks
      );


    stats.value[3].value =
      formatNumber(
        overdueBooks
      );


    console.log(
      "BORROWED BOOKS:",
      borrowedBooks
    );


    console.log(
      "OVERDUE BOOKS:",
      overdueBooks
    );


  } catch (error) {

    console.error(
      "FETCH BORROWING STATS ERROR:",
      error
    );


    stats.value[2].value =
      "0";


    stats.value[3].value =
      "0";

  } finally {

    stats.value[2].loading =
      false;


    stats.value[3].loading =
      false;

  }

}


// =====================================================
// GET TOTAL FINES
// =====================================================

async function fetchTotalFines() {

  try {

    const summary =
      await fetchFineSummary();


    /*
     * Backend:
     *
     * FineSummaryResponse
     *
     * totalFines
     * unpaid
     * paid
     * totalLateDays
     */

    const totalFines =
      Number(
        summary?.totalFines ?? 0
      );


    stats.value[4].value =
      formatMoney(totalFines);


    console.log(
      "TOTAL FINES:",
      totalFines
    );


  } catch (error) {

    console.error(
      "FETCH TOTAL FINES ERROR:",
      error
    );


    stats.value[4].value =
      "$0.00";

  } finally {

    stats.value[4].loading =
      false;

  }

}


// =====================================================
// LOAD ALL DASHBOARD STATS
// =====================================================

async function loadDashboardStats() {

  await Promise.all([

    fetchTotalBooks(),

    fetchTotalUsers(),

    fetchBorrowingStats(),

    fetchTotalFines()

  ]);

}


// =====================================================
// ON MOUNTED
// =====================================================

onMounted(() => {

  loadDashboardStats();

});

</script>


<style scoped>

/* =================================================
   STATS GRID
================================================= */

.stats-grid {

  display: grid;

  grid-template-columns:
    repeat(5, minmax(0, 1fr));

  gap: 16px;

  margin-bottom: 20px;

}


/* =================================================
   STAT CARD
================================================= */

.stat-card {

  min-width: 0;

  display: flex;

  align-items: center;

  gap: 15px;

  background: white;

  border: 1px solid #e6e9ef;

  border-radius: 12px;

  padding: 20px;

  box-shadow:
    0 2px 5px rgba(0, 0, 0, 0.02);

}


/* =================================================
   ICON
================================================= */

.stat-icon {

  width: 48px;

  height: 48px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 10px;

  font-size: 21px;

  flex-shrink: 0;

}


/* =================================================
   PURPLE
================================================= */

.stat-icon.purple {

  background: #eeeaff;

  color: #5b3df5;

}


/* =================================================
   BLUE
================================================= */

.stat-icon.blue {

  background: #e7f1ff;

  color: #2878d4;

}


/* =================================================
   ORANGE
================================================= */

.stat-icon.orange {

  background: #fff2dd;

  color: #e88a16;

}


/* =================================================
   RED
================================================= */

.stat-icon.red {

  background: #ffe8e8;

  color: #e05252;

}


/* =================================================
   GREEN
================================================= */

.stat-icon.green {

  background: #e5f8ed;

  color: #16864a;

}


/* =================================================
   CONTENT
================================================= */

.stat-content {

  min-width: 0;

}


.stat-content p {

  margin: 0;

  color: #7b8497;

  font-size: 12px;

}


.stat-content h2 {

  margin: 5px 0;

  font-size: 23px;

  color: #172033;

  font-weight: 700;

}


/* =================================================
   LIVE STATUS
================================================= */

.live-status {

  display: flex;

  align-items: center;

  gap: 4px;

  font-size: 10px;

  font-weight: 500;

  color: #16864a;

}


.live-status i {

  font-size: 10px;

}


.live-status.loading {

  color: #9aa1b1;

}


/* =================================================
   SPINNING
================================================= */

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


/* =================================================
   RESPONSIVE
================================================= */

@media (max-width: 1200px) {

  .stats-grid {

    grid-template-columns:
      repeat(3, minmax(0, 1fr));

  }

}


@media (max-width: 768px) {

  .stats-grid {

    grid-template-columns: 1fr;

  }

}

</style>
```
