<template>
  <div class="layout-wrapper">
    <Usersidebar />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import Usersidebar from "@/components/common/Usersidebar.vue";
</script>

<style scoped>
.layout-wrapper { display: flex; min-height: 100vh; background: #f8fafc; }
.main-content { flex: 1; padding: 1.5rem 2rem; overflow-y: auto; }
</style>