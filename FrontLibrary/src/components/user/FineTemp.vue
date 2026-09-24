<template>
  <div class="fine-card">

    <!-- =========================
         BOOK
    ========================== -->
    <div class="book-info">

      <div class="book-cover">
        <img
          v-if="getBookImage(fine)"
          :src="getBookImage(fine)"
          :alt="getBookTitle(fine)"
        />

        <div v-else class="no-cover">
          <i class="bi bi-book"></i>
        </div>
      </div>

      <div class="book-details">

        <h3>
          {{ getBookTitle(fine) }}
        </h3>

        <p
          v-if="getUserName(fine)"
          class="user-name"
        >
          <i class="bi bi-person"></i>
          {{ getUserName(fine) }}
        </p>

        <p
          v-if="getCategory(fine)"
          class="category"
        >
          {{ getCategory(fine) }}
        </p>

        <p
          v-if="getISBN(fine)"
          class="isbn"
        >
          ISBN: {{ getISBN(fine) }}
        </p>

      </div>

    </div>


    <!-- =========================
         FINE DETAILS
    ========================== -->
    <div class="fine-details">

      <div class="detail">
        <span>Due Date</span>

        <strong>
          {{ getDueDate(fine) }}
        </strong>
      </div>


      <div class="detail">
        <span>Returned Date</span>

        <strong>
          {{ getReturnedDate(fine) }}
        </strong>
      </div>


      <div class="detail">
        <span>Late</span>

        <strong>
          {{ getLateDays(fine) }}
          {{ getLateDays(fine) === 1 ? "day" : "days" }}
        </strong>
      </div>

    </div>


    <!-- =========================
         RESULT
    ========================== -->
    <div class="fine-result">

      <span
        class="status"
        :class="getStatusClass(fine)"
      >
        {{ getStatus(fine) }}
      </span>

      <span class="amount">
        ៛{{ formatAmount(getFineAmount(fine)) }}
      </span>

    </div>

  </div>
</template>


<script>
export default {
  name: "FineCard",

  props: {
    fine: {
      type: Object,
      required: true
    }
  },

  methods: {

    /* =========================
       USER
    ========================== */
    getUserName(fine) {

      if (!fine) {
        return "Unknown User";
      }

      return (
        fine.userName ||
        fine.username ||
        fine.borrowerName ||
        fine.name ||
        fine.user?.userName ||
        fine.user?.username ||
        fine.user?.name ||
        fine.user?.fullName ||
        fine.borrower?.userName ||
        fine.borrower?.username ||
        fine.borrower?.name ||
        fine.borrower?.fullName ||
        "Unknown User"
      );
    },


    /* =========================
       BOOK TITLE
    ========================== */
    getBookTitle(fine) {

      if (!fine) {
        return "Unknown Book";
      }

      if (fine.bookTitle) {
        return fine.bookTitle;
      }

      if (fine.title) {
        return fine.title;
      }

      if (typeof fine.book === "string") {
        return fine.book;
      }

      if (fine.book?.title) {
        return fine.book.title;
      }

      if (fine.borrowing?.book?.title) {
        return fine.borrowing.book.title;
      }

      if (fine.borrowing?.bookTitle) {
        return fine.borrowing.bookTitle;
      }

      return "Unknown Book";
    },


    /* =========================
       BOOK IMAGE
    ========================== */
    getBookImage(fine) {

      if (!fine) {
        return null;
      }

      return (
        fine.image ||
        fine.bookImage ||
        fine.book?.image ||
        fine.borrowing?.book?.image ||
        null
      );
    },


    /* =========================
       CATEGORY
    ========================== */
    getCategory(fine) {

      if (!fine) {
        return "";
      }

      return (
        fine.category ||
        fine.book?.category ||
        fine.borrowing?.book?.category ||
        ""
      );
    },


    /* =========================
       ISBN
    ========================== */
    getISBN(fine) {

      if (!fine) {
        return "";
      }

      return (
        fine.isbn ||
        fine.book?.isbn ||
        fine.borrowing?.book?.isbn ||
        ""
      );
    },


    /* =========================
       DUE DATE
    ========================== */
    getDueDate(fine) {

      if (!fine) {
        return "-";
      }

      return (
        fine.dueDate ||
        fine.borrowing?.dueDate ||
        "-"
      );
    },


    /* =========================
       RETURNED DATE
    ========================== */
    getReturnedDate(fine) {

      if (!fine) {
        return "-";
      }

      return (
        fine.returnedDate ||
        fine.returnDate ||
        fine.borrowing?.returnedDate ||
        fine.borrowing?.returnDate ||
        "-"
      );
    },


    /* =========================
       LATE DAYS
    ========================== */
    getLateDays(fine) {

      if (!fine) {
        return 0;
      }

      const value =
        fine.daysLate ??
        fine.lateDays ??
        fine.overdueDays ??
        fine.borrowing?.daysLate ??
        0;

      const number = Number(value);

      return Number.isFinite(number) && number >= 0
        ? number
        : 0;
    },


    /* =========================
       FINE AMOUNT
    ========================== */
    getFineAmount(fine) {

      if (!fine) {
        return 0;
      }

      let value =
        fine.totalAmount ??
        fine.amount ??
        fine.fineAmount ??
        fine.totalFine ??
        fine.fine ??
        0;


      /* Object response */
      if (
        typeof value === "object" &&
        value !== null
      ) {
        value =
          value.amount ??
          value.totalAmount ??
          value.value ??
          0;
      }


      /* String response */
      if (typeof value === "string") {

        value = value
          .replace(/,/g, "")
          .replace(/[៛$]/g, "")
          .trim();
      }


      const number = Number(value);

      return Number.isFinite(number)
        ? number
        : 0;
    },


    /* =========================
       FORMAT AMOUNT
    ========================== */
    formatAmount(amount) {

      const number = Number(amount);

      if (!Number.isFinite(number)) {
        return "0.00";
      }

      return number.toLocaleString("en-US", {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      });
    },


    /* =========================
       STATUS
    ========================== */
    getStatus(fine) {

      if (!fine) {
        return "UNPAID";
      }

      const status =
        fine.status ??
        fine.paymentStatus ??
        "UNPAID";

      return String(status).toUpperCase();
    },


    /* =========================
       STATUS CLASS
    ========================== */
    getStatusClass(fine) {

      const status =
        this.getStatus(fine).toLowerCase();

      if (status === "paid") {
        return "paid";
      }

      if (status === "pending") {
        return "pending";
      }

      if (status === "overdue") {
        return "overdue";
      }

      return "unpaid";
    }

  }
};
</script>


