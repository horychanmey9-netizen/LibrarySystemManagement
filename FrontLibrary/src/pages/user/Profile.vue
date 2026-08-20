<script setup>
import { ref } from "vue";
import ProfileCard from "@/components/user/ProfileCard.vue";

// =========================
// GET LOGGED-IN USER
// =========================
const storedUser = sessionStorage.getItem("user");

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

// =========================
// PROFILE DATA
// =========================
const profile = ref({
  id: user?.id || "",

  // Login user
  fullName: user?.name || user?.fullName || "",
  email: user?.email || "",

  // Backend Profile
  gender: user?.gender || "Male",
  phone: user?.phone || "",
  address: user?.address || "",
  dateOfBirth: user?.dateOfBirth || "",
  postalCode: user?.postalCode || "",

  // Avatar
  avatar: user?.avatar || "",
});
// =========================
// SAVE PROFILE
// =========================
const saveProfile = (updatedProfile) => {
  console.log(
    "Updated profile:",
    updatedProfile
  );

  // Get current user
  const currentUser = JSON.parse(
    sessionStorage.getItem("user")
  );

  // Update user
  const updatedUser = {
    ...currentUser,

    name:
      updatedProfile.fullName,

    email:
      updatedProfile.email,

    gender:
      updatedProfile.gender,

    address:
      updatedProfile.address,

    phone:
      updatedProfile.phone,

    dateOfBirth:
      updatedProfile.dateOfBirth,

    location:
      updatedProfile.location,

    postalCode:
      updatedProfile.postalCode,

    avatar:
      updatedProfile.avatar
  };

  // Save back to sessionStorage
  sessionStorage.setItem(
    "user",
    JSON.stringify(updatedUser)
  );

  console.log(
    "Updated user:",
    updatedUser
  );
};
</script>

<template>

  <!-- =========================
       USER FOUND
  ========================== -->
  <ProfileCard
    v-if="user"
    :profile="profile"
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