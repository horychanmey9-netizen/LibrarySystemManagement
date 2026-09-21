<template>

  <header class="admin-navbar">

    <!-- =========================
         Left Side
    ========================== -->

    <div class="navbar-left">

      <!-- Mobile Menu -->
      <button
        type="button"
        class="mobile-menu-btn"
        title="Open Menu"
        @click="openSidebar"
      >
        <i class="bi bi-list"></i>
      </button>


      <!-- Welcome -->
      <div class="welcome-text">

        <h3>
          Welcome, {{ adminName }}
        </h3>

        <p>
          Have a great day!
        </p>

      </div>

    </div>


    <!-- =========================
         Right Side
    ========================== -->

    <div class="navbar-right">

      <!-- =================================
           ADMIN PROFILE DROPDOWN
      ================================== -->

      <div
        ref="adminMenuRef"
        class="admin-menu"
      >

        <!-- Admin Profile Button -->

        <button
          type="button"
          class="admin-profile"
          title="Admin Menu"
          :class="{
            'admin-profile-open': isMenuOpen
          }"
          @click="toggleMenu"
          aria-haspopup="true"
          :aria-expanded="isMenuOpen"
        >

          <!-- PROFILE IMAGE -->

          <div class="profile-avatar">

            <img
              v-if="adminAvatar"
              :src="adminAvatar"
              :alt="adminName"
              class="avatar-image"
              @error="handleImageError"
            />

            <span v-else>
              {{ getInitial(adminName) }}
            </span>

          </div>


          <!-- Profile Info -->

          <div class="profile-info">

            <h4>
              {{ adminName }}
            </h4>

            <span>
              Administrator
            </span>

          </div>


          <!-- Dropdown Arrow -->

          <i
            class="bi bi-chevron-down profile-arrow"
            :class="{
              rotated: isMenuOpen
            }"
          ></i>

        </button>


        <!-- =================================
             DROPDOWN
        ================================== -->

        <Transition name="admin-dropdown">

          <div
            v-if="isMenuOpen"
            class="admin-dropdown"
          >

            <!-- =================================
                 USER INFORMATION
            ================================== -->

            <div class="dropdown-user-info">

              <div class="dropdown-avatar">

                <img
                  v-if="adminAvatar"
                  :src="adminAvatar"
                  :alt="adminName"
                  class="dropdown-avatar-image"
                  @error="handleImageError"
                />

                <span v-else>
                  {{ getInitial(adminName) }}
                </span>

              </div>


              <div class="dropdown-user-details">

                <div class="dropdown-user-name">
                  {{ adminName }}
                </div>

                <div
                  v-if="adminEmail"
                  class="dropdown-user-email"
                >
                  {{ adminEmail }}
                </div>

                <div class="dropdown-user-role">
                  Administrator
                </div>

              </div>

            </div>


            <!-- Divider -->

            <div class="dropdown-divider"></div>


            <!-- =================================
                 PROFILE
            ================================== -->

            <button
              type="button"
              class="dropdown-item"
              @click="goToProfile"
            >

              <i class="bi bi-person"></i>

              <span>
                Profile
              </span>

            </button>


            <!-- =================================
                 CHANGE PASSWORD
            ================================== -->

            <button
              type="button"
              class="dropdown-item"
              @click="goToChangePassword"
            >

              <i class="bi bi-lock"></i>

              <span>
                Change Password
              </span>

            </button>


            <!-- Divider -->

            <div class="dropdown-divider"></div>


            

          </div>

        </Transition>

      </div>

    </div>

  </header>

</template>


<script setup>

import {
  computed,
  onMounted,
  onBeforeUnmount,
  ref
} from "vue";


import {
  useRouter
} from "vue-router";


import {
  getProfile
} from "../../service/profileservice";


// ========================================
// ROUTER
// ========================================

const router = useRouter();


// ========================================
// ADMIN PROFILE
// ========================================

const admin = ref({

  name: "",

  email: "",

  avatar: "",

});


// ========================================
// ADMIN DROPDOWN
// ========================================

const isMenuOpen = ref(false);

const adminMenuRef = ref(null);


// ========================================
// GET PROFILE
// ========================================

