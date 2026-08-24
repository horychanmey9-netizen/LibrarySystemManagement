<template>
  <div class="profile-page">

    <!-- ================= HEADER ================= -->
    <div class="page-header">
      <div>
        <h1>My Profile</h1>

        <p>
          Manage your personal account information
        </p>
      </div>
    </div>


    <!-- ================= LOADING ================= -->
    <div
      v-if="loading"
      class="flex items-center justify-center py-20"
    >
      <div class="text-center">

        <div
          class="w-10 h-10 border-4 border-gray-200
                 border-t-indigo-600 rounded-full
                 animate-spin mx-auto"
        ></div>

        <p class="text-gray-500 mt-4">
          Loading profile...
        </p>

      </div>
    </div>


    <!-- ================= PROFILE ================= -->
    <ProfileCard
      v-else-if="profile"
      :profile="profile"
      :saving="saving"
      role="ADMIN"
      @save="saveProfile"
    />


    <!-- ================= ERROR ================= -->
    <div
      v-else
      class="bg-white rounded-2xl shadow-sm
             border border-gray-100 p-8 text-center"
    >

      <i
        class="bi bi-person-x text-4xl
               text-gray-400"
      ></i>

      <h2
        class="text-xl font-semibold
               text-gray-700 mt-4"
      >
        Profile not found
      </h2>

      <p class="text-gray-500 mt-2">
        Unable to load your profile information.
      </p>

    </div>

  </div>
</template>


<script setup>
import { ref, onMounted } from "vue";

import ProfileCard from "@/components/user/ProfileCard.vue";

import {
  getProfile,
  createProfile,
  updateProfile,
} from "../../service/profileservice";


// ========================================
// USER FROM SESSION
// ========================================
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


// ========================================
// PROFILE
// ========================================
const profile = ref({
  id: "",

  fullName:
    user?.name ||
    user?.fullName ||
    "",

  email:
    user?.email ||
    "",

  gender: "",
  phone: "",
  address: "",
  dateOfBirth: "",

  avatar: "",
});


// ========================================
// STATES
// ========================================
const loading = ref(true);
const saving = ref(false);
const errorMessage = ref("");


// ========================================
// GET PROFILE
// ========================================
const loadProfile = async () => {

  try {

    loading.value = true;
    errorMessage.value = "";

    const response =
      await getProfile();

    console.log(
      "ADMIN PROFILE RESPONSE:",
      response
    );

    const data =
      response?.data;

    console.log(
      "ADMIN PROFILE DATA:",
      data
    );

    if (data) {

      console.log(
        "ADMIN PROFILE IMAGE:",
        data.image
      );

      profile.value = {

        id:
          data.id ||
          "",

        fullName:
          data.fullName ||
          data.name ||
          user?.name ||
          "",

        email:
          data.email ||
          user?.email ||
          "",

        gender:
          data.gender ||
          "",

        phone:
          data.phone ||
          "",

        address:
          data.address ||
          "",

        dateOfBirth:
          data.dateOfBirth ||
          "",

        avatar:
          data.image ||
          data.avatar ||
          "",
      };

    } else {

      profile.value = null;

    }

  } catch (error) {

    console.error(
      "Get admin profile error:",
      error
    );

    errorMessage.value =
      error.message ||
      "Failed to load profile";

  } finally {

    loading.value = false;

  }

};


// ========================================
// SAVE PROFILE
// ========================================
const saveProfile = async (
  updatedProfile
) => {

  try {

    saving.value = true;
    errorMessage.value = "";

    console.log(
      "Updated admin profile:",
      updatedProfile
    );


    // ========================================
    // IMAGE FILE
    // ========================================
    const imageFile =
      updatedProfile.imageFile ||
      null;


    let response;


    // ========================================
    // UPDATE EXISTING PROFILE
    // ========================================
    if (profile.value?.id) {

      response =
        await updateProfile(
          updatedProfile,
          imageFile
        );

    }


    // ========================================
    // CREATE NEW PROFILE
    // ========================================
    else {

      response =
        await createProfile(
          updatedProfile,
          imageFile
        );

    }


    console.log(
      "SAVE ADMIN PROFILE RESPONSE:",
      response
    );


    // ========================================
    // RESPONSE DATA
    // ========================================
    const data =
      response?.data;


    if (data) {

      profile.value = {

        ...profile.value,

        id:
          data.id ||
          profile.value.id,

        fullName:
          data.fullName ||
          data.name ||
          updatedProfile.fullName,

        email:
          data.email ||
          updatedProfile.email,

        gender:
          data.gender ||
          updatedProfile.gender,

        phone:
          data.phone ||
          updatedProfile.phone,

        address:
          data.address ||
          updatedProfile.address,

        dateOfBirth:
          data.dateOfBirth ||
          updatedProfile.dateOfBirth,

        avatar:
          data.image ||
          data.avatar ||
          profile.value.avatar,
      };

    }


    // ========================================
    // UPDATE SESSION USER
    // ========================================
    const currentUser =
      JSON.parse(
        sessionStorage.getItem(
          "user"
        ) || "{}"
      );


    const updatedUser = {

      ...currentUser,

      name:
        profile.value.fullName,

      email:
        profile.value.email,

      avatar:
        profile.value.avatar,
    };


    sessionStorage.setItem(
      "user",
      JSON.stringify(
        updatedUser
      )
    );


    console.log(
      "Updated session user:",
      updatedUser
    );


    // ========================================
    // NOTIFY NAVBAR
    // ========================================
    window.dispatchEvent(
      new Event(
        "profile-updated"
      )
    );


    alert(
      "Profile updated successfully!"
    );


  } catch (error) {

    console.error(
      "Save admin profile error:",
      error
    );

    errorMessage.value =
      error.message ||
      "Failed to save profile";


    alert(
      errorMessage.value
    );

  } finally {

    saving.value = false;

  }

};


// ========================================
// LOAD PROFILE WHEN PAGE OPENS
// ========================================
onMounted(() => {

  loadProfile();

});
</script>


<style scoped>

/* ========================================
   PAGE
======================================== */

.profile-page {

  width: 100%;

  min-height:
    calc(100vh - 70px);

  padding: 30px;

  box-sizing: border-box;

  background: #f8fafc;

}


/* ========================================
   HEADER
======================================== */

.page-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  margin-bottom: 24px;

}


.page-header h1 {

  margin: 0;

  font-size: 28px;

  font-weight: 700;

  color: #1e293b;

}


.page-header p {

  margin: 6px 0 0;

  font-size: 14px;

  color: #94a3b8;

}


/* ========================================
   RESPONSIVE
======================================== */

@media (max-width: 768px) {

  .profile-page {

    padding: 20px;

  }


  .page-header h1 {

    font-size: 24px;

  }

}

</style>