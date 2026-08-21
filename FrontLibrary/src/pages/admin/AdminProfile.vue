<template>
  <div class="profile-page">

    <!-- =========================
         LEFT SIDEBAR
    ========================== -->
    <aside class="profile-sidebar">

      <!-- Admin Profile -->
      <div class="profile-header">

        <!-- Avatar -->
        <div class="avatar">
          AD
        </div>

        <h2>{{ form.fullName }}</h2>

        <p>Administrator</p>

      </div>


      <!-- =========================
           Navigation
      ========================== -->
      <nav class="profile-navigation">

        <!-- Personal Information -->
        <button
          class="nav-item"
          :class="{ active: activeMenu === 'personal' }"
          @click="activeMenu = 'personal'"
        >
          <span class="nav-icon">👤</span>
          <span>Personal Information</span>
        </button>


        <!-- Login & Password -->
        <button
          class="nav-item"
          :class="{ active: activeMenu === 'password' }"
          @click="activeMenu = 'password'"
        >
          <span class="nav-icon">🔒</span>
          <span>Login &amp; Password</span>
        </button>


        <!-- Log Out -->
        <button
          class="nav-item"
          @click="logout"
        >
          <span class="nav-icon">↪</span>
          <span>Log Out</span>
        </button>

      </nav>


      <!-- =========================
           Security Card
      ========================== -->
      <div class="security-card">

        <div class="security-icon">
          🔐
        </div>

        <h3>
          Keep your admin account secure
        </h3>

        <p>
          Make sure your administrator information
          and password are always up to date.
        </p>

      </div>

    </aside>


    <!-- =========================
         MAIN CONTENT
    ========================== -->
    <main class="profile-content">

      <!-- =========================
           PERSONAL INFORMATION
      ========================== -->
      <div
        v-if="activeMenu === 'personal'"
        class="form-card"
      >

        <!-- Header -->
        <div class="form-header">

          <h1>
            Admin Profile
          </h1>

          <p>
            Manage your administrator account information
          </p>

        </div>


        <!-- =========================
             GENDER
        ========================== -->
        <div class="form-group gender-group">

          <label>
            Gender
          </label>

          <div class="gender-options">

            <!-- Male -->
            <label class="radio-option">

              <input
                type="radio"
                value="Male"
                v-model="form.gender"
              />

              <span class="custom-radio"></span>

              <span>Male</span>

            </label>


            <!-- Female -->
            <label class="radio-option">

              <input
                type="radio"
                value="Female"
                v-model="form.gender"
              />

              <span class="custom-radio"></span>

              <span>Female</span>

            </label>

          </div>

        </div>


        <!-- =========================
             FORM GRID
        ========================== -->
        <div class="form-grid">

          <!-- Full Name -->
          <div class="form-group">

            <label for="fullName">
              Full Name
            </label>

            <div class="input-wrapper blue">

              <div class="input-icon">
                👤
              </div>

              <input
                id="fullName"
                type="text"
                v-model="form.fullName"
              />

            </div>

          </div>


          <!-- Phone Number -->
          <div class="form-group">

            <label for="phone">
              Phone Number
            </label>

            <div class="input-wrapper pink">

              <div class="input-icon">
                ☎
              </div>

              <input
                id="phone"
                type="tel"
                v-model="form.phone"
              />

            </div>

          </div>


          <!-- Email -->
          <div class="form-group full-width">

            <label for="email">
              Email
            </label>

            <div class="input-wrapper blue">

              <div class="input-icon">
                ✉
              </div>

              <input
                id="email"
                type="email"
                v-model="form.email"
              />

              <span
                v-if="form.emailVerified"
                class="verified"
              >
                Verified
              </span>

            </div>

          </div>


          <!-- Address -->
          <div class="form-group full-width">

            <label for="address">
              Address
            </label>

            <div class="input-wrapper pink">

              <div class="input-icon">
                🏠
              </div>

              <input
                id="address"
                type="text"
                v-model="form.address"
              />

            </div>

          </div>

          <!-- Date of Birth -->
          <div class="form-group">

            <label for="dob">
              Date of Birth
            </label>

            <div class="input-wrapper pink">

              <div class="input-icon">
                📅
              </div>

              <input
                id="dob"
                type="date"
                v-model="form.dateOfBirth"
              />

            </div>

          </div>

          <!-- Postal Code -->
          <div class="form-group">

            <label for="postalCode">
              Postal Code
            </label>

            <div class="input-wrapper pink">

              <div class="input-icon">
                #
              </div>

              <input
                id="postalCode"
                type="text"
                v-model="form.postalCode"
              />

            </div>

          </div>

        </div>


        <!-- =========================
             BUTTONS
        ========================== -->
        <div class="form-actions">

          <button
            class="discard-btn"
            @click="discardChanges"
          >
            Discard Changes
          </button>

          <button
            class="save-btn"
            @click="saveChanges"
          >
            Save Changes
          </button>

        </div>

      </div>


      <!-- =========================
           LOGIN & PASSWORD
      ========================== -->
      <div
        v-else-if="activeMenu === 'password'"
        class="form-card password-card"
      >

        <div class="form-header">

          <h1>
            Login &amp; Password
          </h1>

          <p>
            Manage your administrator login information
          </p>

        </div>


        <!-- Email -->
        <div class="form-group">

          <label>
            Login Email
          </label>

          <div class="input-wrapper blue">

            <div class="input-icon">
              ✉
            </div>

            <input
              type="email"
              v-model="form.email"
            />

          </div>

        </div>


        <!-- Current Password -->
        <div class="form-group">

          <label>
            Current Password
          </label>

          <div class="input-wrapper pink">

            <div class="input-icon">
              🔒
            </div>

            <input
              type="password"
              placeholder="Enter current password"
            />

          </div>

        </div>


        <!-- New Password -->
        <div class="form-group">

          <label>
            New Password
          </label>

          <div class="input-wrapper blue">

            <div class="input-icon">
              🔑
            </div>

            <input
              type="password"
              placeholder="Enter new password"
            />

          </div>

        </div>


        <!-- Confirm Password -->
        <div class="form-group">

          <label>
            Confirm New Password
          </label>

          <div class="input-wrapper pink">

            <div class="input-icon">
              🔑
            </div>

            <input
              type="password"
              placeholder="Confirm new password"
            />

          </div>

        </div>


        <!-- Password Button -->
        <div class="form-actions">

          <button
            class="save-btn"
            @click="updatePassword"
          >
            Update Password
          </button>

        </div>

      </div>

    </main>

  </div>
