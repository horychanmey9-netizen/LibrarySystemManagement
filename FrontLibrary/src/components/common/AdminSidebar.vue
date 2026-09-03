<template>

  <aside
    class="sidebar"
    :class="{ 'sidebar-open': isOpen }"
  >

    <!-- =========================
         Logo
    ========================== -->

    <div class="logo-section">

      <div class="logo-icon">
        <img
          :src="logo"
          alt="Library Logo"
          class="logo-image"
        />
      </div>

      <div class="logo-text">

        <h1>
          Library
        </h1>

        <span>
          Management System
        </span>

      </div>


      <!-- Mobile Close -->

      <button
        type="button"
        class="mobile-close"
        title="Close Menu"
        @click="closeSidebar"
      >

        <i class="bi bi-x-lg"></i>

      </button>

    </div>


    <!-- =========================
         Navigation
    ========================== -->

    <nav class="navigation">

      <!-- Main Menu -->

      <RouterLink
        v-for="item in mainMenu"
        :key="item.to"
        :to="item.to"
        class="nav-item"
        active-class="active"
        @click="closeSidebarOnMobile"
      >

        <span class="icon">

          <i
            :class="item.icon"
          ></i>

        </span>

        <span>
          {{ item.label }}
        </span>

      </RouterLink>


      <!-- =========================
           Management
      ========================== -->

      <p class="menu-title">
        MANAGEMENT
      </p>


      <RouterLink
        v-for="item in managementMenu"
        :key="item.to"
        :to="item.to"
        class="nav-item"
        active-class="active"
        @click="closeSidebarOnMobile"
      >

        <span class="icon">

          <i
            :class="item.icon"
          ></i>

        </span>

        <span>
          {{ item.label }}
        </span>

      </RouterLink>

    </nav>


    <!-- =========================
         Bottom Profile
    ========================== -->

    <div class="sidebar-bottom">

      <button
        type="button"
        class="admin-profile"
        title="Admin Profile"
        @click="goToProfile"
      >

        <!-- =========================
             Profile Image
        ========================== -->

        <div class="avatar">

          <img
            v-if="adminImage"
            :src="adminImage"
            :alt="adminName"
            class="avatar-image"
            @error="handleImageError"
          />

          <span v-else>
            {{ getInitial(adminName) }}
          </span>

        </div>


        <!-- =========================
             Admin Information
        ========================== -->

        <div class="admin-info">

          <strong>
            {{ adminName }}
          </strong>

          <span>
            Administrator
          </span>

        </div>

      </button>

    </div>

  </aside>


  <!-- =========================
       Mobile Overlay
  ========================== -->

  <div
    v-if="isOpen"
    class="sidebar-overlay"
    @click="closeSidebar"
  ></div>

</template>


<script setup>

import {
  computed,
  ref,
  onMounted,
  onUnmounted
} from "vue";

import {
  useRouter
} from "vue-router";

import {
  getProfile
} from "../../service/profileservice";

import logo from "../../assets/logo.png"

// ========================================
// ROUTER
// ========================================

const router = useRouter();


// ========================================
// SIDEBAR STATE
// ========================================

const isOpen = ref(false);


// ========================================
// ADMIN PROFILE
// ========================================

const admin = ref({

  name: "",

  email: "",

  image: "",

});


// ========================================
// LOAD ADMIN PROFILE
// ========================================

const loadAdminProfile = async () => {

  try {

    // ====================================
    // GET PROFILE FROM BACKEND
    // ====================================

    const response =
      await getProfile();


    console.log(
      "ADMIN SIDEBAR PROFILE:",
      response
    );


    const data =
      response?.data;


    if (!data) {

      console.warn(
        "No profile data found"
      );

      return;

    }


    // ====================================
    // SET ADMIN PROFILE
    // SAME AS NAVBAR
    // ====================================

    admin.value = {

      name:
        data.name ||
        data.fullName ||
        "Admin",

      email:
        data.email ||
        "",

      image:
        data.image ||
        "",

    };


    console.log(
      "ADMIN SIDEBAR NAME:",
      admin.value.name
    );


    console.log(
      "ADMIN SIDEBAR IMAGE:",
      admin.value.image
    );


    // ====================================
    // UPDATE SESSION STORAGE
    // ====================================

    try {

      const currentUser =
        JSON.parse(
          sessionStorage.getItem(
            "user"
          ) || "{}"
        );


      const updatedUser = {

        ...currentUser,

        name:
          admin.value.name,

        email:
          admin.value.email,

        image:
          admin.value.image,

      };


      sessionStorage.setItem(
        "user",
        JSON.stringify(
          updatedUser
        )
      );


    } catch (sessionError) {

      console.error(
        "Failed to update session user:",
        sessionError
      );

    }

  } catch (error) {

    console.error(
      "Failed to load admin profile:",
      error
    );


    // ====================================
    // FALLBACK TO SESSION STORAGE
    // ====================================

    try {

      const storedUser =
        sessionStorage.getItem(
          "user"
        );


      if (!storedUser) {

        return;

      }


      const user =
        JSON.parse(
          storedUser
        );


      admin.value = {

        name:
          user?.name ||
          user?.fullName ||
          "Admin",

        email:
          user?.email ||
          "",

        image:
          user?.image ||
          "",

      };


    } catch (sessionError) {

      console.error(
        "Failed to load session user:",
        sessionError
      );

    }

  }

};


