<template>

  <div class="users-page">

    <!-- =========================
         HEADER
    ========================== -->

    <div class="page-header">

      <div class="header-title">

        <div class="header-icon">
          <i class="bi bi-people-fill"></i>
        </div>

        <div>
          <h1>
            Users
          </h1>

          <p>
            Manage all users in the library system
          </p>
        </div>

      </div>

    </div>


    <!-- =========================
         SUMMARY CARDS
    ========================== -->

    <div
      v-if="!loading && !error"
      class="summary-grid"
    >

      <!-- TOTAL -->

      <div class="summary-card">

        <div class="summary-icon total">
          <i class="bi bi-people-fill"></i>
        </div>

        <div class="summary-info">

          <span>
            Total Users
          </span>

          <strong>
            {{ users.length }}
          </strong>

        </div>

      </div>


      <!-- ACTIVE -->

      <div class="summary-card">

        <div class="summary-icon active">
          <i class="bi bi-person-check-fill"></i>
        </div>

        <div class="summary-info">

          <span>
            Active Users
          </span>

          <strong>
            {{ activeUsers }}
          </strong>

        </div>

      </div>


      <!-- INACTIVE -->

      <div class="summary-card">

        <div class="summary-icon inactive">
          <i class="bi bi-person-x-fill"></i>
        </div>

        <div class="summary-info">

          <span>
            Inactive Users
          </span>

          <strong>
            {{ inactiveUsers }}
          </strong>

        </div>

      </div>


      <!-- ADMIN -->

      <div class="summary-card">

        <div class="summary-icon admin">
          <i class="bi bi-shield-lock-fill"></i>
        </div>

        <div class="summary-info">

          <span>
            Administrators
          </span>

          <strong>
            {{ adminUsers }}
          </strong>

        </div>

      </div>

    </div>


    <!-- =========================
         TOOLBAR
    ========================== -->

    <div class="toolbar">

      <div class="search-box">

        <i class="bi bi-search search-icon"></i>

        <input
          v-model="search"
          type="text"
          placeholder="Search users..."
        />

        <button
          v-if="search"
          type="button"
          class="clear-search"
          @click="search = ''"
          aria-label="Clear search"
        >
          <i class="bi bi-x-circle-fill"></i>
        </button>

      </div>

      <div class="result-count">

        <i class="bi bi-person-lines-fill"></i>

        <span>
          {{ filteredUsers.length }}
          {{ filteredUsers.length === 1 ? "user" : "users" }}
        </span>

      </div>

    </div>


    <!-- =========================
         LOADING
    ========================== -->

    <div
      v-if="loading"
      class="state-message"
    >

      <i class="bi bi-arrow-repeat loading-icon"></i>

      <span>
        Loading users...
      </span>

    </div>


    <!-- =========================
         ERROR
    ========================== -->

    <div
      v-else-if="error"
      class="state-message error"
    >

      <div class="error-icon">
        <i class="bi bi-exclamation-triangle-fill"></i>
      </div>

      <strong>
        Unable to load users
      </strong>

      <span>
        {{ error }}
      </span>

      <button
        type="button"
        @click="loadUsers"
      >
        <i class="bi bi-arrow-clockwise"></i>
        Try Again
      </button>

    </div>


    <!-- =========================
         USER TABLE
    ========================== -->

    <UserTable
      v-else
      :users="filteredUsers"
      @view="viewUser"
      @delete="deleteUser"
      @activate="activateUserHandler"
    />


    <!-- =========================
         VIEW USER MODAL
    ========================== -->

    <UserDetailModal
      :user="selectedUser"
      @close="closeModal"
    />

  </div>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from "vue";


import UserTable
  from "../../components/admin/UserTable.vue";


import UserDetailModal
  from "../../components/admin/UserDetailModal.vue";


import {
  getUsers,
  deactivateUser,
  activateUser
} from "../../service/userService";



/* =========================
   USERS
========================= */

const users = ref([]);



/* =========================
   LOADING
========================= */

const loading = ref(false);



/* =========================
   ERROR
========================= */

const error = ref("");



/* =========================
   SEARCH
========================= */

const search = ref("");



/* =========================
   SELECTED USER
========================= */

const selectedUser = ref(null);



/* =========================
   ACTIVE USERS
========================= */

const activeUsers = computed(() => {

  return users.value.filter(
    user => user.active
  ).length;

});



/* =========================
   INACTIVE USERS
========================= */

const inactiveUsers = computed(() => {

  return users.value.filter(
    user => !user.active
  ).length;

});



/* =========================
   ADMIN USERS
========================= */

