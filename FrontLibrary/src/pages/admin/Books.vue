<template>
  <div class="books-page">
    <BookTable />
  </div>
</template>

<script setup>
import BookTable from "@/components/books/BookTable.vue";
</script>

<style scoped>
.books-page {
  min-height: calc(100vh - 70px);
  padding: 30px;
  background: #f8fafc;
}
</style>