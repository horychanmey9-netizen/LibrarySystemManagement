<template>

  <div class="users-page">

    <!-- =========================
         HEADER
    ========================== -->

    <div class="page-header">

      <div>

        <h1>
          Users
        </h1>

        <p>
          Manage all users in the library system
        </p>

      </div>

    </div>


    <!-- =========================
         SEARCH
    ========================== -->

    <div class="toolbar">

      <div class="search-box">

        <i
          class="bi bi-search search-icon"
        ></i>

        <input
          v-model="search"
          type="text"
          placeholder="Search users..."
        />

      </div>

    </div>


    <!-- =========================
         USER TABLE
    ========================== -->

    <UserTable
      :users="filteredUsers"
      @view="viewUser"
      @delete="deleteUser"
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
  computed
} from "vue";


import UserTable
  from "../../components/admin/UserTable.vue";


import UserDetailModal
  from "../../components/admin/UserDetailModal.vue";



/* =================================================
   USERS DATA
================================================= */

const users = ref([

  {
    id: 1,

    name: "Sreynob",

    email: "admin@gmail.com",

    phone: "012 345 678",

    role: "ADMIN",

    createdDate: "2026-08-01"
  },


  {
    id: 2,

    name: "Dara",

    email: "dara@gmail.com",

    phone: "010 234 567",

    role: "USER",

    createdDate: "2026-08-03"
  },


  {
    id: 3,

    name: "Lina",

    email: "lina@gmail.com",

    phone: "097 456 789",

    role: "USER",

    createdDate: "2026-08-05"
  },


  {
    id: 4,

    name: "Sokha",

    email: "sokha@gmail.com",

    phone: "096 123 456",

    role: "USER",

    createdDate: "2026-08-06"
  },


  {
    id: 5,

    name: "Vanna",

    email: "vanna@gmail.com",

    phone: "098 555 222",

    role: "USER",

    createdDate: "2026-08-08"
  },


  {
    id: 6,

    name: "Chantha",

    email: "chantha@gmail.com",

    phone: "097 888 999",

    role: "USER",

    createdDate: "2026-08-10"
  }

]);



/* =================================================
   SEARCH
================================================= */

const search = ref("");



/* =================================================
   FILTER USERS
================================================= */

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

      String(user.id)
        .includes(keyword)

      ||

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

      ||

      user.createdDate
        .toLowerCase()
        .includes(keyword)

    );

  });

});



/* =================================================
   SELECTED USER
================================================= */

const selectedUser = ref(null);



/* =================================================
   VIEW USER
================================================= */

function viewUser(user) {

  selectedUser.value = user;

}



/* =================================================
   CLOSE MODAL
================================================= */

function closeModal() {

  selectedUser.value = null;

}



/* =================================================
   DELETE USER
================================================= */

function deleteUser(user) {

  const confirmed =
    window.confirm(
      `Are you sure you want to delete ${user.name}?`
    );


  if (!confirmed) {

    return;

  }


  users.value =
    users.value.filter(
      item =>
        item.id !== user.id
    );


  /*

    If the deleted user
    is currently open
    in modal, close it.

  */

  if (
    selectedUser.value?.id === user.id
  ) {

    selectedUser.value = null;

  }

}

</script>


<style scoped>

/* =================================================
   PAGE
================================================= */

.users-page {

  min-height: calc(100vh - 70px);

  padding: 30px;

  background: #f8fafc;

  box-sizing: border-box;

}


/* =================================================
   HEADER
================================================= */

.page-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  margin-bottom: 24px;

}


.page-header h1 {

  margin: 0;

  color: #1e293b;

  font-size: 26px;

  font-weight: 700;

}


.page-header p {

  margin: 6px 0 0;

  color: #64748b;

  font-size: 14px;

}


/* =================================================
   TOOLBAR
================================================= */

.toolbar {

  display: flex;

  align-items: center;

  margin-bottom: 18px;

}


/* =================================================
   SEARCH
================================================= */

.search-box {

  width: 320px;

  height: 42px;

  display: flex;

  align-items: center;

  padding: 0 12px;

  background: white;

  border: 1px solid #d1d5db;

  border-radius: 8px;

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


/* =================================================
   RESPONSIVE
================================================= */

@media (max-width: 768px) {

  .users-page {

    padding: 20px;

  }


  .page-header {

    flex-direction: column;

    align-items: flex-start;

    gap: 15px;

  }


  .search-box {

    width: 100%;

  }

}

</style>