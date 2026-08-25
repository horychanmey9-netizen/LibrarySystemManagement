<template>
  <div class="borrowings-page">

    <!-- =========================
         Header
    ========================== -->
    <div class="page-header">
      <div>
        <h1>Borrowing Management</h1>

        <p>
          Manage borrowed books
        </p>
      </div>
    </div>


    <!-- =========================
         Search & Filter
    ========================== -->
    <div class="toolbar">

      <!-- Search -->
      <div class="search-box">

        <i class="bi bi-search search-icon"></i>

        <input
          v-model="search"
          type="text"
          placeholder="Search user or book..."
        />

      </div>


      <!-- Status Filter -->
      <select
        v-model="statusFilter"
        class="status-filter"
      >

        <option value="All">
          All Status
        </option>

        <option value="Borrowed">
          Borrowed
        </option>

        <option value="Late">
          Late
        </option>

        <option value="Returned">
          Returned
        </option>

      </select>

    </div>


    <!-- =========================
         Loading
    ========================== -->
    <div
      v-if="loading"
      class="loading-message"
    >
      Loading borrowings...
    </div>


    <!-- =========================
         Error
    ========================== -->
    <div
      v-else-if="error"
      class="error-message"
    >
      {{ error }}

      <button
        class="retry-btn"
        @click="fetchBorrowings"
      >
        Try Again
      </button>
    </div>


    <!-- =========================
         Borrowing Table
    ========================== -->
    <BorrowingTable
      v-else
      :borrowings="filteredBorrowings"
      @view="viewBorrowing"
      @return="returnBook"
      @renew="renewBook"
    />


    <!-- =========================
         Borrowing Details
    ========================== -->
    <BorrowingDetails
      :borrowing="selectedBorrowing"
      @close="closeDetails"
    />

  </div>
</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from "vue";


import BorrowingTable
  from "@/components/admin/borrowings/BorrowingTable.vue";


import BorrowingDetails
  from "@/components/admin/borrowings/BorrowingDetails.vue";


import {
  getBorrowings,
  updateBorrowing
} from "@/service/borrowingService.js";


/* =========================
   Search
========================= */

const search = ref("");



/* =========================
   Status Filter
========================= */

const statusFilter = ref("All");



/* =========================
   Selected Borrowing
========================= */

const selectedBorrowing = ref(null);



/* =========================
   Borrowing Data
========================= */

const borrowings = ref([]);



/* =========================
   Loading
========================= */

const loading = ref(false);



/* =========================
   Error
========================= */

const error = ref("");



/* =========================
   Fetch Borrowings
========================= */

async function fetchBorrowings() {

  loading.value = true;

  error.value = "";


  try {

    const data =
      await getBorrowings();


    /*
     * Backend should return:
     *
     * [
     *   {
     *     id,
     *     userId,
     *     userName,
     *     bookId,
     *     bookTitle,
     *     borrowDate,
     *     dueDate,
     *     returnDate,
     *     fine,
     *     status,
     *     createdAt,
     *     updatedAt
     *   }
     * ]
     */


    borrowings.value =
      Array.isArray(data)
        ? data
        : [];


  } catch (err) {

    console.error(
      "Failed to fetch borrowings:",
      err
    );


    error.value =
      "Failed to load borrowing data.";

  } finally {

    loading.value = false;

  }

}



/* =========================
   Load Data
========================= */

onMounted(() => {

  fetchBorrowings();

});



/* =========================
   Search + Filter
========================= */

const filteredBorrowings = computed(() => {

  const keyword =
    search.value
      .toLowerCase()
      .trim();


  return borrowings.value.filter(
    item => {

      const userName =
        item.userName || "";


      const bookTitle =
        item.bookTitle || "";


      /*
       * Search User
       */

      const matchesUser =
        userName
          .toLowerCase()
          .includes(keyword);


      /*
       * Search Book
       */

      const matchesBook =
        bookTitle
          .toLowerCase()
          .includes(keyword);


      /*
       * Search Result
       */

      const matchesSearch =
        matchesUser ||
        matchesBook;


      /*
       * Status Filter
       */

      const matchesStatus =
        statusFilter.value === "All"
        ||
        item.status ===
          statusFilter.value;


      return (
        matchesSearch &&
        matchesStatus
      );

    }
  );

});



/* =========================
   View Borrowing
========================= */

function viewBorrowing(item) {

  selectedBorrowing.value =
    item;

}



/* =========================
   Close Details
========================= */

function closeDetails() {

  selectedBorrowing.value =
    null;

}



/* =========================
   Return Book
========================= */