const loadAdminProfile = async () => {

  try {

    // ====================================
    // FIRST: GET FROM SESSION
    // ====================================

    const storedUser =
      sessionStorage.getItem("user");


    if (storedUser) {

      try {

        const user =
          JSON.parse(storedUser);


        admin.value.name =
          user?.name ||
          user?.fullName ||
          "Admin";


        admin.value.email =
          user?.email ||
          "";


        admin.value.avatar =
          user?.avatar ||
          user?.image ||
          "";

      } catch (error) {

        console.error(
          "Invalid session user:",
          error
        );

      }

    }


    // ====================================
    // SECOND: FETCH FROM BACKEND
    // ====================================

    const response =
      await getProfile();


    console.log(
      "ADMIN NAVBAR PROFILE:",
      response
    );


    const data =
      response?.data;


    if (data) {

      admin.value = {

        name:
          data.fullName ||
          data.name ||
          admin.value.name ||
          "Admin",

        email:
          data.email ||
          admin.value.email ||
          "",

        avatar:
          data.image ||
          data.avatar ||
          admin.value.avatar ||
          "",

      };


      // ==================================
      // UPDATE SESSION
      // ==================================

      const currentUser =
        JSON.parse(
          sessionStorage.getItem(
            "user"
          ) || "{}"
        );


      const updatedUser = {

        ...currentUser,

        name:
          admin.value.name,

        email:
          admin.value.email,

        avatar:
          admin.value.avatar,

      };


      sessionStorage.setItem(
        "user",
        JSON.stringify(
          updatedUser
        )
      );

    }

  } catch (error) {

    console.error(
      "Failed to load admin profile:",
      error
    );

  }

};


// ========================================
// COMPUTED NAME
// ========================================

const adminName = computed(() => {

  return (
    admin.value.name ||
    "Admin"
  );

});


// ========================================
// COMPUTED EMAIL
// ========================================

const adminEmail = computed(() => {

  return (
    admin.value.email ||
    ""
  );

});


// ========================================
// COMPUTED AVATAR
// ========================================

const adminAvatar = computed(() => {

  return (
    admin.value.avatar ||
    ""
  );

});


// ========================================
// GET INITIAL
// ========================================

function getInitial(name) {

  if (!name) {

    return "A";

  }


  const names =
    name
      .trim()
      .split(/\s+/);


  // One name

  if (
    names.length === 1
  ) {

    return names[0]
      .substring(0, 2)
      .toUpperCase();

  }


  // First + Last

  return (
    names[0].charAt(0) +
    names[
      names.length - 1
    ].charAt(0)
  ).toUpperCase();

}


// ========================================
// TOGGLE MENU
// ========================================

function toggleMenu() {

  isMenuOpen.value =
    !isMenuOpen.value;

}


// ========================================
// CLOSE MENU
// ========================================

function closeMenu() {

  isMenuOpen.value = false;

}


// ========================================
// CLICK OUTSIDE
// ========================================

function handleClickOutside(event) {

  if (
    adminMenuRef.value &&
    !adminMenuRef.value.contains(
      event.target
    )
  ) {

    closeMenu();

  }

}


// ========================================
// PROFILE
// ========================================

function goToProfile() {

  closeMenu();

  router.push(
    "/admin/profile"
  );

}


// ========================================
// CHANGE PASSWORD
// ========================================

function goToChangePassword() {

  closeMenu();

  router.push(
    "/admin/change-password"
  );

}


// ========================================
// LOGOUT
// ========================================

function logout() {

  closeMenu();


  // Remove logged-in user

  sessionStorage.removeItem(
    "user"
  );


  /*
   * If your JWT/token is stored in
   * sessionStorage, remove it here.
   *
   * Example:
   *
   * sessionStorage.removeItem("token");
   *
   */


  /*
   * If your JWT/token is stored in
   * localStorage, remove it here.
   *
   * Example:
   *
   * localStorage.removeItem("token");
   *
   */


  router.push(
    "/login"
  );

}


// ========================================
// IMAGE ERROR
// ========================================

function handleImageError(event) {

  console.error(
    "Admin avatar failed to load:",
    event.target.src
  );


  admin.value.avatar = "";

}


// ========================================
// PROFILE UPDATED EVENT
// ========================================

const handleProfileUpdated = () => {

  console.log(
    "Profile updated → reload Admin Navbar"
  );


  loadAdminProfile();

};


// ========================================
// OPEN SIDEBAR
// ========================================

