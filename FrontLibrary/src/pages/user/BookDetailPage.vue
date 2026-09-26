```vue
<template>

  <!-- =====================================================
       PAGE
  ====================================================== -->
  <div
    class="min-h-screen
           bg-slate-50"
  >

    <!-- =====================================================
         HEADER
    ====================================================== -->
    <header
      class="sticky top-0 z-40
             bg-white
             border-b border-slate-200
             shadow-sm"
    >

      <div
        class="w-full
               max-w-5xl
               mx-auto
               px-4
               sm:px-6
               lg:px-8
               h-14
               flex items-center"
      >

        <!-- BACK BUTTON -->
        <button
          type="button"
          @click="goBack"
          class="w-9 h-9
                 flex items-center justify-center
                 rounded-lg
                 text-slate-600
                 hover:bg-slate-100
                 hover:text-slate-900
                 transition"
        >

          <i class="bi bi-arrow-left text-lg"></i>

        </button>


        <!-- TITLE -->
        <div class="ml-3">

          <h1
            class="text-base
                   sm:text-lg
                   font-bold
                   text-slate-800"
          >
            Book Details
          </h1>

          <p
            class="hidden sm:block
                   text-[11px]
                   text-slate-400"
          >
            View complete information about this book
          </p>

        </div>

      </div>

    </header>


    <!-- =====================================================
         LOADING
    ====================================================== -->
    <div
      v-if="loading"
      class="min-h-[calc(100vh-56px)]
             flex items-center
             justify-center
             px-4"
    >

      <div class="text-center">

        <div
          class="w-10 h-10
                 mx-auto
                 rounded-full
                 border-4
                 border-slate-200
                 border-t-blue-600
                 animate-spin"
        ></div>

        <p
          class="mt-3
                 text-sm
                 text-slate-500"
        >
          Loading book details...
        </p>

      </div>

    </div>


    <!-- =====================================================
         ERROR
    ====================================================== -->
    <div
      v-else-if="errorMessage"
      class="min-h-[calc(100vh-56px)]
             flex items-center
             justify-center
             px-4"
    >

      <div
        class="w-full
               max-w-md
               bg-white
               border border-red-100
               rounded-2xl
               shadow-sm
               p-6
               text-center"
      >

        <div
          class="w-14 h-14
                 mx-auto
                 rounded-full
                 bg-red-50
                 flex items-center
                 justify-center"
        >

          <i
            class="bi bi-exclamation-triangle
                   text-2xl
                   text-red-500"
          ></i>

        </div>


        <h2
          class="mt-4
                 text-lg
                 font-bold
                 text-slate-800"
        >
          Unable to Load Book
        </h2>


        <p
          class="mt-2
                 text-sm
                 leading-6
                 text-slate-500"
        >
          {{ errorMessage }}
        </p>


        <div
          class="mt-5
                 flex
                 gap-2"
        >

          <button
            type="button"
            @click="loadBook"
            class="flex-1
                   bg-blue-600
                   hover:bg-blue-700
                   text-white
                   py-2.5
                   rounded-lg
                   text-sm
                   font-semibold
                   transition"
          >
            Try Again
          </button>


          <button
            type="button"
            @click="goBack"
            class="flex-1
                   border border-slate-200
                   hover:bg-slate-50
                   text-slate-700
                   py-2.5
                   rounded-lg
                   text-sm
                   font-medium
                   transition"
          >
            Go Back
          </button>

        </div>

      </div>

    </div>


    <!-- =====================================================
         BOOK CONTENT
    ====================================================== -->
    <main
      v-else-if="book"
      class="w-full
             max-w-5xl
             mx-auto
             px-4
             py-5
             sm:px-6
             sm:py-7
             lg:px-8"
    >

      <!-- ===================================================
           MAIN CARD
      ==================================================== -->
      <div
        class="bg-white
               rounded-2xl
               border border-slate-200
               shadow-sm
               overflow-hidden"
      >

        <div
          class="p-4
                 sm:p-6
                 lg:p-8"
        >

          <!-- =================================================
               BOOK GRID
          ================================================== -->
          <div
            class="grid
                   grid-cols-1
                   md:grid-cols-5
                   gap-6
                   lg:gap-8"
          >

            <!-- ===============================================
                 IMAGE
            ================================================ -->
            <div
              class="md:col-span-2"
            >

              <div
                class="w-full
                       h-[380px]
                       sm:h-[430px]
                       md:h-[400px]
                       lg:h-[450px]
                       bg-slate-100
                       rounded-xl
                       overflow-hidden
                       flex items-center
                       justify-center"
              >

                <!-- IMAGE -->
                <img
                  v-if="imageUrl"
                  :src="imageUrl"
                  :alt="book.title"
                  class="w-full
                         h-full
                         object-cover"
                  @error="handleImageError"
                />


                <!-- NO IMAGE -->
                <div
                  v-else
                  class="flex
                         flex-col
                         items-center
                         justify-center"
                >

                  <span
                    class="text-6xl"
                  >
                    📚
                  </span>

                  <span
                    class="mt-3
                           text-sm
                           text-slate-400"
                  >
                    No Image Available
                  </span>

                </div>

              </div>


              <!-- =============================================
                   STATUS
              ============================================== -->
              <div class="mt-3">

                <!-- AVAILABLE -->
                <div
                  v-if="isAvailable"
                  class="flex
                         items-center
                         justify-center
                         gap-2
                         w-full
                         px-3
                         py-2.5
                         rounded-lg
                         bg-emerald-50
                         text-emerald-600
                         text-sm
                         font-semibold"
                >

                  <span>
                    ✓
                  </span>

                  Available

                </div>


                <!-- UNAVAILABLE -->
                <div
                  v-else
                  class="flex
                         items-center
                         justify-center
                         gap-2
                         w-full
                         px-3
                         py-2.5
                         rounded-lg
                         bg-red-50
                         text-red-500
                         text-sm
                         font-semibold"
                >

                  <span>
                    ×
                  </span>

                  Unavailable

                </div>

              </div>

            </div>


            <!-- ===============================================
                 BOOK INFORMATION
            ================================================ -->
            <div
              class="md:col-span-3"
            >

              <!-- TITLE -->
              <h2
                class="text-2xl
                       sm:text-3xl
                       font-bold
                       text-slate-900
                       leading-tight"
              >
                {{ book.title }}
              </h2>


              <!-- AUTHOR -->
              <p
                class="mt-2
                       text-sm
                       text-slate-500"
              >

                Written by

                <span
                  class="font-semibold
                         text-slate-700"
                >
                  {{ book.author || "Unknown Author" }}
                </span>

              </p>


              <!-- =============================================
                   INFORMATION
              ============================================== -->
              <div
                class="grid
                       grid-cols-2
                       gap-3
                       mt-6"
              >

                <!-- QUANTITY -->
                <div
                  class="bg-slate-50
                         rounded-lg
                         p-3.5"
                >

                  <p
                    class="text-[10px]
                           text-slate-400
                           font-semibold
                           uppercase"
                  >
                    Quantity
                  </p>

                  <p
                    class="mt-1
                           text-base
                           font-bold
                           text-slate-800"
                  >
                    {{ book.qty ?? book.quantity ?? 0 }}
                  </p>

                </div>


                <!-- PAGES -->
                <div
                  class="bg-slate-50
                         rounded-lg
                         p-3.5"
                >

                  <p
                    class="text-[10px]
                           text-slate-400
                           font-semibold
                           uppercase"
                  >
                    Pages
                  </p>

                  <p
                    class="mt-1
                           text-base
                           font-bold
                           text-slate-800"
                  >
                    {{ book.pages || "-" }}
                  </p>

                </div>


                <!-- PUBLICATION YEAR -->
                <div
                  class="bg-slate-50
                         rounded-lg
                         p-3.5"
                >

                  <p
                    class="text-[10px]
                           text-slate-400
                           font-semibold
                           uppercase"
                  >
                    Publication Year
                  </p>

                  <p
                    class="mt-1
                           text-base
                           font-bold
                           text-slate-800"
                  >
                    {{ book.publicationYear || "-" }}
                  </p>

                </div>


                <!-- LANGUAGE -->
                <div
                  class="bg-slate-50
                         rounded-lg
                         p-3.5"
                >

                  <p
                    class="text-[10px]
                           text-slate-400
                           font-semibold
                           uppercase"
                  >
                    Language
                  </p>

                  <p
                    class="mt-1
                           text-base
                           font-bold
                           text-slate-800"
                  >
                    {{ book.language || "-" }}
                  </p>

                </div>


                <!-- ISBN -->
                <div
                  class="col-span-2
                         bg-slate-50
                         rounded-lg
                         p-3.5"
                >

                  <p
                    class="text-[10px]
                           text-slate-400
                           font-semibold
                           uppercase"
                  >
                    ISBN
                  </p>

                  <p
                    class="mt-1
                           text-sm
                           font-mono
                           text-slate-800
                           font-medium
                           break-all"
                  >
                    {{ book.isbn || "-" }}
                  </p>

                </div>

              </div>


              <!-- =============================================
                   DESCRIPTION
              ============================================== -->
              <div class="mt-6">

                <h3
                  class="text-xs
                         font-bold
                         text-slate-800
                         uppercase
                         tracking-wide"
                >
                  Description
                </h3>


                <p
                  class="mt-2
                         text-sm
                         text-slate-600
                         leading-6"
                >
                  {{ book.description || "No description available." }}
                </p>

              </div>


              <!-- =============================================
                   BORROW ACTION
              ============================================== -->
              <div class="mt-6">

                <!-- BORROW -->
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
                         rounded-lg
                         text-sm
                         font-semibold
                         transition
                         flex
                         items-center
                         justify-center
                         gap-2"
                >

                  <span
                    v-if="!borrowing"
                  >
                    📚
                  </span>

                  <span
                    v-else
                  >
                    ⏳
                  </span>

                  <span>
                    {{
                      borrowing
                        ? "Sending Request..."
                        : "Borrow Book"
                    }}
                  </span>

                </button>


                <!-- UNAVAILABLE -->
                <button
                  v-else
                  type="button"
                  disabled
                  class="w-full
                         bg-slate-200
                         text-slate-400
                         py-3
                         rounded-lg
                         text-sm
                         font-semibold
                         cursor-not-allowed"
                >
                  Book Unavailable
                </button>

              </div>


              <!-- =============================================
                   SUCCESS
              ============================================== -->
              <div
                v-if="successMessage"
                class="mt-3
                       p-3
                       rounded-lg
                       bg-emerald-50
                       border border-emerald-100
                       text-emerald-700
                       text-xs
                       font-medium"
              >

                ✓ {{ successMessage }}

              </div>


              <!-- =============================================
                   ERROR
              ============================================== -->
              <div
                v-if="borrowError"
                class="mt-3
                       p-3
                       rounded-lg
                       bg-red-50
                       border border-red-100
                       text-red-600
                       text-xs
                       font-medium"
              >

                {{ borrowError }}

              </div>

            </div>

          </div>

        </div>

      </div>

    </main>

  </div>

</template>


<script>

import {
  createBorrowing
} from "../../service/borrowingService.js";


export default {

  name: "BookDetailPage",


  // =====================================================
  // DATA
  // =====================================================

  data() {

    return {

      book: null,

      loading: true,

      errorMessage: "",

      borrowing: false,

      successMessage: "",

      borrowError: "",

      imageError: false

    };

  },


  // =====================================================
  // COMPUTED
  // =====================================================

  computed: {

    // ===================================================
    // BOOK ID
    // ===================================================

    bookId() {

      return this.$route.params.id;

    },


    // ===================================================
    // IMAGE URL
    // ===================================================

    imageUrl() {

      if (this.imageError) {
        return "";
      }


      if (!this.book?.image) {
        return "";
      }


      const image =
        String(
          this.book.image
        ).trim();


      if (!image) {
        return "";
      }


      // Full URL
      if (
        image.startsWith("http://") ||
        image.startsWith("https://")
      ) {

        return image;

      }


      // Backend path
      return `https://librarymanagementsystem-ukyq.onrender.com${
        image.startsWith("/")
          ? ""
          : "/"
      }${image}`;

    },


    // ===================================================
    // AVAILABLE
    // ===================================================

    isAvailable() {

      if (!this.book) {
        return false;
      }


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


  // =====================================================
  // CREATED
  // =====================================================

  created() {

    this.loadBook();

  },


  // =====================================================
  // METHODS
  // =====================================================

  methods: {

    // ===================================================
    // LOAD BOOK
    // ===================================================

    async loadBook() {

      this.loading = true;

      this.errorMessage = "";

      this.book = null;

      this.imageError = false;


      if (!this.bookId) {

        this.errorMessage =
          "Book ID is missing.";

        this.loading = false;

        return;

      }


      try {

        console.log(
          "Loading book:",
          this.bookId
        );


        const token =
          sessionStorage.getItem(
            "token"
          );


        const response =
          await fetch(
            `https://librarymanagementsystem-ukyq.onrender.com/api/books/${this.bookId}`,
            {
              method: "GET",

              headers: {
                Authorization:
                  `Bearer ${token}`,

                "Content-Type":
                  "application/json"
              }
            }
          );


        console.log(
          "Book Detail Status:",
          response.status
        );


        const data =
          await response.json();


        console.log(
          "Book Detail Response:",
          data
        );


        // CHECK HTTP ERROR
        if (!response.ok) {

          throw new Error(
            data?.message ||
            data?.msg ||
            data?.error ||
            `Failed to load book. Status: ${response.status}`
          );

        }


        /*
         * Some Spring Boot APIs return:
         *
         * response.data
         *
         * Others return:
         *
         * response.data.data
         */

        this.book =
          data?.data ??
          data;


        if (!this.book) {

          throw new Error(
            "Book not found."
          );

        }

      } catch (error) {

        console.error(
          "Load Book Error:",
          error
        );


        this.errorMessage =
          error?.message ||
          "Failed to load book details.";

      } finally {

        this.loading = false;

      }

    },


    // ===================================================
    // IMAGE ERROR
    // ===================================================

    handleImageError() {

      console.error(
        "Book image failed:",
        this.imageUrl
      );


      this.imageError = true;

    },


    // ===================================================
    // GO BACK
    // ===================================================

    goBack() {

      if (
        window.history.length > 1
      ) {

        this.$router.back();

      } else {

        this.$router.push(
          "/user/home"
        );

      }

    },


    // ===================================================
    // REQUEST BORROW
    // ===================================================

    async requestBorrow() {

      if (this.borrowing) {
        return;
      }


      this.successMessage = "";

      this.borrowError = "";


      // CHECK BOOK
      if (!this.book?.id) {

        this.borrowError =
          "Book ID is missing.";

        return;

      }


      // CHECK LOGIN
      const token =
        sessionStorage.getItem(
          "token"
        );


      if (!token) {

        this.borrowError =
          "Please login before borrowing a book.";

        return;

      }


      // GET USER
      const storedUser =
        sessionStorage.getItem(
          "user"
        );


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


      // CHECK USER ID
      if (!user?.id) {

        this.borrowError =
          "User information was not found.";

        return;

      }


      // CHECK AVAILABILITY
      if (!this.isAvailable) {

        this.borrowError =
          "This book is currently unavailable.";

        return;

      }


      // DATES
      const borrowDate =
        this.getToday();


      const dueDate =
        this.getDueDate(7);


      // PAYLOAD
      const payload = {

        userId:
          Number(user.id),

        bookId:
          Number(this.book.id),

        borrowDate:
          borrowDate,

        dueDate:
          dueDate,

        returnDate:
          null

      };


      console.log(
        "Borrow Request:",
        payload
      );


      this.borrowing = true;


      try {

        const response =
          await createBorrowing(
            payload
          );


        console.log(
          "Borrow Request Response:",
          response
        );


        this.successMessage =
          "Borrow request sent successfully. Please wait for admin approval.";

      } catch (error) {

        console.error(
          "Borrow Request Error:",
          error
        );


        this.borrowError =
          error?.response?.data?.message ||
          error?.response?.data?.msg ||
          error?.response?.data?.error ||
          error?.message ||
          "Failed to send borrow request.";

      } finally {

        this.borrowing = false;

      }

    },


    // ===================================================
    // GET TODAY
    // ===================================================

    getToday() {

      const date =
        new Date();


      const year =
        date.getFullYear();


      const month =
        String(
          date.getMonth() + 1
        ).padStart(
          2,
          "0"
        );


      const day =
        String(
          date.getDate()
        ).padStart(
          2,
          "0"
        );


      return `${year}-${month}-${day}`;

    },


    // ===================================================
    // GET DUE DATE
    // ===================================================

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
        ).padStart(
          2,
          "0"
        );


      const day =
        String(
          date.getDate()
        ).padStart(
          2,
          "0"
        );


      return `${year}-${month}-${day}`;

    }

  }

};

</script>
```
