
<template>
  <div
    class="group relative min-w-0"
  >

    <!-- =====================================================
         BOOK COVER
    ====================================================== -->

    <div
      class="relative flex aspect-[3.5/5] w-full
             items-center justify-center
             overflow-hidden rounded-2xl
             bg-slate-100"
    >

      <!-- =================================================
           BOOK IMAGE
      ================================================== -->

      <button
        type="button"
        @click="viewDetails"
        class="flex h-full w-full items-center
               justify-center overflow-hidden
               text-left outline-none"
        :aria-label="`View details of ${book.title}`"
      >

        <img
          v-if="book.image"
          :src="book.image"
          :alt="book.title"
          class="h-full w-full object-cover
                 transition duration-300
                 group-hover:scale-105"
        />

        <!-- Fallback -->

        <div
          v-else
          class="flex h-full w-full items-center
                 justify-center text-5xl
                 text-slate-400"
        >
          📚
        </div>

      </button>


      <!-- =================================================
           FAVORITE BUTTON
      ================================================== -->

      <button
  type="button"
  @click.stop="toggleBookmark"
  :disabled="favoriteLoading"
  class="absolute right-2 top-2
         flex h-8 w-8 items-center
         justify-center rounded-full
         bg-white/95 shadow-sm
         outline-none transition duration-200
         hover:scale-110
         disabled:cursor-not-allowed
         disabled:opacity-60
         sm:right-3 sm:top-3
         sm:h-10 sm:w-10"
  :class="
    book.bookmarked
      ? 'text-red-500'
      : 'text-pink-500'
  "
  :aria-label="
    book.bookmarked
      ? 'Remove from favorites'
      : 'Add to favorites'
  "
>

  <!-- Filled Heart -->

  <span
    v-if="book.bookmarked"
    class="text-lg leading-none
           sm:text-2xl"
  >
    ♥
  </span>

  <!-- Empty Heart -->

  <span
    v-else
    class="text-lg leading-none
           sm:text-2xl"
  >
    ♡
  </span>

</button>


      <!-- =================================================
           VIEW DETAILS BUTTON
           OVER IMAGE - BOTTOM LEFT
      ================================================== -->

      <button
  type="button"
  @click.stop="viewDetails"
  class="absolute bottom-2 left-2
         inline-flex items-center gap-1.5
         rounded-lg
         bg-blue-200/95
         px-2.5 py-1.5
         text-[11px] font-semibold
         text-blue-700
         shadow-sm
         backdrop-blur-sm
         transition duration-200
         hover:bg-blue-300
         hover:text-blue-800
         hover:shadow-md
         focus:outline-none
         focus:ring-2
         focus:ring-blue-400
         focus:ring-offset-2
         sm:bottom-3 sm:left-3
         sm:gap-2
         sm:rounded-xl
         sm:px-4 sm:py-2.5
         sm:text-sm"
>

        <!-- Eye Icon -->

        <svg
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 24 24"
          fill="none"
          stroke="currentColor"
          stroke-width="2"
          class="h-4 w-4"
        >

          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            d="M2.25 12s3.75-7 9.75-7
               9.75 7 9.75 7-3.75 7-9.75 7
               -9.75-7-9.75-7Z"
          />

          <circle
            cx="12"
            cy="12"
            r="3"
          />

        </svg>

        View Details

      </button>

    </div>


    <!-- =====================================================
         BOOK TITLE
    ====================================================== -->

    <div
      class="mt-3 px-1"
    >

      <h3
        class="line-clamp-2
               min-h-[44px]
               text-base font-semibold
               leading-snug text-slate-800"
        :title="book.title"
      >
        {{ book.title }}
      </h3>

    </div>

  </div>
</template>


<script>

export default {

  name: "BookCard",

  props: {

    book: {

      type: Object,

      required: true

    }

  },

  emits: [

    "bookmark",

    "view-details"

  ],

  data() {

    return {

      favoriteLoading: false

    };

  },

  methods: {

    

    // =====================================================
    // BOOKMARK
    // =====================================================

    toggleBookmark() {

      if (this.favoriteLoading) {

        return;

      }

      this.favoriteLoading = true;

      /*
       * Home.vue handles the actual
       * Favorite API request.
       */

      this.$emit(

        "bookmark",

        this.book

      );

      /*
       * Reset loading after request event.
       */

      setTimeout(() => {

        this.favoriteLoading = false;

      }, 500);

    },


    // =====================================================
    // VIEW DETAILS
    // =====================================================

    viewDetails() {

      this.$emit(

        "view-details",

        this.book

      );

    }

  }

};

</script>


<style scoped>

/* =====================================================
   BUTTON
===================================================== */

button {

  -webkit-tap-highlight-color: transparent;

}


/* =====================================================
   BOOK IMAGE
===================================================== */

img {

  user-select: none;

  -webkit-user-drag: none;

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 639px) {

  .group {

    min-width: 0;

  }

}

</style>