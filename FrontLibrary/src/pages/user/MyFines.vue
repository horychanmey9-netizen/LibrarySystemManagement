<template>

  <div class="my-fines-page">

    <!-- ================= HEADER ================= -->

    <div class="page-header">

      <div>

        <h1>
          My Fines
        </h1>

        <p>
          View your fines and payment status
        </p>

      </div>

    </div>


    <!-- ================= SUMMARY ================= -->

    <div class="summary-grid">

      <!-- ================= TOTAL FINES ================= -->

      <div class="summary-card">

        <div
          class="summary-icon total-icon"
        >

          <i class="bi bi-cash-coin"></i>

        </div>


        <div class="summary-content">

          <span>
            Total Fines
          </span>

          <strong>
            {{ totalFines.toFixed(2) }}៛
          </strong>

        </div>

      </div>


      <!-- ================= UNPAID ================= -->

      <div class="summary-card">

        <div
          class="summary-icon unpaid-icon"
        >

          <i
            class="bi bi-exclamation-triangle"
          ></i>

        </div>


        <div class="summary-content">

          <span>
            Unpaid Fines
          </span>

          <strong>
            {{ unpaidFines.toFixed(2) }}៛
          </strong>

        </div>

      </div>


      <!-- ================= PAID ================= -->

      <div class="summary-card">

        <div
          class="summary-icon paid-icon"
        >

          <i
            class="bi bi-check-circle"
          ></i>

        </div>


        <div class="summary-content">

          <span>
            Paid Fines
          </span>

          <strong>
            {{ paidFines.toFixed(2) }}៛
          </strong>

        </div>

      </div>

    </div>


    <!-- ================= FINE HISTORY ================= -->

    <section class="fines-section">

      <div class="section-header">

        <div>

          <h2>
            Fine History
          </h2>

          <p>
            Fines from your borrowed books
          </p>

        </div>


        <span class="record-count">

          {{ fines.length }} records

        </span>

      </div>


      <!-- ================= LOADING ================= -->

      <div
        v-if="loading"
        class="empty-state"
      >

        <div
          class="empty-icon loading-icon"
        >

          <i class="bi bi-arrow-repeat"></i>

        </div>


        <h3>
          Loading Fines...
        </h3>


        <p>
          Please wait while we load your fine history.
        </p>

      </div>


      <!-- ================= ERROR ================= -->

      <div
        v-else-if="error"
        class="empty-state"
      >

        <div
          class="empty-icon error-icon"
        >

          <i
            class="bi bi-exclamation-circle"
          ></i>

        </div>


        <h3>
          Unable to Load Fines
        </h3>


        <p>
          {{ error }}
        </p>


        <button
          class="retry-button"
          @click="fetchMyFines"
        >

          Try Again

        </button>

      </div>


      <!-- ================= FINE CARDS ================= -->

      <div
        v-else-if="fines.length > 0"
        class="fine-list"
      >

        <FineCard
          v-for="fine in fines"
          :key="fine.id"
          :fine="fine"
        />

      </div>


      <!-- ================= EMPTY STATE ================= -->

      <div
        v-else
        class="empty-state"
      >

        <div class="empty-icon">

          <i class="bi bi-cash-stack"></i>

        </div>


        <h3>
          No Fines
        </h3>


        <p>
          You don't have any library fines.
        </p>

      </div>

    </section>

  </div>

</template>


<script>

import FineCard
  from "../../components/user/FineTemp.vue";

import {
  getMyFines
} from "../../service/FineService";


