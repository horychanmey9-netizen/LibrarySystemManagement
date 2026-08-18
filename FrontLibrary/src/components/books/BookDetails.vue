<template>
  <!-- ================= OVERLAY ================= -->
  <div
    class="fixed inset-0 z-50
           bg-black/50 backdrop-blur-sm
           flex items-center justify-center
           px-4 py-6"
    @click.self="$emit('close')"
  >

    <!-- ================= MODAL ================= -->
    <div
      class="w-full max-w-5xl
             max-h-[92vh]
             bg-white rounded-2xl
             shadow-2xl overflow-hidden"
    >

      <!-- ================= HEADER ================= -->
      <div
        class="flex items-center justify-between
               px-6 py-4
               border-b border-slate-200"
      >
        <div>
          <h2
            class="text-xl font-bold
                   text-slate-800"
          >
            Book Details
          </h2>

          <p class="text-xs text-slate-400 mt-1">
            View complete information about this book
          </p>
        </div>

        <!-- Close -->
        <button
          type="button"
          @click="$emit('close')"
          class="w-10 h-10
                 flex items-center justify-center
                 rounded-xl
                 text-slate-500
                 hover:bg-slate-100
                 hover:text-slate-800
                 transition"
        >
          ✕
        </button>
      </div>


      <!-- ================= CONTENT ================= -->
      <div
        class="p-6 overflow-y-auto
               max-h-[calc(92vh-80px)]"
      >

        <div
          class="grid grid-cols-1
                 md:grid-cols-5
                 gap-8"
        >

          <!-- ================= IMAGE ================= -->
          <div class="md:col-span-2">

            <div
              class="h-[450px]
                     bg-slate-100
                     rounded-2xl
                     overflow-hidden
                     flex items-center
                     justify-center"
            >
              <img
                v-if="book.image"
                :src="book.image"
                :alt="book.title"
                class="w-full h-full
                       object-contain"
              />

              <div
                v-else
                class="text-6xl text-slate-300"
              >
                📚
              </div>
            </div>


            <!-- ================= STATUS ================= -->
            <div class="mt-4">

              <!-- Available -->
              <div
                v-if="book.status === true"
                class="flex items-center
                       justify-center gap-2
                       w-full
                       px-4 py-3
                       rounded-xl
                       bg-emerald-50
                       text-emerald-600
                       font-semibold"
              >
                <span>✓</span>
                Available
              </div>


              <!-- Borrowed -->
              <div
                v-else
                class="flex items-center
                       justify-center gap-2
                       w-full
                       px-4 py-3
                       rounded-xl
                       bg-red-50
                       text-red-500
                       font-semibold"
              >
                <span>×</span>
                Borrowed
              </div>

            </div>

          </div>


          <!-- ================= BOOK INFO ================= -->
          <div class="md:col-span-3">

            <!-- Title -->
            <h1
              class="text-3xl
                     font-bold
                     text-slate-900
                     leading-tight"
            >
              {{ book.title }}
            </h1>


            <!-- Author -->
            <p
              class="mt-2
                     text-slate-500"
            >
              Written by

              <span
                class="font-semibold
                       text-slate-700"
              >
                {{ book.author }}
              </span>
            </p>


            <!-- ================= INFORMATION GRID ================= -->
            <div
              class="grid
                     grid-cols-2
                     sm:grid-cols-3
                     gap-4
                     mt-7"
            >

              <!-- Quantity -->
              <div
                class="bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  Quantity
                </p>

                <p
                  class="mt-1
                         text-lg
                         font-bold
                         text-slate-800"
                >
                  {{ book.qty }}
                </p>
              </div>


              <!-- Pages -->
              <div
                class="bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  Pages
                </p>

                <p
                  class="mt-1
                         text-lg
                         font-bold
                         text-slate-800"
                >
                  {{ book.pages }}
                </p>
              </div>


              <!-- Language -->
              <div
                class="bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  Language
                </p>

                <p
                  class="mt-1
                         text-lg
                         font-bold
                         text-slate-800"
                >
                  {{ book.language }}
                </p>
              </div>


              <!-- ISBN -->
              <div
                class="col-span-2
                       sm:col-span-3
                       bg-slate-50
                       rounded-xl
                       p-4"
              >
                <p
                  class="text-xs
                         text-slate-400
                         font-semibold
                         uppercase"
                >
                  ISBN
                </p>

                <p
                  class="mt-1
                         font-mono
                         text-slate-800
                         font-medium"
                >
                  {{ book.isbn }}
                </p>
              </div>

            </div>


            <!-- ================= DESCRIPTION ================= -->
            <div class="mt-7">

              <h3
                class="text-sm
                       font-bold
                       text-slate-800
                       uppercase
                       tracking-wide"
              >
                Description
              </h3>

              <p
                class="mt-3
                       text-sm
                       text-slate-600
                       leading-7"
              >
                {{ book.description }}
              </p>

            </div>


            <!-- ================= ACTION ================= -->
            <div
              class="mt-8
                     flex gap-3"
            >

              <!-- Close -->
              <button
                type="button"
                @click="$emit('close')"
                class="flex-1
                       border border-slate-200
                       hover:bg-slate-50
                       text-slate-700
                       py-3
                       rounded-xl
                       font-medium
                       transition"
              >
                Close
              </button>


              <!-- Borrow -->
              <button
                v-if="book.status === true"
                type="button"
                @click="borrowBook"
                class="flex-1
                       bg-blue-600
                       hover:bg-blue-700
                       text-white
                       py-3
                       rounded-xl
                       font-medium
                       transition"
              >
                Borrow Book
              </button>


              <!-- Already Borrowed -->
              <button
                v-else
                type="button"
                disabled
                class="flex-1
                       bg-slate-100
                       text-slate-400
                       py-3
                       rounded-xl
                       font-medium
                       cursor-not-allowed"
              >
                Currently Borrowed
              </button>

            </div>

          </div>

        </div>

      </div>

    </div>

  </div>
</template>


<script>
export default {

  name: "BookDetail",

  props: {
    book: {
      type: Object,
      required: true
    }
  },

  emits: [
    "close",
    "borrow"
  ],

  methods: {

    borrowBook() {
      this.$emit("borrow", this.book);
    }

  }

};
</script>