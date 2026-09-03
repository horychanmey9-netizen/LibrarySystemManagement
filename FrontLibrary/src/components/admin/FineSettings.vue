<template>
  <div class="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
    <!-- Header -->
    <div class="flex items-center gap-3 mb-6">
      <div
        class="w-11 h-11 rounded-full bg-green-100 flex items-center justify-center"
      >
        <i class="bi bi-currency-dollar text-xl text-green-600"></i>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800">
          2. Fine Settings
        </h2>

        <p class="text-sm text-gray-500">
          Configure library fines
        </p>
      </div>
    </div>

    <!-- Fine Per Day -->
    <div class="grid grid-cols-3 gap-4 items-center mb-5">
      <label class="text-sm font-medium text-gray-700">
        Fine per Overdue Day
      </label>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.finePerDay"
            type="number"
            min="0"
            step="0.01"
            class="flex-1 min-w-0 px-4 py-2.5 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-green-500 focus:border-green-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            USD/day
          </span>
        </div>
      </div>
    </div>

    <!-- Grace Period -->
    <div class="grid grid-cols-3 gap-4 items-start mb-5">
      <div>
        <label class="text-sm font-medium text-gray-700">
          Grace Period
        </label>

        <p class="text-xs text-gray-400 mt-2">
          Days allowed after due date without fine
        </p>
      </div>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.gracePeriod"
            type="number"
            min="0"
            class="flex-1 min-w-0 px-4 py-2.5 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-green-500 focus:border-green-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            days
          </span>
        </div>
      </div>
    </div>

    <!-- Lost Book Fine -->
    <div class="grid grid-cols-3 gap-4 items-start mb-5">
      <div>
        <label class="text-sm font-medium text-gray-700">
          Lost Book Fine
        </label>

        <p class="text-xs text-gray-400 mt-2">
          80% of the book price
        </p>
      </div>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.lostBookFinePercentage"
            type="number"
            min="0"
            max="100"
            step="1"
            class="flex-1 min-w-0 px-4 py-2.5 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-green-500 focus:border-green-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            %
          </span>
        </div>
      </div>
    </div>

    <!-- Damaged Book Fine -->
    <div class="grid grid-cols-3 gap-4 items-center">
      <label class="text-sm font-medium text-gray-700">
        Damaged Book Fine
      </label>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.damagedBookFine"
            type="number"
            min="0"
            step="0.01"
            class="flex-1 min-w-0 px-4 py-2.5 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-green-500 focus:border-green-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            USD
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const settings = reactive({
  finePerDay: 0.5,
  gracePeriod: 1,
  lostBookFinePercentage: 80,
  damagedBookFine: 10,
});
</script>