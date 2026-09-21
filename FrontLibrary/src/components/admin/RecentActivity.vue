<template>
  <div class="activity-card">

    <!-- =========================================
         HEADER
    ========================================== -->
    <div class="activity-header">
      <div>
        <h3>Recent Activities</h3>
        <p>Latest activities in the library</p>
      </div>

      <button
        class="refresh-btn"
        type="button"
        :disabled="loading"
        @click="fetchActivities"
        title="Refresh"
      >
        <i
          class="bi"
          :class="loading ? 'bi-arrow-repeat spin' : 'bi-arrow-clockwise'"
        ></i>
      </button>
    </div>

    <!-- =========================================
         LOADING
    ========================================== -->
    <div v-if="loading" class="activity-state">
      <div class="loading-spinner"></div>
      <p>Loading activities...</p>
    </div>

    <!-- =========================================
         ERROR
    ========================================== -->
    <div v-else-if="error" class="activity-state error-state">
      <div class="state-icon error-icon">
        <i class="bi bi-exclamation-triangle"></i>
      </div>

      <p>{{ error }}</p>

      <button
        type="button"
        class="retry-btn"
        @click="fetchActivities"
      >
        Try Again
      </button>
    </div>

    <!-- =========================================
         EMPTY
    ========================================== -->
    <div v-else-if="activities.length === 0" class="activity-state">
      <div class="state-icon empty-icon">
        <i class="bi bi-activity"></i>
      </div>

      <p>No recent activities</p>
      <span>Activities will appear here when something happens.</span>
    </div>

    <!-- =========================================
         ACTIVITIES
    ========================================== -->
    <div v-else class="activity-list">

      <div
        v-for="(activity, index) in activities"
        :key="activity.id || `${activity.type}-${index}`"
        class="activity-item"
      >

        <!-- ICON -->
        <div
          class="activity-icon"
          :class="`icon-${activity.color}`"
        >
          <i :class="`bi ${activity.icon}`"></i>
        </div>

        <!-- CONTENT -->
        <div class="activity-content">

          <div class="activity-title-row">
            <h4>{{ activity.title }}</h4>

            <span class="activity-time">
              {{ formatRelativeTime(activity.date) }}
            </span>
          </div>

          <p>{{ activity.description }}</p>

        </div>
      </div>

    </div>

  </div>
</template>

<script setup>
import { ref, onMounted } from "vue";

import { getBooks } from "../../service/bookService";
import { getBorrowings } from "../../service/borrowingService";
import { getUsers } from "../../service/userService";

/* =====================================================
   STATE
===================================================== */

const activities = ref([]);
const loading = ref(false);
const error = ref("");

/* =====================================================
   HELPERS
===================================================== */

/**
 * Return first valid value
 */
function firstValue(...values) {
  for (const value of values) {
    if (
      value !== undefined &&
      value !== null &&
      String(value).trim() !== ""
    ) {
      return value;
    }
  }

  return null;
}

/**
 * Convert backend date into Date object
 */
function parseDate(value) {
  if (!value) return null;

  if (value instanceof Date) {
    return isNaN(value.getTime()) ? null : value;
  }

  const date = new Date(value);

  if (isNaN(date.getTime())) {
    return null;
  }

  return date;
}

/**
 * Find date from common backend fields
 */
function getActivityDate(item) {
  if (!item) return null;

  const possibleDates = [
    item.createdAt,
    item.created_at,

    item.requestedAt,
    item.requested_at,

    item.borrowDate,
    item.borrow_date,

    item.borrowedAt,
    item.borrowed_at,

    item.returnDate,
    item.return_date,

    item.returnedAt,
    item.returned_at,

    item.rejectedAt,
    item.rejected_at,

    item.updatedAt,
    item.updated_at,

    item.date,
    item.timestamp,
  ];

  for (const value of possibleDates) {
    const date = parseDate(value);

    if (date) {
      return date;
    }
  }

  return null;
}

/**
 * Get book title
 */
function getBookTitle(borrowing) {
  return firstValue(
    borrowing?.book?.title,
    borrowing?.bookTitle,
    borrowing?.bookName,
    borrowing?.book?.name,
    borrowing?.title,
    "Unknown Book"
  );
}

/**
 * Get borrower/user name
 */
function getBorrowerName(borrowing) {
  return firstValue(
    borrowing?.user?.name,
    borrowing?.borrower?.name,

    borrowing?.user?.fullName,
    borrowing?.borrower?.fullName,

    borrowing?.userName,
    borrowing?.borrowerName,

    borrowing?.name,

    "Unknown User"
  );
}

