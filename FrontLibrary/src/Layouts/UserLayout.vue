<template>
  <div class="layout-wrapper">

    <!-- Overlay -->
    <div
      v-if="sidebarOpen"
      class="mobile-overlay"
      @click="sidebarOpen = false"
    ></div>

    <!-- Sidebar -->
    <Usersidebar
      :sidebar-open="sidebarOpen"
      @close-sidebar="sidebarOpen = false"
    />

    <!-- Main -->
    <div class="main-area">

      <!-- Navbar -->
      <UserNavbar
        @toggle-sidebar="sidebarOpen = !sidebarOpen"
      />

      <main class="main-content">
        <router-view />
      </main>

    </div>

  </div>
</template>

<script setup>
import { ref } from "vue";

import Usersidebar from "@/components/common/Usersidebar.vue";
import UserNavbar from "@/components/common/UserNavbar.vue";

const sidebarOpen = ref(false);
</script>

<style scoped>
.layout-wrapper {
  display: flex;
  min-height: 100vh;
  background: #f8fafc;
}

.main-area {
  flex: 1;
  min-width: 0;
}

.main-content {
  padding: 1.5rem 2rem;
}

/* Mobile */
.mobile-overlay {
  display: none;
}

@media (max-width: 1023px) {
  .layout-wrapper {
    display: block;
  }

  .main-content {
    padding: 1rem;
  }

  .mobile-overlay {
    display: block;
    position: fixed;
    inset: 0;
    background: rgba(0, 0, 0, 0.4);
    z-index: 40;
  }
}
</style>