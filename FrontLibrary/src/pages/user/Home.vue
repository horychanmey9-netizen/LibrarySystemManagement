<template>
  <div class="min-h-screen bg-slate-50 p-0">

    <!-- =====================================================
         HERO BANNER
    ====================================================== -->
    <section class=" sm:px-6 lg:px-8">
  <div
    class="relative mx-auto max-w-7xl
           overflow-hidden rounded-2xl shadow-sm"
  >

    <!-- Banner Image -->
    <img
      src="/Banner.png"
      alt="Library Banner"
      class="absolute inset-0
             h-full w-full
             object-cover"
    />

    <!-- Overlay -->
    <div
      class="absolute inset-0
             bg-gradient-to-r
             from-cyan-800/90
             via-cyan-700/65
             to-cyan-700/20"
    ></div>

    <!-- Content -->
    <div
      class="relative z-10
             flex min-h-[250px]
             items-center
             px-5 py-8
             sm:min-h-[280px]
             sm:px-6 sm:py-9
             lg:min-h-[320px]"
    >

      <div class="w-full">

        <!-- Label -->
        <div
          class="mb-3 flex items-center gap-2
                 text-white
                 sm:mb-4"
        >

          <div
            class="flex h-8 w-8 shrink-0
                   items-center justify-center
                   rounded-lg bg-white/15"
          >

            <i class="bi bi-book text-lg"></i>

          </div>

          <span class="text-sm font-medium sm:text-sm">
            Welcome to our Library
          </span>

        </div>


        <!-- Title -->
        <h1
          class="max-w-xl
                 text-sm font-extrabold
                 leading-tight text-white
                 sm:text-sm
                 lg:text-5xl"
        >

          Discover Your Next

          <span class="block text-sky-200">
            Favorite Book
          </span>

        </h1>


        <!-- Description -->
        <p
          class="mt-3 max-w-lg
                 text- leading-5
                 text-white/85
                 sm:text-base sm:leading-6"
        >

          Explore our collection of books and find something interesting
          to read.

        </p>


        <!-- Favorite Button -->
        <button
          type="button"
          @click="$router.push('/user/myfavorite')"
          class="mt-4
                 inline-flex items-center gap-1
                 rounded-xl
                 bg-white
                 px-3 py-2.5
                 text-xs font-semibold
                 text-slate-700
                 shadow-sm
                 transition
                 hover:bg-slate-100
                 hover:shadow-md
                 sm:mt-5
                 sm:px-5 sm:py-3
                 sm:text-sm"
        >

          <i class="bi bi-heart text-pink-500"></i>

          <span>
            My Favorites
          </span>

        </button>

      </div>

    </div>

  </div>
