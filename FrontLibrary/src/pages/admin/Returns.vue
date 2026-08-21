<template>

  <div class="p-6 bg-gray-50 min-h-screen">

    <!-- =========================
         Header
    ========================== -->

    <div class="mb-6">

      <h1 class="text-2xl font-bold text-gray-800">
        Return Management
      </h1>

      <p class="text-gray-500 mt-1">
        Manage returned books
      </p>

    </div>


    <!-- =========================
         Search & Filter
    ========================== -->

    <div class="bg-white p-4 rounded-xl shadow-sm mb-6">

      <div class="flex items-center gap-3">

        <!-- Search -->

        <div class="relative w-72">

          <i
            class="bi bi-search absolute left-3 top-1/2
                   -translate-y-1/2 text-gray-400"
          ></i>

          <input
            v-model="search"
            type="text"
            placeholder="Search..."
            class="w-full pl-9 pr-3 py-2
                   border border-gray-200
                   rounded-lg text-sm
                   focus:outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          />

        </div>


        <!-- Status Filter -->

        <select
          v-model="statusFilter"
          class="w-32 px-3 py-2
                 border border-gray-200
                 rounded-lg text-sm
                 text-gray-600
                 focus:outline-none
                 focus:ring-2
                 focus:ring-blue-500"
        >

          <option value="All">
            All
          </option>

          <option value="Returned">
            Returned
          </option>

          <option value="Late">
            Late
          </option>

        </select>

      </div>

    </div>


    <!-- =========================
         Return Table
    ========================== -->

    <div class="bg-white rounded-xl shadow-sm overflow-hidden">

      <div class="overflow-x-auto">

        <table class="w-full text-left">

          <!-- =========================
               Table Header
          ========================== -->

          <thead class="bg-gray-50 border-b border-gray-200">

            <tr>

              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                ID
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                User
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                Book
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                Borrow Date
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                Due Date
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                Return Date
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
                Fine
              </th>


              <th
                class="px-6 py-4 text-sm
                       font-semibold text-gray-600"
              >
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


          <!-- =========================
               Table Body
          ========================== -->

          <tbody>

            <tr
              v-for="item in filteredReturns"
              :key="item.id"
              class="border-b border-gray-100
                     hover:bg-gray-50 transition"
            >

              <!-- ID -->

              <td class="px-6 py-4 text-gray-600">

                #{{ item.id }}

              </td>


              <!-- User -->

              <td class="px-6 py-4">

                <div class="flex items-center gap-3">

                  <div
                    class="w-9 h-9 rounded-full
                           bg-blue-100 text-blue-600
                           flex items-center
                           justify-center
                           font-semibold"
                  >

                    {{ item.user.charAt(0) }}

                  </div>


                  <div>

                    <p class="font-medium text-gray-800">

                      {{ item.user }}

                    </p>


                    <p class="text-xs text-gray-400">

                      {{ item.email }}

                    </p>

                  </div>

                </div>

              </td>


              <!-- Book -->

              <td class="px-6 py-4">

                <span class="font-medium text-gray-800">

                  {{ item.book }}

                </span>

              </td>


              <!-- Borrow Date -->

              <td class="px-6 py-4 text-gray-600">

                {{ item.borrowDate }}

              </td>


              <!-- Due Date -->

              <td class="px-6 py-4 text-gray-600">

                {{ item.dueDate }}

              </td>


              <!-- Return Date -->

              <td class="px-6 py-4 text-gray-600">

                {{ item.returnDate }}

              </td>


              <!-- Fine -->

              <td class="px-6 py-4">

                <span
                  :class="
                    item.fine > 0
                      ? 'text-red-600 font-medium'
                      : 'text-green-600 font-medium'
                  "
                >

                  ${{ Number(item.fine).toFixed(2) }}

                </span>

              </td>


              <!-- Status -->

              <td class="px-6 py-4">

                <span
                  :class="getStatusClass(item.status)"
                  class="px-3 py-1 rounded-full
                         text-xs font-medium"
                >

                  {{ item.status }}

                </span>

              </td>


              <!-- Action -->

              <td class="px-6 py-4">

                <div class="flex justify-center gap-2">

                  <!-- View -->

                  <button
                    @click="viewReturn(item)"
                    class="w-9 h-9 flex items-center
                           justify-center rounded-lg
                           bg-gray-100 text-gray-600
                           hover:bg-gray-200 transition"
                    title="View"
                  >

                    <i class="bi bi-eye"></i>

                  </button>


                  <!-- Delete -->

                  <button
                    @click="deleteReturn(item.id)"
                    class="w-9 h-9 flex items-center
                           justify-center rounded-lg
                           bg-red-50 text-red-600
                           hover:bg-red-100 transition"
                    title="Delete"
                  >

                    <i class="bi bi-trash"></i>

                  </button>

                </div>

              </td>

            </tr>


            <!-- =========================
                 Empty
            ========================== -->

            <tr
              v-if="filteredReturns.length === 0"
            >

              <td
                colspan="9"
                class="px-6 py-10
                       text-center text-gray-500"
              >

                No return records found.

              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>


    <!-- =================================================
         View Return Modal
         Same style as Borrowing Details
    ================================================== -->

    <div
      v-if="selectedReturn"
      class="modal-overlay"
      @click.self="closeModal"
    >

      <div class="modal">

        <!-- =========================
             Modal Header
        ========================== -->

        <div class="modal-header">

          <div>

            <h2>
              Return Details
            </h2>

            <p>
              View detailed information about this return
            </p>

          </div>


          <!-- Close Icon -->

          <button
            class="close-icon"
            @click="closeModal"
          >

            ×

          </button>

        </div>


        <!-- =========================
             Details
        ========================== -->

        <div class="details-body">

          <!-- Return ID -->

          <div class="detail-row">

            <span>
              Return ID
            </span>

            <strong>
              #{{ selectedReturn.id }}
            </strong>

          </div>


          <!-- User -->

          <div class="detail-row">

            <span>
              User
            </span>

            <strong>
              {{ selectedReturn.user }}
            </strong>

          </div>


          <!-- Email -->

          <div class="detail-row">

            <span>
              Email
            </span>

            <strong>
              {{ selectedReturn.email }}
            </strong>

          </div>


          <!-- Book -->

          <div class="detail-row">

            <span>
              Book
            </span>

            <strong>
              {{ selectedReturn.book }}
            </strong>

          </div>


          <!-- Borrow Date -->

          <div class="detail-row">

            <span>
              Borrow Date
            </span>

            <strong>
              {{ selectedReturn.borrowDate }}
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
                  selectedReturn.status === 'Late'
              }"
            >

              {{ selectedReturn.dueDate }}

            </strong>

          </div>


          <!-- Return Date -->

          <div class="detail-row">

            <span>
              Return Date
            </span>

            <strong>

              {{ selectedReturn.returnDate || "Not returned" }}

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
                  selectedReturn.fine > 0
              }"
            >

              ${{ Number(selectedReturn.fine).toFixed(2) }}

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


        <!-- =========================
             Modal Footer
        ========================== -->

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
} from 'vue'


