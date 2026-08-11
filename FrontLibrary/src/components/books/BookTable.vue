<template>
  <div class="books-page">

    <!-- Header -->
    <div class="page-header">
        <div>
            <h1>Book Management</h1>
            <p>Manage all library books</p>
        </div>

        <button class="add-btn" @click="openAddBook">
            + Add Book
        </button>
    </div>

    <!-- Search & Filter -->
    <div class="filter-box">

      <!-- Search -->
      <div class="search-box">
        <span class="search-icon">⌕</span>

        <input
          v-model="search"
          type="text"
          placeholder="Search books by title, author or ISBN..."
        />
      </div>

      <!-- Category -->
      <select v-model="selectedCategory">
        <option value="">All Categories</option>

        <option
          v-for="category in categories"
          :key="category"
          :value="category"
        >
          {{ category }}
        </option>
      </select>

      <!-- Status -->
      <select v-model="selectedStatus">
        <option value="">All Status</option>

        <option value="Available">Available</option>
        <option value="Borrowed">Borrowed</option>
        <option value="Overdue">Overdue</option>
      </select>

      <!-- Reset -->
      <button class="reset-btn" @click="resetFilters">
        ↻ Reset
      </button>

    </div>

    <!-- Book Table -->
    <div class="table-container">

      <table>
        <thead>
          <tr>
            <th>#</th>
            <th>BOOK</th>
            <th>AUTHOR</th>
            <th>CATEGORY</th>
            <th>ISBN</th>
            <th>QUANTITY</th>
            <th>STATUS</th>
            <th>ACTION</th>
          </tr>
        </thead>

        <tbody>

          <tr
            v-for="(book, index) in filteredBooks"
            :key="book.id"
          >
            <td>{{ index + 1 }}</td>

            <td>
              <div class="book-info">
                <div class="book-cover">
                  📕
                </div>

                <div>
                  <strong>{{ book.title }}</strong>
                  <small>{{ book.description }}</small>
                </div>
              </div>
            </td>

            <td>{{ book.author }}</td>

            <td>
              <span class="category">
                {{ book.category }}
              </span>
            </td>

            <td>{{ book.isbn }}</td>

            <td>{{ book.quantity }}</td>

            <td>
              <span
                class="status"
                :class="getStatusClass(book.status)"
              >
                {{ book.status }}
              </span>
            </td>

            <td>
              <button class="edit-btn">
                ✏
              </button>

              <button class="delete-btn">
                🗑
              </button>
            </td>

          </tr>

          <!-- No Result -->
          <tr v-if="filteredBooks.length === 0">
            <td colspan="8" class="no-result">
              No books found.
            </td>
          </tr>

        </tbody>
      </table>

    </div>

    <!-- Pagination -->
    <div class="pagination">
      <span>
        Showing {{ filteredBooks.length }} of {{ books.length }} books
      </span>

      <div>
        <button>‹</button>
        <button class="active">1</button>
        <button>2</button>
        <button>3</button>
        <button>›</button>
      </div>
    </div>

  </div>
</template>


<script setup>
import { ref, computed } from 'vue'


// ========================
// Search & Filter
// ========================

const search = ref('')

const selectedCategory = ref('')

const selectedStatus = ref('')


// ========================
// Categories
// ========================

const categories = [
  'Programming',
  'Database',
  'Networking',
  'Security',
  'Web Development'
]


// ========================
// Books Data
// ========================

const books = ref([
  {
    id: 1,
    title: 'Clean Code',
    description: 'A Handbook of Agile Software Craftsmanship',
    author: 'Robert C. Martin',
    category: 'Programming',
    isbn: '978-0132350884',
    quantity: 5,
    status: 'Available'
  },

  {
    id: 2,
    title: 'Java Programming',
    description: 'Comprehensive Guide',
    author: 'John Smith',
    category: 'Programming',
    isbn: '978-0321573513',
    quantity: 3,
    status: 'Borrowed'
  },

  {
    id: 3,
    title: 'Database System Concepts',
    description: 'Database Management',
    author: 'Abraham Silberschatz',
    category: 'Database',
    isbn: '978-0078022159',
    quantity: 4,
    status: 'Available'
  },

  {
    id: 4,
    title: 'Computer Networking',
    description: 'A Top-Down Approach',
    author: 'James F. Kurose',
    category: 'Networking',
    isbn: '978-0133594140',
    quantity: 2,
    status: 'Borrowed'
  },

  {
    id: 5,
    title: 'Web Development',
    description: 'Modern Web Development',
    author: 'David Miller',
    category: 'Web Development',
    isbn: '978-1492052203',
    quantity: 6,
    status: 'Available'
  },

  {
    id: 6,
    title: 'Cyber Security',
    description: 'Introduction to Security',
    author: 'William Stallings',
    category: 'Security',
    isbn: '978-0134091305',
    quantity: 2,
    status: 'Overdue'
  }
])


// ========================
// Filter Books
// ========================

