<template>
  <div class="notification-page">

    <!-- =========================
         Header
    ========================== -->
    <div class="page-header">
      <div>
        <h1>Notifications</h1>
        <p>Manage and monitor library activities</p>
      </div>

      <button
        v-if="unreadCount > 0"
        class="mark-all-btn"
        @click="markAllAsRead"
      >
        <span class="check-icon">✓</span>
        Mark all as read
      </button>
    </div>


    <!-- =========================
         Summary Cards
    ========================== -->
    <div class="summary-grid">

      <!-- Total -->
      <div class="summary-card">
        <div class="summary-icon total">
          <span class="notification-symbol">N</span>
        </div>

        <div>
          <h3>{{ notifications.length }}</h3>
          <p>Total Notifications</p>
        </div>
      </div>


      <!-- Unread -->
      <div class="summary-card">
        <div class="summary-icon unread">
          <span class="notification-symbol">U</span>
        </div>

        <div>
          <h3>{{ unreadCount }}</h3>
          <p>Unread Notifications</p>
        </div>
      </div>


      <!-- Important -->
      <div class="summary-card">
        <div class="summary-icon important">
          <span class="notification-symbol">!</span>
        </div>

        <div>
          <h3>{{ importantCount }}</h3>
          <p>Important</p>
        </div>
      </div>

    </div>


    <!-- =========================
         Filter
    ========================== -->
    <div class="notification-toolbar">

      <div class="filter-buttons">

        <button
          :class="{ active: activeFilter === 'all' }"
          @click="activeFilter = 'all'"
        >
          All
          <span>{{ notifications.length }}</span>
        </button>

        <button
          :class="{ active: activeFilter === 'unread' }"
          @click="activeFilter = 'unread'"
        >
          Unread
          <span>{{ unreadCount }}</span>
        </button>

        <button
          :class="{ active: activeFilter === 'important' }"
          @click="activeFilter = 'important'"
        >
          Important
          <span>{{ importantCount }}</span>
        </button>

        <button
          :class="{ active: activeFilter === 'read' }"
          @click="activeFilter = 'read'"
        >
          Read
          <span>{{ readCount }}</span>
        </button>

      </div>

    </div>


    <!-- =========================
         Notification List
    ========================== -->
    <div class="notification-list">

      <div
        v-for="notification in filteredNotifications"
        :key="notification.id"
        class="notification-item"
        :class="{
          unread: !notification.read,
          important: notification.important
        }"
        @click="markAsRead(notification)"
      >

        <!-- Notification Icon -->
        <div
          class="notification-icon"
          :class="notification.type"
        >

          <span
            v-if="notification.type === 'borrowing'"
            class="icon-letter"
          >
            B
          </span>

          <span
            v-else-if="notification.type === 'return'"
            class="icon-letter"
          >
            R
          </span>

          <span
            v-else-if="notification.type === 'overdue'"
            class="icon-letter"
          >
            O
          </span>

          <span
            v-else-if="notification.type === 'user'"
            class="icon-letter"
          >
            U
          </span>

          <span
            v-else-if="notification.type === 'fine'"
            class="icon-letter"
          >
            F
          </span>

          <span
            v-else-if="notification.type === 'book'"
            class="icon-letter"
          >
            B
          </span>

          <span
            v-else
            class="icon-letter"
          >
            N
          </span>

        </div>


        <!-- Notification Content -->
        <div class="notification-content">

          <div class="title-row">

            <h3>
              {{ notification.title }}
            </h3>

            <span
              v-if="notification.important"
              class="important-badge"
            >
              Important
            </span>

            <span
              v-if="!notification.read"
              class="unread-dot"
            ></span>

          </div>

          <p>
            {{ notification.message }}
          </p>

          <div class="notification-meta">

            <span>
              {{ notification.time }}
            </span>

            <span class="separator">
              |
            </span>

            <span>
              {{ notification.category }}
            </span>

          </div>

        </div>


        <!-- Actions -->
        <div class="notification-actions">

          <button
            v-if="!notification.read"
            class="read-btn"
            title="Mark as read"
            @click.stop="markAsRead(notification)"
          >
            ✓
          </button>

          <button
            class="delete-btn"
            title="Delete"
            @click.stop="deleteNotification(notification.id)"
          >
            Delete
          </button>

        </div>

      </div>


      <!-- =========================
           Empty State
      ========================== -->
      <div
        v-if="filteredNotifications.length === 0"
        class="empty-state"
      >

        <div class="empty-icon">
          <span>N</span>
        </div>

        <h3>
          No notifications
        </h3>

        <p>
          There are no
          {{ activeFilter === "all" ? "" : activeFilter }}
          notifications.
        </p>

      </div>

    </div>

  </div>
</template>


<script setup>
import { ref, computed } from "vue";


// ========================================
// Filter
// ========================================

const activeFilter = ref("all");


// ========================================
// Notifications
// ========================================