</section>


    <!-- =====================================================
         SEARCH + CATEGORIES
    ====================================================== -->
    <section
      class="mx-auto max-w-7xl px-0 pt-5 sm:px-4 sm:pt-6"
    >
      <div
        class="flex flex-col gap-5 lg:flex-row lg:items-start lg:gap-8"
      >

        <!-- LEFT : SEARCH -->
        <div class="w-full lg:w-1/2">
          <div class="flex w-full">
            <div class="relative flex-1">

              <i
                class="bi bi-search absolute left-4 top-1/2 -translate-y-1/2 text-slate-400"
              ></i>

              <input
                v-model="searchInput"
                type="text"
                placeholder="Search by title, author, or ISBN..."
                class="h-11 w-full rounded-xl border border-slate-200 bg-white pl-11 pr-10 text-sm text-slate-700 shadow-sm outline-none transition placeholder:text-slate-400 focus:border-blue-400 focus:ring-2 focus:ring-blue-100"
              />

              <button
                v-if="searchInput"
                type="button"
                @click="clearSearch"
                class="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 transition hover:text-slate-600"
                aria-label="Clear search"
              >
                <i class="bi bi-x-circle-fill"></i>
              </button>

            </div>
          </div>
        </div>


        <!-- RIGHT : CATEGORIES -->
        <div class="w-full min-w-0 overflow-hidden lg:w-1/2">

          <!-- Loading -->
          <div
            v-if="loadingCategories"
            class="category-scroll"
          >
            <div
              v-for="n in 5"
              :key="n"
              class="h-10 w-24 shrink-0 animate-pulse rounded-xl bg-slate-200"
            ></div>
          </div>


          <!-- Categories -->
          <div
            v-else-if="categories.length"
            class="category-scroll"
          >

            <!-- All Books -->
            <button
              type="button"
              @click="clearCategory"
              class="category-box category-all"
              :class="{
                'category-active': !selectedCategory
              }"
            >
              <i class="bi bi-grid-fill"></i>

              <span>
                All Books
              </span>
            </button>


            <!-- Backend Categories -->
            <button
              v-for="(category, index) in categories"
              :key="category.id || category.name"
              type="button"
              @click="selectCategory(category)"
              class="category-box"
              :class="[
                getCategoryColor(index),
                {
                  'category-active':
                    selectedCategory === category.name
                }
              ]"
            >
              <i class="bi bi-book"></i>

              <span>
                {{ category.name }}
              </span>
            </button>

          </div>


          <!-- No Categories -->
          <div
            v-else
            class="py-4 text-sm text-slate-400"
          >
            No categories found.
          </div>

        </div>

      </div>
    </section>


    <!-- =====================================================
         BOOKS
    ====================================================== -->
    <section
      class="mx-auto max-w-7xl px-0 pb-8 pt-4 sm:px-4 sm:pt-5"
    >

      <!-- Books Header -->
      <div class="mb-4 flex items-center justify-between">

        <div>
          <h2 class="text-xl font-bold text-slate-800">
            {{ selectedCategory || "All Books" }}
          </h2>

          <p class="mt-1 text-sm text-slate-400">
            <span v-if="selectedCategory">
              Books in {{ selectedCategory }}
            </span>

            <span v-else>
              Discover books from our library collection
            </span>
          </p>
        </div>


        <!-- Book Count -->
        <div
          v-if="filteredBooks.length"
          class="hidden text-sm text-slate-400 sm:block"
        >
          {{ filteredBooks.length }} books
        </div>

      </div>


      <!-- Search Result -->
      <div
        v-if="searchInput || selectedCategory"
        class="mb-4 text-sm text-slate-500"
      >
        Found

        <span class="font-semibold text-slate-700">
          {{ filteredBooks.length }}
        </span>

        result<span v-if="filteredBooks.length !== 1">s</span>

        <span v-if="searchInput">
          for

          <span class="font-semibold text-slate-700">
            "{{ searchInput }}"
          </span>
        </span>

        <span v-if="selectedCategory">
          in

          <span class="font-semibold text-blue-600">
            {{ selectedCategory }}
          </span>
        </span>
      </div>


      <!-- Loading Books -->
      <div
        v-if="loadingBooks"
        class="grid grid-cols-2 gap-5 sm:grid-cols-2 sm:gap-5 lg:grid-cols-3 xl:grid-cols-4"
      >
        <div
          v-for="n in 8"
          :key="n"
          class="animate-pulse rounded-2xl border border-slate-200 bg-white p-3 sm:p-4"
        >
          <div
            class="mb-3 h-48 rounded-xl bg-slate-200 sm:mb-4 sm:h-56"
          ></div>

          <div
            class="mb-2 h-5 rounded bg-slate-200"
          ></div>

          <div
            class="h-4 w-2/3 rounded bg-slate-200"
          ></div>
        </div>
      </div>


      <!-- Book Grid -->
      <div
        v-else-if="visibleBooks.length"
        class="grid grid-cols-2 gap-5 sm:grid-cols-2 sm:gap-5 lg:grid-cols-3 xl:grid-cols-4"
      >
        <BookCard class="lg:px-3"
          v-for="book in visibleBooks"
          :key="book.id"
          :book="book"
          @bookmark="toggleBookmark"
          @view-details="viewBook"
        />
      </div>


      <!-- Empty Books -->
      <div
        v-else
        class="rounded-2xl border border-slate-200 bg-white p-8 text-center"
      >
        <div class="mb-4 text-5xl">
          📚
        </div>

        <h3 class="text-lg font-semibold text-slate-700">
          No books found
        </h3>

        <p class="mt-2 text-sm text-slate-400">
          {{
            searchInput
              ? "Try another title, author, or ISBN."
              : selectedCategory
                ? `There are no books in ${selectedCategory}.`
                : "There are no books available yet."
          }}
        </p>

        <button
          v-if="searchInput || selectedCategory"
          type="button"
          @click="clearAllFilters"
          class="mt-5 rounded-xl bg-blue-500 px-4 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-600"
        >
          Show All Books
        </button>
      </div>


      <!-- Load More -->
      <div
        v-if="
          !loadingBooks &&
          hasMoreBooks &&
          visibleBooks.length
        "
        class="mt-6 flex justify-center"
      >
        <button
          type="button"
          @click="loadMoreBooks"
          class="inline-flex items-center gap-2 rounded-xl border border-blue-200 bg-sky-300 px-12 py-3 text-sm font-semibold text-blue-600 shadow-sm transition hover:bg-blue-50"
        >

          <span>
            Load More
          </span>
        </button>
      </div>

    </section>


    <!-- =====================================================
         BOOK DETAILS
    ====================================================== -->
    <BookDetails
      v-if="selectedBook"
      :book="selectedBook"
      @close="closeBookDetails"
      @borrow="handleBorrow"
    />

  </div>
