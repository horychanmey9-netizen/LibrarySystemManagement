<template>
  <div class="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
    <!-- Header -->
    <div class="flex items-center gap-3 mb-6">
      <div
        class="w-11 h-11 rounded-full bg-blue-100 flex items-center justify-center"
      >
        <i class="bi bi-book text-xl text-blue-600"></i>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800">
          1. Borrowing Settings
        </h2>

        <p class="text-sm text-gray-500">
          Configure borrowing rules
        </p>
      </div>
    </div>

    <!-- Maximum Books -->
    <div class="grid grid-cols-3 gap-4 items-center mb-5">
      <label class="text-sm font-medium text-gray-700">
        Maximum Books per User
      </label>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.maxBooksPerUser"
            type="number"
            min="1"
            class="flex-1 min-w-0 w-full px-4 py-2.5
                   border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-blue-500
                   focus:border-blue-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            books
          </span>
        </div>
      </div>
    </div>

    <!-- Borrowing Period -->
    <div class="grid grid-cols-3 gap-4 items-center mb-5">
      <label class="text-sm font-medium text-gray-700">
        Borrowing Period
      </label>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.borrowingPeriod"
            type="number"
            min="1"
            class="flex-1 min-w-0 w-full px-4 py-2.5
                   border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-blue-500
                   focus:border-blue-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            days
          </span>
        </div>
      </div>
    </div>

    <!-- Renewal Period -->
    <div class="grid grid-cols-3 gap-4 items-center mb-5">
      <label class="text-sm font-medium text-gray-700">
        Renewal Period
      </label>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.renewalPeriod"
            type="number"
            min="1"
            class="flex-1 min-w-0 w-full px-4 py-2.5
                   border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-blue-500
                   focus:border-blue-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            days
          </span>
        </div>
      </div>
    </div>

    <!-- Maximum Renewals -->
    <div class="grid grid-cols-3 gap-4 items-center mb-6">
      <label class="text-sm font-medium text-gray-700">
        Maximum Renewals
      </label>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.maxRenewals"
            type="number"
            min="0"
            class="flex-1 min-w-0 w-full px-4 py-2.5
                   border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-blue-500
                   focus:border-blue-500
                   outline-none"
          />

          <span class="w-16 text-sm text-gray-500 whitespace-nowrap">
            times
          </span>
        </div>
      </div>
    </div>

    <!-- Allow Renewal -->
    <div class="grid grid-cols-3 gap-4 items-center">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Allow Renewal
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Allow users to renew borrowed books
        </p>
      </div>

      <div class="col-span-2">
        <div class="flex items-center gap-3">
          <!-- Space -->
          <div class="w-69"></div>

          <!-- Toggle Button -->
          <button
            type="button"
            @click="settings.allowRenewal = !settings.allowRenewal"
            :class="[
              'relative inline-flex h-6 w-11 items-center rounded-full transition',
              settings.allowRenewal
                ? 'bg-blue-600'
                : 'bg-gray-300'
            ]"
          >
            <span
              :class="[
                'inline-block h-4 w-4 transform rounded-full bg-white transition',
                settings.allowRenewal
                  ? 'translate-x-6'
                  : 'translate-x-1'
              ]"
            ></span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const settings = reactive({
  maxBooksPerUser: 5,
  borrowingPeriod: 14,
  renewalPeriod: 7,
  maxRenewals: 2,
  allowRenewal: true,
});
</script>