function openSidebar() {

  window.dispatchEvent(
    new CustomEvent(
      "toggle-admin-sidebar"
    )
  );

}


// ========================================
// MOUNT
// ========================================

onMounted(() => {

  loadAdminProfile();


  window.addEventListener(
    "profile-updated",
    handleProfileUpdated
  );


  document.addEventListener(
    "click",
    handleClickOutside
  );

});


// ========================================
// UNMOUNT
// ========================================

onBeforeUnmount(() => {

  window.removeEventListener(
    "profile-updated",
    handleProfileUpdated
  );


  document.removeEventListener(
    "click",
    handleClickOutside
  );

});

</script>


<style scoped>

/* ========================================
   NAVBAR
======================================== */

.admin-navbar {

  position: fixed;

  top: 0;

  left: 250px;

  right: 0;

  height: 75px;

  background: #ffffff;

  border-bottom:
    1px solid #e5e7eb;

  display: flex;

  align-items: center;

  justify-content: space-between;

  padding: 0 28px;

  z-index: 1000;

  box-sizing: border-box;

}


/* ========================================
   LEFT SIDE
======================================== */

.navbar-left {

  display: flex;

  align-items: center;

  gap: 15px;

  min-width: 0;

}


/* ========================================
   MOBILE MENU
======================================== */

.mobile-menu-btn {

  display: none;

  width: 40px;

  height: 40px;

  border: none;

  border-radius: 8px;

  background: #f5f3ff;

  color: #5b3df5;

  font-size: 22px;

  align-items: center;

  justify-content: center;

  cursor: pointer;

  flex-shrink: 0;

  transition: 0.2s;

}


.mobile-menu-btn:hover {

  background: #eeeaff;

}


/* ========================================
   WELCOME
======================================== */

.welcome-text {

  display: flex;

  flex-direction: column;

  min-width: 0;

}


