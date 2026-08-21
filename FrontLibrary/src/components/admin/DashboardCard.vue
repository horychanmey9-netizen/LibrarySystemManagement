<template>
  <div class="stats-grid">

    <div
      v-for="stat in stats"
      :key="stat.title"
      class="stat-card"
    >

      <!-- Icon -->
      <div
        class="stat-icon"
        :class="stat.color"
      >
        <i :class="stat.icon"></i>
      </div>

      <!-- Content -->
      <div class="stat-content">

        <p>{{ stat.title }}</p>

        <h2>{{ stat.value }}</h2>

        <span :class="stat.changeType">
          <i :class="stat.changeIcon"></i>
          {{ stat.change }}

          <small>
            from last month
          </small>
        </span>

      </div>

    </div>

  </div>
</template>


<script setup>

const stats = [

  {
    title: "Total Books",
    value: "1,250",
    icon: "bi bi-book",
    color: "purple",
    changeIcon: "bi bi-arrow-up",
    change: "12.5%",
    changeType: "increase"
  },

  {
    title: "Total Users",
    value: "350",
    icon: "bi bi-people",
    color: "blue",
    changeIcon: "bi bi-arrow-up",
    change: "8.2%",
    changeType: "increase"
  },

  {
    title: "Borrowed Books",
    value: "85",
    icon: "bi bi-journal-bookmark",
    color: "orange",
    changeIcon: "bi bi-arrow-up",
    change: "5.4%",
    changeType: "increase"
  },

  {
    title: "Overdue Books",
    value: "12",
    icon: "bi bi-exclamation-triangle",
    color: "red",
    changeIcon: "bi bi-arrow-down",
    change: "3.1%",
    changeType: "decrease"
  },

  {
    title: "Total Fines",
    value: "$1,240",
    icon: "bi bi-cash-coin",
    color: "green",
    changeIcon: "bi bi-arrow-up",
    change: "6.8%",
    changeType: "increase"
  }

];

</script>


<style scoped>

.stats-grid {
  display: grid;

  grid-template-columns:
    repeat(5, minmax(0, 1fr));

  gap: 16px;

  margin-bottom: 20px;
}


/* =========================
   Stat Card
========================= */

.stat-card {

  min-width: 0;

  display: flex;

  align-items: center;

  gap: 15px;

  background: white;

  border: 1px solid #e6e9ef;

  border-radius: 12px;

  padding: 20px;

  box-shadow:
    0 2px 5px rgba(0, 0, 0, 0.02);

}


/* =========================
   Stat Icon
========================= */

.stat-icon {

  width: 48px;
  height: 48px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 10px;

  font-size: 21px;

  flex-shrink: 0;

}


/* Purple */

.stat-icon.purple {
  background: #eeeaff;
  color: #5b3df5;
}


/* Blue */

.stat-icon.blue {
  background: #e7f1ff;
  color: #2878d4;
}


/* Orange */

.stat-icon.orange {
  background: #fff2dd;
  color: #e88a16;
}


/* Red */

.stat-icon.red {
  background: #ffe8e8;
  color: #e05252;
}


/* Green */

.stat-icon.green {
  background: #e5f8ed;
  color: #16864a;
}


/* =========================
   Content
========================= */

.stat-content {
  min-width: 0;
}


.stat-content p {

  margin: 0;

  color: #7b8497;

  font-size: 12px;

}


.stat-content h2 {

  margin: 5px 0;

  font-size: 23px;

  color: #172033;

}


/* =========================
   Change
========================= */

.increase,
.decrease {

  display: flex;

  align-items: center;

  gap: 3px;

  font-size: 11px;

  font-weight: 600;

}


.increase {

  color: #16864a;

}


.decrease {

  color: #16864a;

}


.increase i,
.decrease i {

  font-size: 10px;

}


.stat-content small {

  margin-left: 3px;

  color: #9aa1b1;

  font-size: 10px;

  font-weight: normal;

}


/* =========================
   Responsive
========================= */

@media (max-width: 1200px) {

  .stats-grid {

    grid-template-columns:
      repeat(3, minmax(0, 1fr));

  }

}


@media (max-width: 768px) {

  .stats-grid {

    grid-template-columns: 1fr;

  }

}

</style>