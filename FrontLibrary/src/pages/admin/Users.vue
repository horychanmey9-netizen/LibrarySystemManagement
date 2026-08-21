<template>
  <div class="users-page">

    <!-- =========================
         Header
    ========================== -->
    <div class="page-header">

      <div>
        <h1>Users</h1>
        <p>Manage all users in the library system</p>
      </div>

    </div>


    <!-- =========================
         Search
    ========================== -->
    <div class="toolbar">

      <div class="search-box">

        <i class="bi bi-search search-icon"></i>

        <input
          v-model="search"
          type="text"
          placeholder="Search users..."
        />

      </div>

    </div>


    <!-- =========================
         Users Table
    ========================== -->
    <div class="table-card">

      <div class="table-wrapper">

        <table>

          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Role</th>
              <th>Created Date</th>
              <th>Action</th>
            </tr>
          </thead>


          <tbody>

            <tr
              v-for="user in filteredUsers"
              :key="user.id"
            >

              <!-- ID -->
              <td>
                {{ user.id }}
              </td>


              <!-- Name -->
              <td>

                <div class="user-name">

                  <div class="avatar">
                    {{ getInitial(user.name) }}
                  </div>

                  <span>
                    {{ user.name }}
                  </span>

                </div>

              </td>


              <!-- Email -->
              <td>
                {{ user.email }}
              </td>


              <!-- Phone -->
              <td>
                {{ user.phone }}
              </td>


              <!-- Role -->
              <td>

                <span
                  class="role-badge"
                  :class="getRoleClass(user.role)"
                >
                  {{ user.role }}
                </span>

              </td>


              <!-- Created Date -->
              <td>
                {{ user.createdDate }}
              </td>


              <!-- Action -->
              <td>

                <div class="action-buttons">

                  <!-- View -->
                  <button
                    class="action-btn view"
                    title="View"
                    @click="viewUser(user)"
                  >
                    <i class="bi bi-eye"></i>
                  </button>


                  <!-- Delete -->
                  <button
                    class="action-btn delete"
                    title="Delete"
                    @click="deleteUser(user)"
                  >
                    <i class="bi bi-trash"></i>
                  </button>

                </div>

              </td>

            </tr>


            <!-- Empty -->
            <tr v-if="filteredUsers.length === 0">

              <td
                colspan="7"
                class="empty"
              >
                No users found.
              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>


    <!-- =========================
         View User Modal
    ========================== -->
    <div
      v-if="showViewModal"
      class="modal-overlay"
      @click.self="showViewModal = false"
    >

      <div class="modal">

        <!-- Modal Header -->
        <div class="modal-header">

          <div>
            <h2>User Details</h2>
            <p>View user information</p>
          </div>


          <button
            class="close-btn"
            @click="showViewModal = false"
            title="Close"
          >
            <i class="bi bi-x-lg"></i>
          </button>

        </div>


        <!-- User Details -->
        <div
          v-if="selectedUser"
          class="user-details"
        >

          <!-- Avatar -->
          <div class="detail-avatar">
            {{ getInitial(selectedUser.name) }}
          </div>


          <!-- Name -->
          <div class="detail-item">

            <span>Name</span>

            <strong>
              {{ selectedUser.name }}
            </strong>

          </div>


          <!-- Email -->
          <div class="detail-item">

            <span>Email</span>

            <strong>
              {{ selectedUser.email }}
            </strong>

          </div>


          <!-- Phone -->
          <div class="detail-item">

            <span>Phone</span>

            <strong>
              {{ selectedUser.phone }}
            </strong>

          </div>


          <!-- Role -->
          <div class="detail-item">

            <span>Role</span>

            <strong>
              {{ selectedUser.role }}
            </strong>

          </div>


          <!-- Created Date -->
          <div class="detail-item">

            <span>Created Date</span>

            <strong>
              {{ selectedUser.createdDate }}
            </strong>

          </div>

        </div>


        <!-- Modal Actions -->
        <div class="modal-actions">

          <button
            class="cancel-btn"
            @click="showViewModal = false"
          >
            Close
          </button>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>

import { ref, computed } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();


/* =========================
   Users Data
========================= */

