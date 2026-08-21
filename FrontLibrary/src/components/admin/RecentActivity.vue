<template>
  <div class="card activity-card">
    <!-- =========================
         Card Header
    ========================== -->
    <div class="card-header">
      <div>
        <h3>Recent Activities</h3>
        <p>Latest system activities</p>
      </div>
    </div>
    <!-- =========================
         Activities
    ========================== -->
    <div class="activities">
      <div
        v-for="activity in activities"
        :key="activity.id"
        class="activity"
      >
        <!-- Activity Icon -->
        <div
          class="activity-icon"
          :class="activity.type"
        >
          <i :class="activity.icon"></i>
        </div>
        <!-- Activity Information -->
        <div class="activity-info">
          <strong>
            {{ activity.title }}
          </strong>
          <span>
            {{ activity.description }}
          </span>
          <small>
            {{ activity.time }}
          </small>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>

const activities = [
  {
    id: 1,
    type: "book",
    icon: "bi bi-book",
    title: "New book added",
    description: "Clean Architecture was added",
    time: "10 minutes ago"
  },
  {
    id: 2,
    type: "borrow",
    icon: "bi bi-journal-bookmark",
    title: "Book borrowed",
    description: "Sok Dara borrowed Clean Code",
    time: "25 minutes ago"
  },
  {
    id: 3,
    type: "return",
    icon: "bi bi-arrow-return-left",
    title: "Book returned",
    description: "Java Programming was returned",
    time: "1 hour ago"
  },
  {
    id: 4,
    type: "fine",
    icon: "bi bi-cash-coin",
    title: "Fine created",
    description: "A $5 fine was created",
    time: "2 hours ago"
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
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.02);
  overflow: hidden;
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
  margin: 0;
  font-size: 16px;
}
.card-header p {
  margin: 5px 0 0;
  font-size: 12px;
  color: #8a92a3;
}
/* =========================
   Activities
========================= */
.activities {
  padding: 10px 20px;
}
.activity {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f1f4;
}
.activity:last-child {
  border-bottom: none;
}
/* =========================
   Activity Icon
========================= */
.activity-icon {
  width: 35px;
  height: 35px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  flex-shrink: 0;
  font-size: 16px;
}
/* Book */
.activity-icon.book {
  background: #eeeaff;
  color: #5b3df5;
}
/* Borrow */
.activity-icon.borrow {
  background: #e7f1ff;
  color: #2878d4;
}
/* Return */
.activity-icon.return {
  background: #e5f8ed;
  color: #16864a;
}
/* Fine */
.activity-icon.fine {
  background: #fff2d8;
  color: #d88916;
}
/* =========================
   Activity Information
========================= */
.activity-info {
  min-width: 0;
}
.activity-info strong {
  display: block;
  font-size: 12px;
}
.activity-info span {
  display: block;
  margin-top: 3px;
  color: #7b8497;
  font-size: 11px;
}
.activity-info small {
  display: block;
  margin-top: 3px;
  color: #a0a6b2;
  font-size: 10px;
}
</style>