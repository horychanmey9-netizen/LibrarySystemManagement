<template>
  <div class="notification-page">
    <AdminNotificationBell/>
  </div>
</template>

<script setup>
import AdminNotificationBell from '../../components/admin/AdminNotificationBell.vue';
</script>

<style scoped>
.notification-page {
  min-height: calc(100vh - 70px);
  padding: 30px;
  background: #f8fafc;
}
</style>