const notifications = ref([

  {
    id: 1,
    title: "New Borrowing Request",
    message:
      'Student "Dara Sok" has requested to borrow "Clean Code". Please review the request.',
    type: "borrowing",
    category: "Borrowing",
    time: "10 minutes ago",
    read: false,
    important: true,
  },

  {
    id: 2,
    title: "Book Returned",
    message:
      'Student "Sokha Chan" returned the book "JavaScript Basics".',
    type: "return",
    category: "Return",
    time: "30 minutes ago",
    read: false,
    important: false,
  },

  {
    id: 3,
    title: "Overdue Book",
    message:
      '"The Great Gatsby" has been overdue for 5 days. Please check the borrowing record.',
    type: "overdue",
    category: "Overdue",
    time: "2 hours ago",
    read: false,
    important: true,
  },

  {
    id: 4,
    title: "New User Registered",
    message:
      'A new user "Sreynob Hory" has registered in the library system.',
    type: "user",
    category: "Users",
    time: "3 hours ago",
    read: true,
    important: false,
  },

  {
    id: 5,
    title: "Unpaid Fine",
    message:
      'User "Dara Sok" has an unpaid fine of $5.00.',
    type: "fine",
    category: "Fines",
    time: "Yesterday",
    read: false,
    important: true,
  },

  {
    id: 6,
    title: "Low Book Stock",
    message:
      '"Introduction to Database Systems" has only 2 copies remaining.',
    type: "book",
    category: "Books",
    time: "Yesterday",
    read: true,
    important: true,
  },

  {
    id: 7,
    title: "Book Returned Successfully",
    message:
      '"Learning Vue.js" was successfully returned by the borrower.',
    type: "return",
    category: "Return",
    time: "2 days ago",
    read: true,
    important: false,
  },

  {
    id: 8,
    title: "New User Registered",
    message:
      "A new member has successfully registered in the library system.",
    type: "user",
    category: "Users",
    time: "3 days ago",
    read: true,
    important: false,
  }

]);


// ========================================
// Computed
// ========================================

const unreadCount = computed(() => {

  return notifications.value.filter(
    notification => !notification.read
  ).length;

});


const readCount = computed(() => {

  return notifications.value.filter(
    notification => notification.read
  ).length;

});


const importantCount = computed(() => {

  return notifications.value.filter(
    notification => notification.important
  ).length;

});


const filteredNotifications = computed(() => {

  if (activeFilter.value === "unread") {

    return notifications.value.filter(
      notification => !notification.read
    );

  }

  if (activeFilter.value === "read") {

    return notifications.value.filter(
      notification => notification.read
    );

  }

  if (activeFilter.value === "important") {

    return notifications.value.filter(
      notification => notification.important
    );

  }

  return notifications.value;

});


// ========================================
// Mark Single Notification As Read
// ========================================

const markAsRead = (notification) => {

  notification.read = true;

};


// ========================================
// Mark All As Read
// ========================================

const markAllAsRead = () => {

  notifications.value.forEach(notification => {

    notification.read = true;

  });

};


// ========================================
// Delete Notification
// ========================================

const deleteNotification = (id) => {

  notifications.value =
    notifications.value.filter(
      notification => notification.id !== id
    );

};

</script>


<style scoped>

/* ========================================
   Page
======================================== */

.notification-page {
  min-height: 100vh;
  padding: 30px;
  background: #f8fafc;
}


/* ========================================
   Header
======================================== */

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.page-header h1 {
  margin: 0;
  color: #1e293b;
  font-size: 28px;
  font-weight: 700;
}

.page-header p {
  margin: 6px 0 0;
  color: #64748b;
  font-size: 14px;
}


/* ========================================
   Mark All Button
======================================== */

.mark-all-btn {
  border: none;
  background: #2563eb;
  color: white;

  padding: 11px 18px;

  border-radius: 8px;

  cursor: pointer;

  font-size: 14px;
  font-weight: 600;

  display: flex;
  align-items: center;
  gap: 7px;

  transition: 0.2s;
}

.mark-all-btn:hover {
  background: #1d4ed8;
}

.check-icon {
  font-size: 14px;
}


/* ========================================
   Summary
======================================== */

.summary-grid {
  display: grid;

  grid-template-columns:
    repeat(3, minmax(0, 1fr));

  gap: 20px;

  margin-bottom: 25px;
}


.summary-card {
  display: flex;
  align-items: center;

  gap: 15px;

  padding: 20px;

  background: white;

  border: 1px solid #e2e8f0;

  border-radius: 12px;
}


.summary-icon {
  width: 48px;
  height: 48px;

  min-width: 48px;

  display: flex;
  align-items: center;
  justify-content: center;

  border-radius: 10px;

  font-size: 17px;
  font-weight: 700;
}


.summary-icon.total {
  background: #eff6ff;
  color: #2563eb;
}


.summary-icon.unread {
  background: #ecfdf5;
  color: #059669;
}


.summary-icon.important {
  background: #fff7ed;
  color: #ea580c;
}


.summary-card h3 {
  margin: 0;

  font-size: 23px;

  color: #1e293b;
}


.summary-card p {
  margin: 4px 0 0;

  font-size: 13px;

  color: #64748b;
}


/* ========================================
   Toolbar
======================================== */