/**
 * Normalize status
 */
function getStatus(borrowing) {
  const status = firstValue(
    borrowing?.status,
    borrowing?.borrowingStatus,
    borrowing?.borrowStatus
  );

  if (!status) return "";

  return String(status)
    .trim()
    .toUpperCase()
    .replace(/[\s-]+/g, "_");
}

/**
 * Get a unique ID
 */
function getActivityId(item, prefix = "") {
  return firstValue(
    item?.id,
    item?.borrowingId,
    item?.bookId,
    item?.userId,
    `${prefix}-${Math.random()}`
  );
}

/* =====================================================
   BOOK ACTIVITIES
   Only newest book
===================================================== */

function createBookActivities(books) {
  const bookActivities = books
    .map((book) => {
      const date = getActivityDate(book);

      if (!date) return null;

      return {
        id: `book-${book?.id ?? date.getTime()}`,
        type: "BOOK_ADDED",
        icon: "bi-book",
        color: "blue",
        title: "New Book Added",
        description: `${firstValue(
          book?.title,
          book?.name,
          "Unknown Book"
        )} was added to the library`,
        date,
      };
    })
    .filter(Boolean)
    .sort((a, b) => b.date.getTime() - a.date.getTime());

  // Only newest book
  return bookActivities.length > 0
    ? [bookActivities[0]]
    : [];
}

/* =====================================================
   USER ACTIVITIES
   Only newest user
===================================================== */

function createUserActivities(users) {
  const userActivities = users
    .map((user) => {
      const date = getActivityDate(user);

      if (!date) return null;

      return {
        id: `user-${user?.id ?? date.getTime()}`,
        type: "USER_REGISTERED",
        icon: "bi-person-plus",
        color: "purple",
        title: "New User Registered",
        description: `${firstValue(
          user?.name,
          user?.fullName,
          user?.email,
          "New user"
        )} joined the library`,
        date,
      };
    })
    .filter(Boolean)
    .sort((a, b) => b.date.getTime() - a.date.getTime());

  // Only newest user
  return userActivities.length > 0
    ? [userActivities[0]]
    : [];
}

/* =====================================================
   BORROWING ACTIVITIES
===================================================== */

function createBorrowingActivities(borrowings) {
  const result = [];

  borrowings.forEach((borrowing) => {
    const status = getStatus(borrowing);

    const bookTitle = getBookTitle(borrowing);
    const borrowerName = getBorrowerName(borrowing);

    const date = getActivityDate(borrowing);

    if (!date) return;

    /* =========================================
       PENDING
    ========================================== */

    if (status === "PENDING") {
      result.push({
        id: `pending-${getActivityId(borrowing, "pending")}`,
        type: "BORROWING_REQUEST",
        icon: "bi-clock-history",
        color: "orange",
        title: "Borrowing Request",
        description: `${borrowerName} requested to borrow ${bookTitle}`,
        date,
      });

      return;
    }

    /* =========================================
       BORROWED
    ========================================== */

    if (status === "BORROWED") {
      result.push({
        id: `borrowed-${getActivityId(borrowing, "borrowed")}`,
        type: "BOOK_BORROWED",
        icon: "bi-bookmark-check",
        color: "green",
        title: "Book Borrowed",
        description: `${borrowerName} borrowed ${bookTitle}`,
        date,
      });

      return;
    }

    /* =========================================
       RETURN REQUESTED
    ========================================== */

    if (status === "RETURN_REQUESTED") {
      result.push({
        id: `return-request-${getActivityId(
          borrowing,
          "return-request"
        )}`,
        type: "RETURN_REQUEST",
        icon: "bi-arrow-return-left",
        color: "cyan",
        title: "Return Request",
        description: `${borrowerName} requested to return ${bookTitle}`,
        date,
      });

      return;
    }

    /* =========================================
       RETURNED
    ========================================== */

    if (status === "RETURNED") {
      result.push({
        id: `returned-${getActivityId(borrowing, "returned")}`,
        type: "BOOK_RETURNED",
        icon: "bi-check-circle",
        color: "green",
        title: "Book Returned",
        description: `${bookTitle} was returned by ${borrowerName}`,
        date,
      });

      return;
    }

    /* =========================================
       REJECTED
    ========================================== */

    if (status === "REJECTED") {
      result.push({
        id: `rejected-${getActivityId(borrowing, "rejected")}`,
        type: "BORROWING_REJECTED",
        icon: "bi-x-circle",
        color: "red",
        title: "Borrowing Rejected",
        description: `${borrowerName}'s request for ${bookTitle} was rejected`,
        date,
      });

      return;
    }

    /* =========================================
       OVERDUE
    ========================================== */

    if (status === "OVERDUE") {
      result.push({
        id: `overdue-${getActivityId(borrowing, "overdue")}`,
        type: "BOOK_OVERDUE",
        icon: "bi-exclamation-circle",
        color: "red",
        title: "Book Overdue",
        description: `${bookTitle} is overdue for ${borrowerName}`,
        date,
      });

      return;
    }
  });

  return result;
}

