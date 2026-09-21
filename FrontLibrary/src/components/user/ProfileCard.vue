<script setup>
import {
  computed,
  ref,
  watch,
} from "vue";

/* =========================================================
   PROPS
========================================================= */

const props = defineProps({
  profile: {
    type: Object,
    required: true,
  },

  saving: {
    type: Boolean,
    default: false,
  },

  role: {
    type: String,
    default: "",
  },

  telegramStatus: {
    type: Object,
    default: () => ({
      connected: false,
      chatId: null,
      username: null,
      connectedAt: null,
    }),
  },

  telegramConnection: {
    type: Object,
    default: () => ({
      code: "",
      botUsername: "",
      telegramLink: "",
      message: "",
    }),
  },

  telegramLoading: {
    type: Boolean,
    default: false,
  },

  telegramStatusLoading: {
    type: Boolean,
    default: false,
  },
});

/* =========================================================
   EMITS
========================================================= */

const emit = defineEmits([
  "save",
  "telegram-connect",
  "telegram-disconnect",
]);

/* =========================================================
   LOCAL STATE
========================================================= */

const editing = ref(false);

const imageFile = ref(null);

const imagePreview = ref("");

const copied = ref(false);

const activeSection = ref("profile");

/* =========================================================
   FORM
========================================================= */

