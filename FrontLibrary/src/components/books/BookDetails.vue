
<template>
  <!-- ================= OVERLAY ================= -->
  <div
    class="fixed inset-0 z-50
           bg-black/50 backdrop-blur-sm
           flex items-center justify-center
           px-4 py-6"
    @click.self="$emit('close')"
  >

    <!-- ================= MODAL ================= -->
    <div
      class="w-full max-w-5xl
             max-h-[92vh]
             bg-white rounded-2xl
             shadow-2xl overflow-hidden"
    >

      <!-- ================= HEADER ================= -->
      <divgit push origin main
        class="flex items-center justify-between
               px-6 py-4
               border-b border-slate-200"
      >

        <div>
          <h2 class="text-xl font-bold text-slate-800">
            Book Details
          </h2>

          <p class="text-xs text-slate-400 mt-1">
            View complete information about this book
          </p>
        </div>

        <button
          type="button"
          @click="$emit('close')"
          class="w-10 h-10
                 flex items-center justify-center
                 rounded-xl
                 text-slate-500
                 hover:bg-slate-100
                 hover:text-slate-800
                 transition"
        >
          ✕
        </button>

      </divgit>


      <!-- ================= CONTENT ================= -->
      <div
        class="p-6 overflow-y-auto
               max-h-[calc(92vh-80px)]"
      >

        <div
          class="grid grid-cols-1
                 md:grid-cols-5
                 gap-8"
        >

          <!-- ================= IMAGE ================= -->
          <div class="md:col-span-2">

            <div
              class="h-[450px]
                     bg-slate-100
                     rounded-2xl
                     overflow-hidden
                     flex items-center
                     justify-center"
            >

              <img
                v-if="book.image"
                :src="book.image"
                :alt="book.title"
                class="w-full h-full object-contain"
              />

              <div
                v-else
                class="text-6xl text-slate-300"
              >
                📚
              </div>

            </div>


            <!-- ================= STATUS ================= -->
            <div class="mt-4">

              <div
                v-if="isAvailable"
                class="flex items-center
                       justify-center gap-2
                       w-full
                       px-4 py-3
                       rounded-xl
                       bg-emerald-50
                       text-emerald-600
                       font-semibold"
              >
                <span>✓</span>
                Available
              </div>

              <div
                v-else
                class="flex items-center
                       justify-center gap-2
                       w-full
                       px-4 py-3
                       rounded-xl
                       bg-red-50
                       text-red-500
                       font-semibold"
              >
                <span>×</span>
                Unavailable
              </div>

            </div>

          </div>


          <!-- ================= BOOK INFO ================= -->
          <div class="md:col-span-3">

            <!-- TITLE -->
            <h1
              class="text-3xl
                     font-bold
                     text-slate-900
                     leading-tight"
            >
              {{ book.title }}
            </h1>


            <!-- AUTHOR -->
            <p class="mt-2 text-slate-500">
              Written by

              <span class="font-semibold text-slate-700">
                {{ book.author || "Unknown Author" }}
              </span>
            </p>


            <!-- ================= INFORMATION ================= -->
            <div
              class="grid
                     grid-cols-2
                     sm:grid-cols-3
                     gap-4
                     mt-7"
            >

              <!-- Quantity -->
              <div
                class="bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  Quantity
                </p>

                <p
                  class="mt-1
                         text-lg
                         font-bold
                         text-slate-800"
                >
                  {{ book.qty ?? 0 }}
                </p>
              </div>


              <!-- Pages -->
              <div
                class="bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  Pages
                </p>

                <p
                  class="mt-1
                         text-lg
                         font-bold
                         text-slate-800"
                >
                  {{ book.pages || "-" }}
                </p>
              </div>


              <!-- Language -->
              <div
                class="bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  Language
                </p>

                <p
                  class="mt-1
                         text-lg
                         font-bold
                         text-slate-800"
                >
                  {{ book.language || "-" }}
                </p>
              </div>


              <!-- ISBN -->
              <div
                class="col-span-2
                       sm:col-span-3
                       bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  ISBN
                </p>

                <p
                  class="mt-1
                         font-mono
                         text-slate-800
                         font-medium"
                >
                  {{ book.isbn || "-" }}
                </p>
              </div>

            </div>


            <!-- ================= DESCRIPTION ================= -->
            <div class="mt-7">

              <h3
                class="text-sm
                       font-bold
                       text-slate-800
                       uppercase
                       tracking-wide"
              >
                Description
              </h3>

              <p
                class="mt-3
                       text-sm
                       text-slate-600
                       leading-7"
              >
                {{ book.description || "No description available." }}
              </p>

            </div>


            <!-- ================= ACTION ================= -->
            <div class="mt-8 flex gap-3">

              <!-- ================= BORROW ================= -->
              <button
                v-if="isAvailable"
                type="button"
                :disabled="borrowing"
                @click="requestBorrow"
                class="w-full
                       bg-blue-600
                       hover:bg-blue-700
                       active:bg-blue-800
                       disabled:bg-blue-300
                       disabled:cursor-not-allowed
                       text-white
                       py-3
                       rounded-xl
                       font-semibold
                       transition
                       flex items-center
                       justify-center
                       gap-2"
              >

                <span v-if="!borrowing">
                  📚
                </span>

                <span v-else>
                  ⏳
                </span>

                <span>
                  {{ borrowing ? "Sending Request..." : "Borrow Book" }}
                </span>

              </button>


              <!-- ================= UNAVAILABLE ================= -->
              <button
                v-else
                type="button"
                disabled
                class="w-full
                       bg-slate-200
                       text-slate-400
                       py-3
                       rounded-xl
                       font-semibold
                       cursor-not-allowed"
              >
                Book Unavailable
              </button>


              <!-- ================= CLOSE ================= -->
              <button
                type="button"
                @click="$emit('close')"
                class="w-full
                       border border-slate-200
                       hover:bg-slate-50
                       text-slate-700
                       py-3
                       rounded-xl
                       font-medium
                       transition"
              >
                Close
              </button>

            </div>


            <!-- ================= MESSAGE ================= -->
            <div
              v-if="successMessage"
              class="mt-4
                     p-4
                     rounded-xl
                     bg-emerald-50
                     border border-emerald-100
                     text-emerald-700
                     text-sm
                     font-medium"
            >
              ✓ {{ successMessage }}
            </div>


            <div
              v-if="errorMessage"
              class="mt-4
                     p-4
                     rounded-xl
                     bg-red-50
                     border border-red-100
                     text-red-600
                     text-sm
                     font-medium"
            >
              {{ errorMessage }}
            </div>

          </div>

        </div>

      </div>

    </div>

  </div>
