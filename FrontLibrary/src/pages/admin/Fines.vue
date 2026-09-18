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
         Error Message
    ========================== -->
    <div
      v-if="error"
      class="error-message"
    >
      <i class="bi bi-exclamation-circle"></i>
      <span>{{ error }}</span>

      <button
        type="button"
        @click="fetchFines"
      >
        Try Again
      </button>
    </div>


    <!-- =========================
         Loading
    ========================== -->
    <div
      v-if="loading"
      class="loading-container"
    >
      <div class="spinner"></div>
      <p>Loading fines...</p>
    </div>


    <!-- =========================
         Main Content
    ========================== -->
    <template v-else>

      <!-- =========================
           Summary Cards
      ========================== -->
      <div class="summary-grid">

        <!-- Total Fines -->
        <div class="summary-card">
          <div class="summary-icon total-icon">
            <i class="bi bi-cash-stack"></i>
          </div>

          <div class="summary-content">
            <p>Total Fines</p>
            <h2>${{ totalFines.toFixed(2) }}</h2>
          </div>
        </div>


        <!-- Unpaid -->
        <div class="summary-card">
          <div class="summary-icon unpaid-icon">
            <i class="bi bi-exclamation-circle"></i>
          </div>

          <div class="summary-content">
            <p>Unpaid</p>
            <h2>${{ unpaidFines.toFixed(2) }}</h2>
          </div>
        </div>


        <!-- Paid -->
        <div class="summary-card">
          <div class="summary-icon paid-icon">
            <i class="bi bi-check-circle"></i>
          </div>

          <div class="summary-content">
            <p>Paid</p>
            <h2>${{ paidFines.toFixed(2) }}</h2>
          </div>
        </div>


        <!-- Total Late Days -->
        <div class="summary-card">
          <div class="summary-icon late-icon">
            <i class="bi bi-calendar-x"></i>
          </div>

          <div class="summary-content">
            <p>Total Late Days</p>
            <h2>{{ totalLateDays }}</h2>
          </div>
        </div>

      </div>


      <!-- =========================
           Toolbar
      ========================== -->
      <div class="toolbar">

        <!-- Search -->
        <div class="search-box">
          <i class="bi bi-search"></i>

          <input
            v-model="search"
            type="text"
            placeholder="Search by user or book..."
          />
        </div>


        <!-- Status -->
        <div class="status-filter">
          <label for="status">Status:</label>

          <select
            id="status"
            v-model="selectedStatus"
          >
            <option value="All">
              All
            </option>

            <option value="Paid">
              Paid
            </option>

            <option value="Unpaid">
              Unpaid
            </option>
          </select>
        </div>

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

    </template>


    <!-- =========================
         Fine Details
    ========================== -->
    <FineDetails
      v-if="selectedFine"
      :fine="selectedFine"
      @close="closeDetails"
    />

  </div>
</template>


<script setup>
import { ref, computed, onMounted } from "vue";

import FineTable from "@/components/admin/fines/FineTable.vue";
import FineDetails from "@/components/admin/fines/FineDetails.vue";

import {
  getFines,
  markFineAsPaid,
  deleteFine as deleteFineApi
} from "@/service/adminfineService";


// ======================================================
// State
// ======================================================

const fines = ref([]);

const search = ref("");

const selectedStatus = ref("All");

const selectedFine = ref(null);

const loading = ref(false);

const error = ref("");


// ======================================================
// Fetch Fines
// ======================================================

const fetchFines = async () => {
  loading.value = true;
  error.value = "";

  try {
    const data = await getFines();

    console.log("Fine API Response:", data);

    /*
     * Support both:
     *
     * [
     *   {...},
     *   {...}
     * ]
     *
     * and
     *
     * {
     *   data: [...]
     * }
     */

    if (Array.isArray(data)) {
      fines.value = data;
    } else if (Array.isArray(data?.data)) {
      fines.value = data.data;
    } else if (Array.isArray(data?.content)) {
      fines.value = data.content;
    } else {
      fines.value = [];
    }

  } catch (err) {
    console.error("Error fetching fines:", err);

    error.value =
      err?.message ||
      "Failed to load fines.";
  } finally {
    loading.value = false;
  }
};


// ======================================================
// Search + Status Filter
// ======================================================

