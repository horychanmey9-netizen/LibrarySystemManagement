<template>

  <!-- =========================
       Modal Overlay
  ========================== -->

  <div
    v-if="borrowing"
    class="modal-overlay"
    @click.self="closeModal"
  >

    <!-- =========================
         Modal
    ========================== -->

    <div class="details-modal">


      <!-- =========================
           Header
      ========================== -->

      <div class="modal-header">

        <div>

          <h2>
            Borrowing Details
          </h2>

          <p>
            View borrowing information
          </p>

        </div>


        <!-- Close Button -->

        <button
          type="button"
          class="close-btn"
          @click="closeModal"
        >

          <i class="bi bi-x-lg"></i>

        </button>

      </div>


      <!-- =========================
           Body
      ========================== -->

      <div class="modal-body">


        <!-- =========================
             Book Section
        ========================== -->

        <div class="book-section">


          <!-- Book Image -->

          <div class="book-image-wrapper">

            <img
              v-if="borrowing.bookImage"
              :src="borrowing.bookImage"
              :alt="borrowing.bookTitle || 'Book'"
              class="book-image"
            />


            <!-- No Image -->

            <div
              v-else
              class="book-image-placeholder"
            >

              <i class="bi bi-book"></i>

            </div>

          </div>


          <!-- Book Information -->

          <div class="book-info">

            <span class="section-label">
              Book
            </span>

            <h3>
              {{ borrowing.bookTitle || "Unknown Book" }}
            </h3>

            <p>
              Book ID:

              <span>
                {{ borrowing.bookId ?? "-" }}
              </span>
            </p>

          </div>

        </div>


        <!-- =========================
             Borrower Information
        ========================== -->

        <div class="section">

          <div class="section-title">

            <i class="bi bi-person"></i>

            <span>
              Borrower Information
            </span>

          </div>


          <div class="info-grid">


            <!-- Borrowing ID -->

            <div class="info-item">

              <span class="label">
                Borrowing ID
              </span>

              <span class="value">
                #{{ borrowing.id ?? "-" }}
              </span>

            </div>


            <!-- User ID -->

            <div class="info-item">

              <span class="label">
                User ID
              </span>

              <span class="value">
                {{ borrowing.userId ?? "-" }}
              </span>

            </div>


            <!-- User Name -->

            <div class="info-item full-width">

              <span class="label">
                User Name
              </span>

              <span class="value">
                {{ borrowing.userName || "-" }}
              </span>

            </div>

          </div>

        </div>


        <!-- =========================
             Borrowing Information
        ========================== -->

        <div class="section">

          <div class="section-title">

            <i class="bi bi-calendar3"></i>

            <span>
              Borrowing Information
            </span>

          </div>


          <div class="info-grid">


            <!-- Borrow Date -->

            <div class="info-item">

              <span class="label">
                Borrow Date
              </span>

              <span class="value">
                {{ formatDate(borrowing.borrowDate) }}
              </span>

            </div>


            <!-- Due Date -->

            <div class="info-item">

              <span class="label">
                Due Date
              </span>

              <span class="value">
                {{ formatDate(borrowing.dueDate) }}
              </span>

            </div>


            <!-- Return Date -->

            <div class="info-item">

              <span class="label">
                Return Date
              </span>

              <span class="value">

                {{
                  borrowing.returnDate
                    ? formatDate(borrowing.returnDate)
                    : "Not returned"
                }}

              </span>

            </div>


            <!-- Fine -->

            <div class="info-item">

              <span class="label">
                Fine
              </span>

              <span
                class="value fine-value"
                :class="{
                  'has-fine':
                    Number(borrowing.fine || 0) > 0
                }"
              >

                ${{ formatFine(borrowing.fine) }}

              </span>

            </div>

          </div>

        </div>


        <!-- =========================
             Status
        ========================== -->

        <div class="section status-section">

          <div class="section-title">

            <i class="bi bi-info-circle"></i>

            <span>
              Status
            </span>

          </div>


          <div>

            <span
              class="status-badge"
              :class="
                getStatusClass(
                  borrowing.status
                )
              "
            >

              <i
                :class="
                  getStatusIcon(
                    borrowing.status
                  )
                "
              ></i>

              {{
                formatStatus(
                  borrowing.status
                )
              }}

            </span>

          </div>

        </div>


      </div>


      <!-- =========================
           Footer
      ========================== -->

      <div class="modal-footer">

        <button
          type="button"
          class="close-footer-btn"
          @click="closeModal"
        >

          Close

        </button>

      </div>


    </div>

  </div>

</template>


<script setup>

import { computed } from "vue";


/* =========================
   Props
========================= */