.notification-toolbar {
  padding: 15px 20px;

  background: white;

  border: 1px solid #e2e8f0;

  border-radius: 12px 12px 0 0;
}


.filter-buttons {
  display: flex;

  gap: 10px;
}


.filter-buttons button {
  border: none;

  background: transparent;

  padding: 8px 15px;

  border-radius: 7px;

  color: #64748b;

  cursor: pointer;

  font-size: 14px;

  font-weight: 500;
}


.filter-buttons button:hover {
  background: #f1f5f9;
}


.filter-buttons button.active {
  background: #eff6ff;

  color: #2563eb;

  font-weight: 600;
}


.filter-buttons span {
  margin-left: 5px;

  font-size: 12px;
}


/* ========================================
   Notification List
======================================== */

.notification-list {
  background: white;

  border: 1px solid #e2e8f0;

  border-top: none;

  border-radius: 0 0 12px 12px;

  overflow: hidden;
}


/* ========================================
   Notification Item
======================================== */

.notification-item {
  display: flex;

  align-items: flex-start;

  gap: 15px;

  padding: 20px;

  border-bottom: 1px solid #f1f5f9;

  cursor: pointer;

  transition: 0.2s;
}


.notification-item:last-child {
  border-bottom: none;
}


.notification-item:hover {
  background: #f8fafc;
}


.notification-item.unread {
  background: #f8fbff;
}


.notification-item.important {
  border-left: 3px solid #f59e0b;
}


/* ========================================
   Notification Icon
======================================== */

.notification-icon {
  width: 45px;
  height: 45px;

  min-width: 45px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  font-size: 15px;
  font-weight: 700;
}


.notification-icon.borrowing {
  background: #eff6ff;
  color: #2563eb;
}


.notification-icon.return {
  background: #ecfdf5;
  color: #059669;
}


.notification-icon.overdue {
  background: #fff7ed;
  color: #ea580c;
}


.notification-icon.user {
  background: #f5f3ff;
  color: #7c3aed;
}


.notification-icon.fine {
  background: #fef2f2;
  color: #dc2626;
}


.notification-icon.book {
  background: #f0fdfa;
  color: #0f766e;
}


.icon-letter {
  display: flex;

  align-items: center;

  justify-content: center;

  width: 100%;

  height: 100%;
}


/* ========================================
   Content
======================================== */

.notification-content {
  flex: 1;
}


.title-row {
  display: flex;

  align-items: center;

  gap: 8px;

  flex-wrap: wrap;
}


.notification-content h3 {
  margin: 0;

  color: #1e293b;

  font-size: 15px;

  font-weight: 600;
}


.notification-content p {
  margin: 7px 0;

  color: #64748b;

  font-size: 14px;

  line-height: 1.5;
}


/* ========================================
   Important Badge
======================================== */

.important-badge {
  padding: 3px 8px;

  border-radius: 20px;

  background: #fff7ed;

  color: #ea580c;

  font-size: 11px;

  font-weight: 600;
}


/* ========================================
   Unread Dot
======================================== */

.unread-dot {
  width: 8px;
  height: 8px;

  border-radius: 50%;

  background: #2563eb;
}


/* ========================================
   Meta
======================================== */

.notification-meta {
  display: flex;

  align-items: center;

  gap: 7px;

  color: #94a3b8;

  font-size: 12px;
}


.separator {
  color: #cbd5e1;
}


/* ========================================
   Actions
======================================== */

.notification-actions {
  display: flex;

  gap: 7px;
}


.notification-actions button {
  height: 34px;

  border: none;

  border-radius: 7px;

  cursor: pointer;

  font-size: 13px;

  font-weight: 600;
}


.read-btn {
  width: 34px;

  background: #eff6ff;

  color: #2563eb;
}


.read-btn:hover {
  background: #dbeafe;
}


.delete-btn {
  padding: 0 10px;

  background: #fef2f2;

  color: #dc2626;
}


.delete-btn:hover {
  background: #fee2e2;
}


/* ========================================
   Empty State
======================================== */

.empty-state {
  text-align: center;

  padding: 70px 20px;
}


.empty-icon {
  width: 70px;
  height: 70px;

  margin: 0 auto 15px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 50%;

  background: #f1f5f9;

  color: #64748b;

  font-size: 22px;

  font-weight: 700;
}


.empty-state h3 {
  margin: 0 0 8px;

  color: #334155;
}


.empty-state p {
  margin: 0;

  color: #94a3b8;

  font-size: 14px;
}


/* ========================================
   Responsive
======================================== */

@media (max-width: 900px) {

  .summary-grid {
    grid-template-columns: 1fr;
  }

}


@media (max-width: 768px) {

  .notification-page {
    padding: 20px;
  }


  .page-header {
    flex-direction: column;

    align-items: flex-start;

    gap: 15px;
  }


  .notification-item {
    padding: 15px;
  }


  .notification-actions {
    flex-direction: column;
  }

}


@media (max-width: 500px) {

  .filter-buttons {
    overflow-x: auto;
  }


  .notification-item {
    gap: 10px;
  }


  .notification-icon {
    width: 40px;
    height: 40px;

    min-width: 40px;
  }

}

</style>