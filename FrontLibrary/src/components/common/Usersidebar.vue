<template>
  <aside class="sidebar">
    <div class="sidebar-header">
      <span class="badge">USER INTERFACE</span>
      <div class="logo">
        <i class="bi bi-book"></i>
        <h2>LIBRARY SYSTEM</h2>
      </div>
    </div>

    <nav class="sidebar-menu">
      <router-link
        v-for="item in menuItems"
        :key="item.path"
        :to="item.path"
        class="menu-item"
        active-class="active"
      >
        <i :class="item.icon"></i>
        <span>{{ item.label }}</span>
        <span v-if="item.badge" class="badge-count">{{ item.badge }}</span>
      </router-link>
    </nav>

    <div class="sidebar-footer">
      <button class="logout-btn" @click="handleLogout">
        <i class="bi bi-box-arrow-right"></i>
        <span>Logout</span>
      </button>
    </div>
  </aside>
</template>

<script>
export default {
  name: "Sidebar",
  data() {
    return {
      menuItems: [
        { label: "Home", path: "/", icon: "bi bi-house-door" },
        { label: "Browse Books", path: "/browse-books", icon: "bi bi-journal-bookmark" },
        { label: "My Borrowings", path: "/my-borrowings", icon: "bi bi-people" },
        { label: "My Fines", path: "/my-fines", icon: "bi bi-search" },
        { label: "Notifications", path: "/notifications", icon: "bi bi-bell", badge: 2 },
        { label: "Profile", path: "/profile", icon: "bi bi-person" }
      ]
    };
  },
  methods: {
    handleLogout() {
      // ដាក់ logic logout (clear token, redirect ទៅ login)
      localStorage.removeItem("token");
      this.$router.push("/login");
    }
  }
};
</script>

<style scoped>
.sidebar {
  width: 250px;
  height: 100vh;
  background: #0f172a;
  color: #cbd5e1;
  display: flex;
  flex-direction: column;
  position: sticky;
  top: 0;
}

.sidebar-header {
  padding: 1.2rem 1rem;
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
  color: #cbd5e1;
  text-decoration: none;
  font-size: 0.9rem;
  transition: background 0.2s ease;
  position: relative;
}

.menu-item:hover {
  background: #1e293b;
  color: #fff;
}

.menu-item.active {
  background: #2563eb;
  color: #fff;
}

.badge-count {
  position: absolute;
  right: 0.8rem;
  background: #ef4444;
  color: #fff;
  font-size: 0.7rem;
  padding: 0.05rem 0.4rem;
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