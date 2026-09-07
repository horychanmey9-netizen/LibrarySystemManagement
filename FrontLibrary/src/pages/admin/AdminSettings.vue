<template>
  <div class="min-h-screen bg-gray-50">
    <!-- ================= HEADER ================= -->
    <div
      class="px-6 py-4
             flex items-center justify-between"
    >
      <!-- Left -->
      <div class="flex items-center gap-3">
        <div
          class="w-10 h-10 rounded-lg bg-blue-100
                 flex items-center justify-center"
        >
          <i class="bi bi-gear text-xl text-blue-600"></i>
        </div>

        <div>
          <h1 class="text-2xl font-bold text-gray-800">
            Settings
          </h1>

          <p class="text-sm text-gray-500">
            Manage your library system settings
          </p>
        </div>
      </div>

      <!-- Save -->
      <button
        @click="saveSettings"
        :disabled="saving"
        class="flex items-center gap-2 px-5 py-2.5
               bg-blue-600 hover:bg-blue-700
               disabled:bg-blue-400
               text-white font-medium rounded-lg
               transition shadow-sm"
      >
        <i
          :class="[
            saving
              ? 'bi bi-arrow-repeat animate-spin'
              : 'bi bi-check-lg'
          ]"
        ></i>

        {{ saving ? "Saving..." : "Save Changes" }}
      </button>
    </div>

    <!-- ================= CONTENT ================= -->
    <main class="p-6">
      <div class="max-w-[1600px] mx-auto">

        <!-- Settings Grid -->
        <div
          class="grid grid-cols-1 xl:grid-cols-2
                 2xl:grid-cols-3 gap-6"
        >
          <!-- Borrowing -->
          <BorrowingSettings />

          <!-- Fine -->
          <FineSettings />

          <!-- Book -->
          <!-- <BookSettings /> -->

          <!-- User -->
          <UserSettings />

          <!-- Notification -->
          <NotificationSettings />

          <!-- Library -->
          <LibraryInformation />
        </div>

        <!-- ================= FOOTER ================= -->
        <div
          class="mt-6 bg-white border border-gray-200
                 rounded-xl px-6 py-4
                 flex flex-col sm:flex-row
                 items-center justify-between gap-4"
        >
          <!-- Last Updated -->
          <div class="flex items-center gap-2 text-sm text-gray-500">
            <i class="bi bi-clock-history"></i>

            <span>
              Last updated:
              <strong class="text-gray-700">
                {{ lastUpdated }}
              </strong>
            </span>
          </div>

          <!-- Reset -->
          <button
            @click="resetSettings"
            class="flex items-center gap-2
                   px-4 py-2
                   border border-gray-300
                   text-gray-600
                   hover:bg-gray-50
                   rounded-lg
                   transition"
          >
            <i class="bi bi-arrow-counterclockwise"></i>

            Reset to Default
          </button>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref } from "vue";

import BorrowingSettings from "@/components/admin/BorrowingSettings.vue";
import FineSettings from "@/components/admin/FineSettings.vue";
// import BookSettings from "@/components/admin/BookSettings.vue";
import UserSettings from "@/components/admin/UserSettings.vue";
import NotificationSettings from "@/components/admin/NotificationSettings.vue";
import LibraryInformation from "@/components/admin/LibraryInformation.vue";

const saving = ref(false);

const lastUpdated = ref("Not saved yet");

/**
 * Save Settings
 *
 * For now, settings are saved locally.
 * Later this can be replaced with API call.
 */
const saveSettings = () => {
  saving.value = true;

  setTimeout(() => {
    const now = new Date();

    lastUpdated.value = now.toLocaleString();

    localStorage.setItem(
      "librarySettingsUpdated",
      lastUpdated.value
    );

    saving.value = false;
  }, 800);
};

/**
 * Reset Settings
 */
const resetSettings = () => {
  const confirmed = window.confirm(
    "Are you sure you want to reset all settings to default?"
  );

  if (!confirmed) return;

  localStorage.removeItem("librarySettingsUpdated");

  lastUpdated.value = "Not saved yet";

  window.location.reload();
};

/**
 * Load last updated time
 */
const storedUpdated = localStorage.getItem(
  "librarySettingsUpdated"
);

if (storedUpdated) {
  lastUpdated.value = storedUpdated;
}
</script>
