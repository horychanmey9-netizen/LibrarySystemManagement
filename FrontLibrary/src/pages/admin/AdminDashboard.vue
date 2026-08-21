<template>
  <div class="dashboard-page">

    <!-- ================= HEADER ================= -->

    <div class="dashboard-header">

      <div>

        <h1>
          Dashboard
        </h1>

        <p>
          Welcome back, Admin! Here's what's happening in your library.
        </p>

      </div>


      <!-- DATE -->

      <button
        class="date-btn"
        type="button"
      >

        <i class="bi bi-calendar3"></i>

        <span>
          August 20, 2026
        </span>

      </button>

    </div>


    <!-- ================= STATISTICS ================= -->

    <DashboardCard />


    <!-- ================= CHARTS ================= -->

    <div class="analytics-grid">

      <BorrowingChart />

      <CategoryChart />

    </div>


    <!-- ================= BOTTOM ================= -->

    <div class="bottom-grid">

      <TransactionTable />

      <RecentActivity />

    </div>


    <!-- ================= OVERDUE ================= -->

    <OverdueBooks />

  </div>
</template>


<script setup>

import DashboardCard from "@/components/admin/DashboardCard.vue";

import BorrowingChart from "@/components/admin/BorrowingChart.vue";

import CategoryChart from "@/components/admin/CategoryChart.vue";

import TransactionTable from "@/components/admin/TransactionTable.vue";

import RecentActivity from "@/components/admin/RecentActivity.vue";

import OverdueBooks from "@/components/admin/OverdueBooks.vue";

</script>


<style scoped>

/* =================================================
   DASHBOARD PAGE
================================================= */

.dashboard-page {

  width: 100%;

  padding: 30px;

  box-sizing: border-box;

  background: #f8f9fc;

  font-family: Arial, sans-serif;

  color: #172033;

}


/* =================================================
   HEADER
================================================= */

.dashboard-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 20px;

  margin-bottom: 25px;

}


.dashboard-header h1 {

  margin: 0;

  font-size: 28px;

  font-weight: 700;

  color: #172033;

}


.dashboard-header p {

  margin: 7px 0 0;

  color: #7b8497;

  font-size: 14px;

}


/* =================================================
   DATE BUTTON
================================================= */

.date-btn {

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 8px;

  padding: 10px 15px;

  border: 1px solid #e1e5ec;

  border-radius: 8px;

  background: #ffffff;

  color: #667085;

  cursor: pointer;

  white-space: nowrap;

  font-size: 14px;

  transition: 0.2s;

}


.date-btn i {

  font-size: 15px;

  color: #2563eb;

}


.date-btn:hover {

  background: #f8fafc;

  border-color: #cbd5e1;

}


/* =================================================
   ANALYTICS
================================================= */

.analytics-grid {

  display: grid;

  grid-template-columns:
    minmax(0, 2fr)
    minmax(300px, 1fr);

  gap: 20px;

  margin-bottom: 20px;

}


/* =================================================
   BOTTOM
================================================= */

.bottom-grid {

  display: grid;

  grid-template-columns:
    minmax(0, 1.6fr)
    minmax(300px, 1fr);

  gap: 20px;

  margin-bottom: 20px;

}


/* =================================================
   RESPONSIVE - 1200px
================================================= */

@media (max-width: 1200px) {

  .analytics-grid {

    grid-template-columns: 1fr;

  }


  .bottom-grid {

    grid-template-columns: 1fr;

  }

}


/* =================================================
   RESPONSIVE - 768px
================================================= */

@media (max-width: 768px) {

  .dashboard-page {

    padding: 20px;

  }


  .dashboard-header {

    flex-direction: column;

    align-items: flex-start;

  }

}


/* =================================================
   RESPONSIVE - 550px
================================================= */

@media (max-width: 550px) {

  .dashboard-page {

    padding: 15px;

  }


  .dashboard-header h1 {

    font-size: 24px;

  }


  .dashboard-header p {

    font-size: 13px;

  }


  .date-btn {

    width: 100%;

  }

}

</style>