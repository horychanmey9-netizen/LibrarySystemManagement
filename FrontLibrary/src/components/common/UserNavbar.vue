<template>

  <header class="navbar">

    <div class="navbar-inner">

      <!-- ========================================
           LEFT
      ======================================== -->

      <div class="navbar-left">

        <!-- Mobile Menu Button -->

        <button
          type="button"
          class="mobile-menu-btn"
          @click="toggleSidebar"
          aria-label="Open menu"
        >

          <i class="bi bi-list"></i>

        </button>


        <!-- Welcome -->

        <div class="welcome-section">

          <h1>
            Welcome, {{ userName }}
          </h1>

          <p>
            Find and manage your favorite books
          </p>

        </div>

      </div>


      <!-- ========================================
           RIGHT
      ======================================== -->

      <div class="navbar-right">

        <button
          type="button"
          class="user-button"
          @click="goToProfile"
        >

          <!-- Avatar -->

          <div class="navbar-avatar">

            <img
              v-if="userAvatar"
              :src="userAvatar"
              :alt="userName"
              class="avatar-image"
              @error="handleAvatarError"
            />

            <span v-else>
              {{ userInitials }}
            </span>

          </div>


          <!-- Name -->

          <span class="navbar-user-name">
            {{ userName }}
          </span>

        </button>

      </div>

    </div>

  </header>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted,
  onUnmounted
} from "vue";

import { useRouter } from "vue-router";

import {
  getProfile
} from "@/service/profileservice";


// ========================================
// ROUTER
// ========================================

const router = useRouter();


// ========================================
// EMIT
// ========================================

const emit = defineEmits([
  "toggle-sidebar"
]);


// ========================================
// PROFILE IMAGE
// ========================================

const profileImage = ref("");


// ========================================
// USER
// ========================================

const storedUser =
  sessionStorage.getItem("user");

let user = null;

try {

  user = storedUser
    ? JSON.parse(storedUser)
    : null;

} catch (error) {

  console.error(
    "Invalid user data:",
    error
  );

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

  return (

    user?.email ||

    ""

  );

});


// ========================================
// LOAD PROFILE
// ========================================

const loadProfile = async () => {

  try {

    const response =
      await getProfile();


    console.log(
      "NAVBAR PROFILE RESPONSE:",
      response
    );


    const data =
      response?.data;


    if (!data) {

      return;

    }


    // ====================================
    // IMAGE
    // ====================================

    if (data.image) {

      if (
        data.image.startsWith("http")
      ) {

        profileImage.value =
          data.image;

      } else {

        profileImage.value =
          `http://localhost:8080${data.image}`;

      }

    }

  } catch (error) {

    console.error(
      "Failed to load profile:",
      error
    );


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

  if (
    profileImage.value
  ) {

    return profileImage.value;

  }


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

  const name =
    userName.value;


  if (
    !name ||
    name === "User"
  ) {

    return "US";

  }


  const names =
    name
      .trim()
      .split(/\s+/);


  if (
    names.length === 1
  ) {

    return names[0]
      .substring(0, 2)
      .toUpperCase();

  }


  return (

    names[0].charAt(0) +

    names[
      names.length - 1
    ].charAt(0)

  ).toUpperCase();

});


// ========================================
// GO TO PROFILE
// ========================================

function goToProfile() {

  router.push("/user/profile");

}


// ========================================
// TOGGLE SIDEBAR
// ========================================

function toggleSidebar() {

  emit(
    "toggle-sidebar"
  );

}


// ========================================
// AVATAR ERROR
// ========================================

function handleAvatarError(event) {

  console.error(
    "Navbar avatar failed:",
    event.target.src
  );

  profileImage.value = "";

}


// ========================================
// PROFILE UPDATED
// ========================================

const handleProfileUpdated = () => {

  console.log(
    "Profile updated → Reload Navbar"
  );

  loadProfile();

};


// ========================================
// MOUNTED
// ========================================

onMounted(() => {

  loadProfile();


  window.addEventListener(
    "profile-updated",
    handleProfileUpdated
  );

});


// ========================================
// UNMOUNTED
// ========================================

onUnmounted(() => {

  window.removeEventListener(
    "profile-updated",
    handleProfileUpdated
  );

});