export default {

  name: "MyFines",


  components: {

    FineCard

  },


  data() {

    return {

      // =============================================
      // FINE DATA
      // =============================================

      fines: [],


      // =============================================
      // LOADING
      // =============================================

      loading: false,


      // =============================================
      // ERROR
      // =============================================

      error: null

    };

  },


  // ===============================================
  // MOUNTED
  // ===============================================

  mounted() {

    this.fetchMyFines();

  },


  // ===============================================
  // COMPUTED
  // ===============================================

  computed: {


    // =============================================
    // TOTAL FINES
    // =============================================

    totalFines() {

      return this.fines.reduce(

        (total, fine) => {

          return (
            total +
            Number(
              fine.totalAmount || 0
            )
          );

        },

        0

      );

    },


    // =============================================
    // UNPAID FINES
    // =============================================

    unpaidFines() {

      return this.fines

        .filter(

          fine =>

            String(
              fine.status || ""
            ).toUpperCase() ===
            "UNPAID"

        )

        .reduce(

          (total, fine) => {

            return (
              total +
              Number(
                fine.totalAmount || 0
              )
            );

          },

          0

        );

    },


    // =============================================
    // PAID FINES
    // =============================================

    paidFines() {

      return this.fines

        .filter(

          fine =>

            String(
              fine.status || ""
            ).toUpperCase() ===
            "PAID"

        )

        .reduce(

          (total, fine) => {

            return (
              total +
              Number(
                fine.totalAmount || 0
              )
            );

          },

          0

        );

    }

  },


  // ===============================================
  // METHODS
  // ===============================================

  methods: {


    // =============================================
    // FETCH MY FINES
    // =============================================

    async fetchMyFines() {

      this.loading = true;

      this.error = null;


      try {

        console.log(
          "Fetching my fines..."
        );


        const data =
          await getMyFines();


        console.log(
          "My fines response:",
          data
        );


        // =========================================
        // CHECK DATA
        // =========================================

        if (!Array.isArray(data)) {

          console.warn(
            "Fine data is not an array:",
            data
          );

          this.fines = [];

          return;

        }


        // =========================================
        // FORMAT DATA
        // =========================================

        this.fines = data.map(

          fine =>
            this.formatFine(fine)

        );


        console.log(
          "Formatted my fines:",
          this.fines
        );

      }


      catch (error) {

        console.error(
          "Fetch my fines error:",
          error
        );


        this.error =

          error?.message ||

          "Unable to load fines.";


        this.fines = [];

      }


      finally {

        this.loading = false;

      }

    },


    // =============================================
    // FORMAT FINE
    // =============================================

    formatFine(fine) {

      return {

        // =========================================
        // FINE ID
        // =========================================

        id:
          fine?.id || null,


        // =========================================
        // USER
        // =========================================

        userId:
          fine?.userId || null,


        userName:
          fine?.userName ||
          "Unknown User",


        // =========================================
        // BOOK
        // =========================================

        bookId:
          fine?.bookId || null,


        title:
          fine?.bookTitle ||
          fine?.title ||
          "Unknown Book",


        author:
          fine?.author ||
          fine?.bookAuthor ||
          "Unknown Author",


        image:
          fine?.image ||
          fine?.bookImage ||
          null,


        category:
          fine?.category ||
          fine?.categoryName ||
          null,


        isbn:
          fine?.isbn ||
          fine?.bookIsbn ||
          null,


        // =========================================
        // DUE DATE
        // =========================================

        dueDate:

          this.formatDate(
            fine?.dueDate
          ),


        // =========================================
        // RETURNED DATE
        // =========================================

        returnedDate:

          this.formatDate(

            fine?.returnedDate ||

            fine?.returnDate

          ),


        // =========================================
        // LATE DAYS
        // =========================================

        daysLate:

          Number(
            fine?.daysLate || 0
          ),


        // =========================================
        // TOTAL AMOUNT
        // =========================================

        totalAmount:

          Number(
            fine?.totalAmount || 0
          ),


        // =========================================
        // STATUS
        // =========================================

        status:

          fine?.status ||
          "UNPAID",


        // =========================================
        // PAID AT
        // =========================================

        paidAt:

          fine?.paidAt || null,


        // =========================================
        // CREATED AT
        // =========================================

        createdAt:

          fine?.createdAt || null,


        // =========================================
        // UPDATED AT
        // =========================================

        updatedAt:

          fine?.updatedAt || null

      };

    },


    // =============================================
    // FORMAT DATE
    // =============================================

    formatDate(date) {

      if (!date) {

        return "-";

      }


      const value =
        String(date);


      // =========================================
      // ISO DATE
      // =========================================

      if (
        value.includes("T")
      ) {

        return value.split("T")[0];

      }


      return value;

    }

  }

};