const filteredFines = computed(() => {
  const keyword = search.value
    .trim()
    .toLowerCase();

  return fines.value.filter((fine) => {

    /*
     * Support possible backend field names.
     */

    const userName =
      fine.user ||
      fine.userName ||
      fine.borrowerName ||
      fine.name ||
      "";

    const bookTitle =
      fine.book ||
      fine.bookTitle ||
      fine.title ||
      "";

    const status =
      fine.status ||
      "";


    const matchesSearch =
      !keyword ||
      String(userName)
        .toLowerCase()
        .includes(keyword) ||
      String(bookTitle)
        .toLowerCase()
        .includes(keyword);


    const matchesStatus =
      selectedStatus.value === "All" ||
      String(status).toLowerCase() ===
        selectedStatus.value.toLowerCase();


    return matchesSearch && matchesStatus;
  });
});


// ======================================================
// Helper - Get Amount
// ======================================================

const getFineAmount = (fine) => {
  const amount =
    fine.amount ??
    fine.fine ??
    fine.totalFine ??
    fine.fineAmount ??
    0;

  const number = Number(amount);

  return Number.isFinite(number)
    ? number
    : 0;
};


// ======================================================
// Helper - Get Late Days
// ======================================================

const getLateDays = (fine) => {
  const days =
    fine.daysLate ??
    fine.lateDays ??
    fine.overdueDays ??
    0;

  const number = Number(days);

  return Number.isFinite(number)
    ? number
    : 0;
};


// ======================================================
// Statistics
// ======================================================

const totalFines = computed(() => {
  return fines.value.reduce(
    (total, fine) => {
      return total + getFineAmount(fine);
    },
    0
  );
});


const unpaidFines = computed(() => {
  return fines.value
    .filter((fine) => {
      return String(fine.status || "")
        .toLowerCase() === "unpaid";
    })
    .reduce(
      (total, fine) => {
        return total + getFineAmount(fine);
      },
      0
    );
});


const paidFines = computed(() => {
  return fines.value
    .filter((fine) => {
      return String(fine.status || "")
        .toLowerCase() === "paid";
    })
    .reduce(
      (total, fine) => {
        return total + getFineAmount(fine);
      },
      0
    );
});


const totalLateDays = computed(() => {
  return fines.value.reduce(
    (total, fine) => {
      return total + getLateDays(fine);
    },
    0
  );
});


// ======================================================
// View Fine
// ======================================================

const viewFine = async (fine) => {
  selectedFine.value = fine;
};


// ======================================================
// Close Details
// ======================================================

const closeDetails = () => {
  selectedFine.value = null;
};


// ======================================================
// Mark Fine As Paid
// ======================================================

const markAsPaid = async (fine) => {

  if (!fine?.id) {
    alert("Fine ID not found.");
    return;
  }


  if (
    !confirm(
      "Are you sure you want to mark this fine as paid?"
    )
  ) {
    return;
  }


  try {

    await markFineAsPaid(fine.id);

    /*
     * Update local data after successful API request.
     */

    fine.status = "Paid";

    /*
     * If FineDetails is currently showing
     * this fine, update it too.
     */

    if (
      selectedFine.value &&
      selectedFine.value.id === fine.id
    ) {
      selectedFine.value = {
        ...selectedFine.value,
        status: "Paid"
      };
    }

    alert("Fine marked as paid successfully.");

  } catch (err) {

    console.error(
      "Error marking fine as paid:",
      err
    );

    alert(
      err?.message ||
      "Failed to mark fine as paid."
    );
  }
};


// ======================================================
// Delete Fine
// ======================================================

const deleteFine = async (id) => {

  if (!id) {
    alert("Fine ID not found.");
    return;
  }


  if (
    !confirm(
      "Are you sure you want to delete this fine?"
    )
  ) {
    return;
  }


  try {

    await deleteFineApi(id);

    /*
     * Remove from frontend after successful delete.
     */

    fines.value = fines.value.filter(
      (fine) => fine.id !== id
    );


    /*
     * Close details if deleted fine
     * is currently selected.
     */

    if (
      selectedFine.value &&
      selectedFine.value.id === id
    ) {
      selectedFine.value = null;
    }


    alert("Fine deleted successfully.");

  } catch (err) {

    console.error(
      "Error deleting fine:",
      err
    );

    alert(
      err?.message ||
      "Failed to delete fine."
    );
  }
};


// ======================================================
// Load Data When Page Opens
// ======================================================

onMounted(() => {
  fetchFines();
});

</script>


<style scoped>

/* ======================================================
   Page
====================================================== */

.fines-page {
  min-height: 100vh;
  background: #f8fafc;
  padding: 60px;
}


/* ======================================================
   Header
====================================================== */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}


.page-header h1 {
  margin: 0;
  font-size: 28px;
  font-weight: 700;
  color: #1e293b;
}


.page-header p {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 14px;
}


/* ======================================================
   Error
====================================================== */

