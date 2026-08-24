<template>
  <div class="w-full">

    <!-- =====================================================
         MAIN PROFILE CONTAINER
    ====================================================== -->
    <div
      class="grid grid-cols-1 lg:grid-cols-[300px_1fr]
             bg-white rounded-2xl shadow-sm
             border border-slate-200 overflow-hidden"
    >

      <!-- =====================================================
           LEFT SIDEBAR
      ====================================================== -->
      <aside
        class="bg-gradient-to-b from-slate-50 to-white
               border-b lg:border-b-0 lg:border-r
               border-slate-200 p-5 lg:p-6
               flex flex-col"
      >

        <!-- ===================================================
             AVATAR
        ==================================================== -->
        <div class="flex flex-col items-center">

          <div
            class="w-24 h-24 rounded-full
                   bg-gradient-to-br from-indigo-100 to-purple-100
                   border-4 border-white
                   shadow-lg
                   flex items-center justify-center
                   overflow-hidden"
          >

            <!-- REAL AVATAR -->
            <img
              v-if="form.avatar && !avatarError"
              :src="form.avatar"
              :alt="form.fullName || defaultName"
              class="w-full h-full object-cover"
              @error="handleAvatarError"
            />

            <!-- INITIALS -->
            <span
              v-else
              class="text-2xl font-bold text-indigo-800"
            >
              {{ initials }}
            </span>

          </div>


          <!-- =================================================
               CHANGE PHOTO
          ================================================== -->
          <label
            class="mt-4 cursor-pointer
                   px-4 py-2 rounded-xl
                   bg-indigo-50
                   text-indigo-600
                   text-sm font-semibold
                   hover:bg-indigo-100
                   transition"
          >

            <i class="bi bi-camera-fill mr-2"></i>

            Change Photo

            <input
              ref="fileInput"
              type="file"
              accept="image/*"
              class="hidden"
              @change="handleImageChange"
            />

          </label>


          <!-- =================================================
               NAME
          ================================================== -->
          <h2
            class="mt-4 text-xl font-bold text-slate-800"
          >
            {{ form.fullName || defaultName }}
          </h2>


          <!-- =================================================
               ROLE
          ================================================== -->
          <p
            class="text-sm text-slate-400 mt-1"
          >
            {{ profileType }}
          </p>

        </div>


        <!-- =====================================================
             MENU
        ====================================================== -->
        <div class="mt-8 space-y-2">

          <!-- PERSONAL INFORMATION -->
          <div
            class="flex items-center gap-3
                   px-4 py-3 rounded-xl
                   bg-gradient-to-r
                   from-indigo-50 to-pink-50
                   text-indigo-600
                   font-semibold"
          >

            <div
              class="w-9 h-9 rounded-lg
                     bg-indigo-100
                     flex items-center justify-center"
            >
              <i class="bi bi-person-fill"></i>
            </div>

            <span>
              Personal Information
            </span>

          </div>


          <!-- LOGIN & PASSWORD -->
          <div
            class="flex items-center gap-3
                   px-4 py-3 rounded-xl
                   text-slate-500
                   hover:bg-slate-50
                   transition cursor-pointer"
            @click="handlePasswordClick"
          >

            <div
              class="w-9 h-9 rounded-lg
                     bg-indigo-50
                     flex items-center justify-center"
            >
              <i class="bi bi-lock-fill"></i>
            </div>

            <span>
              Login &amp; Password
            </span>

          </div>


          <!-- LOG OUT -->
          <div
            class="flex items-center gap-3
                   px-4 py-3 rounded-xl
                   text-slate-500
                   hover:bg-slate-50
                   transition cursor-pointer"
            @click="handleLogout"
          >

            <div
              class="w-9 h-9 rounded-lg
                     bg-indigo-50
                     flex items-center justify-center"
            >
              <i class="bi bi-box-arrow-right"></i>
            </div>

            <span>
              Log Out
            </span>

          </div>

        </div>


        <!-- =====================================================
             SECURITY CARD
        ====================================================== -->
        <div class="mt-auto pt-8">

          <div
            class="rounded-2xl
                   border border-pink-100
                   bg-gradient-to-br
                   from-white to-pink-50
                   p-5"
          >

            <div
              class="w-10 h-10 rounded-xl
                     bg-indigo-100
                     flex items-center justify-center
                     text-indigo-600 mb-4"
            >
              <i class="bi bi-shield-lock-fill"></i>
            </div>


            <h3
              class="font-semibold text-slate-700"
            >
              {{ securityTitle }}
            </h3>


            <p
              class="text-sm text-slate-400
                     leading-6 mt-2"
            >
              {{ securityDescription }}
            </p>

          </div>

        </div>

      </aside>


      <!-- =====================================================
           RIGHT CONTENT
      ====================================================== -->
      <section class="p-6 md:p-8 lg:p-10">

        <!-- ===================================================
             HEADER
        ==================================================== -->
        <div class="mb-8">

          <h1
            class="text-2xl md:text-3xl
                   font-bold text-slate-800"
          >
            {{ profileTitle }}
          </h1>


          <p
            class="text-slate-400 mt-1"
          >
            {{ profileDescription }}
          </p>

        </div>


        <!-- ===================================================
             GENDER
        ==================================================== -->
        <div class="mb-7">

          <label
            class="block text-sm font-medium
                   text-slate-600 mb-3"
          >
            Gender
          </label>


          <div class="flex items-center gap-7">

            <!-- MALE -->
            <label
              class="flex items-center gap-2
                     cursor-pointer text-slate-600"
            >

              <input
                v-model="form.gender"
                type="radio"
                value="Male"
                class="w-5 h-5 accent-indigo-500"
              />

              <span>
                Male
              </span>

            </label>


            <!-- FEMALE -->
            <label
              class="flex items-center gap-2
                     cursor-pointer text-slate-600"
            >

              <input
                v-model="form.gender"
                type="radio"
                value="Female"
                class="w-5 h-5 accent-indigo-500"
              />

              <span>
                Female
              </span>

            </label>

          </div>

        </div>


        <!-- ===================================================
             FULL NAME + PHONE
        ==================================================== -->
        <div
          class="grid grid-cols-1 md:grid-cols-2
                 gap-5 mb-6"
        >

          <!-- FULL NAME -->
          <div>

            <label
              class="block text-sm font-medium
                     text-slate-600 mb-2"
            >
              Full Name
            </label>


            <div class="relative">

              <div
                class="absolute left-3 top-1/2
                       -translate-y-1/2
                       w-9 h-9 rounded-lg
                       bg-indigo-50
                       flex items-center justify-center
                       text-indigo-600"
              >
                <i class="bi bi-person-fill"></i>
              </div>


              <input
                v-model="form.fullName"
                type="text"
                class="w-full pl-14 pr-4 py-3
                       border border-indigo-100
                       rounded-xl
                       text-slate-700
                       outline-none
                       focus:ring-2 focus:ring-indigo-200
                       focus:border-indigo-300"
              />

            </div>

          </div>


          <!-- PHONE -->
          <div>

            <label
              class="block text-sm font-medium
                     text-slate-600 mb-2"
            >
              Phone Number
            </label>


            <div class="relative">

              <div
                class="absolute left-3 top-1/2
                       -translate-y-1/2
                       w-9 h-9 rounded-lg
                       bg-pink-50
                       flex items-center justify-center
                       text-pink-400"
              >
                <i class="bi bi-telephone-fill"></i>
              </div>


              <input
                v-model="form.phone"
                type="tel"
                placeholder="+855 12 345 678"
                class="w-full pl-14 pr-4 py-3
                       border border-pink-100
                       rounded-xl
                       text-slate-700
                       placeholder:text-slate-400
                       outline-none
                       focus:ring-2 focus:ring-pink-100
                       focus:border-pink-200"
              />

            </div>

          </div>

        </div>


        <!-- ===================================================
             EMAIL
        ==================================================== -->
        <div class="mb-6">

          <label
            class="block text-sm font-medium
                   text-slate-600 mb-2"
          >
            Email
          </label>


          <div class="relative">

            <div
              class="absolute left-3 top-1/2
                     -translate-y-1/2
                     w-9 h-9 rounded-lg
                     bg-indigo-50
                     flex items-center justify-center
                     text-indigo-500"
            >
              <i class="bi bi-envelope-fill"></i>
            </div>


            <input
              v-model="form.email"
              type="email"
              class="w-full pl-14 pr-28 py-3
                     border border-indigo-100
                     rounded-xl
                     text-slate-700
                     bg-white
                     outline-none
                     focus:ring-2 focus:ring-indigo-200"
            />


            <!-- VERIFIED -->
            <span
              v-if="showVerified"
              class="absolute right-3 top-1/2
                     -translate-y-1/2
                     px-4 py-1.5
                     rounded-full
                     bg-emerald-50
                     text-emerald-500
                     text-xs font-semibold"
            >
              Verified
            </span>

          </div>

        </div>


        <!-- ===================================================
             ADDRESS
        ==================================================== -->
        <div class="mb-6">

          <label
            class="block text-sm font-medium
                   text-slate-600 mb-2"
          >
            Address
          </label>


          <div class="relative">

            <div
              class="absolute left-3 top-1/2
                     -translate-y-1/2
                     w-9 h-9 rounded-lg
                     bg-pink-50
                     flex items-center justify-center
                     text-pink-400"
            >
              <i class="bi bi-house-fill"></i>
            </div>


            <input
              v-model="form.address"
              type="text"
              placeholder="Enter your address"
              class="w-full pl-14 pr-4 py-3
                     border border-pink-100
                     rounded-xl
                     text-slate-700
                     placeholder:text-slate-400
                     outline-none
                     focus:ring-2 focus:ring-pink-100
                     focus:border-pink-200"
            />

          </div>

        </div>


        <!-- ===================================================
             DATE OF BIRTH
        ==================================================== -->
        <div class="mb-6">

          <label
            class="block text-sm font-medium
                   text-slate-600 mb-2"
          >
            Date of Birth
          </label>


          <div class="relative">

            <div
              class="absolute left-3 top-1/2
                     -translate-y-1/2
                     w-9 h-9 rounded-lg
                     bg-pink-50
                     flex items-center justify-center
                     text-indigo-400
                     pointer-events-none"
            >
              <i class="bi bi-calendar3"></i>
            </div>


            <input
              v-model="form.dateOfBirth"
              type="date"
              class="w-full pl-14 pr-4 py-3
                     border border-pink-100
                     rounded-xl
                     text-slate-700
                     outline-none
                     focus:ring-2 focus:ring-pink-100"
            />

          </div>

        </div>


        <!-- ===================================================
             BUTTONS
        ==================================================== -->
        <div
          class="flex flex-col-reverse sm:flex-row
                 justify-end gap-3
                 mt-10 pt-6
                 border-t border-slate-100"
        >

          <!-- DISCARD -->
          <button
            type="button"
            @click="discardChanges"
            :disabled="saving"
            class="px-7 py-3
                   border border-indigo-200
                   text-indigo-500
                   font-semibold
                   rounded-xl
                   hover:bg-indigo-50
                   transition
                   disabled:opacity-50
                   disabled:cursor-not-allowed"
          >
            Discard Changes
          </button>


          <!-- SAVE -->
          <button
            type="button"
            @click="saveChanges"
            :disabled="saving"
            class="px-8 py-3
                   bg-gradient-to-r
                   from-indigo-500 to-purple-500
                   text-white
                   font-semibold
                   rounded-xl
                   shadow-md
                   hover:shadow-lg
                   hover:opacity-95
                   transition
                   disabled:opacity-50
                   disabled:cursor-not-allowed"
          >

            <span v-if="saving">
              Saving...
            </span>

            <span v-else>
              Save Changes
            </span>

          </button>

        </div>

      </section>

    </div>

  </div>
