<template>

  <div class="min-h-screen bg-slate-50 p-6">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="mb-6">

      <h1 class="text-2xl font-bold text-slate-800">
        Create Borrowing
      </h1>

      <p class="mt-1 text-sm text-slate-500">
        Select a book and enter borrower information
      </p>

    </div>


    <!-- =====================================================
         MAIN LAYOUT
    ====================================================== -->

    <div class="grid grid-cols-1 xl:grid-cols-5 gap-6">


      <!-- ===================================================
           LEFT SIDE - BOOK SEARCH
      ==================================================== -->

      <div
        class="xl:col-span-3 bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden"
      >

        <!-- BOOK HEADER -->

        <div class="p-5 border-b border-slate-200">

          <div class="flex items-center justify-between mb-4">

            <div>

              <h2 class="text-lg font-bold text-slate-800">
                Select Book
              </h2>

              <p class="text-sm text-slate-500 mt-1">
                Search by title, author or ISBN
              </p>

            </div>


            <!-- BOOK COUNT -->

            <div
              class="px-3 py-1.5 rounded-lg bg-blue-50 text-blue-600 text-sm font-semibold"
            >
              {{ filteredBooks.length }} Books
            </div>

          </div>


          <!-- SEARCH -->

          <div class="relative">

            <span
              class="absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
            >
              🔍
            </span>

            <input
              v-model="search"
              type="text"
              placeholder="Search book title, author or ISBN..."
              class="w-full pl-11 pr-4 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
            />

          </div>

        </div>


        <!-- BOOK LIST -->

        <div class="p-5 h-[650px] overflow-y-auto">

          <!-- LOADING -->

          <div
            v-if="loading"
            class="h-full flex items-center justify-center"
          >

            <div class="text-center">

              <div
                class="w-10 h-10 border-4 border-blue-200 border-t-blue-600 rounded-full animate-spin mx-auto"
              ></div>

              <p class="mt-3 text-sm text-slate-500">
                Loading books...
              </p>

            </div>

          </div>


          <!-- NO BOOK -->

          <div
            v-else-if="filteredBooks.length === 0"
            class="h-full flex items-center justify-center"
          >

            <div class="text-center">

              <div class="text-5xl mb-3">
                📚
              </div>

              <h3 class="font-semibold text-slate-700">
                No books found
              </h3>

              <p class="text-sm text-slate-400 mt-1">
                Try another search keyword
              </p>

            </div>

          </div>


          <!-- BOOK GRID -->

          <div
            v-else
            class="grid grid-cols-1 md:grid-cols-2 gap-4"
          >

            <div
              v-for="book in filteredBooks"
              :key="book.id"
              class="relative"
            >

              <BorrowBookCard
                :book="book"
                @select="selectBook"
              />


              <!-- SELECTED OVERLAY -->

              <div
                v-if="
                  selectedBook &&
                  String(selectedBook.id) === String(book.id)
                "
                class="absolute inset-0 rounded-2xl border-2 border-blue-500 pointer-events-none"
              >

                <div
                  class="absolute top-3 right-3 bg-blue-600 text-white text-xs font-semibold px-3 py-1 rounded-full"
                >
                  Selected
                </div>

              </div>

            </div>

          </div>

        </div>

      </div>


      <!-- ===================================================
           RIGHT SIDE - BORROWER
      ==================================================== -->

      <div
        class="xl:col-span-2 bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden"
      >

        <!-- BORROWER HEADER -->

        <div class="p-5 border-b border-slate-200">

          <h2 class="text-lg font-bold text-slate-800">
            Borrower Information
          </h2>

          <p class="text-sm text-slate-500 mt-1">
            Enter information of the person borrowing
          </p>

        </div>


        <!-- RIGHT CONTENT -->

        <div class="p-5 h-[700px] overflow-y-auto">


          <!-- =================================================
               SELECTED BOOK
          ================================================== -->

          <div class="mb-6">

            <h3 class="text-sm font-bold text-slate-700 mb-3">
              Selected Book
            </h3>


            <!-- NO BOOK -->

            <div
              v-if="!selectedBook"
              class="border-2 border-dashed border-slate-200 rounded-xl p-6 text-center"
            >

              <div class="text-4xl">
                📖
              </div>

              <p class="mt-2 text-sm font-medium text-slate-600">
                No book selected
              </p>

              <p class="text-xs text-slate-400 mt-1">
                Select a book from the left side
              </p>

            </div>


            <!-- SELECTED BOOK -->

            <div
              v-else
              class="rounded-xl border border-blue-200 bg-blue-50 p-4"
            >

              <div class="flex items-start gap-4">

                <!-- IMAGE -->

                <div
                  class="w-16 h-20 rounded-lg overflow-hidden bg-white flex-shrink-0"
                >

                  <img
                    v-if="selectedBook.image"
                    :src="selectedBook.image"
                    :alt="selectedBook.title"
                    class="w-full h-full object-cover"
                  />

                  <div
                    v-else
                    class="w-full h-full flex items-center justify-center text-2xl"
                  >
                    📚
                  </div>

                </div>


                <!-- INFO -->

                <div class="min-w-0 flex-1">

                  <h4
                    class="font-bold text-slate-800 truncate"
                  >
                    {{ selectedBook.title }}
                  </h4>

                  <p class="text-sm text-slate-500 mt-1">
                    {{ selectedBook.author }}
                  </p>

                  <div class="flex items-center gap-2 mt-2">

                    <span
                      class="text-xs font-semibold text-blue-600"
                    >
                      Available:
                    </span>

                    <span
                      class="text-xs font-bold text-slate-700"
                    >
                      {{ maxQuantity }}
                    </span>

                  </div>

                </div>


                <!-- REMOVE -->

                <button
                  type="button"
                  @click="removeSelectedBook"
                  class="w-8 h-8 flex items-center justify-center rounded-lg text-slate-400 hover:bg-white hover:text-red-500 transition"
                >
                  ✕
                </button>

              </div>

            </div>

          </div>


          <!-- =================================================
               BORROWER FORM
          ================================================== -->

          <div class="space-y-5">


            <!-- NAME -->

            <div>

              <label
                class="block text-sm font-semibold text-slate-700 mb-2"
              >
                Borrower Name
                <span class="text-red-500">*</span>
              </label>

              <input
                v-model="borrower.name"
                type="text"
                placeholder="Enter borrower name"
                class="w-full px-4 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
              />

            </div>


            <!-- EMAIL -->

            <div>

              <label
                class="block text-sm font-semibold text-slate-700 mb-2"
              >
                Email
                <span class="text-red-500">*</span>
              </label>

              <input
                v-model="borrower.email"
                @input="onEmailInput"
                type="email"
                placeholder="Enter borrower email"
                class="w-full px-4 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
              />

              <p
                v-if="selectedUser"
                class="text-xs text-green-600 mt-2"
              >
                ✓ User found: {{ selectedUser.name }}
              </p>

            </div>


            <!-- PHONE -->

            <div>

              <label
                class="block text-sm font-semibold text-slate-700 mb-2"
              >
                Phone
              </label>

              <input
                v-model="borrower.phone"
                type="text"
                placeholder="Enter phone number"
                class="w-full px-4 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500 focus:border-blue-500 transition"
              />

            </div>


            <!-- DATE -->

            <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">

              <!-- BORROW DATE -->

              <div>

                <label
                  class="block text-sm font-semibold text-slate-700 mb-2"
                >
                  Borrow Date
                  <span class="text-red-500">*</span>
                </label>

                <input
                  v-model="borrower.borrowDate"
                  type="date"
                  class="w-full px-3 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500"
                />

              </div>


              <!-- DUE DATE -->

              <div>

                <label
                  class="block text-sm font-semibold text-slate-700 mb-2"
                >
                  Due Date
                  <span class="text-red-500">*</span>
                </label>

                <input
                  v-model="borrower.dueDate"
                  type="date"
                  :min="borrower.borrowDate"
                  class="w-full px-3 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500"
                />

              </div>

            </div>


            <!-- QUANTITY -->

            <div>

              <label
                class="block text-sm font-semibold text-slate-700 mb-2"
              >
                Quantity
              </label>

              <div class="flex items-center gap-3">

                <button
                  type="button"
                  @click="decreaseQuantity"
                  :disabled="borrower.quantity <= 1"
                  class="w-11 h-11 rounded-xl border border-slate-200 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed font-bold"
                >
                  −
                </button>


                <input
                  v-model.number="borrower.quantity"
                  type="number"
                  min="1"
                  :max="maxQuantity"
                  class="flex-1 text-center px-4 py-3 border border-slate-200 rounded-xl outline-none focus:ring-2 focus:ring-blue-500"
                />


                <button
                  type="button"
                  @click="increaseQuantity"
                  :disabled="
                    !selectedBook ||
                    borrower.quantity >= maxQuantity
                  "
                  class="w-11 h-11 rounded-xl border border-slate-200 hover:bg-slate-50 disabled:opacity-40 disabled:cursor-not-allowed font-bold"
                >
                  +
                </button>

              </div>

              <p class="text-xs text-slate-400 mt-2">
                Maximum available:
                {{ maxQuantity }}
              </p>

            </div>


            <!-- ERROR -->

            <div
              v-if="validationError"
              class="p-3 rounded-xl bg-red-50 border border-red-100 text-sm text-red-600"
            >
              {{ validationError }}
            </div>


            <!-- CREATE BUTTON -->

            <button
              type="button"
              @click="submitBorrowing"
              :disabled="submitting || !canSubmit"
              class="w-full py-3.5 rounded-xl font-semibold text-white transition"
              :class="
                submitting || !canSubmit
                  ? 'bg-slate-300 cursor-not-allowed'
                  : 'bg-blue-600 hover:bg-blue-700'
              "
            >

              <span v-if="submitting">
                Creating Borrowing...
              </span>

              <span v-else>
                Create Borrowing
              </span>

            </button>


            <!-- RESET -->

            <button
              type="button"
              @click="resetForm"
              :disabled="submitting"
              class="w-full py-3 rounded-xl border border-slate-200 text-slate-600 hover:bg-slate-50 transition disabled:opacity-50"
            >
              Reset
            </button>

          </div>

        </div>

      </div>

    </div>

  </div>

