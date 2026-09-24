
<template>

  <div class="table-card">

    <!-- =========================
         Table Header
    ========================== -->

    <div class="table-header">

      <div>

        <h2>
          Fines
        </h2>

        <p>
          List of all library fines
        </p>

      </div>


      <span class="fine-count">
        {{ fines.length }} records
      </span>

    </div>


    <!-- =========================
         Table
    ========================== -->

    <div class="table-wrapper">

      <table>

        <thead>

          <tr>

            <th>ID</th>

            <th>User</th>

            <th>Book</th>

            <th>Days Late</th>

            <th>Amount</th>

            <th>Status</th>

            <th>Action</th>

          </tr>

        </thead>


        <tbody>

          <!-- =========================
               Data
          ========================== -->

          <tr
            v-for="fine in fines"
            :key="fine.id"
          >

            <!-- =========================
                 ID
            ========================== -->

            <td>

              <span class="id-number">
                {{ fine.id }}
              </span>

            </td>


            <!-- =========================
                 User
            ========================== -->

            <td>

              <div class="user-info">

                <div class="avatar">

                  {{ getInitials(getUserName(fine)) }}

                </div>


                <span class="user-name">

                  {{ getUserName(fine) }}

                </span>

              </div>

            </td>


            <!-- =========================
                 Book
            ========================== -->

            <td>

              <span class="book-name">

                {{ getBookTitle(fine) }}

              </span>

            </td>


            <!-- =========================
                 Days Late
            ========================== -->

            <td>

              <span class="late-days">

                {{ getLateDays(fine) }}

                {{ getLateDays(fine) === 1 ? "day" : "days" }}

              </span>

            </td>


            <!-- =========================
                 Amount
            ========================== -->

            <td>

              <strong class="amount">

                ៛{{ formatAmount(getFineAmount(fine)) }}

              </strong>

            </td>


            <!-- =========================
                 Status
            ========================== -->

            <td>

              <span
                class="status-badge"
                :class="getStatusClass(fine)"
              >

                {{ getStatus(fine) }}

              </span>

            </td>


            <!-- =========================
                 Actions
            ========================== -->

            <td>

              <div class="actions">

                <!-- =========================
                     View
                ========================== -->

                <button
                  type="button"
                  class="action-btn view"
                  title="View Fine"
                  @click="viewFine(fine)"
                >

                  <i class="bi bi-eye"></i>

                </button>


                <!-- =========================
                     Mark As Paid
                ========================== -->

                <button
                  type="button"
                  class="action-btn paid"
                  title="Mark as Paid"
                  :disabled="isPaid(fine)"
                  @click="markPaid(fine)"
                >

                  <i class="bi bi-check-lg"></i>

                </button>


                <!-- =========================
                     Delete
                ========================== -->

                <button
                  type="button"
                  class="action-btn delete"
                  title="Delete Fine"
                  @click="deleteFine(fine.id)"
                >

                  <i class="bi bi-trash3"></i>

                </button>

              </div>

            </td>

          </tr>


          <!-- =========================
               Empty State
          ========================== -->

          <tr v-if="fines.length === 0">

            <td
              colspan="7"
              class="empty-state"
            >

              <div class="empty-icon">

                <i class="bi bi-cash-stack"></i>

              </div>


              <h3>
                No fines found
              </h3>


              <p>
                There are no fines matching your search.
              </p>

            </td>

          </tr>

        </tbody>

      </table>

    </div>

  </div>

</template>


<script setup>

/* ======================================================
   PROPS
====================================================== */

const props = defineProps({

  fines: {

    type: Array,

    default: () => []

  }

});


/* ======================================================
   EVENTS
====================================================== */

const emit = defineEmits([
  "view",
  "mark-paid",
  "delete"
]);


/* ======================================================
   GET USER NAME
====================================================== */

function getUserName(fine) {

  if (!fine) {
    return "Unknown User";
  }


  /*
   * Normal field from FinesManagement.vue
   */

  if (
    fine.userName !== null &&
    fine.userName !== undefined &&
    String(fine.userName).trim() !== ""
  ) {

    return String(
      fine.userName
    );

  }


  /*
   * Other possible backend fields
   */

  if (
    fine.borrowerName !== null &&
    fine.borrowerName !== undefined &&
    String(fine.borrowerName).trim() !== ""
  ) {

    return String(
      fine.borrowerName
    );

  }


  /*
   * Nested user object
   */

  if (fine.user) {

    if (fine.user.userName) {
      return String(
        fine.user.userName
      );
    }


    if (fine.user.username) {
      return String(
        fine.user.username
      );
    }


    if (fine.user.name) {
      return String(
        fine.user.name
      );
    }


    if (fine.user.fullName) {
      return String(
        fine.user.fullName
      );
    }

  }


  /*
   * Nested borrower object
   */

  if (fine.borrower) {

    if (fine.borrower.userName) {
      return String(
        fine.borrower.userName
      );
    }


    if (fine.borrower.username) {
      return String(
        fine.borrower.username
      );
    }


    if (fine.borrower.name) {
      return String(
        fine.borrower.name
      );
    }


    if (fine.borrower.fullName) {
      return String(
        fine.borrower.fullName
      );
    }

  }


  return "Unknown User";

}


