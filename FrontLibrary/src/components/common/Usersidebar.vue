<template>

  <aside
    class="sidebar"
    :class="{ 'sidebar-open': sidebarOpen }"
  >

    <!-- ========================================
         LOGO
    ======================================== -->

    <div class="logo-section">

      <div class="logo-icon">

        <img
          src="/logo1.png"
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


    <!-- ========================================
         NAVIGATION
    ======================================== -->

    <nav class="navigation">

      <RouterLink
        v-for="item in menuItems"
        :key="item.path"
        :to="item.path"
        class="nav-item"
        exact-active-class="active"
        @click="closeSidebarOnMobile"
      >

        <span class="icon">

          <i
            :class="item.icon"
          ></i>

        </span>

        <span class="nav-label">
          {{ item.label }}
        </span>


        <!-- Notification Badge -->

        <span
          v-if="
            item.path === '/user/notifications' &&
            unreadCount > 0
          "
          class="badge-count"
        >
          {{ unreadCount > 9 ? "9+" : unreadCount }}
        </span>

      </RouterLink>

    </nav>


    <!-- ========================================
         BOTTOM
    ======================================== -->

    <div class="sidebar-bottom">

      <button
        type="button"
        class="logout-btn"
        title="Logout"
        @click="handleLogout"
      >

        <span class="logout-icon">

          <i class="bi bi-box-arrow-right"></i>

        </span>

        <span>
          Logout
        </span>

      </button>

    </div>

  </aside>


  <!-- ========================================
       MOBILE OVERLAY
  ======================================== -->

  <div
    v-if="sidebarOpen"
    class="sidebar-overlay"
    @click="closeSidebar"
  ></div>

</template>


<script>

import {
  getUnreadCount,
  notificationUpdatedEvent,
} from "../../service/notificationService";


export default {

  name: "Sidebar",


  emits: [
    "close-sidebar"
  ],


  props: {

    sidebarOpen: {
      type: Boolean,
      default: false,
    },

  },


  data() {

    return {

      unreadCount: 0,


      // ======================================
      // MENU
      // ======================================

      menuItems: [

        {
          label: "Home",
          path: "/user/home",
          icon: "bi bi-house-door",
        },

        {
          label: "Browse Books",
          path: "/user/browse-books",
          icon: "bi bi-journal-bookmark",
        },

        {
          label: "My Borrowings",
          path: "/user/my-borrowings",
          icon: "bi bi-journal-arrow-down",
        },

        {
          label: "My Fines",
          path: "/user/my-fines",
          icon: "bi bi-cash-coin",
        },

        {
          label: "Setting",
          path: "/user/setting",
          icon: "bi bi-gear",
        },

      ],

    };

  },


  methods: {


    // ======================================
    // CLOSE SIDEBAR
    // ======================================

    closeSidebar() {

      this.$emit(
        "close-sidebar"
      );

    },


    // ======================================
    // CLOSE ON MOBILE
    // ======================================

    closeSidebarOnMobile() {

      if (
        window.innerWidth <= 768
      ) {

        this.$emit(
          "close-sidebar"
        );

      }

    },



    // ======================================
    // SYNC NOTIFICATIONS
    // ======================================

    syncNotifications() {

      this.loadUnreadCount();

    },


    // ======================================
    // LOGOUT
    // ======================================

    handleLogout() {

      sessionStorage.removeItem(
        "token"
      );

      sessionStorage.removeItem(
        "role"
      );

      sessionStorage.removeItem(
        "user"
      );


      this.$router.push(
        "/login"
      );

    },

  },



  // ========================================
  // UNMOUNTED
  // ========================================

  beforeUnmount() {

    window.removeEventListener(
      notificationUpdatedEvent(),
      this.syncNotifications
    );

  },

};

</script>


<style scoped>

/* =====================================================
   SIDEBAR
===================================================== */

.sidebar {

  width: 250px;

  height: 100vh;

  position: sticky;

  top: 0;

  left: 0;

  flex-shrink: 0;

  background: #ffffff;

  border-right: 1px solid #e5e7eb;

  display: flex;

  flex-direction: column;

  box-sizing: border-box;

  overflow: hidden;

  z-index: 100;

}


/* =====================================================
   LOGO SECTION
===================================================== */

.logo-section {

  width: 100%;

  height: 75px;

  min-height: 75px;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 0 18px;

  border-bottom: 1px solid #f0f0f0;

  box-sizing: border-box;

  background: #ffffff;

}


/* =====================================================
   LOGO ICON
===================================================== */

.logo-icon {

  width: 42px;

  height: 42px;

  min-width: 42px;

  display: flex;

  align-items: center;

  justify-content: center;

  overflow: hidden;

  border-radius: 10px;

  background: transparent;

}


/* =====================================================
   LOGO IMAGE
===================================================== */

