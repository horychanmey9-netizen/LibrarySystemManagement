<template>

  <div class="table-card">

    <!-- =====================================================
         TABLE HEADER
    ====================================================== -->

    <div class="table-header">

      <div>

        <h2>
          Borrowings
        </h2>

        <p>
          List of all borrowing records
        </p>

      </div>


      <span class="borrowing-count">

        {{ borrowings.length }} records

      </span>

    </div>


    <!-- =====================================================
         TABLE
    ====================================================== -->

    <div class="table-wrapper">

      <table>

        <!-- =================================================
             HEADER
        ================================================== -->

        <thead>

          <tr>

            <th>ID</th>

            <th>User</th>

            <th>Book</th>

            <th>Borrow Date</th>

            <th>Due Date</th>

            <th>Return Date</th>

            <th>Fine</th>

            <th>Status</th>

            <th>Action</th>

          </tr>

        </thead>


        <!-- =================================================
             BODY
        ================================================== -->

        <tbody>

          <!-- =================================================
               BORROWING DATA
          ================================================== -->

          <tr
            v-for="item in borrowings"
            :key="item.id"
          >

            <!-- =================================================
                 ID
            ================================================== -->

            <td>

              <span class="id-number">

                #{{ item.id }}

              </span>

            </td>


            <!-- =================================================
                 USER
            ================================================== -->

            <td>

              <div class="user-info">

                <!-- AVATAR -->

                <div class="avatar">

                  {{ getInitials(item.userName) }}

                </div>


                <!-- USER NAME -->

                <div>

                  <span class="user-name">

                    {{ item.userName || "Unknown User" }}

                  </span>

                  <small>

                    User ID:
                    {{ item.userId || "-" }}

                  </small>

                </div>

              </div>

            </td>


            <!-- =================================================
                 BOOK
            ================================================== -->

            <td>

              <div class="book-info">

                <span class="book-name">

                  {{ item.bookTitle || "Unknown Book" }}

                </span>

                <small>

                  Book ID:
                  {{ item.bookId || "-" }}

                </small>

              </div>

            </td>


            <!-- =================================================
                 BORROW DATE
            ================================================== -->

            <td>

              <span>

                {{ formatDate(item.borrowDate) }}

              </span>

            </td>


            <!-- =================================================
                 DUE DATE
            ================================================== -->

            <td>

              <span
                :class="{
                  'late-date': isLate(item)
                }"
              >

                {{ formatDate(item.dueDate) }}

              </span>

            </td>


            <!-- =================================================
                 RETURN DATE
            ================================================== -->

            <td>

              <span
                :class="{
                  'returned-date': item.returnDate
                }"
              >

                {{
                  item.returnDate
                    ? formatDate(item.returnDate)
                    : "Not returned"
                }}

              </span>

            </td>


            <!-- =================================================
                 FINE
            ================================================== -->

            <td>

              <span
                class="fine"
                :class="{
                  'has-fine': Number(item.fine || 0) > 0
                }"
              >

                ${{ formatFine(item.fine) }}

              </span>

            </td>


            <!-- =================================================
                 STATUS
            ================================================== -->

            <td>

              <span
                class="status-badge"
                :class="getStatusClass(item.status)"
              >

                <i
                  :class="getStatusIcon(item.status)"
                ></i>

                {{ formatStatus(item.status) }}

              </span>

            </td>


            <!-- =================================================
                 ACTION
            ================================================== -->

            <td>

              <div class="actions">

                <!-- VIEW -->

                <button
                  type="button"
                  class="action-btn view"
                  title="View Details"
                  @click="$emit('view', item)"
                >

                  <i class="bi bi-eye"></i>

                </button>


                <!-- RETURN -->

                <button
                  type="button"
                  class="action-btn return"
                  title="Return Book"
                  :disabled="
                    isReturned(item)
                  "
                  @click="$emit('return', item)"
                >

                  <i class="bi bi-arrow-return-left"></i>

                </button>


                <!-- RENEW -->

                <button
                  type="button"
                  class="action-btn renew"
                  title="Renew Book"
                  :disabled="
                    isReturned(item)
                  "
                  @click="$emit('renew', item)"
                >

                  <i class="bi bi-arrow-clockwise"></i>

                </button>

              </div>

            </td>

          </tr>


          <!-- =================================================
               EMPTY
          ================================================== -->

          <tr
            v-if="borrowings.length === 0"
          >

            <td
              colspan="9"
              class="empty-state"
            >

              <div class="empty-icon">

                <i class="bi bi-inbox"></i>

              </div>

              <h3>
                No borrowing records found
              </h3>

              <p>
                There are currently no borrowing records.
              </p>

            </td>

          </tr>

        </tbody>

      </table>

    </div>

  </div>

