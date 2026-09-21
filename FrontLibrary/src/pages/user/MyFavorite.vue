<template>
  <div class="My_Favorite_page min-h-screen bg-slate-50 p-6 max-md:p-5 max-[550px]:p-3">

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

      <div class="text-center">

        <div
          class="mx-auto mb-3 h-8 w-8
                 animate-spin rounded-full
                 border-4 border-slate-200
                 border-t-blue-600"
        ></div>

        <p class="text-slate-500">
          Loading favorites...
        </p>

      </div>

    </div>


    <!-- =====================================================
         ERROR
    ====================================================== -->

    <div
      v-else-if="error"
      class="rounded-xl border border-red-200
             bg-red-50 p-5 text-red-600"
    >

      {{ error }}

    </div>


    <!-- =====================================================
         EMPTY
    ====================================================== -->

    <div
      v-else-if="favorites.length === 0"
      class="rounded-2xl border border-slate-200
             bg-white p-10 text-center"
    >

      <div class="mb-4 text-5xl">
        ♡
      </div>

      <h2 class="text-xl font-semibold text-slate-700">
        No Favorite Books
      </h2>

      <p class="mt-2 text-slate-400">
        You haven't added any books to your favorites yet.
      </p>

      <button
        type="button"
        @click="goToBrowseBooks"
        class="mt-6 rounded-xl
               bg-blue-600 px-5 py-2.5
               font-medium text-white
               transition hover:bg-blue-700"
      >
        Browse Books
      </button>

    </div>


    <!-- =====================================================
         FAVORITE BOOK GRID
         SAME STYLE AS BOOK CARD
    ====================================================== -->

    <div
      v-else
      class="grid grid-cols-2 gap-3
             sm:grid-cols-3
             lg:grid-cols-4
             xl:grid-cols-5
             2xl:grid-cols-6"
    >

      <BookCard
      class="px-3"
        v-for="favorite in favoriteBooks"
        :key="favorite.id"
        :book="favorite"
        @bookmark="handleBookmark"
        @view-details="viewDetails"
      />

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

import BookCard
  from "../../components/books/BookCard.vue";

import BookDetails
  from "../../components/books/BookDetails.vue";


export default {

  name: "MyFavorite",


  components: {

    BookCard,

    BookDetails

  },


  data() {

    return {

      // =====================================================
      // FAVORITES
      // =====================================================

      favorites: [],


      // =====================================================
      // LOADING
      // =====================================================

      loading: false,


      // =====================================================
      // ERROR
      // =====================================================

      error: null,


      // =====================================================
      // SELECTED BOOK
      // =====================================================

      selectedBook: null,


      // =====================================================
      // DETAILS LOADING
      // =====================================================

      detailLoadingId: null

    };

  },


  computed: {

    // =====================================================
    // CONVERT FAVORITE DATA
    // INTO BOOKCARD FORMAT
    // =====================================================

    favoriteBooks() {

      return this.favorites.map((favorite) => {

        return {

          id: favorite.bookId,

          title: favorite.title || "",

          author:
            favorite.author ||
            "Unknown Author",

          image:
            favorite.image ||
            favorite.imageUrl ||
            favorite.coverImage ||
            null,

          bookmarked: true,

          category:
            favorite.category?.name ||
            favorite.categoryName ||
            favorite.category ||
            "Other"

        };

      });

    }

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

        }

        else if (Array.isArray(response)) {

          this.favorites =
            response;

        }

        else {

          this.favorites = [];

        }

      }

      catch (error) {

        console.error(
          "Failed to load favorites:",
          error
        );

        this.error =
          error.message ||
          "Failed to load favorite books.";

      }

      finally {

        this.loading = false;

      }

    },


    // =====================================================
    // BOOKMARK CLICK
    // =====================================================

    async handleBookmark(book) {

      if (!book?.id) {

        return;

      }

      try {

        await deleteFavorite(book.id);


        // Remove from local favorite list

        this.favorites =
          this.favorites.filter(
            favorite =>
              Number(favorite.bookId) !==
              Number(book.id)
          );

      }

      catch (error) {

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

    async viewDetails(book) {

      if (!book?.id) {

        return;

      }


      this.detailLoadingId =
        book.id;


      try {

        const response =
          await getBooks();


        const books =
          Array.isArray(response?.data)
            ? response.data
            : Array.isArray(response)
              ? response
              : [];


        const foundBook =
          books.find(
            item =>
              Number(item.id) ===
              Number(book.id)
          );


        if (!foundBook) {

          alert(
            "Book information could not be found."
          );

          return;

        }


        // =================================================
        // NORMALIZE BOOK DATA
        // =================================================

        this.selectedBook = {

          id:
            foundBook.id,

          title:
            foundBook.title || "",

          author:
            foundBook.author || "",

          category:
            foundBook.category?.name ||
            foundBook.categoryName ||
            foundBook.category ||
            "Other",

          status:
            foundBook.status === true ||
            foundBook.status === "Available" ||
            foundBook.status === "AVAILABLE",

          qty:
            Number(foundBook.qty || 0),

          pages:
            Number(foundBook.pages || 0),

          isbn:
            foundBook.isbn || "",

          language:
            foundBook.language || "",

          description:
            foundBook.description || "",

          image:
            foundBook.image ||
            foundBook.imageUrl ||
            foundBook.coverImage ||
            null,

          publicationYear:
            foundBook.publicationYear ||
            null

        };

      }

      catch (error) {

        console.error(
          "Failed to load book details:",
          error
        );

        alert(
          error.message ||
          "Failed to load book details."
        );

      }

      finally {

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


<style scoped>

button {

  -webkit-tap-highlight-color: transparent;

}

.My_Favorite_page{
    width: 100%;

  min-height: 100%;

  padding: 20px 90px;

  box-sizing: border-box;

  background: #f8faff;
}




/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 639px) {

  .min-h-screen {

    padding-left: 12px;
    padding-right: 12px;

  }

}

</style>