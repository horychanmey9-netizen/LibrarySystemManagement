
<template>
  <header
    class="w-full bg-white border-b border-gray-100
           shadow-sm sticky top-0 z-40 py-1"
  >

    <div class="px-4 sm:px-6 lg:px-8 py-2">

      <div class="flex items-center justify-between gap-4">

        <!-- ================= LEFT ================= -->
        <div class="flex items-center gap-3 min-w-0">

          <!-- Mobile Menu -->
          <button
            @click="$emit('toggle-sidebar')"
            class="lg:hidden flex-shrink-0 w-10 h-10
                   rounded-xl flex items-center justify-center
                   text-gray-600 hover:text-blue-600
                   hover:bg-blue-50 transition"
            aria-label="Open menu"
          >

            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="w-6 h-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>

          </button>


          <!-- Welcome -->
          <div class="min-w-0">

            <h1
              class="text-lg sm:text-xl lg:text-xl
                     font-bold text-gray-900 truncate"
            >
              Welcome, John Doe 👋
            </h1>

            <p
              class="text-xs sm:text-sm text-gray-500
                     mt-1 truncate"
            >
              Find and manage your favorite books
            </p>

          </div>

        </div>


        <!-- ================= RIGHT ================= -->
        <div
          class="flex items-center gap-2 sm:gap-4
                 flex-shrink-0"
        >

          <!-- Notification Bell -->
          <NotificationBell />


          <!-- User -->
          <button
            class="flex items-center gap-2 sm:gap-3
                   px-2 py-1.5 rounded-xl
                   hover:bg-gray-50 transition"
          >

            <img
              src="https://i.pravatar.cc/100?img=12"
              alt="John Doe"
              class="w-9 h-9 sm:w-10 sm:h-10
                     rounded-full object-cover
                     border border-gray-200"
            />

            <span
              class="hidden sm:block text-sm
                     font-medium text-gray-800"
            >
              John Doe
            </span>

            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="hidden sm:block w-4 h-4 text-gray-500"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              stroke-width="2"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                d="M19 9l-7 7-7-7"
              />
            </svg>

          </button>

        </div>

      </div>

    </div>

  </header>
</template>


<script setup>
import NotificationBell from "../user/NotificationBell.vue"

defineEmits(["toggle-sidebar"]);
</script>