<style scoped>

/* =========================
   CARD
========================= */

.fine-card {
  width: 100%;

  display: grid;

  grid-template-columns:
    2fr
    2fr
    auto;

  align-items: center;

  gap: 24px;

  padding: 20px;

  box-sizing: border-box;

  background: #ffffff;

  border: 1px solid #e5e7eb;

  border-radius: 14px;

  transition: 0.2s ease;
}


.fine-card:hover {
  transform: translateY(-2px);

  box-shadow:
    0 6px 18px
    rgba(0, 0, 0, 0.06);
}


/* =========================
   BOOK
========================= */

.book-info {
  display: flex;

  align-items: center;

  gap: 16px;

  min-width: 0;
}


.book-cover {
  width: 75px;

  height: 100px;

  flex-shrink: 0;

  overflow: hidden;

  border-radius: 8px;

  background: #f1f5f9;
}


.book-cover img {
  width: 100%;

  height: 100%;

  object-fit: cover;
}


.no-cover {
  width: 100%;

  height: 100%;

  display: flex;

  align-items: center;

  justify-content: center;

  color: #94a3b8;
}


.no-cover i {
  font-size: 28px;
}


.book-details {
  min-width: 0;

  overflow: hidden;
}


.book-details h3 {
  margin: 0;

  font-size: 16px;

  font-weight: 600;

  color: #1f2937;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;
}


/* =========================
   USER
========================= */

.user-name {
  display: flex;

  align-items: center;

  gap: 5px;

  margin: 5px 0 0;

  font-size: 13px;

  color: #475569;
}


.user-name i {
  font-size: 12px;

  color: #64748b;
}


/* =========================
   CATEGORY
========================= */

.category {
  margin: 5px 0 0;

  font-size: 12px;

  color: #2563eb;
}


/* =========================
   ISBN
========================= */

.isbn {
  margin: 4px 0 0;

  font-size: 11px;

  color: #9ca3af;
}


/* =========================
   FINE DETAILS
========================= */

.fine-details {
  display: grid;

  grid-template-columns:
    repeat(3, 1fr);

  gap: 16px;
}


.detail {
  display: flex;

  flex-direction: column;

  gap: 5px;
}


.detail span {
  font-size: 11px;

  color: #9ca3af;
}


.detail strong {
  font-size: 13px;

  font-weight: 500;

  color: #374151;

  white-space: nowrap;
}


/* =========================
   RESULT
========================= */

.fine-result {
  min-width: 90px;

  display: flex;

  flex-direction: column;

  align-items: flex-end;

  gap: 8px;
}


/* =========================
   STATUS
========================= */

.status {
  padding: 5px 10px;

  border-radius: 999px;

  font-size: 11px;

  font-weight: 600;

  white-space: nowrap;
}


/* Paid */

.status.paid {
  background: #dcfce7;

  color: #16a34a;
}


/* Unpaid */

.status.unpaid {
  background: #fee2e2;

  color: #dc2626;
}


/* Pending */

.status.pending {
  background: #fef3c7;

  color: #d97706;
}


/* Overdue */

.status.overdue {
  background: #fee2e2;

  color: #dc2626;
}


/* =========================
   AMOUNT
========================= */

.amount {
  font-size: 20px;

  font-weight: 700;

  color: #ef4444;

  white-space: nowrap;
}


/* =========================
   PAID AMOUNT
========================= */

.status.paid + .amount {
  color: #16a34a;
}


/* =========================
   RESPONSIVE
========================= */

@media (max-width: 950px) {

  .fine-card {
    grid-template-columns: 1fr;

    align-items: stretch;
  }


  .fine-result {
    flex-direction: row;

    align-items: center;

    justify-content: space-between;
  }

}


/* =========================
   TABLET / MOBILE
========================= */

@media (max-width: 600px) {

  .fine-card {
    padding: 16px;

    gap: 18px;
  }


  .fine-details {
    grid-template-columns: 1fr;
  }


  .book-cover {
    width: 65px;

    height: 90px;
  }


  .book-details h3 {
    font-size: 15px;
  }


  .amount {
    font-size: 18px;
  }

}


/* =========================
   SMALL MOBILE
========================= */

@media (max-width: 400px) {

  .book-info {
    gap: 12px;
  }


  .book-cover {
    width: 58px;

    height: 80px;
  }


  .fine-card {
    padding: 14px;
  }


  .fine-result {
    gap: 6px;
  }

}

</style>