.logo-image {

  width: 100%;

  height: 100%;

  object-fit: contain;

  display: block;

}


/* =====================================================
   LOGO TEXT
===================================================== */

.logo-text {

  min-width: 0;

  overflow: hidden;

}


.logo-text h1 {

  margin: 0;

  padding: 0;

  font-size: 18px;

  line-height: 22px;

  font-weight: 700;

  color: #172033;

  white-space: nowrap;

}


.logo-text span {

  display: block;

  margin-top: 2px;

  font-size: 10px;

  line-height: 14px;

  color: #8a92a3;

  white-space: nowrap;

}


/* =====================================================
   NAVIGATION
===================================================== */

.navigation {

  flex: 1;

  width: 100%;

  padding: 20px 14px;

  box-sizing: border-box;

  overflow-y: auto;

  overflow-x: hidden;

}


/* =====================================================
   SCROLLBAR
===================================================== */

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


/* =====================================================
   NAV ITEM
===================================================== */

.nav-item {

  position: relative;

  width: 100%;

  min-height: 48px;

  display: flex;

  align-items: center;

  gap: 13px;

  padding: 11px 13px;

  margin-bottom: 4px;

  box-sizing: border-box;

  border-radius: 9px;

  text-decoration: none;

  color: #536b8c;

  font-size: 14px;

  font-weight: 500;

  transition:
    background 0.2s ease,
    color 0.2s ease;

}


/* =====================================================
   NAV HOVER
===================================================== */

.nav-item:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


/* =====================================================
   NAV ACTIVE
===================================================== */

.nav-item.active {

  background: #eeeaff;

  color: #5b3df5;

  font-weight: 600;

}


/* =====================================================
   ICON
===================================================== */

.icon {

  width: 22px;

  min-width: 22px;

  height: 22px;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 17px;

}


/* =====================================================
   LABEL
===================================================== */

.nav-label {

  flex: 1;

  min-width: 0;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


/* =====================================================
   NOTIFICATION BADGE
===================================================== */

.badge-count {

  min-width: 20px;

  height: 20px;

  padding: 0 5px;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #ef4444;

  color: #ffffff;

  border-radius: 999px;

  font-size: 10px;

  font-weight: 700;

  line-height: 1;

}


/* =====================================================
   SIDEBAR BOTTOM
===================================================== */

.sidebar-bottom {

  width: 100%;

  padding: 15px;

  box-sizing: border-box;

  background: #ffffff;

  border-top: 1px solid #eeeeee;

  flex-shrink: 0;

}


/* =====================================================
   LOGOUT
===================================================== */

.logout-btn {

  width: 100%;

  min-height: 44px;

  display: flex;

  align-items: center;

  gap: 13px;

  padding: 8px 13px;

  border: none;

  border-radius: 8px;

  background: transparent;

  color: #667085;

  font-size: 14px;

  font-weight: 500;

  cursor: pointer;

  text-align: left;

  box-sizing: border-box;

  transition:
    background 0.2s ease,
    color 0.2s ease;

}


.logout-btn:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


/* =====================================================
   LOGOUT ICON
===================================================== */

.logout-icon {

  width: 22px;

  min-width: 22px;

  height: 22px;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 17px;

}


/* =====================================================
   MOBILE CLOSE
===================================================== */

.mobile-close {

  display: none;

  margin-left: auto;

  width: 36px;

  height: 36px;

  border: none;

  border-radius: 8px;

  background: transparent;

  color: #667085;

  align-items: center;

  justify-content: center;

  cursor: pointer;

  font-size: 17px;

}


.mobile-close:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


/* =====================================================
   OVERLAY
===================================================== */

.sidebar-overlay {

  display: none;

  position: fixed;

  inset: 0;

  background: rgba(
    0,
    0,
    0,
    0.35
  );

  z-index: 90;

}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 1024px) {

  .sidebar {

    width: 230px;

  }


  .logo-section {

    padding: 0 16px;

  }


  .navigation {

    padding: 18px 12px;

  }

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 768px) {

  .sidebar {

    width: 270px;

    max-width: 85vw;

    height: 100vh;

    position: fixed;

    top: 0;

    left: 0;

    transform:
      translateX(-100%);

    transition:
      transform 0.3s ease;

    box-shadow:
      8px 0 25px
      rgba(
        0,
        0,
        0,
        0.08
      );

    z-index: 1100;

  }


  .sidebar.sidebar-open {

    transform:
      translateX(0);

  }


  .logo-section {

    height: 70px;

    min-height: 70px;

    padding: 0 18px;

  }


  .mobile-close {

    display: flex;

  }


  .navigation {

    padding: 18px 12px;

  }


  .nav-item {

    min-height: 48px;

    padding: 12px 13px;

  }


  .sidebar-overlay {

    display: block;

    z-index: 1050;

  }

}


/* =====================================================
   SMALL MOBILE
===================================================== */

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