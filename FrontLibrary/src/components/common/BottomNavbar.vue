<template>

  <nav class="bottom-nav">

    <div class="bottom-nav-inner">

      <!-- =================================================
           HOME
      ================================================== -->

      <router-link
        to="/user/home"
        class="bottom-nav-item"
        active-class="active"
      >

        <i class="bi bi-house-fill"></i>

        <span>
          Home
        </span>

      </router-link>


      <!-- =================================================
           MY BORROWING
      ================================================== -->

      <router-link
        to="/user/my-borrowings"
        class="bottom-nav-item"
        active-class="active"
      >

        <i class="bi bi-book"></i>

        <span>
          My Borrowing
        </span>

      </router-link>


      <!-- =================================================
           MY FINE
      ================================================== -->

      <router-link
        to="/user/my-fines"
        class="bottom-nav-item"
        active-class="active"
      >

        <i class="bi bi-wallet2"></i>

        <span>
          My Fine
        </span>

      </router-link>

    </div>

  </nav>

</template>


<script setup>

/*
  No JavaScript logic is required.
  router-link automatically handles
  active route.
*/

</script>


<style scoped>

/* =====================================================
   BOTTOM NAVIGATION
===================================================== */

.bottom-nav {

  display: none;

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 767px) {

  .bottom-nav {

    display: block;

    position: fixed;

    left: 0;

    right: 0;

    bottom: 0;

    width: 100%;

    height: 70px;

    background: rgba(255, 255, 255, 0.97);

    border-top:
      1px solid #e5e7eb;

    box-shadow:
      0 -4px 20px rgba(15, 23, 42, 0.08);

    z-index: 1000;

    backdrop-filter: blur(12px);

    -webkit-backdrop-filter: blur(12px);

  }


  /* =================================================
     INNER
  ================================================== */

  .bottom-nav-inner {

    width: 100%;

    height: 100%;

    display: grid;

    grid-template-columns:
      1fr
      1fr
      1fr;

    align-items: center;

    justify-items: center;

    padding:
      5px
      12px
      calc(5px + env(safe-area-inset-bottom));

    box-sizing: border-box;

  }


  /* =================================================
     NORMAL BUTTON
  ================================================== */

  .bottom-nav-item {

    width: 90px;

    height: 58px;

    display: flex;

    flex-direction: column;

    align-items: center;

    justify-content: center;

    gap: 5px;

    color: #9ca3af;

    text-decoration: none;

    border-radius: 14px;

    transition:
      color 0.2s ease,
      background 0.2s ease,
      transform 0.2s ease;

  }


  /* =================================================
     ICON
  ================================================== */

  .bottom-nav-item i {

    font-size: 21px;

    line-height: 1;

  }


  /* =================================================
     TEXT
  ================================================== */

  .bottom-nav-item span {

    font-size: 11px;

    line-height: 14px;

    font-weight: 500;

    white-space: nowrap;

  }


  /* =================================================
     HOVER
  ================================================== */

  .bottom-nav-item:hover {

    color: #5b3df5;

  }


  /* =================================================
     ACTIVE
  ================================================== */

  .bottom-nav-item.active {

    color: #5b3df5;

  }


  .bottom-nav-item.active i {

    transform:
      translateY(-1px);

  }

}


/* =====================================================
   SMALL PHONE
===================================================== */

@media (max-width: 379px) {

  .bottom-nav {

    height: 68px;

  }


  .bottom-nav-inner {

    padding-left: 4px;

    padding-right: 4px;

  }


  .bottom-nav-item {

    width: 80px;

    height: 54px;

  }


  .bottom-nav-item i {

    font-size: 19px;

  }


  .bottom-nav-item span {

    font-size: 10px;

  }

}

</style>