const form = ref({
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
   COPY PROFILE INTO FORM
========================================================= */

const syncForm = () => {
  form.value = {
    id: props.profile?.id || null,

    fullName:
      props.profile?.fullName || "",

    email:
      props.profile?.email || "",

    gender:
      props.profile?.gender || "",

    phone:
      props.profile?.phone || "",

    address:
      props.profile?.address || "",

    dateOfBirth:
      props.profile?.dateOfBirth || "",

    avatar:
      props.profile?.avatar || "",
  };

  imagePreview.value =
    props.profile?.avatar || "";
};

/* =========================================================
   WATCH PROFILE
========================================================= */

watch(
  () => props.profile,
  () => {
    if (!editing.value) {
      syncForm();
    }
  },
  {
    deep: true,
    immediate: true,
  }
);

/* =========================================================
   ROLE LABEL
========================================================= */

const roleLabel = computed(() => {
  if (!props.role) {
    return "User";
  }

  return (
    props.role.charAt(0).toUpperCase() +
    props.role.slice(1).toLowerCase()
  );
});

/* =========================================================
   INITIALS
========================================================= */

const initials = computed(() => {
  const name =
    form.value.fullName ||
    form.value.email ||
    "User";

  const parts = name
    .trim()
    .split(/\s+/)
    .filter(Boolean);

  if (parts.length === 1) {
    return parts[0]
      .substring(0, 2)
      .toUpperCase();
  }

  return (
    parts[0][0] +
    parts[parts.length - 1][0]
  ).toUpperCase();
});

/* =========================================================
   DATE FORMAT
========================================================= */

const formattedConnectedDate =
  computed(() => {
    if (
      !props.telegramStatus?.connectedAt
    ) {
      return "";
    }

    const date = new Date(
      props.telegramStatus.connectedAt
    );

    if (Number.isNaN(date.getTime())) {
      return props.telegramStatus
        .connectedAt;
    }

    return date.toLocaleString(
      undefined,
      {
        dateStyle: "medium",
        timeStyle: "short",
      }
    );
  });

/* =========================================================
   EDIT
========================================================= */

const startEditing = () => {
  syncForm();
  editing.value = true;
};

const cancelEditing = () => {
  syncForm();
  imageFile.value = null;
  imagePreview.value =
    props.profile?.avatar || "";
  editing.value = false;
};

/* =========================================================
   IMAGE
========================================================= */

const handleImageChange = (event) => {
  const file =
    event.target.files?.[0];

  if (!file) {
    return;
  }

  /*
   * Limit to common image types.
   */

  if (!file.type.startsWith("image/")) {
    alert(
      "Please select a valid image file."
    );

    return;
  }

  imageFile.value = file;

  imagePreview.value =
    URL.createObjectURL(file);
};

/* =========================================================
   SAVE
========================================================= */

const save = () => {
  emit("save", {
    ...form.value,
    imageFile: imageFile.value,
    avatar: imagePreview.value,
  });

  editing.value = false;
};

/* =========================================================
   TELEGRAM CONNECT
========================================================= */

const connectTelegram = () => {
  emit("telegram-connect");
};

/* =========================================================
   TELEGRAM DISCONNECT
========================================================= */

const disconnectTelegram = () => {
  emit("telegram-disconnect");
};

/* =========================================================
   COPY CODE
========================================================= */

const copyConnectionCode =
  async () => {
    const code =
      props.telegramConnection?.code;

    if (!code) {
      return;
    }

    try {
      await navigator.clipboard.writeText(
        code
      );

      copied.value = true;

      setTimeout(() => {
        copied.value = false;
      }, 2000);
    } catch (error) {
      console.error(
        "Copy failed:",
        error
      );

      alert(
        "Could not copy the connection code."
      );
    }
  };

/* =========================================================
   OPEN TELEGRAM
========================================================= */

const openTelegram = () => {
  const link =
    props.telegramConnection
      ?.telegramLink;

  if (!link) {
    return;
  }

  window.open(
    link,
    "_blank",
    "noopener,noreferrer"
  );
};
</script>

<template>
  <div
    class="overflow-hidden rounded-2xl border border-slate-200 bg-white shadow-sm"
  >
    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div
      class="border-b border-slate-200 bg-white px-4 py-5 sm:px-6 lg:px-8"
    >
      <div
        class="flex flex-col gap-5 sm:flex-row sm:items-center sm:justify-between"
      >
        <!-- USER -->

        <div
          class="flex min-w-0 items-center gap-4"
        >
          <!-- AVATAR -->

<div
  class="relative h-16 w-16 shrink-0 sm:h-20 sm:w-20"
>
  <!-- PROFILE IMAGE -->

  <div
    class="flex h-full w-full items-center justify-center overflow-hidden rounded-full bg-blue-100 text-lg font-bold text-blue-700 ring-4 ring-blue-50 sm:text-xl"
  >
    <img
      v-if="
        imagePreview ||
        form.avatar
      "
      :src="
        imagePreview ||
        form.avatar
      "
      alt="Profile"
      class="h-full w-full object-cover"
    />

    <span v-else>
      {{ initials }}
    </span>
  </div>

  <!-- CAMERA BUTTON -->

  <label
  v-if="editing"
  class="absolute bottom-0 right-0 flex h-6 w-6 cursor-pointer items-center justify-center rounded-full bg-blue-600 text-white shadow-sm ring-2 ring-white transition hover:bg-blue-700 sm:h-7 sm:w-7"
  title="Change profile photo"
>
  <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-camera" viewBox="0 0 16 16">
  <path d="M15 12a1 1 0 0 1-1 1H2a1 1 0 0 1-1-1V6a1 1 0 0 1 1-1h1.172a3 3 0 0 0 2.12-.879l.83-.828A1 1 0 0 1 6.827 3h2.344a1 1 0 0 1 .707.293l.828.828A3 3 0 0 0 12.828 5H14a1 1 0 0 1 1 1zM2 4a2 2 0 0 0-2 2v6a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V6a2 2 0 0 0-2-2h-1.172a2 2 0 0 1-1.414-.586l-.828-.828A2 2 0 0 0 9.172 2H6.828a2 2 0 0 0-1.414.586l-.828.828A2 2 0 0 1 3.172 4z"/>
  <path d="M8 11a2.5 2.5 0 1 1 0-5 2.5 2.5 0 0 1 0 5m0 1a3.5 3.5 0 1 0 0-7 3.5 3.5 0 0 0 0 7M3 6.5a.5.5 0 1 1-1 0 .5.5 0 0 1 1 0"/>
  </svg>

  <input
    type="file"
    accept="image/*"
    class="hidden"
    @change="handleImageChange"
  />
</label>
</div>

          <!-- NAME -->

          <div class="min-w-0">
            <div
              class="flex flex-wrap items-center gap-2"
            >
              <h1
                class="truncate text-lg font-bold text-slate-900 sm:text-xl"
              >
                {{
                  form.fullName ||
                  "User"
                }}
              </h1>

              <span
                class="rounded-full bg-blue-50 px-2.5 py-1 text-xs font-semibold text-blue-700"
              >
                {{ roleLabel }}
              </span>
            </div>

            <p
              class="mt-1 truncate text-sm text-slate-500"
            >
              {{ form.email || "No email" }}
            </p>
          </div>
        </div>

        <!-- EDIT BUTTON -->

        <div class="shrink-0">
          <button
            v-if="!editing"
            type="button"
            class="inline-flex w-full items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 sm:w-auto"
            @click="startEditing"
          >
            <!-- Pencil -->

            <svg
              class="h-4 w-4"
              viewBox="0 0 24 24"
              fill="none"
              stroke="currentColor"
              stroke-width="2"
            >
              <path
                d="M12 20h9"
              />
              <path
                d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4Z"
              />
            </svg>

            Edit Profile
          </button>
        </div>
      </div>
    </div>

    <!-- =====================================================
         NAVIGATION
    ====================================================== -->

    <div
      class="border-b border-slate-200 bg-slate-50 px-3 sm:px-6 lg:px-8"
    >
      <div
        class="flex overflow-x-auto"
      >
        <button
          type="button"
          class="shrink-0 border-b-2 px-4 py-3 text-sm font-semibold transition"
          :class="
            activeSection === 'profile'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-700'
          "
          @click="
            activeSection = 'profile'
          "
        >
          Profile
        </button>

        <button
          type="button"
          class="shrink-0 border-b-2 px-4 py-3 text-sm font-semibold transition"
          :class="
            activeSection === 'telegram'
              ? 'border-blue-600 text-blue-600'
              : 'border-transparent text-slate-500 hover:text-slate-700'
          "
          @click="
            activeSection = 'telegram'
          "
        >
          Telegram
        </button>
      </div>
    </div>

    <!-- =====================================================
         CONTENT
    ====================================================== -->

    <div
      class="p-4 sm:p-6 lg:p-8"
    >
      <!-- ===================================================
           PROFILE SECTION
      ==================================================== -->

      <div
        v-if="activeSection === 'profile'"
        class="space-y-6"
      >
        <!-- PROFILE INFO -->

        <div>
          <div
            class="mb-4"
          >
            <h2
              class="text-base font-bold text-slate-900"
            >
              Personal Information
            </h2>

            <p
              class="mt-1 text-sm text-slate-500"
            >
              Manage your personal profile
              information.
            </p>
          </div>

          <!-- FORM -->

          <div
            class="grid grid-cols-1 gap-5 md:grid-cols-2"
          >
            <!-- FULL NAME -->

            <div>
              <label
                class="mb-2 block text-sm font-semibold text-slate-700"
              >
                Full Name
              </label>

              <input
                v-model="form.fullName"
                type="text"
                disabled
                class="w-full rounded-lg border border-slate-200 bg-slate-50 px-4 py-2.5 text-sm text-slate-500 outline-none"
              />
            </div>

            <!-- EMAIL -->

            <div>
              <label
                class="mb-2 block text-sm font-semibold text-slate-700"
              >
                Email
              </label>

              <input
                v-model="form.email"
                type="email"
                disabled
                class="w-full rounded-lg border border-slate-200 bg-slate-50 px-4 py-2.5 text-sm text-slate-500 outline-none"
              />
            </div>

            <!-- PHONE -->

            <div>
              <label
                class="mb-2 block text-sm font-semibold text-slate-700"
              >
                Phone Number
              </label>

              <input
                v-model="form.phone"
                type="tel"
                :disabled="!editing"
                placeholder="Enter phone number"
                class="w-full rounded-lg border px-4 py-2.5 text-sm outline-none transition"
                :class="
                  editing
                    ? 'border-slate-300 bg-white text-slate-900 focus:border-blue-500 focus:ring-2 focus:ring-blue-100'
                    : 'border-slate-200 bg-slate-50 text-slate-500'
                "
              />
            </div>

            <!-- GENDER -->

            <div>
              <label
                class="mb-2 block text-sm font-semibold text-slate-700"
              >
                Gender
              </label>

              <select
                v-model="form.gender"
                :disabled="!editing"
                class="w-full rounded-lg border px-4 py-2.5 text-sm outline-none transition"
                :class="
                  editing
                    ? 'border-slate-300 bg-white text-slate-900 focus:border-blue-500 focus:ring-2 focus:ring-blue-100'
                    : 'border-slate-200 bg-slate-50 text-slate-500'
                "
              >
                <option value="">
                  Select gender
                </option>

                <option value="MALE">
                  Male
                </option>

                <option value="FEMALE">
                  Female
                </option>

                <option value="OTHER">
                  Other
                </option>
              </select>
            </div>

            <!-- DATE OF BIRTH -->

            <div>
              <label
                class="mb-2 block text-sm font-semibold text-slate-700"
              >
                Date of Birth
              </label>

              <input
                v-model="form.dateOfBirth"
                type="date"
                :disabled="!editing"
                class="w-full rounded-lg border px-4 py-2.5 text-sm outline-none transition"
                :class="
                  editing
                    ? 'border-slate-300 bg-white text-slate-900 focus:border-blue-500 focus:ring-2 focus:ring-blue-100'
                    : 'border-slate-200 bg-slate-50 text-slate-500'
                "
              />
            </div>

            <!-- ADDRESS -->

            <div>
              <label
                class="mb-2 block text-sm font-semibold text-slate-700"
              >
                Address
              </label>

              <input
                v-model="form.address"
                type="text"
                :disabled="!editing"
                placeholder="Enter address"
                class="w-full rounded-lg border px-4 py-2.5 text-sm outline-none transition"
                :class="
                  editing
                    ? 'border-slate-300 bg-white text-slate-900 focus:border-blue-500 focus:ring-2 focus:ring-blue-100'
                    : 'border-slate-200 bg-slate-50 text-slate-500'
                "
              />
            </div>
          </div>
        </div>

        <!-- PHOTO -->

        <!-- <div
          v-if="editing"
          class="rounded-xl border border-slate-200 bg-slate-50 p-4"
        >
          <div
            class="flex flex-col gap-4 sm:flex-row sm:items-center"
          >
            <div
              class="flex h-20 w-20 shrink-0 items-center justify-center overflow-hidden rounded-full bg-blue-100 font-bold text-blue-700"
            >
              <img
                v-if="imagePreview"
                :src="imagePreview"
                alt="Preview"
                class="h-full w-full object-cover"
              />

              <span v-else>
                {{ initials }}
              </span>
            </div>

            <div class="min-w-0">
              <p
                class="text-sm font-semibold text-slate-800"
              >
                Profile photo
              </p>

              <p
                class="mt-1 text-xs text-slate-500"
              >
                Choose an image for your
                profile.
              </p>

              <label
                class="mt-3 inline-flex cursor-pointer items-center rounded-lg border border-slate-300 bg-white px-3 py-2 text-sm font-medium text-slate-700 transition hover:bg-slate-100"
              >
                Choose Image

                <input
                  type="file"
                  accept="image/*"
                  class="hidden"
                  @change="
                    handleImageChange
                  "
                />
              </label>
            </div>
          </div>
        </div> -->

        <!-- SAVE BUTTONS -->

        <div
          v-if="editing"
          class="flex flex-col-reverse gap-3 border-t border-slate-200 pt-5 sm:flex-row sm:justify-end"
        >
          <button
            type="button"
            :disabled="saving"
            class="w-full rounded-lg border border-slate-300 bg-white px-5 py-2.5 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto"
            @click="cancelEditing"
          >
            Cancel
          </button>

          <button
            type="button"
            :disabled="saving"
            class="flex w-full items-center justify-center gap-2 rounded-lg bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto"
            @click="save"
          >
            <span
              v-if="saving"
              class="h-4 w-4 animate-spin rounded-full border-2 border-white/40 border-t-white"
            ></span>

            {{
              saving
                ? "Saving..."
                : "Save Changes"
            }}
          </button>
        </div>
      </div>

      <!-- ===================================================
           TELEGRAM SECTION
      ==================================================== -->

      <div
        v-else-if="
          activeSection === 'telegram'
        "
        class="space-y-6"
      >
        <!-- TITLE -->

        <div>
          <h2
            class="text-base font-bold text-slate-900"
          >
            Telegram
          </h2>

          <p
            class="mt-1 text-sm text-slate-500"
          >
            Connect your Telegram account to
            your library account.
          </p>
        </div>

        <!-- =================================================
             STATUS LOADING
        ================================================== -->

        <div
          v-if="telegramStatusLoading"
          class="flex min-h-[220px] items-center justify-center rounded-xl border border-slate-200 bg-slate-50"
        >
          <div
            class="flex flex-col items-center gap-3"
          >
            <div
              class="h-7 w-7 animate-spin rounded-full border-4 border-slate-200 border-t-blue-600"
            ></div>

            <span
              class="text-sm text-slate-500"
            >
              Checking Telegram connection...
            </span>
          </div>
        </div>

        <!-- =================================================
             CONNECTED
        ================================================== -->

        <div
          v-else-if="
            telegramStatus.connected
          "
          class="overflow-hidden rounded-2xl border border-emerald-200 bg-white"
        >
          <!-- CONNECTED HEADER -->

          <div
            class="border-b border-emerald-100 bg-emerald-50 p-5 sm:p-6"
          >
            <div
              class="flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between"
            >
              <div
                class="flex items-center gap-4"
              >
                <!-- TELEGRAM ICON -->

                <div
                  class="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-blue-500 text-white"
                >
                  <svg
                    class="h-6 w-6"
                    viewBox="0 0 24 24"
                    fill="currentColor"
                  >
                    <path
                      d="M21.8 3.2 18.7 20c-.2 1.2-.9 1.5-1.8.9l-5-3.7-2.4 2.3c-.3.3-.5.5-1 .5l.4-5.1 9.3-8.4c.4-.4-.1-.6-.6-.2L6.1 13.6l-4.9-1.5c-1.1-.3-1.1-1.1.2-1.6L20.6 2.2c.9-.3 1.7.2 1.2 1z"
                    />
                  </svg>
                </div>

                <div>
                  <div
                    class="flex flex-wrap items-center gap-2"
                  >
                    <h3
                      class="font-bold text-slate-900"
                    >
                      Telegram Connected
                    </h3>

                    <span
                      class="rounded-full bg-emerald-100 px-2.5 py-1 text-xs font-semibold text-emerald-700"
                    >
                      Connected
                    </span>
                  </div>

                  <p
                    v-if="
                      telegramStatus.username
                    "
                    class="mt-1 text-sm text-slate-600"
                  >
                    @{{ telegramStatus.username }}
                  </p>
                </div>
              </div>

              <!-- DISCONNECT -->

              <button
                type="button"
                :disabled="telegramLoading"
                class="w-full rounded-lg border border-red-200 bg-white px-4 py-2.5 text-sm font-semibold text-red-600 transition hover:bg-red-50 disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto"
                @click="disconnectTelegram"
              >
                <span
                  v-if="telegramLoading"
                >
                  Disconnecting...
                </span>

                <span v-else>
                  Disconnect
                </span>
              </button>
            </div>
          </div>

          <!-- CONNECTED DETAILS -->

          <div
            class="grid grid-cols-1 gap-4 p-5 sm:grid-cols-2 sm:p-6"
          >
            <div
              class="rounded-xl bg-slate-50 p-4"
            >
              <p
                class="text-xs font-semibold uppercase tracking-wide text-slate-400"
              >
                Username
              </p>

              <p
                class="mt-1 break-all text-sm font-semibold text-slate-800"
              >
                {{
                  telegramStatus.username
                    ? `@${telegramStatus.username}`
                    : "Not available"
                }}
              </p>
            </div>

            <div
              class="rounded-xl bg-slate-50 p-4"
            >
              <p
                class="text-xs font-semibold uppercase tracking-wide text-slate-400"
              >
                Chat ID
              </p>

              <p
                class="mt-1 break-all text-sm font-semibold text-slate-800"
              >
                {{
                  telegramStatus.chatId ||
                  "Not available"
                }}
              </p>
            </div>

            <div
              class="rounded-xl bg-slate-50 p-4 sm:col-span-2"
            >
              <p
                class="text-xs font-semibold uppercase tracking-wide text-slate-400"
              >
                Connected At
              </p>

              <p
                class="mt-1 text-sm font-semibold text-slate-800"
              >
                {{
                  formattedConnectedDate ||
                  "Not available"
                }}
              </p>
            </div>
          </div>
        </div>

        <!-- =================================================
             NOT CONNECTED
        ================================================== -->

        <div
          v-else
          class="overflow-hidden rounded-2xl border border-slate-200 bg-white"
        >
          <div
            class="p-5 sm:p-6"
          >
            <div
              class="flex flex-col gap-5 sm:flex-row sm:items-center"
            >
              <!-- ICON -->

              <div
                class="flex h-14 w-14 shrink-0 items-center justify-center rounded-2xl bg-blue-50 text-blue-600"
              >
                <svg
                  class="h-7 w-7"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                >
                  <path
                    d="M21.8 3.2 18.7 20c-.2 1.2-.9 1.5-1.8.9l-5-3.7-2.4 2.3c-.3.3-.5.5-1 .5l.4-5.1 9.3-8.4c.4-.4-.1-.6-.6-.2L6.1 13.6l-4.9-1.5c-1.1-.3-1.1-1.1.2-1.6L20.6 2.2c.9-.3 1.7.2 1.2 1z"
                  />
                </svg>
              </div>

              <!-- TEXT -->

              <div
                class="min-w-0 flex-1"
              >
                <h3
                  class="text-base font-bold text-slate-900"
                >
                  Connect Telegram
                </h3>

                <p
                  class="mt-1 max-w-2xl text-sm leading-6 text-slate-500"
                >
                  Connect your Telegram account
                  to receive library notifications
                  and communicate with the library
                  bot.
                </p>
              </div>

              <!-- BUTTON -->

              <button
                type="button"
                :disabled="telegramLoading"
                class="inline-flex w-full shrink-0 items-center justify-center gap-2 rounded-lg bg-blue-600 px-5 py-2.5 text-sm font-semibold text-white transition hover:bg-blue-700 disabled:cursor-not-allowed disabled:opacity-50 sm:w-auto"
                @click="connectTelegram"
              >
                <span
                  v-if="telegramLoading"
                  class="h-4 w-4 animate-spin rounded-full border-2 border-white/40 border-t-white"
                ></span>

                {{
                  telegramLoading
                    ? "Generating..."
                    : "Connect Telegram"
                }}
              </button>
            </div>
          </div>

          <!-- =================================================
               CONNECTION INSTRUCTIONS
          ================================================== -->

          <div
            v-if="
              telegramConnection.code
            "
            class="border-t border-slate-200 bg-slate-50 p-5 sm:p-6"
          >
            <div
              class="mb-5"
            >
              <h3
                class="text-sm font-bold text-slate-900"
              >
                Finish Telegram Connection
              </h3>

              <p
                class="mt-1 text-sm leading-6 text-slate-500"
              >
                Open Telegram using the button
                below. Then start the bot using
                your connection link.
              </p>
            </div>

            <!-- CODE -->

            <div
              class="rounded-xl border border-slate-200 bg-white p-4"
            >
              <div
                class="flex flex-col gap-3 sm:flex-row sm:items-center sm:justify-between"
              >
                <div>
                  <p
                    class="text-xs font-semibold uppercase tracking-wide text-slate-400"
                  >
                    Connection Code
                  </p>

                  <p
                    class="mt-1 break-all font-mono text-lg font-bold tracking-wider text-slate-900"
                  >
                    {{
                      telegramConnection.code
                    }}
                  </p>
                </div>

                <button
                  type="button"
                  class="inline-flex w-full items-center justify-center gap-2 rounded-lg border border-slate-300 bg-white px-4 py-2.5 text-sm font-semibold text-slate-700 transition hover:bg-slate-50 sm:w-auto"
                  @click="
                    copyConnectionCode
                  "
                >
                  <svg
                    v-if="!copied"
                    class="h-4 w-4"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <rect
                      x="9"
                      y="9"
                      width="13"
                      height="13"
                      rx="2"
                    />
                    <path
                      d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1"
                    />
                  </svg>

                  <svg
                    v-else
                    class="h-4 w-4 text-emerald-600"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                  >
                    <path
                      d="m5 12 4 4L19 6"
                    />
                  </svg>

                  {{
                    copied
                      ? "Copied"
                      : "Copy Code"
                  }}
                </button>
              </div>
            </div>

            <!-- LINK -->

            <div
              class="mt-4 rounded-xl border border-blue-100 bg-blue-50 p-4"
            >
              <p
                class="text-xs font-semibold uppercase tracking-wide text-blue-500"
              >
                Telegram Bot
              </p>

              <p
                class="mt-1 break-all text-sm font-semibold text-blue-800"
              >
                {{
                  telegramConnection.botUsername
                    ? `@${telegramConnection.botUsername}`
                    : "Telegram Bot"
                }}
              </p>
            </div>

            <!-- ACTION -->

            <div
              class="mt-4 flex flex-col gap-3 sm:flex-row"
            >
              <button
                type="button"
                class="inline-flex w-full items-center justify-center gap-2 rounded-lg bg-blue-600 px-5 py-3 text-sm font-semibold text-white transition hover:bg-blue-700 sm:w-auto"
                @click="openTelegram"
              >
                <svg
                  class="h-5 w-5"
                  viewBox="0 0 24 24"
                  fill="currentColor"
                >
                  <path
                    d="M21.8 3.2 18.7 20c-.2 1.2-.9 1.5-1.8.9l-5-3.7-2.4 2.3c-.3.3-.5.5-1 .5l.4-5.1 9.3-8.4c.4-.4-.1-.6-.6-.2L6.1 13.6l-4.9-1.5c-1.1-.3-1.1-1.1.2-1.6L20.6 2.2c.9-.3 1.7.2 1.2 1z"
                  />
                </svg>

                Open Telegram
              </button>

              <div
                class="flex items-center text-xs leading-5 text-slate-500"
              >
                After opening Telegram, press
                <strong class="mx-1">
                  Start
                </strong>
                in the bot.
              </div>
            </div>

            <!-- MESSAGE -->

            <p
              v-if="
                telegramConnection.message
              "
              class="mt-4 text-xs leading-5 text-slate-500"
            >
              {{
                telegramConnection.message
              }}
            </p>

            <!-- POLLING -->

            <div
              class="mt-5 flex items-center gap-2 rounded-lg border border-amber-200 bg-amber-50 px-4 py-3 text-xs text-amber-700"
            >
              <span
                class="h-2 w-2 animate-pulse rounded-full bg-amber-500"
              ></span>

              Waiting for Telegram
              connection...
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>