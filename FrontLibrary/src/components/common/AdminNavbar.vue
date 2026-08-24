<template>
  <header class="admin-navbar">

    <!-- =========================
         Left Side
    ========================== -->

    <div class="navbar-left">

      <!-- Mobile Menu -->
      <button
        type="button"
        class="mobile-menu-btn"
        title="Open Menu"
        @click="openSidebar"
      >
        <i class="bi bi-list"></i>
      </button>


      <!-- Welcome -->
      <div class="welcome-text">

        <h3>
          Welcome, {{ adminName }}
        </h3>

        <p>
          Have a great day!
        </p>

      </div>

    </div>


    <!-- =========================
         Right Side
    ========================== -->

    <div class="navbar-right">

      <!-- Notification -->
      <button
        type="button"
        class="notification-btn"
        title="Notifications"
        @click="goToNotifications"
      >
        <i class="bi bi-bell"></i>
      </button>


      <!-- Admin Profile -->
      <button
        type="button"
        class="admin-profile"
        title="Admin Profile"
        @click="goToProfile"
      >

        <div class="profile-avatar">
          {{ getInitial(adminName) }}
        </div>

        <div class="profile-info">

          <h4>
            {{ adminName }}
          </h4>

          <span>
            Administrator
          </span>

        </div>

        <i class="bi bi-chevron-down profile-arrow"></i>

      </button>

    </div>

  </header>
</template>


<script setup>

import { computed } from "vue";
import { useRouter } from "vue-router";


/* =========================
   Router
========================= */

const router = useRouter();


/* =========================
   Admin Name
========================= */

const adminName = computed(() => {

  return (
    localStorage.getItem("adminName")
    || "Admin"
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
   Open Sidebar
========================= */

function openSidebar() {

  window.dispatchEvent(
    new CustomEvent("toggle-admin-sidebar")
  );

}


/* =========================
   Go To Notifications
========================= */

function goToNotifications() {

  router.push("/admin/notifications");

}


/* =========================
   Go To Profile
========================= */

function goToProfile() {

  router.push("/admin/profile");

}

</script>


<style scoped>

/* ========================================
   NAVBAR
======================================== */

.admin-navbar {

  position: fixed;

  top: 0;
  left: 250px;
  right: 0;

  height: 75px;

  background: #ffffff;

  border-bottom: 1px solid #e5e7eb;

  display: flex;

  align-items: center;

  justify-content: space-between;

  padding: 0 28px;

  z-index: 1000;

  box-sizing: border-box;

}


/* ========================================
   LEFT SIDE
======================================== */

.navbar-left {

  display: flex;

  align-items: center;

  gap: 15px;

  min-width: 0;

}


/* ========================================
   MOBILE MENU
======================================== */

.mobile-menu-btn {

  display: none;

  width: 40px;
  height: 40px;

  border: none;

  border-radius: 8px;

  background: #f5f3ff;

  color: #5b3df5;

  font-size: 22px;

  align-items: center;
  justify-content: center;

  cursor: pointer;

  flex-shrink: 0;

  transition: 0.2s;

}


.mobile-menu-btn:hover {

  background: #eeeaff;

}


/* ========================================
   WELCOME
======================================== */

.welcome-text {

  display: flex;

  flex-direction: column;

  min-width: 0;

}


.welcome-text h3 {

  margin: 0;

  color: #111827;

  font-size: 18px;

  font-weight: 600;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.welcome-text p {

  margin: 3px 0 0;

  color: #6b7280;

  font-size: 12px;

}


/* ========================================
   RIGHT SIDE
======================================== */

.navbar-right {

  display: flex;

  align-items: center;

  gap: 18px;

  flex-shrink: 0;

}


/* ========================================
   NOTIFICATION
======================================== */

.notification-btn {

  width: 42px;

  height: 42px;

  border: none;

  border-radius: 8px;

  background: transparent;

  color: #4b5563;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  transition: 0.2s;

  flex-shrink: 0;

}


.notification-btn:hover {

  background: #f3f4f6;

  color: #5b3df5;

}


.notification-btn i {

  font-size: 20px;

}


/* ========================================
   ADMIN PROFILE
======================================== */

.admin-profile {

  display: flex;

  align-items: center;

  gap: 10px;

  padding: 6px 10px;

  border: none;

  border-radius: 8px;

  background: transparent;

  cursor: pointer;

  transition: 0.2s;

  text-align: left;

  flex-shrink: 0;

}


.admin-profile:hover {

  background: #f3f4f6;

}


/* ========================================
   AVATAR
======================================== */

.profile-avatar {

  width: 38px;

  height: 38px;

  border-radius: 50%;

  background: #5b3df5;

  color: #ffffff;

  font-weight: 600;

  display: flex;

  align-items: center;

  justify-content: center;

  flex-shrink: 0;

}


/* ========================================
   PROFILE INFO
======================================== */

.profile-info {

  display: flex;

  flex-direction: column;

  min-width: 100px;

}


.profile-info h4 {

  margin: 0;

  font-size: 14px;

  color: #111827;

  font-weight: 600;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.profile-info span {

  font-size: 12px;

  color: #6b7280;

  margin-top: 2px;

}


/* ========================================
   PROFILE ARROW
======================================== */

.profile-arrow {

  font-size: 14px;

  color: #6b7280;

  margin-left: 3px;

}


/* ========================================
   TABLET
======================================== */

@media (max-width: 1024px) {

  .admin-navbar {

    left: 230px;

    padding: 0 20px;

  }

}


/* ========================================
   MOBILE
======================================== */

@media (max-width: 768px) {

  .admin-navbar {

    left: 0;

    height: 65px;

    padding: 0 15px;

  }


  /* Show Hamburger */

  .mobile-menu-btn {

    display: flex;

  }


  /* Welcome */

  .welcome-text h3 {

    font-size: 16px;

  }


  .welcome-text p {

    display: none;

  }


  /* Right */

  .navbar-right {

    gap: 6px;

  }


  /* Notification */

  .notification-btn {

    width: 40px;

    height: 40px;

  }


  /* Hide profile text */

  .profile-info {

    display: none;

  }


  /* Hide arrow */

  .profile-arrow {

    display: none;

  }


  .admin-profile {

    padding: 5px;

  }

}


/* ========================================
   SMALL MOBILE
======================================== */

@media (max-width: 480px) {

  .admin-navbar {

    padding: 0 10px;

  }


  .navbar-left {

    gap: 8px;

    min-width: 0;

  }


  .welcome-text {

    max-width: 140px;

  }


  .welcome-text h3 {

    font-size: 14px;

  }


  .mobile-menu-btn {

    width: 38px;

    height: 38px;

    font-size: 20px;

  }


  .notification-btn {

    width: 36px;

    height: 36px;

  }


  .notification-btn i {

    font-size: 18px;

  }


  .profile-avatar {

    width: 35px;

    height: 35px;

    font-size: 13px;

  }

}


/* ========================================
   VERY SMALL MOBILE
======================================== */

@media (max-width: 360px) {

  .welcome-text {

    max-width: 100px;

  }


  .welcome-text h3 {

    font-size: 13px;

  }


  .navbar-right {

    gap: 2px;

  }

}

</style>