const props = defineProps({

  borrowing: {

    type: Object,

    default: null

  }

});


/* =========================
   Emits
========================= */

const emit = defineEmits([
  "close"
]);


/* =========================
   Close Modal
========================= */

function closeModal() {

  emit("close");

}


/* =========================
   Format Date
========================= */

function formatDate(date) {

  if (!date) {

    return "-";

  }


  /*
   * Backend returns:
   *
   * 2026-08-26
   *
   * We convert it to:
   *
   * Aug 26, 2026
   */

  const parsedDate =
    new Date(
      `${date}T00:00:00`
    );


  if (
    Number.isNaN(
      parsedDate.getTime()
    )
  ) {

    return date;

  }


  return parsedDate.toLocaleDateString(
    "en-US",
    {
      month: "short",
      day: "numeric",
      year: "numeric"
    }
  );

}


/* =========================
   Format Fine
========================= */

function formatFine(fine) {

  const amount =
    Number(fine || 0);


  return amount.toFixed(2);

}


/* =========================
   Format Status
========================= */

function formatStatus(status) {

  if (!status) {

    return "Unknown";

  }


  const statusMap = {

    PENDING:
      "Pending",

    BORROWED:
      "Borrowed",

    OVERDUE:
      "Overdue",

    RETURN_REQUESTED:
      "Return Requested",

    RETURNED:
      "Returned",

    REJECTED:
      "Rejected"

  };


  return (
    statusMap[status]
    ||
    status
      .toString()
      .replaceAll("_", " ")
      .toLowerCase()
      .replace(
        /\b\w/g,
        letter =>
          letter.toUpperCase()
      )
  );

}


/* =========================
   Status Class
========================= */

function getStatusClass(status) {

  const statusClasses = {

    PENDING:
      "status-pending",

    BORROWED:
      "status-borrowed",

    OVERDUE:
      "status-overdue",

    RETURN_REQUESTED:
      "status-return-requested",

    RETURNED:
      "status-returned",

    REJECTED:
      "status-rejected"

  };


  return (
    statusClasses[status]
    ||
    "status-default"
  );

}


/* =========================
   Status Icon
========================= */

function getStatusIcon(status) {

  const statusIcons = {

    PENDING:
      "bi bi-clock",

    BORROWED:
      "bi bi-book",

    OVERDUE:
      "bi bi-exclamation-triangle",

    RETURN_REQUESTED:
      "bi bi-arrow-return-left",

    RETURNED:
      "bi bi-check-circle",

    REJECTED:
      "bi bi-x-circle"

  };


  return (
    statusIcons[status]
    ||
    "bi bi-info-circle"
  );

}

</script>


<style scoped>

/* =========================
   Overlay
========================= */

.modal-overlay {

  position: fixed;

  inset: 0;

  z-index: 1000;

  display: flex;

  align-items: center;

  justify-content: center;

  padding: 20px;

  background:
    rgba(15, 23, 42, 0.45);

}


/* =========================
   Modal
========================= */

.details-modal {

  width: 500px;

  max-width: 100%;

  max-height:
    calc(100vh - 40px);

  overflow-y: auto;

  background: white;

  border-radius: 12px;

  box-shadow:
    0 20px 50px
    rgba(0, 0, 0, 0.18);

}


/* =========================
   Header
========================= */

.modal-header {

  display: flex;

  align-items: flex-start;

  justify-content:
    space-between;

  gap: 15px;

  padding: 20px 22px;

  border-bottom:
    1px solid #e5e7eb;

}


.modal-header h2 {

  margin: 0;

  color: #1f2937;

  font-size: 20px;

  font-weight: 700;

}


.modal-header p {

  margin:
    5px 0 0;

  color: #6b7280;

  font-size: 13px;

}


/* =========================
   Close Button
========================= */

.close-btn {

  width: 34px;

  height: 34px;

  display: flex;

  align-items: center;

  justify-content: center;

  border: none;

  border-radius: 7px;

  background: #f3f4f6;

  color: #6b7280;

  cursor: pointer;

  transition:
    0.2s;

}


.close-btn:hover {

  background: #e5e7eb;

  color: #1f2937;

}


/* =========================
   Body
========================= */

.modal-body {

  padding: 22px;

}


/* =========================
   Book Section
========================= */

.book-section {

  display: flex;

  align-items: center;

  gap: 16px;

  margin-bottom: 24px;

  padding-bottom: 20px;

  border-bottom:
    1px solid #e5e7eb;

}


/* =========================
   Book Image
========================= */

.book-image-wrapper {

  width: 70px;

  height: 90px;

  flex-shrink: 0;

  overflow: hidden;

  border-radius: 8px;

  background: #f1f5f9;

}


