<template>
  <div class="roles-page">

    <!-- =========================
         Header
    ========================== -->
    <div class="page-header">
      <div>
        <h1>Roles Management</h1>
        <p>Manage user roles and permissions</p>
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
          placeholder="Search roles..."
        />

      </div>

    </div>


    <!-- =========================
         Roles Table
    ========================== -->
    <div class="table-card">

      <div class="table-header">

        <div>
          <h2>Roles</h2>

          <p>
            {{ filteredRoles.length }} roles found
          </p>
        </div>

      </div>


      <div class="table-wrapper">

        <table>

          <thead>
            <tr>
              <th>ID</th>
              <th>ROLE NAME</th>
              <th>DESCRIPTION</th>
              <th>PERMISSIONS</th>
              <th>ACTION</th>
            </tr>
          </thead>


          <tbody>

            <tr
              v-for="role in filteredRoles"
              :key="role.id"
            >

              <!-- =========================
                   ID
              ========================== -->
              <td>
                <span class="role-id">
                  #{{ role.id }}
                </span>
              </td>


              <!-- =========================
                   Role Name
              ========================== -->
              <td>

                <span
                  class="role-badge"
                  :class="getRoleClass(role.name)"
                >
                  {{ role.name }}
                </span>

              </td>


              <!-- =========================
                   Description
              ========================== -->
              <td>

                <span class="description">
                  {{ role.description }}
                </span>

              </td>


              <!-- =========================
                   Permissions
              ========================== -->
              <td>

                <div class="permission-list">

                  <span
                    v-for="permission in role.permissions"
                    :key="permission"
                    class="permission-tag"
                  >
                    {{ permission }}
                  </span>

                </div>

              </td>


              <!-- =========================
                   Actions
              ========================== -->
              <td>

                <div class="actions">

                  <!-- Edit -->
                  <button
                    class="action-btn edit"
                    title="Edit"
                    @click="openEditModal(role)"
                  >
                    <i class="bi bi-pencil"></i>
                  </button>


                  <!-- Delete -->
                  <button
                    class="action-btn delete"
                    title="Delete"
                    @click="deleteRole(role)"
                  >
                    <i class="bi bi-trash3"></i>
                  </button>

                </div>

              </td>

            </tr>


            <!-- =========================
                 Empty State
            ========================== -->
            <tr v-if="filteredRoles.length === 0">

              <td
                colspan="5"
                class="empty-state"
              >
                No roles found.
              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>


    <!-- =========================
         Edit Modal
    ========================== -->
    <div
      v-if="showModal"
      class="modal-overlay"
      @click.self="closeModal"
    >

      <div class="modal">

        <!-- =========================
             Modal Header
        ========================== -->
        <div class="modal-header">

          <div>

            <h2>Edit Role</h2>

            <p>
              Update role information and permissions
            </p>

          </div>


          <button
            class="close-btn"
            title="Close"
            @click="closeModal"
          >
            <i class="bi bi-x-lg"></i>
          </button>

        </div>


        <!-- =========================
             Modal Body
        ========================== -->
        <div class="modal-body">

          <!-- Role Name -->
          <div class="form-group">

            <label>
              Role Name
              <span>*</span>
            </label>

            <input
              v-model="form.name"
              type="text"
              placeholder="Example: ADMIN"
              :disabled="form.name === 'ADMIN'"
            />

          </div>


          <!-- Description -->
          <div class="form-group">

            <label>
              Description
              <span>*</span>
            </label>

            <textarea
              v-model="form.description"
              rows="3"
              placeholder="Enter role description..."
            ></textarea>

          </div>


          <!-- Permissions -->
          <div class="form-group">

            <div class="permission-title">

              <label>
                Permissions
              </label>

              <button
                class="select-all-btn"
                type="button"
                @click="toggleAllPermissions"
              >
                {{
                  allPermissionsSelected
                    ? "Unselect All"
                    : "Select All"
                }}
              </button>

            </div>


            <div class="permissions-grid">

              <label
                v-for="permission in availablePermissions"
                :key="permission"
                class="permission-item"
              >

                <input
                  type="checkbox"
                  :value="permission"
                  v-model="form.permissions"
                />

                <span>
                  {{ permission }}
                </span>

              </label>

            </div>

          </div>

        </div>


        <!-- =========================
             Modal Footer
        ========================== -->
        <div class="modal-footer">

          <button
            class="cancel-btn"
            type="button"
            @click="closeModal"
          >
            Cancel
          </button>


          <button
            class="save-btn"
            type="button"
            @click="saveRole"
          >
            Update Role
          </button>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>

