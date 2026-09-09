<template>
  <div class="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
    <!-- Header -->
    <div class="flex items-center gap-3 mb-6">
      <div
        class="w-11 h-11 rounded-full bg-pink-100 flex items-center justify-center"
      >
        <i class="bi bi-bell text-xl text-pink-600"></i>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800">
          4. Notification Settings
        </h2>

        <p class="text-sm text-gray-500">
          Configure system notifications
        </p>
      </div>
    </div>

    <!-- Due Date Reminder -->
    <div class="mb-6">
      <div class="flex items-center justify-between">
        <div>
          <p class="text-sm font-medium text-gray-700">
            Due Date Reminder
          </p>

          <p class="text-xs text-gray-500 mt-1">
            Send reminder before due date
          </p>
        </div>

        <div class="flex items-center gap-3">
          <input
            v-model.number="settings.dueDateReminder"
            type="number"
            min="0"
            class="w-20 px-3 py-2 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-pink-500
                   focus:border-pink-500 outline-none"
          />

          <span class="text-sm text-gray-500">
            days before
          </span>
        </div>
      </div>
    </div>

    <!-- Overdue -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Overdue Notification
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Send notification when book is overdue
        </p>
      </div>

      <button
        type="button"
        @click="
          settings.overdueNotification =
            !settings.overdueNotification
        "
        :class="[
          'relative inline-flex h-6 w-11 items-center rounded-full transition',
          settings.overdueNotification
            ? 'bg-blue-600'
            : 'bg-gray-300'
        ]"
      >
        <span
          :class="[
            'inline-block h-4 w-4 transform rounded-full bg-white transition',
            settings.overdueNotification
              ? 'translate-x-6'
              : 'translate-x-1'
          ]"
        ></span>
      </button>
    </div>

    <!-- Fine Notification -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Fine Notification
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Send notification when fine is generated
        </p>
      </div>

      <button
        type="button"
        @click="
          settings.fineNotification =
            !settings.fineNotification
        "
        :class="[
          'relative inline-flex h-6 w-11 items-center rounded-full transition',
          settings.fineNotification
            ? 'bg-blue-600'
            : 'bg-gray-300'
        ]"
      >
        <span
          :class="[
            'inline-block h-4 w-4 transform rounded-full bg-white transition',
            settings.fineNotification
              ? 'translate-x-6'
              : 'translate-x-1'
          ]"
        ></span>
      </button>
    </div>

    <!-- Return Reminder -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Book Return Reminder
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Send reminder if book is not returned
        </p>
      </div>

      <button
        type="button"
        @click="
          settings.bookReturnReminder =
            !settings.bookReturnReminder
        "
        :class="[
          'relative inline-flex h-6 w-11 items-center rounded-full transition',
          settings.bookReturnReminder
            ? 'bg-blue-600'
            : 'bg-gray-300'
        ]"
      >
        <span
          :class="[
            'inline-block h-4 w-4 transform rounded-full bg-white transition',
            settings.bookReturnReminder
              ? 'translate-x-6'
              : 'translate-x-1'
          ]"
        ></span>
      </button>
    </div>

    <!-- Enable Notifications -->
    <div class="flex items-center justify-between">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Enable Notifications
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Enable or disable all notifications
        </p>
      </div>

      <button
        type="button"
        @click="
          settings.enableNotifications =
            !settings.enableNotifications
        "
        :class="[
          'relative inline-flex h-6 w-11 items-center rounded-full transition',
          settings.enableNotifications
            ? 'bg-blue-600'
            : 'bg-gray-300'
        ]"
      >
        <span
          :class="[
            'inline-block h-4 w-4 transform rounded-full bg-white transition',
            settings.enableNotifications
              ? 'translate-x-6'
              : 'translate-x-1'
          ]"
        ></span>
      </button>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const settings = reactive({
  dueDateReminder: 2,
  overdueNotification: true,
  fineNotification: true,
  bookReturnReminder: true,
  enableNotifications: true,
});
</script>