</template>


<script>
import BookCard from "../../components/books/BookCard.vue";
import BookDetails from "../../components/books/BookDetails.vue";

import { getCategories } from "../../service/categoryService.js";
import { getBooks } from "../../service/bookService.js";

import {
  getFavorites,
  addFavorite,
  deleteFavorite
} from "../../service/favoriteService.js";

export default {
  name: "Home",

  components: {
    BookCard,
    BookDetails
  },

  data() {
    return {
      categories: [],
      loadingCategories: true,

      allBooks: [],
      loadingBooks: true,

      searchInput: "",
      selectedCategory: "",

      displayLimit: 12,
      loadMoreAmount: 12,

      favoriteBookIds: [],

      selectedBook: null
    };
  },


  computed: {
    filteredBooks() {
      let books = this.allBooks;

      if (this.selectedCategory) {
        const categoryName =
          this.selectedCategory.toLowerCase();

        books = books.filter(book =>
          String(book.category || "")
            .toLowerCase() === categoryName
        );
      }

      if (this.searchInput.trim()) {
        const query =
          this.searchInput.trim().toLowerCase();

        books = books.filter(book => {
          const title =
            String(book.title || "").toLowerCase();

          const author =
            String(book.author || "").toLowerCase();

          const isbn =
            String(book.isbn || "").toLowerCase();

          return (
            title.includes(query) ||
            author.includes(query) ||
            isbn.includes(query)
          );
        });
      }

      return books;
    },


    visibleBooks() {
      return this.filteredBooks.slice(
        0,
        this.displayLimit
      );
    },


    hasMoreBooks() {
      return (
        this.displayLimit <
        this.filteredBooks.length
      );
    }
  },


  async created() {
    /*
     * Important:
     * Load favorites first.
     * After that, load books and set bookmarked correctly.
     */
    await this.fetchCategories();

    await this.fetchFavorites();

    await this.fetchBooks();

    this.updateCategoryBookCount();
  },


  methods: {

    // =====================================================
    // CATEGORY COLOR
    // =====================================================
    getCategoryColor(index) {
      const colors = [
        "category-blue",
        "category-purple",
        "category-cyan",
        "category-pink",
        "category-orange",
        "category-green"
      ];

      return colors[index % colors.length];
    },


    // =====================================================
    // FETCH CATEGORIES
    // =====================================================
    async fetchCategories() {
      try {
        const response = await getCategories();

        const data =
          Array.isArray(response?.data)
            ? response.data
            : Array.isArray(response)
              ? response
              : [];

        this.categories = data.map(category => ({
          ...category,
          bookCount: 0
        }));

      } catch (error) {
        console.error(
          "Failed to load categories:",
          error
        );

        this.categories = [];

      } finally {
        this.loadingCategories = false;
      }
    },


    // =====================================================
    // FETCH FAVORITES
    // =====================================================
    async fetchFavorites() {
      try {
        const response = await getFavorites();

        const data =
          Array.isArray(response?.data)
            ? response.data
            : Array.isArray(response)
              ? response
              : [];

        console.log("Favorite response:", data);

        this.favoriteBookIds = data
          .map(item => {

            /*
             * Response example:
             * {
             *   id: 1,
             *   bookId: 5
             * }
             */

            if (
              item.bookId !== undefined &&
              item.bookId !== null
            ) {
              return Number(item.bookId);
            }


            /*
             * Response example:
             * {
             *   id: 1,
             *   book: {
             *     id: 5
             *   }
             * }
             */

            if (
              item.book?.id !== undefined &&
              item.book?.id !== null
            ) {
              return Number(item.book.id);
            }


            /*
             * Another possible response:
             * {
             *   book: {
             *     bookId: 5
             *   }
             * }
             */

            if (
              item.book?.bookId !== undefined &&
              item.book?.bookId !== null
            ) {
              return Number(item.book.bookId);
            }

            return null;
          })
          .filter(id =>
            id !== null &&
            !Number.isNaN(id)
          );

        console.log(
          "Favorite Book IDs:",
          this.favoriteBookIds
        );

      } catch (error) {
        console.error(
          "Failed to load favorites:",
          error
        );

        this.favoriteBookIds = [];
      }
    },


    // =====================================================
    // FETCH BOOKS
    // =====================================================
    async fetchBooks() {
      try {
        const response = await getBooks();

        const data =
          Array.isArray(response?.data)
            ? response.data
            : Array.isArray(response)
              ? response
              : [];

        this.allBooks = data.map(book => ({
          id: book.id,

          title: book.title || "",

          author: book.author || "",

          category:
            book.category?.name ||
            book.categoryName ||
            book.category ||
            "Other",

          status:
            book.status === true ||
            book.status === "Available" ||
            book.status === "AVAILABLE",

          /*
           * This now works because fetchFavorites()
           * already finished before fetchBooks()
           */
          bookmarked: this.favoriteBookIds.includes(
            Number(book.id)
          ),

          qty: Number(book.qty || 0),

          pages: Number(book.pages || 0),

          isbn: book.isbn || "",

          language: book.language || "",

          description: book.description || "",

          image:
            book.image ||
            book.imageUrl ||
            book.coverImage ||
            null
        }));

      } catch (error) {
        console.error(
          "Failed to load books:",
          error
        );

        this.allBooks = [];

      } finally {
        this.loadingBooks = false;
      }
    },


    // =====================================================
    // UPDATE CATEGORY BOOK COUNT
    // =====================================================
    updateCategoryBookCount() {
      this.categories = this.categories.map(category => {
        const categoryName =
          String(category.name || "").toLowerCase();

        const bookCount =
          this.allBooks.filter(book =>
            String(book.category || "").toLowerCase() ===
            categoryName
          ).length;

        return {
          ...category,
          bookCount
        };
      });
    },


    // =====================================================
    // SELECT CATEGORY
    // =====================================================
    selectCategory(category) {
      this.selectedCategory =
        category.name || "";

      this.displayLimit = 12;

      this.$nextTick(() => {
        window.scrollTo({
          top: 430,
          behavior: "smooth"
        });
      });
    },


    // =====================================================
    // CLEAR CATEGORY
    // =====================================================
    clearCategory() {
      this.selectedCategory = "";
      this.displayLimit = 12;
    },


    // =====================================================
    // CLEAR SEARCH
    // =====================================================
    clearSearch() {
      this.searchInput = "";
      this.displayLimit = 12;
    },


    // =====================================================
    // CLEAR ALL FILTERS
    // =====================================================
    clearAllFilters() {
      this.selectedCategory = "";
      this.searchInput = "";
      this.displayLimit = 12;
    },


    // =====================================================
    // LOAD MORE BOOKS
    // =====================================================
    loadMoreBooks() {
      this.displayLimit += this.loadMoreAmount;
    },


    // =====================================================
    // TOGGLE FAVORITE
    // =====================================================
    async toggleBookmark(book) {
      const bookId = Number(book.id);

      try {

        if (book.bookmarked) {

          await deleteFavorite(bookId);

          book.bookmarked = false;

          this.favoriteBookIds =
            this.favoriteBookIds.filter(
              id => id !== bookId
            );

        } else {

          await addFavorite(bookId);

          book.bookmarked = true;

          if (
            !this.favoriteBookIds.includes(bookId)
          ) {
            this.favoriteBookIds.push(bookId);
          }
        }

      } catch (error) {
        console.error(
          "Favorite error:",
          error
        );

        alert(
          error.response?.data?.message ||
          error.message ||
          "Failed to update favorite"
        );
      }
    },


    // =====================================================
    // VIEW BOOK DETAILS
    // =====================================================
    viewBook(book) {
      this.selectedBook = {
        ...book
      };

      document.body.style.overflow = "hidden";
    },


    // =====================================================
    // CLOSE BOOK DETAILS
    // =====================================================
    closeBookDetails() {
      this.selectedBook = null;

      document.body.style.overflow = "";
    },


    // =====================================================
    // HANDLE BORROW
    // =====================================================
    handleBorrow(book) {
      console.log(
        "Borrow requested:",
        book
      );
    }

  },


  // =====================================================
  // BEFORE UNMOUNT
  // =====================================================
  beforeUnmount() {
    document.body.style.overflow = "";
  }
};
</script>


