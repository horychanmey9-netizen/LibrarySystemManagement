```vue
<template>

  <!-- ================= OVERLAY ================= -->
  <div
    class="fixed inset-0 z-50
           bg-black/50 backdrop-blur-sm
           flex items-center justify-center
           px-3 py-4"
    @click.self="$emit('close')"
  >

    <!-- ================= MODAL ================= -->
    <div
      class="w-full max-w-3xl
             max-h-[88vh]
             bg-white rounded-xl
             shadow-2xl overflow-hidden"
    >

      <!-- ================= HEADER ================= -->
      <div
        class="flex items-center justify-between
               px-5 py-3
               border-b border-slate-200"
      >

        <div>

          <h2 class="text-lg font-bold text-slate-800">
            Book Details
          </h2>

          <p class="text-[11px] text-slate-400 mt-0.5">
            View complete information about this book
          </p>

        </div>


        <!-- CLOSE -->
        <button
          type="button"
          @click="$emit('close')"
          class="w-8 h-8
                 flex items-center justify-center
                 rounded-lg
                 text-slate-500
                 hover:bg-slate-100
                 hover:text-slate-800
                 transition"
        >
          ✕
        </button>

      </div>


      <!-- ================= CONTENT ================= -->
      <div
        class="p-5 overflow-y-auto
               max-h-[calc(88vh-65px)]"
      >

        <div
          class="grid grid-cols-1
                 md:grid-cols-5
                 gap-5"
        >

          <!-- ================= IMAGE ================= -->
          <div class="md:col-span-2">

            <div
              class="h-[400px]
                     bg-slate-100
                     rounded-xl
                     overflow-hidden
                     flex items-center
                     justify-center"
            >

              <!-- BOOK IMAGE -->
              <img
                v-if="imageUrl"
                :src="imageUrl"
                :alt="book.title"
                class="w-full h-full object-cover"
                @error="handleImageError"
              />

              <!-- NO IMAGE -->
              <div
                v-else
                class="flex flex-col
                       items-center
                       justify-center
                       text-slate-300"
              >

                <span class="text-5xl">
                  📚
                </span>

                <span
                  class="mt-2
                         text-xs
                         text-slate-400"
                >
                  No Image
                </span>

              </div>

            </div>


            <!-- ================= STATUS ================= -->
            <div class="mt-3">

              <!-- AVAILABLE -->
              <div
                v-if="isAvailable"
                class="flex items-center
                       justify-center gap-2
                       w-full
                       px-3 py-2
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
                class="flex items-center
                       justify-center gap-2
                       w-full
                       px-3 py-2
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


          <!-- ================= BOOK INFO ================= -->
          <div class="md:col-span-3">

            <!-- TITLE -->
            <h1
              class="text-2xl
                     font-bold
                     text-slate-900
                     leading-tight"
            >
              {{ book.title }}
            </h1>


            <!-- AUTHOR -->
            <p class="mt-1 text-sm text-slate-500">

              Written by

              <span class="font-semibold text-slate-700">
                {{ book.author || "Unknown Author" }}
              </span>

            </p>


            <!-- ================= INFORMATION ================= -->
            <div
              class="grid
                     grid-cols-2
                     gap-3
                     mt-5"
            >

              <!-- QUANTITY -->
              <div
                class="bg-slate-50
                       rounded-lg
                       p-3"
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
                  class="mt-0.5
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
                       p-3"
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
                  class="mt-0.5
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
                       p-3"
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
                  class="mt-0.5
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
                       p-3"
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
                  class="mt-0.5
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
                       p-3"
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
                  class="mt-0.5
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


            <!-- ================= DESCRIPTION ================= -->
            <div class="mt-5">

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
                       text-xs
                       text-slate-600
                       leading-6
                       line-clamp-4"
              >
                {{ book.description || "No description available." }}
              </p>

            </div>


            <!-- ================= ACTION ================= -->
            <div class="mt-5 flex gap-2">

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
                       py-2.5
                       rounded-lg
                       text-sm
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
                       py-2.5
                       rounded-lg
                       text-sm
                       font-semibold
                       cursor-not-allowed"
              >
                Book Unavailable
              </button>


              <!-- CLOSE -->
              <button
                type="button"
                @click="$emit('close')"
                class="w-full
                       border border-slate-200
                       hover:bg-slate-50
                       text-slate-700
                       py-2.5
                       rounded-lg
                       text-sm
                       font-medium
                       transition"
              >
                Close
              </button>

            </div>


            <!-- ================= SUCCESS MESSAGE ================= -->
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


            <!-- ================= ERROR MESSAGE ================= -->
            <div
              v-if="errorMessage"
              class="mt-3
                     p-3
                     rounded-lg
                     bg-red-50
                     border border-red-100
                     text-red-600
                     text-xs
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


  // =====================================================
  // PROPS
  // =====================================================

  props: {

    book: {
      type: Object,
      required: true
    }

  },


  // =====================================================
  // EMITS
  // =====================================================

  emits: [
    "close",
    "borrow"
  ],


  // =====================================================
  // DATA
  // =====================================================

  data() {

    return {

      borrowing: false,

      successMessage: "",

      errorMessage: "",

      imageError: false

    };

  },


  // =====================================================
  // COMPUTED
  // =====================================================

  computed: {

    // ===================================================
    // BOOK IMAGE URL
    // ===================================================

    imageUrl() {

      /*
       * If image has already failed,
       * show fallback instead.
       */

      if (this.imageError) {
        return "";
      }


      /*
       * No image
       */

      if (!this.book?.image) {
        return "";
      }


      const image =
        String(this.book.image).trim();


      if (!image) {
        return "";
      }


      /*
       * Image is already a complete URL
       */

      if (
        image.startsWith("http://") ||
        image.startsWith("https://")
      ) {

        return image;

      }


      /*
       * Backend image path
       *
       * Example:
       * /uploads/books/book.jpg
       *
       * becomes:
       * http://localhost:8080/uploads/books/book.jpg
       */

      return `http://localhost:8080${
        image.startsWith("/")
          ? ""
          : "/"
      }${image}`;

    },


    // ===================================================
    // AVAILABLE
    // ===================================================

    isAvailable() {

      /*
       * If backend sends status = false
       * book is unavailable.
       */

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
  // METHODS
  // =====================================================

  methods: {

    // ===================================================
    // IMAGE ERROR
    // ===================================================

    handleImageError() {

      console.error(
        "Book image failed to load:",
        this.imageUrl
      );


      this.imageError = true;

    },


    // ===================================================
    // REQUEST BORROW
    // ===================================================

    async requestBorrow() {

      if (this.borrowing) {
        return;
      }


      this.successMessage = "";

      this.errorMessage = "";


      // CHECK BOOK
      if (!this.book?.id) {

        this.errorMessage =
          "Book ID is missing.";

        return;

      }


      // CHECK LOGIN
      const token =
        sessionStorage.getItem("token");


      if (!token) {

        this.errorMessage =
          "Please login before borrowing a book.";

        return;

      }


      // GET USER
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


      // CHECK USER ID
      if (!user?.id) {

        this.errorMessage =
          "User information was not found.";

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


      // START REQUEST
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


        // SUCCESS
        this.successMessage =
          "Borrow request sent successfully. Please wait for admin approval.";


        // SEND EVENT
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
        ).padStart(2, "0");


      const day =
        String(
          date.getDate()
        ).padStart(2, "0");


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

### ចំណុចដែលបានកែ

Image ឥឡូវប្រើ៖

```vue
:src="imageUrl"
```

ហើយ `imageUrl()` នឹង handle៖

```text
https://example.com/book.jpg
```

ឬ

```text
/uploads/books/book.jpg
```

ដោយបម្លែង path ទីពីរទៅជា៖

```text
http://localhost:8080/uploads/books/book.jpg
```

ហើយ `object-cover` ធ្វើឱ្យ **រូបពេញប្រអប់** មិនសល់ space ដូចដែលអ្នកចង់បាន។

**ចំណាំ:** បើ `BookCard.vue` របស់អ្នកមាន image URL ជាក់លាក់ផ្សេងពី `http://localhost:8080/...` សូមផ្ញើ `BookCard.vue` មក។ ខ្ញុំនឹងធ្វើ `BookDetail` ឱ្យប្រើ image logic ដូចគ្នា 100%។
