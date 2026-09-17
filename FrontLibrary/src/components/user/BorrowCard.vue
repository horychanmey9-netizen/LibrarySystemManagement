
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
    class="flex flex-col gap-5 rounded-2xl border border-gray-100 bg-white p-5 shadow-sm transition duration-200 hover:shadow-md sm:flex-row"
  >

    <!-- =========================
         BOOK IMAGE
    ========================== -->

    <div
      class="h-44 w-full shrink-0 overflow-hidden rounded-xl bg-gray-100 sm:h-36 sm:w-28"
    >

      <img
        v-if="book.image"
        :src="book.image"
        :alt="book.title"
        class="h-full w-full object-cover"
      />

      <div
        v-else
        class="flex h-full w-full items-center justify-center text-gray-400"
      >

        <i class="bi bi-book text-4xl"></i>

      </div>

    </div>


    <!-- =========================
         BOOK INFORMATION
    ========================== -->

    <div class="min-w-0 flex-1">


      <!-- TITLE + STATUS -->

      <div
        class="flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between"
      >

        <div class="min-w-0">

          <h2
            class="truncate text-lg font-semibold text-gray-800"
          >

            {{ book.title }}

          </h2>

          <p
            class="mt-1 text-sm text-gray-500"
          >

            {{ book.author }}

          </p>

          <p
            v-if="book.category"
            class="mt-1 text-xs text-gray-400"
          >

            {{ book.category }}

          </p>

        </div>


        <!-- STATUS -->

        <span
          class="inline-flex h-fit w-fit shrink-0 items-center gap-1.5 rounded-full px-3 py-1 text-xs font-semibold"
          :class="getStatusClass()"
        >

          <i
            v-if="getStatusIcon()"
            :class="getStatusIcon()"
            class="text-[0.7rem]"
          ></i>

          <span
            v-else
            class="h-1.5 w-1.5 rounded-full bg-current"
          ></span>

          {{ getStatusLabel() }}

        </span>

      </div>


      <!-- =========================
           BORROW INFORMATION + ACTIONS
      ========================== -->

      <div
        class="mt-5 flex flex-col gap-3 sm:flex-row sm:items-start sm:justify-between"
      >


        <div
          class="grid w-full grid-cols-1 gap-3 sm:grid-cols-2"
        >


          <!-- BORROWED DATE -->

          <div
            class="rounded-xl bg-blue-50 p-3"
          >

            <div
              class="flex items-center gap-2"
            >

              <div
                class="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-blue-100 text-blue-600"
              >

                <i class="bi bi-calendar-check"></i>

              </div>

              <div class="min-w-0">

                <p
                  class="text-xs text-gray-400"
                >

                  Borrowed Date

                </p>

                <p
                  class="mt-0.5 text-sm font-semibold text-gray-700"
                >

                  {{ book.borrowedDate || "-" }}

                </p>

              </div>

            </div>

          </div>


          <!-- DUE DATE -->

          <div
            class="rounded-xl bg-orange-50 p-3"
          >

            <div
              class="flex items-center gap-2"
            >

              <div
                class="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-orange-100 text-orange-600"
              >

                <i class="bi bi-calendar-event"></i>

              </div>

              <div class="min-w-0">

                <p
                  class="text-xs text-gray-400"
                >

                  Due Date

                </p>

                <p
                  class="mt-0.5 text-sm font-semibold text-gray-700"
                >

                  {{ book.dueDate || "-" }}

                </p>

              </div>

            </div>

          </div>


          <!-- RETURNED DATE -->

          <div
            v-if="
              isReturned() &&
              book.returnedDate
            "
            class="rounded-xl bg-green-50 p-3 sm:col-span-2"
          >

            <div
              class="flex items-center gap-2"
            >

              <div
                class="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-green-100 text-green-600"
              >

                <i class="bi bi-check-circle"></i>

              </div>

              <div class="min-w-0">

                <p
                  class="text-xs text-gray-400"
                >

                  Returned Date

                </p>

                <p
                  class="mt-0.5 text-sm font-semibold text-gray-700"
                >

                  {{ book.returnedDate }}

                </p>

              </div>

            </div>

          </div>


          <!-- REJECTED MESSAGE -->

          <div
            v-if="isRejected()"
            class="rounded-xl bg-gray-50 p-3 sm:col-span-2"
          >

            <div
              class="flex items-center gap-2"
            >

              <div
                class="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-gray-200 text-gray-600"
              >

                <i class="bi bi-x-circle"></i>

              </div>

              <div class="min-w-0">

                <p
                  class="text-xs text-gray-400"
                >

                  Borrowing Status

                </p>

                <p
                  class="mt-0.5 text-sm font-semibold text-gray-700"
                >

                  Your borrowing request was not accepted.

                </p>

              </div>

            </div>

          </div>


          <!-- RETURN REQUESTED MESSAGE -->

          <div
            v-if="isReturnRequested()"
            class="rounded-xl bg-purple-50 p-3 sm:col-span-2"
          >

            <div
              class="flex items-center gap-2"
            >

              <div
                class="flex h-8 w-8 shrink-0 items-center justify-center rounded-lg bg-purple-100 text-purple-600"
              >

                <i class="bi bi-clock-history"></i>

              </div>

              <div class="min-w-0">

                <p
                  class="text-xs text-gray-400"
                >

                  Return Status

                </p>

                <p
                  class="mt-0.5 text-sm font-semibold text-gray-700"
                >

                  Your return request is waiting for approval.

                </p>

              </div>

            </div>

          </div>


        </div>


        <!-- =========================
             ACTIONS
        ========================== -->

        <!--
          RETURN BUTTON ONLY BORROWED
        -->

        <div
          v-if="
            !hideReturnButton &&
            getStatus() === 'BORROWED' ||
            getStatus() === 'OVERDUE'
          "
          class="flex w-full shrink-0 flex-col items-start gap-1.5 sm:w-auto sm:items-end"
        >

          <button
            type="button"
            class="inline-flex w-full items-center justify-center gap-2 rounded-lg px-4 py-2 text-sm font-semibold text-white transition sm:w-auto"
            :class="
              getStatus() === 'OVERDUE'
                ? 'bg-red-500 hover:bg-red-600'
                : 'bg-blue-500 hover:bg-blue-600'
            "
            @click="emit('return', book)"
          >

            <i class="bi bi-arrow-left-right"></i>

            Return Book

          </button>


          <!-- OVERDUE -->

          <p
            v-if="getStatus() === 'OVERDUE'"
            class="text-xs font-medium text-red-500"
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