// ========================================
// ADMIN NAME
// ========================================

const adminName = computed(() => {

  return (
    admin.value.name ||
    "Admin"
  );

});


// ========================================
// ADMIN IMAGE
// ========================================

const adminImage = computed(() => {

  return (
    admin.value.image ||
    ""
  );

});


// ========================================
// GET INITIAL
// ========================================

function getInitial(name) {

  if (!name) {

    return "A";

  }


  return name
    .charAt(0)
    .toUpperCase();

}


// ========================================
// IMAGE ERROR
// ========================================

function handleImageError(event) {

  console.error(
    "Admin sidebar image failed to load:",
    event.target.src
  );


  admin.value.image = "";

}


// ========================================
// TOGGLE SIDEBAR
// ========================================

function toggleSidebar() {

  isOpen.value =
    !isOpen.value;

}


// ========================================
// CLOSE SIDEBAR
// ========================================

function closeSidebar() {

  isOpen.value = false;

}


// ========================================
// CLOSE ON MOBILE
// ========================================

function closeSidebarOnMobile() {

  if (
    window.innerWidth <= 768
  ) {

    isOpen.value = false;

  }

}


// ========================================
// GO TO PROFILE
// ========================================

function goToProfile() {

  router.push(
    "/admin/profile"
  );


  closeSidebar();

}


// ========================================
// PROFILE UPDATED EVENT
// ========================================

async function handleProfileUpdated() {

  console.log(
    "Profile updated → reload Admin Sidebar"
  );


  await loadAdminProfile();

}


// ========================================
// TOGGLE EVENT
// ========================================

function handleToggleSidebar() {

  toggleSidebar();

}


// ========================================
// MOUNT
// ========================================

onMounted(() => {

  // ====================================
  // LOAD PROFILE
  // ====================================

  loadAdminProfile();


  // ====================================
  // SIDEBAR TOGGLE
  // ====================================

  window.addEventListener(
    "toggle-admin-sidebar",
    handleToggleSidebar
  );


  // ====================================
  // PROFILE UPDATED
  // ====================================

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
    "toggle-admin-sidebar",
    handleToggleSidebar
  );


  window.removeEventListener(
    "profile-updated",
    handleProfileUpdated
  );

});


// ========================================
// EXPOSE
// ========================================

defineExpose({

  toggleSidebar,

  closeSidebar

});


// ========================================
// MAIN MENU
// ========================================

const mainMenu = [

  {
    label: "Dashboard",

    to: "/admin/dashboard",

    icon: "bi bi-grid"
  },


  {
    label: "Books",

    to: "/admin/books",

    icon: "bi bi-book"
  },


  {
    label: "Categories",

    to: "/admin/categories",

    icon: "bi bi-tags"
  },


  {
    label: "Borrowing",

    to: "/admin/borrowing",

    icon: "bi bi-journal-arrow-down"
  },

  {
    label: "Borrower",

    to: "/admin/borrower",

    icon: "bi bi-journal-arrow-down"
  },


  {
    label: "Returns",

    to: "/admin/returns",

    icon: "bi bi-arrow-return-left"
  }

];


// ========================================
// MANAGEMENT MENU
// ========================================

const managementMenu = [

  {
    label: "Users",

    to: "/admin/users",

    icon: "bi bi-people"
  },

  {
    label: "Fines",

    to: "/admin/fines",

    icon: "bi bi-cash-coin"
  },


  {
    label: "Notifications",

    to: "/admin/notifications",

    icon: "bi bi-bell"
  }

];

</script>


<style scoped>

/* ========================================
   SIDEBAR
======================================== */

.sidebar {

  width: 250px;

  height: 100vh;

  position: fixed;

  top: 0;

  left: 0;

  background: #ffffff;

  border-right: 1px solid #e5e7eb;

  display: flex;

  flex-direction: column;

  box-sizing: border-box;

  overflow: hidden;

  z-index: 1100;

}


/* ========================================
   LOGO
======================================== */

.logo-section {

  height: 75px;

  min-height: 75px;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 0 22px;

  border-bottom: 1px solid #f0f0f0;

  box-sizing: border-box;

}


.logo-icon {
  width: 42px;
  height: 42px;
  min-width: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  overflow: hidden;
  background: transparent;
}

.logo-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}


.logo-section h1 {

  margin: 0;

  font-size: 18px;

  font-weight: 700;

  color: #172033;

}


.logo-section span {

  display: block;

  margin-top: 2px;

  font-size: 10px;

  color: #8a92a3;

}


/* ========================================
   NAVIGATION
======================================== */

