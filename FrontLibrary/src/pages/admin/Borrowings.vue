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
         Borrowing Table
    ========================== -->
    <BorrowingTable
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
  computed
} from "vue";

import BorrowingTable from "@/components/admin/borrowings/BorrowingTable.vue";


import BorrowingDetails from "@/components/admin/borrowings/BorrowingDetails.vue";


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

const borrowings = ref([

  {
    id: 1,
    user: "Dara Sok",
    email: "dara@gmail.com",
    book: "Clean Code",
    borrowDate: "2026-08-01",
    dueDate: "2026-08-15",
    returnDate: null,
    fine: 0,
    status: "Borrowed"
  },

  {
    id: 2,
    user: "Sreyneang Kim",
    email: "sreyneang@gmail.com",
    book: "Java Programming",
    borrowDate: "2026-07-15",
    dueDate: "2026-07-29",
    returnDate: null,
    fine: 1.50,
    status: "Late"
  },

  {
    id: 3,
    user: "Rithy Chan",
    email: "rithy@gmail.com",
    book: "Database System",
    borrowDate: "2026-08-05",
    dueDate: "2026-08-19",
    returnDate: null,
    fine: 0,
    status: "Borrowed"
  },

  {
    id: 4,
    user: "Sokha Lim",
    email: "sokha@gmail.com",
    book: "Web Development",
    borrowDate: "2026-07-05",
    dueDate: "2026-07-19",
    returnDate: null,
    fine: 2.00,
    status: "Late"
  },

  {
    id: 5,
    user: "Vanna Chea",
    email: "vanna@gmail.com",
    book: "Python Programming",
    borrowDate: "2026-07-25",
    dueDate: "2026-08-08",
    returnDate: "2026-08-07",
    fine: 0,
    status: "Returned"
  }

]);


/* =========================
   Search + Filter
========================= */

const filteredBorrowings = computed(() => {

  const keyword =
    search.value
      .toLowerCase()
      .trim();


  return borrowings.value.filter(item => {

    const matchesSearch =
      item.user
        .toLowerCase()
        .includes(keyword)

      ||

      item.book
        .toLowerCase()
        .includes(keyword);


    const matchesStatus =
      statusFilter.value === "All"

      ||

      item.status === statusFilter.value;


    return (
      matchesSearch &&
      matchesStatus
    );

  });

});


/* =========================
   View Borrowing
========================= */

function viewBorrowing(item) {

  selectedBorrowing.value = item;

}


/* =========================
   Close Details
========================= */

function closeDetails() {

  selectedBorrowing.value = null;

}


/* =========================
   Return Book
========================= */

function returnBook(item) {

  if (item.status === "Returned") {

    return;

  }


  const confirmed = window.confirm(

    `Are you sure you want to return "${item.book}"?`

  );


  if (!confirmed) {

    return;

  }


  /* Today's Date */

  const today = new Date();

  const year =
    today.getFullYear();

  const month =
    String(
      today.getMonth() + 1
    ).padStart(2, "0");

  const day =
    String(
      today.getDate()
    ).padStart(2, "0");


  const returnDate =
    `${year}-${month}-${day}`;


  /* Update Return Date */

  item.returnDate =
    returnDate;


  /* Calculate Fine */

  if (
    returnDate >
    item.dueDate
  ) {

    item.fine =
      calculateFine(
        item.dueDate,
        returnDate
      );

  }


  /* Change Status */

  item.status =
    "Returned";


  alert(
    `"${item.book}" has been returned successfully.`
  );

}


/* =========================
   Calculate Fine
========================= */

function calculateFine(
  dueDate,
  returnDate
) {

  const due =
    new Date(dueDate);

  const returned =
    new Date(returnDate);


  const difference =
    returned - due;


  const lateDays =
    Math.ceil(
      difference /
      (1000 * 60 * 60 * 24)
    );


  const finePerDay =
    0.50;


  return Math.max(
    0,
    lateDays * finePerDay
  );

}


/* =========================
   Renew Book
========================= */

function renewBook(item) {
  if (
    item.status === "Returned"
  ) {
    return;
  }
  const confirmed =
    window.confirm(
      `Do you want to renew "${item.book}" for 7 more days?`
    );
  if (!confirmed) {
    return;
  }
  /* Calculate New Due Date */
  const due =
    new Date(item.dueDate);
  due.setDate(
    due.getDate() + 7
  );
  const year =
    due.getFullYear();
  const month =
    String(
      due.getMonth() + 1
    ).padStart(2, "0");
  const day =
    String(
      due.getDate()
    ).padStart(2, "0");
  const newDueDate =
    `${year}-${month}-${day}`;
  /* Update */
  item.dueDate =newDueDate;
  item.fine =0;
  item.status ="Borrowed";
  alert(
    `"${item.book}" has been renewed successfully.\n\n` +
    `New Due Date: ${newDueDate}`
  );
}
</script>

<style scoped>
.borrowings-page {
  min-height: calc(100vh - 70px);
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
  justify-content: space-between;
  gap: 15px;
  margin-bottom: 20px;
}
.search-box {
  position: relative;
  width: 350px;
}
.search-icon {
  position: absolute;
  left: 13px;
  top: 50%;
  transform: translateY(-50%);
  color: #9ca3af;
  font-size: 16px;
}
.search-box input {
  width: 100%;
  height: 44px;
  box-sizing: border-box;
  padding:0 15px 0 40px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  outline: none;
  background: white;
  color: #374151;
  font-size: 14px;
}
.search-box input:focus {
  border-color: #2563eb;
}
.status-filter {
  width: 160px;
  height: 44px;
  padding: 0 12px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  outline: none;
  background: white;
  color: #374151;
  cursor: pointer;
}
/* =========================
   Responsive
========================= */
@media (max-width: 650px) {
  .borrowings-page {
    padding: 20px;
  }
  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }
  .search-box {
    width: 100%;
  }
  .status-filter {
    width: 100%;
  }
}
</style>