<style scoped>
button {
  -webkit-tap-highlight-color: transparent;
}


/* =====================================================
   CATEGORY HORIZONTAL SCROLL
===================================================== */
.category-scroll {
  display: flex;
  align-items: center;
  gap: 10px;

  width: 100%;
  max-width: 100%;
  min-width: 0;

  overflow-x: auto;
  overflow-y: hidden;

  white-space: nowrap;

  scrollbar-width: none;
  -ms-overflow-style: none;

  overscroll-behavior-x: contain;
  touch-action: pan-x;

  -webkit-overflow-scrolling: touch;
}

.category-scroll::-webkit-scrollbar {
  display: none;
}


/* =====================================================
   CATEGORY BOX
===================================================== */
.category-box {
  display: inline-flex;
  align-items: center;
  justify-content: center;

  flex: 0 0 auto;

  gap: 7px;

  min-height: 40px;

  padding: 0 15px;

  border-radius: 12px;

  font-size: 13px;
  font-weight: 600;

  white-space: nowrap;

  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;

  -webkit-tap-highlight-color: transparent;
}

.category-box i {
  font-size: 14px;
}

.category-box:active {
  transform: scale(0.97);
}


/* =====================================================
   COLORS
===================================================== */
.category-blue {
  background: #dbeafe;
  color: #2563eb;
}

