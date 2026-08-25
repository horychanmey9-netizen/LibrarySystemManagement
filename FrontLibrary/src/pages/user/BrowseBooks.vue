<template>
  <div class="browse-page">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="page-header">

      <div>
        <h1>Browse Books</h1>

        <p>
          Discover and explore books from our library collection.
        </p>
      </div>

    </div>


    <!-- =====================================================
         TOP FILTER BAR
    ====================================================== -->

    <div class="top-filters">

      <!-- SEARCH -->

      <div class="search-box">

        <i class="bi bi-search"></i>

        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search books by title, author, ISBN..."
        />

      </div>


      <!-- CATEGORY DROPDOWN -->

      <div class="select-box">

        <select
          v-model="selectedCategory"
        >

          <option value="All">
            All Categories
          </option>

          <option
            v-for="category in categories"
            :key="category.id"
            :value="category.name"
          >
            {{ category.name }}
          </option>

          <option value="Other">
            Other
          </option>

        </select>

        <i class="bi bi-chevron-down"></i>

      </div>


      <!-- AVAILABILITY -->

      <div class="select-box">

        <select
          v-model="selectedAvailability"
        >

          <option value="All">
            All Availability
          </option>

          <option value="Available">
            Available
          </option>

          <option value="Borrowed">
            Borrowed
          </option>

        </select>

        <i class="bi bi-chevron-down"></i>

      </div>


      <!-- SORT -->

      <div class="select-box">

        <select
          v-model="selectedSort"
        >

          <option value="default">
            Sort by
          </option>

          <option value="title-asc">
            Title A-Z
          </option>

          <option value="title-desc">
            Title Z-A
          </option>

          <option value="author-asc">
            Author A-Z
          </option>

          <option value="author-desc">
            Author Z-A
          </option>

        </select>

        <i class="bi bi-chevron-down"></i>

      </div>

    </div>


    <!-- =====================================================
         MAIN CONTENT
    ====================================================== -->

    <div class="browse-content">


      <!-- ===================================================
           SIDEBAR
      ==================================================== -->

      <aside class="filter-sidebar">

        <!-- FILTER HEADER -->

        <div class="filter-header">

          <h2>
            Filter
          </h2>

          <button
            type="button"
            @click="clearFilters"
          >
            Clear all
          </button>

        </div>


        <!-- =================================================
             CATEGORIES
        ================================================== -->

        <div class="filter-group">

          <h3>
            Categories
          </h3>


          <!-- LOADING -->

          <div
            v-if="loadingCategories"
            class="filter-loading"
          >
            Loading categories...
          </div>


          <!-- ERROR -->

          <div
            v-else-if="categoryError"
            class="filter-error"
          >
            {{ categoryError }}
          </div>


          <!-- CATEGORY LIST -->

          <div
            v-else
            class="checkbox-list"
          >

            <!-- BACKEND CATEGORIES -->

            <label
              v-for="category in categories"
              :key="category.id"
              class="checkbox-row"
            >

              <input
                type="checkbox"
                :value="category.name"
                v-model="selectedCategories"
                @change="syncCategoryFromCheckbox"
              />

              <span class="checkbox-custom"></span>

              <span class="checkbox-name">
                {{ category.name }}
              </span>

              <span class="category-count">
                {{ getCategoryCount(category.name) }}
              </span>

            </label>


            <!-- OTHER -->

            <label class="checkbox-row">

              <input
                type="checkbox"
                value="Other"
                v-model="selectedCategories"
                @change="syncCategoryFromCheckbox"
              />

              <span class="checkbox-custom"></span>

              <span class="checkbox-name">
                Other
              </span>

              <span class="category-count">
                {{ getOtherCount() }}
              </span>

            </label>

          </div>

        </div>


        <!-- =================================================
             AVAILABILITY
        ================================================== -->

        <div class="filter-group">

          <h3>
            Availability
          </h3>


          <div class="checkbox-list">

            <!-- AVAILABLE -->

            <label class="checkbox-row">

              <input
                type="checkbox"
                value="Available"
                v-model="selectedAvailabilityFilters"
                @change="syncAvailability"
              />

              <span class="checkbox-custom"></span>

              <span class="checkbox-name">
                Available
              </span>

              <span class="category-count">
                {{ availableCount }}
              </span>

            </label>


            <!-- BORROWED -->

            <label class="checkbox-row">

              <input
                type="checkbox"
                value="Borrowed"
                v-model="selectedAvailabilityFilters"
                @change="syncAvailability"
              />

              <span class="checkbox-custom"></span>

              <span class="checkbox-name">
                Borrowed
              </span>

              <span class="category-count">
                {{ borrowedCount }}
              </span>

            </label>

          </div>

        </div>


        <!-- =================================================
             LANGUAGE
        ================================================== -->

        <div class="filter-group">

          <h3>
            Language
          </h3>


          <div class="checkbox-list">

            <!-- ENGLISH -->

            <label class="checkbox-row">

              <input
                type="checkbox"
                value="English"
                v-model="selectedLanguages"
              />

              <span class="checkbox-custom"></span>

              <span class="checkbox-name">
                English
              </span>

              <span class="category-count">
                {{ getLanguageCount("English") }}
              </span>

            </label>


            <!-- KHMER -->

            <label class="checkbox-row">

              <input
                type="checkbox"
                value="Khmer"
                v-model="selectedLanguages"
              />

              <span class="checkbox-custom"></span>

              <span class="checkbox-name">
                Khmer
              </span>

              <span class="category-count">
                {{ getLanguageCount("Khmer") }}
              </span>

            </label>

          </div>

        </div>

      </aside>


      <!-- ===================================================
           BOOK CONTENT
      ==================================================== -->

      <main class="books-content">

        <!-- =================================================
             BOOK TOOLBAR
        ================================================== -->

        <div class="books-toolbar">

          <p class="result-count">

            Showing

            <strong>
              {{ filteredBooks.length }}
            </strong>

            of

            <strong>
              {{ books.length }}
            </strong>

            books

          </p>


          <!-- VIEW TOGGLE -->

          <div class="view-toggle">

            <button
              type="button"
              :class="{
                active: viewMode === 'grid'
              }"
              @click="viewMode = 'grid'"
              aria-label="Grid view"
            >

              <i class="bi bi-grid-3x3-gap"></i>

            </button>


            <button
              type="button"
              :class="{
                active: viewMode === 'list'
              }"
              @click="viewMode = 'list'"
              aria-label="List view"
            >

              <i class="bi bi-list"></i>

            </button>

          </div>

        </div>


        <!-- =================================================
             BOOK LOADING
        ================================================== -->

        <div
          v-if="loadingBooks"
          class="books-loading"
        >

          <i class="bi bi-arrow-repeat"></i>

          <p>
            Loading books...
          </p>

        </div>


        <!-- =================================================
             BOOK ERROR
        ================================================== -->

        <div
          v-else-if="bookError"
          class="books-error"
        >

          <i class="bi bi-exclamation-circle"></i>

          <p>
            {{ bookError }}
          </p>

          <button
            type="button"
            @click="fetchBooks"
          >
            Try Again
          </button>

        </div>


        <!-- =================================================
             GRID VIEW
        ================================================== -->

        <div
          v-else-if="
            viewMode === 'grid' &&
            filteredBooks.length > 0
          "
          class="book-grid"
        >

          <BookCard
            v-for="book in filteredBooks"
            :key="book.id"
            :book="book"
            @bookmark="toggleBookmark"
            @view-details="viewBook"
          />

        </div>


        <!-- =================================================
             LIST VIEW
        ================================================== -->

        <div
          v-else-if="
            viewMode === 'list' &&
            filteredBooks.length > 0
          "
          class="book-list"
        >

          <div
            v-for="book in filteredBooks"
            :key="book.id"
            class="book-list-card"
          >

            <!-- IMAGE -->

            <div class="list-image">

              <img
                v-if="book.image"
                :src="book.image"
                :alt="book.title"
                @error="handleImageError"
              />

              <div v-else>
                📚
              </div>

            </div>


            <!-- INFORMATION -->

            <div class="list-info">

              <h3>
                {{ book.title }}
              </h3>

              <p class="list-author">
                {{ book.author }}
              </p>

              <span class="book-category">
                {{ book.category }}
              </span>

              <span
                v-if="book.status === true"
                class="status available"
              >
                ✓ Available
              </span>

              <span
                v-else
                class="status borrowed"
              >
                × Borrowed
              </span>

            </div>


            <!-- ACTION -->

            <div class="list-action">

              <button
                type="button"
                @click="viewBook(book)"
              >
                View Details
              </button>

            </div>

          </div>

        </div>


        <!-- =================================================
             EMPTY STATE
        ================================================== -->

        <div
          v-else
          class="empty-state"
        >

          <div class="empty-icon">
            📚
          </div>

          <h3>
            No books found
          </h3>

          <p>
            Try changing your search or filters.
          </p>

          <button
            type="button"
            @click="clearFilters"
          >
            Clear Filters
          </button>

        </div>

      </main>

    </div>


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

