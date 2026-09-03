<template>

  <div
    class="min-h-screen w-full bg-slate-50 p-4 sm:p-6 lg:p-8"
  >

    <!-- =====================================================
         CATEGORIES
    ====================================================== -->

    <section class="mb-10">

      <!-- SECTION HEADER -->
      <div
        class="mb-4 flex items-end justify-between gap-4"
      >

        <div>

          <h2
            class="m-0 text-xl font-bold text-slate-900"
          >
            Categories
          </h2>

          <p
            class="mt-1 text-sm text-slate-400"
          >
            Explore books by category
          </p>

        </div>

        <router-link
          :to="{ name: 'BrowseBooks' }"
          class="text-sm font-medium text-blue-600 no-underline transition hover:underline"
        >
          View all
        </router-link>

      </div>


      <!-- =====================================================
           CATEGORY LOADING
      ====================================================== -->

      <div
        v-if="loadingCategories"
        class="flex items-center justify-center gap-2 px-5 py-8 text-sm text-slate-400"
      >

        <i
          class="bi bi-arrow-repeat animate-spin"
        ></i>

        Loading categories...

      </div>


      <!-- =====================================================
           CATEGORY CARDS
      ====================================================== -->

      <div
        v-else-if="categories.length > 0"
        class="flex gap-4 overflow-x-auto pb-2
               [&::-webkit-scrollbar]:h-1.5
               [&::-webkit-scrollbar-thumb]:rounded-full
               [&::-webkit-scrollbar-thumb]:bg-slate-300"
      >

        <CategoryCard
          v-for="category in categories"
          :key="category.id"
          :category="category"
          @click="goToCategory"
        />

      </div>


      <!-- =====================================================
           NO CATEGORIES
      ====================================================== -->

      <div
        v-else
        class="flex flex-col items-center justify-center p-8 text-slate-400"
      >

        <i
          class="bi bi-folder2-open mb-2 text-3xl"
        ></i>

        <p class="m-0">
          No categories found.
        </p>

      </div>

    </section>


    <!-- =====================================================
         RECOMMENDED BOOKS
    ====================================================== -->

    <section class="mb-10">

      <!-- SECTION HEADER -->

      <div
        class="mb-4 flex items-end justify-between gap-4"
      >

        <div>

          <h2
            class="m-0 text-xl font-bold text-slate-900"
          >
            Recommended Books
          </h2>

          <p
            class="mt-1 text-sm text-slate-400"
          >
            Books you may like
          </p>

        </div>

        <router-link
          :to="{ name: 'BrowseBooks' }"
          class="text-sm font-medium text-blue-600 no-underline transition hover:underline"
        >
          View all
        </router-link>

      </div>


      <!-- =====================================================
           BOOK LOADING
      ====================================================== -->

      <div
        v-if="loadingBooks"
        class="flex items-center justify-center gap-2 px-5 py-12 text-sm text-slate-400"
      >

        <i
          class="bi bi-arrow-repeat animate-spin"
        ></i>

        Loading books...

      </div>


      <!-- =====================================================
           BOOK CARDS
      ====================================================== -->

      <div
        v-else-if="books.length > 0"
        class="grid grid-cols-1 gap-4
               sm:grid-cols-2
               lg:grid-cols-3
               xl:grid-cols-4"
      >

        <BookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
          @bookmark="toggleBookmark"
          @view-details="viewBook"
        />

      </div>


      <!-- =====================================================
           NO BOOKS
      ====================================================== -->

      <div
        v-else
        class="flex flex-col items-center justify-center
               rounded-[14px] border border-slate-200
               bg-white px-5 py-12 text-center"
      >

        <i
          class="bi bi-book mb-2.5 text-4xl text-slate-400"
        ></i>

        <h3
          class="m-0 text-base font-medium text-slate-700"
        >
          No books found
        </h3>

        <p
          class="mt-1.5 text-sm text-slate-400"
        >
          There are no books available in the library yet.
        </p>

      </div>

    </section>


    <!-- =====================================================
         BOOK DETAILS
    ====================================================== -->

    <BookDetails
      v-if="selectedBook"
      :book="selectedBook"
      @close="closeBookDetails"
    />

  </div>

</template>


<script>

import CategoryCard
  from "../../components/books/CategoryCard.vue";

import BookCard
  from "../../components/books/BookCard.vue";

import BookDetails
  from "../../components/books/BookDetails.vue";


import {
  getCategories
} from "../../service/categoryService.js";


import {
  getBooks
} from "../../service/bookService.js";


