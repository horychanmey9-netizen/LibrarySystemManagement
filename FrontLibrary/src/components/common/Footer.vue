```vue
<template>

  <footer class="footer">

    <div class="footer-container">

      <!-- =================================================
           LIBRARY INFORMATION
      ================================================== -->

      <div class="footer-section library-info">

        <div class="footer-logo">

          <img
            src="/logo1.png"
            alt="Library Logo"
          />

          <div>

            <h3>
              Library Management System
            </h3>

            <p>
              Your trusted place for books and knowledge.
            </p>

          </div>

        </div>

      </div>


      <!-- =================================================
           CONTACT
      ================================================== -->

      <div class="footer-section">

        <h4>
          Contact Us
        </h4>

        <div class="footer-item">

          <i class="bi bi-geo-alt"></i>

          <span>
            Phnom Penh, Cambodia
          </span>

        </div>

        <div class="footer-item">

          <i class="bi bi-telephone"></i>

          <span>
            +855 12 345 678
          </span>

        </div>

        <div class="footer-item">

          <i class="bi bi-envelope"></i>

          <span>
            library@example.com
          </span>

        </div>

      </div>


      <!-- =================================================
           OPENING HOURS
      ================================================== -->

      <div class="footer-section">

        <h4>
          Opening Hours
        </h4>

        <div class="opening-row">

          <span>
            Monday - Friday
          </span>

          <span>
            8:00 AM - 5:00 PM
          </span>

        </div>

        <div class="opening-row">

          <span>
            Saturday
          </span>

          <span>
            8:00 AM - 12:00 PM
          </span>

        </div>

        <div class="opening-row">

          <span>
            Sunday
          </span>

          <span class="closed">
            Closed
          </span>

        </div>

      </div>


      <!-- =================================================
           INFORMATION
      ================================================== -->

      <div class="footer-section">

        <h4>
          Information
        </h4>

        <p class="info-text">
          Manage your books, borrowings,
          favorites and account information
          easily through our library system.
        </p>

      </div>

    </div>


    <!-- =================================================
         BOTTOM
    ================================================== -->

    <div class="footer-bottom">

      <div class="footer-bottom-container">

        <span>
          © {{ currentYear }}
          Library Management System
        </span>

        <span>
          All rights reserved.
        </span>

      </div>

    </div>

  </footer>

</template>


<script setup>

const currentYear =
  new Date().getFullYear();

</script>


<style scoped>

/* =====================================================
   FOOTER
===================================================== */

.footer {

  width: 100%;

  margin-top: 40px;

  background: #d8f9ff;

  border-top:
    1px solid #eef0f4;

}


/* =====================================================
   MAIN CONTAINER
===================================================== */

.footer-container {

  max-width: 1280px;

  margin: 0 auto;

  padding: 35px 20px;

  display: grid;

  grid-template-columns:
    1.5fr
    1fr
    1.2fr
    1.2fr;

  gap: 35px;

}


/* =====================================================
   SECTION
===================================================== */

.footer-section {

  min-width: 0;

}


/* =====================================================
   LIBRARY INFO
===================================================== */

.library-info {

  display: flex;

  align-items: flex-start;

}


/* =====================================================
   LOGO
===================================================== */

.footer-logo {

  display: flex;

  align-items: flex-start;

  gap: 12px;

}


.footer-logo img {

  width: 42px;

  height: 42px;

  object-fit: contain;

  flex-shrink: 0;

}


/* =====================================================
   LIBRARY TITLE
===================================================== */

.footer-logo h3 {

  margin: 2px 0 6px;

  font-size: 15px;

  font-weight: 700;

  color: #1f2937;

}


.footer-logo p {

  margin: 0;

  max-width: 230px;

  font-size: 12px;

  line-height: 1.6;

  color: #6b7280;

}


/* =====================================================
   HEADINGS
===================================================== */

.footer-section h4 {

  margin: 0 0 14px;

  font-size: 14px;

  font-weight: 600;

  color: #1f2937;

}


/* =====================================================
   CONTACT ITEM
===================================================== */

.footer-item {

  display: flex;

  align-items: center;

  gap: 9px;

  margin-bottom: 10px;

  font-size: 12px;

  color: #6b7280;

}


.footer-item i {

  width: 16px;

  color: #2563eb;

  font-size: 13px;

}


/* =====================================================
   OPENING HOURS
===================================================== */

.opening-row {

  display: flex;

  justify-content: space-between;

  gap: 12px;

  margin-bottom: 9px;

  font-size: 11px;

  color: #6b7280;

}


.opening-row span:last-child {

  color: #374151;

  white-space: nowrap;

}


.opening-row .closed {

  color: #ef4444;

}


/* =====================================================
   INFORMATION
===================================================== */

.info-text {

  margin: 0;

  max-width: 230px;

  font-size: 12px;

  line-height: 1.7;

  color: #6b7280;

}


/* =====================================================
   BOTTOM
===================================================== */

.footer-bottom {

  border-top:
    1px solid #eef0f4;

}


.footer-bottom-container {

  max-width: 1280px;

  margin: 0 auto;

  padding: 14px 20px;

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 15px;

  font-size: 11px;

  color: #9ca3af;

}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 992px) {

  .footer-container {

    grid-template-columns:
      1.3fr
      1fr
      1.2fr;

    gap: 25px;

  }


  .library-info {

    grid-column: span 3;

  }

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 768px) {

  .footer {

    margin-top: 30px;

  }


  .footer-container {

    grid-template-columns:
      1fr 1fr;

    gap: 25px 18px;

    padding: 28px 16px;

  }


  .library-info {

    grid-column: span 2;

  }


  .footer-logo img {

    width: 36px;

    height: 36px;

  }


  .footer-logo h3 {

    font-size: 13px;

  }


  .footer-logo p {

    font-size: 11px;

  }


  .footer-section h4 {

    font-size: 13px;

    margin-bottom: 11px;

  }


  .footer-item {

    font-size: 11px;

  }


  .opening-row {

    font-size: 10px;

  }


  .info-text {

    font-size: 11px;

  }


  .footer-bottom-container {

    padding: 12px 16px;

    font-size: 9px;

  }

}


/* =====================================================
   SMALL MOBILE
===================================================== */

@media (max-width: 480px) {

  .footer {

    margin-top: 24px;

  }


  .footer-container {

    grid-template-columns: 1fr;

    gap: 22px;

    padding: 24px 12px;

  }


  .library-info {

    grid-column: span 1;

  }


  .footer-logo img {

    width: 32px;

    height: 32px;

  }


  .footer-logo h3 {

    font-size: 12px;

  }


  .footer-logo p {

    font-size: 10px;

  }


  .footer-section h4 {

    font-size: 12px;

  }


  .footer-item {

    font-size: 10px;

    margin-bottom: 8px;

  }


  .opening-row {

    font-size: 10px;

  }


  .info-text {

    font-size: 10px;

    max-width: 100%;

  }


  .footer-bottom-container {

    flex-direction: column;

    justify-content: center;

    text-align: center;

    gap: 4px;

    font-size: 8px;

  }

}

/* =====================================================
   HIDE FOOTER ON MOBILE
===================================================== */

@media (max-width: 768px) {

  .footer {
    display: none;
  }

}

</style>
```
