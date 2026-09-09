<template>
  <div
    class="w-full bg-white rounded-xl border border-gray-200 shadow-sm p-6">
    <!-- Header -->
    <div class="flex items-center gap-3 mb-6">
      <div
        class="w-11 h-11 rounded-full bg-cyan-100 flex items-center justify-center"
      >
        <i class="bi bi-building text-xl text-cyan-600"></i>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800">
          5. Library Information
        </h2>

        <p class="text-sm text-gray-500">
          Manage library information
        </p>
      </div>
    </div>

    <!-- Library Name -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Library Name
      </label>

      <div class="col-span-2">
        <input
          v-model="settings.libraryName"
          type="text"
          placeholder="Enter library name"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none"
        />
      </div>
    </div>

    <!-- Email -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Email
      </label>

      <div class="col-span-2">
        <input
          v-model="settings.email"
          type="email"
          placeholder="library@example.com"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none"
        />
      </div>
    </div>

    <!-- Phone -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Phone Number
      </label>

      <div class="col-span-2">
        <input
          v-model="settings.phoneNumber"
          type="tel"
          placeholder="012 345 678"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none"
        />
      </div>
    </div>

    <!-- Address -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Address
      </label>

      <div class="col-span-2">
        <textarea
          v-model="settings.address"
          rows="2"
          placeholder="Enter library address"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none resize-none"
        ></textarea>
      </div>
    </div>

    <!-- Opening Time -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Opening Time
      </label>

      <div class="col-span-2">
        <input
          v-model="settings.openingTime"
          type="time"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none"
        />
      </div>
    </div>

    <!-- Closing Time -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Closing Time
      </label>

      <div class="col-span-2">
        <input
          v-model="settings.closingTime"
          type="time"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none"
        />
      </div>
    </div>

    <!-- Currency -->
    <div class="grid grid-cols-3 gap-4 items-center mb-4">
      <label class="text-sm font-medium text-gray-700">
        Currency
      </label>

      <div class="col-span-2">
        <select
          v-model="settings.currency"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none bg-white"
        >
          <option value="USD">
            USD (US Dollar)
          </option>

          <option value="KHR">
            KHR (Cambodian Riel)
          </option>
        </select>
      </div>
    </div>

    <!-- Timezone -->
    <div class="grid grid-cols-3 gap-4 items-center">
      <label class="text-sm font-medium text-gray-700">
        Timezone
      </label>

      <div class="col-span-2">
        <select
          v-model="settings.timezone"
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 focus:ring-2 focus:ring-cyan-500
                 focus:border-cyan-500 outline-none bg-white"
        >
          <option value="Asia/Phnom_Penh">
            (UTC+07:00) Phnom Penh
          </option>

          <option value="Asia/Ho_Chi_Minh">
            (UTC+07:00) Ho Chi Minh
          </option>
        </select>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const settings = reactive({
  libraryName: "Royal University Library",
  email: "library@rupp.edu.kh",
  phoneNumber: "012 345 678",
  address: "Russian Federation Blvd (110), Phnom Penh, Cambodia",
  openingTime: "07:30",
  closingTime: "18:00",
  currency: "USD",
  timezone: "Asia/Phnom_Penh",
});
</script>