</script>


<style scoped>

/* ==================================================
   PAGE
================================================== */

.my-fines-page {

  width: 100%;

  min-height: 100%;

  padding: 20px 90px;

  box-sizing: border-box;

  background: #f8faff;

}


/* ==================================================
   HEADER
================================================== */

.page-header {

  margin-bottom: 24px;

}


.page-header h1 {

  margin: 0;

  font-size: 26px;

  font-weight: 700;

  color: #1f2937;

}


.page-header p {

  margin: 6px 0 0;

  font-size: 14px;

  color: #6b7280;

}


/* ==================================================
   SUMMARY
================================================== */

.summary-grid {

  display: grid;

  grid-template-columns:
    repeat(3, minmax(0, 1fr));

  gap: 16px;

  margin-bottom: 30px;

}


.summary-card {

  display: flex;

  align-items: center;

  gap: 14px;

  min-width: 0;

  background: #ffffff;

  border: 1px solid #e5e7eb;

  border-radius: 14px;

  padding: 18px;

  box-sizing: border-box;

}


.summary-icon {

  width: 48px;

  height: 48px;

  display: flex;

  align-items: center;

  justify-content: center;

  flex-shrink: 0;

  border-radius: 11px;

}


.summary-icon i {

  font-size: 21px;

}


/* ==================================================
   TOTAL
================================================== */

.total-icon {

  background: #fef2f2;

  color: #ef4444;

}


/* ==================================================
   UNPAID
================================================== */

.unpaid-icon {

  background: #fff7ed;

  color: #f97316;

}


/* ==================================================
   PAID
================================================== */

.paid-icon {

  background: #f0fdf4;

  color: #22c55e;

}


/* ==================================================
   SUMMARY CONTENT
================================================== */

.summary-content {

  min-width: 0;

}


.summary-content span {

  display: block;

  margin-bottom: 5px;

  font-size: 13px;

  color: #6b7280;

}


.summary-content strong {

  display: block;

  font-size: 21px;

  font-weight: 700;

  color: #1f2937;

}


/* ==================================================
   FINE SECTION
================================================== */

.fines-section {

  width: 100%;

}


.section-header {

  display: flex;

  align-items: flex-end;

  justify-content: space-between;

  gap: 20px;

  margin-bottom: 14px;

}


.section-header h2 {

  margin: 0;

  font-size: 19px;

  font-weight: 600;

  color: #1f2937;

}


.section-header p {

  margin: 4px 0 0;

  font-size: 13px;

  color: #9ca3af;

}


.record-count {

  flex-shrink: 0;

  font-size: 13px;

  color: #9ca3af;

}


/* ==================================================
   FINE LIST
================================================== */

.fine-list {

  width: 100%;

  display: flex;

  flex-direction: column;

  gap: 14px;

}


/* ==================================================
   EMPTY / LOADING / ERROR
================================================== */

.empty-state {

  width: 100%;

  box-sizing: border-box;

  padding: 60px 20px;

  text-align: center;

  background: #ffffff;

  border: 1px solid #e5e7eb;

  border-radius: 14px;

}


.empty-icon {

  font-size: 48px;

  color: #94a3b8;

}


.loading-icon {

  color: #2563eb;

  animation:
    spin 1s linear infinite;

}


.error-icon {

  color: #ef4444;

}


@keyframes spin {

  from {

    transform: rotate(0deg);

  }

  to {

    transform: rotate(360deg);

  }

}


/* ==================================================
   EMPTY TEXT
================================================== */

.empty-state h3 {

  margin:
    12px
    0
    5px;

  font-size: 18px;

  font-weight: 600;

  color: #374151;

}


