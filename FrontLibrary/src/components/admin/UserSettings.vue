```vue
<template>
  <div class="bg-white rounded-xl border border-gray-200 shadow-sm p-6">
    <!-- Header -->
    <div class="flex items-center gap-3 mb-6">
      <div
        class="w-11 h-11 rounded-full bg-orange-100 flex items-center justify-center"
      >
        <i class="bi bi-people text-xl text-orange-600"></i>
      </div>

      <div>
        <h2 class="text-lg font-semibold text-gray-800">
          4. User Settings
        </h2>

        <p class="text-sm text-gray-500">
          Configure user account rules
        </p>
      </div>
    </div>

    <!-- Registration -->
    <div class="grid grid-cols-3 gap-4 items-center mb-6">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Allow User Registration
        </p>

        <p class="text-xs text-gray-500 mt-1">
          Allow new users to create an account
        </p>
      </div>

      <div class="col-span-2 flex justify-end">
        <button
          type="button"
          @click="
            settings.allowUserRegistration =
              !settings.allowUserRegistration
          "
          :class="[
            'relative inline-flex h-6 w-11 items-center rounded-full transition',
            settings.allowUserRegistration
              ? 'bg-blue-600'
              : 'bg-gray-300'
          ]"
        >
          <span
            :class="[
              'inline-block h-4 w-4 transform rounded-full bg-white transition',
              settings.allowUserRegistration
                ? 'translate-x-6'
                : 'translate-x-1'
            ]"
          ></span>
        </button>
      </div>
    </div>

    <!-- Admin Approval -->
    <div class="grid grid-cols-3 gap-4 items-center mb-6">
      <div>
        <p class="text-sm font-medium text-gray-700">
          Require Admin Approval
        </p>

        <p class="text-xs text-gray-500 mt-1">
          New users must be approved by admin
        </p>
      </div>

      <div class="col-span-2 flex justify-end">
        <button
          type="button"
          @click="
            settings.requireAdminApproval =
              !settings.requireAdminApproval
          "
          :class="[
            'relative inline-flex h-6 w-11 items-center rounded-full transition',
            settings.requireAdminApproval
              ? 'bg-blue-600'
              : 'bg-gray-300'
          ]"
        >
          <span
            :class="[
              'inline-block h-4 w-4 transform rounded-full bg-white transition',
              settings.requireAdminApproval
                ? 'translate-x-6'
                : 'translate-x-1'
            ]"
          ></span>
        </button>
      </div>
    </div>

    <!-- Default User Role -->
    <div class="grid grid-cols-3 gap-4 items-center mb-6">
      <label class="text-sm font-medium text-gray-700">
        Default User Role
      </label>

      <div class="col-span-2">
        <input
          type="text"
          :value="settings.defaultUserRole"
          readonly
          class="w-full px-4 py-2.5 border border-gray-300 rounded-lg
                 bg-gray-50 text-gray-700 outline-none cursor-not-allowed"
        />
      </div>
    </div>

    <!-- Inactive Account -->
    <div class="grid grid-cols-3 gap-4 items-start">
      <div>
        <label class="text-sm font-medium text-gray-700">
          Inactive Account Period
        </label>

        <p class="text-xs text-gray-400 mt-2">
          Accounts inactive for this period may be deactivated
        </p>
      </div>

      <div class="col-span-2">
        <div class="relative">
          <input
            v-model.number="settings.inactiveAccountPeriod"
            type="number"
            min="1"
            class="w-full px-4 py-2.5 pr-14 border border-gray-300 rounded-lg
                   focus:ring-2 focus:ring-blue-500
                   focus:border-blue-500 outline-none"
          />

          <span
            class="absolute right-4 top-1/2 -translate-y-1/2
                   text-sm text-gray-500"
          >
            days
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive } from "vue";

const settings = reactive({
  allowUserRegistration: true,
  requireAdminApproval: true,
  defaultUserRole: "Admin",
  inactiveAccountPeriod: 90,
});
</script>
```
