<template>

  <div class="admin-layout">

    <!-- =========================
         ADMIN SIDEBAR
    ========================== -->

    <AdminSidebar />


    <!-- =========================
         MAIN CONTENT
    ========================== -->

    <div class="main-content">

      <!-- =========================
           ADMIN NAVBAR
      ========================== -->

      <AdminNavbar />


      <!-- =========================
           PAGE CONTENT
      ========================== -->

      <main class="content">

        <RouterView />

      </main>

    </div>

  </div>

</template>


<script setup>

// ========================================
// COMPONENTS
// ========================================

import AdminSidebar
  from "@/components/common/AdminSidebar.vue";

import AdminNavbar
  from "@/components/common/AdminNavbar.vue";

</script>


<style scoped>

/* ========================================
   ADMIN LAYOUT
======================================== */

.admin-layout {

  width: 100%;

  min-height: 100vh;

  background: #f8f9fc;

}


/* ========================================
   MAIN CONTENT
   Desktop
======================================== */

.main-content {

  margin-left: 250px;

  width: calc(100% - 250px);

  min-height: 100vh;

  box-sizing: border-box;

}


/* ========================================
   PAGE CONTENT
======================================== */

.content {

  width: 100%;

  min-height: 100vh;

  padding-top: 75px;

  box-sizing: border-box;

}


/* ========================================
   TABLET
======================================== */

@media (max-width: 1024px) {

  .main-content {

    margin-left: 230px;

    width: calc(100% - 230px);

  }

}


/* ========================================
   MOBILE
======================================== */

@media (max-width: 768px) {

  .main-content {

    margin-left: 0;

    width: 100%;

    min-width: 0;

  }


  .content {

    width: 100%;

    min-height: 100vh;

    padding-top: 65px;

  }

}

</style>