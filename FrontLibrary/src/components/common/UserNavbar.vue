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
              Welcome, {{ userName }}
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


          <!-- ================= USER ================= -->
          <div class="relative">

            <!-- User Button -->
            <button
              @click="showProfile = !showProfile"
              class="flex items-center gap-2 sm:gap-3
                     px-2 py-1.5 rounded-xl
                     hover:bg-gray-50 transition"
            >

              <!-- Avatar -->
              <div
                class="w-9 h-9 sm:w-10 sm:h-10
                       rounded-full overflow-hidden
                       border border-gray-200
                       flex items-center justify-center
                       bg-gradient-to-br
                       from-indigo-100 to-purple-100
                       text-indigo-700 font-bold"
              >

                <!-- Real Avatar -->
                <img
                  v-if="userAvatar"
                  :src="userAvatar"
                  :alt="userName"
                  class="w-full h-full object-cover"
                />

                <!-- Initial -->
                <span v-else>
                  {{ userInitials }}
                </span>

              </div>


              <!-- User Name -->
              <span
                class="hidden sm:block text-sm
                       font-medium text-gray-800"
              >
                {{ userName }}
              </span>


              <!-- Arrow -->
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


            <!-- ================= PROFILE DROPDOWN ================= -->
            <div
              v-if="showProfile"
              class="absolute right-0 mt-3
                     w-72 bg-white
                     rounded-2xl shadow-xl
                     border border-gray-100
                     overflow-hidden z-50"
            >

              <!-- User Info -->
              <div
                class="p-4 bg-gradient-to-r
                       from-indigo-50 to-purple-50"
              >

                <div class="flex items-center gap-3">

                  <!-- Avatar -->
                  <div
                    class="w-12 h-12 rounded-full
                           overflow-hidden
                           flex-shrink-0
                           bg-gradient-to-br
                           from-indigo-100 to-purple-100
                           flex items-center justify-center
                           text-indigo-700 font-bold"
                  >

                    <img
                      v-if="userAvatar"
                      :src="userAvatar"
                      :alt="userName"
                      class="w-full h-full object-cover"
                    />

                    <span v-else>
                      {{ userInitials }}
                    </span>

                  </div>


                  <!-- Name / Email -->
                  <div class="min-w-0">

                    <p
                      class="font-semibold text-gray-800
                             truncate"
                    >
                      {{ userName }}
                    </p>

                    <p
                      class="text-xs text-gray-500
                             truncate mt-1"
                    >
                      {{ userEmail }}
                    </p>

                  </div>

                </div>

              </div>


              <!-- Profile Link -->
              <div class="p-2">

                <router-link
                  to="/user/profile"
                  @click="showProfile = false"
                  class="flex items-center gap-3
                         px-3 py-3 rounded-xl
                         text-gray-600
                         hover:bg-indigo-50
                         hover:text-indigo-600
                         transition"
                >

                  <div
                    class="w-9 h-9 rounded-lg
                           bg-indigo-50
                           flex items-center justify-center
                           text-indigo-600"
                  >
                    <i class="bi bi-person-fill"></i>
                  </div>

                  <div>
                    <p class="text-sm font-medium">
                      My Profile
                    </p>

                    <p class="text-xs text-gray-400">
                      View your profile
                    </p>
                  </div>

                </router-link>

              </div>

            </div>

          </div>

        </div>

      </div>

    </div>

  </header>
</template>


<script setup>
import { ref, computed, onMounted, onUnmounted } from "vue";
import NotificationBell from "../user/NotificationBell.vue";
import { getProfile } from "@/service/profileservice";

// ========================================
// EMIT
// ========================================
defineEmits(["toggle-sidebar"]);

// ========================================
// PROFILE DROPDOWN
// ========================================
const showProfile = ref(false);

// ========================================
// PROFILE IMAGE FROM BACKEND
// ========================================
const profileImage = ref("");

// ========================================
// GET USER FROM SESSION
// ========================================
const storedUser = sessionStorage.getItem("user");

let user = null;

try {
  user = storedUser
    ? JSON.parse(storedUser)
    : null;
} catch (error) {
  console.error("Invalid user data:", error);
  user = null;
}

// ========================================
// USER NAME
// ========================================
const userName = computed(() => {
  return (
    user?.name ||
    user?.fullName ||
    "User"
  );
});

// ========================================
// USER EMAIL
// ========================================
const userEmail = computed(() => {
  return user?.email || "";
});

// ========================================
// LOAD PROFILE FROM BACKEND
// ========================================
const loadProfile = async () => {
  try {
    const response = await getProfile();

    console.log("NAVBAR PROFILE RESPONSE:", response);

    const data = response?.data;

    if (!data) {
      profileImage.value = "";
      return;
    }

    // ========================================
    // GET IMAGE
    // ========================================
    if (data.image) {

      // Backend returns full URL
      if (data.image.startsWith("http")) {
        profileImage.value = data.image;
      }

      // Backend returns /uploads/...
      else {
        profileImage.value =
          `http://localhost:8080${data.image}`;
      }

      console.log(
        "NAVBAR PROFILE IMAGE:",
        profileImage.value
      );
    }

  } catch (error) {
    console.error(
      "Failed to load profile in Navbar:",
      error
    );

    // fallback to sessionStorage
    profileImage.value =
      user?.avatar ||
      user?.image ||
      "";
  }
};

// ========================================
// USER AVATAR
// ========================================
const userAvatar = computed(() => {

  // Backend profile image first
  if (profileImage.value) {
    return profileImage.value;
  }

  // Session fallback
  return (
    user?.avatar ||
    user?.image ||
    ""
  );
});

// ========================================
// USER INITIALS
// ========================================
const userInitials = computed(() => {

  const name = userName.value;

  if (!name || name === "User") {
    return "US";
  }

  const names = name
    .trim()
    .split(/\s+/);

  if (names.length === 1) {
    return names[0]
      .substring(0, 2)
      .toUpperCase();
  }

  return (
    names[0].charAt(0) +
    names[names.length - 1].charAt(0)
  ).toUpperCase();
});

// ========================================
// PROFILE UPDATED EVENT
// ========================================
const handleProfileUpdated = () => {
  console.log(
    "Profile updated → Reload Navbar profile"
  );

  loadProfile();
};

// ========================================
// MOUNT
// ========================================
onMounted(() => {

  // Load profile image every time Navbar starts
  loadProfile();

  // Listen for profile update
  window.addEventListener(
    "profile-updated",
    handleProfileUpdated
  );
});

// ========================================
// UNMOUNT
// ========================================
onUnmounted(() => {

  window.removeEventListener(
    "profile-updated",
    handleProfileUpdated
  );

});
</script>