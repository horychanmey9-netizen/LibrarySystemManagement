<template>
  <div class="card recent-card">

    <div class="card-header">

      <div>
        <h3>Recent Borrowings</h3>
        <p>Latest book borrowing activities</p>
      </div>

      <RouterLink to="/admin/borrowings">
        View All →
      </RouterLink>

    </div>

    <div class="table-container">

      <table>

        <thead>
          <tr>
            <th>USER</th>
            <th>BOOK</th>
            <th>DATE</th>
            <th>STATUS</th>
          </tr>
        </thead>

        <tbody>

          <tr
            v-for="borrow in recentBorrowings"
            :key="borrow.id"
          >

            <td>

              <div class="user-cell">

                <div class="user-avatar">
                  {{ borrow.user.charAt(0) }}
                </div>

                <span>{{ borrow.user }}</span>

              </div>

            </td>

            <td>{{ borrow.book }}</td>

            <td>{{ borrow.date }}</td>

            <td>

              <span
                class="status"
                :class="borrow.status.toLowerCase()"
              >
                {{ borrow.status }}
              </span>

            </td>

          </tr>

        </tbody>

      </table>

    </div>

  </div>
</template>

<script setup>

const recentBorrowings = [
  {
    id: 1,
    user: "Sok Dara",
    book: "Clean Code",
    date: "Aug 12, 2026",
    status: "Borrowed"
  },
  {
    id: 2,
    user: "Chan Vichea",
    book: "Java Programming",
    date: "Aug 11, 2026",
    status: "Returned"
  },
  {
    id: 3,
    user: "Dara Kim",
    book: "Database System Concepts",
    date: "Aug 11, 2026",
    status: "Borrowed"
  },
  {
    id: 4,
    user: "Sokha Mean",
    book: "Computer Networking",
    date: "Aug 10, 2026",
    status: "Returned"
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

.card-header a {
  color: #5b3df5;

  text-decoration: none;

  font-size: 13px;

  font-weight: 600;

  white-space: nowrap;
}

.table-container {
  width: 100%;

  overflow-x: auto;
}

table {
  width: 100%;

  min-width: 500px;

  border-collapse: collapse;
}

th {
  text-align: left;

  padding: 13px 20px;

  background: #fafbfc;

  color: #8a92a3;

  font-size: 10px;
}

td {
  padding: 13px 20px;

  border-top: 1px solid #f0f1f4;

  color: #4b5565;

  font-size: 12px;
}

.user-cell {
  display: flex;

  align-items: center;

  gap: 8px;

  font-weight: 600;
}

.user-avatar {
  width: 30px;
  height: 30px;

  display: flex;

  align-items: center;
  justify-content: center;

  border-radius: 50%;

  background: #eeeaff;

  color: #5b3df5;

  font-weight: 700;
}

.status {
  padding: 5px 9px;

  border-radius: 5px;

  font-size: 10px;

  font-weight: 600;
}

.status.borrowed {
  background: #fff2d8;

  color: #b77900;
}

.status.returned {
  background: #e5f8ed;

  color: #16864a;
}

</style>