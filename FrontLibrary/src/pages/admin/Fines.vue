
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

            <h2>
              ៛{{ formatAmount(totalFines) }}
            </h2>
          </div>
        </div>


        <!-- Unpaid -->
        <div class="summary-card">
          <div class="summary-icon unpaid-icon">
            <i class="bi bi-exclamation-circle"></i>
          </div>

          <div class="summary-content">
            <p>Unpaid</p>

            <h2>
              ៛{{ formatAmount(unpaidFines) }}
            </h2>
          </div>
        </div>


        <!-- Paid -->
        <div class="summary-card">
          <div class="summary-icon paid-icon">
            <i class="bi bi-check-circle"></i>
          </div>

          <div class="summary-content">
            <p>Paid</p>

            <h2>
              ៛{{ formatAmount(paidFines) }}
            </h2>
          </div>
        </div>


        <!-- Total Late Days -->
        <div class="summary-card">
          <div class="summary-icon late-icon">
            <i class="bi bi-calendar-x"></i>
          </div>

          <div class="summary-content">
            <p>Total Late Days</p>

            <h2>
              {{ totalLateDays }}
            </h2>
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

          <label for="status">
            Status:
          </label>

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

import {
  ref,
  computed,
  onMounted
} from "vue";


import FineTable
  from "@/components/admin/fines/FineTable.vue";


import FineDetails
  from "@/components/admin/fines/FineDetails.vue";


import {
  getFines,
  markFineAsPaid,
  deleteFine as deleteFineApi
} from "@/service/adminfineService";


// ======================================================
// STATE
// ======================================================

const fines = ref([]);

const search = ref("");

const selectedStatus = ref("All");

const selectedFine = ref(null);

const loading = ref(false);

const error = ref("");


// ======================================================
// FORMAT AMOUNT
// ======================================================

const formatAmount = (amount) => {

  const number = Number(amount);

  if (!Number.isFinite(number)) {
    return "0.00";
  }

  return number.toLocaleString(
    "en-US",
    {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }
  );

};


// ======================================================
// GET SAFE AMOUNT
// ======================================================

const getFineAmount = (fine) => {

  if (!fine) {
    return 0;
  }


  let amount =
    fine.totalAmount ??
    fine.amount ??
    fine.fineAmount ??
    fine.totalFine ??
    fine.fine ??
    0;


  // -----------------------------------------------
  // Number
  // -----------------------------------------------

  if (typeof amount === "number") {

    return Number.isFinite(amount)
      ? amount
      : 0;

  }


  // -----------------------------------------------
  // String
  // -----------------------------------------------

  if (typeof amount === "string") {

    const cleanedAmount =
      amount
        .replace(/,/g, "")
        .replace(/[៛$]/g, "")
        .trim();


    const number =
      Number(cleanedAmount);


    return Number.isFinite(number)
      ? number
      : 0;

  }


  // -----------------------------------------------
  // Object
  // -----------------------------------------------

  if (
    typeof amount === "object" &&
    amount !== null
  ) {

    const value =
      amount.value ??
      amount.amount ??
      amount.totalAmount ??
      0;


    const number =
      Number(value);


    return Number.isFinite(number)
      ? number
      : 0;

  }


  return 0;

};


// ======================================================
// GET LATE DAYS
// ======================================================

const getLateDays = (fine) => {

  if (!fine) {
    return 0;
  }


  const days =
    fine.daysLate ??
    fine.lateDays ??
    fine.overdueDays ??
    0;


  const number =
    Number(days);


  return Number.isFinite(number)
    ? number
    : 0;

};


// ======================================================
// GET USER NAME
// ======================================================

const getUserName = (fine) => {

  if (!fine) {
    return "";
  }


  const user =
    fine.user ||
    fine.borrower ||
    fine.member ||
    fine.account ||
    null;


  return (
    fine.userName ??
    fine.borrowerName ??
    fine.memberName ??
    fine.name ??
    user?.userName ??
    user?.username ??
    user?.name ??
    user?.fullName ??
    user?.firstName ??
    ""
  );

};


// ======================================================
// GET BOOK TITLE
// ======================================================

const getBookTitle = (fine) => {

  if (!fine) {
    return "";
  }


  const book =
    fine.book ||
    fine.borrowing?.book ||
    fine.borrower?.book ||
    null;


  return (
    fine.bookTitle ??
    fine.title ??
    book?.title ??
    book?.bookTitle ??
    ""
  );

};


// ======================================================
// GET STATUS
// ======================================================

const getFineStatus = (fine) => {

  if (!fine) {
    return "";
  }


  return String(
    fine.status ??
    fine.paymentStatus ??
    ""
  ).trim();

};


// ======================================================
// NORMALIZE FINE
// ======================================================

