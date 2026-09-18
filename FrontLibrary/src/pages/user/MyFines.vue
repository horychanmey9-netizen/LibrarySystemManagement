<template>
  <div class="my-fines-page">

    <!-- ================= HEADER ================= -->
    <div class="page-header">
      <div>
        <h1>My Fines</h1>

        <p>
          View your fines and payment status
        </p>
      </div>
    </div>


    <!-- ================= SUMMARY ================= -->
    <div class="summary-grid">

      <!-- Total Fines -->
      <div class="summary-card">

        <div class="summary-icon total-icon">
          <i class="bi bi-cash-coin"></i>
        </div>

        <div class="summary-content">
          <span>Total Fines</span>

          <strong>
            ${{ totalFines.toFixed(2) }}
          </strong>
        </div>

      </div>


      <!-- Unpaid Fines -->
      <div class="summary-card">

        <div class="summary-icon unpaid-icon">
          <i class="bi bi-exclamation-triangle"></i>
        </div>

        <div class="summary-content">
          <span>Unpaid Fines</span>

          <strong>
            ${{ unpaidFines.toFixed(2) }}
          </strong>
        </div>

      </div>


      <!-- Paid Fines -->
      <div class="summary-card">

        <div class="summary-icon paid-icon">
          <i class="bi bi-check-circle"></i>
        </div>

        <div class="summary-content">
          <span>Paid Fines</span>

          <strong>
            ${{ paidFines.toFixed(2) }}
          </strong>
        </div>

      </div>

    </div>


    <!-- ================= FINE HISTORY ================= -->
    <section class="fines-section">

      <div class="section-header">

        <div>
          <h2>Fine History</h2>

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

        <div class="empty-icon loading-icon">
          <i class="bi bi-arrow-repeat"></i>
        </div>

        <h3>Loading Fines...</h3>

        <p>
          Please wait while we load your fine history.
        </p>

      </div>


      <!-- ================= ERROR ================= -->
      <div
        v-else-if="error"
        class="empty-state"
      >

        <div class="empty-icon error-icon">
          <i class="bi bi-exclamation-circle"></i>
        </div>

        <h3>Unable to Load Fines</h3>

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

        <h3>No Fines</h3>

        <p>
          You don't have any library fines.
        </p>

      </div>

    </section>

  </div>
</template>


<script>

import FineCard from "../../components/user/FineTemp.vue";

import { getMyFines }
  from "../../service/userfineService";


export default {

  name: "MyFines",


  components: {

    FineCard

  },


  data() {

    return {

      /*
       * =========================
       * Fine Data
       * =========================
       */

      fines: [],


      /*
       * =========================
       * Loading
       * =========================
       */

      loading: false,


      /*
       * =========================
       * Error
       * =========================
       */

      error: null

    };

  },


  /*
   * =========================
   * Mounted
   * =========================
   */

  mounted() {

    this.fetchMyFines();

  },


  /*
   * =========================
   * Computed
   * =========================
   */

  computed: {


    /*
     * =========================
     * Total Fines
     * =========================
     */

    totalFines() {

      return this.fines.reduce(

        (total, fine) => {

          return total + Number(
            fine.amount || 0
          );

        },

        0

      );

    },


    /*
     * =========================
     * Unpaid Fines
     * =========================
     */

    unpaidFines() {

      return this.fines

        .filter(

          fine =>
            String(fine.status)
              .toUpperCase() === "UNPAID"

        )

        .reduce(

          (total, fine) => {

            return total + Number(
              fine.amount || 0
            );

          },

          0

        );

    },


    /*
     * =========================
     * Paid Fines
     * =========================
     */

    paidFines() {

      return this.fines

        .filter(

          fine =>
            String(fine.status)
              .toUpperCase() === "PAID"

        )

        .reduce(

          (total, fine) => {

            return total + Number(
              fine.amount || 0
            );

          },

          0

        );

    }

  },


  /*
   * =========================
   * Methods
   * =========================
   */

  methods: {


    /*
     * =========================
     * Fetch My Fines
     * =========================
     */

    async fetchMyFines() {

      this.loading = true;

      this.error = null;


      try {

        console.log(
          "Fetching my fines..."
        );


        /*
         * Get data from service
         */

        const data = await getMyFines();


        console.log(
          "My fines response:",
          data
        );


        /*
         * Make sure data is array
         */

        if (!Array.isArray(data)) {

          console.warn(
            "Fine data is not an array:",
            data
          );

          this.fines = [];

          return;

        }


        /*
         * =========================
         * Format API Data
         * =========================
         */

        this.fines = data.map(

          fine => this.formatFine(fine)

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
          error.message ||
          "Unable to load fines.";


        this.fines = [];

      }


      finally {

        this.loading = false;

      }

    },


    /*
     * =========================
     * Format Fine
     * =========================
     */

    formatFine(fine) {

      return {

        /*
         * Fine ID
         */

        id:
          fine.id,


        /*
         * Book ID
         */

        bookId:
          fine.bookId,


        /*
         * Book Title
         */

        title:
          fine.title ||
          fine.bookTitle ||
          "Unknown Book",


        /*
         * Author
         */

        author:
          fine.author ||
          fine.bookAuthor ||
          "Unknown Author",


        /*
         * Book Image
         */

        image:
          fine.image ||
          fine.bookImage ||
          null,


        /*
         * Category
         */

        category:
          fine.category ||
          fine.categoryName ||
          null,


        /*
         * ISBN
         */

        isbn:
          fine.isbn ||
          fine.bookIsbn ||
          null,


        /*
         * Due Date
         */

        dueDate:
          this.formatDate(
            fine.dueDate
          ),


        /*
         * Returned Date
         */

        returnedDate:
          this.formatDate(

            fine.returnedDate ||
            fine.returnDate

          ),


        /*
         * Late Days
         */

        lateDays:
          Number(

            fine.lateDays ||
            fine.lateDay ||
            0

          ),


        /*
         * Fine Amount
         */

        amount:
          Number(

            fine.amount ||
            fine.fine ||
            0

          ),


        /*
         * Status
         */

        status:
          fine.status ||
          "Unpaid"

      };

    },


    /*
     * =========================
     * Format Date
     * =========================
     */

    formatDate(date) {

      if (!date) {

        return "-";

      }


      return String(date)
        .split("T")[0];

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

  padding:
    1.5rem
    2rem;

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



/* Total */

.total-icon {

  background: #fef2f2;

  color: #ef4444;

}


/* Unpaid */

.unpaid-icon {

  background: #fff7ed;

  color: #f97316;

}


/* Paid */

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
   RESPONSIVE
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


@media (max-width: 700px) {

  .my-fines-page {

    padding: 16px;

  }


  .summary-grid {

    grid-template-columns: 1fr;

  }


  .section-header {

    align-items: flex-start;

    flex-direction: column;

    gap: 8px;

  }

}

</style>