const filteredBooks = computed(() => {

  return books.value.filter(book => {

    // Search
    const searchText = search.value.toLowerCase()

    const matchesSearch =
      book.title.toLowerCase().includes(searchText) ||
      book.author.toLowerCase().includes(searchText) ||
      book.isbn.toLowerCase().includes(searchText)


    // Category
    const matchesCategory =
      selectedCategory.value === '' ||
      book.category === selectedCategory.value


    // Status
    const matchesStatus =
      selectedStatus.value === '' ||
      book.status === selectedStatus.value


    return (
      matchesSearch &&
      matchesCategory &&
      matchesStatus
    )

  })

})


// ========================
// Reset Filters
// ========================

function resetFilters() {

  search.value = ''

  selectedCategory.value = ''

  selectedStatus.value = ''

}


// ========================
// Status Class
// ========================

function getStatusClass(status) {

  if (status === 'Available') {
    return 'available'
  }

  if (status === 'Borrowed') {
    return 'borrowed'
  }

  if (status === 'Overdue') {
    return 'overdue'
  }

  return ''

}
</script>


<style scoped>

/* ========================
   Page
======================== */

.books-page {
  padding: 30px;
  background: #f8f9fc;
  min-height: 100vh;
  font-family: Arial, sans-serif;
}


/* ========================
   Header
======================== */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.page-header h1 {
  margin: 0;
  font-size: 28px;
  color: #172033;
}

.page-header p {
  margin-top: 6px;
  color: #7b8497;
}


/* ========================
   Add Button
======================== */

.add-btn {
  background: #5b3df5;
  color: white;
  border: none;
  padding: 12px 20px;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.add-btn:hover {
  background: #4930d5;
}


/* ========================
   Filter Box
======================== */

.filter-box {
  display: flex;
  gap: 12px;
  margin-bottom: 20px;
  background: white;
  padding: 18px;
  border-radius: 10px;
  border: 1px solid #e5e8ef;
}


/* Search */

.search-box {
  flex: 1;
  position: relative;
}

.search-box input {
  width: 100%;
  box-sizing: border-box;
  padding: 12px 15px 12px 40px;
  border: 1px solid #dfe3eb;
  border-radius: 8px;
  outline: none;
}

.search-box input:focus {
  border-color: #5b3df5;
}

.search-icon {
  position: absolute;
  left: 14px;
  top: 10px;
  font-size: 20px;
}


/* Select */

select {
  width: 180px;
  padding: 12px;
  border: 1px solid #dfe3eb;
  border-radius: 8px;
  background: white;
  cursor: pointer;
}


/* Reset */

.reset-btn {
  padding: 12px 18px;
  border: 1px solid #dfe3eb;
  background: white;
  border-radius: 8px;
  cursor: pointer;
}

.reset-btn:hover {
  background: #f1f2f6;
}


/* ========================
   Table
======================== */

.table-container {
  background: white;
  border-radius: 10px;
  overflow-x: auto;
  border: 1px solid #e5e8ef;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  text-align: left;
  padding: 15px;
  font-size: 12px;
  color: #7c8497;
  background: #fafbfc;
}

td {
  padding: 15px;
  border-top: 1px solid #edf0f5;
  color: #333b4f;
  font-size: 14px;
}


/* ========================
   Book Info
======================== */

.book-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.book-cover {
  width: 38px;
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f0efff;
  border-radius: 5px;
}

.book-info strong {
  display: block;
}

.book-info small {
  color: #8a92a3;
  display: block;
  margin-top: 4px;
}


/* ========================
   Category
======================== */

.category {
  padding: 5px 9px;
  background: #eeeeff;
  color: #5941e8;
  border-radius: 5px;
  font-size: 12px;
}


/* ========================
   Status
======================== */

.status {
  padding: 5px 10px;
  border-radius: 5px;
  font-size: 12px;
  font-weight: 600;
}

.available {
  background: #e5f8ed;
  color: #16864a;
}

.borrowed {
  background: #fff3d8;
  color: #b77900;
}

.overdue {
  background: #ffe5e5;
  color: #d93636;
}


/* ========================
   Actions
======================== */

.edit-btn,
.delete-btn {
  border: none;
  padding: 7px 9px;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 5px;
}

.edit-btn {
  background: #eeeaff;
}

.delete-btn {
  background: #ffe7e7;
}


/* ========================
   No Result
======================== */

.no-result {
  text-align: center;
  padding: 40px;
  color: #888;
}


/* ========================
   Pagination
======================== */

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 18px 5px;
  color: #777;
  font-size: 14px;
}

.pagination button {
  width: 35px;
  height: 35px;
  margin-left: 5px;
  border: 1px solid #ddd;
  background: white;
  border-radius: 6px;
  cursor: pointer;
}

.pagination button.active {
  background: #5b3df5;
  color: white;
  border-color: #5b3df5;
}


/* ========================
   Responsive
======================== */

@media (max-width: 900px) {

  .filter-box {
    flex-direction: column;
  }

  select {
    width: 100%;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

}

</style>