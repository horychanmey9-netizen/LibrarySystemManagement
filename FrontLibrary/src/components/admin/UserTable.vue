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

            <th>Status</th>

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

            <td class="user-id">

              #{{ user.id }}

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

                <div class="name-info">

                  <span class="name">
                    {{ user.name }}
                  </span>

                </div>

              </div>

            </td>


            <!-- EMAIL -->

            <td>

              <span class="email">
                {{ user.email }}
              </span>

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

                <i
                  :class="
                    String(user.role).toUpperCase() === 'ADMIN'
                      ? 'bi bi-shield-fill'
                      : 'bi bi-person-fill'
                  "
                ></i>

                {{ user.role }}

              </span>

            </td>


            <!-- STATUS -->

            <td>

              <span
                class="status-badge"
                :class="
                  user.active
                    ? 'active'
                    : 'inactive'
                "
              >

                <span class="status-dot"></span>

                {{ user.active ? "Active" : "Inactive" }}

              </span>

            </td>


            <!-- CREATED DATE -->

            <td class="created-date">

              {{ formatDate(user.createdAt) }}

            </td>


            <!-- ACTION -->

            <td>

              <div class="action-buttons">

                <!-- VIEW -->

                <button
                  type="button"
                  class="action-btn view"
                  @click="$emit('view', user)"
                >

                  <i class="bi bi-eye"></i>

                  <span>
                    View
                  </span>

                </button>


                <!-- DEACTIVATE -->

                <button
                  v-if="user.active"
                  type="button"
                  class="action-btn deactivate"
                  @click="$emit('delete', user)"
                >

                  <i class="bi bi-person-x"></i>

                  <span>
                    Deactivate
                  </span>

                </button>


                <!-- ACTIVATE -->

                <button
                  v-else
                  type="button"
                  class="action-btn activate"
                  @click="$emit('activate', user)"
                >

                  <i class="bi bi-person-check"></i>

                  <span>
                    Activate
                  </span>

                </button>

              </div>

            </td>

          </tr>


          <!-- =========================
               EMPTY
          ========================== -->

          <tr v-if="users.length === 0">

            <td
              colspan="8"
              class="empty"
            >

              <div class="empty-content">

                <div class="empty-icon">

                  <i class="bi bi-people"></i>

                </div>

                <strong>
                  No users found
                </strong>

                <p>
                  Try changing your search.
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
  "delete",
  "activate"
]);



/* =========================
   INITIAL
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



/* =========================
   FORMAT DATE
========================= */

function formatDate(date) {

  if (!date) {

    return "-";

  }

  return String(date)
    .split("T")[0];

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

  box-shadow:
    0 2px 8px
    rgba(15, 23, 42, 0.03);

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

  min-width: 1100px;

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

  padding: 14px 18px;

  font-size: 12px;

  font-weight: 700;

  color: #64748b;

  border-bottom:
    1px solid #e5e7eb;

  white-space: nowrap;

  text-transform: uppercase;

  letter-spacing: 0.02em;

}


/* =========================
   TD
========================= */

td {

  padding: 14px 18px;

  font-size: 14px;

  color: #475569;

  border-bottom:
    1px solid #f1f5f9;

  white-space: nowrap;

}


tbody tr {

  transition: 0.2s;

}


tbody tr:hover {

  background: #fafbff;

}


tbody tr:last-child td {

  border-bottom: none;

}


/* =========================
   USER ID
========================= */

.user-id {

  color: #64748b;

  font-size: 13px;

  font-weight: 600;

}


/* =========================
   USER NAME
========================= */

.user-name {

  display: flex;

  align-items: center;

  gap: 10px;

}


.name {

  color: #1e293b;

  font-weight: 600;

}


/* =========================
   AVATAR
========================= */

.avatar {

  width: 36px;

  height: 36px;

  flex-shrink: 0;

  border-radius: 50%;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 13px;

  font-weight: 700;

}


.user-avatar {

  background: #e0e7ff;

  color: #4f46e5;

}


.admin-avatar {

  background: #ede9fe;

  color: #7c3aed;

}


/* =========================
   EMAIL
========================= */

.email {

  color: #475569;

}


/* =========================
   ROLE
========================= */

.role-badge {

  display: inline-flex;

  align-items: center;

  gap: 5px;

  padding: 5px 10px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


.role-badge.admin {

  background: #ede9fe;

  color: #7c3aed;

}


.role-badge.user {

  background: #e0f2fe;

  color: #0369a1;

}


/* =========================
   STATUS
========================= */

.status-badge {

  display: inline-flex;

  align-items: center;

  gap: 7px;

  padding: 5px 10px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


.status-badge.active {

  background: #ecfdf5;

  color: #15803d;

}


.status-badge.inactive {

  background: #fef2f2;

  color: #dc2626;

}


.status-dot {

  width: 6px;

  height: 6px;

  border-radius: 50%;

  background: currentColor;

}


/* =========================
   CREATED DATE
========================= */

.created-date {

  color: #334155;

  font-weight: 600;

}


/* =========================
   ACTIONS
========================= */

.action-buttons {

  display: flex;

  align-items: center;

  gap: 7px;

}


/* =========================
   ACTION BUTTON
========================= */

.action-btn {

  height: 32px;

  display: inline-flex;

  align-items: center;

  gap: 6px;

  border: 1px solid #e5e7eb;

  border-radius: 7px;

  background: white;

  padding: 0 10px;

  font-size: 12px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s;

  white-space: nowrap;

}


.action-btn i {

  font-size: 13px;

}


/* =========================
   VIEW
========================= */

.action-btn.view {

  color: #2563eb;

}


.action-btn.view:hover {

  background: #eff6ff;

  border-color: #bfdbfe;

}


/* =========================
   DEACTIVATE
========================= */

.action-btn.deactivate {

  color: #dc2626;

}


.action-btn.deactivate:hover {

  background: #fef2f2;

  border-color: #fecaca;

}


/* =========================
   ACTIVATE
========================= */

.action-btn.activate {

  color: #16a34a;

}


.action-btn.activate:hover {

  background: #f0fdf4;

  border-color: #bbf7d0;

}


/* =========================
   EMPTY
========================= */

.empty {

  padding: 65px 20px !important;

  text-align: center;

}


.empty-content {

  display: flex;

  flex-direction: column;

  align-items: center;

  gap: 7px;

  color: #94a3b8;

}


.empty-icon {

  width: 52px;

  height: 52px;

  margin-bottom: 5px;

  border-radius: 50%;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #f1f5f9;

  color: #94a3b8;

  font-size: 21px;

}


.empty-content strong {

  color: #475569;

  font-size: 14px;

}


.empty-content p {

  margin: 0;

  color: #94a3b8;

  font-size: 13px;

}


/* =========================
   MOBILE
========================= */

@media (max-width: 768px) {

  .table-card {

    border-radius: 10px;

  }

}

</style>