/* =====================================================
   GET LATEST ACTIVITY BY TYPE
===================================================== */

function getLatestByType(activityList) {
  const latest = {};

  activityList.forEach((activity) => {
    if (!activity || !activity.date) return;

    const type = activity.type;

    if (
      !latest[type] ||
      activity.date.getTime() > latest[type].date.getTime()
    ) {
      latest[type] = activity;
    }
  });

  return Object.values(latest);
}

/* =====================================================
   FETCH ACTIVITIES
===================================================== */

async function fetchActivities() {
  loading.value = true;
  error.value = "";

  try {
    const [books, borrowings, users] = await Promise.all([
      getBooks(),
      getBorrowings(),
      getUsers(),
    ]);

    console.log("Recent Activities - Books:", books);
    console.log("Recent Activities - Borrowings:", borrowings);
    console.log("Recent Activities - Users:", users);

    /* =========================================
       CREATE ACTIVITIES
    ========================================== */

    const bookActivities = createBookActivities(
      Array.isArray(books) ? books : []
    );

    const borrowingActivities = createBorrowingActivities(
      Array.isArray(borrowings) ? borrowings : []
    );

    const userActivities = createUserActivities(
      Array.isArray(users) ? users : []
    );

    /* =========================================
       COMBINE
    ========================================== */

    const allActivities = [
      ...bookActivities,
      ...borrowingActivities,
      ...userActivities,
    ];

    /*
     * IMPORTANT:
     *
     * Each activity type only keeps the newest
     * record.
     *
     * Example:
     * BOOK_ADDED       -> 1
     * BORROWING_REQUEST-> 1
     * BOOK_BORROWED    -> 1
     * RETURN_REQUEST   -> 1
     * BOOK_RETURNED    -> 1
     * REJECTED         -> 1
     * OVERDUE          -> 1
     * USER_REGISTERED  -> 1
     */
    const latestActivities = getLatestByType(allActivities);

    /* =========================================
       SORT NEWEST FIRST
    ========================================== */

    latestActivities.sort(
      (a, b) => b.date.getTime() - a.date.getTime()
    );

    /*
     * Maximum number shown in dashboard
     */
    activities.value = latestActivities.slice(0, 8);

    console.log(
      "Recent Activities - Final:",
      activities.value
    );
  } catch (err) {
    console.error(
      "Fetch recent activities error:",
      err
    );

    error.value =
      err?.message ||
      "Unable to load recent activities.";
  } finally {
    loading.value = false;
  }
}

/* =====================================================
   RELATIVE TIME
===================================================== */

function formatRelativeTime(date) {
  if (!date) return "";

  const now = new Date();

  const diff = now.getTime() - date.getTime();

  const seconds = Math.floor(diff / 1000);

  if (seconds < 0) {
    return "Just now";
  }

  if (seconds < 60) {
    return "Just now";
  }

  const minutes = Math.floor(seconds / 60);

  if (minutes < 60) {
    return `${minutes} min${minutes > 1 ? "s" : ""} ago`;
  }

  const hours = Math.floor(minutes / 60);

  if (hours < 24) {
    return `${hours} hour${hours > 1 ? "s" : ""} ago`;
  }

  const days = Math.floor(hours / 24);

  if (days < 7) {
    return `${days} day${days > 1 ? "s" : ""} ago`;
  }

  return date.toLocaleDateString("en-US", {
    month: "short",
    day: "numeric",
    year: "numeric",
  });
}

/* =====================================================
   MOUNT
===================================================== */

onMounted(() => {
  fetchActivities();
});
</script>

<style scoped>
/* =====================================================
   CARD
===================================================== */

