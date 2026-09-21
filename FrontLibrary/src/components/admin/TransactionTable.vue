
<template>
  <div class="card recent-card">

    <!-- =========================
         HEADER
    ========================== -->
    <div class="card-header">

      <div>
        <h3>Recent Borrowings</h3>
        <p>Latest book borrowing activities</p>
      </div>

      <!-- View All -->
      <button
        type="button"
        class="view-all-btn"
        @click="goToBorrower"
      >
        View All →
      </button>

    </div>


    <!-- =========================
         LOADING
    ========================== -->
    <div
      v-if="loading"
      class="state-container"
    >

      <div class="loading-spinner"></div>

      <span>Loading borrowings...</span>

    </div>


    <!-- =========================
         ERROR
    ========================== -->
    <div
      v-else-if="error"
      class="state-container error-state"
    >

      <i class="bi bi-exclamation-circle"></i>

      <span>{{ error }}</span>

    </div>


    <!-- =========================
         EMPTY
    ========================== -->
    <div
      v-else-if="recentBorrowings.length === 0"
      class="state-container empty-state"
    >

      <i class="bi bi-book"></i>

      <span>No recent borrowings found.</span>

    </div>


    <!-- =========================
         TABLE
    ========================== -->
    <div
      v-else
      class="table-container"
    >

      <table>

        <thead>

          <tr>
            <th>USER</th>
            <th>BOOK</th>
            <th>DATE</th>
            <th>STATUS</th>
          </tr>

        </thead>


        <tbody>

          <tr
            v-for="borrow in recentBorrowings"
            :key="borrow.id"
          >

            <!-- =========================
                 USER
            ========================== -->
            <td>

              <div class="user-cell">

                <div class="user-avatar">
                  {{ getInitial(borrow.user) }}
                </div>

                <span>
                  {{ borrow.user }}
                </span>

              </div>

            </td>


            <!-- =========================
                 BOOK
            ========================== -->
            <td>

              <span class="book-title">
                {{ borrow.book }}
              </span>

            </td>


            <!-- =========================
                 DATE
            ========================== -->
            <td>

              <span class="borrow-date">
                {{ borrow.date }}
              </span>

            </td>


            <!-- =========================
                 STATUS
            ========================== -->
            <td>

              <span
                class="status"
                :class="getStatusClass(borrow.status)"
              >
                {{ formatStatus(borrow.status) }}
              </span>

            </td>

          </tr>

        </tbody>

      </table>

    </div>

  </div>
</template>


<script setup>

import { ref, onMounted } from "vue";

import { useRouter } from "vue-router";

import { getBorrowings } from "../../service/borrowingService";


// =====================================================
// ROUTER
// =====================================================

const router = useRouter();


// =====================================================
// STATE
// =====================================================

const recentBorrowings = ref([]);

const loading = ref(false);

const error = ref("");


// =====================================================
// GO TO BORROWER PAGE
// =====================================================

const goToBorrower = () => {

  console.log("Going to Admin Borrower page...");

  router.push("/admin/borrower");

};


// =====================================================
// FETCH BORROWINGS
// =====================================================

