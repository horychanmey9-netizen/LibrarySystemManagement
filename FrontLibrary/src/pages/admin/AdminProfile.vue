<template>
  <div class="profile-page">

    <!-- =====================================================
         LOADING
    ====================================================== -->

    <div
      v-if="loading"
      class="flex items-center justify-center py-20"
    >
      <div class="text-center">

        <div
          class="mx-auto h-10 w-10 animate-spin rounded-full border-4 border-gray-200 border-t-indigo-600"
        ></div>

        <p class="mt-4 text-gray-500">
          Loading profile...
        </p>

      </div>
    </div>


    <!-- =====================================================
         PROFILE
    ====================================================== -->

    <ProfileCard
      v-else-if="profile"
      :profile="profile"
      :saving="saving"
      role="ADMIN"

      :telegram-status="telegramStatus"
      :telegram-connection="telegramConnection"
      :telegram-loading="telegramLoading"
      :telegram-status-loading="telegramStatusLoading"

      @save="saveProfile"
      @telegram-connect="handleTelegramConnect"
      @telegram-disconnect="handleTelegramDisconnect"
    />


    <!-- =====================================================
         ERROR
    ====================================================== -->

    <div
      v-else
      class="rounded-2xl border border-gray-100 bg-white p-8 text-center shadow-sm"
    >

      <i
        class="bi bi-person-x text-4xl text-gray-400"
      ></i>

      <h2
        class="mt-4 text-xl font-semibold text-gray-700"
      >
        Profile not found
      </h2>

      <p class="mt-2 text-gray-500">
        Unable to load your profile information.
      </p>

    </div>

  </div>
</template>


<script setup>

import {
  ref,
  onMounted,
  onBeforeUnmount,
} from "vue";

import ProfileCard
  from "@/components/user/ProfileCard.vue";

import {
  getProfile,
  createProfile,
  updateProfile,
} from "../../service/profileservice";

import {
  getTelegramStatus,
  connectTelegram,
  disconnectTelegram,
} from "../../service/telegramService";


// =========================================================
// USER FROM SESSION
// =========================================================

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


// =========================================================
// PROFILE
// =========================================================

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


// =========================================================
// PROFILE STATES
// =========================================================

const loading = ref(true);

const saving = ref(false);

const errorMessage = ref("");


// =========================================================
// TELEGRAM STATES
// =========================================================

const telegramStatus = ref({

  connected: false,

  chatId: null,

  username: null,

  connectedAt: null,

});


const telegramConnection = ref({

  code: "",

  botUsername: "",

  telegramLink: "",

  message: "",

});


const telegramLoading =
  ref(false);


const telegramStatusLoading =
  ref(false);


// =========================================================
// TELEGRAM POLLING
// =========================================================

let telegramPolling = null;


// =========================================================
// GET PROFILE
// =========================================================

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


      console.log(
        "PROFILE STATE:",
        profile.value
      );

    } else {

      profile.value = null;

    }

  } catch (error) {

    console.error(
      "Get admin profile error:",
      error
    );


    errorMessage.value =
      error?.message ||
      "Failed to load profile";


  } finally {

    loading.value = false;

  }

};


// =========================================================
// SAVE PROFILE
// =========================================================

const saveProfile =
  async (profileData) => {

  try {

    saving.value = true;


    console.log(
      "PROFILE DATA RECEIVED:",
      profileData
    );


    let response;


    // =====================================================
    // UPDATE
    // =====================================================

    if (profileData?.id) {

      console.log(
        "Updating existing profile..."
      );


      response =
        await updateProfile(
          profileData,
          profileData.imageFile
        );


      console.log(
        "PROFILE UPDATE RESPONSE:",
        response
      );

    }


    // =====================================================
    // CREATE
    // =====================================================

    else {

      console.log(
        "Creating new profile..."
      );


      response =
        await createProfile(
          profileData,
          profileData.imageFile
        );


      console.log(
        "PROFILE CREATE RESPONSE:",
        response
      );

    }


    // =====================================================
    // UPDATE LOCAL PROFILE
    // =====================================================

    const data =
      response?.data;


    if (data) {

      profile.value = {

        ...profile.value,

        id:
          data.id ||
          profileData.id ||
          "",

        fullName:
          data.fullName ||
          data.name ||
          profileData.fullName ||
          "",

        email:
          data.email ||
          profileData.email ||
          "",

        gender:
          data.gender ||
          profileData.gender ||
          "",

        phone:
          data.phone ||
          profileData.phone ||
          "",

        address:
          data.address ||
          profileData.address ||
          "",

        dateOfBirth:
          data.dateOfBirth ||
          profileData.dateOfBirth ||
          "",

        avatar:
          data.image ||
          data.avatar ||
          profileData.avatar ||
          "",

      };


      console.log(
        "PROFILE UPDATED LOCALLY:",
        profile.value
      );

    }


    // =====================================================
    // RELOAD PROFILE
    // =====================================================

    await loadProfile();


  } catch (error) {

    console.error(
      "Save admin profile error:",
      error
    );


  } finally {

    saving.value = false;

  }

};


