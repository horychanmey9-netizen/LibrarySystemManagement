<template>
  <div class="book-grid">
    <BookCard
      v-for="book in books"
      :key="book.id"
      :book="book"
      @view-details="handleViewDetails"
    />
  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";
// import axios from "axios"; // ❌ បិទបណ្តោះអាសន្ន (ប្រើ Fake data ជំនួស)
import BookCard from "./BookCard.vue";

const books = ref([]);

// ✅ Fake data សម្រាប់សាកល្បង design (comment ចេញពេលចង់ប្រើ backend ពិត)
const fakeBooks = [
  {
    id: 1,
    title: "The Great Gatsby",
    author: "F. Scott Fitzgerald",
    cover: "https://covers.openlibrary.org/b/id/8432047-L.jpg",
    rating: 4.5,
    reviews: 128
  },
  {
    id: 2,
    title: "1984",
    author: "George Orwell",
    cover: "https://covers.openlibrary.org/b/id/7222246-L.jpg",
    rating: 4.8,
    reviews: 342
  },
  {
    id: 3,
    title: "To Kill a Mockingbird",
    author: "Harper Lee",
    cover: "https://covers.openlibrary.org/b/id/8231856-L.jpg",
    rating: 4.7,
    reviews: 210
  },
  {
    id: 4,
    title: "Pride and Prejudice",
    author: "Jane Austen",
    cover: "https://covers.openlibrary.org/b/id/8479394-L.jpg",
    rating: 4.6,
    reviews: 178
  },
  {
    id: 5,
    title: "The Hobbit",
    author: "J.R.R. Tolkien",
    cover: "https://covers.openlibrary.org/b/id/6979861-L.jpg",
    rating: 4.9,
    reviews: 512
  },
  {
    id: 6,
    title: "Sapiens",
    author: "Yuval Noah Harari",
    cover: "https://covers.openlibrary.org/b/id/8406786-L.jpg",
    rating: 4.4,
    reviews: 95
  }
];

// --- Real API fetch (កូដចាស់ រក្សាទុកមិនលុប — កុំលុប!) ---
// const fetchBooks = async () => {
//   try {
//     const res = await axios.get("http://localhost:8080/api/books");
//     books.value = res.data;
//   } catch (err) {
//     console.error("Fetch books error:", err);
//   }
// };

onMounted(() => {
  // ✅ ប្រើ Fake data ដើម្បីមើល design (កំពុងប្រើសកម្ម)
  books.value = fakeBooks;

  // 👉 ពេលចង់ភ្ជាប់ backend ពិតវិញ៖
  // 1. Uncomment "import axios" ខាងលើ
  // 2. Uncomment function fetchBooks() ខាងលើ
  // 3. Comment បន្ទាត់ "books.value = fakeBooks;" ខាងលើនេះ
  // 4. Uncomment បន្ទាត់ខាងក្រោម
  // fetchBooks();
});

const handleViewDetails = (book) => {
  console.log("View details:", book);
};
</script>

<style scoped>
.book-grid {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  padding: 1rem;
}
</style>