import {
  ref,
  computed
} from "vue"


/* =========================
   Search
========================= */

const search = ref("")


/* =========================
   Modal
========================= */

const showModal = ref(false)


/* =========================
   Available Permissions
========================= */

const availablePermissions = [

  "Dashboard",

  "Books",

  "Categories",

  "Borrowings",

  "Returns",

  "Users",

  "Roles",

  "Reports",

  "Fines",

  "Notifications",

  "Browse Books",

  "My Borrowings",

  "My Fines",

  "Profile"

]


/* =========================
   Roles Data
========================= */

const roles = ref([

  {
    id: 1,

    name: "ADMIN",

    description:
      "Full system access",

    permissions: [

      "Dashboard",

      "Books",

      "Categories",

      "Borrowings",

      "Returns",

      "Users",

      "Roles",

      "Reports",

      "Fines",

      "Notifications"

    ]

  },


  {
    id: 2,

    name: "USER",

    description:
      "Borrow and return books",

    permissions: [

      "Dashboard",

      "Browse Books",

      "My Borrowings",

      "My Fines",

      "Profile"

    ]

  }

])


/* =========================
   Form
========================= */

const form = ref({

  id: null,

  name: "",

  description: "",

  permissions: []

})


/* =========================
   Filter Roles
========================= */

const filteredRoles = computed(() => {

  const keyword =
    search.value
      .toLowerCase()
      .trim()


  if (!keyword) {

    return roles.value

  }


  return roles.value.filter(role => {

    return (

      role.name
        .toLowerCase()
        .includes(keyword)

      ||

      role.description
        .toLowerCase()
        .includes(keyword)

      ||

      role.permissions.some(permission =>
        permission
          .toLowerCase()
          .includes(keyword)
      )

    )

  })

})


/* =========================
   Role Class
========================= */

const getRoleClass = (roleName) => {

  switch (roleName) {

    case "ADMIN":

      return "admin"


    case "USER":

      return "user"


    default:

      return ""

  }

}


/* =========================
   Open Edit Modal
========================= */

const openEditModal = (role) => {

  form.value = {

    id: role.id,

    name: role.name,

    description: role.description,

    permissions: [
      ...role.permissions
    ]

  }


  showModal.value = true

}


/* =========================
   Close Modal
========================= */

const closeModal = () => {

  showModal.value = false

}


/* =========================
   All Permissions Selected
========================= */

const allPermissionsSelected = computed(() => {

  return (
    form.value.permissions.length ===
    availablePermissions.length
  )

})


/* =========================
   Toggle All Permissions
========================= */

const toggleAllPermissions = () => {

  if (allPermissionsSelected.value) {

    form.value.permissions = []

  }

  else {

    form.value.permissions = [

      ...availablePermissions

    ]

  }

}


/* =========================
   Save Role
========================= */

const saveRole = () => {

  /* =========================
     Validate Description
  ========================== */

  if (!form.value.description.trim()) {

    alert(
      "Please enter description."
    )

    return

  }


  /* =========================
     Validate Permissions
  ========================== */

  if (
    form.value.permissions.length === 0
  ) {

    alert(
      "Please select at least one permission."
    )

    return

  }


  /* =========================
     Find Role
  ========================== */

  const index =
    roles.value.findIndex(
      role =>
        role.id === form.value.id
    )


  /* =========================
     Update Role
  ========================== */

  if (index !== -1) {

    roles.value[index] = {

      id: form.value.id,

      name:
        form.value.name
          .trim()
          .toUpperCase(),

      description:
        form.value.description
          .trim(),

      permissions: [

        ...form.value.permissions

      ]

    }

  }


  closeModal()

}


/* =========================
   Delete Role
========================= */

const deleteRole = (role) => {

  /* =========================
     Protect ADMIN
  ========================== */

  if (role.name === "ADMIN") {

    alert(
      "ADMIN role cannot be deleted."
    )

    return

  }


  /* =========================
     Confirmation
  ========================== */

  const confirmed = confirm(

    `Are you sure you want to delete ${role.name} role?`

  )


  if (!confirmed) {

    return

  }


  /* =========================
     Delete
  ========================== */

  roles.value =
    roles.value.filter(
      item =>
        item.id !== role.id
    )

}