</template>


<script setup>
import {
  reactive,
  computed,
  ref,
  watch,
} from "vue";


// =====================================================
// PROPS
// =====================================================

const props = defineProps({

  /*
   * Profile data from backend
   */
  profile: {
    type: Object,
    required: true,
  },


  /*
   * Loading state
   */
  saving: {
    type: Boolean,
    default: false,
  },


  /*
   * USER or ADMIN
   *
   * Example:
   *
   * role="USER"
   *
   * role="ADMIN"
   */
  role: {
    type: String,
    default: "USER",
  },

});


// =====================================================
// EMITS
// =====================================================

const emit = defineEmits([
  "save",
  "password",
  "logout",
]);


// =====================================================
// ROLE
// =====================================================

const isAdmin = computed(() => {

  return (
    props.role?.toUpperCase() === "ADMIN"
  );

});


// =====================================================
// DYNAMIC TEXT
// =====================================================

const profileTitle = computed(() => {

  return isAdmin.value
    ? "Admin Profile"
    : "User Profile";

});


const profileDescription = computed(() => {

  return isAdmin.value
    ? "Manage your administrator account information"
    : "Manage your personal account information";

});


const profileType = computed(() => {

  return isAdmin.value
    ? "Administrator"
    : "Library User";

});


const defaultName = computed(() => {

  return isAdmin.value
    ? "Admin"
    : "User";

});