.navigation {

  flex: 1;

  padding: 20px 14px;

  overflow-y: auto;

  overflow-x: hidden;

  box-sizing: border-box;

}


.navigation::-webkit-scrollbar {

  width: 5px;

}


.navigation::-webkit-scrollbar-track {

  background: transparent;

}


.navigation::-webkit-scrollbar-thumb {

  background: #d1d5db;

  border-radius: 10px;

}


/* ========================================
   MENU TITLE
======================================== */

.menu-title {

  margin: 14px 10px 8px;

  font-size: 10px;

  font-weight: 700;

  letter-spacing: 1px;

  color: #9aa1b1;

}


/* ========================================
   NAV ITEM
======================================== */

.nav-item {

  display: flex;

  align-items: center;

  gap: 13px;

  width: 100%;

  box-sizing: border-box;

  padding: 11px 13px;

  margin-bottom: 4px;

  border-radius: 8px;

  text-decoration: none;

  color: #667085;

  font-size: 14px;

  font-weight: 500;

  transition: all 0.2s ease;

}


.nav-item:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


.nav-item.active {

  background: #eeeaff;

  color: #5b3df5;

  font-weight: 600;

}


/* ========================================
   ICON
======================================== */

.icon {

  width: 22px;

  min-width: 22px;

  display: flex;

  justify-content: center;

  align-items: center;

  font-size: 17px;

}


/* ========================================
   SIDEBAR BOTTOM
======================================== */

.sidebar-bottom {

  padding: 15px;

  background: #ffffff;

  border-top: 1px solid #eeeeee;

  flex-shrink: 0;

  box-sizing: border-box;

}


/* ========================================
   ADMIN PROFILE
======================================== */

.admin-profile {

  width: 100%;

  display: flex;

  align-items: center;

  gap: 10px;

  padding: 8px;

  border: none;

  border-radius: 8px;

  background: transparent;

  cursor: pointer;

  text-align: left;

  transition: 0.2s;

}


.admin-profile:hover {

  background: #f5f3ff;

}


/* ========================================
   AVATAR
======================================== */

.avatar {

  width: 38px;

  height: 38px;

  min-width: 38px;

  border-radius: 50%;

  background: #5b3df5;

  color: white;

  font-weight: 600;

  display: flex;

  align-items: center;

  justify-content: center;

  overflow: hidden;

}


/* ========================================
   AVATAR IMAGE
======================================== */

.avatar-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


/* ========================================
   ADMIN INFO
======================================== */

.admin-info {

  overflow: hidden;

}


.admin-info strong {

  display: block;

  font-size: 13px;

  color: #172033;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.admin-info span {

  display: block;

  margin-top: 2px;

  font-size: 11px;

  color: #8a92a3;

}


/* ========================================
   MOBILE CLOSE
======================================== */

.mobile-close {

  display: none;

  margin-left: auto;

  width: 36px;

  height: 36px;

  border: none;

  background: transparent;

  color: #667085;

  font-size: 18px;

  border-radius: 8px;

  cursor: pointer;

}


.mobile-close:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


/* ========================================
   OVERLAY
======================================== */

.sidebar-overlay {

  display: none;

  position: fixed;

  inset: 0;

  background: rgba(0, 0, 0, 0.35);

  z-index: 1050;

}


/* ========================================
   TABLET
======================================== */

@media (max-width: 1024px) {

  .sidebar {

    width: 230px;

  }


  .logo-section {

    padding: 0 18px;

  }


  .navigation {

    padding: 18px 12px;

  }

}


/* ========================================
   MOBILE
======================================== */

@media (max-width: 768px) {

  .sidebar {

    width: 270px;

    max-width: 85vw;

    position: fixed;

    top: 0;

    left: 0;

    right: auto;

    transform: translateX(-100%);

    transition:
      transform 0.3s ease;

    box-shadow:
      8px 0 25px
      rgba(0, 0, 0, 0.08);

  }


  .sidebar.sidebar-open {

    transform: translateX(0);

  }


  .logo-section {

    height: 70px;

    min-height: 70px;

    padding: 0 18px;

  }


  .logo-text {

    display: block;

  }


  .mobile-close {

    display: flex;

    align-items: center;

    justify-content: center;

  }


  .navigation {

    padding: 18px 12px;

  }


  .nav-item {

    justify-content: flex-start;

    padding: 12px 13px;

    font-size: 14px;

  }


  .nav-item span:last-child {

    display: block;

  }


  .menu-title {

    display: block;

  }


  .admin-profile {

    justify-content: flex-start;

    padding: 8px;

  }


  .admin-info {

    display: block;

  }


  .sidebar-overlay {

    display: block;

  }

}


/* ========================================
   SMALL MOBILE
======================================== */

@media (max-width: 480px) {

  .sidebar {

    width: 260px;

    max-width: 88vw;

  }


  .logo-section {

    padding: 0 15px;

  }


  .navigation {

    padding: 15px 10px;

  }


  .nav-item {

    padding: 11px 12px;

  }


  .sidebar-bottom {

    padding: 12px;

  }

}

</style>
