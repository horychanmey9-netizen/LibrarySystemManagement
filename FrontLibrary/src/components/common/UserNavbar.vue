```vue
<template>

  <header class="navbar">

    <div class="navbar-inner container">

      <!-- =====================================================
           LEFT
      ====================================================== -->

      <div class="navbar-left">

        <div class="logo-section">

          <div class="logo-icon">

            <img
              src="/logo1.png"
              alt="Library Logo"
              class="logo-image"
            />

          </div>

          <div class="logo-text">

            <h1>
              Library
            </h1>

            <span>
              Management System
            </span>

          </div>

        </div>

      </div>


      <!-- =====================================================
           NAVIGATION MENU
      ====================================================== -->

      <nav class="navbar-menu">

        <router-link
          to="/user/home"
          class="nav-link"
          active-class="active"
        >

          <i class="bi bi-house"></i>

          <span>
            Home
          </span>

        </router-link>


        <router-link
          to="/user/my-borrowings"
          class="nav-link"
          active-class="active"
        >

          <i class="bi bi-book"></i>

          <span>
            My Borrowings
          </span>

        </router-link>


        <router-link
          to="/user/my-fines"
          class="nav-link"
          active-class="active"
        >

          <i class="bi bi-cash-stack"></i>

          <span>
            My Fines
          </span>

        </router-link>

      </nav>


      <!-- =====================================================
           RIGHT
           FAVORITE + PROFILE
      ====================================================== -->

      <div class="navbar-right">


        <!-- =================================================
             FAVORITE BUTTON
        ================================================== -->

        <button
          type="button"
          class="favorite-button"
          @click="goToFavorite"
          title="My Favorite"
        >

          <i class="bi bi-heart"></i>

        </button>


        <!-- =================================================
             USER DROPDOWN
        ================================================== -->

        <div
          ref="userMenuRef"
          class="user-menu"
        >

          <!-- USER BUTTON -->

          <button
            type="button"
            class="user-button"
            :class="{ 'user-button-open': isUserMenuOpen }"
            @click="toggleUserMenu"
            aria-haspopup="true"
            :aria-expanded="isUserMenuOpen"
          >

            <div class="navbar-avatar">

              <img
                v-if="userAvatar"
                :src="userAvatar"
                :alt="userName"
                class="avatar-image"
                @error="handleAvatarError"
              />

              <span v-else>
                {{ userInitials }}
              </span>

            </div>


            <span class="navbar-user-name">
              {{ userName }}
            </span>


            <!-- CHEVRON -->

            <i
              class="bi bi-chevron-down user-chevron"
              :class="{ rotated: isUserMenuOpen }"
            ></i>

          </button>


          <!-- =================================================
               DROPDOWN
          ================================================== -->

          <Transition name="user-dropdown">

            <div
              v-if="isUserMenuOpen"
              class="user-dropdown"
            >

              <!-- =================================================
                   USER INFORMATION
              ================================================== -->

              <div class="dropdown-user-info">

                <div class="dropdown-avatar">

                  <img
                    v-if="userAvatar"
                    :src="userAvatar"
                    :alt="userName"
                    class="dropdown-avatar-image"
                    @error="handleAvatarError"
                  />

                  <span v-else>
                    {{ userInitials }}
                  </span>

                </div>


                <div class="dropdown-user-details">

                  <div class="dropdown-user-name">
                    {{ userName }}
                  </div>

                  <div
                    v-if="userEmail"
                    class="dropdown-user-email"
                  >
                    {{ userEmail }}
                  </div>

                </div>

              </div>


              <!-- DIVIDER -->

              <div class="dropdown-divider"></div>


              <!-- =================================================
                   PROFILE
              ================================================== -->

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


              <!-- =================================================
                   CHANGE PASSWORD
              ================================================== -->

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


              <!-- DIVIDER -->

              <div class="dropdown-divider"></div>


              <!-- =================================================
                   LOGOUT
              ================================================== -->

              <button
                type="button"
                class="dropdown-item logout-item"
                @click="logout"
              >

                <i class="bi bi-box-arrow-right"></i>

                <span>
                  Logout
                </span>

              </button>

            </div>

          </Transition>

        </div>

      </div>

    </div>

  </header>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted,
  onUnmounted
} from "vue";


import {
  useRouter
} from "vue-router";


import {
  getProfile
} from "@/service/profileservice";


const router = useRouter();


/* =====================================================
   EVENTS
===================================================== */

const emit = defineEmits([
  "toggle-sidebar"
]);


/* =====================================================
   PROFILE IMAGE
===================================================== */

const profileImage = ref("");


/* =====================================================
   USER DROPDOWN
===================================================== */

const isUserMenuOpen = ref(false);

const userMenuRef = ref(null);


/* =====================================================
   GET USER FROM SESSION STORAGE
===================================================== */

const storedUser =
  sessionStorage.getItem("user");


let user = null;


try {

  user = storedUser
    ? JSON.parse(storedUser)
    : null;

} catch (error) {

  console.error(
    "Invalid user data:",
    error
  );

  user = null;

}


/* =====================================================
   USER NAME
   This comes from the name used when the user
   registered/logged in.
   Example:
   {
      "name": "Sara",
      "email": "norchanden@gmail.com"
   }
===================================================== */

const userName = computed(() => {

  return (
    user?.name ||
    user?.fullName ||
    "User"
  );

});


/* =====================================================
   USER EMAIL
   This also comes from the authenticated user.
===================================================== */

const userEmail = computed(() => {

  return (
    user?.email ||
    user?.username ||
    ""
  );

});


/* =====================================================
   LOAD PROFILE
===================================================== */

const loadProfile = async () => {

  try {

    const response =
      await getProfile();


    console.log(
      "NAVBAR PROFILE RESPONSE:",
      response
    );


    const data =
      response?.data;


    if (!data) {
      return;
    }


    /* =================================================
       PROFILE IMAGE
    ================================================== */

    if (data.image) {

      if (
        data.image.startsWith("http")
      ) {

        profileImage.value =
          data.image;

      } else {

        profileImage.value =
          `http://localhost:8080${data.image}`;

      }

    }

  } catch (error) {

    console.error(
      "Failed to load profile:",
      error
    );


    profileImage.value =
      user?.avatar ||
      user?.image ||
      "";

  }

};