const securityTitle = computed(() => {

  return isAdmin.value
    ? "Keep your admin account secure"
    : "Keep your account secure";

});


const securityDescription = computed(() => {

  return isAdmin.value
    ? "Make sure your administrator information and password are always up to date."
    : "Make sure your personal information and password are always up to date.";

});


/*
 * Email verified
 *
 * Backend may send:
 *
 * emailVerified: true
 *
 * If it doesn't exist, we show Verified
 * for compatibility with your current UI.
 */
const showVerified = computed(() => {

  if (
    props.profile.emailVerified === false
  ) {
    return false;
  }

  return true;

});


// =====================================================
// AVATAR
// =====================================================

const avatarError = ref(false);

const selectedImageFile = ref(null);

const fileInput = ref(null);


// =====================================================
// FORM
// =====================================================

const form = reactive({

  fullName:
    props.profile.fullName || "",

  email:
    props.profile.email || "",

  gender:
    props.profile.gender || "Male",

  phone:
    props.profile.phone || "",

  address:
    props.profile.address || "",

  dateOfBirth:
    props.profile.dateOfBirth || "",

  avatar:
    props.profile.avatar || "",

});


// =====================================================
// ORIGINAL PROFILE
// =====================================================

const originalProfile = ref(
  JSON.parse(
    JSON.stringify(form)
  )
);