</template>


<script setup>

/* =========================================================
   PROPS
========================================================= */

defineProps({

  borrowings: {

    type: Array,

    default: () => []

  }

});


/* =========================================================
   EVENTS
========================================================= */

defineEmits([

  "view",

  "return",

  "renew"

]);


/* =========================================================
   GET INITIALS
========================================================= */

function getInitials(name) {

  if (!name) {

    return "?";

  }


  return String(name)

    .trim()

    .split(/\s+/)

    .filter(Boolean)

    .map(word => word.charAt(0))

    .slice(0, 2)

    .join("")

    .toUpperCase();

}


/* =========================================================
   FORMAT DATE
========================================================= */

function formatDate(date) {

  if (!date) {

    return "-";

  }


  try {

    const value =
      new Date(date + "T00:00:00");


    if (Number.isNaN(value.getTime())) {

      return date;

    }


    return value.toLocaleDateString(
      "en-GB",
      {
        day: "2-digit",
        month: "short",
        year: "numeric"
      }
    );

  } catch (error) {

    return date;

  }

}


/* =========================================================
   FORMAT FINE
========================================================= */

function formatFine(fine) {

  const value =
    Number(fine || 0);


  return value.toFixed(2);

}


/* =========================================================
   FORMAT STATUS
========================================================= */

function formatStatus(status) {

  if (!status) {

    return "Unknown";

  }


  const value =
    String(status)
      .toLowerCase();


  return value.charAt(0).toUpperCase()
    + value.slice(1);

}


/* =========================================================
   STATUS CLASS
========================================================= */

function getStatusClass(status) {

  if (!status) {

    return "unknown";

  }


  const value =
    String(status)
      .toLowerCase();


  if (
    value === "borrowed"
  ) {

    return "borrowed";

  }


  if (
    value === "returned"
  ) {

    return "returned";

  }


  if (
    value === "late" ||
    value === "overdue"
  ) {

    return "late";

  }


  if (
    value === "renewed"
  ) {

    return "renewed";

  }


  return "unknown";

}


/* =========================================================
   STATUS ICON
========================================================= */

function getStatusIcon(status) {

  if (!status) {

    return "bi bi-question-circle";

  }


  const value =
    String(status)
      .toLowerCase();


  if (
    value === "borrowed"
  ) {

    return "bi bi-book";

  }


  if (
    value === "returned"
  ) {

    return "bi bi-check-circle";

  }


  if (
    value === "late" ||
    value === "overdue"
  ) {

    return "bi bi-clock";

  }


  if (
    value === "renewed"
  ) {

    return "bi bi-arrow-clockwise";

  }


  return "bi bi-question-circle";

}


/* =========================================================
   IS RETURNED
========================================================= */

function isReturned(item) {

  return (

    String(item.status || "")
      .toUpperCase() === "RETURNED"

    ||

    !!item.returnDate

  );

}


/* =========================================================
   IS LATE
========================================================= */

function isLate(item) {

  if (!item.dueDate) {

    return false;

  }


  if (isReturned(item)) {

    return false;

  }


  const status =
    String(item.status || "")
      .toUpperCase();


  if (
    status === "LATE" ||
    status === "OVERDUE"
  ) {

    return true;

  }


  const today =
    new Date();

  today.setHours(
    0,
    0,
    0,
    0
  );


  const due =
    new Date(
      item.dueDate + "T00:00:00"
    );


  return due < today;

}

</script>


<style scoped>

/* =========================================================
   CARD
========================================================= */

.table-card {

  overflow: hidden;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 10px;

}


/* =========================================================
   HEADER
========================================================= */

.table-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  padding: 20px;

  border-bottom:
    1px solid #e5e7eb;

}


.table-header h2 {

  margin: 0;

  color: #1f2937;

  font-size: 18px;

  font-weight: 700;

}


.table-header p {

  margin: 5px 0 0;

  color: #6b7280;

  font-size: 13px;

}


.borrowing-count {

  padding: 6px 10px;

  border-radius: 6px;

  background: #f3f4f6;

  color: #4b5563;

  font-size: 13px;

  font-weight: 600;

}


