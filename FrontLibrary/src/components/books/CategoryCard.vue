<template>
  <div class="category-card">
    <div class="icon-box" :style="{ background: category.bg }">
      <i :class="category.icon" :style="{ color: category.color }"></i>
    </div>
    <div class="card-text">
      <h3 class="card-title">{{ category.name }}</h3>
      <p class="card-count">{{ category.count }} Books</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "CategoryCard",
  props: {
    category: {
      type: Object,
      required: true
    }
  }
};
</script>

<style scoped>
.category-card {
  display: flex;
  align-items: center;
  gap: 0.7rem;
  background: #fff;
  border: 1px solid #eee;
  border-radius: 12px;
  padding: 0.6rem 1rem;
  flex: 1;
  min-width: 150px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.04);
  transition: box-shadow 0.2s ease, transform 0.2s ease;
  cursor: pointer;
}

.category-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  transform: translateY(-2px);
}

.icon-box {
  width: 40px;
  height: 40px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.card-text {
  display: flex;
  flex-direction: column;
}

.card-title {
  font-size: 0.9rem;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.card-count {
  font-size: 0.78rem;
  color: #9ca3af;
  margin: 0;
}
</style>