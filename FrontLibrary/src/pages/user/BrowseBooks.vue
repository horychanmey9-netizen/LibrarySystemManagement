<template>
  <div class="min-h-screen bg-slate-50 text-slate-900">

    <!-- ================= CONTENT ================= -->
    <section class="p-4 sm:p-6 lg:p-8">

      <!-- ================= TITLE ================= -->
      <div class="mb-7">
        <h2 class="text-3xl font-bold">
          Browse Books
        </h2>

        <p class="text-slate-400 mt-1">
          Discover and explore books from our library collection.
        </p>
      </div>


      <!-- ================= SEARCH & FILTERS ================= -->
      <div class="grid grid-cols-1 lg:grid-cols-4 gap-4 mb-7">

        <!-- Search -->
        <div class="relative">

          <span
            class="absolute left-4 top-1/2 -translate-y-1/2
                   text-slate-400 text-lg"
          >
            ⌕
          </span>

          <input
            v-model="search"
            type="text"
            placeholder="Search books by title, author, or keyword..."
            class="w-full bg-white border border-slate-200
                   rounded-xl pl-11 pr-4 py-3.5
                   outline-none
                   focus:ring-2 focus:ring-blue-500"
          />

        </div>


        <!-- Category -->
        <select
          v-model="category"
          class="bg-white border border-slate-200
                 rounded-xl px-4 py-3.5
                 outline-none
                 focus:ring-2 focus:ring-blue-500"
        >

          <option value="all">
            All Categories
          </option>

          <option
            v-for="item in categories"
            :key="item.name"
            :value="item.name"
          >
            {{ item.name }}
          </option>

        </select>


        <!-- Availability -->
        <select
          v-model="availability"
          class="bg-white border border-slate-200
                 rounded-xl px-4 py-3.5
                 outline-none
                 focus:ring-2 focus:ring-blue-500"
        >

          <option value="all">
            All Availability
          </option>

          <option value="Available">
            Available
          </option>

          <option value="Borrowed">
            Borrowed
          </option>

        </select>


        <!-- Sort -->
        <select
          v-model="sort"
          class="bg-white border border-slate-200
                 rounded-xl px-4 py-3.5
                 outline-none
                 focus:ring-2 focus:ring-blue-500"
        >

          <option value="default">
            Sort by
          </option>

          <option value="title">
            Title A-Z
          </option>

          <option value="author">
            Author A-Z
          </option>

        </select>

      </div>


      <!-- ================= BOOK AREA ================= -->
      <div class="flex gap-6">


        <!-- ================= FILTER SIDEBAR ================= -->
        <aside class="w-60 shrink-0 hidden xl:block">

          <div
            class="bg-white rounded-2xl
                   border border-slate-200
                   p-5 sticky top-28"
          >

            <!-- Filter Header -->
            <div
              class="flex items-center
                     justify-between mb-6"
            >

              <h3 class="font-bold">
                Filter
              </h3>

              <button
                @click="clearFilters"
                class="text-blue-600 text-sm
                       hover:text-blue-700
                       transition"
              >
                Clear all
              </button>

            </div>


            <!-- ================= CATEGORIES ================= -->
            <div
              class="pb-5
                     border-b border-slate-100"
            >

              <h4
                class="font-semibold
                       text-sm mb-4"
              >
                Categories
              </h4>

              <div class="space-y-3 text-sm">

                <label
                  v-for="item in categories"
                  :key="item.name"
                  class="flex items-center
                         justify-between
                         cursor-pointer"
                >

                  <span
                    class="flex gap-2
                           items-center"
                  >

                    <input
                      v-model="selectedCategories"
                      type="checkbox"
                      :value="item.name"
                      class="rounded
                             border-slate-300
                             text-blue-600
                             focus:ring-blue-500"
                    />

                    {{ item.name }}

                  </span>

                  <span
                    class="text-slate-400"
                  >
                    {{ item.count }}
                  </span>

                </label>

              </div>

            </div>


            <!-- ================= AVAILABILITY ================= -->
            <div
              class="py-5
                     border-b border-slate-100"
            >

              <h4
                class="font-semibold
                       text-sm mb-4"
              >
                Availability
              </h4>

              <div
                class="space-y-3
                       text-sm"
              >

                <!-- Available -->
                <label
                  class="flex items-center
                         gap-2 cursor-pointer"
                >

                  <input
                    type="checkbox"
                    v-model="availableOnly"
                    class="rounded
                           border-slate-300
                           text-blue-600
                           focus:ring-blue-500"
                  />

                  Available

                </label>


                <!-- Borrowed -->
                <label
                  class="flex items-center
                         gap-2 cursor-pointer"
                >

                  <input
                    type="checkbox"
                    v-model="borrowedOnly"
                    class="rounded
                           border-slate-300
                           text-blue-600
                           focus:ring-blue-500"
                  />

                  Borrowed

                </label>

              </div>

            </div>


            <!-- ================= LANGUAGE ================= -->
            <div class="py-5">

              <h4
                class="font-semibold
                       text-sm mb-4"
              >
                Language
              </h4>

              <div
                class="space-y-3
                       text-sm"
              >

                <!-- English -->
                <label
                  class="flex items-center
                         gap-2 cursor-pointer"
                >

                  <input
                    v-model="selectedLanguages"
                    type="checkbox"
                    value="English"
                    class="rounded
                           border-slate-300
                           text-blue-600
                           focus:ring-blue-500"
                  />

                  English

                </label>


                <!-- Khmer -->
                <label
                  class="flex items-center
                         gap-2 cursor-pointer"
                >

                  <input
                    v-model="selectedLanguages"
                    type="checkbox"
                    value="Khmer"
                    class="rounded
                           border-slate-300
                           text-blue-600
                           focus:ring-blue-500"
                  />

                  Khmer

                </label>


                <!-- Other -->
                <label
                  class="flex items-center
                         gap-2 cursor-pointer"
                >

                  <input
                    v-model="selectedLanguages"
                    type="checkbox"
                    value="Other"
                    class="rounded
                           border-slate-300
                           text-blue-600
                           focus:ring-blue-500"
                  />

                  Other

                </label>

              </div>

            </div>

          </div>

        </aside>


        <!-- ================= BOOKS ================= -->
        <div class="flex-1">


          <!-- Book Header -->
          <div
            class="flex
                   justify-between
                   items-center
                   mb-5"
          >

            <p
              class="text-slate-500
                     text-sm"
            >

              Showing

              <span
                class="font-semibold
                       text-slate-700"
              >
                {{ displayedBooks.length }}
              </span>

              of

              <span
                class="font-semibold
                       text-slate-700"
              >
                {{ filteredBooks.length }}
              </span>

              books

            </p>


            <!-- View Switch -->
            <div
              class="flex
                     bg-white
                     border border-slate-200
                     rounded-lg p-1"
            >

              <button
                class="px-3 py-1.5
                       bg-blue-50
                       text-blue-600
                       rounded-md"
              >
                ▦
              </button>

              <button
                class="px-3 py-1.5
                       text-slate-400
                       rounded-md
                       hover:bg-slate-50"
              >
                ☷
              </button>

            </div>

          </div>


          <!-- ================= NO BOOK ================= -->
          <div
            v-if="displayedBooks.length === 0"
            class="bg-white
                   border border-slate-200
                   rounded-2xl
                   p-10
                   text-center"
          >

            <div
              class="text-5xl
                     mb-4"
            >
              📚
            </div>

            <h3
              class="font-bold
                     text-lg"
            >
              No books found
            </h3>

            <p
              class="text-slate-400
                     text-sm mt-1"
            >
              Try changing your search or filters.
            </p>

            <button
              @click="clearFilters"
              class="mt-5
                     px-5 py-2.5
                     bg-blue-600
                     hover:bg-blue-700
                     text-white
                     rounded-xl
                     font-medium
                     transition"
            >
              Clear Filters
            </button>

          </div>


          <!-- ================= BOOK GRID ================= -->
          <div
            v-else
            class="grid
                   grid-cols-1
                   md:grid-cols-2
                   xl:grid-cols-3
                   gap-5"
          >

            <BookCard
  v-for="book in displayedBooks"
  :key="book.id"
  :book="book"
  @bookmark="toggleBookmark"
  @view-details="viewBook"