// =====================================================
// WATCH BACKEND PROFILE
// =====================================================

watch(
  () => props.profile,

  (newProfile) => {

    if (!newProfile) {
      return;
    }

    console.log(
      "PROFILE CARD:",
      newProfile
    );

    console.log(
      "PROFILE CARD AVATAR:",
      newProfile.avatar
    );


    Object.assign(form, {

      fullName:
        newProfile.fullName || "",

      email:
        newProfile.email || "",

      gender:
        newProfile.gender || "Male",

      phone:
        newProfile.phone || "",

      address:
        newProfile.address || "",

      dateOfBirth:
        newProfile.dateOfBirth || "",

      avatar:
        newProfile.avatar || "",

    });


    /*
     * Save fresh original data
     */
    originalProfile.value =
      JSON.parse(
        JSON.stringify(form)
      );


    avatarError.value = false;

    selectedImageFile.value = null;

  },

  {
    deep: true,
    immediate: true,
  }

);


// =====================================================
// IMAGE CHANGE
// =====================================================

const handleImageChange = (event) => {

  const file =
    event.target.files?.[0];

  if (!file) {
    return;
  }


  /*
   * Save actual image file
   */
  selectedImageFile.value = file;


  /*
   * Preview
   */
  const previewUrl =
    URL.createObjectURL(file);

  form.avatar = previewUrl;

  avatarError.value = false;

};


// =====================================================
// SAVE
// =====================================================

const saveChanges = () => {

  emit("save", {

    ...form,

    imageFile:
      selectedImageFile.value,

  });

};


// =====================================================
// DISCARD
// =====================================================

const discardChanges = () => {

  Object.assign(
    form,

    JSON.parse(
      JSON.stringify(
        originalProfile.value
      )
    )

  );


  selectedImageFile.value = null;

  avatarError.value = false;


  /*
   * Reset file input
   */
  if (fileInput.value) {

    fileInput.value.value = "";

  }

};


// =====================================================
// AVATAR ERROR
// =====================================================

const handleAvatarError = () => {

  console.error(
    "Failed to load avatar:",
    form.avatar
  );

  avatarError.value = true;

};


// =====================================================
// INITIALS
// =====================================================

const initials = computed(() => {

  const name =
    form.fullName?.trim();


  if (!name) {

    return isAdmin.value
      ? "AD"
      : "US";

  }


  const names =
    name.split(/\s+/);


  /*
   * One word
   */
  if (names.length === 1) {

    return names[0]
      .substring(0, 2)
      .toUpperCase();

  }


  /*
   * First + Last
   */
  return (
    names[0].charAt(0) +
    names[names.length - 1].charAt(0)
  ).toUpperCase();

});


// =====================================================
// PASSWORD
// =====================================================

const handlePasswordClick = () => {

  emit("password");

};


// =====================================================
// LOGOUT
// =====================================================

const handleLogout = () => {

  emit("logout");

};

</script>