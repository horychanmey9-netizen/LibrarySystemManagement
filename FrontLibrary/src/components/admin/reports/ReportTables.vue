<template>
  <div class="tables-container">

    <!-- Most Borrowed Books -->
    <div class="table-card">

      <div class="table-header">
        <div>
          <h2>Most Borrowed Books</h2>
          <p>Books with the highest borrowing activity</p>
        </div>

        <button @click="viewAllBooks">
          View All
        </button>
      </div>

      <div class="table-wrapper">
        <table>
          <thead>
            <tr>
              <th>#</th>
              <th>Book</th>
              <th>Category</th>
              <th>Borrowed</th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="(book, index) in mostBorrowedBooks"
              :key="book.title"
            >
              <td>{{ index + 1 }}</td>

              <td>
                <strong>{{ book.title }}</strong>
              </td>

              <td>
                <span class="category-badge">
                  {{ book.category }}
                </span>
              </td>

              <td>
                <strong>{{ book.borrowed }}</strong>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>


    <!-- Most Active Users -->
    <div class="table-card">

      <div class="table-header">
        <div>
          <h2>Most Active Users</h2>
          <p>Users with the highest borrowing activity</p>
        </div>

        <button @click="viewAllUsers">
          View All
        </button>
      </div>

      <div class="table-wrapper">
        <table>

          <thead>
            <tr>
              <th>#</th>
              <th>User</th>
              <th>Borrowings</th>
              <th>Returns</th>
            </tr>
          </thead>

          <tbody>
            <tr
              v-for="(user, index) in activeUsers"
              :key="user.name"
            >
              <td>{{ index + 1 }}</td>

              <td>
                <div class="user-cell">
                  <div class="avatar">
                    {{ user.name.charAt(0) }}
                  </div>

                  <strong>{{ user.name }}</strong>
                </div>
              </td>

              <td>{{ user.borrowings }}</td>

              <td>{{ user.returns }}</td>
            </tr>
          </tbody>

        </table>
      </div>

    </div>


    <!-- Fine Summary -->
    <div class="table-card">

      <div class="table-header">
        <div>
          <h2>Fine Summary</h2>
          <p>Overview of library fines</p>
        </div>
      </div>

      <div class="fine-grid">

        <div class="fine-item">
          <span>Total Fines</span>
          <strong>$125.00</strong>
        </div>

        <div class="fine-item">
          <span>Paid Fines</span>
          <strong>$80.00</strong>
        </div>

        <div class="fine-item">
          <span>Unpaid Fines</span>
          <strong>$45.00</strong>
        </div>

      </div>

    </div>

  </div>
</template>

<script setup>
const mostBorrowedBooks = [
  {
    title: 'Clean Code',
    category: 'Technology',
    borrowed: 45
  },
  {
    title: 'Java Programming',
    category: 'Technology',
    borrowed: 38
  },
  {
    title: 'The Great Gatsby',
    category: 'Literature',
    borrowed: 32
  },
  {
    title: 'Business Basics',
    category: 'Business',
    borrowed: 28
  },
  {
    title: 'Introduction to Science',
    category: 'Science',
    borrowed: 25
  }
]

const activeUsers = [
  {
    name: 'Dara',
    borrowings: 25,
    returns: 23
  },
  {
    name: 'Sokha',
    borrowings: 21,
    returns: 20
  },
  {
    name: 'Rina',
    borrowings: 18,
    returns: 17
  },
  {
    name: 'Vanna',
    borrowings: 16,
    returns: 15
  },
  {
    name: 'Mony',
    borrowings: 14,
    returns: 14
  }
]

const viewAllBooks = () => {
  console.log('View all books')
}

const viewAllUsers = () => {
  console.log('View all users')
}
</script>

<style scoped>
.tables-container {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-top: 24px;
}

.table-card {
  background: #ffffff;
  border: 1px solid #e5e7eb;
  border-radius: 14px;
  padding: 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
}

.table-card:last-child {
  grid-column: 1 / -1;
}

.table-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.table-header h2 {
  margin: 0;
  color: #111827;
  font-size: 18px;
}

.table-header p {
  margin: 5px 0 0;
  color: #6b7280;
  font-size: 13px;
}

.table-header button {
  background: transparent;
  border: none;
  color: #2563eb;
  font-size: 13px;
  cursor: pointer;
  font-weight: 600;
}

.table-wrapper {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th {
  text-align: left;
  padding: 12px;
  background: #f9fafb;
  color: #6b7280;
  font-size: 12px;
  font-weight: 600;
}

td {
  padding: 14px 12px;
  border-bottom: 1px solid #f3f4f6;
  color: #374151;
  font-size: 13px;
}

.category-badge {
  background: #eff6ff;
  color: #2563eb;
  padding: 5px 9px;
  border-radius: 6px;
  font-size: 11px;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 10px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background: #e5e7eb;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 600;
  color: #374151;
}

.fine-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 15px;
}

.fine-item {
  background: #f9fafb;
  border-radius: 10px;
  padding: 18px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.fine-item span {
  color: #6b7280;
  font-size: 13px;
}

.fine-item strong {
  color: #111827;
  font-size: 22px;
}

@media (max-width: 900px) {
  .tables-container {
    grid-template-columns: 1fr;
  }

  .table-card:last-child {
    grid-column: auto;
  }
}

@media (max-width: 600px) {
  .fine-grid {
    grid-template-columns: 1fr;
  }
}
</style>