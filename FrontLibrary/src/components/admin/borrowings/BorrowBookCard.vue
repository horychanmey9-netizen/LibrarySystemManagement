<template>

  <div
    class="bg-white
           border border-slate-200
           rounded-2xl
           overflow-hidden
           transition-all
           duration-200
           hover:shadow-lg
           hover:border-blue-300"
  >

    <!-- ================= IMAGE ================= -->
    <div
      class="h-52
             bg-slate-100
             flex items-center
             justify-center
             overflow-hidden"
    >

      <img
        v-if="book.image"
        :src="book.image"
        :alt="book.title"
        class="w-full h-full
               object-contain
               p-3"
      />

      <div
        v-else
        class="text-5xl text-slate-300"
      >
        📚
      </div>

    </div>


    <!-- ================= CONTENT ================= -->
    <div class="p-4">

      <!-- Title -->
      <h3
        class="font-bold
               text-slate-800
               text-base
               line-clamp-2
               min-h-[48px]"
      >
        {{ book.title }}
      </h3>


      <!-- Author -->
      <p
        class="mt-2
               text-sm
               text-slate-500
               truncate"
      >
        {{ book.author || "Unknown Author" }}
      </p>


      <!-- ISBN -->
      <p
        class="mt-1
               text-xs
               text-slate-400
               font-mono"
      >
        ISBN: {{ book.isbn || "N/A" }}
      </p>


      <!-- Available -->
      <div class="mt-4">

        <span
          v-if="availableQuantity > 0"
          class="inline-flex
                 items-center
                 px-3 py-1
                 rounded-full
                 bg-emerald-50
                 text-emerald-600
                 text-xs
                 font-semibold"
        >
          ✓ Available: {{ availableQuantity }}
        </span>


        <span
          v-else
          class="inline-flex
                 items-center
                 px-3 py-1
                 rounded-full
                 bg-red-50
                 text-red-500
                 text-xs
                 font-semibold"
        >
          × Not Available
        </span>

      </div>


      <!-- Select -->
      <button
        type="button"
        :disabled="availableQuantity <= 0"
        @click="$emit('select', book)"
        class="w-full
               mt-4
               py-2.5
               rounded-xl
               font-semibold
               text-sm
               transition"
        :class="
          availableQuantity > 0
            ? 'bg-blue-600 text-white hover:bg-blue-700'
            : 'bg-slate-100 text-slate-400 cursor-not-allowed'
        "
      >
        {{ availableQuantity > 0 ? "Select Book" : "Unavailable" }}
      </button>

    </div>

  </div>

</template>


<script>
export default {

  name: "BorrowBookCard",

  props: {

    book: {
      type: Object,
      required: true
    }

  },

  emits: [
    "select"
  ],

  computed: {

    availableQuantity() {

      // Support different backend field names
      if (this.book.availableQuantity !== undefined) {
        return Number(this.book.availableQuantity);
      }

      if (this.book.qty !== undefined) {
        return Number(this.book.qty);
      }

      if (this.book.quantity !== undefined) {
        return Number(this.book.quantity);
      }

      return 0;
    }

  }

};
</script>