const adminUsers = computed(() => {

  return users.value.filter(
    user =>
      String(user.role || "")
        .toUpperCase() === "ADMIN"
  ).length;

});



/* =========================
   LOAD USERS
========================= */

async function loadUsers() {

  loading.value = true;

  error.value = "";

  try {

    const data =
      await getUsers();

    users.value =
      Array.isArray(data)
        ? data
        : [];

  } catch (err) {

    console.error(
      "Failed to load users:",
      err
    );

    error.value =
      err?.message ||
      "Failed to load users.";

  } finally {

    loading.value = false;

  }

}



/* =========================
   FILTER USERS
========================= */

const filteredUsers = computed(() => {

  const keyword =
    search.value
      .toLowerCase()
      .trim();


  if (!keyword) {

    return users.value;

  }


  return users.value.filter(
    (user) => {

      return (

        String(user.id ?? "")
          .toLowerCase()
          .includes(keyword)

        ||

        String(user.name ?? "")
          .toLowerCase()
          .includes(keyword)

        ||

        String(user.email ?? "")
          .toLowerCase()
          .includes(keyword)

        ||

        String(user.phone ?? "")
          .toLowerCase()
          .includes(keyword)

        ||

        String(user.role ?? "")
          .toLowerCase()
          .includes(keyword)

        ||

        String(
          user.active
            ? "active"
            : "inactive"
        )
          .toLowerCase()
          .includes(keyword)

        ||

        formatDate(user.createdAt)
          .toLowerCase()
          .includes(keyword)

      );

    }
  );

});



/* =========================
   FORMAT DATE
========================= */

function formatDate(date) {

  if (!date) {

    return "-";

  }


  const parsedDate =
    new Date(date);


  if (
    Number.isNaN(
      parsedDate.getTime()
    )
  ) {

    return "-";

  }


  return parsedDate.toLocaleDateString(
    "en-GB",
    {
      day: "2-digit",
      month: "2-digit",
      year: "numeric"
    }
  );

}



/* =========================
   VIEW USER
========================= */

function viewUser(user) {

  selectedUser.value =
    user;

}



/* =========================
   CLOSE MODAL
========================= */

function closeModal() {

  selectedUser.value =
    null;

}



/* =========================
   DEACTIVATE USER
========================= */

async function deleteUser(user) {

  if (!user?.id) {

    window.alert(
      "Invalid user."
    );

    return;

  }


  if (!user.active) {

    window.alert(
      "This account is already inactive."
    );

    return;

  }


  const confirmed =
    window.confirm(
      `Are you sure you want to deactivate ${user.name}'s account?`
    );


  if (!confirmed) {

    return;

  }


  try {

    await deactivateUser(
      user.id
    );


    await loadUsers();


    if (
      selectedUser.value?.id ===
      user.id
    ) {

      selectedUser.value =
        null;

    }


    window.alert(
      `${user.name}'s account has been deactivated successfully.`
    );

  } catch (err) {

    console.error(
      "Failed to deactivate user:",
      err
    );


    window.alert(
      err?.message ||
      "Failed to deactivate user."
    );

  }

}



/* =========================
   ACTIVATE USER
========================= */

async function activateUserHandler(user) {

  if (!user?.id) {

    window.alert(
      "Invalid user."
    );

    return;

  }


  if (user.active) {

    window.alert(
      "This account is already active."
    );

    return;

  }


  const confirmed =
    window.confirm(
      `Are you sure you want to activate ${user.name}'s account?`
    );


  if (!confirmed) {

    return;

  }


  try {

    await activateUser(
      user.id
    );


    await loadUsers();


    window.alert(
      `${user.name}'s account has been activated successfully.`
    );

  } catch (err) {

    console.error(
      "Failed to activate user:",
      err
    );


    window.alert(
      err?.message ||
      "Failed to activate user."
    );

  }

}



/* =========================
   INITIAL LOAD
========================= */

onMounted(() => {

  loadUsers();

});

</script>


<style scoped>

/* =========================
   PAGE
========================= */

.users-page {

  min-height: calc(100vh - 70px);

  padding: 50px 60px;

  background: #f8fafc;

  box-sizing: border-box;

}


/* =========================
   HEADER
========================= */

.page-header {

  margin-bottom: 28px;

}


.header-title {

  display: flex;

  align-items: center;

  gap: 14px;

}


.header-icon {

  width: 46px;

  height: 46px;

  border-radius: 12px;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #eef2ff;

  color: #4f46e5;

  font-size: 21px;

}


.page-header h1 {

  margin: 0;

  color: #1e293b;

  font-size: 27px;

  font-weight: 700;

}


.page-header p {

  margin: 5px 0 0;

  color: #64748b;

  font-size: 14px;

}