/* =========================================================
   TABLE WRAPPER
========================================================= */

.table-wrapper {

  width: 100%;

  overflow-x: auto;

}


/* =========================================================
   TABLE
========================================================= */

table {

  width: 100%;

  min-width: 1200px;

  border-collapse: collapse;

}


thead {

  background: #f9fafb;

}


th {

  padding: 14px 18px;

  text-align: left;

  color: #6b7280;

  font-size: 12px;

  font-weight: 600;

  text-transform: uppercase;

  white-space: nowrap;

}


td {

  padding: 15px 18px;

  border-top:
    1px solid #f1f5f9;

  color: #374151;

  font-size: 14px;

  white-space: nowrap;

}


tbody tr {

  transition: background 0.2s ease;

}


tbody tr:hover {

  background: #fafafa;

}


/* =========================================================
   ID
========================================================= */

.id-number {

  color: #6b7280;

  font-weight: 600;

}


/* =========================================================
   USER
========================================================= */

.user-info {

  display: flex;

  align-items: center;

  gap: 10px;

}


.avatar {

  width: 36px;

  height: 36px;

  display: flex;

  align-items: center;

  justify-content: center;

  flex-shrink: 0;

  border-radius: 50%;

  background: #dbeafe;

  color: #2563eb;

  font-size: 12px;

  font-weight: 700;

}


.user-name {

  display: block;

  color: #374151;

  font-weight: 600;

}


.user-info small {

  display: block;

  margin-top: 3px;

  color: #9ca3af;

  font-size: 11px;

}


/* =========================================================
   BOOK
========================================================= */

.book-info {

  display: flex;

  flex-direction: column;

}


.book-name {

  color: #374151;

  font-weight: 600;

}


.book-info small {

  margin-top: 3px;

  color: #9ca3af;

  font-size: 11px;

}


/* =========================================================
   DATES
========================================================= */

.late-date {

  color: #dc2626;

  font-weight: 700;

}


.returned-date {

  color: #15803d;

  font-weight: 600;

}


/* =========================================================
   FINE
========================================================= */

.fine {

  color: #64748b;

  font-weight: 600;

}


.fine.has-fine {

  color: #dc2626;

}


/* =========================================================
   STATUS
========================================================= */

.status-badge {

  display: inline-flex;

  align-items: center;

  gap: 5px;

  padding: 5px 10px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


.status-badge.borrowed {

  background: #dbeafe;

  color: #2563eb;

}


.status-badge.late {

  background: #fee2e2;

  color: #dc2626;

}


.status-badge.returned {

  background: #dcfce7;

  color: #15803d;

}


.status-badge.renewed {

  background: #ffedd5;

  color: #ea580c;

}


.status-badge.unknown {

  background: #f1f5f9;

  color: #64748b;

}


/* =========================================================
   ACTIONS
========================================================= */

.actions {

  display: flex;

  gap: 7px;

}


.action-btn {

  width: 34px;

  height: 34px;

  display: flex;

  align-items: center;

  justify-content: center;

  border: none;

  border-radius: 6px;

  cursor: pointer;

  font-size: 14px;

  transition: 0.2s;

}


/* =========================================================
   VIEW
========================================================= */

.action-btn.view {

  background: #eff6ff;

  color: #2563eb;

}


.action-btn.view:hover {

  background: #dbeafe;

}


/* =========================================================
   RETURN
========================================================= */

.action-btn.return {

  background: #ecfdf5;

  color: #059669;

}


.action-btn.return:hover {

  background: #d1fae5;

}


/* =========================================================
   RENEW
========================================================= */

.action-btn.renew {

  background: #fff7ed;

  color: #ea580c;

}


.action-btn.renew:hover {

  background: #ffedd5;

}


/* =========================================================
   DISABLED
========================================================= */

.action-btn:disabled {

  opacity: 0.4;

  cursor: not-allowed;

}


/* =========================================================
   EMPTY
========================================================= */

.empty-state {

  padding: 60px 20px !important;

  text-align: center;

}


.empty-icon {

  margin-bottom: 10px;

  color: #d1d5db;

  font-size: 40px;

}


.empty-state h3 {

  margin: 0;

  color: #374151;

  font-size: 16px;

}


.empty-state p {

  margin-top: 5px;

  color: #9ca3af;

  font-size: 14px;

}

</style>