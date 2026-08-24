<template>

  <div class="p-6 bg-gray-50 min-h-screen">


    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="mb-6">

      <h1 class="text-2xl font-bold text-gray-800">
        Return Management
      </h1>

      <p class="text-gray-500 mt-1">
        Manage returned books
      </p>

    </div>


    <!-- =====================================================
         SEARCH & FILTER
    ====================================================== -->

    <div
      class="bg-white p-4 rounded-xl
             shadow-sm mb-6"
    >

      <div
        class="flex flex-wrap
               items-center gap-3"
      >


        <!-- SEARCH -->

        <div class="relative w-72">

          <i
            class="bi bi-search
                   absolute left-3 top-1/2
                   -translate-y-1/2
                   text-gray-400"
          ></i>

          <input
            v-model="search"
            type="text"
            placeholder="Search user or book..."
            class="w-full pl-9 pr-3 py-2
                   border border-gray-200
                   rounded-lg text-sm
                   focus:outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          />

        </div>


        <!-- STATUS -->

        <select
          v-model="statusFilter"
          class="w-36 px-3 py-2
                 border border-gray-200
                 rounded-lg text-sm
                 text-gray-600
                 focus:outline-none
                 focus:ring-2
                 focus:ring-blue-500"
        >

          <option value="All">
            All Status
          </option>

          <option value="Returned">
            Returned
          </option>

          <option value="Late">
            Late
          </option>

          <option value="Borrowed">
            Borrowed
          </option>

        </select>


        <!-- RESET -->

        <button
          v-if="search || statusFilter !== 'All'"
          type="button"
          @click="resetFilter"
          class="px-4 py-2
                 border border-gray-200
                 rounded-lg
                 text-sm text-gray-600
                 hover:bg-gray-50
                 transition"
        >

          <i class="bi bi-arrow-counterclockwise mr-1"></i>

          Reset

        </button>

      </div>

    </div>


    <!-- =====================================================
         SUMMARY
    ====================================================== -->

    <div
      class="grid grid-cols-1
             sm:grid-cols-2
             lg:grid-cols-4
             gap-4 mb-6"
    >

      <!-- Total -->

      <div
        class="bg-white rounded-xl
               shadow-sm p-5
               border border-gray-100"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Total Returns
            </p>

            <h2 class="text-2xl font-bold text-gray-800 mt-1">
              {{ returns.length }}
            </h2>

          </div>

          <div
            class="w-11 h-11 rounded-lg
                   bg-blue-50 text-blue-600
                   flex items-center justify-center"
          >

            <i class="bi bi-arrow-return-left text-xl"></i>

          </div>

        </div>

      </div>


      <!-- Returned -->

      <div
        class="bg-white rounded-xl
               shadow-sm p-5
               border border-gray-100"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Returned
            </p>

            <h2 class="text-2xl font-bold text-green-600 mt-1">
              {{ returnedCount }}
            </h2>

          </div>

          <div
            class="w-11 h-11 rounded-lg
                   bg-green-50 text-green-600
                   flex items-center justify-center"
          >

            <i class="bi bi-check-circle text-xl"></i>

          </div>

        </div>

      </div>


      <!-- Late -->

      <div
        class="bg-white rounded-xl
               shadow-sm p-5
               border border-gray-100"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Late Returns
            </p>

            <h2 class="text-2xl font-bold text-red-600 mt-1">
              {{ lateCount }}
            </h2>

          </div>

          <div
            class="w-11 h-11 rounded-lg
                   bg-red-50 text-red-600
                   flex items-center justify-center"
          >

            <i class="bi bi-clock-history text-xl"></i>

          </div>

        </div>

      </div>


      <!-- Fine -->

      <div
        class="bg-white rounded-xl
               shadow-sm p-5
               border border-gray-100"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Total Fine
            </p>

            <h2 class="text-2xl font-bold text-orange-600 mt-1">
              ${{ totalFine.toFixed(2) }}
            </h2>

          </div>

          <div
            class="w-11 h-11 rounded-lg
                   bg-orange-50 text-orange-600
                   flex items-center justify-center"
          >

            <i class="bi bi-cash-stack text-xl"></i>

          </div>

        </div>

      </div>

    </div>


    <!-- =====================================================
         TABLE
    ====================================================== -->

    <div
      class="bg-white rounded-xl
             shadow-sm overflow-hidden"
    >

      <div class="overflow-x-auto">

        <table class="w-full text-left">


          <!-- TABLE HEADER -->

          <thead
            class="bg-gray-50
                   border-b border-gray-200"
          >

            <tr>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                ID
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                User
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Book
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Borrow Date
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Due Date
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Return Date
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Fine
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Status
              </th>

              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600
                       text-center"
              >
                Action
              </th>

            </tr>

          </thead>


          <!-- TABLE BODY -->

          <tbody>

            <ReturnCard
              v-for="item in filteredReturns"
              :key="item.id"
              :item="item"
              @view="viewReturn"
              @delete="deleteReturn"
            />


            <!-- EMPTY -->

            <tr
              v-if="filteredReturns.length === 0"
            >

              <td
                colspan="9"
                class="px-6 py-12
                       text-center text-gray-500"
              >

                <div class="flex flex-col items-center">

                  <i
                    class="bi bi-inbox
                           text-4xl text-gray-300
                           mb-3"
                  ></i>

                  <p class="font-medium">
                    No return records found
                  </p>

                  <p class="text-sm text-gray-400 mt-1">
                    Try changing your search or filter.
                  </p>

                </div>

              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>


    <!-- =====================================================
         VIEW RETURN MODAL
    ====================================================== -->

    <div
      v-if="selectedReturn"
      class="modal-overlay"
      @click.self="closeModal"
    >

      <div class="modal">


        <!-- HEADER -->

        <div class="modal-header">

          <div>

            <div class="flex items-center gap-3">

              <div
                class="w-10 h-10 rounded-lg
                       bg-blue-50 text-blue-600
                       flex items-center justify-center"
              >

                <i class="bi bi-arrow-return-left"></i>

              </div>

              <div>

                <h2>
                  Return Details
                </h2>

                <p>
                  Return #{{ selectedReturn.id }}
                </p>

              </div>

            </div>

          </div>


          <!-- CLOSE -->

          <button
            class="close-icon"
            @click="closeModal"
          >

            ×

          </button>

        </div>


        <!-- BODY -->

        <div class="details-body">


          <!-- USER -->

          <div class="detail-row">

            <span>
              User
            </span>

            <div class="text-right">

              <strong class="block">
                {{ selectedReturn.user }}
              </strong>

              <small class="text-gray-400">
                {{ selectedReturn.email }}
              </small>

            </div>

          </div>


          <!-- BOOK -->

          <div class="detail-row">

            <span>
              Book
            </span>

            <strong>
              {{ selectedReturn.book }}
            </strong>

          </div>


          <!-- BORROW DATE -->

          <div class="detail-row">

            <span>
              Borrow Date
            </span>

            <strong>
              {{ selectedReturn.borrowDate }}
            </strong>

          </div>


          <!-- DUE DATE -->

          <div class="detail-row">

            <span>
              Due Date
            </span>

            <strong
              :class="{
                late:
                  selectedReturn.status === 'Late'
              }"
            >

              {{ selectedReturn.dueDate }}

            </strong>

          </div>


          <!-- RETURN DATE -->

          <div class="detail-row">

            <span>
              Return Date
            </span>

            <strong>

              {{
                selectedReturn.returnDate ||
                "Not returned"
              }}

            </strong>

          </div>


          <!-- FINE -->

          <div class="detail-row">

            <span>
              Fine
            </span>

            <strong
              :class="{
                fine:
                  Number(selectedReturn.fine) > 0
              }"
            >

              ${{ Number(selectedReturn.fine || 0).toFixed(2) }}

            </strong>

          </div>


          <!-- STATUS -->

          <div class="detail-row">

            <span>
              Status
            </span>

            <span
              class="status-badge"
              :class="
                selectedReturn.status.toLowerCase()
              "
            >

              <i
                :class="
                  selectedReturn.status === 'Returned'
                    ? 'bi bi-check-circle'
                    : selectedReturn.status === 'Late'
                      ? 'bi bi-clock'
                      : 'bi bi-book'
                "
              ></i>

              {{ selectedReturn.status }}

            </span>

          </div>

        </div>


        <!-- FOOTER -->

        <div class="modal-footer">

          <button
            class="close-btn"
            @click="closeModal"
          >

            Close

          </button>

        </div>

      </div>

    </div>

  </div>

