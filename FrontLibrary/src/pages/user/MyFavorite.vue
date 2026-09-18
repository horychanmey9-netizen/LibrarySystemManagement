
<template>

  <div class="min-h-screen bg-slate-50 p-6">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="mb-6">

      <h1 class="text-2xl font-bold text-slate-800">
        My Favorites
      </h1>

      <p class="mt-1 text-slate-500">
        Books you have saved to your favorite list
      </p>

    </div>


    <!-- =====================================================
         LOADING
    ====================================================== -->

    <div
      v-if="loading"
      class="flex items-center justify-center py-20"
    >

      <p class="text-slate-500">
        Loading favorites...
      </p>

    </div>


    <!-- =====================================================
         ERROR
    ====================================================== -->

    <div
      v-else-if="error"
      class="bg-red-50
             border border-red-200
             rounded-xl
             p-5
             text-red-600"
    >

      {{ error }}

    </div>


    <!-- =====================================================
         EMPTY
    ====================================================== -->

    <div
      v-else-if="favorites.length === 0"
      class="bg-white
             border border-slate-200
             rounded-2xl
             p-10
             text-center"
    >

      <div class="text-5xl mb-4">
        ♡
      </div>

      <h2 class="text-xl font-semibold text-slate-700">
        No Favorite Books
      </h2>

      <p class="text-slate-400 mt-2">
        You haven't added any books to your favorites yet.
      </p>

      <button
        type="button"
        @click="goToBrowseBooks"
        class="mt-6
               px-5 py-2.5
               rounded-xl
               bg-blue-600
               hover:bg-blue-700
               text-white
               font-medium
               transition"
      >
        Browse Books
      </button>

    </div>


    <!-- =====================================================
         FAVORITE GRID
    ====================================================== -->

    <div
      v-else
      class="grid
             grid-cols-1
             sm:grid-cols-2
             lg:grid-cols-3
             xl:grid-cols-4
             gap-6"
    >

      <div
        v-for="favorite in favorites"
        :key="favorite.id"
        class="bg-white
               border border-slate-200
               rounded-2xl
               p-4
               hover:-translate-y-1
               hover:shadow-lg
               transition duration-300"
      >

        <!-- =================================================
             FAVORITE BUTTON
        ================================================== -->

        <div class="flex justify-end mb-2">

          <button
            type="button"
            @click="removeFavorite(favorite.bookId)"
            class="w-9 h-9
                   border border-slate-200
                   rounded-lg
                   text-blue-600
                   bg-blue-50
                   hover:bg-red-50
                   hover:text-red-500
                   transition"
            title="Remove from favorite"
          >
            ♥
          </button>

        </div>


        <!-- =================================================
             BOOK IMAGE
        ================================================== -->

        <div
          class="h-56
                 bg-slate-100
                 rounded-xl
                 flex items-center
                 justify-center
                 overflow-hidden
                 mb-4"
        >

          <img
            v-if="favorite.image"
            :src="favorite.image"
            :alt="favorite.title"
            class="h-full w-full object-contain"
          />

          <div
            v-else
            class="text-slate-400 text-5xl"
          >
            📚
          </div>

        </div>


        <!-- =================================================
             TITLE
        ================================================== -->

        <h3
          class="font-bold
                 text-lg
                 leading-tight
                 min-h-[48px]"
        >
          {{ favorite.title }}
        </h3>


        <!-- =================================================
             AUTHOR
        ================================================== -->

        <p class="text-sm text-slate-400 mt-1">
          {{ favorite.author || "Unknown Author" }}
        </p>


        <!-- =================================================
             FAVORITE BADGE
        ================================================== -->

        <span
          class="inline-block
                 mt-3
                 px-3 py-1
                 rounded-full
                 bg-blue-50
                 text-blue-600
                 text-xs
                 font-medium"
        >
          ♥ Favorite
        </span>


        <!-- =================================================
             VIEW DETAILS
        ================================================== -->

        <button
          type="button"
          @click="viewDetails(favorite)"
          :disabled="detailLoadingId === favorite.bookId"
          class="w-full
                 mt-4
                 border-2
                 border-blue-500
                 text-blue-600
                 hover:bg-blue-600
                 hover:text-white
                 disabled:opacity-50
                 disabled:cursor-not-allowed
                 py-2.5
                 rounded-xl
                 font-medium
                 transition"
        >

          {{
            detailLoadingId === favorite.bookId
              ? "Loading..."
              : "View Details"
          }}

        </button>

      </div>

    </div>


    <!-- =====================================================
         BOOK DETAILS
    ====================================================== -->

    <BookDetails
      v-if="selectedBook"
      :book="selectedBook"
      @close="selectedBook = null"
      @borrow="handleBorrow"
    />

  </div>