const normalizeFine = (fine) => {

  const amount =
    getFineAmount(fine);


  const lateDays =
    getLateDays(fine);


  const userName =
    getUserName(fine);


  const bookTitle =
    getBookTitle(fine);


  const status =
    getFineStatus(fine);


  return {

    ...fine,

    // -----------------------------------------------
    // Standard fields used by FineTable
    // -----------------------------------------------

    userName:
      userName || "Unknown User",

    bookTitle:
      bookTitle || "Unknown Book",

    totalAmount:
      amount,

    daysLate:
      lateDays,

    status:
      status || "UNPAID"

  };

};


// ======================================================
// FETCH FINES
// ======================================================

const fetchFines = async () => {

  loading.value = true;

  error.value = "";


  try {

    const data =
      await getFines();


    console.log(
      "Fine API Response:",
      data
    );


    // ==================================================
    // FIND ARRAY
    // ==================================================

    let fineList = [];


    if (
      Array.isArray(data)
    ) {

      fineList = data;

    }

    else if (
      Array.isArray(data?.data)
    ) {

      fineList = data.data;

    }

    else if (
      Array.isArray(data?.content)
    ) {

      fineList = data.content;

    }

    else if (
      Array.isArray(data?.data?.content)
    ) {

      fineList =
        data.data.content;

    }


    // ==================================================
    // NORMALIZE
    // ==================================================

    fines.value =
      fineList.map(
        normalizeFine
      );


    console.log(
      "Normalized Fines:",
      fines.value
    );


  }

  catch (err) {

    console.error(
      "Error fetching fines:",
      err
    );


    error.value =
      err?.response?.data?.message ||
      err?.message ||
      "Failed to load fines.";

  }

  finally {

    loading.value = false;

  }

};


// ======================================================
// SEARCH + STATUS FILTER
// ======================================================

const filteredFines =
  computed(() => {

    const keyword =
      search.value
        .trim()
        .toLowerCase();


    return fines.value.filter(
      (fine) => {

        const userName =
          String(
            getUserName(fine)
          )
            .toLowerCase();


        const bookTitle =
          String(
            getBookTitle(fine)
          )
            .toLowerCase();


        const status =
          getFineStatus(
            fine
          )
            .toLowerCase();


        // ---------------------------------------------
        // Search
        // ---------------------------------------------

        const matchesSearch =
          !keyword ||
          userName.includes(keyword) ||
          bookTitle.includes(keyword);


        // ---------------------------------------------
        // Status
        // ---------------------------------------------

        const matchesStatus =
          selectedStatus.value === "All" ||
          status ===
            selectedStatus.value
              .toLowerCase();


        return (
          matchesSearch &&
          matchesStatus
        );

      }
    );

  });


// ======================================================
// TOTAL FINES
// ======================================================

const totalFines =
  computed(() => {

    return fines.value.reduce(
      (total, fine) => {

        return (
          total +
          getFineAmount(fine)
        );

      },
      0
    );

  });


// ======================================================
// UNPAID FINES
// ======================================================

const unpaidFines =
  computed(() => {

    return fines.value
      .filter((fine) => {

        const status =
          getFineStatus(
            fine
          ).toLowerCase();


        return (
          status === "unpaid"
        );

      })
      .reduce(
        (total, fine) => {

          return (
            total +
            getFineAmount(fine)
          );

        },
        0
      );

  });


// ======================================================
// PAID FINES
// ======================================================

const paidFines =
  computed(() => {

    return fines.value
      .filter((fine) => {

        const status =
          getFineStatus(
            fine
          ).toLowerCase();


        return (
          status === "paid"
        );

      })
      .reduce(
        (total, fine) => {

          return (
            total +
            getFineAmount(fine)
          );

        },
        0
      );

  });


// ======================================================
// TOTAL LATE DAYS
// ======================================================

const totalLateDays =
  computed(() => {

    return fines.value.reduce(
      (total, fine) => {

        return (
          total +
          getLateDays(fine)
        );

      },
      0
    );

  });


// ======================================================
// VIEW FINE
// ======================================================

const viewFine = (fine) => {

  selectedFine.value = {
    ...normalizeFine(fine)
  };

};


// ======================================================
// CLOSE DETAILS
// ======================================================

const closeDetails = () => {

  selectedFine.value =
    null;

};


// ======================================================
// MARK FINE AS PAID
// ======================================================

