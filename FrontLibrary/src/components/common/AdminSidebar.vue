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
        <i class="bi bi-book"></i>
      </div>

      <div class="logo-text">
        <h1>Library</h1>
        <span>Management System</span>
      </div>

      <!-- Close button Mobile -->
      <button
        class="mobile-close"
        @click="closeSidebar"
      >
        <i class="bi bi-x-lg"></i>
      </button>

    </div>


    <!-- =========================
         Navigation
    ========================== -->
    <nav class="navigation">

      <RouterLink
        v-for="item in mainMenu"
        :key="item.to"
        :to="item.to"
        class="nav-item"
        active-class="active"
        @click="closeSidebarOnMobile"
      >
        <span class="icon">
          <i :class="item.icon"></i>
        </span>

        <span>{{ item.label }}</span>
      </RouterLink>


      <!-- Management -->
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
          <i :class="item.icon"></i>
        </span>

        <span>{{ item.label }}</span>
      </RouterLink>

    </nav>


    <!-- =========================
         Bottom Profile
    ========================== -->
    <div class="sidebar-bottom">

      <button
        class="admin-profile"
        @click="goToProfile"
      >

        <!-- Avatar -->
        <div class="avatar">
          {{ getInitial(adminName) }}
        </div>


        <!-- Admin Information -->
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

import { computed, ref } from "vue";
import { useRouter } from "vue-router";


import { onMounted, onUnmounted } from "vue";


function handleToggleSidebar() {
  toggleSidebar();
}


onMounted(() => {

  window.addEventListener(
    "toggle-admin-sidebar",
    handleToggleSidebar
  );

});


onUnmounted(() => {

  window.removeEventListener(
    "toggle-admin-sidebar",
    handleToggleSidebar
  );

});


/* =========================
   Router
========================= */

const router = useRouter();


/* =========================
   Sidebar State
========================= */

const isOpen = ref(false);


/*
  Expose method to parent
  Example:
  ref="sidebar"
  sidebar.value?.toggleSidebar()
*/

function toggleSidebar() {
  isOpen.value = !isOpen.value;
}

function closeSidebar() {
  isOpen.value = false;
}

function closeSidebarOnMobile() {

  if (window.innerWidth <= 768) {
    isOpen.value = false;
  }

}


/* =========================
   Expose
========================= */

defineExpose({
  toggleSidebar,
  closeSidebar
});


/* =========================
   Main Menu
========================= */

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
    label: "Borrowings",
    to: "/admin/borrowings",
    icon: "bi bi-journal-arrow-down"
  },

  {
    label: "Returns",
    to: "/admin/returns",
    icon: "bi bi-arrow-return-left"
  }

];


/* =========================
   Management Menu
========================= */

const managementMenu = [

  {
    label: "Users",
    to: "/admin/users",
    icon: "bi bi-people"
  },

  {
    label: "Reports",
    to: "/admin/reports",
    icon: "bi bi-bar-chart"
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


/* =========================
   Admin Name
========================= */

const adminName = computed(() => {

  return (
    localStorage.getItem("adminName") ||
    "Admin"
  );

});


/* =========================
   Get Initial
========================= */

function getInitial(name) {

  if (!name) {
    return "A";
  }

  return name
    .charAt(0)
    .toUpperCase();

}


/* =========================
   Go To Profile
========================= */

function goToProfile() {

  router.push("/admin/profile");

  closeSidebar();

}

</script>


<style scoped>

/* ========================================
   Sidebar
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
   Logo
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

  background: #5b3df5;

  color: white;

  border-radius: 10px;

  font-size: 21px;

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
   Navigation
======================================== */

.navigation {

  flex: 1;

  padding: 20px 14px;

  overflow-y: auto;
  overflow-x: hidden;

  box-sizing: border-box;

}


/* Scrollbar */

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
   Menu Title
======================================== */

.menu-title {

  margin: 14px 10px 8px;

  font-size: 10px;

  font-weight: 700;

  letter-spacing: 1px;

  color: #9aa1b1;

}


/* ========================================
   Navigation Item
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
   Icon
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
   Bottom Profile
======================================== */

.sidebar-bottom {

  padding: 15px;

  background: #ffffff;

  border-top: 1px solid #eeeeee;

  flex-shrink: 0;

  box-sizing: border-box;

}


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
   Avatar
======================================== */

.avatar {

  width: 38px;
  height: 38px;

  min-width: 38px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background: #5b3df5;

  color: white;

  font-weight: 600;

}


/* ========================================
   Admin Info
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
   Mobile Close Button
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
   Overlay
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

    transition: transform 0.3s ease;

    box-shadow: 8px 0 25px rgba(0, 0, 0, 0.08);
  }

  .sidebar.sidebar-open {
    transform: translateX(0);
  }




  /* Open Sidebar */

  .sidebar.sidebar-open {

    transform: translateX(0);

  }


  /* Logo */

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


  /* Navigation */

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


  /* Profile */

  .admin-profile {

    justify-content: flex-start;

    padding: 8px;

  }


  .admin-info {

    display: block;

  }


  /* Overlay */

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