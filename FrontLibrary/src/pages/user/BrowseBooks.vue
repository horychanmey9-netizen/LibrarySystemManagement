<template>
  <div
    class="min-h-screen w-full bg-slate-50 p-4 sm:p-6 lg:p-8"
  >

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="mb-8">

      <h1
        class="m-0 text-2xl font-bold tracking-tight text-slate-900"
      >
        Browse Books
      </h1>

      <p
        class="mt-1.5 text-base text-slate-400"
      >
        Discover and explore books from our library collection.
      </p>

    </div>


    <!-- =====================================================
         TOP FILTER BAR
    ====================================================== -->

    <div
      class="mb-8 grid grid-cols-1 gap-4
             lg:grid-cols-2
             xl:grid-cols-4"
    >

      <!-- SEARCH -->

      <div
        class="flex h-[58px] items-center gap-3
               rounded-[14px] border border-slate-200
               bg-white px-4"
      >

        <i
          class="bi bi-search text-base text-slate-400"
        ></i>

        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search books by title, author, ISBN..."
          class="h-full w-full border-none bg-transparent
                 text-[15px] text-slate-700
                 outline-none placeholder:text-slate-400"
        />

      </div>


      <!-- CATEGORY DROPDOWN -->

      <div class="relative h-[58px]">

        <select
          v-model="selectedCategory"
          class="h-full w-full cursor-pointer
                 appearance-none rounded-[14px]
                 border border-slate-200 bg-white
                 px-4 pr-11 text-[15px] text-slate-800
                 outline-none transition
                 focus:border-blue-300
                 focus:ring-4 focus:ring-blue-500/10"
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

        <i
          class="bi bi-chevron-down pointer-events-none
                 absolute right-4 top-1/2
                 -translate-y-1/2 text-slate-900"
        ></i>

      </div>


      <!-- AVAILABILITY -->

      <div class="relative h-[58px]">

        <select
          v-model="selectedAvailability"
          class="h-full w-full cursor-pointer
                 appearance-none rounded-[14px]
                 border border-slate-200 bg-white
                 px-4 pr-11 text-[15px] text-slate-800
                 outline-none transition
                 focus:border-blue-300
                 focus:ring-4 focus:ring-blue-500/10"
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

        <i
          class="bi bi-chevron-down pointer-events-none
                 absolute right-4 top-1/2
                 -translate-y-1/2 text-slate-900"
        ></i>

      </div>


      <!-- SORT -->

      <div class="relative h-[58px]">

        <select
          v-model="selectedSort"
          class="h-full w-full cursor-pointer
                 appearance-none rounded-[14px]
                 border border-slate-200 bg-white
                 px-4 pr-11 text-[15px] text-slate-800
                 outline-none transition
                 focus:border-blue-300
                 focus:ring-4 focus:ring-blue-500/10"
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

        <i
          class="bi bi-chevron-down pointer-events-none
                 absolute right-4 top-1/2
                 -translate-y-1/2 text-slate-900"
        ></i>

      </div>

    </div>


    <!-- =====================================================
         MAIN CONTENT
    ====================================================== -->

    <div
      class="grid grid-cols-1 items-start gap-6
             lg:grid-cols-[220px_minmax(0,1fr)]
             xl:grid-cols-[260px_minmax(0,1fr)]
             xl:gap-10"
    >


      <!-- ===================================================
           SIDEBAR
      ==================================================== -->

      <aside
        class="w-full rounded-[14px]
               border border-slate-200 bg-white p-5"
      >

        <!-- FILTER HEADER -->

        <div
          class="mb-6 flex items-center justify-between"
        >

          <h2
            class="m-0 text-base font-bold text-slate-900"
          >
            Filter
          </h2>

          <button
            type="button"
            @click="clearFilters"
            class="border-none bg-transparent
                   text-sm text-blue-600
                   transition hover:underline"
          >
            Clear all
          </button>

        </div>


        <!-- =================================================
             CATEGORIES
        ================================================== -->

        <div
          class="mb-5 border-b border-slate-100
                 pb-5 last:mb-0 last:border-b-0"
        >

          <h3
            class="mb-3.5 text-sm font-semibold text-slate-900"
          >
            Categories
          </h3>


          <!-- LOADING -->

          <div
            v-if="loadingCategories"
            class="py-1 text-xs text-slate-400"
          >
            Loading categories...
          </div>


          <!-- ERROR -->

          <div
            v-else-if="categoryError"
            class="text-xs leading-relaxed text-red-600"
          >
            {{ categoryError }}
          </div>


          <!-- CATEGORY LIST -->

          <div
            v-else
            class="flex flex-col gap-3"
          >

            <!-- BACKEND CATEGORIES -->

            <label
              v-for="category in categories"
              :key="category.id"
              class="group relative flex min-h-5
                     cursor-pointer items-center gap-2.5"
            >

              <input
                type="checkbox"
                :value="category.name"
                v-model="selectedCategories"
                @change="syncCategoryFromCheckbox"
                class="peer absolute h-0 w-0 opacity-0"
              />

              <span
                class="h-[15px] w-[15px] shrink-0
                       rounded-[3px]
                       border-[1.5px] border-slate-400
                       bg-white transition
                       peer-checked:border-blue-600
                       peer-checked:bg-blue-600
                       peer-checked:shadow-[inset_0_0_0_3px_white]"
              ></span>

              <span
                class="flex-1 text-sm text-slate-700"
              >
                {{ category.name }}
              </span>

              <span
                class="text-xs text-slate-400"
              >
                {{ getCategoryCount(category.name) }}
              </span>

            </label>


            <!-- OTHER -->

            <label
              class="group relative flex min-h-5
                     cursor-pointer items-center gap-2.5"
            >

              <input
                type="checkbox"
                value="Other"
                v-model="selectedCategories"
                @change="syncCategoryFromCheckbox"
                class="peer absolute h-0 w-0 opacity-0"
              />

              <span
                class="h-[15px] w-[15px] shrink-0
                       rounded-[3px]
                       border-[1.5px] border-slate-400
                       bg-white transition
                       peer-checked:border-blue-600
                       peer-checked:bg-blue-600
                       peer-checked:shadow-[inset_0_0_0_3px_white]"
              ></span>

              <span
                class="flex-1 text-sm text-slate-700"
              >
                Other
              </span>

              <span
                class="text-xs text-slate-400"
              >
                {{ getOtherCount() }}
              </span>

            </label>

          </div>

        </div>


        <!-- =================================================
             AVAILABILITY
        ================================================== -->

        <div
          class="mb-5 border-b border-slate-100
                 pb-5"
        >

          <h3
            class="mb-3.5 text-sm font-semibold text-slate-900"
          >
            Availability
          </h3>


          <div
            class="flex flex-col gap-3"
          >

            <!-- AVAILABLE -->

            <label
              class="relative flex min-h-5
                     cursor-pointer items-center gap-2.5"
            >

              <input
                type="checkbox"
                value="Available"
                v-model="selectedAvailabilityFilters"
                @change="syncAvailability"
                class="peer absolute h-0 w-0 opacity-0"
              />

              <span
                class="h-[15px] w-[15px] shrink-0
                       rounded-[3px]
                       border-[1.5px] border-slate-400
                       bg-white transition
                       peer-checked:border-blue-600
                       peer-checked:bg-blue-600
                       peer-checked:shadow-[inset_0_0_0_3px_white]"
              ></span>

              <span
                class="flex-1 text-sm text-slate-700"
              >
                Available
              </span>

              <span
                class="text-xs text-slate-400"
              >
                {{ availableCount }}
              </span>

            </label>


            <!-- BORROWED -->

            <label
              class="relative flex min-h-5
                     cursor-pointer items-center gap-2.5"
            >

              <input
                type="checkbox"
                value="Borrowed"
                v-model="selectedAvailabilityFilters"
                @change="syncAvailability"
                class="peer absolute h-0 w-0 opacity-0"
              />

              <span
                class="h-[15px] w-[15px] shrink-0
                       rounded-[3px]
                       border-[1.5px] border-slate-400
                       bg-white transition
                       peer-checked:border-blue-600
                       peer-checked:bg-blue-600
                       peer-checked:shadow-[inset_0_0_0_3px_white]"
              ></span>

              <span
                class="flex-1 text-sm text-slate-700"
              >
                Borrowed
              </span>

              <span
                class="text-xs text-slate-400"
              >
                {{ borrowedCount }}
              </span>

            </label>

          </div>

        </div>


        <!-- =================================================
             LANGUAGE
        ================================================== -->

        <div
          class="pb-0"
        >

          <h3
            class="mb-3.5 text-sm font-semibold text-slate-900"
          >
            Language
          </h3>


          <div
            class="flex flex-col gap-3"
          >

            <!-- ENGLISH -->

            <label
              class="relative flex min-h-5
                     cursor-pointer items-center gap-2.5"
            >

              <input
                type="checkbox"
                value="English"
                v-model="selectedLanguages"
                class="peer absolute h-0 w-0 opacity-0"
              />

              <span
                class="h-[15px] w-[15px] shrink-0
                       rounded-[3px]
                       border-[1.5px] border-slate-400
                       bg-white transition
                       peer-checked:border-blue-600
                       peer-checked:bg-blue-600
                       peer-checked:shadow-[inset_0_0_0_3px_white]"
              ></span>

              <span
                class="flex-1 text-sm text-slate-700"
              >
                English
              </span>

              <span
                class="text-xs text-slate-400"
              >
                {{ getLanguageCount("English") }}
              </span>

            </label>


            <!-- KHMER -->

            <label
              class="relative flex min-h-5
                     cursor-pointer items-center gap-2.5"
            >

              <input
                type="checkbox"
                value="Khmer"
                v-model="selectedLanguages"
                class="peer absolute h-0 w-0 opacity-0"
              />

              <span
                class="h-[15px] w-[15px] shrink-0
                       rounded-[3px]
                       border-[1.5px] border-slate-400
                       bg-white transition
                       peer-checked:border-blue-600
                       peer-checked:bg-blue-600
                       peer-checked:shadow-[inset_0_0_0_3px_white]"
              ></span>

              <span
                class="flex-1 text-sm text-slate-700"
              >
                Khmer
              </span>

              <span
                class="text-xs text-slate-400"
              >
                {{ getLanguageCount("Khmer") }}
              </span>

            </label>

          </div>

        </div>

      </aside>


      <!-- ===================================================
           BOOK CONTENT
      ==================================================== -->

      <main class="min-w-0">

        <!-- =================================================
             BOOK TOOLBAR
        ================================================== -->

        <div
          class="mb-5 flex items-center
                 justify-between gap-4"
        >

          <p
            class="m-0 text-sm text-slate-400"
          >

            Showing

            <strong
              class="font-semibold text-slate-600"
            >
              {{ filteredBooks.length }}
            </strong>

            of

            <strong
              class="font-semibold text-slate-600"
            >
              {{ books.length }}
            </strong>

            books

          </p>


          <!-- VIEW TOGGLE -->

          <div
            class="flex items-center gap-1
                   rounded-[10px]
                   border border-slate-200
                   bg-white p-1"
          >

            <button
              type="button"
              :class="[
                'flex h-[38px] w-[42px] items-center justify-center rounded-lg border-none text-base transition',
                viewMode === 'grid'
                  ? 'bg-blue-50 text-blue-600'
                  : 'bg-transparent text-slate-400 hover:bg-slate-50'
              ]"
              @click="viewMode = 'grid'"
              aria-label="Grid view"
            >

              <i class="bi bi-grid-3x3-gap"></i>

            </button>


            <button
              type="button"
              :class="[
                'flex h-[38px] w-[42px] items-center justify-center rounded-lg border-none text-base transition',
                viewMode === 'list'
                  ? 'bg-blue-50 text-blue-600'
                  : 'bg-transparent text-slate-400 hover:bg-slate-50'
              ]"
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
          class="flex min-h-[300px]
                 flex-col items-center
                 justify-center
                 rounded-[14px]
                 border border-slate-200
                 bg-white text-slate-500"
        >

          <i
            class="bi bi-arrow-repeat
                   mb-3 text-3xl
                   animate-spin text-blue-600"
          ></i>

          <p
            class="m-0 text-sm"
          >
            Loading books...
          </p>

        </div>


        <!-- =================================================
             BOOK ERROR
        ================================================== -->

        <div
          v-else-if="bookError"
          class="flex min-h-[300px]
                 flex-col items-center
                 justify-center
                 rounded-[14px]
                 border border-red-200
                 bg-white
                 text-center text-red-600"
        >

          <i
            class="bi bi-exclamation-circle
                   mb-3 text-3xl"
          ></i>

          <p
            class="mb-4 text-sm"
          >
            {{ bookError }}
          </p>

          <button
            type="button"
            @click="fetchBooks"
            class="rounded-lg border-none
                   bg-blue-600 px-4 py-2
                   text-sm font-medium text-white
                   transition hover:bg-blue-700"
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
          class="grid grid-cols-1 gap-5
                 sm:grid-cols-2
                 xl:grid-cols-3"
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
          class="flex flex-col gap-4"
        >

          <div
            v-for="book in filteredBooks"
            :key="book.id"
            class="flex items-center gap-5
                   rounded-[14px]
                   border border-slate-200
                   bg-white p-4
                   transition hover:shadow-sm
                   max-sm:flex-col
                   max-sm:items-start"
          >

            <!-- IMAGE -->

            <div
              class="flex h-[130px] w-[100px]
                     shrink-0 items-center
                     justify-center overflow-hidden
                     rounded-[10px]
                     bg-slate-100"
            >

              <img
                v-if="book.image"
                :src="book.image"
                :alt="book.title"
                @error="handleImageError"
                class="h-full w-full object-contain"
              />

              <div
                v-else
                class="text-4xl"
              >
                📚
              </div>

            </div>


            <!-- INFORMATION -->

            <div class="min-w-0 flex-1">

              <h3
                class="m-0 text-base font-bold
                       text-slate-900"
              >
                {{ book.title }}
              </h3>

              <p
                class="my-1.5 text-sm text-slate-400"
              >
                {{ book.author }}
              </p>


              <div
                class="flex flex-wrap items-center gap-2"
              >

                <span
                  class="inline-block rounded-full
                         bg-blue-50 px-2.5 py-1
                         text-xs text-blue-600"
                >
                  {{ book.category }}
                </span>


                <span
                  v-if="book.status === true"
                  class="inline-block rounded-full
                         bg-emerald-50 px-2.5 py-1
                         text-xs text-emerald-600"
                >
                  ✓ Available
                </span>


                <span
                  v-else
                  class="inline-block rounded-full
                         bg-red-50 px-2.5 py-1
                         text-xs text-red-500"
                >
                  × Borrowed
                </span>

              </div>

            </div>


            <!-- ACTION -->

            <div
              class="shrink-0 max-sm:w-full"
            >

              <button
                type="button"
                @click="viewBook(book)"
                class="rounded-[9px]
                       border border-blue-600
                       bg-white px-4 py-2.5
                       text-sm font-medium
                       text-blue-600
                       transition
                       hover:bg-blue-600
                       hover:text-white
                       max-sm:w-full"
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
          class="rounded-[14px]
                 border border-slate-200
                 bg-white px-4 py-20
                 text-center"
        >

          <div
            class="mb-3 text-5xl"
          >
            📚
          </div>

          <h3
            class="m-0 text-lg font-semibold
                   text-slate-700"
          >
            No books found
          </h3>

          <p
            class="mb-4 mt-1.5 text-sm text-slate-400"
          >
            Try changing your search or filters.
          </p>

          <button
            type="button"
            @click="clearFilters"
            class="rounded-lg border-none
                   bg-blue-600 px-4 py-2
                   text-sm font-medium text-white
                   transition hover:bg-blue-700"
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


        if (
          result &&
          Array.isArray(result.data)
        ) {

          bookData =
            result.data;

        }


        else if (
          Array.isArray(result)
        ) {

          bookData =
            result;

        }


        else {

          bookData = [];

        }


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


        if (
          result &&
          Array.isArray(result.data)
        ) {

          this.categories =
            result.data;

        }


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