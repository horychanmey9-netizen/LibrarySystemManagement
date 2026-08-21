<template>

  <div
    v-if="borrowing"
    class="modal-overlay"
    @click.self="closeDetails"
  >

    <div class="modal">

      <!-- =========================
           Header
      ========================== -->

      <div class="modal-header">

        <div>

          <h2>
            Borrowing Details
          </h2>

          <p>
            View detailed information about this borrowing
          </p>

        </div>


        <button
          class="close-icon"
          @click="closeDetails"
        >
          ×
        </button>

      </div>


      <!-- =========================
           Details
      ========================== -->

      <div class="details-body">

        <!-- ID -->

        <div class="detail-row">

          <span>
            Borrowing ID
          </span>

          <strong>
            #{{ borrowing.id }}
          </strong>

        </div>


        <!-- User -->

        <div class="detail-row">

          <span>
            User
          </span>

          <strong>
            {{ borrowing.user }}
          </strong>

        </div>


        <!-- Email -->

        <div class="detail-row">

          <span>
            Email
          </span>

          <strong>
            {{ borrowing.email }}
          </strong>

        </div>


        <!-- Book -->

        <div class="detail-row">

          <span>
            Book
          </span>

          <strong>
            {{ borrowing.book }}
          </strong>

        </div>


        <!-- Borrow Date -->

        <div class="detail-row">

          <span>
            Borrow Date
          </span>

          <strong>
            {{ borrowing.borrowDate }}
          </strong>

        </div>


        <!-- Due Date -->

        <div class="detail-row">

          <span>
            Due Date
          </span>

          <strong
            :class="{
              late:
                borrowing.status === 'Late'
            }"
          >
            {{ borrowing.dueDate }}
          </strong>

        </div>


        <!-- Return Date -->

        <div class="detail-row">

          <span>
            Return Date
          </span>

          <strong>
            {{ borrowing.returnDate || "Not returned" }}
          </strong>

        </div>


        <!-- Fine -->

        <div class="detail-row">

          <span>
            Fine
          </span>

          <strong
            :class="{
              fine:
                borrowing.fine > 0
            }"
          >
            ${{ Number(borrowing.fine).toFixed(2) }}
          </strong>

        </div>


        <!-- Status -->

        <div class="detail-row">

          <span>
            Status
          </span>

          <span
            class="status-badge"
            :class="
              borrowing.status.toLowerCase()
            "
          >

            <i
              :class="
                borrowing.status === 'Borrowed'
                  ? 'bi bi-book'
                  : borrowing.status === 'Late'
                    ? 'bi bi-clock'
                    : 'bi bi-check-circle'
              "
            ></i>

            {{ borrowing.status }}

          </span>

        </div>

      </div>


      <!-- =========================
           Footer
      ========================== -->

      <div class="modal-footer">

        <button
          class="close-btn"
          @click="closeDetails"
        >
          Close
        </button>

      </div>

    </div>

  </div>

</template>


<script setup>

/* =========================
   Props
========================= */

defineProps({

  borrowing: {
    type: Object,
    default: null
  }

});


/* =========================
   Events
========================= */

const emit = defineEmits([
  "close"
]);


/* =========================
   Close
========================= */

function closeDetails() {

  emit("close");

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
    rgba(0, 0, 0, 0.45);
}


/* =========================
   Modal
========================= */

.modal {
  width: 500px;

  max-width: 100%;

  overflow: hidden;

  background: white;

  border-radius: 12px;

  box-shadow:
    0 20px 50px
    rgba(0, 0, 0, 0.2);
}


/* =========================
   Header
========================= */

.modal-header {
  display: flex;

  align-items: flex-start;

  justify-content: space-between;

  padding: 20px;

  border-bottom:
    1px solid #e5e7eb;
}


.modal-header h2 {
  margin: 0;

  color: #111827;

  font-size: 20px;
}


.modal-header p {
  margin: 5px 0 0;

  color: #6b7280;

  font-size: 13px;
}


/* =========================
   Close Icon
========================= */

.close-icon {
  width: 32px;

  height: 32px;

  display: flex;

  align-items: center;

  justify-content: center;

  border: none;

  border-radius: 6px;

  background: #f3f4f6;

  color: #374151;

  font-size: 22px;

  cursor: pointer;
}


.close-icon:hover {
  background: #e5e7eb;
}


/* =========================
   Details
========================= */

.details-body {
  padding: 20px;
}


.detail-row {
  min-height: 45px;

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 20px;

  border-bottom:
    1px solid #f3f4f6;
}


.detail-row:last-child {
  border-bottom: none;
}


.detail-row > span:first-child {
  color: #6b7280;

  font-size: 14px;
}


.detail-row strong {
  color: #374151;

  font-size: 14px;

  text-align: right;
}


/* =========================
   Late
========================= */

.detail-row .late {
  color: #dc2626;
}


/* =========================
   Fine
========================= */

.detail-row .fine {
  color: #dc2626;

  font-size: 18px;
}


/* =========================
   Status
========================= */

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


/* =========================
   Footer
========================= */

.modal-footer {
  display: flex;

  justify-content: flex-end;

  padding: 15px 20px;

  border-top:
    1px solid #e5e7eb;
}


/* =========================
   Close Button
========================= */

.close-btn {
  height: 40px;

  padding: 0 16px;

  border:
    1px solid #d1d5db;

  border-radius: 7px;

  background: white;

  color: #374151;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;
}


.close-btn:hover {
  background: #f9fafb;
}


/* =========================
   Responsive
========================= */

@media (max-width: 600px) {

  .modal {
    width: 100%;
  }

  .detail-row {
    gap: 15px;
  }

  .detail-row strong {
    max-width: 55%;
  }

}

</style>