</template>


<script setup>

import { reactive, ref, watch } from "vue";
import { useRouter } from "vue-router";


// =========================
// ROUTER
// =========================

const router = useRouter();


// =========================
// ACTIVE MENU
// =========================

const activeMenu = ref("personal");


// =========================
// ADMIN FORM DATA
// =========================

const form = reactive({

  gender: "Male",

  fullName: "Admin",

  phone: "+855 12 345 678",

  email: "admin@gmail.com",

  emailVerified: true,

  address: "Phnom Penh, Cambodia",

  dateOfBirth: "1995-05-15",

  location: "Phnom Penh, Cambodia",

  postalCode: "12000",

});


// =========================
// SAVE ORIGINAL DATA
// =========================

const originalForm = {

  gender: "Male",

  fullName: "Admin",

  phone: "+855 12 345 678",

  email: "admin@gmail.com",

  emailVerified: true,

  address: "Phnom Penh, Cambodia",

  dateOfBirth: "1995-05-15",

  location: "Phnom Penh, Cambodia",

  postalCode: "12000",

};


// =========================
// SAVE CHANGES
// =========================

const saveChanges = () => {

  localStorage.setItem(
    "adminName",
    form.fullName
  );

  console.log(
    "Saving admin profile:",
    form
  );

  alert(
    "Admin profile has been updated successfully!"
  );

};


// =========================
// DISCARD CHANGES
// =========================

const discardChanges = () => {

  Object.assign(
    form,
    originalForm
  );

};


// =========================
// UPDATE PASSWORD
// =========================

const updatePassword = () => {

  alert(
    "Password update feature is ready to connect with your backend."
  );

};


