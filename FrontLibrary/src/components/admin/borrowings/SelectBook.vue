<template>

  <div
    v-if="book"
    class="mt-5
           p-4
           bg-blue-50
           border border-blue-100
           rounded-2xl"
  >

    <div
      class="flex items-center
             justify-between
             gap-4"
    >

      <!-- LEFT -->
      <div class="flex items-center gap-4 min-w-0">

        <!-- Image -->
        <div
          class="w-16 h-20
                 rounded-xl
                 bg-white
                 border border-blue-100
                 overflow-hidden
                 flex items-center
                 justify-center
                 shrink-0"
        >

          <img
            v-if="book.image"
            :src="book.image"
            :alt="book.title"
            class="w-full h-full object-contain"
          />

          <span
            v-else
            class="text-2xl"
          >
            📚
          </span>

        </div>


        <!-- Info -->
        <div class="min-w-0">

          <p
            class="text-xs
                   text-blue-500
                   font-semibold
                   uppercase"
          >
            Selected Book
          </p>

          <h3
            class="font-bold
                   text-slate-800
                   truncate"
          >
            {{ book.title }}
          </h3>

          <p
            class="text-sm
                   text-slate-500
                   truncate"
          >
            {{ book.author }}
          </p>

          <p
            class="text-xs
                   text-slate-400
                   font-mono
                   mt-1"
          >
            ISBN: {{ book.isbn || "N/A" }}
          </p>

        </div>

      </div>


      <!-- RIGHT -->
      <div class="text-right shrink-0">

        <p
          class="text-xs
                 text-slate-500"
        >
          Available
        </p>

        <p
          class="text-xl
                 font-bold
                 text-emerald-600"
        >
          {{ availableQuantity }}
        </p>

      </div>

    </div>


    <!-- Remove -->
    <button
      type="button"
      @click="$emit('remove')"
      class="mt-3
             text-sm
             text-red-500
             hover:text-red-600
             font-medium"
    >
      Change Book
    </button>

  </div>

</template>


<script>
export default {

  name: "SelectedBook",

  props: {

    book: {
      type: Object,
      default: null
    }

  },

  emits: [
    "remove"
  ],

  computed: {

    availableQuantity() {

      if (!this.book) {
        return 0;
      }

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