/>

          </div>


          <!-- ================= LOADING ================= -->
          <div
            v-if="loading"
            class="text-center
                   py-10
                   text-slate-400"
          >

            <div
              class="inline-block
                     w-6 h-6
                     border-2
                     border-slate-300
                     border-t-blue-600
                     rounded-full
                     animate-spin"
            ></div>

            <p class="mt-3">
              Loading more books...
            </p>

          </div>


          <!-- ================= END ================= -->
          <div
            v-if="
              !loading &&
              displayedBooks.length > 0 &&
              displayedBooks.length >= filteredBooks.length
            "
            class="text-center
                   py-10
                   text-slate-400
                   text-sm"
          >

            You've reached the end
            of the collection.

          </div>

        </div>

      </div>


      <!-- ================= BOOK DETAIL ================= -->
      <BookDetails
  v-if="selectedBook"
  :book="selectedBook"
  @close="closeBookDetails"
/>

    </section>

  </div>
</template>


<script>

import BookCard from "../../components/books/BookCard.vue";

import BookDetails from "../../components/books/BookDetails.vue";

export default {

  name: "BrowseBooks",


  components: {

    BookCard,

    BookDetails

  },


  data() {

    return {

      // =================================================
      // SEARCH
      // =================================================

      search: "",


      // =================================================
      // FILTER
      // =================================================

      category: "all",

      availability: "all",

      sort: "default",


      selectedCategories: [],

      selectedLanguages: [],


      availableOnly: false,

      borrowedOnly: false,


      // =================================================
      // BOOK DETAIL
      // =================================================

      selectedBook: null,


      // =================================================
      // LOADING
      // =================================================

      loading: false,

      displayLimit: 8,


      // =================================================
      // CATEGORIES
      // =================================================

      categories: [

        {
          name: "Fiction",
          count: 320
        },

        {
          name: "Science",
          count: 180
        },

        {
          name: "Technology",
          count: 150
        },

        {
          name: "History",
          count: 120
        },

        {
          name: "Self Help",
          count: 95
        },

        {
          name: "Psychology",
          count: 80
        }

      ],


      // =================================================
      // BOOKS
      // =================================================

      books: [

        {
          id: 1,

          title: "The Great Gatsby",

          qty: 5,

          description:
            "The Great Gatsby is a classic American novel that explores themes of wealth, love, ambition, and the American Dream during the Jazz Age.",

          author: "F. Scott Fitzgerald",

          pages: 180,

          isbn: "9780743273565",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780743273565-L.jpg",

          status: true,

          category: "Fiction",

          bookmarked: false
        },


        {
          id: 2,

          title: "A Brief History of Time",

          qty: 4,

          description:
            "A Brief History of Time explores the origins and structure of the universe, including space, time, black holes, and the nature of reality.",

          author: "Stephen Hawking",

          pages: 256,

          isbn: "9780553380163",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780553380163-L.jpg",

          status: true,

          category: "Science",

          bookmarked: false
        },


        {
          id: 3,

          title: "Clean Code",

          qty: 3,

          description:
            "Clean Code provides practical principles and techniques for writing readable, maintainable, and professional software.",

          author: "Robert C. Martin",

          pages: 464,

          isbn: "9780132350884",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780132350884-L.jpg",

          status: false,

          category: "Technology",

          bookmarked: false
        },


        {
          id: 4,

          title: "Sapiens",

          qty: 6,

          description:
            "Sapiens examines the history of humankind, from early human societies to modern civilization and the forces that shaped our world.",

          author: "Yuval Noah Harari",

          pages: 443,

          isbn: "9780062316097",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780062316097-L.jpg",

          status: true,

          category: "History",

          bookmarked: false
        },


        {
          id: 5,

          title: "Atomic Habits",

          qty: 8,

          description:
            "Atomic Habits explains how small, consistent changes can create powerful habits and lead to meaningful long-term improvement.",

          author: "James Clear",

          pages: 320,

          isbn: "9780735211292",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780735211292-L.jpg",

          status: true,

          category: "Self Help",

          bookmarked: false
        },


        {
          id: 6,

          title: "The Alchemist",

          qty: 7,

          description:
            "The Alchemist follows a young shepherd on a journey to discover his personal legend and pursue his dreams.",

          author: "Paulo Coelho",

          pages: 208,

          isbn: "9780062315007",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780062315007-L.jpg",

          status: true,

          category: "Fiction",

          bookmarked: false
        },


        {
          id: 7,

          title: "Thinking, Fast and Slow",

          qty: 4,

          description:
            "Thinking, Fast and Slow explores two different systems of human thought and explains how they influence judgment and decision-making.",

          author: "Daniel Kahneman",

          pages: 499,

          isbn: "9780374533557",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780374533557-L.jpg",

          status: true,

          category: "Psychology",

          bookmarked: false
        },


        {
          id: 8,

          title: "1984",

          qty: 2,

          description:
            "1984 is a dystopian novel about surveillance, authoritarianism, freedom, and the struggle of an individual against an oppressive society.",

          author: "George Orwell",

          pages: 328,

          isbn: "9780451524935",

          language: "English",

          image:
            "https://covers.openlibrary.org/b/isbn/9780451524935-L.jpg",

          status: false,

          category: "Fiction",

          bookmarked: false
        }

      ]

    };

  },


  // =================================================
  // COMPUTED
  // =================================================

  computed: {

    filteredBooks() {

      let result = this.books.filter(book => {

        // =============================================
        // SEARCH
        // =============================================

        const searchText =
          this.search
            .toLowerCase()
            .trim();


        const matchSearch =

          !searchText ||

          book.title
            .toLowerCase()
            .includes(searchText) ||

          book.author
            .toLowerCase()
            .includes(searchText) ||

          book.category
            .toLowerCase()
            .includes(searchText) ||

          book.isbn
            .toLowerCase()
            .includes(searchText);


        // =============================================
        // CATEGORY
        // =============================================

        const matchCategory =

          this.category === "all" ||

          book.category === this.category;


        // =============================================
        // AVAILABILITY
        // =============================================

        const bookAvailability =
          book.status
            ? "Available"
            : "Borrowed";


        const matchAvailability =

          this.availability === "all" ||

          bookAvailability === this.availability;


        // =============================================
        // SIDEBAR CATEGORY
        // =============================================

        const matchCheckboxCategory =

          this.selectedCategories.length === 0 ||

          this.selectedCategories.includes(
            book.category
          );


        // =============================================
        // AVAILABILITY CHECKBOX
        // =============================================

        const matchAvailable =

          !this.availableOnly ||

          book.status === true;


        // =============================================
        // BORROWED CHECKBOX
        // =============================================

        const matchBorrowed =

          !this.borrowedOnly ||

          book.status === false;


        // =============================================
        // LANGUAGE
        // =============================================

        const matchLanguage =

          this.selectedLanguages.length === 0 ||

          this.selectedLanguages.includes(
            book.language
          );


        return (

          matchSearch &&

          matchCategory &&

          matchAvailability &&

          matchCheckboxCategory &&

          matchAvailable &&

          matchBorrowed &&

          matchLanguage

        );

      });


      // =================================================
      // SORT TITLE
      // =================================================

      if (this.sort === "title") {

        result.sort((a, b) =>
          a.title.localeCompare(
            b.title
          )
        );

      }


      // =================================================
      // SORT AUTHOR
      // =================================================

      if (this.sort === "author") {

        result.sort((a, b) =>
          a.author.localeCompare(
            b.author
          )
        );

      }


      return result;

    },


    // =================================================
    // DISPLAYED BOOKS
    // =================================================

    displayedBooks() {

      return this.filteredBooks.slice(
        0,
        this.displayLimit
      );

    }

  },


  // =================================================
  // WATCH
  // =================================================

  watch: {

    search() {
      this.resetScroll();
    },

    category() {
      this.resetScroll();
    },

    availability() {
      this.resetScroll();
    },

    sort() {
      this.resetScroll();
    },

    selectedCategories() {
      this.resetScroll();
    },

    selectedLanguages() {
      this.resetScroll();
    },

    availableOnly() {
      this.resetScroll();
    },

    borrowedOnly() {
      this.resetScroll();
    }

  },


  // =================================================
  // LIFECYCLE
  // =================================================

  mounted() {

    window.addEventListener(
      "scroll",
      this.handleScroll
    );

  },


  beforeUnmount() {

    window.removeEventListener(
      "scroll",
      this.handleScroll
    );

    document.body.style.overflow = "";

  },


  // =================================================
  // METHODS
  // =================================================

  methods: {


    // =================================================
    // INFINITE SCROLL
    // =================================================

    handleScroll() {

      const scrollBottom =

        window.innerHeight +

        window.scrollY >=

        document.documentElement
          .scrollHeight - 500;


      if (

        scrollBottom &&

        !this.loading &&

        this.displayLimit <
        this.filteredBooks.length

      ) {

        this.loadMore();

      }

    },


    // =================================================
    // LOAD MORE
    // =================================================

    loadMore() {

      this.loading = true;


      setTimeout(() => {

        this.displayLimit += 8;

        this.loading = false;

      }, 500);

    },


    // =================================================
    // RESET PAGINATION
    // =================================================

    resetScroll() {

      this.displayLimit = 8;

    },


    // =================================================
    // CLEAR FILTERS
    // =================================================

    clearFilters() {

      this.search = "";

      this.category = "all";

      this.availability = "all";

      this.sort = "default";

      this.selectedCategories = [];

      this.selectedLanguages = [];

      this.availableOnly = false;

      this.borrowedOnly = false;

      this.displayLimit = 8;

    },


    // =================================================
    // BOOKMARK
    // =================================================

    toggleBookmark(book) {

      book.bookmarked =
        !book.bookmarked;

    },


    // =================================================
    // VIEW BOOK DETAILS
    // =================================================

    viewBook(book) {
  console.log("CLICKED:", book);

  this.selectedBook = book;
  document.body.style.overflow = "hidden";
},

closeBookDetails() {
  this.selectedBook = null;
  document.body.style.overflow = "";
}
  }

};
</script>