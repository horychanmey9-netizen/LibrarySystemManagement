<template>
  <div class="reports-page">

    <div class="page-header">
      <div>
        <h1>Reports</h1>
        <p>View and analyze library statistics</p>
      </div>
    </div>

    <ReportSummaryCards />

    <div class="charts-grid">
      <BorrowingChart />
      <CategoryChart />
    </div>

    <ReportTables />

    <ReportActions />

  </div>
</template>

<script setup>
import ReportSummaryCards from '@/components/admin/reports/ReportSummaryCards.vue'
import BorrowingChart from '@/components/admin/reports/BorrowingChart.vue'
import CategoryChart from '@/components/admin/reports/CategoryChart.vue'
import ReportTables from '@/components/admin/reports/ReportTables.vue'
import ReportActions from '@/components/admin/reports/ReportActions.vue'
</script>

<style scoped>
.reports-page {
  padding: 24px;
  background: #f9fafb;
  min-height: calc(100vh - 70px);
}

.page-header {
  margin-bottom: 24px;
}

.page-header h1 {
  margin: 0;
  color: #111827;
  font-size: 26px;
  font-weight: 700;
}

.page-header p {
  margin: 6px 0 0;
  color: #6b7280;
  font-size: 14px;
}

.charts-grid {
  display: grid;
  grid-template-columns: 1.5fr 1fr;
  gap: 20px;
}

@media (max-width: 1000px) {
  .charts-grid {
    grid-template-columns: 1fr;
  }
}
</style>