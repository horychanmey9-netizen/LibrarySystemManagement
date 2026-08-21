<template>
  <div
    class="grid grid-cols-1 gap-5 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4"
  >
    <div
      v-for="book in books"
      :key="book.id"
      class="min-w-0"
    >
      <BookCard
        :book="book"
        @view="handleView"
      />
    </div>
  </div>
</template>

<script setup>
import BookCard from './BookCard.vue'

defineProps({
  books: {
    type: Array,
    default: () => []
  }
})

const emit = defineEmits(['view'])

const handleView = (book) => {
  emit('view', book)
}
</script>