.activity-card {
  width: 100%;
  background: #ffffff;
  border: 1px solid #e8eef5;
  border-radius: 16px;
  overflow: hidden;
}

/* =====================================================
   HEADER
===================================================== */

.activity-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 20px 22px;
  border-bottom: 1px solid #eef2f7;
}

.activity-header h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 700;
  color: #172033;
}

.activity-header p {
  margin: 5px 0 0;
  font-size: 13px;
  color: #8a94a6;
}

/* =====================================================
   REFRESH BUTTON
===================================================== */

.refresh-btn {
  width: 36px;
  height: 36px;
  border: 1px solid #e4eaf1;
  background: #f8fafc;
  border-radius: 9px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  color: #64748b;
  transition: 0.2s ease;
}

.refresh-btn:hover {
  background: #eef6ff;
  color: #2563eb;
  border-color: #cfe2ff;
}

.refresh-btn:disabled {
  cursor: not-allowed;
  opacity: 0.6;
}

.spin {
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }
}

/* =====================================================
   ACTIVITY LIST
===================================================== */

.activity-list {
  display: flex;
  flex-direction: column;
}

/* =====================================================
   ACTIVITY ITEM
===================================================== */

.activity-item {
  display: flex;
  align-items: flex-start;
  gap: 14px;
  padding: 17px 22px;
  border-bottom: 1px solid #f0f3f7;
  transition: background 0.2s ease;
}

.activity-item:last-child {
  border-bottom: none;
}

.activity-item:hover {
  background: #fafcff;
}

/* =====================================================
   ICON
===================================================== */

.activity-icon {
  width: 40px;
  height: 40px;
  min-width: 40px;
  border-radius: 11px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 17px;
}

/* Blue */
.icon-blue {
  background: #eaf3ff;
  color: #2563eb;
}

/* Purple */
.icon-purple {
  background: #f2eaff;
  color: #7c3aed;
}

/* Orange */
.icon-orange {
  background: #fff4df;
  color: #d97706;
}

/* Green */
.icon-green {
  background: #e9f9f0;
  color: #16a34a;
}

/* Cyan */
.icon-cyan {
  background: #e6f9fb;
  color: #0891b2;
}

/* Red */
.icon-red {
  background: #ffeded;
  color: #dc2626;
}

/* =====================================================
   CONTENT
===================================================== */

.activity-content {
  flex: 1;
  min-width: 0;
}

.activity-title-row {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 10px;
}

.activity-title-row h4 {
  margin: 0;
  color: #1e293b;
  font-size: 14px;
  font-weight: 650;
  line-height: 1.4;
}

.activity-content p {
  margin: 4px 0 0;
  color: #7b8798;
  font-size: 13px;
  line-height: 1.5;
}

.activity-time {
  flex-shrink: 0;
  font-size: 11px;
  color: #9aa4b2;
  white-space: nowrap;
}

/* =====================================================
   STATES
===================================================== */

.activity-state {
  min-height: 220px;
  padding: 30px 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  text-align: center;
}

.activity-state p {
  margin: 10px 0 0;
  color: #64748b;
  font-size: 14px;
}

.activity-state span {
  margin-top: 4px;
  color: #9aa4b2;
  font-size: 12px;
}

.state-icon {
  width: 46px;
  height: 46px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
}

.empty-icon {
  background: #f1f5f9;
  color: #94a3b8;
}

.error-icon {
  background: #fff1f2;
  color: #e11d48;
}

/* =====================================================
   LOADING
===================================================== */

.loading-spinner {
  width: 30px;
  height: 30px;
  border: 3px solid #e2e8f0;
  border-top-color: #3b82f6;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

/* =====================================================
   RETRY
===================================================== */

.retry-btn {
  margin-top: 12px;
  padding: 7px 14px;
  border: none;
  border-radius: 7px;
  background: #2563eb;
  color: white;
  font-size: 12px;
  cursor: pointer;
}

.retry-btn:hover {
  background: #1d4ed8;
}

/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 640px) {
  .activity-header {
    padding: 16px;
  }

  .activity-item {
    padding: 14px 16px;
    gap: 11px;
  }

  .activity-icon {
    width: 36px;
    height: 36px;
    min-width: 36px;
    font-size: 15px;
  }

  .activity-title-row {
    flex-direction: column;
    gap: 2px;
  }

  .activity-time {
    font-size: 10px;
  }

  .activity-content p {
    font-size: 12px;
  }
}
</style>