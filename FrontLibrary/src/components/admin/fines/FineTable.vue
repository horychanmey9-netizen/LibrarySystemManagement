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

            <!-- ID -->

            <td>

              <span class="id-number">
                {{ fine.id }}
              </span>

            </td>


            <!-- User -->

            <td>

              <div class="user-info">

                <div class="avatar">
                  {{ getInitials(fine.user) }}
                </div>

                <span>
                  {{ fine.user }}
                </span>

              </div>

            </td>


            <!-- Book -->

            <td>

              <span class="book-name">
                {{ fine.book }}
              </span>

            </td>


            <!-- Days Late -->

            <td>

              <span class="late-days">
                {{ fine.daysLate }} days
              </span>

            </td>


            <!-- Amount -->

            <td>

              <strong class="amount">
                ${{ Number(fine.amount).toFixed(2) }}
              </strong>

            </td>


            <!-- Status -->

            <td>

              <span
                class="status-badge"
                :class="fine.status.toLowerCase()"
              >
                {{ fine.status }}
              </span>

            </td>


            <!-- Actions -->

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
                  :disabled="fine.status === 'Paid'"
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

/* =========================
   Props
========================= */

defineProps({

  fines: {
    type: Array,
    default: () => []
  }

});


/* =========================
   Events
========================= */

const emit = defineEmits([
  "view",
  "mark-paid",
  "delete"
]);


/* =========================
   View Fine
========================= */

function viewFine(fine) {

  emit(
    "view",
    fine
  );

}


/* =========================
   Mark As Paid
========================= */

function markPaid(fine) {

  if (!fine) return;

  emit(
    "mark-paid",
    fine
  );

}


/* =========================
   Delete Fine
========================= */

function deleteFine(id) {

  emit(
    "delete",
    id
  );

}


/* =========================
   User Initials
========================= */

function getInitials(name) {

  if (!name) return "";

  return name
    .split(" ")
    .map(
      word => word[0]
    )
    .join("")
    .toUpperCase();

}

</script>


<style scoped>

/* =========================
   Card
========================= */

.table-card {

  overflow: hidden;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 10px;

}


/* =========================
   Header
========================= */

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

}


.table-header p {

  margin: 5px 0 0;

  color: #6b7280;

  font-size: 13px;

}


.fine-count {

  padding: 6px 10px;

  border-radius: 6px;

  background: #f3f4f6;

  color: #4b5563;

  font-size: 13px;

}


/* =========================
   Table
========================= */

.table-wrapper {

  overflow-x: auto;

}


table {

  width: 100%;

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

}


td {

  padding: 15px 18px;

  border-top:
    1px solid #f1f5f9;

  color: #374151;

  font-size: 14px;

}


tbody tr:hover {

  background: #fafafa;

}


/* =========================
   ID
========================= */

.id-number {

  color: #6b7280;

  font-weight: 600;

}


/* =========================
   User
========================= */

.user-info {

  display: flex;

  align-items: center;

  gap: 10px;

}


.avatar {

  width: 34px;

  height: 34px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  background: #e0e7ff;

  color: #4338ca;

  font-size: 12px;

  font-weight: 700;

}


/* =========================
   Book
========================= */

.book-name {

  color: #374151;

  font-weight: 500;

}


/* =========================
   Late Days
========================= */

.late-days {

  color: #dc2626;

}


/* =========================
   Amount
========================= */

.amount {

  color: #111827;

}


/* =========================
   Status
========================= */

.status-badge {

  display: inline-flex;

  align-items: center;

  padding: 5px 10px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


.status-badge.paid {

  background: #dcfce7;

  color: #15803d;

}


.status-badge.unpaid {

  background: #fee2e2;

  color: #dc2626;

}


/* =========================
   Actions
========================= */

.actions {

  display: flex;

  gap: 7px;

}


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

  transition: 0.2s;

}


/* =========================
   View Button
========================= */

.action-btn.view {

  background: #eff6ff;

  color: #2563eb;

}


.action-btn.view:hover {

  background: #dbeafe;

}


/* =========================
   Paid Button
========================= */

.action-btn.paid {

  background: #ecfdf5;

  color: #16a34a;

}


.action-btn.paid:hover {

  background: #dcfce7;

}


.action-btn.paid:disabled {

  opacity: 0.5;

  cursor: not-allowed;

}


/* =========================
   Delete Button
========================= */

.action-btn.delete {

  background: #fef2f2;

  color: #dc2626;

}


.action-btn.delete:hover {

  background: #fee2e2;

}


/* =========================
   Empty
========================= */

.empty-state {

  padding: 60px 20px;

  text-align: center;

}


.empty-icon {

  width: 60px;

  height: 60px;

  margin: 0 auto 15px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  background: #f1f5f9;

  color: #64748b;

  font-size: 28px;

}


.empty-state h3 {

  margin: 0;

  color: #374151;

}


.empty-state p {

  margin-top: 5px;

  color: #9ca3af;

  font-size: 14px;

}

</style>