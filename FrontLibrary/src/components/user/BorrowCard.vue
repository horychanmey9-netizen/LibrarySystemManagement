```vue
<script setup>

const props = defineProps({

  book: {
    type: Object,
    required: true
  },

  hideReturnButton: {
    type: Boolean,
    default: false
  }

});

const emit = defineEmits(["return"]);


// =====================================================
// DAYS OVERDUE
// =====================================================

function daysOverdue(dueDate) {

  if (!dueDate) return 0;

  const due = new Date(dueDate);

  const today = new Date();

  due.setHours(0, 0, 0, 0);

  today.setHours(0, 0, 0, 0);

  const diff = Math.floor(
    (today - due) /
    (1000 * 60 * 60 * 24)
  );

  return diff > 0 ? diff : 0;

}


// =====================================================
// STATUS
// =====================================================

function getStatus() {

  return props.book.status?.toUpperCase() || "BORROWED";

}


// =====================================================
// STATUS CHECK
// =====================================================

function isReturned() {

  return getStatus() === "RETURNED";

}


function isRejected() {

  return getStatus() === "REJECTED";

}


function isReturnRequested() {

  return getStatus() === "RETURN_REQUESTED";

}


// =====================================================
// STATUS CLASS
// =====================================================

function getStatusClass() {

  const status = getStatus();

  if (status === "RETURNED") {

    return "bg-green-100 text-green-600";

  }

  if (status === "OVERDUE") {

    return "bg-red-100 text-red-600";

  }

  if (status === "PENDING") {

    return "bg-yellow-100 text-yellow-600";

  }

  if (status === "REJECTED") {

    return "bg-gray-100 text-gray-600";

  }

  if (status === "RETURN_REQUESTED") {

    return "bg-purple-100 text-purple-600";

  }

  return "bg-blue-100 text-blue-600";

}


// =====================================================
// STATUS ICON
// =====================================================

function getStatusIcon() {

  const status = getStatus();

  if (status === "RETURNED") {

    return "bi bi-check-circle-fill";

  }

  if (status === "OVERDUE") {

    return "bi bi-exclamation-triangle-fill";

  }

  if (status === "PENDING") {

    return "bi bi-hourglass-split";

  }

  if (status === "REJECTED") {

    return "bi bi-x-circle-fill";

  }

  if (status === "RETURN_REQUESTED") {

    return "bi bi-clock-history";

  }

  return "";

}


// =====================================================
// STATUS LABEL
// =====================================================

function getStatusLabel() {

  const status = getStatus();

  if (status === "REJECTED") {

    return "Not Accept";

  }

  if (status === "RETURN_REQUESTED") {

    return "Return Requested";

  }

  return props.book.status || "BORROWED";

}

</script>


<template>

  <div
    class="borrow-card"
  >

    <!-- =========================
         BOOK IMAGE
    ========================== -->

    <div
      class="book-image"
    >

      <img
        v-if="book.image"
        :src="book.image"
        :alt="book.title"
      />

      <div
        v-else
        class="book-image-empty"
      >

        <i class="bi bi-book"></i>

      </div>

    </div>


    <!-- =========================
         BOOK INFORMATION
    ========================== -->

    <div class="book-information">


      <!-- TITLE + STATUS -->

      <div class="book-top">


        <div class="book-heading">

          <h2>
            {{ book.title }}
          </h2>
          <p
            v-if="book.category"
            class="book-category"
          >
            {{ book.category }}
          </p>

        </div>


        <!-- STATUS -->

        <span
          class="status-badge"
          :class="getStatusClass()"
        >

          <i
            v-if="getStatusIcon()"
            :class="getStatusIcon()"
          ></i>

          <span
            v-else
            class="status-dot"
          ></span>

          {{ getStatusLabel() }}

        </span>

      </div>


      <!-- =========================
           BORROW INFORMATION + ACTIONS
      ========================== -->

      <div class="borrow-bottom">


        <!-- INFORMATION -->

        <div class="date-grid">


          <!-- BORROWED DATE -->

          <div class="date-box borrowed-box">

            <div class="date-content">

              <div class="date-icon borrowed-icon">

                <i class="bi bi-calendar-check"></i>

              </div>

              <div class="date-text">

                <p>
                  Borrowed Date
                </p>

                <strong>
                  {{ book.borrowedDate || "-" }}
                </strong>

              </div>

            </div>

          </div>


          <!-- DUE DATE -->

          <div class="date-box due-box">

            <div class="date-content">

              <div class="date-icon due-icon">

                <i class="bi bi-calendar-event"></i>

              </div>

              <div class="date-text">

                <p>
                  Due Date
                </p>

                <strong>
                  {{ book.dueDate || "-" }}
                </strong>

              </div>

            </div>

          </div>


          <!-- RETURNED DATE -->

          <div
            v-if="
              isReturned() &&
              book.returnedDate
            "
            class="date-box returned-box"
          >

            <div class="date-content">

              <div class="date-icon returned-icon ">

                <i class="bi bi-check-circle flex"></i>

              </div>

              <div class="date-text">

                <p>
                  Returned Date
                </p>

                <strong>
                  {{ book.returnedDate }}
                </strong>

              </div>

            </div>

          </div>


          <!-- REJECTED MESSAGE -->

          <div
            v-if="isRejected()"
            class="message-box rejected-box"
          >

            <div class="date-content">

              <div class="date-icon rejected-icon">

                <i class="bi bi-x-circle"></i>

              </div>

              <div class="date-text">

                <p>
                  Borrowing Status
                </p>

                <strong>
                  Your borrowing request was not accepted.
                </strong>

              </div>

            </div>

          </div>


          <!-- RETURN REQUESTED MESSAGE -->

          <div
            v-if="isReturnRequested()"
            class="message-box requested-box"
          >

            <div class="date-content">

              <div class="date-icon requested-icon">

                <i class="bi bi-clock-history"></i>

              </div>

              <div class="date-text">

                <p>
                  Return Status
                </p>

                <strong>
                  Your return request is waiting for approval.
                </strong>

              </div>

            </div>

          </div>


        </div>


        <!-- =========================
             ACTIONS
        ========================== -->

        <div
          v-if="
            !hideReturnButton &&
            (
              getStatus() === 'BORROWED' ||
              getStatus() === 'OVERDUE'
            )
          "
          class="actions"
        >

          <button
            type="button"
            class="return-button"
            :class="
              getStatus() === 'OVERDUE'
                ? 'overdue-button'
                : 'borrowed-button'
            "
            @click="emit('return', book)"
          >

            <i class="bi bi-arrow-left-right"></i>

            Return Book

          </button>


          <!-- OVERDUE -->

          <p
            v-if="getStatus() === 'OVERDUE'"
            class="overdue-text"
          >

            <i class="bi bi-clock-history"></i>

            Overdue by
            {{ daysOverdue(book.dueDate) }}
            days

          </p>

        </div>


      </div>


    </div>

  </div>

</template>


<style scoped>

/* =====================================================
   CARD
===================================================== */

.borrow-card {

  display: flex;

  align-items: stretch;

  gap: 20px;

  width: 100%;

  padding: 20px;

  background: white;

  border-radius: 16px;

  border: 1px solid #f1f5f9;

  box-shadow:
    0 3px 10px
    rgba(0, 0, 0, 0.03);

  transition: 0.25s ease;

}

.borrow-card:hover {

  box-shadow:
    0 8px 24px
    rgba(0, 0, 0, 0.07);

}


/* =====================================================
   BOOK IMAGE
===================================================== */

.book-image {

  width: 112px;

  height: 144px;

  flex: 0 0 112px;

  overflow: hidden;

  border-radius: 12px;

  background: #f3f4f6;

}

.book-image img {

  width: 100%;

  height: 100%;

  object-fit: cover;

}

.book-image-empty {

  width: 100%;
  height: 100%;

  display: flex;

  align-items: center;
  justify-content: center;

  color: #9ca3af;

  font-size: 38px;

}


/* =====================================================
   INFORMATION
===================================================== */

.book-information {

  min-width: 0;

  flex: 1;

  display: flex;

  flex-direction: column;

}


/* =====================================================
   TOP
===================================================== */

.book-top {

  display: flex;

  align-items: flex-start;

  justify-content: space-between;

  gap: 15px;

}

.book-heading {

  min-width: 0;

}

.book-heading h2 {

  margin: 0;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  font-size: 18px;

  font-weight: 600;

  color: #1f2937;

}

.book-author {

  margin: 5px 0 0;

  font-size: 14px;

  color: #6b7280;

}

.book-category {

  margin: 4px 0 0;

  font-size: 12px;

  color: #9ca3af;

}


/* =====================================================
   STATUS
===================================================== */

.status-badge {

  display: inline-flex;

  align-items: center;
  justify-content: center;

  gap: 6px;

  width: fit-content;

  height: fit-content;

  flex-shrink: 0;

  padding: 6px 11px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

  white-space: nowrap;

}

.status-badge i {

  font-size: 11px;

}

.status-dot {

  width: 6px;

  height: 6px;

  border-radius: 50%;

  background: currentColor;

}


/* =====================================================
   BOTTOM
===================================================== */

.borrow-bottom {

  display: flex;

  align-items: flex-start;

  justify-content: space-between;

  gap: 15px;

  margin-top: 18px;

}


/* =====================================================
   DATE GRID
===================================================== */

.date-grid {

  display: grid;

  grid-template-columns:
    repeat(2, minmax(0, 1fr));

  gap: 10px;

  flex: 1;

  min-width: 0;

}

.date-box {

  min-width: 0;

  padding: 10px;

  border-radius: 12px;

}

.borrowed-box {

  background: #eff6ff;

}

.due-box {

  background: #fff7ed;

}

.returned-box {

  background: #f0fdf4;

  grid-column: span 2;

}

.message-box {

  grid-column: span 2;

  padding: 10px;

  border-radius: 12px;

}

.rejected-box {

  background: #f9fafb;

}

.requested-box {

  background: #faf5ff;

}


/* =====================================================
   DATE CONTENT
===================================================== */

.date-content {

  display: flex;

  align-items: center;

  gap: 9px;

}

.date-icon {

  width: 34px;
  height: 34px;

  min-width: 34px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 9px;

}

.borrowed-icon {

  background: #dbeafe;

  color: #2563eb;

}

.due-icon {

  background: #ffedd5;

  color: #ea580c;

}

.returned-icon {

  background: #dcfce7;

  color: #16a34a;

}

.rejected-icon {

  background: #e5e7eb;

  color: #4b5563;

}

.requested-icon {

  background: #f3e8ff;

  color: #9333ea;

}

.date-text {

  min-width: 0;

}

.date-text p {

  margin: 0;

  font-size: 11px;

  color: #9ca3af;

}

.date-text strong {

  display: block;

  margin-top: 2px;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  font-size: 13px;

  font-weight: 600;

  color: #374151;

}


/* =====================================================
   ACTIONS
===================================================== */

.actions {

  width: auto;

  min-width: 125px;

  flex-shrink: 0;

  display: flex;

  flex-direction: column;

  align-items: flex-end;

  gap: 6px;

}

.return-button {

  display: inline-flex;

  align-items: center;

  justify-content: center;

  gap: 7px;

  width: 100%;

  padding: 9px 13px;

  border: none;

  border-radius: 9px;

  color: white;

  font-size: 13px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s ease;

}

.borrowed-button {

  background: #3b82f6;

}

.borrowed-button:hover {

  background: #2563eb;

}

.overdue-button {

  background: #ef4444;

}

.overdue-button:hover {

  background: #dc2626;

}

.overdue-text {

  margin: 0;

  font-size: 11px;

  font-weight: 500;

  color: #ef4444;

  white-space: nowrap;

}


/* =====================================================
   MOBILE
   Same Desktop layout, only smaller
===================================================== */

@media (max-width: 768px) {

  .borrow-card {

    gap: 14px;

    padding: 14px;

    border-radius: 14px;

  }


  /* IMAGE */

  .book-image {

    width: 88px;

    height: 116px;

    flex: 0 0 88px;

    border-radius: 10px;

  }

  .book-image-empty {

    font-size: 30px;

  }


  /* TOP */

  .book-top {

    gap: 8px;

  }

  .book-heading h2 {

    font-size: 15px;

  }

  .book-author {

    margin-top: 3px;

    font-size: 12px;

  }

  .book-category {

    margin-top: 3px;

    font-size: 10px;

  }


  /* STATUS */

  .status-badge {

    padding: 5px 8px;

    gap: 4px;

    font-size: 10px;

  }

  .status-badge i {

    font-size: 9px;

  }


  /* BOTTOM */

  .borrow-bottom {

    gap: 8px;

    margin-top: 12px;

  }


  /* DATE */

  .date-grid {

    gap: 7px;

  }

  .date-box,
  .message-box {

    padding: 8px;

    border-radius: 9px;

  }

  .date-content {

    gap: 6px;

  }

  .date-icon {

    width: 28px;
    height: 28px;

    min-width: 28px;

    border-radius: 7px;

    font-size: 11px;

  }

  .date-text p {

    font-size: 9px;

  }

  .date-text strong {

    font-size: 10px;

  }


  /* ACTION */

  .actions {

    min-width: 100px;

  }

  .return-button {

    padding: 8px 9px;

    gap: 5px;

    font-size: 11px;

    white-space: nowrap;

  }

  .overdue-text {

    font-size: 9px;

  }

}


/* =====================================================
   SMALL MOBILE
===================================================== */

/* =====================================================
   MOBILE DATE - TEXT ONLY
===================================================== */

/* =====================================================
   MOBILE DATE - TEXT ONLY / VERTICAL
===================================================== */

@media (max-width: 768px) {

  /* Borrowed Date on top, Due Date below */
  .date-grid {
    display: flex;
    flex-direction: column;
    gap: 5px;
    width: 100%;
  }

  /* Remove date card design */
  .date-box,
  .message-box {
    padding: 0;
    border: none;
    border-radius: 0;
    background: transparent;
    box-shadow: none;
  }

  /* Remove icons */
  .date-icon {
    display: none;
  }

  /* Text only */
  .date-content {
    display: block;
  }

  .date-text {
    min-width: 0;
  }

  .date-text p {
    margin: 0;
    font-size: 9px;
    color: #9ca3af;
  }

  .date-text strong {
    display: block;
    margin-top: 1px;
    font-size: 10px;
    font-weight: 600;
    color: #374151;
  }

}
</style>
```
