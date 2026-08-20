<template>
  <div class="w-full">

    <!-- ========================================
         MAIN PROFILE CONTAINER
    ========================================= -->
    <div
      class="grid grid-cols-1 lg:grid-cols-[300px_1fr]
             bg-white rounded-2xl shadow-sm
             border border-slate-200 overflow-hidden"
    >

      <!-- ========================================
           LEFT SIDEBAR
      ========================================= -->
      <aside
        class="bg-gradient-to-b from-slate-50 to-white
               border-b lg:border-b-0 lg:border-r
               border-slate-200 p-5 lg:p-6
               flex flex-col"
      >

        <!-- Avatar -->
        <div class="flex flex-col items-center">

          <div
            class="w-24 h-24 rounded-full
                   bg-gradient-to-br from-indigo-100 to-purple-100
                   border-4 border-white
                   shadow-lg
                   flex items-center justify-center
                   overflow-hidden"
          >

            <!-- =========================
                 AVATAR FROM BACKEND
            ========================== -->
            <img
              v-if="form.avatar"
              :src="form.avatar"
              :alt="form.fullName || 'User'"
              class="w-full h-full object-cover"
              @error="avatarError = true"
            />

            <!-- =========================
                 INITIAL IF NO AVATAR
            ========================== -->
            <span
              v-if="!form.avatar || avatarError"
              class="text-2xl font-bold text-indigo-800"
            >
              {{ initials }}
            </span>

          </div>

          <!-- User Name -->
          <h2
            class="mt-4 text-xl font-bold text-slate-800"
          >
            {{ form.fullName || "User" }}
          </h2>

          <!-- Role -->
          <p
            class="text-sm text-slate-400 mt-1"
          >
            Library User
          </p>

        </div>


        <!-- ========================================
             MENU
        ========================================= -->
        <div class="mt-8 space-y-2">

          <!-- Personal Information -->
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


          <!-- Login & Password -->
          <div
            class="flex items-center gap-3
                   px-4 py-3 rounded-xl
                   text-slate-500
                   hover:bg-slate-50
                   transition cursor-pointer"
          >

            <div
              class="w-9 h-9 rounded-lg
                     bg-indigo-50
                     flex items-center justify-center"
            >
              <i class="bi bi-lock-fill"></i>
            </div>

            <span>
              Login & Password
            </span>

          </div>


          <!-- Logout -->
          <div
            class="flex items-center gap-3
                   px-4 py-3 rounded-xl
                   text-slate-500
                   hover:bg-slate-50
                   transition cursor-pointer"
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


        <!-- ========================================
             SECURITY CARD
        ========================================= -->
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
              Keep your account secure
            </h3>

            <p
              class="text-sm text-slate-400
                     leading-6 mt-2"
            >
              Make sure your personal information
              and password are always up to date.
            </p>

          </div>

        </div>

      </aside>


      <!-- ========================================
           RIGHT CONTENT
      ========================================= -->
      <section class="p-6 md:p-8 lg:p-10">

        <!-- Header -->
        <div class="mb-8">

          <h1
            class="text-2xl md:text-3xl
                   font-bold text-slate-800"
          >
            User Profile
          </h1>

          <p class="text-slate-400 mt-1">
            Manage your personal account information
          </p>

        </div>


        <!-- ========================================
             GENDER
        ========================================= -->
        <div class="mb-7">

          <label
            class="block text-sm font-medium
                   text-slate-600 mb-3"
          >
            Gender
          </label>

          <div class="flex items-center gap-7">

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

              <span>Male</span>

            </label>


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

              <span>Female</span>

            </label>

          </div>

        </div>


        <!-- ========================================
             FULL NAME + PHONE
        ========================================= -->
        <div
          class="grid grid-cols-1 md:grid-cols-2
                 gap-5 mb-6"
        >

          <!-- Full Name -->
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


          <!-- Phone -->
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


        <!-- ========================================
             EMAIL
        ========================================= -->
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

            <span
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


        <!-- ========================================
             ADDRESS
        ========================================= -->
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


        <!-- ========================================
             DATE + POSTAL
        ========================================= -->
        <div
          class="grid grid-cols-1 md:grid-cols-2
                 gap-5"
        >

          <!-- Date of Birth -->
          <div>

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


          <!-- Postal Code -->
          <div>

            <label
              class="block text-sm font-medium
                     text-slate-600 mb-2"
            >
              Postal Code
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
                <i class="bi bi-hash"></i>
              </div>

              <input
                v-model="form.postalCode"
                type="text"
                placeholder="12000"
                class="w-full pl-14 pr-4 py-3
                       border border-pink-100
                       rounded-xl
                       text-slate-700
                       placeholder:text-slate-400
                       outline-none
                       focus:ring-2 focus:ring-pink-100"
              />

            </div>

          </div>

        </div>


        <!-- ========================================
             BUTTONS
        ========================================= -->
        <div
          class="flex flex-col-reverse sm:flex-row
                 justify-end gap-3
                 mt-10 pt-6
                 border-t border-slate-100"
        >

          <button
            type="button"
            @click="discardChanges"
            class="px-7 py-3
                   border border-indigo-200
                   text-indigo-500
                   font-semibold
                   rounded-xl
                   hover:bg-indigo-50
                   transition"
          >
            Discard Changes
          </button>


          <button
            type="button"
            @click="saveChanges"
            class="px-8 py-3
                   bg-gradient-to-r
                   from-indigo-500 to-purple-500
                   text-white
                   font-semibold
                   rounded-xl
                   shadow-md
                   hover:shadow-lg
                   hover:opacity-95
                   transition"
          >
            Save Changes
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
  ref
} from "vue";

// ========================================
// PROPS
// ========================================
const props = defineProps({
  profile: {
    type: Object,
    required: true,
  },
});

// ========================================
// EMIT
// ========================================
const emit = defineEmits(["save"]);

// ========================================
// AVATAR ERROR
// ========================================
const avatarError = ref(false);

// ========================================
// FORM
// ========================================
const form = reactive({
  fullName: props.profile.fullName || "",
  email: props.profile.email || "",

  // Profile information
  gender: props.profile.gender || "Male",
  phone: props.profile.phone || "",
  address: props.profile.address || "",
  dateOfBirth: props.profile.dateOfBirth || "",
  postalCode: props.profile.postalCode || "",

  // Avatar from Backend
  avatar: props.profile.avatar || "",
});

// ========================================
// ORIGINAL DATA
// ========================================
const originalProfile = JSON.parse(
  JSON.stringify(form)
);

// ========================================
// SAVE
// ========================================
const saveChanges = () => {

  emit("save", {
    ...form,
  });

};

// ========================================
// DISCARD
// ========================================
const discardChanges = () => {

  Object.assign(
    form,
    JSON.parse(
      JSON.stringify(originalProfile)
    )
  );

  avatarError.value = false;
};

// ========================================
// INITIALS
// ========================================
const initials = computed(() => {

  const name = form.fullName?.trim();

  if (!name) {
    return "US";
  }

  const names = name.split(/\s+/);

  if (names.length === 1) {
    return names[0]
      .substring(0, 2)
      .toUpperCase();
  }

  return (
    names[0].charAt(0) +
    names[names.length - 1].charAt(0)
  ).toUpperCase();
});
</script>