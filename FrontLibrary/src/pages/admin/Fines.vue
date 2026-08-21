<template>
  <div class="fines-page">

    <!-- =========================
         Header
    ========================== -->
    <div class="page-header">
      <div>
        <h1>Fines Management</h1>
        <p>Manage overdue book fines and payments</p>
      </div>
    </div>


    <!-- =========================
         Summary Cards
    ========================== -->
    <div class="summary-grid">

      <!-- Total Fines -->
      <div class="summary-card">
        <div class="summary-icon total">
          <i class="bi bi-cash-stack"></i>
        </div>

        <div>
          <p>Total Fines</p>
          <h2>${{ totalFines.toFixed(2) }}</h2>
        </div>
      </div>


      <!-- Unpaid -->
      <div class="summary-card">
        <div class="summary-icon unpaid">
          <i class="bi bi-exclamation-triangle"></i>
        </div>

        <div>
          <p>Unpaid</p>
          <h2>${{ unpaidFines.toFixed(2) }}</h2>
        </div>
      </div>


      <!-- Paid -->
      <div class="summary-card">
        <div class="summary-icon paid">
          <i class="bi bi-check-circle"></i>
        </div>

        <div>
          <p>Paid</p>
          <h2>${{ paidFines.toFixed(2) }}</h2>
        </div>
      </div>


      <!-- Late Days -->
      <div class="summary-card">
        <div class="summary-icon days">
          <i class="bi bi-calendar3"></i>
        </div>

        <div>
          <p>Total Late Days</p>
          <h2>{{ totalLateDays }}</h2>
        </div>
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
        v-model="selectedStatus"
        class="status-filter"
      >
        <option value="All">
          All Status
        </option>

        <option value="Paid">
          Paid
        </option>

        <option value="Unpaid">
          Unpaid
        </option>
      </select>

    </div>


    <!-- =========================
         Fine Table
    ========================== -->
    <FineTable
      :fines="filteredFines"
      @view="viewFine"
      @mark-paid="markAsPaid"
      @delete="deleteFine"
    />


    <!-- =========================
         Fine Details
    ========================== -->
    <FineDetails
      :fine="selectedFine"
      @close="closeDetails"
    />

  </div>
</template>


<script setup>

import { ref, computed } from "vue";

import FineTable from "@/components/admin/fines/FineTable.vue";
import FineDetails from "@/components/admin/fines/FineDetails.vue";


/* =========================
   Search
========================= */

const search = ref("");

const selectedStatus = ref("All");


/* =========================
   Selected Fine
========================= */

const selectedFine = ref(null);


/* =========================
   Fine Data
========================= */

const fines = ref([
  {
    id: 1,
    user: "Dara",
    book: "Java Book",
    dueDate: "August 10, 2026",
    returnedDate: "August 13, 2026",
    daysLate: 3,
    rate: 0.50,
    amount: 1.50,
    status: "Unpaid"
  },

  {
    id: 2,
    user: "Lina",
    book: "Database",
    dueDate: "August 10, 2026",
    returnedDate: "August 15, 2026",
    daysLate: 5,
    rate: 0.50,
    amount: 2.50,
    status: "Paid"
  },

  {
    id: 3,
    user: "Sokha",
    book: "Web Development",
    dueDate: "August 12, 2026",
    returnedDate: "August 14, 2026",
    daysLate: 2,
    rate: 0.50,
    amount: 1.00,
    status: "Unpaid"
  },

  {
    id: 4,
    user: "Vanna",
    book: "Spring Boot",
    dueDate: "August 8, 2026",
    returnedDate: "August 12, 2026",
    daysLate: 4,
    rate: 0.50,
    amount: 2.00,
    status: "Paid"
  },

  {
    id: 5,
    user: "Sreyneang",
    book: "JavaScript",
    dueDate: "August 5, 2026",
    returnedDate: "August 12, 2026",
    daysLate: 7,
    rate: 0.50,
    amount: 3.50,
    status: "Unpaid"
  }
]);


/* =========================
   Filter
========================= */

const filteredFines = computed(() => {

  const keyword = search.value
    .toLowerCase()
    .trim();

  return fines.value.filter((fine) => {

    const matchesSearch =
      fine.user
        .toLowerCase()
        .includes(keyword) ||

      fine.book
        .toLowerCase()
        .includes(keyword);

    const matchesStatus =
      selectedStatus.value === "All" ||
      fine.status === selectedStatus.value;

    return matchesSearch && matchesStatus;

  });

});


/* =========================
   Statistics
========================= */

const totalFines = computed(() => {

  return fines.value.reduce(
    (total, fine) =>
      total + fine.amount,
    0
  );

});


const unpaidFines = computed(() => {

  return fines.value
    .filter(
      fine => fine.status === "Unpaid"
    )
    .reduce(
      (total, fine) =>
        total + fine.amount,
      0
    );

});


const paidFines = computed(() => {

  return fines.value
    .filter(
      fine => fine.status === "Paid"
    )
    .reduce(
      (total, fine) =>
        total + fine.amount,
      0
    );

});


const totalLateDays = computed(() => {

  return fines.value.reduce(
    (total, fine) =>
      total + fine.daysLate,
    0
  );

});


/* =========================
   View Fine
========================= */

function viewFine(fine) {

  selectedFine.value = fine;

}


/* =========================
   Close Details
========================= */

function closeDetails() {

  selectedFine.value = null;

}


/* =========================
   Mark As Paid
========================= */

function markAsPaid(fine) {

  if (!fine) return;

  if (fine.status === "Paid") return;

  const confirmed = window.confirm(
    "Are you sure you want to mark as paid?"
  );

  if (!confirmed) return;

  fine.status = "Paid";

}


/* =========================
   Delete Fine
========================= */

function deleteFine(id) {

  const confirmed = window.confirm(
    "Are you sure you want to delete this fine?"
  );

  if (!confirmed) return;

  fines.value = fines.value.filter(
    fine => fine.id !== id
  );

  if (
    selectedFine.value &&
    selectedFine.value.id === id
  ) {

    closeDetails();

  }

}

</script>


<style scoped>

/* =========================
   Page
========================= */

.fines-page {
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
   Summary
========================= */

.summary-grid {
  display: grid;

  grid-template-columns:
    repeat(4, 1fr);

  gap: 18px;

  margin-bottom: 25px;
}


.summary-card {
  display: flex;

  align-items: center;

  gap: 15px;

  padding: 20px;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 10px;
}


.summary-icon {
  width: 45px;
  height: 45px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 10px;

  font-size: 20px;
}


.summary-icon.total {
  background: #eff6ff;
  color: #2563eb;
}


.summary-icon.unpaid {
  background: #fff7ed;
  color: #ea580c;
}


.summary-icon.paid {
  background: #ecfdf5;
  color: #16a34a;
}


.summary-icon.days {
  background: #f5f3ff;
  color: #7c3aed;
}


.summary-card p {
  margin: 0 0 5px;

  color: #6b7280;

  font-size: 13px;
}


.summary-card h2 {
  margin: 0;

  color: #111827;

  font-size: 22px;
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

  font-size: 17px;

  pointer-events: none;
}


.search-box input {
  width: 100%;

  height: 44px;

  box-sizing: border-box;

  padding:
    0 15px 0 40px;

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


.status-filter:focus {
  border-color: #2563eb;
}


/* =========================
   Responsive
========================= */

@media (max-width: 1000px) {

  .summary-grid {
    grid-template-columns:
      repeat(2, 1fr);
  }

}


@media (max-width: 650px) {

  .fines-page {
    padding: 20px;
  }

  .summary-grid {
    grid-template-columns: 1fr;
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