/* ======================================================
   GET BOOK TITLE
====================================================== */

function getBookTitle(fine) {

  if (!fine) {
    return "Unknown Book";
  }


  /*
   * Normal field
   */

  if (
    fine.bookTitle !== null &&
    fine.bookTitle !== undefined &&
    String(fine.bookTitle).trim() !== ""
  ) {

    return String(
      fine.bookTitle
    );

  }


  /*
   * Direct book field
   */

  if (
    fine.book !== null &&
    fine.book !== undefined
  ) {

    /*
     * book is string
     */

    if (
      typeof fine.book === "string"
    ) {

      return fine.book;

    }


    /*
     * book is object
     */

    if (
      typeof fine.book === "object"
    ) {

      if (fine.book.title) {
        return String(
          fine.book.title
        );
      }


      if (fine.book.bookTitle) {
        return String(
          fine.book.bookTitle
        );
      }

    }

  }


  /*
   * Borrowing -> Book
   */

  if (
    fine.borrowing?.book
  ) {

    if (
      fine.borrowing.book.title
    ) {

      return String(
        fine.borrowing.book.title
      );

    }


    if (
      fine.borrowing.book.bookTitle
    ) {

      return String(
        fine.borrowing.book.bookTitle
      );

    }

  }


  /*
   * Borrowing -> bookTitle
   */

  if (
    fine.borrowing?.bookTitle
  ) {

    return String(
      fine.borrowing.bookTitle
    );

  }


  /*
   * Fallback title
   */

  if (
    fine.title
  ) {

    return String(
      fine.title
    );

  }


  return "Unknown Book";

}


/* ======================================================
   GET FINE AMOUNT
====================================================== */

function getFineAmount(fine) {

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


  /*
   * Number
   */

  if (
    typeof amount === "number"
  ) {

    return Number.isFinite(amount)
      ? amount
      : 0;

  }


  /*
   * String
   */

  if (
    typeof amount === "string"
  ) {

    const cleaned =
      amount
        .replace(/,/g, "")
        .replace(/[៛$]/g, "")
        .trim();


    if (!cleaned) {
      return 0;
    }


    const number =
      Number(cleaned);


    return Number.isFinite(number)
      ? number
      : 0;

  }


  /*
   * Object
   */

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

}


/* ======================================================
   FORMAT AMOUNT
====================================================== */

function formatAmount(amount) {

  const number =
    Number(amount);


  if (
    !Number.isFinite(number)
  ) {

    return "0.00";

  }


  return number.toLocaleString(
    "en-US",
    {
      minimumFractionDigits: 2,
      maximumFractionDigits: 2
    }
  );

}


/* ======================================================
   GET LATE DAYS
====================================================== */

function getLateDays(fine) {

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

}


/* ======================================================
   GET STATUS
====================================================== */

function getStatus(fine) {

  if (!fine) {
    return "UNPAID";
  }


  const status =
    fine.status ??
    fine.paymentStatus ??
    "UNPAID";


  return String(
    status
  ).toUpperCase();

}


/* ======================================================
   STATUS CLASS
====================================================== */

function getStatusClass(fine) {

  const status =
    getStatus(fine)
      .toLowerCase();


  return status;

}


/* ======================================================
   CHECK PAID
====================================================== */

function isPaid(fine) {

  return (
    getStatus(fine)
      .toLowerCase() ===
    "paid"
  );

}


/* ======================================================
   VIEW FINE
====================================================== */

function viewFine(fine) {

  if (!fine) {
    return;
  }


  emit(
    "view",
    fine
  );

}


/* ======================================================
   MARK AS PAID
====================================================== */

function markPaid(fine) {

  if (!fine) {
    return;
  }


  if (
    isPaid(fine)
  ) {

    return;

  }


  emit(
    "mark-paid",
    fine
  );

}


/* ======================================================
   DELETE FINE
====================================================== */

function deleteFine(id) {

  if (!id) {
    return;
  }


  emit(
    "delete",
    id
  );

}


/* ======================================================
   USER INITIALS
====================================================== */

function getInitials(name) {

  if (
    !name ||
    name === "Unknown User"
  ) {

    return "?";

  }


  return String(name)
    .trim()
    .split(/\s+/)
    .filter(Boolean)
    .map(
      word =>
        word
          .charAt(0)
          .toUpperCase()
    )
    .slice(0, 2)
    .join("");

}

</script>


<style scoped>

/* ======================================================
   CARD
====================================================== */

.table-card {

  overflow: hidden;

  background: white;

  border:
    1px solid
    #e5e7eb;

  border-radius: 10px;

}


/* ======================================================
   HEADER
====================================================== */

.table-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  padding: 20px;

  border-bottom:
    1px solid
    #e5e7eb;

}


.table-header h2 {

  margin: 0;

  color: #1f2937;

  font-size: 18px;

}