</script>


<style scoped>

/* =========================
   Page
========================= */

.roles-page {

  min-height: 100vh;

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

  margin-bottom: 25px;

}


.page-header h1 {

  margin: 0;

  font-size: 28px;

  font-weight: 700;

  color: #1e293b;

}


.page-header p {

  margin: 6px 0 0;

  color: #64748b;

  font-size: 14px;

}


/* =========================
   Toolbar
========================= */

.toolbar {

  margin-bottom: 20px;

}


.search-box {

  width: 320px;

  height: 42px;

  background: white;

  border: 1px solid #e2e8f0;

  border-radius: 8px;

  display: flex;

  align-items: center;

  padding: 0 12px;

  box-sizing: border-box;

}


.search-icon {

  color: #64748b;

  font-size: 16px;

  margin-right: 8px;

  flex-shrink: 0;

}


.search-box input {

  width: 100%;

  border: none;

  outline: none;

  font-size: 14px;

  color: #334155;

}


/* =========================
   Table Card
========================= */

.table-card {

  background: white;

  border-radius: 12px;

  border: 1px solid #e2e8f0;

  overflow: hidden;

}


.table-header {

  padding: 20px;

  border-bottom:
    1px solid #e2e8f0;

}


.table-header h2 {

  margin: 0;

  font-size: 18px;

  color: #1e293b;

}


.table-header p {

  margin: 5px 0 0;

  color: #64748b;

  font-size: 13px;

}


/* =========================
   Table
========================= */

.table-wrapper {

  width: 100%;

  overflow-x: auto;

}


table {

  width: 100%;

  min-width: 900px;

  border-collapse: collapse;

}


thead {

  background: #f8fafc;

}


th {

  padding: 14px 18px;

  text-align: left;

  font-size: 12px;

  color: #64748b;

  font-weight: 700;

  white-space: nowrap;

}


td {

  padding: 17px 18px;

  border-top:
    1px solid #f1f5f9;

  vertical-align: middle;

}


tbody tr:hover {

  background: #f8fafc;

}


/* =========================
   Role ID
========================= */

.role-id {

  color: #64748b;

  font-size: 13px;

}


/* =========================
   Role Badge
========================= */

.role-badge {

  display: inline-flex;

  padding: 6px 11px;

  border-radius: 20px;

  font-size: 12px;

  font-weight: 700;

}


.role-badge.admin {

  background: #fee2e2;

  color: #b91c1c;

}


.role-badge.user {

  background: #dbeafe;

  color: #1d4ed8;

}


/* =========================
   Description
========================= */

.description {

  color: #475569;

  font-size: 14px;

}


/* =========================
   Permission Tags
========================= */

.permission-list {

  display: flex;

  flex-wrap: wrap;

  gap: 5px;

  max-width: 420px;

}


.permission-tag {

  background: #f1f5f9;

  color: #475569;

  padding: 4px 8px;

  border-radius: 5px;

  font-size: 11px;

}


/* =========================
   Actions
========================= */

.actions {

  display: flex;

  align-items: center;

  gap: 7px;

}


.action-btn {

  width: 34px;

  height: 34px;

  border: none;

  border-radius: 7px;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 15px;

  transition: 0.2s;

}


.action-btn i {

  font-size: 14px;

}


/* Edit */

.action-btn.edit {

  background: #eff6ff;

  color: #2563eb;

}


.action-btn.edit:hover {

  background: #dbeafe;

}


/* Delete */

.action-btn.delete {

  background: #fef2f2;

  color: #dc2626;

}


.action-btn.delete:hover {

  background: #fee2e2;

}


/* =========================
   Empty State
========================= */

.empty-state {

  text-align: center;

  padding: 40px;

  color: #94a3b8;

}


/* =========================
   Modal Overlay
========================= */

.modal-overlay {

  position: fixed;

  inset: 0;

  background:
    rgba(15, 23, 42, 0.55);

  display: flex;

  align-items: center;

  justify-content: center;

  padding: 20px;

  z-index: 1000;

}


