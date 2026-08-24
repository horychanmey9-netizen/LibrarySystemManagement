<script setup>
import { ref, onMounted } from "vue";
import ProfileCard from "@/components/user/ProfileCard.vue";

import {
  getProfile,
  createProfile,
  updateProfile,
} from "../../service/profileservice";

// =========================
// USER FROM SESSION
// =========================
const storedUser = sessionStorage.getItem("user");

let user = null;

try {
  user = storedUser ? JSON.parse(storedUser) : null;
} catch (error) {
  console.error("Invalid user data:", error);
  user = null;
}

// =========================
// PROFILE
// =========================
const profile = ref({
  id: "",
  fullName: user?.name || user?.fullName || "",
  email: user?.email || "",

  gender: "",
  phone: "",
  address: "",
  dateOfBirth: "",

  avatar: "",
});

// =========================
// LOADING
// =========================
const loading = ref(true);
const saving = ref(false);
const errorMessage = ref("");

// =========================
// GET PROFILE
// =========================
const loadProfile = async () => {
  try {
    loading.value = true;
    errorMessage.value = "";

    const response = await getProfile();

    console.log("Profile response:", response);
    console.log("PROFILE IMAGE:", response.data.image);

    const data = response.data;

    if (data) {
      profile.value = {
        id: data.id || "",

        // User information
        fullName:
          data.fullName ||
          data.name ||
          user?.name ||
          "",

        email:
          data.email ||
          user?.email ||
          "",

        // Profile information
        gender: data.gender || "",
        phone: data.phone || "",
        address: data.address || "",
        dateOfBirth: data.dateOfBirth || "",

        // Backend uses image
        avatar:
          data.image ||
          data.avatar ||
          "",
      };
    }
  } catch (error) {
    console.error("Get profile error:", error);

    errorMessage.value =
      error.message || "Failed to load profile";
  } finally {
    loading.value = false;
  }
};

// =========================
// SAVE PROFILE
// =========================
const saveProfile = async (updatedProfile) => {
  try {
    saving.value = true;
    errorMessage.value = "";

    console.log("Updated profile:", updatedProfile);

    /*
     * ProfileCard should send image file
     * as updatedProfile.imageFile
     */
    const imageFile =
      updatedProfile.imageFile || null;

    let response;

    // Existing profile
    if (profile.value.id) {
      response = await updateProfile(
        updatedProfile,
        imageFile
      );
    }

    // New profile
    else {
      response = await createProfile(
        updatedProfile,
        imageFile
      );
    }

    console.log(
      "Save profile response:",
      response
    );

    // =========================
    // UPDATE LOCAL PROFILE
    // =========================
    const data = response.data;

    if (data) {
      profile.value = {
        ...profile.value,

        id: data.id || profile.value.id,

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

    // =========================
    // UPDATE SESSION USER
    // =========================
    const currentUser =
      JSON.parse(
        sessionStorage.getItem("user") || "{}"
      );

    const updatedUser = {
      ...currentUser,

      name:
        profile.value.fullName,

      email:
        profile.value.email,

      avatar: profile.value.avatar
    };

    sessionStorage.setItem(
      "user",
      JSON.stringify(updatedUser)
    );

    console.log(
      "Updated session user:",
      updatedUser
    );

    alert("Profile updated successfully!");
    window.location.reload();

  } catch (error) {
    console.error(
      "Save profile error:",
      error
    );

    errorMessage.value =
      error.message ||
      "Failed to save profile";

    alert(errorMessage.value);

  } finally {
    saving.value = false;
  }
};

// =========================
// LOAD WHEN PAGE OPENS
// =========================
onMounted(() => {
  loadProfile();
});
</script>

<template>

  <!-- =========================
       LOADING
  ========================== -->
  <div
    v-if="loading"
    class="min-h-[400px] flex items-center justify-center"
  >
    <div class="text-center">

      <div
        class="w-10 h-10 border-4 border-gray-200 border-t-black rounded-full animate-spin mx-auto"
      ></div>

      <p class="text-gray-500 mt-4">
        Loading profile...
      </p>

    </div>
  </div>


  <!-- =========================
       USER FOUND
  ========================== -->
  <ProfileCard
    v-else-if="user"
    :profile="profile"
    :saving="saving"
    role="USER"
    @save="saveProfile"
  />


  <!-- =========================
       USER NOT FOUND
  ========================== -->
  <div
    v-else
    class="min-h-[400px] flex items-center justify-center"
  >
    <div
      class="bg-white rounded-2xl shadow p-8 text-center"
    >

      <i
        class="bi bi-person-x text-4xl text-gray-400"
      ></i>

      <h2
        class="text-xl font-semibold text-gray-700 mt-4"
      >
        User information not found
      </h2>

      <p
        class="text-gray-500 mt-2"
      >
        Please login again.
      </p>

    </div>
  </div>

</template>