import BookCard
  from "../../components/books/BookCard.vue";

import BookDetails
  from "../../components/books/BookDetails.vue";

import {
  getBooks
} from "../../service/bookService.js";

import {
  getCategories
} from "../../service/categoryService.js";


export default {

  name: "BrowseBooks",


  components: {

    BookCard,

    BookDetails

  },


  data() {

    return {

      // ==================================================
      // SEARCH
      // ==================================================

      searchQuery: "",


      // ==================================================
      // CATEGORY
      // ==================================================

      categories: [],

      loadingCategories: false,

      categoryError: null,


      // ==================================================
      // CATEGORY FILTER
      // ==================================================

      selectedCategory: "All",

      selectedCategories: [],


      // ==================================================
      // AVAILABILITY
      // ==================================================

      selectedAvailability: "All",

      selectedAvailabilityFilters: [],


      // ==================================================
      // LANGUAGE
      // ==================================================

      selectedLanguages: [],


      // ==================================================
      // SORT
      // ==================================================

      selectedSort: "default",


      // ==================================================
      // VIEW
      // ==================================================

      viewMode: "grid",


      // ==================================================
      // BOOKS FROM BACKEND
      // ==================================================

      books: [],

      loadingBooks: false,

      bookError: null,


      // ==================================================
      // SELECTED BOOK
      // ==================================================

      selectedBook: null

    };

  },


  // =====================================================
  // COMPUTED
  // =====================================================

  computed: {

    // ==================================================
    // FILTERED BOOKS
    // ==================================================

    filteredBooks() {

      let result = [
        ...this.books
      ];


      // =================================================
      // SEARCH
      // =================================================

      if (
        this.searchQuery.trim()
      ) {

        const keyword =
          this.searchQuery
            .toLowerCase()
            .trim();


        result =
          result.filter(book => {

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

      }


      // =================================================
      // CATEGORY DROPDOWN
      // =================================================

      if (
        this.selectedCategory !== "All"
      ) {

        if (
          this.selectedCategory === "Other"
        ) {

          result =
            result.filter(book => {

              return !this.categories.some(
                category =>
                  category.name ===
                  book.category
              );

            });

        }

        else {

          result =
            result.filter(book => {

              return (
                book.category ===
                this.selectedCategory
              );

            });

        }

      }


      // =================================================
      // SIDEBAR CATEGORY CHECKBOX
      // =================================================

      if (
        this.selectedCategories.length > 0
      ) {

        result =
          result.filter(book => {

            // OTHER

            if (
              this.selectedCategories.includes(
                "Other"
              )
            ) {

              const normalCategory =
                this.categories.some(
                  category =>
                    category.name ===
                    book.category
                );


              if (!normalCategory) {

                return true;

              }

            }


            // NORMAL CATEGORY

            return this.selectedCategories.includes(
              book.category
            );

          });

      }


      // =================================================
      // AVAILABILITY DROPDOWN
      // =================================================

      if (
        this.selectedAvailability !== "All"
      ) {

        if (
          this.selectedAvailability ===
          "Available"
        ) {

          result =
            result.filter(
              book =>
                book.status === true
            );

        }


        if (
          this.selectedAvailability ===
          "Borrowed"
        ) {

          result =
            result.filter(
              book =>
                book.status === false
            );

        }

      }


      // =================================================
      // AVAILABILITY CHECKBOX
      // =================================================

      if (
        this.selectedAvailabilityFilters.length > 0
      ) {

        result =
          result.filter(book => {

            if (
              book.status === true &&
              this.selectedAvailabilityFilters.includes(
                "Available"
              )
            ) {

              return true;

            }


            if (
              book.status === false &&
              this.selectedAvailabilityFilters.includes(
                "Borrowed"
              )
            ) {

              return true;

            }


            return false;

          });

      }


      // =================================================
      // LANGUAGE
      // =================================================

      if (
        this.selectedLanguages.length > 0
      ) {

        result =
          result.filter(book => {

            return this.selectedLanguages.includes(
              book.language
            );

          });

      }


      // =================================================
      // SORT
      // =================================================

      switch (
        this.selectedSort
      ) {

        case "title-asc":

          result.sort(
            (a, b) =>
              String(a.title || "")
                .localeCompare(
                  String(b.title || "")
                )
          );

          break;


        case "title-desc":

          result.sort(
            (a, b) =>
              String(b.title || "")
                .localeCompare(
                  String(a.title || "")
                )
          );

          break;


        case "author-asc":

          result.sort(
            (a, b) =>
              String(a.author || "")
                .localeCompare(
                  String(b.author || "")
                )
          );

          break;


        case "author-desc":

          result.sort(
            (a, b) =>
              String(b.author || "")
                .localeCompare(
                  String(a.author || "")
                )
          );

          break;

      }


      return result;

    },


    // ==================================================
    // AVAILABLE COUNT
    // ==================================================

    availableCount() {

      return this.books.filter(
        book =>
          book.status === true
      ).length;

    },


    // ==================================================
    // BORROWED COUNT
    // ==================================================

    borrowedCount() {

      return this.books.filter(
        book =>
          book.status === false
      ).length;

    }

  },


  // =====================================================
  // MOUNTED
  // =====================================================

  async mounted() {

    await Promise.all([
      this.fetchCategories(),
      this.fetchBooks()
    ]);


    this.readCategoryFromURL();

  },


  // =====================================================
  // METHODS
  // =====================================================

  methods: {

    // ==================================================
    // FETCH BOOKS
    // ==================================================

    async fetchBooks() {

      this.loadingBooks = true;

      this.bookError = null;


      try {

        const result =
          await getBooks();


        console.log(
          "BOOK API RESPONSE:",
          result
        );


        let bookData = [];


        // =================================================
        // RESPONSE:
        //
        // {
        //   status: 200,
        //   msg: "...",
        //   data: [...]
        // }
        // =================================================

        if (
          result &&
          Array.isArray(result.data)
        ) {

          bookData =
            result.data;

        }


        // =================================================
        // ARRAY DIRECTLY
        // =================================================

        else if (
          Array.isArray(result)
        ) {

          bookData =
            result;

        }


        // =================================================
        // NO DATA
        // =================================================

        else {

          bookData = [];

        }


        // =================================================
        // CONVERT BACKEND BOOK
        // TO FRONTEND BOOK FORMAT
        // =================================================

        this.books =
          bookData.map(book => {

            return {

              id:
                book.id,

              title:
                book.title || "",

              author:
                book.author || "",

              category:
                book.category?.name ||
                book.categoryName ||
                book.category ||
                "Other",

              status:
                book.status === true ||
                book.status === "Available" ||
                book.status === "AVAILABLE",

              bookmarked:
                false,

              qty:
                Number(
                  book.qty || 0
                ),

              pages:
                Number(
                  book.pages || 0
                ),

              isbn:
                book.isbn || "",

              language:
                book.language || "",

              description:
                book.description || "",

              image:
                book.image ||
                book.imageUrl ||
                book.coverImage ||
                null

            };

          });


        console.log(
          "BOOKS FROM BACKEND:",
          this.books
        );

      }

      catch (error) {

        console.error(
          "Fetch books error:",
          error
        );


        this.bookError =
          error.message ||
          "Failed to load books";


        this.books = [];

      }

      finally {

        this.loadingBooks = false;

      }

    },


    // ==================================================
    // FETCH CATEGORIES
    // ==================================================

    async fetchCategories() {

      this.loadingCategories =
        true;

      this.categoryError =
        null;


      try {

        const result =
          await getCategories();


        console.log(
          "CATEGORY API RESPONSE:",
          result
        );


        // =================================================
        // RESPONSE:
        //
        // {
        //   status: 200,
        //   msg: "...",
        //   data: [...]
        // }
        // =================================================

        if (
          result &&
          Array.isArray(result.data)
        ) {

          this.categories =
            result.data;

        }


        // =================================================
        // ARRAY DIRECTLY
        // =================================================

        else if (
          Array.isArray(result)
        ) {

          this.categories =
            result;

        }


        else {

          this.categories =
            [];

        }


        console.log(
          "CATEGORIES FROM BACKEND:",
          this.categories
        );

      }

      catch (error) {

        console.error(
          "Fetch categories error:",
          error
        );


        this.categoryError =
          error.message ||
          "Failed to load categories";


        this.categories =
          [];

      }

      finally {

        this.loadingCategories =
          false;

      }

    },


    // ==================================================
    // READ CATEGORY FROM URL
    // ==================================================

    readCategoryFromURL() {

      const category =
        this.$route.query.category;


      if (!category) {

        this.selectedCategory =
          "All";

        this.selectedCategories =
          [];

        return;

      }


      this.selectedCategory =
        category;


      if (
        category !== "All"
      ) {

        this.selectedCategories =
          [category];

      }

    },


    // ==================================================
    // CATEGORY CHECKBOX SYNC
    // ==================================================

    syncCategoryFromCheckbox() {

      // ================================================
      // NO CATEGORY
      // ================================================

      if (
        this.selectedCategories.length === 0
      ) {

        this.selectedCategory =
          "All";


        this.$router.push({

          name:
            "BrowseBooks"

        });

        return;

      }


      // ================================================
      // ONE CATEGORY
      // ================================================

      if (
        this.selectedCategories.length === 1
      ) {

        const category =
          this.selectedCategories[0];


        this.selectedCategory =
          category;


        this.$router.push({

          name:
            "BrowseBooks",

          query: {

            category:
              category

          }

        });

      }


      // ================================================
      // MULTIPLE CATEGORIES
      // ================================================

      else {

        this.selectedCategory =
          "All";


        this.$router.push({

          name:
            "BrowseBooks"

        });

      }

    },


    // ==================================================
    // AVAILABILITY SYNC
    // ==================================================

    syncAvailability() {

      if (
        this.selectedAvailabilityFilters.length === 0
      ) {

        this.selectedAvailability =
          "All";

        return;

      }


      if (
        this.selectedAvailabilityFilters.length === 1
      ) {

        this.selectedAvailability =
          this.selectedAvailabilityFilters[0];

      }

      else {

        this.selectedAvailability =
          "All";

      }

    },


    // ==================================================
    // CATEGORY COUNT
    // ==================================================

    getCategoryCount(categoryName) {

      return this.books.filter(
        book =>
          book.category ===
          categoryName
      ).length;

    },


    // ==================================================
    // OTHER COUNT
    // ==================================================

    getOtherCount() {

      return this.books.filter(
        book => {

          return !this.categories.some(
            category =>
              category.name ===
              book.category
          );

        }
      ).length;

    },


    // ==================================================
    // LANGUAGE COUNT
    // ==================================================

    getLanguageCount(language) {

      return this.books.filter(
        book =>
          book.language ===
          language
      ).length;

    },


    // ==================================================
    // CLEAR FILTERS
    // ==================================================

    clearFilters() {

      this.searchQuery =
        "";


      this.selectedCategory =
        "All";


      this.selectedCategories =
        [];


      this.selectedAvailability =
        "All";


      this.selectedAvailabilityFilters =
        [];


      this.selectedLanguages =
        [];


      this.selectedSort =
        "default";


      this.$router.push({

        name:
          "BrowseBooks"

      });

    },


    // ==================================================
    // BOOKMARK
    // ==================================================

    toggleBookmark(book) {

      book.bookmarked =
        !book.bookmarked;

    },


    // ==================================================
    // VIEW DETAILS
    // ==================================================

    viewBook(book) {

      console.log(
        "Browse Books - View Book:",
        book
      );


      this.selectedBook =
        book;


      document.body.style.overflow =
        "hidden";

    },


    // ==================================================
    // CLOSE DETAILS
    // ==================================================

    closeBookDetails() {

      this.selectedBook =
        null;


      document.body.style.overflow =
        "";

    },


    // ==================================================
    // IMAGE ERROR
    // ==================================================

    handleImageError(event) {

      event.target.style.display =
        "none";

    }

  },


  // =====================================================
  // WATCH ROUTE
  // =====================================================

  watch: {

    "$route.query.category"() {

      this.readCategoryFromURL();

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

/* =====================================================
   PAGE
===================================================== */

.browse-page {

  min-height: 100vh;

  width: 100%;

  background: #f8faff;

  padding:
    2rem 4.2rem;

}


/* =====================================================
   HEADER
===================================================== */

.page-header {

  margin-bottom:
    2rem;

}


.page-header h1 {

  margin: 0;

  color: #0f172a;

  font-size:
    2rem;

  font-weight:
    700;

  letter-spacing:
    -0.02em;

}


.page-header p {

  margin:
    0.35rem 0 0;

  color:
    #94a3b8;

  font-size:
    1rem;

}


/* =====================================================
   TOP FILTERS
===================================================== */

.top-filters {

  display: grid;

  grid-template-columns:
    1.15fr
    1fr
    1fr
    1fr;

  gap:
    1rem;

  margin-bottom:
    2rem;

}


/* =====================================================
   SEARCH
===================================================== */

.search-box {

  height:
    58px;

  display:
    flex;

  align-items:
    center;

  gap:
    0.8rem;

  padding:
    0 1rem;

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

  box-sizing:
    border-box;

}


.search-box i {

  color:
    #94a3b8;

  font-size:
    1rem;

}


.search-box input {

  width:
    100%;

  height:
    100%;

  border:
    none;

  outline:
    none;

  background:
    transparent;

  color:
    #334155;

  font-size:
    0.95rem;

}


.search-box input::placeholder {

  color:
    #94a3b8;

}


/* =====================================================
   SELECT
===================================================== */

.select-box {

  position:
    relative;

  height:
    58px;

}


.select-box select {

  width:
    100%;

  height:
    100%;

  appearance:
    none;

  -webkit-appearance:
    none;

  padding:
    0 2.8rem 0 1.2rem;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

  background:
    white;

  color:
    #1e293b;

  font-size:
    0.95rem;

  outline:
    none;

  cursor:
    pointer;

}


.select-box select:focus {

  border-color:
    #93c5fd;

  box-shadow:
    0 0 0 3px
    rgba(59, 130, 246, 0.08);

}


.select-box i {

  position:
    absolute;

  right:
    1rem;

  top:
    50%;

  transform:
    translateY(-50%);

  pointer-events:
    none;

  color:
    #0f172a;

}


/* =====================================================
   MAIN CONTENT
===================================================== */

.browse-content {

  display:
    grid;

  grid-template-columns:
    260px minmax(0, 1fr);

  gap:
    2.5rem;

  align-items:
    start;

}


/* =====================================================
   SIDEBAR
===================================================== */

.filter-sidebar {

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

  padding:
    1.3rem;

}


/* =====================================================
   FILTER HEADER
===================================================== */

.filter-header {

  display:
    flex;

  justify-content:
    space-between;

  align-items:
    center;

  margin-bottom:
    1.5rem;

}


.filter-header h2 {

  margin:
    0;

  color:
    #0f172a;

  font-size:
    1rem;

  font-weight:
    700;

}


.filter-header button {

  border:
    none;

  background:
    transparent;

  color:
    #2563eb;

  font-size:
    0.85rem;

  cursor:
    pointer;

}


.filter-header button:hover {

  text-decoration:
    underline;

}


/* =====================================================
   FILTER GROUP
===================================================== */

.filter-group {

  padding:
    0 0 1.25rem;

  margin-bottom:
    1.25rem;

  border-bottom:
    1px solid #f1f5f9;

}


.filter-group:last-child {

  border-bottom:
    none;

  margin-bottom:
    0;

  padding-bottom:
    0;

}


.filter-group h3 {

  margin:
    0 0 0.9rem;

  color:
    #0f172a;

  font-size:
    0.9rem;

  font-weight:
    600;

}


/* =====================================================
   CHECKBOX LIST
===================================================== */

.checkbox-list {

  display:
    flex;

  flex-direction:
    column;

  gap:
    0.75rem;

}


.checkbox-row {

  position:
    relative;

  display:
    flex;

  align-items:
    center;

  gap:
    0.65rem;

  min-height:
    20px;

  cursor:
    pointer;

}


.checkbox-row input {

  position:
    absolute;

  opacity:
    0;

  pointer-events:
    none;

}


.checkbox-custom {

  width:
    15px;

  height:
    15px;

  flex-shrink:
    0;

  border:
    1.5px solid #94a3b8;

  border-radius:
    3px;

  background:
    white;

  transition:
    all 0.2s ease;

}


.checkbox-row input:checked
+ .checkbox-custom {

  border-color:
    #2563eb;

  background:
    #2563eb;

  box-shadow:
    inset 0 0 0 3px white;

}


.checkbox-name {

  flex:
    1;

  color:
    #334155;

  font-size:
    0.88rem;

}


.category-count {

  color:
    #94a3b8;

  font-size:
    0.82rem;

}


/* =====================================================
   LOADING
===================================================== */

.filter-loading {

  color:
    #94a3b8;

  font-size:
    0.82rem;

  padding:
    0.3rem 0;

}


/* =====================================================
   ERROR
===================================================== */

.filter-error {

  color:
    #dc2626;

  font-size:
    0.82rem;

  line-height:
    1.4;

}


/* =====================================================
   BOOK CONTENT
===================================================== */

.books-content {

  min-width:
    0;

}


/* =====================================================
   BOOK TOOLBAR
===================================================== */

.books-toolbar {

  display:
    flex;

  justify-content:
    space-between;

  align-items:
    center;

  margin-bottom:
    1.2rem;

}


.result-count {

  margin:
    0;

  color:
    #94a3b8;

  font-size:
    0.9rem;

}


.result-count strong {

  color:
    #64748b;

  font-weight:
    600;

}


/* =====================================================
   VIEW TOGGLE
===================================================== */

.view-toggle {

  display:
    flex;

  align-items:
    center;

  gap:
    0.25rem;

  padding:
    0.2rem;

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    10px;

}


.view-toggle button {

  width:
    42px;

  height:
    38px;

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  border:
    none;

  border-radius:
    8px;

  background:
    transparent;

  color:
    #94a3b8;

  cursor:
    pointer;

  font-size:
    1rem;

}


.view-toggle button.active {

  background:
    #eff6ff;

  color:
    #2563eb;

}


/* =====================================================
   BOOK LOADING
===================================================== */

.books-loading {

  min-height:
    300px;

  display:
    flex;

  flex-direction:
    column;

  align-items:
    center;

  justify-content:
    center;

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

  color:
    #64748b;

}


.books-loading i {

  font-size:
    2rem;

  margin-bottom:
    0.8rem;

  color:
    #2563eb;

  animation:
    spin 1s linear infinite;

}


.books-loading p {

  margin:
    0;

  font-size:
    0.9rem;

}


/* =====================================================
   BOOK ERROR
===================================================== */

.books-error {

  min-height:
    300px;

  display:
    flex;

  flex-direction:
    column;

  align-items:
    center;

  justify-content:
    center;

  text-align:
    center;

  background:
    white;

  border:
    1px solid #fecaca;

  border-radius:
    14px;

  color:
    #dc2626;

}


.books-error i {

  font-size:
    2rem;

  margin-bottom:
    0.8rem;

}


.books-error p {

  margin:
    0 0 1rem;

  font-size:
    0.9rem;

}


.books-error button {

  border:
    none;

  border-radius:
    8px;

  padding:
    0.6rem 1rem;

  background:
    #2563eb;

  color:
    white;

  cursor:
    pointer;

}


.books-error button:hover {

  background:
    #1d4ed8;

}


/* =====================================================
   BOOK GRID
===================================================== */

.book-grid {

  display:
    grid;

  grid-template-columns:
    repeat(3, minmax(0, 1fr));

  gap:
    1.2rem;

}


/* =====================================================
   LIST VIEW
===================================================== */

.book-list {

  display:
    flex;

  flex-direction:
    column;

  gap:
    1rem;

}


.book-list-card {

  display:
    flex;

  align-items:
    center;

  gap:
    1.2rem;

  padding:
    1rem;

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

}


.list-image {

  width:
    100px;

  height:
    130px;

  flex-shrink:
    0;

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  overflow:
    hidden;

  background:
    #f1f5f9;

  border-radius:
    10px;

}


.list-image img {

  width:
    100%;

  height:
    100%;

  object-fit:
    contain;

}


.list-info {

  flex:
    1;

}


.list-info h3 {

  margin:
    0;

  color:
    #0f172a;

  font-size:
    1.05rem;

  font-weight:
    700;

}


.list-author {

  margin:
    0.4rem 0;

  color:
    #94a3b8;

  font-size:
    0.85rem;

}


.book-category {

  display:
    inline-block;

  margin-right:
    0.5rem;

  padding:
    0.3rem 0.65rem;

  border-radius:
    999px;

  background:
    #eff6ff;

  color:
    #2563eb;

  font-size:
    0.75rem;

}


.status {

  display:
    inline-block;

  padding:
    0.3rem 0.65rem;

  border-radius:
    999px;

  font-size:
    0.75rem;

}


.status.available {

  background:
    #ecfdf5;

  color:
    #059669;

}


.status.borrowed {

  background:
    #fef2f2;

  color:
    #ef4444;

}


.list-action button {

  padding:
    0.65rem 1rem;

  border:
    1px solid #2563eb;

  border-radius:
    9px;

  background:
    white;

  color:
    #2563eb;

  cursor:
    pointer;

}


.list-action button:hover {

  background:
    #2563eb;

  color:
    white;

}


/* =====================================================
   EMPTY STATE
===================================================== */

.empty-state {

  padding:
    5rem 1rem;

  text-align:
    center;

  background:
    white;

  border:
    1px solid #e2e8f0;

  border-radius:
    14px;

}


.empty-icon {

  font-size:
    3.5rem;

  margin-bottom:
    0.8rem;

}


.empty-state h3 {

  margin:
    0;

  color:
    #334155;

  font-size:
    1.1rem;

}


.empty-state p {

  margin:
    0.4rem 0 1rem;

  color:
    #94a3b8;

  font-size:
    0.85rem;

}


.empty-state button {

  border:
    none;

  border-radius:
    8px;

  padding:
    0.6rem 1rem;

  background:
    #2563eb;

  color:
    white;

  cursor:
    pointer;

}


/* =====================================================
   ANIMATION
===================================================== */

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


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 1400px) {

  .browse-page {

    padding:
      2rem;

  }

}


@media (max-width: 1100px) {

  .top-filters {

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

  }


  .browse-content {

    grid-template-columns:
      220px minmax(0, 1fr);

    gap:
      1.5rem;

  }


  .book-grid {

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

  }

}


@media (max-width: 800px) {

  .browse-content {

    grid-template-columns:
      1fr;

  }


  .filter-sidebar {

    width:
      100%;

    box-sizing:
      border-box;

  }


  .book-grid {

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

  }

}


@media (max-width: 600px) {

  .browse-page {

    padding:
      1rem;

  }


  .top-filters {

    grid-template-columns:
      1fr;

  }


  .book-grid {

    grid-template-columns:
      1fr;

  }


  .book-list-card {

    flex-direction:
      column;

    align-items:
      flex-start;

  }


  .list-action {

    width:
      100%;

  }


  .list-action button {

    width:
      100%;

  }

}

</style>