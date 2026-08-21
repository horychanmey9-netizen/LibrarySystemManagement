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

.main-content {
  margin-left: 250px;

  width: calc(100% - 250px);

  min-height: 100vh;

  box-sizing: border-box;
}

   /* Page Content */

.content {
  padding-top: 75px;

  min-height: 100vh;

  box-sizing: border-box;
}


/*Responsive*/

@media (max-width: 768px) {

  .main-content {
    margin-left: 75px;

    width: calc(100% - 75px);
  }

}

</style>