/* =========================
   SUMMARY
========================= */

.summary-grid {

  display: grid;

  grid-template-columns:
    repeat(4, 1fr);

  gap: 16px;

  margin-bottom: 24px;

}


.summary-card {

  min-height: 104px;

  padding: 20px;

  box-sizing: border-box;

  display: flex;

  align-items: center;

  gap: 14px;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 12px;

  transition: 0.2s;

}


.summary-card:hover {

  border-color: #dbeafe;

  box-shadow:
    0 8px 24px
    rgba(15, 23, 42, 0.06);

  transform: translateY(-1px);

}


.summary-icon {

  width: 44px;

  height: 44px;

  flex-shrink: 0;

  border-radius: 10px;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 19px;

}


/* TOTAL */

.summary-icon.total {

  background: #eef2ff;

  color: #4f46e5;

}


/* ACTIVE */

.summary-icon.active {

  background: #ecfdf5;

  color: #16a34a;

}


/* INACTIVE */

.summary-icon.inactive {

  background: #fef2f2;

  color: #dc2626;

}


/* ADMIN */

.summary-icon.admin {

  background: #f5f3ff;

  color: #7c3aed;

}


.summary-info {

  min-width: 0;

}


.summary-info span {

  display: block;

  margin-bottom: 5px;

  color: #64748b;

  font-size: 13px;

}


.summary-info strong {

  display: block;

  color: #1e293b;

  font-size: 23px;

  font-weight: 700;

}


/* =========================
   TOOLBAR
========================= */

.toolbar {

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 15px;

  margin-bottom: 18px;

}


.search-box {

  width: 400px;

  height: 43px;

  display: flex;

  align-items: center;

  padding: 0 12px;

  background: white;

  border: 1px solid #d1d5db;

  border-radius: 9px;

  box-sizing: border-box;

  transition: 0.2s;

}


.search-box:focus-within {

  border-color: #818cf8;

  box-shadow:
    0 0 0 3px
    rgba(99, 102, 241, 0.1);

}


.search-icon {

  margin-right: 9px;

  color: #94a3b8;

  font-size: 15px;

}


.search-box input {

  width: 100%;

  border: none;

  outline: none;

  background: transparent;

  color: #334155;

  font-size: 14px;

}


.search-box input::placeholder {

  color: #94a3b8;

}


.clear-search {

  border: none;

  background: transparent;

  color: #94a3b8;

  cursor: pointer;

  padding: 3px;

}


.clear-search:hover {

  color: #64748b;

}


.result-count {

  display: flex;

  align-items: center;

  gap: 7px;

  color: #64748b;

  font-size: 13px;

}


.result-count i {

  color: #94a3b8;

}


/* =========================
   LOADING / ERROR
========================= */

.state-message {

  min-height: 240px;

  padding: 30px;

  box-sizing: border-box;

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  gap: 9px;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 12px;

  color: #64748b;

  font-size: 14px;

}


.loading-icon {

  font-size: 25px;

  animation: spin 1s linear infinite;

}


@keyframes spin {

  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }

}


.state-message.error {

  color: #64748b;

}


.error-icon {

  width: 48px;

  height: 48px;

  border-radius: 50%;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #fef2f2;

  color: #dc2626;

  font-size: 20px;

}


.state-message.error strong {

  color: #1e293b;

  font-size: 15px;

}


.state-message.error button {

  margin-top: 6px;

  padding: 8px 15px;

  display: flex;

  align-items: center;

  gap: 7px;

  border: none;

  border-radius: 7px;

  background: #4f46e5;

  color: white;

  cursor: pointer;

  font-size: 13px;

  font-weight: 600;

}


.state-message.error button:hover {

  background: #4338ca;

}


/* =========================
   RESPONSIVE
========================= */

@media (max-width: 1100px) {

  .summary-grid {

    grid-template-columns:
      repeat(2, 1fr);

  }

}


@media (max-width: 768px) {

  .users-page {

    padding: 25px 20px;

  }


  .page-header {

    margin-bottom: 22px;

  }


  .summary-grid {

    grid-template-columns: 1fr 1fr;

    gap: 12px;

  }


  .summary-card {

    padding: 15px;

  }


  .toolbar {

    flex-direction: column;

    align-items: stretch;

  }


  .search-box {

    width: 100%;

  }


  .result-count {

    justify-content: flex-end;

  }

}


@media (max-width: 480px) {

  .summary-grid {

    grid-template-columns: 1fr;

  }


  .header-icon {

    width: 42px;

    height: 42px;

  }


  .page-header h1 {

    font-size: 23px;

  }

}

</style>