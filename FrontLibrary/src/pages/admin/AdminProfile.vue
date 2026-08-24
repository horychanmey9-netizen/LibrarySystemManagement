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


    <!-- ================= PROFILE CARD ================= -->
    <ProfileCard
  :profile="profile"
  :saving="saving"
  role="ADMIN"
  @save="saveProfile"
/>

  </div>
</template>


<script setup>
import { ref, onMounted } from "vue";

import ProfileCard from "@/components/user/ProfileCard.vue";


// ========================================
// PROFILE DATA
// ========================================

const profile = ref({

  fullName: "John Doe",

  email: "john@gmail.com",

  gender: "Male",

  phone: "+855 12 345 678",

  address: "Phnom Penh, Cambodia",

  dateOfBirth: "2000-05-15",

  avatar: "",

});


// ========================================
// SAVING
// ========================================

const saving = ref(false);


// ========================================
// LOAD PROFILE
// ========================================

onMounted(() => {

  const savedProfile =
    localStorage.getItem("userProfile");

  if (savedProfile) {

    try {

      profile.value = {
        ...profile.value,
        ...JSON.parse(savedProfile),
      };

    } catch (error) {

      console.error(
        "Failed to load profile:",
        error
      );

    }

  }

});


// ========================================
// SAVE PROFILE
// ========================================

const handleSave = async (updatedProfile) => {

  try {

    saving.value = true;

    console.log(
      "Updated Profile:",
      updatedProfile
    );


    // ====================================
    // SAVE TO LOCAL STORAGE FOR NOW
    // ====================================

    profile.value = {
      ...profile.value,
      ...updatedProfile,
    };


    localStorage.setItem(
      "userProfile",
      JSON.stringify(profile.value)
    );


    // ====================================
    // BACKEND API
    // ====================================
    //
    // Later you can replace the localStorage
    // part with your backend API:
    //
    // await profileService.updateProfile(
    //   updatedProfile
    // );
    //
    // ====================================


    alert(
      "Profile updated successfully!"
    );

  } catch (error) {

    console.error(
      "Update profile error:",
      error
    );

    alert(
      "Failed to update profile."
    );

  } finally {

    saving.value = false;

  }

};

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