async function returnBook(item) {

  /*
   * Already returned
   */

  if (
    item.status === "Returned"
  ) {

    return;

  }


  /*
   * Confirmation
   */

  const confirmed =
    window.confirm(
      `Are you sure you want to return "${item.bookTitle}"?`
    );


  if (!confirmed) {

    return;

  }


  try {

    /*
     * Call Backend API
     */

    const updatedBorrowing =
      await returnBorrowing(
        item.id
      );


    /*
     * Update selected item
     */

    if (updatedBorrowing) {

      Object.assign(
        item,
        updatedBorrowing
      );

    }


    /*
     * Close details if opened
     */

    selectedBorrowing.value =
      null;


    /*
     * Refresh data
     */

    await fetchBorrowings();


    /*
     * Success message
     */

    alert(
      `"${item.bookTitle}" has been returned successfully.`
    );


  } catch (err) {

    console.error(
      "Failed to return book:",
      err
    );


    alert(
      "Failed to return book. Please try again."
    );

  }

}



/* =========================
   Renew Book
========================= */

async function renewBook(item) {

  /*
   * Cannot renew returned book
   */

  if (
    item.status === "Returned"
  ) {

    return;

  }


  /*
   * Confirmation
   */

  const confirmed =
    window.confirm(
      `Do you want to renew "${item.bookTitle}" for 7 more days?`
    );


  if (!confirmed) {

    return;

  }


  try {

    /*
     * Call Backend API
     */

    const updatedBorrowing =
      await renewBorrowing(
        item.id
      );


    /*
     * Update item
     */

    if (updatedBorrowing) {

      Object.assign(
        item,
        updatedBorrowing
      );

    }


    /*
     * Close details
     */

    selectedBorrowing.value =
      null;


    /*
     * Refresh data
     */

    await fetchBorrowings();


    /*
     * Success message
     */

    alert(
      `"${item.bookTitle}" has been renewed successfully.\n\n` +
      `New Due Date: ${item.dueDate}`
    );


  } catch (err) {

    console.error(
      "Failed to renew borrowing:",
      err
    );


    alert(
      "Failed to renew borrowing. Please try again."
    );

  }

}

</script>


<style scoped>

.borrowings-page {

  min-height:
    calc(100vh - 70px);

  padding: 30px;

  background: #f8fafc;

  box-sizing: border-box;

}


/* =========================
   Header
========================= */

.page-header {

  margin-bottom: 25px;

}


.page-header h1 {

  margin: 0;

  color: #1f2937;

  font-size: 28px;

  font-weight: 700;

}


.page-header p {

  margin: 6px 0 0;

  color: #6b7280;

  font-size: 14px;

}


/* =========================
   Toolbar
========================= */

.toolbar {

  display: flex;

  align-items: center;

  justify-content:
    space-between;

  gap: 15px;

  margin-bottom: 20px;

}


/* =========================
   Search Box
========================= */

.search-box {

  position: relative;

  width: 350px;

}


.search-icon {

  position: absolute;

  left: 13px;

  top: 50%;

  transform:
    translateY(-50%);

  color: #9ca3af;

  font-size: 16px;

}


.search-box input {

  width: 100%;

  height: 44px;

  box-sizing: border-box;

  padding:
    0 15px 0 40px;

  border:
    1px solid #d1d5db;

  border-radius: 8px;

  outline: none;

  background: white;

  color: #374151;

  font-size: 14px;

}


.search-box input:focus {

  border-color: #2563eb;

}


/* =========================
   Status Filter
========================= */

.status-filter {

  width: 160px;

  height: 44px;

  padding:
    0 12px;

  border:
    1px solid #d1d5db;

  border-radius: 8px;

  outline: none;

  background: white;

  color: #374151;

  cursor: pointer;

}


.status-filter:focus {

  border-color: #2563eb;

}


/* =========================
   Loading
========================= */

.loading-message {

  padding: 40px;

  text-align: center;

  background: white;

  border:
    1px solid #e5e7eb;

  border-radius: 10px;

  color: #6b7280;

  font-size: 14px;

}


/* =========================
   Error
========================= */

.error-message {

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 15px;

  padding: 30px;

  background: white;

  border:
    1px solid #fecaca;

  border-radius: 10px;

  color: #dc2626;

  font-size: 14px;

}


.retry-btn {

  padding:
    8px 16px;

  border: none;

  border-radius: 6px;

  background: #2563eb;

  color: white;

  cursor: pointer;

  font-size: 13px;

}


.retry-btn:hover {

  background: #1d4ed8;

}


/* =========================
   Responsive
========================= */

@media (max-width: 650px) {

  .borrowings-page {

    padding: 20px;

  }


  .toolbar {

    flex-direction:
      column;

    align-items:
      stretch;

  }


  .search-box {

    width: 100%;

  }


  .status-filter {

    width: 100%;

  }


  .error-message {

    flex-direction:
      column;

  }

}

</style>