.table-header p {

  margin:
    5px 0 0;

  color: #6b7280;

  font-size: 13px;

}


.fine-count {

  padding:
    6px
    10px;

  border-radius: 6px;

  background: #f3f4f6;

  color: #4b5563;

  font-size: 13px;

}


/* ======================================================
   TABLE
====================================================== */

.table-wrapper {

  overflow-x: auto;

}


table {

  width: 100%;

  min-width: 850px;

  border-collapse: collapse;

}


thead {

  background: #f9fafb;

}


th {

  padding:
    14px
    18px;

  text-align: left;

  color: #6b7280;

  font-size: 12px;

  font-weight: 600;

  text-transform: uppercase;

  white-space: nowrap;

}


td {

  padding:
    15px
    18px;

  border-top:
    1px solid
    #f1f5f9;

  color: #374151;

  font-size: 14px;

}


tbody tr {

  transition:
    background
    0.2s ease;

}


tbody tr:hover {

  background:
    #fafafa;

}


/* ======================================================
   ID
====================================================== */

.id-number {

  color: #6b7280;

  font-weight: 600;

}


/* ======================================================
   USER
====================================================== */

.user-info {

  display: flex;

  align-items: center;

  gap: 10px;

  min-width: 180px;

}


.avatar {

  width: 34px;

  height: 34px;

  flex-shrink: 0;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  background: #e0e7ff;

  color: #4338ca;

  font-size: 12px;

  font-weight: 700;

}


.user-name {

  max-width: 180px;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  color: #374151;

  font-weight: 500;

}


/* ======================================================
   BOOK
====================================================== */

.book-name {

  display: block;

  max-width: 220px;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  color: #374151;

  font-weight: 500;

}


/* ======================================================
   LATE DAYS
====================================================== */

.late-days {

  color: #dc2626;

  font-weight: 500;

  white-space: nowrap;

}


/* ======================================================
   AMOUNT
====================================================== */

.amount {

  color: #111827;

  font-weight: 700;

  white-space: nowrap;

}


/* ======================================================
   STATUS
====================================================== */

.status-badge {

  display: inline-flex;

  align-items: center;

  justify-content: center;

  padding:
    5px
    10px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

  white-space: nowrap;

}


/* PAID */

.status-badge.paid {

  background: #dcfce7;

  color: #15803d;

}


/* UNPAID */

.status-badge.unpaid {

  background: #fee2e2;

  color: #dc2626;

}


/* PENDING */

.status-badge.pending {

  background: #fef3c7;

  color: #b45309;

}


/* OVERDUE */

.status-badge.overdue {

  background: #ffedd5;

  color: #c2410c;

}


/* ======================================================
   ACTIONS
====================================================== */

.actions {

  display: flex;

  gap: 7px;

}


/* ======================================================
   ACTION BUTTON
====================================================== */

.action-btn {

  width: 32px;

  height: 32px;

  display: flex;

  align-items: center;

  justify-content: center;

  border: none;

  border-radius: 6px;

  cursor: pointer;

  font-size: 15px;

  transition:
    0.2s ease;

}


/* ======================================================
   VIEW
====================================================== */

.action-btn.view {

  background:
    #eff6ff;

  color:
    #2563eb;

}


.action-btn.view:hover {

  background:
    #dbeafe;

  transform:
    translateY(-1px);

}


/* ======================================================
   PAID
====================================================== */

.action-btn.paid {

  background:
    #ecfdf5;

  color:
    #16a34a;

}


.action-btn.paid:hover {

  background:
    #dcfce7;

  transform:
    translateY(-1px);

}


.action-btn.paid:disabled {

  opacity:
    0.45;

  cursor:
    not-allowed;

  transform:
    none;

}


/* ======================================================
   DELETE
====================================================== */

.action-btn.delete {

  background:
    #fef2f2;

  color:
    #dc2626;

}


.action-btn.delete:hover {

  background:
    #fee2e2;

  transform:
    translateY(-1px);

}


/* ======================================================
   EMPTY
====================================================== */

.empty-state {

  padding:
    60px
    20px;

  text-align:
    center;

}


.empty-icon {

  width:
    60px;

  height:
    60px;

  margin:
    0 auto 15px;

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  border-radius:
    50%;

  background:
    #f1f5f9;

  color:
    #64748b;

  font-size:
    28px;

}


.empty-state h3 {

  margin:
    0;

  color:
    #374151;

  font-size:
    16px;

}


.empty-state p {

  margin-top:
    5px;

  color:
    #9ca3af;

  font-size:
    14px;

}


/* ======================================================
   MOBILE
====================================================== */

@media (max-width: 768px) {

  .table-header {

    padding:
      16px;

  }


  .table-header h2 {

    font-size:
      16px;

  }


  .table-header p {

    font-size:
      12px;

  }


  .fine-count {

    font-size:
      12px;

    padding:
      5px
      8px;

  }


  th {

    padding:
      12px
      14px;

  }


  td {

    padding:
      13px
      14px;

  }

}

</style>
