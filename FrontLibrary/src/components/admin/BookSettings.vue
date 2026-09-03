<template>
  <div class="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
    <!-- Header -->
    <div class="flex items-center gap-3 mb-6">
      <div
        class="w-11 h-11 rounded-full bg-purple-100 flex items-center justify-center"
      >
        <i class="bi bi-book-half text-xl text-purple-600"></i>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800">
          3. Book Settings
        </h2>

        <p class="text-sm text-gray-500">
          Configure book management rules
        </p>
      </div>
    </div>

    <!-- Low Stock Threshold -->
    <div class="grid grid-cols-3 gap-4 items-start mb-6">
      <div>
        <label class="text-sm font-medium text-gray-700">
          Low Stock Threshold
        </label>

        <p class="text-xs text-gray-400 mt-2">
          Show alert when stock is less than or equal to this value
        </p>
      </div>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.lowStockThreshold"
            type="number"
            min="0"
            class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-purple-500
                   focus:border-purple-500 outline-none"
          />

          <span class="text-sm text-gray-500 whitespace-nowrap">
            copies
          </span>
        </div>
      </div>
    </div>

    <!-- Duplicate ISBN -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Allow Duplicate ISBN
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Allow adding books with duplicate ISBN
        </p>
      </div>

      <button
        type="button"
        @click="
          settings.allowDuplicateISBN =
            !settings.allowDuplicateISBN
        "
        :class="[
          'relative inline-flex h-6 w-11 items-center rounded-full transition',
          settings.allowDuplicateISBN
            ? 'bg-purple-600'
            : 'bg-gray-300'
        ]"
      >
        <span
          :class="[
            'inline-block h-4 w-4 transform rounded-full bg-white transition',
            settings.allowDuplicateISBN
              ? 'translate-x-6'
              : 'translate-x-1'
          ]"
        ></span>
      </button>
    </div>

    <!-- Book Availability -->
    <div>
      <label class="block text-sm font-medium text-gray-700 mb-2">
        Book Availability
      </label>

      <select
        v-model="settings.bookAvailability"
        class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
               focus:ring-2 focus:ring-purple-500
               focus:border-purple-500 outline-none bg-white"
      >
        <option value="available">
          Only available copies can be borrowed
        </option>

        <option value="all">
          All books can be requested
        </option>

        <option value="reserved">
          Only reserved books can be borrowed
        </option>
      </select>

      <p class="text-xs text-gray-400 mt-2">
        Rule for book availability
      </p>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const settings = reactive({
  lowStockThreshold: 3,
  allowDuplicateISBN: false,
  bookAvailability: "available",
});
</script>