// =========================================================
// GET TELEGRAM STATUS
// =========================================================

const loadTelegramStatus =
  async () => {

  try {

    telegramStatusLoading.value =
      true;


    const response =
      await getTelegramStatus();


    console.log(
      "TELEGRAM STATUS RESPONSE:",
      response
    );


    const data =
      response?.data;


    console.log(
      "TELEGRAM STATUS DATA:",
      data
    );


    if (data) {

      telegramStatus.value = {

        connected:
          data.connected === true,

        chatId:
          data.chatId ??
          null,

        username:
          data.username ??
          null,

        connectedAt:
          data.connectedAt ??
          null,

      };


      console.log(
        "TELEGRAM STATUS UPDATED:",
        telegramStatus.value
      );


      // ===================================================
      // IF CONNECTED
      // ===================================================

      if (
        telegramStatus.value.connected
      ) {

        stopTelegramPolling();


        // Connection is completed.
        // We no longer need the old code.

        telegramConnection.value = {

          code: "",

          botUsername: "",

          telegramLink: "",

          message:
            "Telegram connected successfully.",

        };

      }

    }

  } catch (error) {

    console.error(
      "Get Telegram status error:",
      error
    );

  } finally {

    telegramStatusLoading.value =
      false;

  }

};


// =========================================================
// CONNECT TELEGRAM
// =========================================================

const handleTelegramConnect =
  async () => {

  try {

    telegramLoading.value =
      true;


    console.log(
      "Generating Telegram connection..."
    );


    const response =
      await connectTelegram();


    console.log(
      "TELEGRAM CONNECT RESPONSE:",
      response
    );


    const data =
      response?.data;


    console.log(
      "TELEGRAM CONNECTION DATA:",
      data
    );


    if (data) {

      telegramConnection.value = {

        /*
         * Support both:
         *
         * data.code
         *
         * and
         *
         * data.connectionCode
         */

        code:
          data.code ||
          data.connectionCode ||
          "",


        botUsername:
          data.botUsername ||
          data.username ||
          "",


        telegramLink:
          data.telegramLink ||
          data.link ||
          "",


        message:
          data.message ||
          "Open Telegram and press Start.",

      };


      console.log(
        "TELEGRAM CONNECTION STATE:",
        telegramConnection.value
      );


      // =================================================
      // START POLLING
      // =================================================

      startTelegramPolling();

    }

  } catch (error) {

    console.error(
      "Connect Telegram error:",
      error
    );

  } finally {

    telegramLoading.value =
      false;

  }

};


// =========================================================
// START TELEGRAM POLLING
// =========================================================

const startTelegramPolling =
  () => {

  // Prevent multiple intervals.

  stopTelegramPolling();


  console.log(
    "Starting Telegram status polling..."
  );


  telegramPolling =
    setInterval(
      async () => {

        console.log(
          "Checking Telegram connection..."
        );


        await loadTelegramStatus();


        // loadTelegramStatus()
        // automatically stops polling
        // if connected.

      },
      3000
    );

};


// =========================================================
// STOP TELEGRAM POLLING
// =========================================================

const stopTelegramPolling =
  () => {

  if (telegramPolling) {

    clearInterval(
      telegramPolling
    );


    telegramPolling = null;


    console.log(
      "Telegram polling stopped."
    );

  }

};


// =========================================================
// DISCONNECT TELEGRAM
// =========================================================

const handleTelegramDisconnect =
  async () => {

  try {

    telegramLoading.value =
      true;


    console.log(
      "Disconnecting Telegram..."
    );


    await disconnectTelegram();


    console.log(
      "Telegram disconnected successfully."
    );


    stopTelegramPolling();


    telegramStatus.value = {

      connected: false,

      chatId: null,

      username: null,

      connectedAt: null,

    };


    telegramConnection.value = {

      code: "",

      botUsername: "",

      telegramLink: "",

      message: "",

    };

  } catch (error) {

    console.error(
      "Disconnect Telegram error:",
      error
    );

  } finally {

    telegramLoading.value =
      false;

  }

};


// =========================================================
// PAGE LOAD
// =========================================================

onMounted(
  async () => {

    await loadProfile();

    await loadTelegramStatus();

  }
);


// =========================================================
// PAGE UNMOUNT
// =========================================================

onBeforeUnmount(() => {

  stopTelegramPolling();

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

  padding: 16px;

  box-sizing: border-box;

  background:
    #f8fafc;

}


/* ========================================
   HEADER
======================================== */

.page-header {

  display: flex;

  align-items: center;

  justify-content: space-between;

  margin-bottom: 16px;

}


.page-header h1 {

  margin: 0;

  font-size: 26px;

  font-weight: 700;

  color: #1e293b;

}


.page-header p {

  margin: 4px 0 0;

  font-size: 14px;

  color: #94a3b8;

}


/* ========================================
   RESPONSIVE
======================================== */

@media (max-width: 768px) {

  .profile-page {

    padding: 12px;

  }


  .page-header {

    margin-bottom: 12px;

  }


  .page-header h1 {

    font-size: 22px;

  }

}

</style>