.book-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

}


.book-image-placeholder {

  width: 100%;

  height: 100%;

  display: flex;

  align-items: center;

  justify-content: center;

  color: #94a3b8;

  font-size: 28px;

}


/* =========================
   Book Info
========================= */

.book-info {

  min-width: 0;

}


.section-label {

  display: block;

  margin-bottom: 5px;

  color: #94a3b8;

  font-size: 12px;

  font-weight: 500;

}


.book-info h3 {

  margin:
    0 0 7px;

  overflow: hidden;

  color: #1f2937;

  font-size: 17px;

  font-weight: 600;

  text-overflow: ellipsis;

  white-space: nowrap;

}


.book-info p {

  margin: 0;

  color: #6b7280;

  font-size: 12px;

}


.book-info p span {

  color: #374151;

  font-weight: 500;

}


/* =========================
   Section
========================= */

.section {

  margin-bottom: 22px;

}


.section:last-child {

  margin-bottom: 0;

}


/* =========================
   Section Title
========================= */

.section-title {

  display: flex;

  align-items: center;

  gap: 8px;

  margin-bottom: 14px;

  color: #374151;

  font-size: 14px;

  font-weight: 600;

}


.section-title i {

  color: #64748b;

  font-size: 15px;

}


/* =========================
   Info Grid
========================= */

.info-grid {

  display: grid;

  grid-template-columns:
    repeat(2, minmax(0, 1fr));

  gap: 15px;

}


/* =========================
   Info Item
========================= */

.info-item {

  display: flex;

  flex-direction: column;

  gap: 5px;

  min-width: 0;

}


.full-width {

  grid-column:
    1 / -1;

}


.label {

  color: #94a3b8;

  font-size: 11px;

  font-weight: 500;

  text-transform:
    uppercase;

  letter-spacing:
    0.3px;

}


.value {

  overflow: hidden;

  color: #374151;

  font-size: 14px;

  font-weight: 500;

  text-overflow: ellipsis;

  white-space: nowrap;

}


.fine-value {

  color: #16a34a;

}


.fine-value.has-fine {

  color: #dc2626;

}


/* =========================
   Status Section
========================= */

.status-section {

  padding-top: 5px;

}


/* =========================
   Status Badge
========================= */

.status-badge {

  display: inline-flex;

  align-items: center;

  gap: 6px;

  padding:
    6px 11px;

  border-radius: 20px;

  font-size: 12px;

  font-weight: 600;

}


.status-badge i {

  font-size: 12px;

}


/* =========================
   Pending
========================= */

.status-pending {

  background: #fef3c7;

  color: #92400e;

}


/* =========================
   Borrowed
========================= */

.status-borrowed {

  background: #dbeafe;

  color: #1d4ed8;

}


/* =========================
   Overdue
========================= */

.status-overdue {

  background: #fee2e2;

  color: #b91c1c;

}


/* =========================
   Return Requested
========================= */

.status-return-requested {

  background: #ede9fe;

  color: #6d28d9;

}


/* =========================
   Returned
========================= */

.status-returned {

  background: #dcfce7;

  color: #15803d;

}


/* =========================
   Rejected
========================= */

.status-rejected {

  background: #f1f5f9;

  color: #475569;

}


/* =========================
   Default
========================= */

.status-default {

  background: #f3f4f6;

  color: #4b5563;

}


/* =========================
   Footer
========================= */

.modal-footer {

  display: flex;

  justify-content:
    flex-end;

  padding:
    15px 22px;

  border-top:
    1px solid #e5e7eb;

}


/* =========================
   Footer Button
========================= */

.close-footer-btn {

  min-width: 80px;

  padding:
    9px 18px;

  border: none;

  border-radius: 7px;

  background: #2563eb;

  color: white;

  cursor: pointer;

  font-size: 13px;

  font-weight: 500;

  transition:
    0.2s;

}


.close-footer-btn:hover {

  background: #1d4ed8;

}


/* =========================
   Responsive
========================= */

@media (max-width: 550px) {

  .modal-overlay {

    padding: 12px;

  }


  .details-modal {

    width: 100%;

    max-height:
      calc(100vh - 24px);

  }


  .modal-header {

    padding:
      17px;

  }


  .modal-body {

    padding:
      17px;

  }


  .modal-footer {

    padding:
      13px 17px;

  }


  .info-grid {

    grid-template-columns:
      1fr;

  }


  .full-width {

    grid-column:
      auto;

  }


  .book-section {

    align-items:
      flex-start;

  }


  .book-info h3 {

    white-space:
      normal;

  }

}

</style>