/* =========================
   Search
========================= */

const search = ref('')


/* =========================
   Status Filter
========================= */

const statusFilter = ref('All')


/* =========================
   Selected Return
========================= */

const selectedReturn = ref(null)


/* =========================
   Return Data
========================= */

const returns = ref([

  {
    id: 1,
    user: 'Dara Sok',
    email: 'dara@gmail.com',
    book: 'Clean Code',
    borrowDate: '2026-07-20',
    dueDate: '2026-08-03',
    returnDate: '2026-08-02',
    fine: 0,
    status: 'Returned'
  },


  {
    id: 2,
    user: 'Sreyneang Kim',
    email: 'sreyneang@gmail.com',
    book: 'Java Programming',
    borrowDate: '2026-07-15',
    dueDate: '2026-07-29',
    returnDate: '2026-08-01',
    fine: 1.50,
    status: 'Late'
  },


  {
    id: 3,
    user: 'Rithy Chan',
    email: 'rithy@gmail.com',
    book: 'Database System',
    borrowDate: '2026-07-10',
    dueDate: '2026-07-24',
    returnDate: '2026-07-24',
    fine: 0,
    status: 'Returned'
  },


  {
    id: 4,
    user: 'Sokha Lim',
    email: 'sokha@gmail.com',
    book: 'Web Development',
    borrowDate: '2026-07-05',
    dueDate: '2026-07-19',
    returnDate: '2026-07-23',
    fine: 2.00,
    status: 'Late'
  },


  {
    id: 5,
    user: 'Vanna Chea',
    email: 'vanna@gmail.com',
    book: 'Python Programming',
    borrowDate: '2026-07-25',
    dueDate: '2026-08-08',
    returnDate: '2026-08-07',
    fine: 0,
    status: 'Returned'
  }

])


/* =========================
   Search + Filter
========================= */

const filteredReturns = computed(() => {

  return returns.value.filter(item => {

    const keyword =
      search.value
        .toLowerCase()
        .trim()


    /* Search User or Book */

    const matchesSearch =
      item.user
        .toLowerCase()
        .includes(keyword) ||

      item.book
        .toLowerCase()
        .includes(keyword)


    /* Status */

    const matchesStatus =
      statusFilter.value === 'All' ||

      item.status ===
      statusFilter.value


    return (
      matchesSearch &&
      matchesStatus
    )

  })

})


/* =========================
   Status Style
========================= */

const getStatusClass = (status) => {

  if (status === 'Returned') {

    return 'bg-green-100 text-green-700'

  }


  if (status === 'Late') {

    return 'bg-red-100 text-red-700'

  }


  return 'bg-gray-100 text-gray-700'

}


/* =========================
   View Return
========================= */

const viewReturn = (item) => {

  selectedReturn.value = item

}


/* =========================
   Close Modal
========================= */

const closeModal = () => {

  selectedReturn.value = null

}


/* =========================
   Delete Return
========================= */

const deleteReturn = (id) => {

  const confirmed = confirm(
    'Are you sure you want to delete this return record?'
  )


  if (!confirmed) {

    return

  }


  returns.value =
    returns.value.filter(
      item => item.id !== id
    )


  /*

    If the deleted return is
    currently open in the modal,
    close the modal.

  */

  if (
    selectedReturn.value?.id === id
  ) {

    selectedReturn.value = null

  }

}

</script>


<style scoped>

/* =========================
   Modal Overlay
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
   Modal Header
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


/* =========================
   Modal Footer
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