const users = ref([

  {
    id: 1,
    name: "Sreynob",
    email: "admin@gmail.com",
    phone: "012 345 678",
    role: "ADMIN",
    createdDate: "2026-08-01",
  },

  {
    id: 2,
    name: "Dara",
    email: "dara@gmail.com",
    phone: "010 234 567",
    role: "USER",
    createdDate: "2026-08-03",
  },

  {
    id: 3,
    name: "Lina",
    email: "lina@gmail.com",
    phone: "097 456 789",
    role: "USER",
    createdDate: "2026-08-05",
  },

  {
    id: 4,
    name: "Sokha",
    email: "sokha@gmail.com",
    phone: "096 123 456",
    role: "USER",
    createdDate: "2026-08-06",
  },

]);


/* =========================
   Search
========================= */

const search = ref("");


const filteredUsers = computed(() => {

  const keyword =
    search.value
      .toLowerCase()
      .trim();


  if (!keyword) {

    return users.value;

  }


  return users.value.filter((user) => {

    return (

      user.name
        .toLowerCase()
        .includes(keyword)

      ||

      user.email
        .toLowerCase()
        .includes(keyword)

      ||

      user.phone
        .toLowerCase()
        .includes(keyword)

      ||

      user.role
        .toLowerCase()
        .includes(keyword)

    );

  });

});


/* =========================
   View Modal
========================= */

const showViewModal = ref(false);

const selectedUser = ref(null);


/* =========================
   Go To Add User
========================= */

function goToAddUser() {

  router.push("/admin/users/add");

}


/* =========================
   View User
========================= */

function viewUser(user) {

  selectedUser.value = user;

  showViewModal.value = true;

}


/* =========================
   Delete User
========================= */

function deleteUser(user) {

  const confirmed = window.confirm(
    `Are you sure you want to delete ${user.name}?`
  );


  if (!confirmed) {

    return;

  }


  users.value = users.value.filter(
    (item) => item.id !== user.id
  );

}


/* =========================
   Helpers
========================= */

function getInitial(name) {

  return name
    ? name.charAt(0).toUpperCase()
    : "?";

}


function getRoleClass(role) {

  return role.toLowerCase();

}

</script>


<style scoped>

/* =========================
   Page
========================= */

.users-page {

  min-height: calc(100vh - 70px);

  padding: 30px;

  background: #f8fafc;

  box-sizing: border-box;

}


/* =========================
   Header
========================= */

.page-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  margin-bottom: 24px;

}


.page-header h1 {

  margin: 0;

  font-size: 26px;

  font-weight: 700;

  color: #1f2937;

}


.page-header p {

  margin: 6px 0 0;

  color: #6b7280;

  font-size: 14px;

}


/* =========================
   Search
========================= */

.toolbar {

  margin-bottom: 18px;

}


.search-box {

  width: 320px;

  height: 42px;

  background: white;

  border: 1px solid #d1d5db;

  border-radius: 7px;

  display: flex;

  align-items: center;

  padding: 0 12px;

  box-sizing: border-box;

}


.search-icon {

  color: #6b7280;

  font-size: 15px;

  margin-right: 9px;

}


.search-box input {

  width: 100%;

  border: none;

  outline: none;

  font-size: 14px;

  color: #374151;

}


/* =========================
   Table
========================= */

.table-card {

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 10px;

  overflow: hidden;

}


.table-wrapper {

  width: 100%;

  overflow-x: auto;

}


table {

  width: 100%;

  border-collapse: collapse;

  min-width: 900px;

}


thead {

  background: #f9fafb;

}


th {

  text-align: left;

  padding: 15px 18px;

  font-size: 13px;

  font-weight: 600;

  color: #6b7280;

  border-bottom: 1px solid #e5e7eb;

  white-space: nowrap;

}


td {

  padding: 15px 18px;

  font-size: 14px;

  color: #374151;

  border-bottom: 1px solid #f1f5f9;

}


tbody tr:hover {

  background: #f9fafb;

}


/* =========================
   User
========================= */

.user-name {

  display: flex;

  align-items: center;

  gap: 10px;

  font-weight: 600;

  color: #1f2937;

}