</template>


<script>

import {
  getFavorites,
  deleteFavorite
} from "../../service/favoriteService.js";

import {
  getBooks
} from "../../service/bookService.js";

import BookDetails
  from "../../components/books/BookDetails.vue";


export default {

  name: "MyFavorite",


  components: {

    BookDetails

  },


  data() {

    return {

      favorites: [],

      loading: false,

      error: null,

      selectedBook: null,

      detailLoadingId: null

    };

  },


  mounted() {

    this.fetchFavorites();

  },


  methods: {

    // =====================================================
    // GET FAVORITES
    // =====================================================

    async fetchFavorites() {

      this.loading = true;

      this.error = null;

      try {

        const response =
          await getFavorites();

        if (Array.isArray(response?.data)) {

          this.favorites =
            response.data;

        } else if (Array.isArray(response)) {

          this.favorites =
            response;

        } else {

          this.favorites = [];

        }

      } catch (error) {

        console.error(
          "Failed to load favorites:",
          error
        );

        this.error =
          error.message ||
          "Failed to load favorite books.";

      } finally {

        this.loading = false;

      }

    },


    // =====================================================
    // REMOVE FAVORITE
    // =====================================================

    async removeFavorite(bookId) {

      try {

        await deleteFavorite(bookId);

        this.favorites =
          this.favorites.filter(
            favorite =>
              Number(favorite.bookId) !==
              Number(bookId)
          );

      } catch (error) {

        console.error(
          "Failed to remove favorite:",
          error
        );

        alert(
          error.message ||
          "Failed to remove favorite."
        );

      }

    },


    // =====================================================
    // VIEW BOOK DETAILS
    // =====================================================

    async viewDetails(favorite) {

      if (!favorite?.bookId) {

        return;

      }

      this.detailLoadingId =
        favorite.bookId;

      try {

        const response =
          await getBooks();

        const books =
          Array.isArray(response?.data)
            ? response.data
            : Array.isArray(response)
              ? response
              : [];

        const book =
          books.find(
            item =>
              Number(item.id) ===
              Number(favorite.bookId)
          );

        if (!book) {

          alert(
            "Book information could not be found."
          );

          return;

        }


        // =================================================
        // NORMALIZE BOOK DATA
        // =================================================

        this.selectedBook = {

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

          qty:
            Number(book.qty || 0),

          pages:
            Number(book.pages || 0),

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
            null,

          publicationYear:
            book.publicationYear || null

        };

      } catch (error) {

        console.error(
          "Failed to load book details:",
          error
        );

        alert(
          error.message ||
          "Failed to load book details."
        );

      } finally {

        this.detailLoadingId = null;

      }

    },


    // =====================================================
    // BORROW EVENT
    // =====================================================

    handleBorrow(data) {

      console.log(
        "Borrow request:",
        data
      );

    },


    // =====================================================
    // GO TO BROWSE BOOKS
    // =====================================================

    goToBrowseBooks() {

      this.$router.push({
        name: "BrowseBooks"
      });

    }

  }

};

</script>