// =========================
// LOGOUT
// =========================

const logout = () => {

  const confirmLogout = confirm(
    "Are you sure you want to log out?"
  );

  if (!confirmLogout) {
    return;
  }

  localStorage.removeItem("adminName");

  router.push("/");

};


// =========================
// LOAD ADMIN NAME
// =========================

const savedAdminName =
  localStorage.getItem("adminName");

if (savedAdminName) {

  form.fullName =
    savedAdminName;

}


// =========================
// WATCH ADMIN NAME
// =========================

watch(
  () => form.fullName,
  (newName) => {

    if (newName) {

      localStorage.setItem(
        "adminName",
        newName
      );

    }

  }
);

</script>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.profile-page {

  min-height: calc(100vh - 75px);

  display: flex;

  gap: 24px;

  padding: 22px 24px;

  background: #ffffff;

  box-sizing: border-box;

  font-family:
    Inter,
    "Segoe UI",
    Arial,
    sans-serif;

  color: #334b70;

}


/* =====================================================
   SIDEBAR
===================================================== */

.profile-sidebar {

  width: 338px;

  min-width: 338px;

  min-height:
    calc(100vh - 119px);

  border:
    1px solid #dce8ff;

  border-radius: 18px;

  background:
    linear-gradient(
      180deg,
      #f5f9ff 0%,
      #ffffff 52%,
      #fff7fb 100%
    );

  padding:
    28px 24px;

  box-sizing: border-box;

  display: flex;

  flex-direction: column;

  box-shadow:
    0 4px 18px
    rgba(
      68,
      107,
      160,
      0.05
    );

}


/* =====================================================
   PROFILE HEADER
===================================================== */

.profile-header {

  text-align: center;

}


/* =====================================================
   AVATAR
===================================================== */

.avatar {

  width: 112px;

  height: 112px;

  margin:
    0 auto 18px;

  border-radius: 50%;

  background:
    linear-gradient(
      135deg,
      #dcecff,
      #f8e6f5
    );

  border:
    5px solid #ffffff;

  box-shadow:
    0 8px 25px
    rgba(
      57,
      83,
      123,
      0.16
    );

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 30px;

  font-weight: 700;

  color: #31527f;

}


/* =====================================================
   PROFILE NAME
===================================================== */

.profile-header h2 {

  margin: 0;

  font-size: 20px;

  font-weight: 700;

  color: #203f6b;

}


.profile-header p {

  margin:
    4px 0 0;

  font-size: 14px;

  color: #8195b7;

}


/* =====================================================
   NAVIGATION
===================================================== */

.profile-navigation {

  margin-top: 38px;

  display: flex;

  flex-direction: column;

  gap: 10px;

}


.nav-item {

  width: 100%;

  min-height: 70px;

  border: none;

  border-radius: 17px;

  background: transparent;

  color: #6780a9;

  font-size: 17px;

  font-weight: 500;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: flex-start;

  padding:
    0 22px;

  transition:
    0.2s ease;

}


.nav-item:hover {

  background:
    rgba(
      224,
      236,
      255,
      0.5
    );

}


.nav-item.active {

  background:
    linear-gradient(
      100deg,
      #f1f7ff,
      #fff1f8
    );

  color: #3178e9;

  box-shadow:
    0 4px 10px
    rgba(
      60,
      86,
      124,
      0.10
    );

  font-weight: 600;

}


/* =====================================================
   NAV ICON
===================================================== */

.nav-icon {

  width: 40px;

  height: 40px;

  min-width: 40px;

  border-radius: 11px;

  margin-right: 15px;

  background: #dcecff;

  display: flex;

  align-items: center;

  justify-content: center;

}


/* =====================================================
   SECURITY CARD
===================================================== */

.security-card {

  margin-top: auto;

  padding: 20px;

  border-radius: 19px;

  border:
    1px solid #f1dbea;

  background:
    linear-gradient(
      135deg,
      #fffafd,
      #fff7fb
    );

}