const markAsPaid =
  async (fine) => {

    if (!fine?.id) {

      alert(
        "Fine ID not found."
      );

      return;

    }


    // ==================================================
    // ALREADY PAID
    // ==================================================

    if (
      getFineStatus(fine)
        .toLowerCase() ===
      "paid"
    ) {

      alert(
        "This fine is already paid."
      );

      return;

    }


    // ==================================================
    // CONFIRM
    // ==================================================

    if (
      !confirm(
        "Are you sure you want to mark this fine as paid?"
      )
    ) {

      return;

    }


    try {

      await markFineAsPaid(
        fine.id
      );


      // ==================================================
      // UPDATE LOCAL
      // ==================================================

      fine.status =
        "PAID";


      fine.paidAt =
        new Date()
          .toISOString();


      // ==================================================
      // UPDATE DETAILS
      // ==================================================

      if (
        selectedFine.value &&
        selectedFine.value.id ===
          fine.id
      ) {

        selectedFine.value = {

          ...selectedFine.value,

          status:
            "PAID",

          paidAt:
            fine.paidAt

        };

      }


      alert(
        "Fine marked as paid successfully."
      );


      // ==================================================
      // REFRESH
      // ==================================================

      await fetchFines();

    }

    catch (err) {

      console.error(
        "Error marking fine as paid:",
        err
      );


      alert(
        err?.response?.data?.message ||
        err?.message ||
        "Failed to mark fine as paid."
      );

    }

  };


// ======================================================
// DELETE FINE
// ======================================================

const deleteFine =
  async (id) => {

    if (!id) {

      alert(
        "Fine ID not found."
      );

      return;

    }


    // ==================================================
    // CONFIRM
    // ==================================================

    if (
      !confirm(
        "Are you sure you want to delete this fine?"
      )
    ) {

      return;

    }


    try {

      await deleteFineApi(
        id
      );


      // ==================================================
      // REMOVE
      // ==================================================

      fines.value =
        fines.value.filter(
          (fine) =>
            fine.id !== id
        );


      // ==================================================
      // CLOSE DETAILS
      // ==================================================

      if (
        selectedFine.value &&
        selectedFine.value.id ===
          id
      ) {

        selectedFine.value =
          null;

      }


      alert(
        "Fine deleted successfully."
      );

    }

    catch (err) {

      console.error(
        "Error deleting fine:",
        err
      );


      alert(
        err?.response?.data?.message ||
        err?.message ||
        "Failed to delete fine."
      );

    }

  };


// ======================================================
// LOAD DATA
// ======================================================

onMounted(() => {

  fetchFines();

});

</script>


<style scoped>

/* ======================================================
   PAGE
====================================================== */

.fines-page {
  min-height: 100vh;

  background: #f8fafc;

  padding: 60px;
}


/* ======================================================
   HEADER
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
   ERROR
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
   LOADING
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

  animation:
    spin 0.8s linear infinite;
}


@keyframes spin {

  to {
    transform: rotate(360deg);
  }

}


/* ======================================================
   SUMMARY GRID
====================================================== */

.summary-grid {
  display: grid;

  grid-template-columns:
    repeat(4, minmax(0, 1fr));

  gap: 20px;

  margin-bottom: 25px;
}


/* ======================================================
   SUMMARY CARD
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
    0 1px 2px
    rgba(15, 23, 42, 0.04);
}


.summary-icon {
  width: 48px;

  height: 48px;

  flex-shrink: 0;

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

  word-break: break-word;
}


/* ======================================================
   TOOLBAR
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
   SEARCH
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

  transform:
    translateY(-50%);

  color: #94a3b8;

  font-size: 15px;
}


.search-box input {
  width: 100%;

  box-sizing: border-box;

  padding:
    11px
    14px
    11px
    40px;

  border:
    1px solid
    #cbd5e1;

  border-radius: 8px;

  outline: none;

  font-size: 14px;

  color: #334155;

  background: white;
}


.search-box input:focus {
  border-color: #2563eb;

  box-shadow:
    0 0 0 3px
    rgba(37, 99, 235, 0.1);
}


.search-box input::placeholder {
  color: #94a3b8;
}


/* ======================================================
   STATUS FILTER
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

  padding:
    10px
    12px;

  border:
    1px solid
    #cbd5e1;

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
    0 0 0 3px
    rgba(37, 99, 235, 0.1);
}


/* ======================================================
   RESPONSIVE
====================================================== */

@media (max-width: 1100px) {

  .summary-grid {
    grid-template-columns:
      repeat(2, minmax(0, 1fr));
  }

}


/* ======================================================
   TABLET / MOBILE
====================================================== */

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


/* ======================================================
   SMALL MOBILE
====================================================== */

@media (max-width: 520px) {

  .summary-grid {
    grid-template-columns: 1fr;
  }


  .fines-page {
    padding: 15px;
  }


  .summary-card {
    padding: 16px;
  }


  .summary-icon {
    width: 44px;

    height: 44px;

    font-size: 19px;
  }


  .summary-content h2 {
    font-size: 20px;
  }

}

</style>
