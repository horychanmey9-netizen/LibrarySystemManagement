
<template>
  <aside
    class="sidebar"
    :class="{ 'sidebar-open': sidebarOpen }"
  >

    <!-- ================= HEADER ================= -->
    <div class="sidebar-header bg-white border-b border-gray-100 shadow-sm">

      <div class="logo">

        <i class="bi bi-book text-black"></i>

        <h1>
          LIBRARY SYSTEM
        </h1>

      </div>

    </div>
    


    <!-- ================= MENU ================= -->
    <nav class="sidebar-menu">

      <router-link 
  v-for="item in menuItems" 
  :key="item.path" 
  :to="item.path" 
  class="menu-item" 
  exact-active-class="active"
  @click="closeSidebar"
>

        <i :class="item.icon"></i>

        <span>
          {{ item.label }}
        </span>


        <!-- Notification Badge -->
        <span
          v-if="
            item.path === '/notifications' &&
            unreadCount > 0
          "
          class="badge-count"
        >
          {{ unreadCount > 9 ? "9+" : unreadCount }}
        </span>

      </router-link>

    </nav>


    <!-- ================= FOOTER ================= -->
    <div class="sidebar-footer">

      <button
        class="logout-btn"
        @click="handleLogout"
      >

        <i class="bi bi-box-arrow-right"></i>

        <span>
          Logout
        </span>

      </button>

    </div>

  </aside>
</template>


<script>
import {
  getUnreadCount,
  notificationUpdatedEvent,
} from "../../service/notificationService";

export default {

  name: "Sidebar",

  emits: ["close-sidebar"],

  props: {

    sidebarOpen: {
      type: Boolean,
      default: false,
    },

  },


  data() {

    return {

      unreadCount: 0,

      menuItems: [
  {
    label: "Home",
    path: "/user",
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
    icon: "bi bi-people",
  },

  {
    label: "My Fines",
    path: "/user/my-fines",
    icon: "bi bi-search",
  },

  {
    label: "Notifications",
    path: "/user/notifications",
    icon: "bi bi-bell",
  },

  {
    label: "Profile",
    path: "/user/profile",
    icon: "bi bi-person",
  },
],

    };
  },


  methods: {

    closeSidebar() {
    this.$emit("close-sidebar");
  },

    // ===============================
    // LOAD COUNT
    // ===============================
    loadUnreadCount() {

      this.unreadCount =
        getUnreadCount();

    },


    // ===============================
    // SYNC
    // ===============================
    syncNotifications() {

      this.loadUnreadCount();

    },


    // ===============================
    // LOGOUT
    // ===============================
    handleLogout() {

      localStorage.removeItem("token");

      this.$router.push("/login");

    },

  },


  mounted() {

    this.loadUnreadCount();


    // Listen for notification changes
    window.addEventListener(
      notificationUpdatedEvent(),
      this.syncNotifications
    );

  },


  beforeUnmount() {

    window.removeEventListener(
      notificationUpdatedEvent(),
      this.syncNotifications
    );

  },

};
</script>


<style scoped>

.sidebar {
  width: 250px;
  height: 100vh;
  background: #ffffff;
  color: #232d38;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
  flex-shrink: 0;
  z-index: 50;
}


/* Mobile */
@media (max-width: 1023px) {

  .sidebar {
    position: fixed;
    left: 0;
    top: 0;

    transform: translateX(-100%);

    transition:
      transform 0.3s ease;
  }

  .sidebar.sidebar-open {
    transform: translateX(0);
  }

}


.sidebar-header {
  padding: 1.2rem 1rem;
  font-size: 1rem;
  font-weight: 500 ;
}


.badge {
  background: #2563eb;
  color: #fff;
  font-size: 0.7rem;
  font-weight: 700;
  padding: 0.25rem 0.6rem;
  border-radius: 6px;
}


.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  margin-top: 0.8rem;
}


.logo h2 {
  font-size: 0.95rem;
  color: #fff;
  margin: 0;
}


.sidebar-menu {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0.3rem;
  padding: 1rem 0.7rem;
}


.menu-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.65rem 0.9rem;
  border-radius: 8px;
  color: #3f5d81;
  text-decoration: none;
  font-size: 0.9rem;
  font-weight: 600;
  transition: background 0.2s ease;

  position: relative;
}


.menu-item:hover {
  background: #f5f3ff;
  color: #5b3df5;
}


.menu-item.active {
  background: #f5f3ff;
  color: #5b3df5;
}


.badge-count {
  position: absolute;
  right: 0.8rem;

  background: #ef4444;
  color: #fff;

  min-width: 20px;
  height: 20px;

  display: flex;
  align-items: center;
  justify-content: center;

  font-size: 0.7rem;
  font-weight: 700;

  padding: 0.05rem 0.35rem;

  border-radius: 999px;
}


.sidebar-footer {
  padding: 1rem 0.7rem;
  border-top: 1px solid #1e293b;
}


.logout-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;

  background: none;
  border: none;

  color: #cbd5e1;
  font-size: 0.9rem;

  cursor: pointer;

  width: 100%;
  padding: 0.6rem 0.9rem;
}


.logout-btn:hover {
  color: #fff;
}

</style>