const fetchRecentBorrowings = async () => {

  loading.value = true;

  error.value = "";

  try {

    const response = await getBorrowings();

    console.log(
      "Recent Borrowings API Response:",
      response
    );


    // =================================================
    // HANDLE DIFFERENT RESPONSE STRUCTURES
    // =================================================

    let borrowings = [];


    // -----------------------------------------------
    // Case 1:
    // getBorrowings() returns array directly
    // -----------------------------------------------

    if (Array.isArray(response)) {

      borrowings = response;

    }


    // -----------------------------------------------
    // Case 2:
    // response.data is array
    // -----------------------------------------------

    else if (
      response &&
      Array.isArray(response.data)
    ) {

      borrowings = response.data;

    }


    // -----------------------------------------------
    // Case 3:
    // response.data.data is array
    // -----------------------------------------------

    else if (
      response &&
      response.data &&
      Array.isArray(response.data.data)
    ) {

      borrowings = response.data.data;

    }


    // -----------------------------------------------
    // Case 4:
    // response.result is array
    // -----------------------------------------------

    else if (
      response &&
      Array.isArray(response.result)
    ) {

      borrowings = response.result;

    }


    // -----------------------------------------------
    // Case 5:
    // response.data.result is array
    // -----------------------------------------------

    else if (
      response &&
      response.data &&
      Array.isArray(response.data.result)
    ) {

      borrowings = response.data.result;

    }


    // =================================================
    // NO DATA
    // =================================================

    if (!Array.isArray(borrowings)) {

      borrowings = [];

    }


    console.log(
      "Borrowings Array:",
      borrowings
    );


    // =================================================
    // SORT NEWEST FIRST
    // =================================================

    borrowings.sort((a, b) => {

      const dateA = new Date(
        a.createdAt ||
        a.borrowDate ||
        a.date ||
        0
      ).getTime();


      const dateB = new Date(
        b.createdAt ||
        b.borrowDate ||
        b.date ||
        0
      ).getTime();


      return dateB - dateA;

    });


    // =================================================
    // GET ONLY 4 RECENT BORROWINGS
    // =================================================

    recentBorrowings.value = borrowings
      .slice(0, 4)
      .map((borrow) => {

        return {

          id: borrow.id,

          user:
            borrow.userName ||
            borrow.user?.name ||
            borrow.user?.username ||
            borrow.borrowerName ||
            borrow.borrower?.name ||
            "Unknown User",

          book:
            borrow.bookTitle ||
            borrow.book?.title ||
            borrow.bookName ||
            "Unknown Book",

          date:
            formatDate(
              borrow.borrowDate ||
              borrow.createdAt ||
              borrow.date
            ),

          status:
            normalizeStatus(
              borrow.status
            )

        };

      });


    console.log(
      "Recent Borrowings Display:",
      recentBorrowings.value
    );

  } catch (err) {

    console.error(
      "Failed to fetch recent borrowings:",
      err
    );

    error.value =
      "Failed to load recent borrowings.";

  } finally {

    loading.value = false;

  }

};


// =====================================================
// FORMAT DATE
// =====================================================

const formatDate = (value) => {

  if (!value) {

    return "-";

  }


  const date = new Date(value);


  if (Number.isNaN(date.getTime())) {

    return "-";

  }


  return date.toLocaleDateString(
    "en-US",
    {
      month: "short",
      day: "2-digit",
      year: "numeric"
    }
  );

};


// =====================================================
// NORMALIZE STATUS
// =====================================================

const normalizeStatus = (status) => {

  if (!status) {

    return "UNKNOWN";

  }


  return String(status)
    .trim()
    .toUpperCase();

};


// =====================================================
// FORMAT STATUS
// =====================================================

const formatStatus = (status) => {

  if (!status) {

    return "Unknown";

  }


  return String(status)
    .toLowerCase()
    .replace(/_/g, " ")
    .replace(/-/g, " ")
    .replace(/\b\w/g, (char) => {

      return char.toUpperCase();

    });

};


// =====================================================
// STATUS CLASS
// =====================================================

const getStatusClass = (status) => {

  const value = String(status || "")
    .toLowerCase()
    .replace(/_/g, "-")
    .replace(/\s+/g, "-");


  return {

    borrowed:
      value === "borrowed",

    returned:
      value === "returned",

    pending:
      value === "pending",

    rejected:
      value === "rejected",

    overdue:
      value === "overdue",

    "return-requested":
      value === "return-requested"

  };

};


// =====================================================
// USER INITIAL
// =====================================================

const getInitial = (name) => {

  if (!name) {

    return "?";

  }


  return String(name)
    .trim()
    .charAt(0)
    .toUpperCase();

};


// =====================================================
// LOAD DATA
// =====================================================

onMounted(() => {

  fetchRecentBorrowings();

});

</script>


<style scoped>

/* =====================================================
   CARD
===================================================== */

.card {
  min-width: 0;

  background: white;

  border: 1px solid #e6e9ef;

  border-radius: 12px;

  box-shadow:
    0 2px 5px rgba(0, 0, 0, 0.02);

  overflow: hidden;
}