.category-purple {
  background: #ede9fe;
  color: #7c3aed;
}

.category-cyan {
  background: #cffafe;
  color: #0891b2;
}

.category-pink {
  background: #fce7f3;
  color: #db2777;
}

.category-orange {
  background: #ffedd5;
  color: #ea580c;
}

.category-green {
  background: #dcfce7;
  color: #16a34a;
}


/* =====================================================
   ALL BOOKS
===================================================== */
.category-all {
  background: #e0f2fe;
  color: #0284c7;
}


/* =====================================================
   ACTIVE CATEGORY
===================================================== */
.category-active {
  background: #3b82f6 !important;
  color: white !important;

  box-shadow: 0 4px 10px rgba(59, 130, 246, 0.18);
}


/* =====================================================
   MOBILE
===================================================== */
@media (max-width: 639px) {
  .category-scroll {
    gap: 8px;

    padding: 2px 12px 4px;

    overflow-x: auto;
    overflow-y: hidden;

    touch-action: pan-x;
    overscroll-behavior-x: contain;
  }

  .category-box {
    min-height: 38px;

    padding: 0 13px;

    border-radius: 11px;

    font-size: 12px;
  }

  .category-box i {
    font-size: 13px;
  }

  .grid {
    min-width: 0;
  }
}


/* =====================================================
   PREVENT HORIZONTAL SCROLL
===================================================== */
:global(html),
:global(body) {
  overflow-x: hidden;
}
</style>