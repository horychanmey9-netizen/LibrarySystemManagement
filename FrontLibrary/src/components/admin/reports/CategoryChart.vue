<template>
  <div class="category-card">

    <div class="card-header">
      <div>
        <h2>Books by Category</h2>
        <p>Distribution of books</p>
      </div>
    </div>

    <div class="category-content">

      <!-- Donut -->
      <div
        class="donut"
        :style="{
          background: `conic-gradient(
            #2563eb 0% 35%,
            #16a34a 35% 58%,
            #f59e0b 58% 75%,
            #9333ea 75% 88%,
            #ef4444 88% 100%
          )`
        }"
      >
        <div class="donut-center">
          <strong>{{ totalBooks }}</strong>
          <span>Books</span>
        </div>
      </div>

      <!-- Legend -->
      <div class="legend">

        <div
          v-for="category in categories"
          :key="category.name"
          class="legend-item"
        >
          <div class="legend-left">
            <span
              class="legend-dot"
              :style="{ background: category.color }"
            ></span>

            <span>{{ category.name }}</span>
          </div>

          <strong>{{ category.value }}%</strong>
        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
import { computed } from 'vue'

const categories = [
  {
    name: 'Technology',
    value: 35,
    color: '#2563eb'
  },
  {
    name: 'Science',
    value: 23,
    color: '#16a34a'
  },
  {
    name: 'Business',
    value: 17,
    color: '#f59e0b'
  },
  {
    name: 'Literature',
    value: 13,
    color: '#9333ea'
  },
  {
    name: 'Other',
    value: 12,
    color: '#ef4444'
  }
]

const totalBooks = computed(() => 1250)
</script>

<style scoped>
.category-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.card-header h2 {
  margin: 0;
  color: #111827;
  font-size: 18px;
}

.card-header p {
  margin: 5px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.category-content {
  display: flex;
  align-items: center;
  justify-content: space-around;
  gap: 30px;
  margin-top: 30px;
}

.donut {
  width: 190px;
  height: 190px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.donut-center {
  width: 120px;
  height: 120px;
  background: white;
  border-radius: 50%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.donut-center strong {
  font-size: 25px;
  color: #111827;
}

.donut-center span {
  color: #6b7280;
  font-size: 12px;
}

.legend {
  flex: 1;
}

.legend-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 0;
  border-bottom: 1px solid #f3f4f6;
}

.legend-left {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #374151;
  font-size: 13px;
}

.legend-dot {
  width: 10px;
  height: 10px;
  border-radius: 50%;
}

.legend-item strong {
  color: #111827;
  font-size: 13px;
}

@media (max-width: 600px) {
  .category-content {
    flex-direction: column;
  }
}
</style>