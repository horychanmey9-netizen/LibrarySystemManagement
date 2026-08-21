<template>
  <div class="card category-card">

    <div class="card-header">
      <div>
        <h3>Book Categories</h3>
        <p>Books by category</p>
      </div>
    </div>

    <div class="category-content">

      <!-- Donut -->
      <div class="donut">

        <div class="donut-center">
          <strong>1,250</strong>
          <span>Books</span>
        </div>

      </div>

      <!-- Category List -->
      <div class="category-list">

        <div
          v-for="category in categoryData"
          :key="category.name"
          class="category-row"
        >

          <div class="category-name">

            <span
              class="dot"
              :class="category.color"
            ></span>

            {{ category.name }}

          </div>

          <strong>{{ category.value }}</strong>

        </div>

      </div>

    </div>

  </div>
</template>

<script setup>

const categoryData = [
  {
    name: "Programming",
    value: 420,
    color: "purple"
  },
  {
    name: "Database",
    value: 280,
    color: "blue"
  },
  {
    name: "Networking",
    value: 220,
    color: "green"
  },
  {
    name: "Security",
    value: 180,
    color: "orange"
  },
  {
    name: "Others",
    value: 150,
    color: "gray"
  }
];

</script>

<style scoped>

.card {
  min-width: 0;

  background: white;

  border: 1px solid #e6e9ef;

  border-radius: 12px;

  box-shadow:
    0 2px 5px rgba(0, 0, 0, 0.02);

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

.category-content {
  display: flex;

  align-items: center;

  gap: 25px;

  padding: 25px;
}

.donut {
  width: 150px;
  height: 150px;

  border-radius: 50%;

  background:
    conic-gradient(
      #5b3df5 0% 34%,
      #4285f4 34% 56%,
      #22a06b 56% 74%,
      #f59e0b 74% 88%,
      #9ca3af 88% 100%
    );

  display: flex;

  align-items: center;
  justify-content: center;

  flex-shrink: 0;
}

.donut-center {
  width: 95px;
  height: 95px;

  border-radius: 50%;

  background: white;

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;
}

.donut-center strong {
  font-size: 20px;
}

.donut-center span {
  color: #8a92a3;

  font-size: 11px;
}

.category-list {
  flex: 1;

  min-width: 0;
}

.category-row {
  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 10px;

  padding: 8px 0;

  font-size: 12px;
}

.category-name {
  display: flex;

  align-items: center;

  gap: 8px;

  color: #667085;
}

.dot {
  width: 9px;
  height: 9px;

  border-radius: 50%;

  flex-shrink: 0;
}

.dot.purple {
  background: #5b3df5;
}

.dot.blue {
  background: #4285f4;
}

.dot.green {
  background: #22a06b;
}

.dot.orange {
  background: #f59e0b;
}

.dot.gray {
  background: #9ca3af;
}

@media (max-width: 768px) {

  .category-content {
    flex-direction: column;

    align-items: center;
  }

  .category-list {
    width: 100%;
  }

}

</style>