/* =====================================================
   HEADER
===================================================== */

.card-header {
  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 15px;

  padding: 20px;

  border-bottom: 1px solid #f0f1f4;
}


.card-header h3 {
  margin: 0;

  font-size: 16px;

  color: #1f2937;
}


.card-header p {
  margin: 5px 0 0;

  font-size: 12px;

  color: #8a92a3;
}


/* =====================================================
   VIEW ALL BUTTON
===================================================== */

.view-all-btn {
  border: none;

  outline: none;

  background: transparent;

  color: #5b3df5;

  padding: 0;

  font-size: 13px;

  font-weight: 600;

  white-space: nowrap;

  cursor: pointer;

  transition:
    color 0.2s ease,
    transform 0.2s ease;
}


.view-all-btn:hover {
  color: #4528d8;

  transform: translateX(2px);
}


.view-all-btn:focus {
  outline: none;
}


/* =====================================================
   TABLE
===================================================== */

.table-container {
  width: 100%;

  overflow-x: auto;
}


table {
  width: 100%;

  min-width: 500px;

  border-collapse: collapse;
}


th {
  text-align: left;

  padding: 13px 20px;

  background: #fafbfc;

  color: #8a92a3;

  font-size: 10px;

  font-weight: 700;

  letter-spacing: 0.4px;
}


td {
  padding: 13px 20px;

  border-top: 1px solid #f0f1f4;

  color: #4b5565;

  font-size: 12px;
}


tbody tr {
  transition:
    background 0.2s ease;
}


tbody tr:hover {
  background: #fafbff;
}


/* =====================================================
   USER
===================================================== */

.user-cell {
  display: flex;

  align-items: center;

  gap: 8px;

  font-weight: 600;

  color: #374151;
}


.user-avatar {
  width: 30px;

  height: 30px;

  flex-shrink: 0;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  background: #eeeaff;

  color: #5b3df5;

  font-size: 11px;

  font-weight: 700;
}


/* =====================================================
   BOOK
===================================================== */

.book-title {
  color: #374151;

  font-weight: 500;
}


/* =====================================================
   DATE
===================================================== */

.borrow-date {
  color: #6b7280;

  white-space: nowrap;
}


/* =====================================================
   STATUS
===================================================== */

.status {
  display: inline-flex;

  align-items: center;

  justify-content: center;

  padding: 5px 9px;

  border-radius: 5px;

  font-size: 10px;

  font-weight: 600;

  white-space: nowrap;
}


/* Borrowed */

.status.borrowed {
  background: #fff2d8;

  color: #b77900;
}


/* Returned */

.status.returned {
  background: #e5f8ed;

  color: #16864a;
}


/* Pending */

.status.pending {
  background: #e9f2ff;

  color: #2874d8;
}


/* Rejected */

.status.rejected {
  background: #ffe7e7;

  color: #d63c3c;
}


/* Overdue */

.status.overdue {
  background: #ffe4e4;

  color: #c62828;
}


/* Return Requested */

.status.return-requested {
  background: #f1e9ff;

  color: #7442c8;
}


/* =====================================================
   STATE
===================================================== */

.state-container {
  min-height: 180px;

  display: flex;

  align-items: center;

  justify-content: center;

  flex-direction: column;

  gap: 10px;

  color: #8a92a3;

  font-size: 12px;
}


.state-container i {
  font-size: 24px;
}


.error-state {
  color: #d63c3c;
}


.empty-state {
  color: #8a92a3;
}


/* =====================================================
   LOADING SPINNER
===================================================== */

.loading-spinner {
  width: 25px;

  height: 25px;

  border: 3px solid #eeeaff;

  border-top-color: #5b3df5;

  border-radius: 50%;

  animation: spin 0.8s linear infinite;
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
   MOBILE
===================================================== */

@media (max-width: 640px) {

  .card-header {
    padding: 16px;
  }


  .card-header h3 {
    font-size: 15px;
  }


  .card-header p {
    font-size: 11px;
  }


  .view-all-btn {
    font-size: 12px;
  }


  th {
    padding: 12px 16px;
  }


  td {
    padding: 12px 16px;
  }

}

</style>