</template>


<script setup>

import {
  ref,
  computed
} from "vue"

import ReturnCard from "@/components/admin/ReturnCard.vue"


// =====================================================
// SEARCH
// =====================================================

const search = ref("")


// =====================================================
// STATUS FILTER
// =====================================================

const statusFilter = ref("All")


// =====================================================
// SELECTED RETURN
// =====================================================

const selectedReturn = ref(null)


// =====================================================
// RETURN DATA
// =====================================================

const returns = ref([

  {
    id: 1,
    user: "Dara Sok",
    email: "dara@gmail.com",
    book: "Clean Code",
    borrowDate: "2026-07-20",
    dueDate: "2026-08-03",
    returnDate: "2026-08-02",
    fine: 0,
    status: "Returned"
  },

  {
    id: 2,
    user: "Sreyneang Kim",
    email: "sreyneang@gmail.com",
    book: "Java Programming",
    borrowDate: "2026-07-15",
    dueDate: "2026-07-29",
    returnDate: "2026-08-01",
    fine: 1.50,
    status: "Late"
  },

  {
    id: 3,
    user: "Rithy Chan",
    email: "rithy@gmail.com",
    book: "Database System",
    borrowDate: "2026-07-10",
    dueDate: "2026-07-24",
    returnDate: "2026-07-24",
    fine: 0,
    status: "Returned"
  },

  {
    id: 4,
    user: "Sokha Lim",
    email: "sokha@gmail.com",
    book: "Web Development",
    borrowDate: "2026-07-05",
    dueDate: "2026-07-19",
    returnDate: "2026-07-23",
    fine: 2.00,
    status: "Late"
  },

  {
    id: 5,
    user: "Vanna Chea",
    email: "vanna@gmail.com",
    book: "Python Programming",
    borrowDate: "2026-07-25",
    dueDate: "2026-08-08",
    returnDate: "2026-08-07",
    fine: 0,
    status: "Returned"
  }

])


