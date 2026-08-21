<template>
  <aside class="sidebar">

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

    </div>


    <!-- =========================
         Navigation
    ========================== -->
    <nav class="navigation">

      <!-- Main Menu -->
      <p class="menu-title">
        MAIN MENU
      </p>

      <RouterLink
        v-for="item in mainMenu"
        :key="item.to"
        :to="item.to"
        class="nav-item"
        active-class="active"
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
</template>


<script setup>

import { computed } from "vue";
import { useRouter } from "vue-router";


/* =========================
   Router
========================= */

const router = useRouter();


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
    label: "Roles",
    to: "/admin/roles",
    icon: "bi bi-shield-lock"
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

}

</script>


<style scoped>

/* =========================
   Sidebar
========================= */

.sidebar {

  width: 250px;
  height: 100vh;

  position: fixed;
  top: 0;
  left: 0;

  flex-shrink: 0;

  background: #ffffff;

  border-right: 1px solid #e5e7eb;

  display: flex;
  flex-direction: column;

  box-sizing: border-box;

  overflow: hidden;

  z-index: 1100;

}


/* =========================
   Logo
========================= */

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


/* =========================
   Navigation
========================= */

.navigation {

  flex: 1;

  padding: 20px 14px;

  overflow-y: auto;
  overflow-x: hidden;

  box-sizing: border-box;

}


/* =========================
   Scrollbar
========================= */

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


/* =========================
   Menu Title
========================= */

.menu-title {

  margin: 14px 10px 8px;

  font-size: 10px;

  font-weight: 700;

  letter-spacing: 1px;

  color: #9aa1b1;

}


/* =========================
   Navigation Item
========================= */

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


/* =========================
   Icon
========================= */

.icon {

  width: 22px;

  min-width: 22px;

  display: flex;

  justify-content: center;

  align-items: center;

  font-size: 17px;

}


/* =========================
   Active
========================= */

.nav-item.active {

  background: #eeeaff;

  color: #5b3df5;

  font-weight: 600;

}


.nav-item.active .icon {

  color: #5b3df5;

}


/* =========================
   Bottom Profile
========================= */

.sidebar-bottom {

  padding: 15px;

  background: #ffffff;

  border-top: 1px solid #eeeeee;

  flex-shrink: 0;

  box-sizing: border-box;

}


/* =========================
   Admin Profile
========================= */

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


/* =========================
   Avatar
========================= */

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


/* =========================
   Admin Info
========================= */

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


/* =========================
   Responsive
========================= */

@media (max-width: 768px) {

  .sidebar {

    width: 75px;

  }


  .logo-section {

    justify-content: center;

    padding: 0;

  }


  .logo-text {

    display: none;

  }


  .navigation {

    padding: 20px 10px;

  }


  .nav-item {

    justify-content: center;

    padding: 12px;

  }


  .nav-item span:last-child {

    display: none;

  }


  .menu-title {

    display: none;

  }


  .admin-profile {

    justify-content: center;

    padding: 8px 0;

  }


  .admin-info {

    display: none;

  }

}

</style>