</template>


<script>
import {
  createBorrowing
} from "../../service/borrowingService.js";


export default {

  name: "BookDetail",


  props: {

    book: {
      type: Object,
      required: true
    }

  },


  emits: [
    "close",
    "borrow"
  ],


  data() {

    return {

      borrowing: false,

      successMessage: "",

      errorMessage: ""

    };

  },


  computed: {

    isAvailable() {

      if (this.book.status === false) {
        return false;
      }

      const quantity =
        Number(
          this.book.availableQuantity ??
          this.book.quantity ??
          this.book.qty ??
          0
        );

      return quantity > 0;

    }

  },


  methods: {

    // =====================================================
    // REQUEST BORROW
    // =====================================================

    async requestBorrow() {

      if (this.borrowing) {
        return;
      }


      this.successMessage = "";

      this.errorMessage = "";


      // -----------------------------------------------
      // CHECK BOOK
      // -----------------------------------------------

      if (!this.book?.id) {

        this.errorMessage =
          "Book ID is missing.";

        return;

      }


      // -----------------------------------------------
      // CHECK LOGIN
      // -----------------------------------------------

      const token =
        sessionStorage.getItem("token");


      if (!token) {

        this.errorMessage =
          "Please login before borrowing a book.";

        return;

      }


      // -----------------------------------------------
      // USER
      // -----------------------------------------------

      const storedUser =
        sessionStorage.getItem("user");


      let user = null;


      try {

        user =
          storedUser
            ? JSON.parse(storedUser)
            : null;

      } catch (error) {

        console.error(
          "Parse user error:",
          error
        );

      }


      if (!user?.id) {

        this.errorMessage =
          "User information was not found.";

        return;

      }


      // -----------------------------------------------
      // DATES
      // -----------------------------------------------

      const borrowDate =
        this.getToday();


      const dueDate =
        this.getDueDate(7);


      // -----------------------------------------------
      // PAYLOAD
      // -----------------------------------------------

      const payload = {
  userId: Number(user.id),
  bookId: Number(this.book.id),
  borrowDate: borrowDate,
  dueDate: dueDate,
  returnDate: null
};


      console.log(
        "Borrow Request:",
        payload
      );


      this.borrowing = true;


      try {

        // ---------------------------------------------
        // CREATE BORROW REQUEST
        // ---------------------------------------------

        const response =
          await createBorrowing(
            payload
          );


        console.log(
          "Borrow Request Response:",
          response
        );


        // ---------------------------------------------
        // SUCCESS
        // ---------------------------------------------

        this.successMessage =
          "Borrow request sent successfully. Please wait for admin approval.";


        // Send event to parent
        this.$emit(
          "borrow",
          {
            book: this.book,
            response: response
          }
        );


      } catch (error) {

        console.error(
          "Borrow Request Error:",
          error
        );


        this.errorMessage =
          error?.response?.data?.message ||
          error?.response?.data?.msg ||
          error?.message ||
          "Failed to send borrow request.";

      } finally {

        this.borrowing = false;

      }

    },


    // =====================================================
    // TODAY
    // =====================================================

    getToday() {

      const date =
        new Date();


      const year =
        date.getFullYear();


      const month =
        String(
          date.getMonth() + 1
        ).padStart(2, "0");


      const day =
        String(
          date.getDate()
        ).padStart(2, "0");


      return `${year}-${month}-${day}`;

    },


    // =====================================================
    // DUE DATE
    // =====================================================

    getDueDate(days) {

      const date =
        new Date();


      date.setDate(
        date.getDate() + days
      );


      const year =
        date.getFullYear();


      const month =
        String(
          date.getMonth() + 1
        ).padStart(2, "0");


      const day =
        String(
          date.getDate()
        ).padStart(2, "0");


      return `${year}-${month}-${day}`;

    }

  }

};
</script>
```