.empty-state p {

  margin: 0;

  font-size: 14px;

  color: #9ca3af;

}


/* ==================================================
   RETRY BUTTON
================================================== */

.retry-button {

  margin-top: 18px;

  padding:
    9px
    18px;

  border: none;

  border-radius: 8px;

  background: #2563eb;

  color: #ffffff;

  font-size: 13px;

  font-weight: 500;

  cursor: pointer;

  transition:
    background 0.2s ease;

}


.retry-button:hover {

  background: #1d4ed8;

}


/* ==================================================
   BROWSE BUTTON
================================================== */

.browse-button {

  display: inline-block;

  margin-top: 18px;

  padding:
    9px
    18px;

  border-radius: 8px;

  background: #2563eb;

  color: #ffffff;

  font-size: 13px;

  font-weight: 500;

  text-decoration: none;

  transition:
    background 0.2s ease;

}


.browse-button:hover {

  background: #1d4ed8;

}


/* ==================================================
   TABLET
================================================== */

@media (max-width: 1000px) {

  .summary-grid {

    grid-template-columns:
      repeat(
        2,
        minmax(0, 1fr)
      );

  }

}


/* ==================================================
   MOBILE
================================================== */

@media (max-width: 700px) {

  .my-fines-page {

    padding: 12px;

  }


  /* ---------------- HEADER ---------------- */

  .page-header {

    margin-bottom: 16px;

  }


  .page-header h1 {

    font-size: 22px;

  }


  .page-header p {

    margin-top: 4px;

    font-size: 12px;

  }


  /* ---------------- SUMMARY ---------------- */

  .summary-grid {

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

    gap: 8px;

    margin-bottom: 22px;

  }


  .summary-card {

    gap: 9px;

    min-height: 68px;

    padding: 11px;

    border-radius: 11px;

  }


  /* Third card full width */

  .summary-card:nth-child(3) {

    grid-column:
      1 / -1;

  }


  .summary-icon {

    width: 34px;

    height: 34px;

    border-radius: 8px;

  }


  .summary-icon i {

    font-size: 15px;

  }


  .summary-content span {

    margin-bottom: 2px;

    font-size: 11px;

    line-height: 1.2;

  }


  .summary-content strong {

    font-size: 16px;

    line-height: 1.2;

  }


  /* ---------------- SECTION HEADER ---------------- */

  .section-header {

    align-items: center;

    flex-direction: row;

    gap: 8px;

    margin-bottom: 10px;

  }


  .section-header h2 {

    font-size: 17px;

  }


  .section-header p {

    margin-top: 3px;

    font-size: 11px;

  }


  .record-count {

    font-size: 11px;

    white-space: nowrap;

  }


  /* ---------------- FINE LIST ---------------- */

  .fine-list {

    gap: 10px;

  }


  /* ---------------- EMPTY ---------------- */

  .empty-state {

    padding: 40px 14px;

    border-radius: 11px;

  }


  .empty-icon {

    font-size: 38px;

  }


  .empty-state h3 {

    margin-top: 9px;

    font-size: 16px;

  }


  .empty-state p {

    font-size: 12px;

    line-height: 1.5;

  }


  .retry-button {

    margin-top: 14px;

    padding:
      8px
      15px;

    font-size: 12px;

  }

}


/* ==================================================
   SMALL MOBILE
================================================== */

@media (max-width: 400px) {

  .my-fines-page {

    padding: 10px;

  }


  .summary-grid {

    gap: 7px;

  }


  .summary-card {

    min-height: 62px;

    padding: 9px;

    gap: 7px;

  }


  .summary-icon {

    width: 30px;

    height: 30px;

  }


  .summary-icon i {

    font-size: 13px;

  }


  .summary-content span {

    font-size: 10px;

  }


  .summary-content strong {

    font-size: 14px;

  }


  .section-header h2 {

    font-size: 16px;

  }


  .section-header p {

    font-size: 10px;

  }


  .record-count {

    font-size: 10px;

  }

}

</style>