</template>


<script>

import BorrowBookCard
  from "../../components/admin/borrowings/BorrowBookCard.vue";

import BorrowerForm
  from "../../components/admin/borrowings/BorrowerForm.vue";

import SelectBook
  from "../../components/admin/borrowings/SelectBook.vue";


/*
|--------------------------------------------------------------------------
| BOOK SERVICE
|--------------------------------------------------------------------------
*/

import {
  getBooks
} from "../../service/bookService.js";


/*
|--------------------------------------------------------------------------
| BORROWING SERVICE
|--------------------------------------------------------------------------
*/

import {
  createBorrowing
} from "../../service/borrowingService.js";


/*
|--------------------------------------------------------------------------
| USER SERVICE
|--------------------------------------------------------------------------
*/

import {
  getUsers
} from "../../service/userService.js";


export default {

  name: "CreateBorrowing",


  // =====================================================
  // COMPONENTS
  // =====================================================

  components: {

    BorrowBookCard,

    BorrowerForm,

    SelectBook

  },


  // =====================================================
  // DATA
  // =====================================================

  data() {

    return {

      // =================================================
      // BOOKS
      // =================================================

      books: [],

      search: "",

      loading: false,


      // =================================================
      // SELECTED BOOK
      // =================================================

      selectedBook: null,


      // =================================================
      // USERS
      // =================================================

      users: [],

      usersLoading: false,

      selectedUser: null,


      // =================================================
      // BORROWER
      // =================================================

      borrower: {

        name: "",

        email: "",

        phone: "",

        borrowDate:
          this.getToday(),

        dueDate: "",

        quantity: 1

      },


      // =================================================
      // SUBMIT
      // =================================================

      submitting: false,


      // =================================================
      // VALIDATION
      // =================================================

      validationError: ""

    };

  },


  // =====================================================
  // COMPUTED
  // =====================================================

  computed: {

    // =================================================
    // FILTER BOOKS
    // =================================================

    filteredBooks() {

      const keyword =
        this.search
          .trim()
          .toLowerCase();


      if (!keyword) {

        return this.books;

      }


      return this.books.filter(book => {

        const title =
          String(
            book.title || ""
          ).toLowerCase();


        const author =
          String(
            book.author || ""
          ).toLowerCase();


        const isbn =
          String(
            book.isbn || ""
          ).toLowerCase();


        return (

          title.includes(keyword) ||

          author.includes(keyword) ||

          isbn.includes(keyword)

        );

      });

    },


    // =================================================
    // MAX QUANTITY
    // =================================================

    maxQuantity() {

      if (!this.selectedBook) {

        return 0;

      }


      return this.getAvailableQuantity(
        this.selectedBook
      );

    },


    // =================================================
    // CAN SUBMIT
    // =================================================

    canSubmit() {

      /*
       * IMPORTANT:
       *
       * DO NOT CHECK selectedUser HERE.
       *
       * selectedUser is found when submitBorrowing()
       * calls validateForm().
       *
       * If we check selectedUser here,
       * button becomes disabled before validation.
       */

      // BOOK

      if (!this.selectedBook) {

        return false;

      }


      // NAME

      if (
        !this.borrower.name.trim()
      ) {

        return false;

      }


      // EMAIL

      if (
        !this.borrower.email.trim()
      ) {

        return false;

      }


      // BORROW DATE

      if (
        !this.borrower.borrowDate
      ) {

        return false;

      }


      // DUE DATE

      if (
        !this.borrower.dueDate
      ) {

        return false;

      }


      // DATE

      if (
        this.borrower.dueDate <=
        this.borrower.borrowDate
      ) {

        return false;

      }


      // QUANTITY

      if (
        !this.borrower.quantity ||
        this.borrower.quantity < 1
      ) {

        return false;

      }


      if (
        this.borrower.quantity >
        this.maxQuantity
      ) {

        return false;

      }


      return true;

    }

  },


  // =====================================================
  // MOUNTED
  // =====================================================

  mounted() {

    this.loadBooks();

    this.loadUsers();

  },


  // =====================================================
  // METHODS
  // =====================================================

  methods: {


    // =================================================
    // TODAY
    // =================================================

    getToday() {

      const today =
        new Date();


      const year =
        today.getFullYear();


      const month =
        String(
          today.getMonth() + 1
        ).padStart(2, "0");


      const day =
        String(
          today.getDate()
        ).padStart(2, "0");


      return `${year}-${month}-${day}`;

    },


    // =================================================
    // LOAD BOOKS
    // =================================================

    async loadBooks() {

      this.loading = true;


      try {

        const response =
          await getBooks();


        console.log(
          "Books API Response:",
          response
        );


        if (
          Array.isArray(response)
        ) {

          this.books =
            response;

        }

        else if (
          Array.isArray(
            response?.data
          )
        ) {

          this.books =
            response.data;

        }

        else if (
          Array.isArray(
            response?.data?.data
          )
        ) {

          this.books =
            response.data.data;

        }

        else {

          this.books = [];

        }


        console.log(
          "Books Loaded:",
          this.books
        );


      } catch (error) {

        console.error(
          "Load books error:",
          error
        );


        this.books = [];

      } finally {

        this.loading = false;

      }

    },


    // =================================================
    // LOAD USERS
    // =================================================

    async loadUsers() {

      this.usersLoading = true;


      try {

        const response =
          await getUsers();


        console.log(
          "Users API Response:",
          response
        );


        if (
          Array.isArray(response)
        ) {

          this.users =
            response;

        }

        else if (
          Array.isArray(
            response?.data
          )
        ) {

          this.users =
            response.data;

        }

        else if (
          Array.isArray(
            response?.data?.data
          )
        ) {

          this.users =
            response.data.data;

        }

        else {

          this.users = [];

        }


        console.log(
          "Users Loaded:",
          this.users
        );


      } catch (error) {

        console.error(
          "Load users error:",
          error
        );


        this.users = [];

      } finally {

        this.usersLoading = false;

      }

    },


    // =================================================
    // FIND USER BY EMAIL
    // =================================================

    findUserByEmail() {

      this.validationError = "";

      this.selectedUser = null;


      const email =
        this.borrower.email
          .trim()
          .toLowerCase();


      if (!email) {

        this.validationError =
          "Please enter borrower email.";

        return null;

      }


      const user =
        this.users.find(item => {

          return String(
            item.email || ""
          )
            .trim()
            .toLowerCase() === email;

        });


      if (!user) {

        this.validationError =
          "User with this email was not found.";

        return null;

      }


      this.selectedUser =
        user;


      /*
       * Get name from database
       */

      this.borrower.name =
        user.name ||
        user.fullName ||
        user.username ||
        "";


      console.log(
        "Selected User:",
        this.selectedUser
      );


      return user;

    },


    // =================================================
    // EMAIL INPUT
    // =================================================

    onEmailInput() {

      this.selectedUser =
        null;

      this.validationError =
        "";

    },


    // =================================================
    // AVAILABLE QUANTITY
    // =================================================

    getAvailableQuantity(book) {

      if (
        book.availableQuantity !==
        undefined
      ) {

        return Number(
          book.availableQuantity
        );

      }


      if (
        book.quantity !==
        undefined
      ) {

        return Number(
          book.quantity
        );

      }


      if (
        book.qty !==
        undefined
      ) {

        return Number(
          book.qty
        );

      }


      return 0;

    },


    // =================================================
    // SELECT BOOK
    // =================================================

    selectBook(book) {

      console.log(
        "Selected Book:",
        book
      );


      const available =
        this.getAvailableQuantity(
          book
        );


      if (available <= 0) {

        alert(
          "This book is currently unavailable."
        );

        return;

      }


      this.selectedBook =
        book;


      this.borrower.quantity =
        1;


      this.validationError =
        "";

    },


    // =================================================
    // REMOVE BOOK
    // =================================================

    removeSelectedBook() {

      this.selectedBook =
        null;


      this.borrower.quantity =
        1;


      this.validationError =
        "";

    },


    // =================================================
    // INCREASE QUANTITY
    // =================================================

    increaseQuantity() {

      if (!this.selectedBook) {

        return;

      }


      if (
        this.borrower.quantity <
        this.maxQuantity
      ) {

        this.borrower.quantity++;

      }

    },


    // =================================================
    // DECREASE QUANTITY
    // =================================================

    decreaseQuantity() {

      if (
        this.borrower.quantity > 1
      ) {

        this.borrower.quantity--;

      }

    },


    // =================================================
    // VALIDATE FORM
    // =================================================

    validateForm() {

      this.validationError =
        "";


      // -----------------------------------------------
      // BOOK
      // -----------------------------------------------

      if (!this.selectedBook) {

        this.validationError =
          "Please select a book.";

        return false;

      }


      // -----------------------------------------------
      // EMAIL
      // -----------------------------------------------

      if (
        !this.borrower.email.trim()
      ) {

        this.validationError =
          "Please enter borrower email.";

        return false;

      }


      // -----------------------------------------------
      // FIND USER
      // -----------------------------------------------

      const user =
        this.findUserByEmail();


      if (!user) {

        return false;

      }


      // -----------------------------------------------
      // USER ID
      // -----------------------------------------------

      if (!user.id) {

        this.validationError =
          "Selected user does not have a valid ID.";

        return false;

      }


      // -----------------------------------------------
      // NAME
      // -----------------------------------------------

      if (
        !this.borrower.name.trim()
      ) {

        this.validationError =
          "Please enter borrower name.";

        return false;

      }


      // -----------------------------------------------
      // BORROW DATE
      // -----------------------------------------------

      if (
        !this.borrower.borrowDate
      ) {

        this.validationError =
          "Please select borrow date.";

        return false;

      }


      // -----------------------------------------------
      // DUE DATE
      // -----------------------------------------------

      if (
        !this.borrower.dueDate
      ) {

        this.validationError =
          "Please select due date.";

        return false;

      }


      // -----------------------------------------------
      // DATE
      // -----------------------------------------------

      if (
        this.borrower.dueDate <=
        this.borrower.borrowDate
      ) {

        this.validationError =
          "Due date must be after borrow date.";

        return false;

      }


      // -----------------------------------------------
      // QUANTITY
      // -----------------------------------------------

      if (
        !this.borrower.quantity ||
        this.borrower.quantity < 1
      ) {

        this.validationError =
          "Quantity must be at least 1.";

        return false;

      }


      if (
        this.borrower.quantity >
        this.maxQuantity
      ) {

        this.validationError =
          `Only ${this.maxQuantity} book(s) are available.`;

        return false;

      }


      return true;

    },


    // =================================================
    // SUBMIT BORROWING
    // =================================================

    async submitBorrowing() {

      console.log(
        "Create Borrowing button clicked"
      );


      // -----------------------------------------------
      // VALIDATE
      // -----------------------------------------------

      if (!this.validateForm()) {

        console.log(
          "Validation failed:",
          this.validationError
        );

        return;

      }


      this.submitting =
        true;


      try {

        // ---------------------------------------------
        // USER
        // ---------------------------------------------

        const user =
          this.selectedUser ||
          this.findUserByEmail();


        if (!user) {

          throw new Error(
            "Borrower user not found."
          );

        }


        // ---------------------------------------------
        // USER ID
        // ---------------------------------------------

        if (!user.id) {

          throw new Error(
            "Borrower user ID is missing."
          );

        }


        // ---------------------------------------------
        // BOOK ID
        // ---------------------------------------------

        if (!this.selectedBook?.id) {

          throw new Error(
            "Book ID is missing."
          );

        }


        // ---------------------------------------------
        // PAYLOAD
        // ---------------------------------------------

        /*
         * Backend:
         *
         * BorrowerRequest
         *
         * private Long userId;
         * private Long bookId;
         * private LocalDate borrowDate;
         * private LocalDate dueDate;
         * private LocalDate returnDate;
         * private BorrowingStatus status;
         *
         * So send ONLY these fields.
         */

        const payload = {

          userId:
            Number(user.id),

          bookId:
            Number(
              this.selectedBook.id
            ),

          borrowDate:
            this.borrower.borrowDate,

          dueDate:
            this.borrower.dueDate,

          returnDate:
            null,

          status:
            "BORROWED"

        };


        console.log(
          "================================="
        );

        console.log(
          "Borrowing Payload:",
          payload
        );

        console.log(
          "User ID:",
          user.id
        );

        console.log(
          "User Name:",
          user.name
        );

        console.log(
          "User Email:",
          user.email
        );

        console.log(
          "Book ID:",
          this.selectedBook.id
        );

        console.log(
          "================================="
        );


        // ---------------------------------------------
        // CREATE BORROWING
        // ---------------------------------------------

        const response =
          await createBorrowing(
            payload
          );


        console.log(
          "Create Borrowing Response:",
          response
        );


        // ---------------------------------------------
        // SUCCESS
        // ---------------------------------------------

        alert(
          "Borrowing created successfully!"
        );


        // ---------------------------------------------
        // RESET
        // ---------------------------------------------

        this.resetForm();


        // ---------------------------------------------
        // RELOAD BOOKS
        // ---------------------------------------------

        await this.loadBooks();


      } catch (error) {

        console.error(
          "Create Borrowing Error:",
          error
        );


        /*
         * Try to show backend error message
         */

        const message =
          error?.response?.data?.message ||
          error?.response?.data?.msg ||
          error?.message ||
          "Failed to create borrowing.";


        alert(message);


      } finally {

        this.submitting =
          false;

      }

    },


    // =================================================
    // RESET
    // =================================================

    resetForm() {

      this.selectedBook =
        null;


      this.selectedUser =
        null;


      this.search =
        "";


      this.validationError =
        "";


      this.borrower = {

        name: "",

        email: "",

        phone: "",

        borrowDate:
          this.getToday(),

        dueDate: "",

        quantity: 1

      };

    }

  }

};

</script>