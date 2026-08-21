<template>

  <div class="table-card">

    <!-- =========================
         Table Header
    ========================== -->

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

            <th>Borrow Date</th>

            <th>Due Date</th>

            <th>Status</th>

            <th>Action</th>

          </tr>

        </thead>


        <tbody>

          <!-- =====================
               Data
          ====================== -->

          <tr
            v-for="item in borrowings"
            :key="item.id"
          >

            <!-- ID -->

            <td>

              <span class="id-number">

                #{{ item.id }}

              </span>

            </td>


            <!-- User -->

            <td>

              <div class="user-info">

                <div class="avatar">

                  {{ getInitials(item.user) }}

                </div>


                <div>

                  <span class="user-name">

                    {{ item.user }}

                  </span>

                  <small>

                    {{ item.email }}

                  </small>

                </div>

              </div>

            </td>


            <!-- Book -->

            <td>

              <span class="book-name">

                {{ item.book }}

              </span>

            </td>


            <!-- Borrow Date -->

            <td>

              {{ item.borrowDate }}

            </td>


            <!-- Due Date -->

            <td>

              <span
                :class="{
                  'late-date':
                    item.status === 'Late'
                }"
              >

                {{ item.dueDate }}

              </span>

            </td>


            <!-- Status -->

            <td>

              <span
                class="status-badge"
                :class="
                  item.status.toLowerCase()
                "
              >

                <i
                  :class="
                    item.status === 'Borrowed'
                      ? 'bi bi-book'
                      : item.status === 'Late'
                        ? 'bi bi-clock'
                        : 'bi bi-check-circle'
                  "
                ></i>

                {{ item.status }}

              </span>

            </td>


            <!-- Actions -->

            <td>

              <div class="actions">

                <!-- View -->

                <button
                  class="action-btn view"
                  title="View Details"
                  @click="
                    $emit('view', item)
                  "
                >

                  <i class="bi bi-eye"></i>

                </button>


                <!-- Return -->

                <button
                  class="action-btn return"
                  title="Return Book"
                  :disabled="
                    item.status === 'Returned'
                  "
                  @click="
                    $emit('return', item)
                  "
                >

                  <i class="bi bi-arrow-return-left"></i>

                </button>


                <!-- Renew -->

                <button
                  class="action-btn renew"
                  title="Renew Book"
                  :disabled="
                    item.status === 'Returned'
                  "
                  @click="
                    $emit('renew', item)
                  "
                >

                  <i class="bi bi-arrow-clockwise"></i>

                </button>

              </div>

            </td>

          </tr>


          <!-- =====================
               Empty State
          ====================== -->

          <tr
            v-if="borrowings.length === 0"
          >

            <td
              colspan="7"
              class="empty-state"
            >

              <div class="empty-icon">

                <i class="bi bi-inbox"></i>

              </div>

              <h3>
                No borrowing records found
              </h3>

              <p>
                Try changing your search or filter.
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

  borrowings: {
    type: Array,
    default: () => []
  }

});


/* =========================
   Events
========================= */

defineEmits([
  "view",
  "return",
  "renew"
]);


/* =========================
   Initials
========================= */

function getInitials(name) {

  if (!name) return "";

  return name
    .split(" ")
    .map(word => word[0])
    .join("")
    .toUpperCase();

}

</script>


<style scoped>

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


.borrowing-count {
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

  min-width: 950px;

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
  width: 36px;

  height: 36px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  background: #dbeafe;

  color: #2563eb;

  font-size: 12px;

  font-weight: 700;
}


.user-info small {
  display: block;

  margin-top: 2px;

  color: #9ca3af;

  font-size: 11px;
}


.user-name {
  color: #374151;

  font-weight: 500;
}


/* =========================
   Book
========================= */

.book-name {
  color: #374151;

  font-weight: 500;
}


/* =========================
   Late Date
========================= */

.late-date {
  color: #dc2626;

  font-weight: 600;
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
   Actions
========================= */

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


/* View */

.action-btn.view {
  background: #eff6ff;

  color: #2563eb;
}


.action-btn.view:hover {
  background: #dbeafe;
}


/* Return */

.action-btn.return {
  background: #ecfdf5;

  color: #059669;
}


.action-btn.return:hover {
  background: #d1fae5;
}


/* Renew */

.action-btn.renew {
  background: #fff7ed;

  color: #ea580c;
}


.action-btn.renew:hover {
  background: #ffedd5;
}


/* Disabled */

.action-btn:disabled {
  opacity: 0.4;

  cursor: not-allowed;
}


/* =========================
   Empty
========================= */

.empty-state {
  padding: 60px 20px;

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
}


.empty-state p {
  margin-top: 5px;

  color: #9ca3af;

  font-size: 14px;
}

</style>