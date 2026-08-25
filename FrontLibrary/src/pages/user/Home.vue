<template>

  <div class="home-page">

    <!-- ================= CATEGORIES ================= -->

    <section class="section">

      <div class="section-header">

        <div>

          <h2>
            Categories
          </h2>

          <p class="section-subtitle">
            Explore books by category
          </p>

        </div>

        <router-link
          :to="{ name: 'BrowseBooks' }"
        >
          View all
        </router-link>

      </div>


      <!-- ================= CATEGORY LOADING ================= -->

      <div
        v-if="loadingCategories"
        class="loading-category"
      >

        <i class="bi bi-arrow-repeat"></i>

        Loading categories...

      </div>


      <!-- ================= CATEGORY CARDS ================= -->

      <div
        v-else-if="categories.length > 0"
        class="category-row"
      >

        <CategoryCard
          v-for="category in categories"
          :key="category.id"
          :category="category"
          @click="goToCategory"
        />

      </div>


      <!-- ================= NO CATEGORIES ================= -->

      <div
        v-else
        class="empty-category"
      >

        <i class="bi bi-folder2-open"></i>

        <p>
          No categories found.
        </p>

      </div>

    </section>


    <!-- ================= RECOMMENDED BOOKS ================= -->

    <section class="section">

      <div class="section-header">

        <div>

          <h2>
            Recommended Books
          </h2>

          <p class="section-subtitle">
            Books you may like
          </p>

        </div>

        <router-link
          :to="{ name: 'BrowseBooks' }"
        >
          View all
        </router-link>

      </div>


      <!-- ================= BOOK LOADING ================= -->

      <div
        v-if="loadingBooks"
        class="loading-books"
      >

        <i class="bi bi-arrow-repeat"></i>

        Loading books...

      </div>


      <!-- ================= BOOK CARDS ================= -->

      <div
        v-else-if="books.length > 0"
        class="book-row"
      >

        <BookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
          @bookmark="toggleBookmark"
          @view-details="viewBook"
        />

      </div>


      <!-- ================= NO BOOKS ================= -->

      <div
        v-else
        class="empty-books"
      >

        <i class="bi bi-book"></i>

        <h3>
          No books found
        </h3>

        <p>
          There are no books available in the library yet.
        </p>

      </div>

    </section>


    <!-- ================= BOOK DETAILS ================= -->

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

    await Promise.all([

      this.fetchCategories(),

      this.fetchBooks()

    ]);

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
         * Expected:
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
         * Expected:
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
         * Make sure every book has
         * bookmarked property
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
    // BOOKMARK
    // ==================================================

    toggleBookmark(book) {

      book.bookmarked =
        !book.bookmarked;

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


<style scoped>

/* ==================================================
   HOME PAGE
================================================== */

.home-page {

  min-height: 100vh;

  padding:
    1.5rem
    2rem;

  width: 100%;

  background:
    #f8faff;

}


/* ==================================================
   SECTION
================================================== */

.section {

  margin-bottom:
    2.5rem;

}


/* ==================================================
   SECTION HEADER
================================================== */

.section-header {

  display:
    flex;

  justify-content:
    space-between;

  align-items:
    flex-end;

  margin-bottom:
    1rem;

}


.section-header h2 {

  font-size:
    1.25rem;

  font-weight:
    700;

  margin:
    0;

  color:
    #0f172a;

}


.section-subtitle {

  color:
    #94a3b8;

  font-size:
    0.85rem;

  margin:
    0.3rem 0 0;

}


.section-header a {

  font-size:
    0.85rem;

  color:
    #2563eb;

  text-decoration:
    none;

  font-weight:
    500;

}


.section-header a:hover {

  text-decoration:
    underline;

}


/* ==================================================
   CATEGORY ROW
================================================== */

.category-row {

  display:
    flex;

  gap:
    1rem;

  overflow-x:
    auto;

  padding-bottom:
    0.5rem;

}


.category-row::-webkit-scrollbar {

  height:
    6px;

}


.category-row::-webkit-scrollbar-thumb {

  background:
    #cbd5e1;

  border-radius:
    999px;

}


/* ==================================================
   CATEGORY LOADING
================================================== */

.loading-category {

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  gap:
    8px;

  padding:
    30px;

  color:
    #94a3b8;

  font-size:
    14px;

}


.loading-category i {

  animation:
    spin 1s linear infinite;

}


/* ==================================================
   BOOK LOADING
================================================== */

.loading-books {

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  gap:
    8px;

  padding:
    50px;

  color:
    #94a3b8;

  font-size:
    14px;

}


.loading-books i {

  animation:
    spin 1s linear infinite;

}


/* ==================================================
   SPIN
================================================== */

@keyframes spin {

  from {

    transform:
      rotate(0deg);

  }

  to {

    transform:
      rotate(360deg);

  }

}


/* ==================================================
   EMPTY CATEGORY
================================================== */

.empty-category {

  display:
    flex;

  flex-direction:
    column;

  align-items:
    center;

  justify-content:
    center;

  padding:
    30px;

  color:
    #94a3b8;

}


.empty-category i {

  font-size:
    30px;

  margin-bottom:
    8px;

}


.empty-category p {

  margin:
    0;

}


/* ==================================================
   BOOK ROW
================================================== */

.book-row {

  display:
    grid;

  grid-template-columns:
    repeat(4, minmax(0, 1fr));

  gap:
    1rem;

}


/* ==================================================
   EMPTY BOOKS
================================================== */

.empty-books {

  display:
    flex;

  flex-direction:
    column;

  align-items:
    center;

  justify-content:
    center;

  padding:
    50px 20px;

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

  text-align:
    center;

}


.empty-books i {

  font-size:
    40px;

  color:
    #94a3b8;

  margin-bottom:
    10px;

}


.empty-books h3 {

  margin:
    0;

  color:
    #334155;

  font-size:
    1rem;

}


.empty-books p {

  margin:
    6px 0 0;

  color:
    #94a3b8;

  font-size:
    0.85rem;

}


/* ==================================================
   RESPONSIVE
================================================== */

@media (max-width: 1200px) {

  .book-row {

    grid-template-columns:
      repeat(3, minmax(0, 1fr));

  }

}


@media (max-width: 900px) {

  .book-row {

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

  }

}


@media (max-width: 640px) {

  .home-page {

    padding:
      1rem;

  }


  .section-header {

    align-items:
      center;

  }


  .book-row {

    grid-template-columns:
      1fr;

  }

}

</style>