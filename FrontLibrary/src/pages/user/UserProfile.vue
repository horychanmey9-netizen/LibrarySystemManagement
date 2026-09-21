<script setup>
import {
  ref,
  onMounted,
  onUnmounted,
} from "vue";

import ProfileCard from "@/components/user/ProfileCard.vue";

import {
  getProfile,
  createProfile,
  updateProfile,
} from "../../service/profileservice";

import {
  getTelegramStatus,
  connectTelegram,
  disconnectTelegram,
} from "../../service/telegramservice";

/* =========================================================
   USER
========================================================= */

const user = ref(null);

const profile = ref({
  id: null,
  fullName: "",
  email: "",
  gender: "",
  phone: "",
  address: "",
  dateOfBirth: "",
  avatar: "",
});

/* =========================================================
   PROFILE STATE
========================================================= */

const loading = ref(true);
const saving = ref(false);
const errorMessage = ref("");

/* =========================================================
   TELEGRAM STATE
========================================================= */

const telegramLoading = ref(false);
const telegramStatusLoading = ref(false);

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

/* =========================================================
   TELEGRAM POLLING
========================================================= */

let telegramPolling = null;

/* =========================================================
   LOAD PROFILE
========================================================= */

const loadProfile = async () => {
  try {
    loading.value = true;
    errorMessage.value = "";

    const sessionUser = sessionStorage.getItem("user");

    if (!sessionUser) {
      errorMessage.value = "User information not found.";
      return;
    }

    user.value = JSON.parse(sessionUser);

    const response = await getProfile();

    console.log("PROFILE RESPONSE:", response);

    /*
     * Adjust these fallbacks depending on your backend response.
     */

    profile.value = {
      id:
        response?.id ||
        user.value?.id ||
        null,

      fullName:
        response?.fullName ||
        user.value?.fullName ||
        user.value?.name ||
        "",

      email:
        response?.email ||
        user.value?.email ||
        "",

      gender:
        response?.gender || "",

      phone:
        response?.phone || "",

      address:
        response?.address || "",

      dateOfBirth:
        response?.dateOfBirth || "",

      avatar:
        response?.avatar ||
        response?.image ||
        "",
    };
  } catch (error) {
    console.error("Load profile error:", error);

    errorMessage.value =
      error?.message ||
      "Failed to load profile.";
  } finally {
    loading.value = false;
  }
};

/* =========================================================
   SAVE PROFILE
========================================================= */

const saveProfile = async (updatedProfile) => {
  try {
    saving.value = true;
    errorMessage.value = "";

    const imageFile =
      updatedProfile?.imageFile || null;

    const payload = {
      phone: updatedProfile.phone || "",
      gender: updatedProfile.gender || "",
      dateOfBirth:
        updatedProfile.dateOfBirth || "",
      address: updatedProfile.address || "",
    };

    let response;

    /*
     * If profile already exists -> UPDATE
     * Otherwise -> CREATE
     */

    if (profile.value.id) {
      response = await updateProfile(
        payload,
        imageFile
      );
    } else {
      response = await createProfile(
        payload,
        imageFile
      );
    }

    console.log(
      "SAVE PROFILE RESPONSE:",
      response
    );

    /*
     * Update local profile.
     */

    profile.value = {
      ...profile.value,
      ...updatedProfile,
      avatar:
        response?.avatar ||
        response?.image ||
        updatedProfile?.avatar ||
        profile.value.avatar,
    };

    /*
     * Update session user.
     */

    const currentUser =
      sessionStorage.getItem("user");

    if (currentUser) {
      const parsedUser =
        JSON.parse(currentUser);

      const updatedUser = {
        ...parsedUser,
        fullName:
          updatedProfile.fullName ||
          parsedUser.fullName,

        email:
          updatedProfile.email ||
          parsedUser.email,

        avatar:
          profile.value.avatar ||
          parsedUser.avatar,
      };

      sessionStorage.setItem(
        "user",
        JSON.stringify(updatedUser)
      );

      user.value = updatedUser;
    }

    /*
     * Notify other components.
     */

    window.dispatchEvent(
      new CustomEvent("profile-updated", {
        detail: profile.value,
      })
    );
  } catch (error) {
    console.error(
      "Save profile error:",
      error
    );

    alert(
      error?.message ||
        "Failed to save profile."
    );
  } finally {
    saving.value = false;
  }
};

/* =========================================================
   LOAD TELEGRAM STATUS
========================================================= */

const loadTelegramStatus = async () => {
  try {
    telegramStatusLoading.value = true;

    const response =
      await getTelegramStatus();

    console.log(
      "Telegram status:",
      response
    );

    telegramStatus.value = {
      connected:
        response?.connected || false,

      chatId:
        response?.chatId || null,

      username:
        response?.username || null,

      connectedAt:
        response?.connectedAt || null,
    };

    /*
     * If already connected, clear old
     * connection information.
     */

    if (telegramStatus.value.connected) {
      telegramConnection.value = {
        code: "",
        botUsername: "",
        telegramLink: "",
        message: "",
      };

      stopTelegramPolling();
    }
  } catch (error) {
    console.error(
      "Get Telegram status error:",
      error
    );
  } finally {
    telegramStatusLoading.value = false;
  }
};