/* =========================
   Modal
========================= */

.modal {

  width: 100%;

  max-width: 650px;

  max-height: 90vh;

  overflow-y: auto;

  background: white;

  border-radius: 14px;

  box-shadow:
    0 20px 50px
    rgba(0, 0, 0, 0.2);

}


/* =========================
   Modal Header
========================= */

.modal-header {

  padding: 20px 24px;

  display: flex;

  justify-content: space-between;

  align-items: flex-start;

  border-bottom:
    1px solid #e2e8f0;

}


.modal-header h2 {

  margin: 0;

  font-size: 20px;

  color: #1e293b;

}


.modal-header p {

  margin: 5px 0 0;

  color: #64748b;

  font-size: 13px;

}


/* Close */

.close-btn {

  width: 34px;

  height: 34px;

  border: none;

  background: transparent;

  border-radius: 6px;

  display: flex;

  align-items: center;

  justify-content: center;

  color: #64748b;

  font-size: 18px;

  cursor: pointer;

  transition: 0.2s;

}


.close-btn:hover {

  background: #f1f5f9;

  color: #1e293b;

}


/* =========================
   Modal Body
========================= */

.modal-body {

  padding: 24px;

}


.form-group {

  margin-bottom: 20px;

}


.form-group > label {

  display: block;

  margin-bottom: 8px;

  color: #334155;

  font-size: 14px;

  font-weight: 600;

}


.form-group label span {

  color: #dc2626;

}


/* Inputs */

.form-group input[type="text"],
.form-group textarea {

  width: 100%;

  box-sizing: border-box;

  border:
    1px solid #cbd5e1;

  border-radius: 8px;

  padding: 11px 12px;

  outline: none;

  font-family: inherit;

  font-size: 14px;

  transition: 0.2s;

}


.form-group input[type="text"]:focus,
.form-group textarea:focus {

  border-color: #2563eb;

  box-shadow:
    0 0 0 3px
    rgba(37, 99, 235, 0.1);

}


.form-group input:disabled {

  background: #f1f5f9;

  cursor: not-allowed;

}


/* =========================
   Permission Title
========================= */

.permission-title {

  display: flex;

  align-items: center;

  justify-content: space-between;

  margin-bottom: 10px;

}


.permission-title label {

  color: #334155;

  font-size: 14px;

  font-weight: 600;

}


.select-all-btn {

  border: none;

  background: transparent;

  color: #2563eb;

  font-size: 12px;

  font-weight: 600;

  cursor: pointer;

}


.select-all-btn:hover {

  color: #1d4ed8;

}


/* =========================
   Permissions Grid
========================= */

.permissions-grid {

  display: grid;

  grid-template-columns:
    repeat(2, 1fr);

  gap: 10px;

  padding: 15px;

  background: #f8fafc;

  border-radius: 8px;

  border:
    1px solid #e2e8f0;

}


.permission-item {

  display: flex;

  align-items: center;

  gap: 9px;

  font-size: 13px;

  color: #475569;

  cursor: pointer;

}


.permission-item input {

  width: 16px;

  height: 16px;

  cursor: pointer;

  accent-color: #2563eb;

}


/* =========================
   Modal Footer
========================= */

.modal-footer {

  display: flex;

  justify-content: flex-end;

  gap: 10px;

  padding: 18px 24px;

  border-top:
    1px solid #e2e8f0;

}


/* =========================
   Footer Buttons
========================= */

.cancel-btn,
.save-btn {

  padding: 10px 18px;

  border-radius: 8px;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s;

}


.cancel-btn {

  border:
    1px solid #cbd5e1;

  background: white;

  color: #475569;

}


.cancel-btn:hover {

  background: #f8fafc;

}


.save-btn {

  border: none;

  background: #2563eb;

  color: white;

}


.save-btn:hover {

  background: #1d4ed8;

}


/* =========================
   Responsive
========================= */

@media (max-width: 768px) {

  .roles-page {

    padding: 20px;

  }


  .page-header {

    align-items: flex-start;

  }


  .search-box {

    width: 100%;

  }


  .permissions-grid {

    grid-template-columns: 1fr;

  }


  .modal {

    max-height: 95vh;

  }


  .modal-body {

    padding: 20px;

  }


  .modal-footer {

    padding: 15px 20px;

  }

}

</style>