.security-icon {

  width: 45px;

  height: 45px;

  border-radius: 12px;

  margin-bottom: 17px;

  background:
    linear-gradient(
      135deg,
      #dbe9ff,
      #f7dff2
    );

  display: flex;

  align-items: center;

  justify-content: center;

}


.security-card h3 {

  margin:
    0 0 8px;

  font-size: 14px;

  font-weight: 700;

  color: #294a76;

}


.security-card p {

  margin: 0;

  font-size: 14px;

  line-height: 1.65;

  color: #8298ba;

}


/* =====================================================
   MAIN CONTENT
===================================================== */

.profile-content {

  flex: 1;

  min-width: 0;

  display: flex;

}


/* =====================================================
   FORM CARD
===================================================== */

.form-card {

  width: 100%;

  border:
    1px solid #e0eaff;

  border-radius: 18px;

  background: #ffffff;

  padding: 32px;

  box-sizing: border-box;

  box-shadow:
    0 4px 18px
    rgba(
      65,
      95,
      139,
      0.06
    );

}


/* =====================================================
   FORM HEADER
===================================================== */

.form-header {

  margin-bottom: 36px;

}


.form-header h1 {

  margin:
    0 0 8px;

  font-size: 30px;

  line-height: 1.2;

  font-weight: 700;

  color: #213f6a;

}


.form-header p {

  margin: 0;

  font-size: 17px;

  color: #8ca1c1;

}


/* =====================================================
   FORM GROUP
===================================================== */

.form-group {

  margin-bottom: 28px;

}


.form-group > label {

  display: block;

  margin-bottom: 10px;

  font-size: 15px;

  font-weight: 600;

  color: #60789f;

}


/* =====================================================
   GENDER
===================================================== */

.gender-group {

  margin-bottom: 31px;

}


.gender-options {

  display: flex;

  align-items: center;

  gap: 34px;

}


.radio-option {

  position: relative;

  display: flex;

  align-items: center;

  gap: 10px;

  cursor: pointer;

  color: #52698c;

  font-size: 17px;

}


.radio-option input {

  position: absolute;

  opacity: 0;

  pointer-events: none;

}


.custom-radio {

  width: 20px;

  height: 20px;

  border:
    1.5px solid #7b7b7b;

  border-radius: 50%;

  display: inline-flex;

  align-items: center;

  justify-content: center;

  box-sizing: border-box;

}


.radio-option input:checked
+ .custom-radio {

  border:
    2px solid #287cff;

}


.radio-option input:checked
+ .custom-radio::after {

  content: "";

  width: 10px;

  height: 10px;

  border-radius: 50%;

  background: #287cff;

}


/* =====================================================
   FORM GRID
===================================================== */

.form-grid {

  display: grid;

  grid-template-columns:
    minmax(0, 1fr)
    minmax(0, 1fr);

  column-gap: 25px;

}


.full-width {

  grid-column: 1 / -1;

}


/* =====================================================
   INPUT
===================================================== */

.input-wrapper {

  position: relative;

  height: 57px;

  border-radius: 14px;

  display: flex;

  align-items: center;

  padding:
    0 14px;

  box-sizing: border-box;

  transition:
    0.2s ease;

}


.input-wrapper.blue {

  border:
    1px solid #d5e6ff;

  background:
    linear-gradient(
      100deg,
      #fafdff,
      #ffffff
    );

}


.input-wrapper.pink {

  border:
    1px solid #f4dce9;

  background:
    linear-gradient(
      100deg,
      #fffafd,
      #ffffff
    );

}


.input-wrapper:focus-within {

  border-color:
    #80b5ff;

  box-shadow:
    0 0 0 3px
    rgba(
      60,
      130,
      246,
      0.08
    );

}


/* =====================================================
   INPUT ICON
===================================================== */

.input-icon {

  width: 40px;

  height: 40px;

  min-width: 40px;

  margin-right: 15px;

  border-radius: 10px;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 17px;

  background: #edf5ff;

  color: #6385b3;

}


.pink .input-icon {

  background: #fff1f7;

  color: #b17898;

}


/* =====================================================
   INPUT FIELD
===================================================== */