</script>


<style scoped>

/* =====================================================
   NAVBAR
===================================================== */

.navbar {

  width: 100%;

  height: 75px;

  min-height: 75px;

  background: #ffffff;

  border-bottom: 1px solid #e5e7eb;

  box-sizing: border-box;

  position: sticky;

  top: 0;

  z-index: 900;

}


/* =====================================================
   NAVBAR INNER
===================================================== */

.navbar-inner {

  width: 100%;

  height: 100%;

  padding: 0 24px;

  display: flex;

  align-items: center;

  justify-content: space-between;

  box-sizing: border-box;

}


/* =====================================================
   LEFT
===================================================== */

.navbar-left {

  min-width: 0;

  display: flex;

  align-items: center;

  gap: 14px;

}


/* =====================================================
   WELCOME
===================================================== */

.welcome-section {

  min-width: 0;

}


.welcome-section h1 {

  margin: 0;

  padding: 0;

  color: #172033;

  font-size: 20px;

  line-height: 26px;

  font-weight: 700;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.welcome-section p {

  margin: 2px 0 0;

  color: #8a92a3;

  font-size: 13px;

  line-height: 18px;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


/* =====================================================
   MOBILE MENU
===================================================== */

.mobile-menu-btn {

  display: none;

  width: 40px;

  height: 40px;

  border: none;

  border-radius: 9px;

  background: transparent;

  color: #667085;

  font-size: 22px;

  align-items: center;

  justify-content: center;

  cursor: pointer;

  transition:
    background 0.2s ease,
    color 0.2s ease;

}


.mobile-menu-btn:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


/* =====================================================
   RIGHT
===================================================== */

.navbar-right {

  display: flex;

  align-items: center;

  gap: 12px;

  flex-shrink: 0;

}


/* =====================================================
   USER BUTTON
===================================================== */

.user-button {

  height: 48px;

  display: flex;

  align-items: center;

  gap: 10px;

  padding: 4px 8px 4px 5px;

  border: none;

  border-radius: 10px;

  background: transparent;

  cursor: pointer;

  transition:
    background 0.2s ease;

}


.user-button:hover {

  background: #f8f9fc;

}


/* =====================================================
   NAVBAR AVATAR
===================================================== */

.navbar-avatar {

  width: 38px;

  height: 38px;

  min-width: 38px;

  border-radius: 50%;

  overflow: hidden;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #eeeaff;

  color: #5b3df5;

  font-size: 13px;

  font-weight: 700;

  border: 1px solid #e6e1ff;

}


/* =====================================================
   AVATAR IMAGE
===================================================== */

.avatar-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


/* =====================================================
   USER NAME
===================================================== */

.navbar-user-name {

  max-width: 150px;

  color: #172033;

  font-size: 14px;

  font-weight: 600;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


/* =====================================================
   ARROW
===================================================== */

.user-button > i {

  color: #8a92a3;

  font-size: 11px;

  transition:
    transform 0.2s ease;

}


.user-button:hover > i {

  color: #5b3df5;

}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 1024px) {

  .navbar-inner {

    padding: 0 18px;

  }


  .navbar-user-name {

    max-width: 120px;

  }

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 768px) {

  .navbar {

    height: 70px;

    min-height: 70px;

  }


  .navbar-inner {

    padding: 0 15px;

  }


  .mobile-menu-btn {

    display: flex;

  }


  .welcome-section h1 {

    font-size: 17px;

    line-height: 22px;

  }


  .welcome-section p {

    font-size: 11px;

    line-height: 16px;

  }


  .navbar-user-name {

    display: none;

  }


  .user-button {

    padding-right: 4px;

  }


  .user-button > i {

    display: none;

  }


  .navbar-right {

    gap: 6px;

  }

}


/* =====================================================
   SMALL MOBILE
===================================================== */

@media (max-width: 480px) {

  .navbar-inner {

    padding: 0 10px;

  }


  .navbar-left {

    gap: 8px;

  }


  .welcome-section h1 {

    max-width: 180px;

    font-size: 15px;

  }


  .welcome-section p {

    max-width: 180px;

    font-size: 10px;

  }


  .navbar-avatar {

    width: 36px;

    height: 36px;

    min-width: 36px;

  }

}

</style>