<template>
  <div class="admin-layout">
    <AdminSidebar />
    <div class="main-content">
      <AdminNavbar />
      <!-- Page Content -->
      <main class="content">
        <RouterView />
      </main>

    </div>

  </div>
</template>


<script setup>

import AdminSidebar from "@/components/common/AdminSidebar.vue";
import AdminNavbar from "@/components/common/AdminNavbar.vue";

</script>


<style scoped>

.admin-layout {
  width: 100%;
  min-height: 100vh;
  background: #f8f9fc;
}


/* =========================
   Desktop
========================= */

.main-content {
  margin-left: 250px;

  width: calc(100% - 250px);

  min-height: 100vh;

  box-sizing: border-box;
}


/* =========================
   Page Content
========================= */

.content {
  padding-top: 75px;

  min-height: 100vh;

  box-sizing: border-box;
}


/* =========================
   Mobile
========================= */

@media (max-width: 768px) {

  .main-content {

    /* ❌ មិនទុក 75px ទៀត */

    margin-left: 0;

    width: 100%;

    min-width: 0;

  }

  .content {

    width: 100%;

    padding-top: 65px;

    box-sizing: border-box;

  }

}

</style>