.welcome-text h3 {

  margin: 0;

  color: #111827;

  font-size: 18px;

  font-weight: 600;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.welcome-text p {

  margin: 3px 0 0;

  color: #6b7280;

  font-size: 12px;

}


/* ========================================
   RIGHT SIDE
======================================== */

.navbar-right {

  display: flex;

  align-items: center;

  gap: 18px;

  flex-shrink: 0;

}


/* ========================================
   ADMIN MENU
======================================== */

.admin-menu {

  position: relative;

  display: flex;

  align-items: center;

}


/* ========================================
   ADMIN PROFILE
======================================== */

.admin-profile {

  display: flex;

  align-items: center;

  gap: 10px;

  padding: 6px 10px;

  border: none;

  border-radius: 8px;

  background: transparent;

  cursor: pointer;

  transition:
    background 0.2s ease;

  text-align: left;

  flex-shrink: 0;

}


.admin-profile:hover {

  background: #f3f4f6;

}


.admin-profile-open {

  background: #f3f4f6;

}


/* ========================================
   AVATAR
======================================== */

.profile-avatar {

  width: 38px;

  height: 38px;

  border-radius: 50%;

  background: #5b3df5;

  color: #ffffff;

  font-weight: 600;

  display: flex;

  align-items: center;

  justify-content: center;

  flex-shrink: 0;

  overflow: hidden;

}


.avatar-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


/* ========================================
   PROFILE INFO
======================================== */

.profile-info {

  display: flex;

  flex-direction: column;

  min-width: 100px;

}


.profile-info h4 {

  margin: 0;

  font-size: 14px;

  color: #111827;

  font-weight: 600;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.profile-info span {

  font-size: 12px;

  color: #6b7280;

  margin-top: 2px;

}


/* ========================================
   PROFILE ARROW
======================================== */

.profile-arrow {

  font-size: 13px;

  color: #6b7280;

  margin-left: 3px;

  transition:
    transform 0.2s ease;

}


.profile-arrow.rotated {

  transform: rotate(180deg);

}


/* ========================================
   ADMIN DROPDOWN
======================================== */

.admin-dropdown {

  position: absolute;

  top: calc(100% + 10px);

  right: 0;

  width: 280px;

  padding: 8px;

  background: #ffffff;

  border:
    1px solid #e5e7eb;

  border-radius: 12px;

  box-shadow:
    0 12px 30px rgba(15, 23, 42, 0.12),
    0 4px 10px rgba(15, 23, 42, 0.05);

  z-index: 1100;

}


/* ========================================
   DROPDOWN USER INFO
======================================== */

.dropdown-user-info {

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 10px;

}


/* ========================================
   DROPDOWN AVATAR
======================================== */

.dropdown-avatar {

  width: 44px;

  height: 44px;

  min-width: 44px;

  border-radius: 50%;

  background: #eeeaff;

  color: #5b3df5;

  font-size: 14px;

  font-weight: 700;

  display: flex;

  align-items: center;

  justify-content: center;

  overflow: hidden;

  border:
    1px solid #e6e1ff;

}


.dropdown-avatar-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


/* ========================================
   DROPDOWN USER DETAILS
======================================== */

.dropdown-user-details {

  min-width: 0;

  flex: 1;

}


.dropdown-user-name {

  color: #111827;

  font-size: 14px;

  font-weight: 700;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.dropdown-user-email {

  margin-top: 3px;

  color: #6b7280;

  font-size: 12px;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.dropdown-user-role {

  margin-top: 4px;

  color: #5b3df5;

  font-size: 11px;

  font-weight: 600;

}


/* ========================================
   DIVIDER
======================================== */

.dropdown-divider {

  height: 1px;

  background: #e5e7eb;

  margin: 6px 4px;

}


/* ========================================
   DROPDOWN ITEM
======================================== */

.dropdown-item {

  width: 100%;

  height: 42px;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 0 12px;

  border: none;

  border-radius: 8px;

  background: transparent;

  color: #344054;

  cursor: pointer;

  font-size: 14px;

  font-weight: 500;

  text-align: left;

  transition:
    background 0.15s ease,
    color 0.15s ease;

}


.dropdown-item i {

  width: 20px;

  font-size: 17px;

  color: #667085;

  transition:
    color 0.15s ease;

}


.dropdown-item:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


.dropdown-item:hover i {

  color: #5b3df5;

}


/* ========================================
   LOGOUT
======================================== */

.logout-item {

  color: #dc2626;

}


.logout-item i {

  color: #dc2626;

}


.logout-item:hover {

  background: #fff1f2;

  color: #dc2626;

}


.logout-item:hover i {

  color: #dc2626;

}


/* ========================================
   DROPDOWN ANIMATION
======================================== */

.admin-dropdown-enter-active,
.admin-dropdown-leave-active {

  transition:
    opacity 0.15s ease,
    transform 0.15s ease;

}


.admin-dropdown-enter-from,
.admin-dropdown-leave-to {

  opacity: 0;

  transform:
    translateY(-6px);

}


/* ========================================
   TABLET
======================================== */

@media (max-width: 1024px) {

  .admin-navbar {

    left: 230px;

    padding: 0 20px;

  }

}


/* ========================================
   MOBILE
======================================== */

@media (max-width: 768px) {

  .admin-navbar {

    left: 0;

    height: 65px;

    padding: 0 15px;

  }


  .mobile-menu-btn {

    display: flex;

  }


  .welcome-text h3 {

    font-size: 16px;

  }


  .welcome-text p {

    display: none;

  }


  .navbar-right {

    gap: 6px;

  }


  .profile-info {

    display: none;

  }


  .profile-arrow {

    display: none;

  }


  .admin-profile {

    padding: 5px;

  }


  .admin-dropdown {

    right: 0;

    width: 270px;

  }

}


/* ========================================
   SMALL MOBILE
======================================== */

@media (max-width: 480px) {

  .admin-navbar {

    padding: 0 10px;

  }


  .navbar-left {

    gap: 8px;

    min-width: 0;

  }


  .welcome-text {

    max-width: 140px;

  }


  .welcome-text h3 {

    font-size: 14px;

  }


  .mobile-menu-btn {

    width: 38px;

    height: 38px;

    font-size: 20px;

  }


  .profile-avatar {

    width: 35px;

    height: 35px;

    font-size: 13px;

  }


  .admin-dropdown {

    width: 260px;

  }

}


/* ========================================
   VERY SMALL MOBILE
======================================== */

@media (max-width: 360px) {

  .welcome-text {

    max-width: 100px;

  }


  .welcome-text h3 {

    font-size: 13px;

  }


  .navbar-right {

    gap: 2px;

  }

}

</style>