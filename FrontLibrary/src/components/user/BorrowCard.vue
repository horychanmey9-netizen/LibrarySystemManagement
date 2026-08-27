<script setup>

defineProps({
  book: {
    type: Object,
    required: true
  }
});

</script>


<template>

  <div
    class="flex flex-col gap-4 rounded-xl border border-gray-100 bg-white p-5 shadow-sm transition hover:shadow-md sm:flex-row"
  >

    <!-- IMAGE -->

    <div
      class="h-40 w-full shrink-0 overflow-hidden rounded-lg bg-gray-100 sm:h-32 sm:w-24"
    >

      <img
        v-if="book.image"
        :src="book.image"
        :alt="book.title"
        class="h-full w-full object-cover"
      />

      <div
        v-else
        class="flex h-full w-full items-center justify-center text-gray-400"
      >

        <i
          class="bi bi-book text-3xl"
        ></i>

      </div>

    </div>


    <!-- INFORMATION -->

    <div class="flex-1">

      <div
        class="flex flex-col justify-between gap-2 sm:flex-row"
      >

        <div>

          <h2
            class="text-lg font-semibold text-gray-800"
          >
            {{ book.title }}
          </h2>

          <p
            class="mt-1 text-sm text-gray-500"
          >
            {{ book.author }}
          </p>

          <p
            v-if="book.category"
            class="mt-1 text-xs text-gray-400"
          >
            {{ book.category }}
          </p>

        </div>


        <!-- STATUS -->

        <span
          class="h-fit w-fit rounded-full bg-blue-100 px-3 py-1 text-xs font-medium text-blue-600"
        >
          {{ book.status }}
        </span>

      </div>


      <!-- BORROW INFORMATION -->

      <div
        class="mt-4 grid grid-cols-1 gap-3 rounded-lg bg-gray-50 p-3 sm:grid-cols-2"
      >

        <div>

          <p
            class="text-xs text-gray-400"
          >
            Borrowed Date
          </p>

          <p
            class="mt-1 text-sm font-medium text-gray-700"
          >
            {{ book.borrowedDate || "-" }}
          </p>

        </div>


        <div>

          <p
            class="text-xs text-gray-400"
          >
            Due Date
          </p>

          <p
            class="mt-1 text-sm font-medium text-gray-700"
          >
            {{ book.dueDate || "-" }}
          </p>

        </div>

      </div>

    </div>

  </div>

</template>