/* =====================================================
   USER AVATAR
===================================================== */

const userAvatar = computed(() => {

  if (profileImage.value) {

    return profileImage.value;

  }


  return (
    user?.avatar ||
    user?.image ||
    ""
  );

});


/* =====================================================
   USER INITIALS
===================================================== */

const userInitials = computed(() => {

  const name =
    userName.value;


  if (
    !name ||
    name === "User"
  ) {

    return "US";

  }


  const names =
    name
      .trim()
      .split(/\s+/);


  /* =================================================
     ONE NAME
  ================================================== */

  if (
    names.length === 1
  ) {

    return names[0]
      .substring(0, 2)
      .toUpperCase();

  }


  /* =================================================
     FIRST + LAST NAME
  ================================================== */

  return (
    names[0].charAt(0) +
    names[
      names.length - 1
    ].charAt(0)
  ).toUpperCase();

});


/* =====================================================
   TOGGLE USER MENU
===================================================== */

function toggleUserMenu() {

  isUserMenuOpen.value =
    !isUserMenuOpen.value;

}


/* =====================================================
   CLOSE USER MENU
===================================================== */

function closeUserMenu() {

  isUserMenuOpen.value = false;

}


/* =====================================================
   CLICK OUTSIDE USER MENU
===================================================== */

function handleClickOutside(event) {

  if (
    userMenuRef.value &&
    !userMenuRef.value.contains(
      event.target
    )
  ) {

    closeUserMenu();

  }

}


/* =====================================================
   GO TO PROFILE
===================================================== */

function goToProfile() {

  closeUserMenu();

  router.push(
    "/user/profile"
  );

}


/* =====================================================
   GO TO CHANGE PASSWORD
===================================================== */

function goToChangePassword() {

  closeUserMenu();

  router.push(
    "/user/change-password"
  );

}


/* =====================================================
   GO TO FAVORITE
===================================================== */

