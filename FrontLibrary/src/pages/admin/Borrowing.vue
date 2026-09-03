<template>

  <div class="min-h-screen bg-slate-50 p-6">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="mb-6">

      <div class="flex items-center justify-between">

        <div>

          <h1 class="text-2xl font-bold text-slate-800">
            Borrowing
          </h1>

          <p class="mt-1 text-sm text-slate-500">
            Manage borrowing requests
          </p>

        </div>


        <button
          type="button"
          @click="loadBorrowings"
          :disabled="loading"
          class="px-4 py-2.5 rounded-xl
                 bg-white border border-slate-200
                 text-slate-700 font-semibold
                 hover:bg-slate-50
                 disabled:opacity-50"
        >

          <span v-if="loading">
            Loading...
          </span>

          <span v-else>
            ↻ Refresh
          </span>

        </button>

      </div>

    </div>


    <!-- =====================================================
         STATISTICS
    ====================================================== -->

    <div
      class="grid grid-cols-1
             sm:grid-cols-2
             lg:grid-cols-4
             gap-4 mb-6"
    >

      <!-- PENDING -->

      <div
        class="bg-white rounded-2xl
               border border-slate-200
               shadow-sm p-5"
      >

        <p class="text-sm text-slate-500">
          Pending
        </p>

        <p class="text-2xl font-bold
                  text-amber-600 mt-1">
          {{ pendingCount }}
        </p>

      </div>


      <!-- BORROWED -->

      <div
        class="bg-white rounded-2xl
               border border-slate-200
               shadow-sm p-5"
      >

        <p class="text-sm text-slate-500">
          Borrowed
        </p>

        <p class="text-2xl font-bold
                  text-blue-600 mt-1">
          {{ borrowedCount }}
        </p>

      </div>


      <!-- RETURNED -->

      <div
        class="bg-white rounded-2xl
               border border-slate-200
               shadow-sm p-5"
      >

        <p class="text-sm text-slate-500">
          Returned
        </p>

        <p class="text-2xl font-bold
                  text-green-600 mt-1">
          {{ returnedCount }}
        </p>

      </div>


      <!-- REJECTED -->

      <div
        class="bg-white rounded-2xl
               border border-slate-200
               shadow-sm p-5"
      >

        <p class="text-sm text-slate-500">
          Rejected
        </p>

        <p class="text-2xl font-bold
                  text-red-600 mt-1">
          {{ rejectedCount }}
        </p>

      </div>

    </div>


    <!-- =====================================================
         SEARCH + FILTER
    ====================================================== -->

    <div
      class="bg-white rounded-2xl
             border border-slate-200
             shadow-sm mb-6 p-5"
    >

      <div
        class="flex flex-col
               lg:flex-row
               lg:items-center
               lg:justify-between
               gap-4"
      >

        <!-- SEARCH -->

        <div class="relative w-full lg:max-w-md">

          <span
            class="absolute left-4 top-1/2
                   -translate-y-1/2
                   text-slate-400"
          >
            🔍
          </span>

          <input
            v-model="search"
            type="text"
            placeholder="Search book or borrower..."
            class="w-full pl-11 pr-4 py-3
                   border border-slate-200
                   rounded-xl outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          />

        </div>


        <!-- STATUS -->

        <div class="flex flex-wrap gap-2">

          <button
            v-for="status in statuses"
            :key="status"
            type="button"
            @click="selectedStatus = status"
            class="px-4 py-2 rounded-lg
                   text-sm font-semibold"
            :class="
              selectedStatus === status
                ? 'bg-blue-600 text-white'
                : 'bg-slate-100 text-slate-600 hover:bg-slate-200'
            "
          >

            {{ status }}

          </button>

        </div>

      </div>

    </div>


    <!-- =====================================================
         ERROR
    ====================================================== -->

    <div
      v-if="error"
      class="mb-6 p-4 rounded-xl
             bg-red-50
             border border-red-200
             text-red-700"
    >

      {{ error }}

    </div>


    <!-- =====================================================
         TABLE
    ====================================================== -->

    <div
      class="bg-white rounded-2xl
             border border-slate-200
             shadow-sm overflow-hidden"
    >

      <!-- LOADING -->

      <div
        v-if="loading"
        class="py-20 text-center"
      >

        <div
          class="w-10 h-10
                 border-4 border-blue-200
                 border-t-blue-600
                 rounded-full
                 animate-spin mx-auto"
        ></div>

        <p class="mt-4 text-sm text-slate-500">
          Loading borrowing requests...
        </p>

      </div>


      <!-- EMPTY -->

      <div
        v-else-if="filteredBorrowings.length === 0"
        class="py-20 text-center"
      >

        <div class="text-5xl mb-4">
          📭
        </div>

        <h3 class="font-bold text-slate-700">
          No borrowing requests
        </h3>

        <p class="text-sm text-slate-400 mt-1">
          No records found.
        </p>

      </div>


      <!-- TABLE -->

      <div
        v-else
        class="overflow-x-auto"
      >

        <table class="w-full min-w-[1000px]">

          <thead
            class="bg-slate-50
                   border-b border-slate-200"
          >

            <tr>

              <th
                class="px-6 py-4 text-left
                       text-xs font-bold
                       uppercase
                       text-slate-500"
              >
                Book
              </th>

              <th
                class="px-6 py-4 text-left
                       text-xs font-bold
                       uppercase
                       text-slate-500"
              >
                Borrower
              </th>

              <th
                class="px-6 py-4 text-left
                       text-xs font-bold
                       uppercase
                       text-slate-500"
              >
                Borrow Date
              </th>

              <th
                class="px-6 py-4 text-left
                       text-xs font-bold
                       uppercase
                       text-slate-500"
              >
                Due Date
              </th>

              <th
                class="px-6 py-4 text-left
                       text-xs font-bold
                       uppercase
                       text-slate-500"
              >
                Status
              </th>

              <th
                class="px-6 py-4 text-right
                       text-xs font-bold
                       uppercase
                       text-slate-500"
              >
                Action
              </th>

            </tr>

          </thead>


          <tbody
            class="divide-y divide-slate-100"
          >

            <tr
              v-for="item in filteredBorrowings"
              :key="item.id"
              class="hover:bg-slate-50"
            >

              <!-- BOOK -->

              <td class="px-6 py-4">

                <div class="flex items-center gap-3">

                  <div
                    class="w-12 h-14
                           rounded-lg
                           bg-slate-100
                           flex items-center
                           justify-center
                           overflow-hidden"
                  >

                    <img
                      v-if="getBookImage(item)"
                      :src="getBookImage(item)"
                      :alt="getBookTitle(item)"
                      class="w-full h-full object-cover"
                    />

                    <span v-else>
                      📚
                    </span>

                  </div>


                  <div>

                    <p
                      class="font-semibold
                             text-slate-800"
                    >
                      {{ getBookTitle(item) }}
                    </p>

                    <p
                      class="text-xs
                             text-slate-400 mt-1"
                    >
                      Book ID:
                      {{ item.bookId }}
                    </p>

                  </div>

                </div>

              </td>


              <!-- USER -->

              <td class="px-6 py-4">

                <p
                  class="font-semibold
                         text-slate-800"
                >
                  {{ getUserName(item) }}
                </p>

                <p
                  class="text-xs
                         text-slate-400 mt-1"
                >
                  User ID:
                  {{ item.userId }}
                </p>

              </td>


              <!-- BORROW DATE -->

              <td
                class="px-6 py-4
                       text-sm
                       text-slate-600"
              >

                {{ formatDate(item.borrowDate) }}

              </td>


              <!-- DUE DATE -->

              <td
                class="px-6 py-4
                       text-sm
                       text-slate-600"
              >

                {{ formatDate(item.dueDate) }}

              </td>


              <!-- STATUS -->

              <td class="px-6 py-4">

                <span
                  class="inline-flex
                         px-3 py-1.5
                         rounded-full
                         text-xs
                         font-bold"
                  :class="
                    statusClass(item.status)
                  "
                >

                  {{ item.status }}

                </span>

              </td>


              <!-- ACTION -->

              <td class="px-6 py-4">

                <div
                  v-if="
                    String(item.status)
                      .toUpperCase()
                      === 'PENDING'
                  "
                  class="flex
                         justify-end
                         gap-2"
                >

                  <!-- ACCEPT -->

                  <button
                    type="button"
                    @click="acceptRequest(item)"
                    :disabled="
                      processingId === item.id
                    "
                    class="px-3 py-2
                           rounded-lg
                           bg-green-600
                           text-white
                           text-sm
                           font-semibold
                           hover:bg-green-700
                           disabled:opacity-50"
                  >

                    <span
                      v-if="
                        processingId === item.id &&
                        processingAction === 'accept'
                      "
                    >
                      Accepting...
                    </span>

                    <span v-else>
                      ✓ Accept
                    </span>

                  </button>


                  <!-- REJECT -->

                  <button
                    type="button"
                    @click="rejectRequest(item)"
                    :disabled="
                      processingId === item.id
                    "
                    class="px-3 py-2
                           rounded-lg
                           bg-red-50
                           text-red-600
                           border
                           border-red-200
                           text-sm
                           font-semibold
                           hover:bg-red-100
                           disabled:opacity-50"
                  >

                    <span
                      v-if="
                        processingId === item.id &&
                        processingAction === 'reject'
                      "
                    >
                      Rejecting...
                    </span>

                    <span v-else>
                      ✕ Reject
                    </span>

                  </button>

                </div>


                <div
                  v-else
                  class="text-right
                         text-xs
                         text-slate-400"
                >
                  No action
                </div>

              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>

  </div>

