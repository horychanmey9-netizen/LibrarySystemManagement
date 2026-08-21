<template>
  <div class="summary-grid">

    <!-- =========================
         Total Books
    ========================== -->

    <div class="summary-card">

      <div class="card-icon books-icon">
        <i class="bi bi-book"></i>
      </div>

      <div class="card-content">
        <p>Total Books</p>

        <h2>
          {{ summary.totalBooks.toLocaleString() }}
        </h2>

        <span>
          Books in library
        </span>
      </div>

    </div>


    <!-- =========================
         Total Users
    ========================== -->

    <div class="summary-card">

      <div class="card-icon users-icon">
        <i class="bi bi-people"></i>
      </div>

      <div class="card-content">
        <p>Total Users</p>

        <h2>
          {{ summary.totalUsers.toLocaleString() }}
        </h2>

        <span>
          Registered users
        </span>
      </div>

    </div>


    <!-- =========================
         Borrowed Books
    ========================== -->

    <div class="summary-card">

      <div class="card-icon borrowed-icon">
        <i class="bi bi-journal-bookmark"></i>
      </div>

      <div class="card-content">
        <p>Borrowed Books</p>

        <h2>
          {{ summary.borrowedBooks.toLocaleString() }}
        </h2>

        <span>
          Currently borrowed
        </span>
      </div>

    </div>


    <!-- =========================
         Overdue Books
    ========================== -->

    <div class="summary-card">

      <div class="card-icon overdue-icon">
        <i class="bi bi-exclamation-triangle"></i>
      </div>

      <div class="card-content">
        <p>Overdue Books</p>

        <h2>
          {{ summary.overdueBooks.toLocaleString() }}
        </h2>

        <span>
          Need attention
        </span>
      </div>

    </div>


    <!-- =========================
         Total Fines
    ========================== -->

    <div class="summary-card">

      <div class="card-icon fines-icon">
        <i class="bi bi-cash-stack"></i>
      </div>

      <div class="card-content">
        <p>Total Fines</p>

        <h2>
          ${{ summary.totalFines.toFixed(2) }}
        </h2>

        <span>
          Total generated fines
        </span>
      </div>

    </div>

  </div>
</template>


<script setup>

import { reactive } from "vue";


/* =========================
   Summary Data
========================= */

const summary = reactive({

  totalBooks: 1250,

  totalUsers: 350,

  borrowedBooks: 120,

  overdueBooks: 18,

  totalFines: 125

});

</script>


<style scoped>

/* =========================
   Summary Grid
========================= */

.summary-grid {

  display: grid;

  grid-template-columns:
    repeat(5, 1fr);

  gap: 18px;

  margin-bottom: 24px;

}


/* =========================
   Summary Card
========================= */

.summary-card {

  background: #ffffff;

  border: 1px solid #e5e7eb;

  border-radius: 14px;

  padding: 20px;

  display: flex;

  align-items: center;

  gap: 15px;

  box-shadow:
    0 2px 8px
    rgba(0, 0, 0, 0.04);

}


/* =========================
   Card Icon
========================= */

.card-icon {

  width: 52px;

  height: 52px;

  min-width: 52px;

  border-radius: 12px;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 24px;

  flex-shrink: 0;

}


/* =========================
   Icon Backgrounds
========================= */

.books-icon {

  background: #e8f1ff;

  color: #2563eb;

}


.users-icon {

  background: #eafaf0;

  color: #16a34a;

}


.borrowed-icon {

  background: #fff4df;

  color: #d97706;

}


.overdue-icon {

  background: #ffeaea;

  color: #dc2626;

}


.fines-icon {

  background: #f3eaff;

  color: #7c3aed;

}


/* =========================
   Card Content
========================= */

.card-content {

  min-width: 0;

}


.card-content p {

  margin: 0;

  color: #6b7280;

  font-size: 13px;

}


.card-content h2 {

  margin: 5px 0;

  color: #111827;

  font-size: 24px;

  font-weight: 700;

}


.card-content span {

  color: #9ca3af;

  font-size: 11px;

}


/* =========================
   Responsive
========================= */

@media (max-width: 1200px) {

  .summary-grid {

    grid-template-columns:
      repeat(3, 1fr);

  }

}


@media (max-width: 768px) {

  .summary-grid {

    grid-template-columns:
      repeat(2, 1fr);

  }

}


@media (max-width: 500px) {

  .summary-grid {

    grid-template-columns: 1fr;

  }

}

</style>