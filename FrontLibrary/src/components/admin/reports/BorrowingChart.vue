<template>
  <div class="chart-card">

    <div class="chart-header">
      <div>
        <h2>Borrowing Statistics</h2>
        <p>Monthly borrowing activity</p>
      </div>

      <select v-model="selectedYear">
        <option>2026</option>
        <option>2025</option>
        <option>2024</option>
      </select>
    </div>

    <div class="chart">

      <div class="y-axis">
        <span>150</span>
        <span>120</span>
        <span>90</span>
        <span>60</span>
        <span>30</span>
        <span>0</span>
      </div>

      <div class="chart-area">

        <div class="grid-line line-1"></div>
        <div class="grid-line line-2"></div>
        <div class="grid-line line-3"></div>
        <div class="grid-line line-4"></div>
        <div class="grid-line line-5"></div>

        <div class="bars">

          <div
            v-for="item in borrowingData"
            :key="item.month"
            class="bar-item"
          >

            <div class="bar-wrapper">
              <div
                class="bar"
                :style="{ height: `${(item.value / 150) * 100}%` }"
                :title="`${item.month}: ${item.value}`"
              ></div>
            </div>

            <span>{{ item.month }}</span>

          </div>

        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { ref } from 'vue'

const selectedYear = ref('2026')

const borrowingData = [
  { month: 'Jan', value: 85 },
  { month: 'Feb', value: 110 },
  { month: 'Mar', value: 95 },
  { month: 'Apr', value: 130 },
  { month: 'May', value: 105 },
  { month: 'Jun', value: 120 },
  { month: 'Jul', value: 90 },
  { month: 'Aug', value: 115 },
  { month: 'Sep', value: 100 },
  { month: 'Oct', value: 125 },
  { month: 'Nov', value: 140 },
  { month: 'Dec', value: 120 }
]
</script>

<style scoped>
.chart-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.chart-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.chart-header h2 {
  margin: 0;
  color: #111827;
  font-size: 18px;
}

.chart-header p {
  margin: 5px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.chart-header select {
  padding: 8px 14px;
  border: 1px solid #d1d5db;
  border-radius: 8px;
  background: #fff;
  cursor: pointer;
}

.chart {
  display: flex;
  height: 300px;
}

.y-axis {
  width: 40px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 28px;
}

.y-axis span {
  font-size: 11px;
  color: #9ca3af;
}

.chart-area {
  position: relative;
  flex: 1;
  border-left: 1px solid #e5e7eb;
  border-bottom: 1px solid #e5e7eb;
}

.grid-line {
  position: absolute;
  left: 0;
  right: 0;
  border-top: 1px dashed #e5e7eb;
}

.line-1 {
  top: 0;
}

.line-2 {
  top: 20%;
}

.line-3 {
  top: 40%;
}

.line-4 {
  top: 60%;
}

.line-5 {
  top: 80%;
}

.bars {
  position: absolute;
  inset: 0;
  display: flex;
  justify-content: space-around;
  align-items: flex-end;
  padding: 0 10px;
}

.bar-item {
  height: 100%;
  flex: 1;
  max-width: 55px;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: center;
  gap: 8px;
}

.bar-wrapper {
  height: calc(100% - 25px);
  width: 55%;
  display: flex;
  align-items: flex-end;
}

.bar {
  width: 100%;
  background: #2563eb;
  border-radius: 6px 6px 0 0;
  min-height: 4px;
  transition: 0.3s;
}

.bar:hover {
  opacity: 0.75;
}

.bar-item span {
  font-size: 11px;
  color: #6b7280;
}

@media (max-width: 768px) {
  .bar-item:nth-child(n+7) {
    display: none;
  }
}
</style>