/* =========================================================
   CONNECT TELEGRAM
========================================================= */

const handleTelegramConnect =
  async () => {
    try {
      telegramLoading.value = true;

      const response =
        await connectTelegram();

      console.log(
        "Telegram connection:",
        response
      );

      telegramConnection.value = {
        code:
          response?.code || "",

        botUsername:
          response?.botUsername || "",

        telegramLink:
          response?.telegramLink || "",

        message:
          response?.message || "",
      };

      /*
       * Check immediately.
       */

      await loadTelegramStatus();

      /*
       * Telegram connection happens asynchronously
       * through the Telegram bot webhook.
       *
       * Therefore poll every 3 seconds.
       */

      startTelegramPolling();
    } catch (error) {
      console.error(
        "Connect Telegram error:",
        error
      );

      alert(
        error?.message ||
          "Failed to connect Telegram."
      );
    } finally {
      telegramLoading.value = false;
    }
  };

/* =========================================================
   DISCONNECT TELEGRAM
========================================================= */

const handleTelegramDisconnect =
  async () => {
    const confirmed =
      window.confirm(
        "Are you sure you want to disconnect your Telegram account?"
      );

    if (!confirmed) {
      return;
    }

    try {
      telegramLoading.value = true;

      await disconnectTelegram();

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

      stopTelegramPolling();
    } catch (error) {
      console.error(
        "Disconnect Telegram error:",
        error
      );

      alert(
        error?.message ||
          "Failed to disconnect Telegram."
      );
    } finally {
      telegramLoading.value = false;
    }
  };

/* =========================================================
   START TELEGRAM POLLING
========================================================= */

const startTelegramPolling = () => {
  stopTelegramPolling();

  telegramPolling =
    setInterval(async () => {
      /*
       * Don't create another request while
       * the previous request is loading.
       */

      if (
        telegramStatusLoading.value
      ) {
        return;
      }

      await loadTelegramStatus();

      /*
       * loadTelegramStatus automatically
       * stops polling when connected.
       */
    }, 3000);
};

/* =========================================================
   STOP TELEGRAM POLLING
========================================================= */

const stopTelegramPolling = () => {
  if (telegramPolling) {
    clearInterval(telegramPolling);
    telegramPolling = null;
  }
};

/* =========================================================
   MOUNT
========================================================= */

onMounted(() => {
  loadProfile();
  loadTelegramStatus();
});

/* =========================================================
   UNMOUNT
========================================================= */

onUnmounted(() => {
  stopTelegramPolling();
});
</script>

<template>
  <div
    class="min-h-screen bg-slate-50 px-3 py-4 sm:px-6 sm:py-6 lg:px-8"
  >
    <!-- =====================================================
         LOADING
    ====================================================== -->

    <div
      v-if="loading"
      class="flex min-h-[500px] items-center justify-center"
    >
      <div class="flex flex-col items-center gap-3">
        <div
          class="h-8 w-8 animate-spin rounded-full border-4 border-slate-200 border-t-blue-600"
        ></div>

        <p class="text-sm text-slate-500">
          Loading profile...
        </p>
      </div>
    </div>

    <!-- =====================================================
         ERROR
    ====================================================== -->

    <div
      v-else-if="errorMessage"
      class="mx-auto max-w-4xl rounded-2xl border border-red-200 bg-red-50 p-6 text-center"
    >
      <div
        class="mx-auto mb-3 flex h-12 w-12 items-center justify-center rounded-full bg-red-100 text-red-600"
      >
        !
      </div>

      <h2
        class="text-lg font-semibold text-red-800"
      >
        Unable to load profile
      </h2>

      <p
        class="mt-2 text-sm text-red-600"
      >
        {{ errorMessage }}
      </p>

      <button
        type="button"
        class="mt-5 rounded-lg bg-red-600 px-5 py-2.5 text-sm font-medium text-white transition hover:bg-red-700"
        @click="loadProfile"
      >
        Try Again
      </button>
    </div>

    <!-- =====================================================
         PROFILE
    ====================================================== -->

    <div
      v-else
      class="mx-auto w-full max-w-6xl"
    >
      <ProfileCard
        :profile="profile"
        :saving="saving"
        role="ADMIN"
        :telegram-status="telegramStatus"
        :telegram-connection="
          telegramConnection
        "
        :telegram-loading="
          telegramLoading
        "
        :telegram-status-loading="
          telegramStatusLoading
        "
        @save="saveProfile"
        @telegram-connect="
          handleTelegramConnect
        "
        @telegram-disconnect="
          handleTelegramDisconnect
        "
      />
    </div>
  </div>
</template>