.input-wrapper input {

  flex: 1;

  width: 100%;

  border: none;

  outline: none;

  background: transparent;

  font-size: 17px;

  color: #3d5376;

  font-family: inherit;

}


.input-wrapper input::placeholder {

  color: #9aabc4;

}


/* =====================================================
   VERIFIED
===================================================== */

.verified {

  padding:
    7px 18px;

  border-radius: 20px;

  background: #eafff4;

  color: #00a866;

  font-size: 14px;

  font-weight: 600;

}


/* =====================================================
   DATE INPUT
===================================================== */

.input-wrapper input[type="date"] {

  color: #3d5376;

  appearance: auto;

}


.input-wrapper input[type="date"]::-webkit-calendar-picker-indicator {

  cursor: pointer;

  opacity: 0.9;

}


/* =====================================================
   PASSWORD CARD
===================================================== */

.password-card {

  max-width: 900px;

}


/* =====================================================
   ACTION BUTTONS
===================================================== */

.form-actions {

  display: flex;

  justify-content: flex-end;

  gap: 15px;

  margin-top: 17px;

  padding-top: 0;

}


.discard-btn,
.save-btn {

  height: 55px;

  padding:
    0 40px;

  border-radius: 16px;

  font-size: 16px;

  font-weight: 600;

  cursor: pointer;

  transition:
    0.2s ease;

}


.discard-btn {

  min-width: 215px;

  border:
    1px solid #b8d4ff;

  background: #ffffff;

  color: #2c7cf6;

}


.discard-btn:hover {

  background: #f5f9ff;

}


.save-btn {

  min-width: 200px;

  border: none;

  background:
    linear-gradient(
      100deg,
      #2e8aff,
      #d76cd0
    );

  color: #ffffff;

  box-shadow:
    0 8px 20px
    rgba(
      72,
      128,
      232,
      0.18
    );

}


.save-btn:hover {

  transform:
    translateY(-1px);

  box-shadow:
    0 10px 24px
    rgba(
      72,
      128,
      232,
      0.25
    );

}


.save-btn:active,
.discard-btn:active {

  transform:
    scale(0.98);

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 1100px) {

  .profile-page {

    padding: 18px;

  }


  .profile-sidebar {

    width: 270px;

    min-width: 270px;

  }


  .form-card {

    padding: 26px;

  }


  .form-header h1 {

    font-size: 26px;

  }


  .input-wrapper {

    height: 54px;

  }

}


/* =====================================================
   TABLET
===================================================== */

@media (max-width: 850px) {

  .profile-page {

    flex-direction: column;

  }


  .profile-sidebar {

    width: 100%;

    min-width: 100%;

    min-height: auto;

  }


  .profile-navigation {

    flex-direction: row;

    flex-wrap: wrap;

  }


  .nav-item {

    width: auto;

    flex: 1;

    min-width: 180px;

  }


  .security-card {

    margin-top: 25px;

  }

}


/* =====================================================
   MOBILE
===================================================== */

@media (max-width: 650px) {

  .profile-page {

    padding: 10px;

    gap: 15px;

  }


  .profile-sidebar {

    padding:
      20px 15px;

  }


  .profile-header h2 {

    font-size: 18px;

  }


  .avatar {

    width: 90px;

    height: 90px;

  }


  .profile-navigation {

    margin-top: 25px;

    flex-direction: column;

  }


  .nav-item {

    width: 100%;

  }


  .form-card {

    padding:
      20px 16px;

  }


  .form-header {

    margin-bottom: 28px;

  }


  .form-header h1 {

    font-size: 24px;

  }


  .form-header p {

    font-size: 14px;

  }


  .form-grid {

    grid-template-columns: 1fr;

  }


  .full-width {

    grid-column: auto;

  }


  .gender-options {

    gap: 20px;

  }


  .input-wrapper {

    height: 54px;

  }


  .input-wrapper input {

    font-size: 15px;

  }


  .verified {

    padding:
      6px 10px;

    font-size: 12px;

  }


  .form-actions {

    flex-direction: column-reverse;

  }


  .discard-btn,
  .save-btn {

    width: 100%;

  }

}

</style>