.avatar {

  width: 34px;

  height: 34px;

  border-radius: 50%;

  background: #dbeafe;

  color: #2563eb;

  display: flex;

  align-items: center;

  justify-content: center;

  font-weight: 700;

}


/* =========================
   Role
========================= */

.role-badge {

  display: inline-flex;

  padding: 5px 10px;

  border-radius: 20px;

  font-size: 12px;

  font-weight: 600;

}


.role-badge.admin {

  background: #ede9fe;

  color: #7c3aed;

}


.role-badge.user {

  background: #f3f4f6;

  color: #4b5563;

}


/* =========================
   Actions
========================= */

.action-buttons {

  display: flex;

  align-items: center;

  gap: 6px;

}


.action-btn {

  width: 32px;

  height: 32px;

  border: 1px solid #e5e7eb;

  border-radius: 6px;

  background: white;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  transition: 0.2s;

}


.action-btn i {

  font-size: 14px;

}


/* View */

.action-btn.view {

  color: #2563eb;

}


.action-btn.view:hover {

  background: #eff6ff;

}


/* Delete */

.action-btn.delete {

  color: #dc2626;

}


.action-btn.delete:hover {

  background: #fef2f2;

}


/* =========================
   Empty
========================= */

.empty {

  text-align: center;

  padding: 40px;

  color: #9ca3af;

}


/* =========================
   Modal Overlay
========================= */

.modal-overlay {

  position: fixed;

  inset: 0;

  background: rgba(15, 23, 42, 0.45);

  display: flex;

  align-items: center;

  justify-content: center;

  z-index: 1000;

  padding: 20px;

}


/* =========================
   Modal
========================= */

.modal {

  width: 100%;

  max-width: 500px;

  max-height: 90vh;

  overflow-y: auto;

  background: white;

  border-radius: 12px;

  padding: 24px;

  box-sizing: border-box;

  box-shadow:
    0 20px 50px rgba(0, 0, 0, 0.15);

}


/* =========================
   Modal Header
========================= */

.modal-header {

  display: flex;

  justify-content: space-between;

  align-items: flex-start;

  margin-bottom: 22px;

}


.modal-header h2 {

  margin: 0;

  font-size: 20px;

  color: #1f2937;

}


.modal-header p {

  margin: 5px 0 0;

  color: #6b7280;

  font-size: 13px;

}


/* Close */

.close-btn {

  width: 32px;

  height: 32px;

  border: none;

  background: transparent;

  color: #6b7280;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 6px;

}


.close-btn:hover {

  background: #f3f4f6;

  color: #374151;

}


.close-btn i {

  font-size: 16px;

}


/* =========================
   User Details
========================= */

.user-details {

  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 16px;

}


/* Detail Avatar */

.detail-avatar {

  grid-column: 1 / -1;

  width: 70px;

  height: 70px;

  border-radius: 50%;

  background: #dbeafe;

  color: #2563eb;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 28px;

  font-weight: 700;

  margin: 0 auto 10px;

}


/* Detail Item */

.detail-item {

  padding: 12px;

  background: #f9fafb;

  border-radius: 7px;

}


.detail-item span {

  display: block;

  color: #6b7280;

  font-size: 12px;

  margin-bottom: 5px;

}


.detail-item strong {

  color: #1f2937;

  font-size: 14px;

}


/* =========================
   Modal Actions
========================= */

.modal-actions {

  display: flex;

  justify-content: flex-end;

  gap: 10px;

  margin-top: 24px;

}


.cancel-btn {

  height: 42px;

  padding: 0 18px;

  border-radius: 7px;

  background: white;

  border: 1px solid #d1d5db;

  color: #374151;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

}


.cancel-btn:hover {

  background: #f9fafb;

}


/* =========================
   Responsive
========================= */

@media (max-width: 768px) {

  .users-page {

    padding: 20px;

  }


  .page-header {

    align-items: flex-start;

    gap: 15px;

    flex-direction: column;

  }


  .search-box {

    width: 100%;

  }


  .user-details {

    grid-template-columns: 1fr;

  }


  .detail-avatar {

    grid-column: auto;

  }

}

</style>