</template>


<script>

import {
  getBorrowings,
  acceptBorrowing,
  rejectBorrowing
} from "../../service/borrowingService.js";


export default {

  name: "Borrowing",


  data() {

    return {

      borrowings: [],

      loading: false,

      error: "",

      search: "",

      selectedStatus: "ALL",

      statuses: [
        "ALL",
        "PENDING",
        "BORROWED",
        "LATE",
        "RETURNED",
        "REJECTED"
      ],

      processingId: null,

      processingAction: ""

    };

  },


  computed: {

    // ===================================================
    // PENDING
    // ===================================================

    pendingCount() {

      return this.borrowings.filter(
        item =>
          String(item.status)
            .toUpperCase() === "PENDING"
      ).length;

    },


    // ===================================================
    // BORROWED
    // ===================================================

    borrowedCount() {

      return this.borrowings.filter(
        item =>
          String(item.status)
            .toUpperCase() === "BORROWED"
      ).length;

    },


    // ===================================================
    // RETURNED
    // ===================================================

    returnedCount() {

      return this.borrowings.filter(
        item =>
          String(item.status)
            .toUpperCase() === "RETURNED"
      ).length;

    },


    // ===================================================
    // REJECTED
    // ===================================================

    rejectedCount() {

      return this.borrowings.filter(
        item =>
          String(item.status)
            .toUpperCase() === "REJECTED"
      ).length;

    },


    // ===================================================
    // FILTER
    // ===================================================

    filteredBorrowings() {

      const keyword =
        this.search
          .trim()
          .toLowerCase();


      return this.borrowings.filter(
        item => {

          const status =
            String(
              item.status || ""
            ).toUpperCase();


          if (
            this.selectedStatus !== "ALL" &&
            status !== this.selectedStatus
          ) {

            return false;

          }


          if (!keyword) {

            return true;

          }


          const book =
            this.getBookTitle(item)
              .toLowerCase();


          const user =
            this.getUserName(item)
              .toLowerCase();


          return (
            book.includes(keyword) ||
            user.includes(keyword) ||
            String(item.bookId)
              .includes(keyword) ||
            String(item.userId)
              .includes(keyword)
          );

        }
      );

    }

  },


  mounted() {

    this.loadBorrowings();

  },


  methods: {

    // =================================================
    // LOAD
    // =================================================

    async loadBorrowings() {

      this.loading = true;

      this.error = "";


      try {

        const response =
          await getBorrowings();


        if (
          Array.isArray(response)
        ) {

          this.borrowings =
            response;

        }

        else if (
          Array.isArray(
            response?.data
          )
        ) {

          this.borrowings =
            response.data;

        }

        else {

          this.borrowings =
            [];

        }


      } catch (error) {

        console.error(
          "Load borrowing error:",
          error
        );


        this.error =
          error?.message ||
          "Failed to load borrowing.";

      } finally {

        this.loading =
          false;

      }

    },


    // =================================================
    // ACCEPT
    // =================================================

    async acceptRequest(item) {

      if (!item?.id) {

        return;

      }


      const confirmed =
        window.confirm(
          `Accept "${this.getBookTitle(item)}" borrowing request?\n\nBook quantity will decrease by 1.`
        );


      if (!confirmed) {

        return;

      }


      this.processingId =
        item.id;

      this.processingAction =
        "accept";

      this.error =
        "";


      try {

        await acceptBorrowing(
          item.id
        );


        alert(
          "Borrowing accepted successfully."
        );


        await this.loadBorrowings();


      } catch (error) {

        console.error(
          "Accept error:",
          error
        );


        this.error =
          error?.message ||
          "Failed to accept borrowing.";

      } finally {

        this.processingId =
          null;

        this.processingAction =
          "";

      }

    },


    // =================================================
    // REJECT
    // =================================================

    async rejectRequest(item) {

      if (!item?.id) {

        return;

      }


      const confirmed =
        window.confirm(
          `Reject "${this.getBookTitle(item)}" borrowing request?`
        );


      if (!confirmed) {

        return;

      }


      this.processingId =
        item.id;

      this.processingAction =
        "reject";

      this.error =
        "";


      try {

        await rejectBorrowing(
          item.id
        );


        alert(
          "Borrowing rejected successfully."
        );


        await this.loadBorrowings();


      } catch (error) {

        console.error(
          "Reject error:",
          error
        );


        this.error =
          error?.message ||
          "Failed to reject borrowing.";

      } finally {

        this.processingId =
          null;

        this.processingAction =
          "";

      }

    },


    // =================================================
    // BOOK TITLE
    // =================================================

    getBookTitle(item) {

      return (
        item?.bookTitle ||
        item?.title ||
        item?.book?.title ||
        "Unknown Book"
      );

    },


    // =================================================
    // BOOK IMAGE
    // =================================================

    getBookImage(item) {

      return (
        item?.bookImage ||
        item?.image ||
        item?.book?.image ||
        ""
      );

    },


    // =================================================
    // USER NAME
    // =================================================

    getUserName(item) {

      return (
        item?.userName ||
        item?.name ||
        item?.user?.name ||
        item?.user?.fullName ||
        "Unknown User"
      );

    },


    // =================================================
    // DATE
    // =================================================

    formatDate(date) {

      if (!date) {

        return "-";

      }


      const value =
        new Date(date);


      if (
        Number.isNaN(
          value.getTime()
        )
      ) {

        return date;

      }


      return value.toLocaleDateString(
        "en-US",
        {
          year: "numeric",
          month: "short",
          day: "numeric"
        }
      );

    },


    // =================================================
    // STATUS
    // =================================================

    statusClass(status) {

      switch (
        String(
          status || ""
        ).toUpperCase()
      ) {

        case "PENDING":

          return "bg-amber-50 text-amber-700";


        case "BORROWED":

          return "bg-blue-50 text-blue-700";


        case "LATE":

          return "bg-orange-50 text-orange-700";


        case "RETURNED":

          return "bg-green-50 text-green-700";


        case "REJECTED":

          return "bg-red-50 text-red-700";


        default:

          return "bg-slate-100 text-slate-600";

      }

    }

  }

};

</script>