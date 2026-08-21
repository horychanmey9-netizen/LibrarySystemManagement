<template>
  <div class="home-page">

    <!-- ================= CATEGORIES ================= -->
    <section class="section">

      <div class="section-header">

        <div>
          <h2>Categories</h2>

          <p class="section-subtitle">
            Explore books by category
          </p>
        </div>

        <router-link to="/browse-books">
          View all
        </router-link>

      </div>


      <!-- Category Cards -->
      <div class="category-row">

        <CategoryCard
          v-for="category in categories"
          :key="category.id"
          :category="category"
          @click="goToCategory(category.name)"
        />

      </div>

    </section>


    <!-- ================= RECOMMENDED BOOKS ================= -->
    <section class="section">

      <div class="section-header">

        <div>
          <h2>Recommended Books</h2>

          <p class="section-subtitle">
            Books you may like
          </p>
        </div>

        <router-link to="/browse-books">
          View all
        </router-link>

      </div>


      <!-- Book Cards -->
      <div class="book-row">

        <BookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
          @bookmark="toggleBookmark"
          @view-details="viewBook"
        />

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
      // CATEGORY JSON DATA
      // ==================================================

      categories: [

        {
          id: 1,
          name: "Fiction",
          count: 320,
          icon: "bi bi-book",
          bg: "#F3E8FF",
          color: "#9333EA"
        },

        {
          id: 2,
          name: "Science",
          count: 180,
          icon: "bi bi-eyedropper",
          bg: "#DCFCE7",
          color: "#16A34A"
        },

        {
          id: 3,
          name: "Technology",
          count: 150,
          icon: "bi bi-laptop",
          bg: "#DBEAFE",
          color: "#2563EB"
        },

        {
          id: 4,
          name: "History",
          count: 120,
          icon: "bi bi-bank",
          bg: "#FFEDD5",
          color: "#EA580C"
        },

        {
          id: 5,
          name: "Others",
          count: 478,
          icon: "bi bi-journal",
          bg: "#F1F5F9",
          color: "#475569"
        }

      ],


      // ==================================================
      // BOOK JSON DATA
      // ==================================================

      books: [

        {
          id: 1,

          title: "The Great Gatsby",

          author: "F. Scott Fitzgerald",

          category: "Fiction",

          status: true,

          bookmarked: false,

          qty: 5,

          pages: 180,

          isbn: "9780743273565",

          language: "English",

          description:
            "The Great Gatsby is a classic American novel that explores themes of wealth, love, ambition, and the American Dream during the Jazz Age.",

          image:
            "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg"
        },


        {
          id: 2,

          title: "A Brief History of Time",

          author: "Stephen Hawking",

          category: "Science",

          status: true,

          bookmarked: false,

          qty: 4,

          pages: 256,

          isbn: "9780553380163",

          language: "English",

          description:
            "A Brief History of Time explores the origins and structure of the universe, including space, time, black holes, and the nature of reality.",

          image:
            "https://covers.openlibrary.org/b/isbn/9780553380163-L.jpg"
        },


        {
          id: 3,

          title: "Clean Code",

          author: "Robert C. Martin",

          category: "Technology",

          status: false,

          bookmarked: false,

          qty: 3,

          pages: 464,

          isbn: "9780132350884",

          language: "English",

          description:
            "Clean Code provides practical principles and techniques for writing readable, maintainable, and professional software.",

          image:
            "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg"
        },


        {
          id: 4,

          title: "Sapiens",

          author: "Yuval Noah Harari",

          category: "History",

          status: true,

          bookmarked: false,

          qty: 6,

          pages: 443,

          isbn: "9780062316097",

          language: "English",

          description:
            "Sapiens examines the history of humankind, from early human societies to modern civilization and the forces that shaped our world.",

          image:
            "https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg"
        }

      ]

    };

  },


  // ==================================================
  // METHODS
  // ==================================================

  methods: {


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

      this.selectedBook = book;

      document.body.style.overflow = "hidden";

    },


    // ==================================================
    // CLOSE BOOK DETAILS
    // ==================================================

    closeBookDetails() {

      this.selectedBook = null;

      document.body.style.overflow = "";

    },


    // ==================================================
    // CATEGORY
    // ==================================================

    goToCategory(category) {

      this.$router.push({

        path: "/browse-books",

        query: {
          category: category
        }

      });

    }

  },


  // ==================================================
  // CLEAN UP
  // ==================================================

  beforeUnmount() {

    document.body.style.overflow = "";

  }

};
</script>


<style scoped>

.home-page {

  min-height: 100vh;

  padding: 1.5rem 2rem;

  width: 100%;

  background: #f8faff;

}


/* ==================================================
   SECTION
================================================== */

.section {

  margin-bottom: 2.5rem;

}


/* ==================================================
   SECTION HEADER
================================================== */

.section-header {

  display: flex;

  justify-content: space-between;

  align-items: flex-end;

  margin-bottom: 1rem;

}


.section-header h2 {

  font-size: 1.25rem;

  font-weight: 700;

  margin: 0;

}


.section-subtitle {

  color: #94a3b8;

  font-size: 0.85rem;

  margin: 0.3rem 0 0;

}


.section-header a {

  font-size: 0.85rem;

  color: #2563eb;

  text-decoration: none;

  font-weight: 500;

}


.section-header a:hover {

  text-decoration: underline;

}


/* ==================================================
   CATEGORY ROW
================================================== */

.category-row {

  display: flex;

  gap: 1rem;

  overflow-x: auto;

  padding-bottom: 0.5rem;

}


.category-row::-webkit-scrollbar {

  height: 6px;

}


.category-row::-webkit-scrollbar-thumb {

  background: #cbd5e1;

  border-radius: 999px;

}


/* ==================================================
   BOOK ROW
================================================== */

.book-row {

  display: grid;

  grid-template-columns:
    repeat(4, minmax(0, 1fr));

  gap: 1rem;

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

    padding: 1rem;

  }


  .section-header {

    align-items: center;

  }


  .book-row {

    grid-template-columns: 1fr;

  }

}

</style>