export default {

  name: "Home",


  components: {

    CategoryCard,

    BookCard,

    BookDetails

  },


  data() {

    return {

      // ==================================================
      // SELECTED BOOK
      // ==================================================

      selectedBook: null,


      // ==================================================
      // CATEGORIES
      // ==================================================

      categories: [],

      loadingCategories: false,


      // ==================================================
      // BOOKS
      // ==================================================

      books: [],

      loadingBooks: false

    };

  },


  // =====================================================
  // CREATED
  // =====================================================

  async created() {

    /*
     * Load both APIs first.
     *
     * After books and categories are loaded,
     * calculate the number of books for every category.
     */

    await Promise.all([
      this.fetchCategories(),
      this.fetchBooks()
    ]);


    /*
     * Calculate book count
     */

    this.updateCategoryBookCount();

  },


  // =====================================================
  // METHODS
  // =====================================================

  methods: {


    // ==================================================
    // FETCH CATEGORIES
    // ==================================================

    async fetchCategories() {

      this.loadingCategories = true;


      try {

        const response =
          await getCategories();


        console.log(
          "HOME CATEGORY RESPONSE:",
          response
        );


        /*
         * Backend response:
         *
         * {
         *   status: 200,
         *   msg: "...",
         *   data: [...]
         * }
         */

        if (
          response &&
          Array.isArray(response.data)
        ) {

          this.categories =
            response.data;

        }


        /*
         * If API returns array directly
         */

        else if (
          Array.isArray(response)
        ) {

          this.categories =
            response;

        }


        else {

          this.categories = [];

        }


        /*
         * Add default bookCount
         *
         * This prevents undefined.
         */

        this.categories =
          this.categories.map(category => ({

            ...category,

            bookCount: 0

          }));


        console.log(
          "HOME CATEGORIES:",
          this.categories
        );

      }


      catch (error) {

        console.error(
          "Failed to load categories:",
          error
        );

        this.categories = [];

      }


      finally {

        this.loadingCategories = false;

      }

    },


    // ==================================================
    // FETCH BOOKS
    // ==================================================

    async fetchBooks() {

      this.loadingBooks = true;


      try {

        const response =
          await getBooks();


        console.log(
          "HOME BOOK RESPONSE:",
          response
        );


        /*
         * Backend response:
         *
         * {
         *   status: 200,
         *   msg: "...",
         *   data: [...]
         * }
         */

        if (
          response &&
          Array.isArray(response.data)
        ) {

          this.books =
            response.data;

        }


        /*
         * If API returns array directly
         */

        else if (
          Array.isArray(response)
        ) {

          this.books =
            response;

        }


        else {

          this.books = [];

        }


        /*
         * Make sure every book has bookmarked
         */

        this.books =
          this.books.map(book => ({

            ...book,

            bookmarked:
              book.bookmarked ?? false

          }));


        console.log(
          "HOME BOOKS:",
          this.books
        );

      }


      catch (error) {

        console.error(
          "Failed to load books:",
          error
        );

        this.books = [];

      }


      finally {

        this.loadingBooks = false;

      }

    },


    // ==================================================
    // UPDATE CATEGORY BOOK COUNT
    // ==================================================

    updateCategoryBookCount() {

      console.log(
        "CALCULATING CATEGORY BOOK COUNT..."
      );


      this.categories =
        this.categories.map(category => {


          /*
           * Count books that belong to
           * this category.
           */

          const count =
            this.books.filter(book => {


              // =========================================
              // CASE 1
              // book.category = { id, name }
              // =========================================

              if (
                book.category &&
                typeof book.category === "object"
              ) {

                return (
                  String(book.category.id) ===
                  String(category.id)
                );

              }


              // =========================================
              // CASE 2
              // book.categoryId = 1
              // =========================================

              if (
                book.categoryId !== undefined &&
                book.categoryId !== null
              ) {

                return (
                  String(book.categoryId) ===
                  String(category.id)
                );

              }


              // =========================================
              // CASE 3
              // book.categoryName = "Programming"
              // =========================================

              if (
                book.categoryName !== undefined &&
                book.categoryName !== null
              ) {

                return (
                  String(book.categoryName)
                    .toLowerCase()
                  ===
                  String(category.name)
                    .toLowerCase()
                );

              }


              // =========================================
              // CASE 4
              // book.category = "Programming"
              // =========================================

              if (
                typeof book.category === "string"
              ) {

                return (
                  book.category
                    .toLowerCase()
                  ===
                  String(category.name)
                    .toLowerCase()
                );

              }


              return false;

            }).length;


          console.log(
            `Category: ${category.name} => ${count} books`
          );


          return {

            ...category,

            bookCount: count

          };

        });


      console.log(
        "FINAL CATEGORIES:",
        this.categories
      );

    },


    // ==================================================
    // BOOKMARK
    // ==================================================

    toggleBookmark(book) {

      if (!book) {

        return;

      }


      book.bookmarked =
        !book.bookmarked;


      console.log(
        "Bookmark:",
        book
      );

    },


    // ==================================================
    // VIEW BOOK DETAILS
    // ==================================================

    viewBook(book) {

      console.log(
        "Home - View Book:",
        book
      );


      this.selectedBook =
        book;


      document.body.style.overflow =
        "hidden";

    },


    // ==================================================
    // CLOSE BOOK DETAILS
    // ==================================================

    closeBookDetails() {

      this.selectedBook =
        null;


      document.body.style.overflow =
        "";

    },


    // ==================================================
    // CATEGORY
    // ==================================================

    goToCategory(category) {

      console.log(
        "Selected category:",
        category
      );


      const categoryName =
        category.isOther
          ? "Other"
          : category.name;


      this.$router.push({

        name:
          "BrowseBooks",

        query: {

          category:
            categoryName

        }

      });

    }

  },


  // =====================================================
  // CLEAN UP
  // =====================================================

  beforeUnmount() {

    document.body.style.overflow =
      "";

  }

};

</script>