// =====================================================
// FILTERED RETURNS
// =====================================================

const filteredReturns = computed(() => {

  const keyword =
    search.value
      .toLowerCase()
      .trim()


  return returns.value.filter(item => {

    const matchesSearch =
      item.user
        .toLowerCase()
        .includes(keyword) ||

      item.email
        .toLowerCase()
        .includes(keyword) ||

      item.book
        .toLowerCase()
        .includes(keyword)


    const matchesStatus =
      statusFilter.value === "All" ||

      item.status ===
      statusFilter.value


    return (
      matchesSearch &&
      matchesStatus
    )

  })

})


// =====================================================
// TOTAL RETURNED
// =====================================================

const returnedCount = computed(() => {

  return returns.value.filter(
    item => item.status === "Returned"
  ).length

})


// =====================================================
// TOTAL LATE
// =====================================================

const lateCount = computed(() => {

  return returns.value.filter(
    item => item.status === "Late"
  ).length

})


// =====================================================
// TOTAL FINE
// =====================================================

const totalFine = computed(() => {

  return returns.value.reduce(
    (total, item) =>
      total + Number(item.fine || 0),
    0
  )

})


// =====================================================
// VIEW RETURN
// =====================================================

const viewReturn = (item) => {

  selectedReturn.value = item

}


// =====================================================
// CLOSE MODAL
// =====================================================

const closeModal = () => {

  selectedReturn.value = null

}


// =====================================================
// DELETE RETURN
// =====================================================

const deleteReturn = (id) => {

  const confirmed = confirm(
    "Are you sure you want to delete this return record?"
  )


  if (!confirmed) {
    return
  }


  returns.value =
    returns.value.filter(
      item => item.id !== id
    )


  if (
    selectedReturn.value?.id === id
  ) {

    selectedReturn.value = null

  }

}


// =====================================================
// RESET FILTER
// =====================================================

const resetFilter = () => {

  search.value = ""

  statusFilter.value = "All"

}

</script>


<style scoped>

/* =====================================================
   MODAL OVERLAY
===================================================== */

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


/* =====================================================
   MODAL
===================================================== */

.modal {

  width: 520px;

  max-width: 100%;

  max-height: 90vh;

  overflow-y: auto;

  background: white;

  border-radius: 16px;

  box-shadow:
    0 20px 50px
    rgba(0, 0, 0, 0.20);

}


/* =====================================================
   MODAL HEADER
===================================================== */

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

  font-weight: 700;

}


.modal-header p {

  margin: 3px 0 0;

  color: #9ca3af;

  font-size: 13px;

}


/* =====================================================
   CLOSE ICON
===================================================== */

.close-icon {

  width: 34px;

  height: 34px;

  display: flex;

  align-items: center;

  justify-content: center;

  border: none;

  border-radius: 8px;

  background: #f3f4f6;

  color: #374151;

  font-size: 22px;

  cursor: pointer;

  transition: 0.2s;

}


.close-icon:hover {

  background: #e5e7eb;

}


/* =====================================================
   DETAILS BODY
===================================================== */

.details-body {

  padding: 20px;

}


/* =====================================================
   DETAIL ROW
===================================================== */

.detail-row {

  min-height: 50px;

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


/* =====================================================
   LATE
===================================================== */

.detail-row .late {

  color: #dc2626;

}


/* =====================================================
   FINE
===================================================== */

.detail-row .fine {

  color: #dc2626;

  font-size: 18px;

}


/* =====================================================
   STATUS
===================================================== */

.status-badge {

  display: inline-flex;

  align-items: center;

  gap: 6px;

  padding:
    6px 11px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


.status-badge.returned {

  background: #dcfce7;

  color: #15803d;

}


.status-badge.late {

  background: #fee2e2;

  color: #dc2626;

}


.status-badge.borrowed {

  background: #dbeafe;

  color: #2563eb;

}


/* =====================================================
   FOOTER
===================================================== */

.modal-footer {

  display: flex;

  justify-content: flex-end;

  padding:
    15px 20px;

  border-top:
    1px solid #e5e7eb;

}


/* =====================================================
   CLOSE BUTTON
===================================================== */

.close-btn {

  height: 40px;

  padding:
    0 18px;

  border:
    1px solid #d1d5db;

  border-radius: 8px;

  background: white;

  color: #374151;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s;

}


.close-btn:hover {

  background: #f9fafb;

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 700px) {

  .modal {

    width: 100%;

  }

}

</style>