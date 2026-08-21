<template>
  <div
    class="bg-white border border-slate-200 rounded-2xl p-4
           hover:-translate-y-1 hover:shadow-lg
           transition duration-300"
  >

    <!-- ================= BOOKMARK ================= -->
    <div class="flex justify-end mb-2">
      <button
        type="button"
        @click.stop="toggleBookmark"
        class="w-9 h-9 border border-slate-200
               rounded-lg hover:bg-blue-50
               transition"
        :class="
          book.bookmarked
            ? 'text-blue-600 bg-blue-50'
            : 'text-slate-500'
        "
        aria-label="Bookmark book"
      >
        {{ book.bookmarked ? '♥' : '♡' }}
      </button>
    </div>


    <!-- ================= BOOK COVER ================= -->
    <button
      type="button"
      @click="viewDetails"
      class="w-full text-left"
    >
      <div
        class="h-56 bg-slate-100 rounded-xl
               flex items-center justify-center
               overflow-hidden mb-4"
      >
        <img
          v-if="book.image"
          :src="book.image"
          :alt="book.title"
          class="h-full w-full object-contain"
        />

        <!-- Fallback -->
        <div
          v-else
          class="text-slate-400 text-5xl"
        >
          📚
        </div>
      </div>
    </button>


    <!-- ================= TITLE ================= -->
    <h3
      class="font-bold text-lg
             leading-tight min-h-[48px]"
    >
      {{ book.title }}
    </h3>


    <!-- ================= AUTHOR ================= -->
    <p class="text-sm text-slate-400 mt-1">
      {{ book.author }}
    </p>


    <!-- ================= CATEGORY ================= -->
    <span
      class="inline-block mt-3 px-3 py-1
             rounded-full bg-blue-50
             text-blue-600 text-xs font-medium"
    >
      {{ book.category }}
    </span>


    <!-- ================= STATUS ================= -->
    <div class="mt-3">

      <!-- Available -->
      <span
        v-if="book.status === true"
        class="inline-flex items-center gap-1
               px-3 py-1 rounded-full
               bg-emerald-50 text-emerald-600
               text-xs font-medium"
      >
        ✓ Available
      </span>

      <!-- Borrowed -->
      <span
        v-else
        class="inline-flex items-center gap-1
               px-3 py-1 rounded-full
               bg-red-50 text-red-500
               text-xs font-medium"
      >
        × Borrowed
      </span>

    </div>


    <!-- ================= VIEW DETAILS ================= -->
    <button
      type="button"
      @click="viewDetails"
      class="w-full mt-4
             border-2 border-blue-500
             text-blue-600
             hover:bg-blue-600
             hover:text-white
             py-2.5 rounded-xl
             font-medium
             transition"
    >
      View Details
    </button>

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

  methods: {

    // ============================
    // BOOKMARK
    // ============================
    toggleBookmark() {
      this.$emit("bookmark", this.book);
    },

    // ============================
    // VIEW DETAILS
    // ============================
    viewDetails() {
      this.$emit("view-details", this.book);
    }

  }
};
</script>