.error-message {
  display: flex;
  align-items: center;
  gap: 10px;

  padding: 14px 16px;
  margin-bottom: 20px;

  background: #fef2f2;
  border: 1px solid #fecaca;

  border-radius: 8px;

  color: #b91c1c;
  font-size: 14px;
}


.error-message button {
  margin-left: auto;

  border: none;
  background: transparent;

  color: #b91c1c;

  font-weight: 600;
  cursor: pointer;
}


.error-message button:hover {
  text-decoration: underline;
}


/* ======================================================
   Loading
====================================================== */

.loading-container {
  min-height: 300px;

  display: flex;
  flex-direction: column;

  align-items: center;
  justify-content: center;

  color: #64748b;
}


.loading-container p {
  margin-top: 12px;
  font-size: 14px;
}


.spinner {
  width: 36px;
  height: 36px;

  border: 4px solid #e2e8f0;
  border-top-color: #2563eb;

  border-radius: 50%;

  animation: spin 0.8s linear infinite;
}


@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}


/* ======================================================
   Summary Grid
====================================================== */

.summary-grid {
  display: grid;

  grid-template-columns:
    repeat(4, minmax(0, 1fr));

  gap: 20px;

  margin-bottom: 25px;
}


/* ======================================================
   Summary Card
====================================================== */

.summary-card {
  background: white;

  border: 1px solid #e2e8f0;
  border-radius: 12px;

  padding: 20px;

  display: flex;
  align-items: center;

  gap: 15px;

  box-shadow:
    0 1px 2px rgba(15, 23, 42, 0.04);
}


.summary-icon {
  width: 48px;
  height: 48px;

  border-radius: 10px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 21px;
}


.total-icon {
  background: #eff6ff;
  color: #2563eb;
}


.unpaid-icon {
  background: #fef2f2;
  color: #dc2626;
}


.paid-icon {
  background: #f0fdf4;
  color: #16a34a;
}


.late-icon {
  background: #fff7ed;
  color: #ea580c;
}


.summary-content {
  min-width: 0;
}


.summary-content p {
  margin: 0 0 5px;

  font-size: 13px;
  color: #64748b;
}


.summary-content h2 {
  margin: 0;

  font-size: 22px;
  font-weight: 700;

  color: #1e293b;
}


/* ======================================================
   Toolbar
====================================================== */

.toolbar {
  background: white;

  border: 1px solid #e2e8f0;
  border-radius: 10px;

  padding: 16px;

  margin-bottom: 20px;

  display: flex;
  align-items: center;
  justify-content: space-between;

  gap: 20px;
}


/* ======================================================
   Search
====================================================== */

.search-box {
  position: relative;

  width: 100%;
  max-width: 420px;
}


.search-box i {
  position: absolute;

  left: 14px;
  top: 50%;

  transform: translateY(-50%);

  color: #94a3b8;

  font-size: 15px;
}


.search-box input {
  width: 100%;

  box-sizing: border-box;

  padding: 11px 14px 11px 40px;

  border: 1px solid #cbd5e1;
  border-radius: 8px;

  outline: none;

  font-size: 14px;

  color: #334155;

  background: white;
}


.search-box input:focus {
  border-color: #2563eb;

  box-shadow:
    0 0 0 3px rgba(37, 99, 235, 0.1);
}


.search-box input::placeholder {
  color: #94a3b8;
}


/* ======================================================
   Status Filter
====================================================== */

.status-filter {
  display: flex;

  align-items: center;

  gap: 10px;

  white-space: nowrap;
}


.status-filter label {
  font-size: 14px;

  color: #475569;

  font-weight: 500;
}


.status-filter select {
  min-width: 120px;

  padding: 10px 12px;

  border: 1px solid #cbd5e1;
  border-radius: 8px;

  background: white;

  color: #334155;

  font-size: 14px;

  outline: none;

  cursor: pointer;
}


.status-filter select:focus {
  border-color: #2563eb;

  box-shadow:
    0 0 0 3px rgba(37, 99, 235, 0.1);
}


/* ======================================================
   Responsive
====================================================== */

@media (max-width: 1100px) {

  .summary-grid {
    grid-template-columns:
      repeat(2, minmax(0, 1fr));
  }

}


@media (max-width: 768px) {

  .fines-page {
    padding: 20px;
  }


  .page-header {
    align-items: flex-start;
  }


  .page-header h1 {
    font-size: 24px;
  }


  .toolbar {
    flex-direction: column;
    align-items: stretch;
  }


  .search-box {
    max-width: none;
  }


  .status-filter {
    justify-content: space-between;
  }

}


@media (max-width: 520px) {

  .summary-grid {
    grid-template-columns: 1fr;
  }


  .fines-page {
    padding: 15px;
  }

}

</style>