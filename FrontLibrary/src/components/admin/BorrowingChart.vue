<template>
  <div class="card borrowing-card">

    <div class="card-header">
      <div>
        <h3>Borrowing Overview</h3>
        <p>Monthly borrowing statistics</p>
      </div>

      <select v-model="selectedYear">
        <option value="2026">2026</option>
        <option value="2025">2025</option>
        <option value="2024">2024</option>
      </select>
    </div>

    <div class="chart">

      <!-- Y Axis -->
      <div class="chart-y">
        <span>50</span>
        <span>40</span>
        <span>30</span>
        <span>20</span>
        <span>10</span>
        <span>0</span>
      </div>

      <!-- Chart Area -->
      <div class="chart-area">

        <!-- Grid Lines -->
        <div class="grid-line line-1"></div>
        <div class="grid-line line-2"></div>
        <div class="grid-line line-3"></div>
        <div class="grid-line line-4"></div>
        <div class="grid-line line-5"></div>

        <!-- Bars -->
        <div class="bars">

          <div
            v-for="item in borrowingData"
            :key="item.month"
            class="bar-wrapper"
          >

            <div
              class="bar"
              :style="{ height: item.value * 4 + 'px' }"
              :title="`${item.value} borrowings`"
            ></div>

            <span>{{ item.month }}</span>

          </div>

        </div>
      </div>
    </div>

  </div>
</template>

<script setup>

import { ref } from "vue";

const selectedYear = ref("2026");

const borrowingData = [
  { month: "Jan", value: 25 },
  { month: "Feb", value: 32 },
  { month: "Mar", value: 28 },
  { month: "Apr", value: 40 },
  { month: "May", value: 35 },
  { month: "Jun", value: 45 },
  { month: "Jul", value: 38 },
  { month: "Aug", value: 48 }
];

</script>

<style scoped>

.card {
  min-width: 0;
  background: white;
  border: 1px solid #e6e9ef;
  border-radius: 12px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.02);
  overflow: hidden;
}

.card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 15px;
  padding: 20px;
  border-bottom: 1px solid #f0f1f4;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
}

.card-header p {
  margin: 5px 0 0;
  font-size: 12px;
  color: #8a92a3;
}

.card-header select {
  padding: 8px 12px;
  border: 1px solid #dfe3eb;
  border-radius: 7px;
  background: white;
  color: #374151;
  font-size: 13px;
  outline: none;
  cursor: pointer;
}

.card-header select:focus {
  border-color: #5b3df5;
}

.chart {
  height: 280px;
  display: flex;
  padding: 20px;
  box-sizing: border-box;
}

.chart-y {
  width: 35px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding-bottom: 28px;
  color: #9aa1b1;
  font-size: 11px;
  flex-shrink: 0;
}

.chart-area {
  position: relative;
  flex: 1;
  min-width: 0;
  margin-left: 5px;
}

.grid-line {
  position: absolute;
  left: 0;
  right: 0;
  border-top: 1px dashed #e8eaf0;
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
  align-items: flex-end;
  justify-content: space-around;
  padding-top: 5px;
}

.bar-wrapper {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-end;
  gap: 8px;
}

.bar {
  width: 28px;
  max-height: 192px;
  background: #5b3df5;
  border-radius: 6px 6px 0 0;
  transition:
    height 0.25s ease,
    transform 0.2s ease,
    background 0.2s ease;
  cursor: pointer;
}

.bar:hover {
  background: #4930d5;
  transform: translateY(-3px);
}

.bar-wrapper span {
  color: #8a92a3;
  font-size: 11px;
  line-height: 1;
}

@media (max-width: 768px) {

  .card-header {
    align-items: flex-start;
    flex-direction: column;
  }

  .card-header select {
    width: 100%;
  }

  .chart {
    height: 250px;
    padding: 15px;
  }

  .chart-y {
    width: 28px;
    font-size: 10px;
  }

  .bar {
    width: 20px;
  }

}

@media (max-width: 480px) {

  .card-header {
    padding: 15px;
  }

  .card-header h3 {
    font-size: 15px;
  }

  .card-header p {
    font-size: 11px;
  }

  .chart {
    height: 220px;
    padding: 12px;
  }

  .chart-y {
    width: 25px;
  }

  .chart-area {
    margin-left: 3px;
  }

  .bar {
    width: 16px;
  }

  .bar-wrapper {
    gap: 6px;
  }

  .bar-wrapper span {
    font-size: 10px;
  }

}

</style>