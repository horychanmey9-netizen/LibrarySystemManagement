<template>
  <div class="layout-wrapper">

    <!-- =====================================================
         MOBILE OVERLAY
    ====================================================== -->
    <div
      v-if="sidebarOpen"
      class="mobile-overlay"
      @click="sidebarOpen = false"
    ></div>


    <!-- =====================================================
         MAIN AREA
    ====================================================== -->
    <div class="main-area">

      <!-- ===================================================
           FIXED NAVBAR
      ==================================================== -->
      <div class="navbar-fixed">
        <UserNavbar
          @toggle-sidebar="sidebarOpen = !sidebarOpen"
        />
      </div>


      <!-- ===================================================
           MAIN CONTENT
      ==================================================== -->
      <main class="main-content">
        <router-view />
      </main>

    </div>


    <!-- =====================================================
         MOBILE BOTTOM NAVBAR
    ====================================================== -->
    <BottomNavbar />


    <!-- =====================================================
         FOOTER
    ====================================================== -->
    <Footer />

  </div>
</template>


<script setup>
import { ref } from "vue";

import UserNavbar from "@/components/common/UserNavbar.vue";
import BottomNavbar from "@/components/common/BottomNavbar.vue";
import Footer from "../components/common/Footer.vue";


const sidebarOpen = ref(false);
</script>


<style scoped>

/* =====================================================
   LAYOUT
===================================================== */

.layout-wrapper {
  width: 100%;
  min-height: 100vh;

  background: #f8fafc;

  /*
    IMPORTANT:
    Prevent the whole page from becoming
    horizontally scrollable.
  */
  overflow-x: hidden;
}


/* =====================================================
   MAIN AREA
===================================================== */

.main-area {
  width: 100%;
  min-width: 0;

  position: relative;
}


/* =====================================================
   FIXED NAVBAR
===================================================== */

.navbar-fixed {
  position: fixed;

  top: 0;
  left: 0;
  right: 0;

  width: 100%;
  height: 75px;

  z-index: 900;

  background: #ffffff;

  /*
    Keep navbar independent from page content.
  */
  isolation: isolate;
}


/* =====================================================
   MAIN CONTENT
===================================================== */

.main-content {
  position: relative;

  z-index: 1;

  width: 100%;
  min-width: 0;

  /*
    IMPORTANT:
    Only prevent page-level horizontal overflow.
    Do NOT use overflow-x: auto here.
  */
  overflow-x: hidden;

  /*
    Space for fixed desktop navbar.
  */
  padding-top: 75px;

  padding-left: 2rem;
  padding-right: 2rem;

  padding-bottom: 2rem;

  box-sizing: border-box;
}


/* =====================================================
   MOBILE OVERLAY
===================================================== */

.mobile-overlay {
  display: none;

  position: fixed;

  inset: 0;

  background: rgba(0, 0, 0, 0.4);

  z-index: 850;
}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 1023px) {

  .main-content {
    padding-top: 75px;

    padding-left: 1rem;
    padding-right: 1rem;

    padding-bottom: 1rem;
  }


  .mobile-overlay {
    display: block;
  }

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 767px) {

  .navbar-fixed {
    height: 64px;
  }


  .main-content {
    padding-top: 64px;

    padding-left: 1rem;
    padding-right: 1rem;

    /*
      Space for BottomNavbar.
    */
    padding-bottom: 80px;
  }

}


/* =====================================================
   SMALL PHONE
===================================================== */

@media (max-width: 379px) {

  .main-content {
    padding-left: 0.75rem;
    padding-right: 0.75rem;
  }

}

</style>