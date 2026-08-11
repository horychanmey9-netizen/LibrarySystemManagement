<template>
  <div class="home-page">
    <!-- Categories -->
    <section class="section">
      <div class="section-header">
        <h2>Categories</h2>
        <router-link to="/browse-books">View all</router-link>
      </div>
      <div class="category-row">
        <CategoryCard
          v-for="cat in categories"
          :key="cat.id"
          :category="cat"
        />
      </div>
    </section>

    <!-- Recommended Books -->
    <section class="section">
      <div class="section-header">
        <h2>Recommended Books</h2>
        <router-link to="/browse-books">View all</router-link>
      </div>
      <div class="book-row">
        <BookCard
          v-for="book in books"
          :key="book.id"
          :book="book"
        />
      </div>
    </section>
  </div>
</template>

<script>
import CategoryCard from "../../components/books/CategoryCard.vue";
import BookCard from "../../components/books/BookCard.vue";



export default {
  name: "Home",
  components: { CategoryCard, BookCard },
  data() {
    return {
      categories: [
        { id: 1, name: "Fiction", count: 320, icon: "bi bi-book", bg: "#F3E8FF", color: "#9333EA" },
        { id: 2, name: "Science", count: 180, icon: "bi bi-eyedropper", bg: "#DCFCE7", color: "#16A34A" },
        { id: 3, name: "Technology", count: 150, icon: "bi bi-laptop", bg: "#DBEAFE", color: "#2563EB" },
        { id: 4, name: "History", count: 120, icon: "bi bi-bank", bg: "#FFEDD5", color: "#EA580C" },
        { id: 5, name: "Others", count: 478, icon: "bi bi-journal", bg: "#F1F5F9", color: "#475569" }
      ],
      books: []
    };
  },
  mounted() {
    // this.fetchRecommendedBooks();
  },
  methods: {
    async fetchRecommendedBooks() {
      try {
        const res = await fetch("http://localhost:8080/api/books/recommended");
        this.books = await res.json();
      } catch (error) {
        console.error("Error fetching books:", error);
      }
    }
  }
};
</script>

<style scoped>
.top-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.welcome {
  font-size: 1.3rem;
  margin: 0;
}

.subtitle {
  color: #64748b;
  font-size: 0.9rem;
  margin: 0.2rem 0 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.8rem;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
}

.search-bar {
  display: flex;
  align-items: center;
  gap: 0.6rem;
  background: #fff;
  border: 1px solid #e2e8f0;
  border-radius: 10px;
  padding: 0.5rem 1rem;
  margin-bottom: 1.8rem;
}

.search-bar input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 0.9rem;
}

.search-bar button {
  background: #2563eb;
  color: #fff;
  border: none;
  padding: 0.5rem 1.2rem;
  border-radius: 8px;
  cursor: pointer;
}

.section {
  margin-bottom: 2rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.8rem;
}

.section-header h2 {
  font-size: 1rem;
  margin: 0;
}

.section-header a {
  font-size: 0.85rem;
  color: #2563eb;
  text-decoration: none;
}

.category-row,
.book-row {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.home-page {
  padding: 1.5rem 2rem;   /* ជំនួស container-fluid */
  width: 100%;
}
</style>