function goToFavorite() {

  router.push(
    "/user/myfavorite"
  );

}


/* =====================================================
   LOGOUT
===================================================== */

function logout() {

  // Close user dropdown
  closeUserMenu();

  // Remove authentication token
  sessionStorage.removeItem("token");

  // Remove logged-in user information
  sessionStorage.removeItem("user");

  // Redirect to login page
  router.push("/login");

}


/* =====================================================
   TOGGLE SIDEBAR
===================================================== */

function toggleSidebar() {

  emit(
    "toggle-sidebar"
  );

}


/* =====================================================
   AVATAR ERROR
===================================================== */

function handleAvatarError(event) {

  console.error(
    "Navbar avatar failed:",
    event.target.src
  );


  profileImage.value = "";

}


/* =====================================================
   PROFILE UPDATED EVENT
===================================================== */

const handleProfileUpdated = () => {

  loadProfile();

};


/* =====================================================
   MOUNT
===================================================== */

onMounted(() => {

  loadProfile();


  window.addEventListener(
    "profile-updated",
    handleProfileUpdated
  );


  document.addEventListener(
    "click",
    handleClickOutside
  );

});


/* =====================================================
   UNMOUNT
===================================================== */

onUnmounted(() => {

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

/* =====================================================
   NAVBAR
===================================================== */

.navbar {

  width: 100%;

  height: 75px;

  min-height: 75px;

  background: #ffffff;

  border-bottom:
    1px solid #e5e7eb;

  box-sizing: border-box;

  position: sticky;

  top: 0;

  z-index: 900;

}


/* =====================================================
   CENTERED CONTAINER
===================================================== */

.navbar-inner {

  width: 100%;

  max-width: 1280px;

  height: 100%;

  margin-left: auto;

  margin-right: auto;

  padding-left: 24px;

  padding-right: 24px;

  display: flex;

  align-items: center;

  box-sizing: border-box;

}


/* =====================================================
   LEFT
===================================================== */

.navbar-left {

  display: flex;

  align-items: center;

  gap: 14px;

  min-width: 0;

  flex-shrink: 0;

}


/* =====================================================
   LOGO
===================================================== */

.logo-section {

  display: flex;

  align-items: center;

  gap: 10px;

  min-width: 0;

}


.logo-icon {

  width: 42px;

  height: 42px;

  flex-shrink: 0;

  display: flex;

  align-items: center;

  justify-content: center;

  overflow: hidden;

  border-radius: 10px;

}


.logo-image {

  width: 100%;

  height: 100%;

  object-fit: contain;

  display: block;

}


/* =====================================================
   LOGO TEXT
===================================================== */

.logo-text {

  display: flex;

  flex-direction: column;

  justify-content: center;

  min-width: 0;

}


.logo-text h1 {

  margin: 0;

  color: #172033;

  font-size: 20px;

  line-height: 24px;

  font-weight: 700;

  white-space: nowrap;

}


.logo-text span {

  margin-top: 2px;

  color: #8a92a3;

  font-size: 13px;

  line-height: 17px;

  white-space: nowrap;

}


/* =====================================================
   NAVIGATION
===================================================== */

.navbar-menu {

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 4px;

  margin-left: auto;

  margin-right: auto;

  height: 100%;

  min-width: 0;

}


/* =====================================================
   NAV LINK
===================================================== */

.nav-link {

  height: 42px;

  padding: 0 14px;

  display: flex;

  align-items: center;

  gap: 8px;

  border-radius: 10px;

  color: #667085;

  text-decoration: none;

  font-size: 15px;

  font-weight: 600;

  white-space: nowrap;

  transition:
    background 0.2s ease,
    color 0.2s ease;

}


/* =====================================================
   NAV ICON
===================================================== */

.nav-link i {

  font-size: 18px;

}


/* =====================================================
   NAV HOVER
===================================================== */

.nav-link:hover {

  background: #f5f3ff;

  color: #5b3df5;

}


/* =====================================================
   NAV ACTIVE
===================================================== */

.nav-link.active {

  background: #eeeaff;

  color: #5b3df5;

}


/* =====================================================
   RIGHT
===================================================== */

.navbar-right {

  display: flex;

  align-items: center;

  flex-shrink: 0;

}


/* =====================================================
   FAVORITE BUTTON
===================================================== */

.favorite-button {

  width: 42px;

  height: 42px;

  display: flex;

  align-items: center;

  justify-content: center;

  margin-right: 8px;

  border: none;

  border-radius: 10px;

  background: transparent;

  color: #667085;

  cursor: pointer;

  font-size: 22px;

  transition:
    background 0.2s ease,
    color 0.2s ease,
    transform 0.2s ease;

}


/* =====================================================
   FAVORITE HOVER
===================================================== */

.favorite-button:hover {

  background: #fff1f2;

  color: #e11d48;

  transform: scale(1.05);

}


/* =====================================================
   FAVORITE ACTIVE
===================================================== */

.favorite-button:active {

  transform: scale(0.95);

}


/* =====================================================
   USER MENU
===================================================== */

.user-menu {

  position: relative;

  display: flex;

  align-items: center;

}


/* =====================================================
   USER BUTTON
===================================================== */

.user-button {

  height: 48px;

  display: flex;

  align-items: center;

  gap: 9px;

  padding: 4px 9px 4px 5px;

  border: none;

  border-radius: 10px;

  background: transparent;

  cursor: pointer;

  transition:
    background 0.2s ease;

}


/* =====================================================
   USER BUTTON HOVER
===================================================== */

.user-button:hover {

  background: #f8f9fc;

}


/* =====================================================
   USER BUTTON OPEN
===================================================== */

.user-button-open {

  background: #f8f9fc;

}


/* =====================================================
   USER NAME
===================================================== */

.navbar-user-name {

  max-width: 150px;

  overflow: hidden;

  text-overflow: ellipsis;

  white-space: nowrap;

  color: #172033;

  font-size: 15px;

  font-weight: 600;

}


/* =====================================================
   USER CHEVRON
===================================================== */

.user-chevron {

  font-size: 12px;

  color: #667085;

  transition:
    transform 0.2s ease;

}


.user-chevron.rotated {

  transform: rotate(180deg);

}


/* =====================================================
   AVATAR
===================================================== */

.navbar-avatar {

  width: 38px;

  height: 38px;

  min-width: 38px;

  border-radius: 50%;

  overflow: hidden;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #eeeaff;

  color: #5b3df5;

  font-size: 14px;

  font-weight: 700;

  border:
    1px solid #e6e1ff;

}


/* =====================================================
   AVATAR IMAGE
===================================================== */

.avatar-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


/* =====================================================
   USER DROPDOWN
===================================================== */

.user-dropdown {

  position: absolute;

  top: calc(100% + 10px);

  right: 0;

  width: 270px;

  padding: 8px;

  background: #ffffff;

  border:
    1px solid #e5e7eb;

  border-radius: 12px;

  box-shadow:
    0 12px 30px rgba(15, 23, 42, 0.12),
    0 4px 10px rgba(15, 23, 42, 0.05);

  z-index: 1000;

}


/* =====================================================
   DROPDOWN USER INFO
===================================================== */

.dropdown-user-info {

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 10px;

}


/* =====================================================
   DROPDOWN AVATAR
===================================================== */

.dropdown-avatar {

  width: 42px;

  height: 42px;

  min-width: 42px;

  border-radius: 50%;

  overflow: hidden;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #eeeaff;

  color: #5b3df5;

  font-size: 14px;

  font-weight: 700;

  border:
    1px solid #e6e1ff;

}


.dropdown-avatar-image {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


/* =====================================================
   DROPDOWN USER DETAILS
===================================================== */

.dropdown-user-details {

  min-width: 0;

  flex: 1;

}


.dropdown-user-name {

  color: #172033;

  font-size: 14px;

  font-weight: 700;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


.dropdown-user-email {

  margin-top: 3px;

  color: #8a92a3;

  font-size: 12px;

  white-space: nowrap;

  overflow: hidden;

  text-overflow: ellipsis;

}


/* =====================================================
   DROPDOWN DIVIDER
===================================================== */

.dropdown-divider {

  height: 1px;

  background: #e5e7eb;

  margin:
    6px 4px;

}


/* =====================================================
   DROPDOWN ITEM
===================================================== */

.dropdown-item {

  width: 100%;

  height: 42px;

  display: flex;

  align-items: center;

  gap: 12px;

  padding:
    0 12px;

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


/* =====================================================
   LOGOUT ITEM
===================================================== */

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


/* =====================================================
   DROPDOWN ANIMATION
===================================================== */

.user-dropdown-enter-active,

.user-dropdown-leave-active {

  transition:
    opacity 0.15s ease,
    transform 0.15s ease;

}


.user-dropdown-enter-from,

.user-dropdown-leave-to {

  opacity: 0;

  transform:
    translateY(-6px);

}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 1100px) {

  .navbar-inner {

    padding-left: 16px;

    padding-right: 16px;

  }


  .navbar-menu {

    gap: 3px;

  }


  .nav-link {

    padding-left: 11px;

    padding-right: 11px;

    font-size: 14px;

    gap: 6px;

  }


  .nav-link i {

    font-size: 17px;

  }


  .navbar-user-name {

    max-width: 120px;

    font-size: 14px;

  }


  .favorite-button {

    width: 40px;

    height: 40px;

    font-size: 21px;

  }

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 767px) {

  .navbar {

    height: 64px;

    min-height: 64px;

  }


  .navbar-inner {

    width: 100%;

    max-width: none;

    padding-left: 8px;

    padding-right: 8px;

  }


  .navbar-left {

    flex: 1;

    min-width: 0;

  }


  /* =================================================
     LOGO MOBILE
  ================================================== */

  .logo-section {

    gap: 8px;

  }


  .logo-icon {

    width: 36px;

    height: 36px;

    border-radius: 9px;

  }


  .logo-text h1 {

    font-size: 17px;

    line-height: 20px;

  }


  /* Hide subtitle */

  .logo-text span {

    display: none;

  }


  /* =================================================
     HIDE NAVIGATION
  ================================================== */

  .navbar-menu {

    display: none;

  }


  /* =================================================
     RIGHT MOBILE
  ================================================== */

  .navbar-right {

    margin-left: auto;

    flex-shrink: 0;

  }


  /* =================================================
     FAVORITE MOBILE
  ================================================== */

  .favorite-button {

    width: 40px;

    height: 40px;

    margin-right: 5px;

    font-size: 21px;

  }


  /* =================================================
     PROFILE MOBILE
  ================================================== */

  .user-button {

    height: 44px;

    padding: 3px;

    gap: 0;

  }


  /* Hide username */

  .navbar-user-name {

    display: none;

  }


  /* Hide chevron */

  .user-chevron {

    display: none;

  }


  /* =================================================
     AVATAR MOBILE
  ================================================== */

  .navbar-avatar {

    width: 36px;

    height: 36px;

    min-width: 36px;

    font-size: 13px;

  }


  /* =================================================
     DROPDOWN MOBILE
  ================================================== */

  .user-dropdown {

    right: 0;

    width: 260px;

  }

}


/* =====================================================
   SMALL PHONE
===================================================== */

@media (max-width: 379px) {

  .navbar-inner {

    padding-left: 6px;

    padding-right: 6px;

  }


  .logo-icon {

    width: 34px;

    height: 34px;

  }


  .logo-text h1 {

    font-size: 16px;

    line-height: 19px;

  }


  /* =================================================
     FAVORITE SMALL PHONE
  ================================================== */

  .favorite-button {

    width: 36px;

    height: 36px;

    margin-right: 3px;

    font-size: 19px;

  }


  /* =================================================
     AVATAR SMALL PHONE
  ================================================== */

  .navbar-avatar {

    width: 34px;

    height: 34px;

    min-width: 34px;

    font-size: 12px;

  }


  .user-dropdown {

    width: 250px;

  }

}

</style>
```

The **only functional change** is:

```js
function logout() {
  closeUserMenu();
  sessionStorage.removeItem("token");
  sessionStorage.removeItem("user");
  router.push("/login");
}
```

Everything else remains as in your original code.
