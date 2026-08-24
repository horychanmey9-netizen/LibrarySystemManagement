<template>
  <div class="table-card">

    <div class="table-wrapper">

      <table>

        <!-- =========================
             TABLE HEADER
        ========================== -->
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


        <!-- =========================
             TABLE BODY
        ========================== -->
        <tbody>

          <tr
            v-for="user in users"
            :key="user.id"
          >

            <!-- ID -->
            <td>
              {{ user.id }}
            </td>


            <!-- NAME -->
            <td>

              <div class="user-name">

                <div
                  class="avatar"
                  :class="getAvatarClass(user.role)"
                >
                  {{ getInitial(user.name) }}
                </div>

                <span>
                  {{ user.name }}
                </span>

              </div>

            </td>


            <!-- EMAIL -->
            <td>
              {{ user.email }}
            </td>


            <!-- PHONE -->
            <td>
              {{ user.phone || "-" }}
            </td>


            <!-- ROLE -->
            <td>

              <span
                class="role-badge"
                :class="getRoleClass(user.role)"
              >
                {{ user.role }}
              </span>

            </td>


            <!-- CREATED DATE -->
            <td>
              {{ user.createdDate }}
            </td>


            <!-- ACTION -->
            <td>

              <div class="action-buttons">

                <!-- VIEW -->
                <button
                  type="button"
                  class="action-btn view"
                  title="View"
                  @click="$emit('view', user)"
                >
                  <i class="bi bi-eye"></i>
                </button>


                <!-- DELETE -->
                <button
                  type="button"
                  class="action-btn delete"
                  title="Delete"
                  @click="$emit('delete', user)"
                >
                  <i class="bi bi-trash"></i>
                </button>

              </div>

            </td>

          </tr>


          <!-- =========================
               EMPTY
          ========================== -->
          <tr v-if="users.length === 0">

            <td
              colspan="7"
              class="empty"
            >
              <div class="empty-content">

                <i class="bi bi-people"></i>

                <p>
                  No users found.
                </p>

              </div>
            </td>

          </tr>

        </tbody>

      </table>

    </div>

  </div>
</template>


<script setup>

defineProps({

  users: {
    type: Array,
    default: () => []
  }

});


defineEmits([
  "view",
  "delete"
]);


/* =========================
   GET INITIAL
========================= */

function getInitial(name) {

  if (!name) {
    return "?";
  }

  return name
    .charAt(0)
    .toUpperCase();

}


/* =========================
   ROLE CLASS
========================= */

function getRoleClass(role) {

  return String(role || "")
    .toLowerCase();

}


/* =========================
   AVATAR CLASS
========================= */

function getAvatarClass(role) {

  if (
    String(role || "")
      .toUpperCase() === "ADMIN"
  ) {

    return "admin-avatar";

  }

  return "user-avatar";

}

</script>


<style scoped>

/* =========================
   TABLE CARD
========================= */

.table-card {

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 12px;

  overflow: hidden;

}


/* =========================
   TABLE WRAPPER
========================= */

.table-wrapper {

  width: 100%;

  overflow-x: auto;

}


/* =========================
   TABLE
========================= */

table {

  width: 100%;

  min-width: 900px;

  border-collapse: collapse;

}


/* =========================
   THEAD
========================= */

thead {

  background: #f8fafc;

}


th {

  text-align: left;

  padding: 15px 18px;

  font-size: 13px;

  font-weight: 600;

  color: #64748b;

  border-bottom: 1px solid #e5e7eb;

  white-space: nowrap;

}


/* =========================
   TD
========================= */

td {

  padding: 15px 18px;

  font-size: 14px;

  color: #475569;

  border-bottom: 1px solid #f1f5f9;

  white-space: nowrap;

}


tbody tr {

  transition: 0.2s;

}


tbody tr:hover {

  background: #f8fafc;

}


tbody tr:last-child td {

  border-bottom: none;

}


/* =========================
   USER NAME
========================= */

.user-name {

  display: flex;

  align-items: center;

  gap: 10px;

  color: #1e293b;

  font-weight: 600;

}


/* =========================
   AVATAR
========================= */

.avatar {

  width: 36px;

  height: 36px;

  border-radius: 50%;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 13px;

  font-weight: 700;

}


/* USER */

.user-avatar {

  background: #e0e7ff;

  color: #4f46e5;

}


/* ADMIN */

.admin-avatar {

  background: #ede9fe;

  color: #7c3aed;

}


/* =========================
   ROLE
========================= */

.role-badge {

  display: inline-flex;

  align-items: center;

  padding: 5px 11px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


/* ADMIN */

.role-badge.admin {

  background: #ede9fe;

  color: #7c3aed;

}


/* USER */

.role-badge.user {

  background: #e0f2fe;

  color: #0369a1;

}


/* =========================
   ACTIONS
========================= */

.action-buttons {

  display: flex;

  align-items: center;

  gap: 7px;

}


.action-btn {

  width: 34px;

  height: 34px;

  border: 1px solid #e5e7eb;

  border-radius: 7px;

  background: white;

  display: flex;

  align-items: center;

  justify-content: center;

  cursor: pointer;

  transition: 0.2s;

}


.action-btn i {

  font-size: 14px;

}


/* VIEW */

.action-btn.view {

  color: #2563eb;

}


.action-btn.view:hover {

  background: #eff6ff;

  border-color: #bfdbfe;

}


/* DELETE */

.action-btn.delete {

  color: #dc2626;

}


.action-btn.delete:hover {

  background: #fef2f2;

  border-color: #fecaca;

}


/* =========================
   EMPTY
========================= */

.empty {

  padding: 50px 20px !important;

  text-align: center;

}


.empty-content {

  display: flex;

  flex-direction: column;

  align-items: center;

  gap: 8px;

  color: #94a3b8;

}


.empty-content i {

  font-size: 32px;

}


.empty-content p {

  margin: 0;

  font-size: 14px;

}

</style>