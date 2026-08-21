<template>
  <div class="card overdue-card">

    <!-- =========================
         Card Header
    ========================== -->
    <div class="card-header">

      <div>
        <h3>
          <i class="bi bi-exclamation-triangle"></i>
          Overdue Books
        </h3>

        <p>Books that need attention</p>
      </div>

      <RouterLink to="/admin/borrowings">
        View All
        <i class="bi bi-arrow-right"></i>
      </RouterLink>

    </div>


    <!-- =========================
         Overdue Books
    ========================== -->
    <div class="overdue-grid">

      <div
        v-for="book in overdueBooks"
        :key="book.id"
        class="overdue-item"
      >

        <!-- Book Icon -->
        <div class="book-icon">
          <i class="bi bi-book"></i>
        </div>


        <!-- Book Information -->
        <div class="overdue-info">

          <strong>
            {{ book.book }}
          </strong>

          <span>
            {{ book.user }}
          </span>

        </div>


        <!-- Overdue Days -->
        <div class="overdue-days">

          <strong>
            {{ book.days }} days
          </strong>

          <span>
            overdue
          </span>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>

const overdueBooks = [

  {
    id: 1,
    book: "Clean Code",
    user: "Sok Dara",
    days: 3
  },

  {
    id: 2,
    book: "Java Programming",
    user: "Chan Vichea",
    days: 2
  },

  {
    id: 3,
    book: "Computer Networking",
    user: "Dara Kim",
    days: 1
  }

];

</script>


<style scoped>

/* =========================
   Card
========================= */

.card {

  min-width: 0;

  background: white;

  border: 1px solid #e6e9ef;

  border-radius: 12px;

  box-shadow:
    0 2px 5px rgba(0, 0, 0, 0.02);

  overflow: hidden;

}


.overdue-card {

  margin-bottom: 20px;

}


/* =========================
   Card Header
========================= */

.card-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 15px;

  padding: 20px;

  border-bottom: 1px solid #f0f1f4;

}


.card-header h3 {

  display: flex;

  align-items: center;

  gap: 7px;

  margin: 0;

  font-size: 16px;

}


.card-header h3 i {

  color: #d93636;

  font-size: 16px;

}


.card-header p {

  margin: 5px 0 0;

  font-size: 12px;

  color: #8a92a3;

}


.card-header a {

  display: flex;

  align-items: center;

  gap: 5px;

  color: #5b3df5;

  text-decoration: none;

  font-size: 13px;

  font-weight: 600;

  white-space: nowrap;

}


.card-header a:hover {

  color: #4528d4;

}


/* =========================
   Overdue Grid
========================= */

.overdue-grid {

  display: grid;

  grid-template-columns:
    repeat(3, minmax(0, 1fr));

  gap: 15px;

  padding: 20px;

}


/* =========================
   Overdue Item
========================= */

.overdue-item {

  min-width: 0;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 14px;

  background: #fff8f8;

  border: 1px solid #ffe0e0;

  border-radius: 9px;

}


/* =========================
   Book Icon
========================= */

.book-icon {

  width: 38px;

  height: 45px;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #ffe5e5;

  color: #d93636;

  border-radius: 6px;

  flex-shrink: 0;

}


.book-icon i {

  font-size: 18px;

}


/* =========================
   Overdue Information
========================= */

.overdue-info {

  flex: 1;

  min-width: 0;

}


.overdue-info strong {

  display: block;

  font-size: 12px;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.overdue-info span {

  display: block;

  margin-top: 3px;

  color: #8a92a3;

  font-size: 10px;

}


/* =========================
   Overdue Days
========================= */

.overdue-days {

  text-align: right;

  flex-shrink: 0;

}


.overdue-days strong {

  display: block;

  color: #d93636;

  font-size: 12px;

}


.overdue-days span {

  color: #9aa1b1;

  font-size: 9px;

}


/* =========================
   Responsive
========================= */

@media (max-width: 1200px) {

  .overdue-grid {

    grid-template-columns:
      repeat(2, minmax(0, 1fr));

  }

}


@media (max-width: 768px) {

  .overdue-grid {

    grid-template-columns: 1fr;

  }


  .card-header {

    align-items: flex-start;

    flex-direction: column;

  }

}

</style>