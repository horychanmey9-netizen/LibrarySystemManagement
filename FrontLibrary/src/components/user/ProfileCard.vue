<script setup>
import { ref } from 'vue'

const props = defineProps({
  profile: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['save'])

const form = ref({
  fullName: props.profile.fullName || '',
  gender: props.profile.gender || 'Male',
  email: props.profile.email || '',
  address: props.profile.address || '',
  phone: props.profile.phone || '',
  dateOfBirth: props.profile.dateOfBirth || '',
  location: props.profile.location || '',
  postalCode: props.profile.postalCode || '',
  avatar: props.profile.avatar || 'https://i.pravatar.cc/150'
})

const activeMenu = ref('personal')

const saveChanges = () => {
  emit('save', { ...form.value })
}
</script>

<template>
  <div
    class="min-h-screen bg-gradient-to-br from-[#f5f9ff] via-[#faf7ff] to-[#fff6fa] p-4 sm:p-6"
  >
    <!-- ================= MAIN CONTAINER ================= -->
    <div
      class="mx-auto flex max-w-6xl flex-col gap-5 rounded-3xl border border-blue-100/70 bg-white/90 p-4 shadow-[0_10px_40px_rgba(100,120,180,0.08)] backdrop-blur md:flex-row md:p-6"
    >

      <!-- ================= LEFT SIDEBAR ================= -->
      <aside
        class="relative w-full overflow-hidden rounded-2xl border border-blue-100 bg-gradient-to-b from-[#f8fbff] via-white to-[#fff8fc] p-5 md:w-[270px]"
      >
        <!-- Decorative circles -->
        <div
          class="absolute -right-10 -top-10 h-28 w-28 rounded-full bg-blue-100/50 blur-2xl"
        ></div>

        <div
          class="absolute -bottom-10 -left-10 h-28 w-28 rounded-full bg-pink-100/60 blur-2xl"
        ></div>

        <!-- ================= AVATAR ================= -->
        <div class="relative z-10 flex flex-col items-center">

          <div class="relative">
            <img
              :src="form.avatar"
              alt="Profile"
              class="h-24 w-24 rounded-full border-4 border-white object-cover shadow-[0_5px_20px_rgba(80,120,200,0.15)]"
            />
          </div>

          <h2 class="mt-3 text-base font-bold text-[#17365f]">
            {{ form.fullName }}
          </h2>

          <p class="text-xs text-[#7b8da8]">
            User
          </p>
        </div>

        <!-- ================= MENU ================= -->
        <nav class="relative z-10 mt-8 space-y-2">

          <!-- Personal -->
          <button
            type="button"
            @click="activeMenu = 'personal'"
            class="group flex w-full items-center gap-3 rounded-xl px-4 py-3 text-left text-sm transition-all duration-200"
            :class="
              activeMenu === 'personal'
                ? 'bg-gradient-to-r from-blue-50 to-pink-50 font-semibold text-[#3478db] shadow-sm'
                : 'text-[#7184a3] hover:bg-blue-50/60 hover:text-[#3478db]'
            "
          >
            <span
              class="flex h-8 w-8 items-center justify-center rounded-lg"
              :class="
                activeMenu === 'personal'
                  ? 'bg-blue-100 text-blue-500'
                  : 'bg-transparent'
              "
            >
              <i class="bi bi-person"></i>
            </span>

            <span>Personal Information</span>
          </button>

          <!-- Login -->
          <button
            type="button"
            @click="activeMenu = 'password'"
            class="group flex w-full items-center gap-3 rounded-xl px-4 py-3 text-left text-sm transition-all duration-200"
            :class="
              activeMenu === 'password'
                ? 'bg-gradient-to-r from-blue-50 to-pink-50 font-semibold text-[#3478db] shadow-sm'
                : 'text-[#7184a3] hover:bg-blue-50/60 hover:text-[#3478db]'
            "
          >
            <span
              class="flex h-8 w-8 items-center justify-center rounded-lg"
              :class="
                activeMenu === 'password'
                  ? 'bg-pink-100 text-pink-500'
                  : 'bg-transparent'
              "
            >
              <i class="bi bi-lock"></i>
            </span>

            <span>Login & Password</span>
          </button>

          <!-- Logout -->
          <button
            type="button"
            class="group flex w-full items-center gap-3 rounded-xl px-4 py-3 text-left text-sm text-[#7184a3] transition hover:bg-pink-50 hover:text-pink-500"
          >
            <span class="flex h-8 w-8 items-center justify-center">
              <i class="bi bi-box-arrow-right"></i>
            </span>

            <span>Log Out</span>
          </button>
        </nav>

        <!-- ================= SECURITY CARD ================= -->
        <div
          class="relative z-10 mt-12 hidden rounded-2xl border border-pink-100 bg-gradient-to-br from-blue-50/80 via-white to-pink-50/80 p-4 md:block"
        >
          <div
            class="mb-3 flex h-9 w-9 items-center justify-center rounded-xl bg-gradient-to-br from-blue-100 to-pink-100 text-blue-500"
          >
            <i class="bi bi-shield-check"></i>
          </div>

          <h3 class="text-xs font-bold text-[#29466d]">
            Keep your account secure
          </h3>

          <p class="mt-1 text-[11px] leading-5 text-[#8495ad]">
            Make sure to keep your information up to date.
          </p>
        </div>
      </aside>

      <!-- ================= RIGHT CONTENT ================= -->
      <main
        class="flex-1 rounded-2xl border border-blue-100/70 bg-white p-3 shadow-sm sm:p-6"
      >

        <!-- ================= PERSONAL INFORMATION ================= -->
        <div v-if="activeMenu === 'personal'">

          <!-- Header -->
          <div>
            <h1 class="text-2xl font-bold text-[#17365f]">
              Personal Information
            </h1>

            <p class="mt-1 text-sm text-[#8a9ab4]">
              Update your personal information
            </p>
          </div>

          <!-- ================= GENDER ================= -->
          <div class="mt-7">

            <label
              class="mb-3 block text-xs font-semibold text-[#5d7192]"
            >
              Gender
            </label>

            <div class="flex gap-7">

              <!-- Male -->
              <label
                class="flex cursor-pointer items-center gap-2 text-sm text-[#405574]"
              >
                <input
                  v-model="form.gender"
                  type="radio"
                  value="Male"
                  class="h-4 w-4 accent-blue-500"
                />

                Male
              </label>

              <!-- Female -->
              <label
                class="flex cursor-pointer items-center gap-2 text-sm text-[#405574]"
              >
                <input
                  v-model="form.gender"
                  type="radio"
                  value="Female"
                  class="h-4 w-4 accent-pink-400"
                />

                Female
              </label>

            </div>
          </div>

          <!-- ================= FULL NAME + PHONE ================= -->
          <div class="mt-6 grid grid-cols-1 gap-5 sm:grid-cols-2">

            <!-- Full Name -->
            <div>
              <label
                class="mb-2 block text-xs font-semibold text-[#5d7192]"
              >
                Full Name
              </label>

              <div class="relative">
                <span
                  class="absolute left-3 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-blue-50 text-blue-400"
                >
                  <i class="bi bi-person"></i>
                </span>

                <input
                  v-model="form.fullName"
                  type="text"
                  class="w-full rounded-xl border border-blue-100 bg-gradient-to-r from-blue-50/50 to-pink-50/30 py-3 pl-14 pr-4 text-sm text-[#263b58] outline-none transition focus:border-blue-300 focus:bg-white focus:ring-4 focus:ring-blue-50"
                />
              </div>
            </div>

            <!-- Phone -->
            <div>
              <label
                class="mb-2 block text-xs font-semibold text-[#5d7192]"
              >
                Phone Number
              </label>

              <div class="relative">
                <span
                  class="absolute left-3 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-pink-50 text-pink-400"
                >
                  <i class="bi bi-telephone"></i>
                </span>

                <input
                  v-model="form.phone"
                  type="text"
                  class="w-full rounded-xl border border-pink-100 bg-gradient-to-r from-pink-50/40 to-blue-50/30 py-3 pl-14 pr-4 text-sm text-[#263b58] outline-none transition focus:border-pink-300 focus:bg-white focus:ring-4 focus:ring-pink-50"
                />
              </div>
            </div>

          </div>

          <!-- ================= EMAIL ================= -->
          <div class="mt-6">

            <label
              class="mb-2 block text-xs font-semibold text-[#5d7192]"
            >
              Email
            </label>

            <div class="relative">

              <span
                class="absolute left-3 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-blue-50 text-blue-400"
              >
                <i class="bi bi-envelope"></i>
              </span>

              <input
                v-model="form.email"
                type="email"
                class="w-full rounded-xl border border-blue-100 bg-gradient-to-r from-blue-50/40 to-pink-50/30 py-3 pl-14 pr-28 text-sm text-[#263b58] outline-none transition focus:border-blue-300 focus:bg-white focus:ring-4 focus:ring-blue-50"
              />

              <span
                class="absolute right-4 top-1/2 flex -translate-y-1/2 items-center gap-1 rounded-full bg-emerald-50 px-3 py-1 text-xs font-semibold text-emerald-500"
              >
                <i class="bi bi-check-circle-fill"></i>
                Verified
              </span>

            </div>
          </div>

          <!-- ================= ADDRESS ================= -->
          <div class="mt-6">

            <label
              class="mb-2 block text-xs font-semibold text-[#5d7192]"
            >
              Address
            </label>

            <div class="relative">

              <span
                class="absolute left-3 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-pink-50 text-pink-400"
              >
                <i class="bi bi-geo-alt"></i>
              </span>

              <input
                v-model="form.address"
                type="text"
                class="w-full rounded-xl border border-pink-100 bg-gradient-to-r from-pink-50/40 to-blue-50/30 py-3 pl-14 pr-4 text-sm text-[#263b58] outline-none transition focus:border-pink-300 focus:bg-white focus:ring-4 focus:ring-pink-50"
              />

            </div>
          </div>

          <!-- ================= PHONE + DATE ================= -->
          <div class="mt-6 grid grid-cols-1 gap-5 sm:grid-cols-2">

            <!-- Phone -->
            <div>

              <label
                class="mb-2 block text-xs font-semibold text-[#5d7192]"
              >
                Phone Number
              </label>

              <div class="relative">

                <span
                  class="absolute left-3 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-blue-50 text-blue-400"
                >
                  <i class="bi bi-telephone"></i>
                </span>

                <input
                  v-model="form.phone"
                  type="text"
                  class="w-full rounded-xl border border-blue-100 bg-gradient-to-r from-blue-50/40 to-pink-50/30 py-3 pl-14 pr-4 text-sm text-[#263b58] outline-none transition focus:border-blue-300 focus:bg-white focus:ring-4 focus:ring-blue-50"
                />

              </div>
            </div>

            <!-- Date -->
            <div>

              <label
                class="mb-2 block text-xs font-semibold text-[#5d7192]"
              >
                Date of Birth
              </label>

              <div class="relative">

                <span
                  class="absolute left-3 top-1/2 z-10 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-pink-50 text-pink-400"
                >
                  <i class="bi bi-calendar3"></i>
                </span>

                <input
                  v-model="form.dateOfBirth"
                  type="date"
                  class="w-full rounded-xl border border-pink-100 bg-gradient-to-r from-pink-50/40 to-blue-50/30 py-3 pl-14 pr-4 text-sm text-[#263b58] outline-none transition focus:border-pink-300 focus:bg-white focus:ring-4 focus:ring-pink-50"
                />

              </div>
            </div>

          </div>

          <!-- ================= LOCATION + POSTAL ================= -->
          <div class="mt-6 grid grid-cols-1 gap-5 sm:grid-cols-2">

            <!-- Location -->
            <div>

              <label
                class="mb-2 block text-xs font-semibold text-[#5d7192]"
              >
                Location
              </label>

              <div class="relative">

                <span
                  class="absolute left-3 top-1/2 z-10 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-blue-50 text-blue-400"
                >
                  <i class="bi bi-geo-alt"></i>
                </span>

                <select
                  v-model="form.location"
                  class="w-full appearance-none rounded-xl border border-blue-100 bg-gradient-to-r from-blue-50/40 to-pink-50/30 py-3 pl-14 pr-10 text-sm text-[#263b58] outline-none transition focus:border-blue-300 focus:bg-white focus:ring-4 focus:ring-blue-50"
                >
                  <option value="">Select location</option>

                  <option value="Phnom Penh, Cambodia">
                    Phnom Penh, Cambodia
                  </option>

                  <option value="Siem Reap, Cambodia">
                    Siem Reap, Cambodia
                  </option>

                  <option value="Battambang, Cambodia">
                    Battambang, Cambodia
                  </option>
                </select>

                <i
                  class="bi bi-chevron-down pointer-events-none absolute right-4 top-1/2 -translate-y-1/2 text-xs text-blue-400"
                ></i>

              </div>
            </div>

            <!-- Postal -->
            <div>

              <label
                class="mb-2 block text-xs font-semibold text-[#5d7192]"
              >
                Postal Code
              </label>

              <div class="relative">

                <span
                  class="absolute left-3 top-1/2 flex h-8 w-8 -translate-y-1/2 items-center justify-center rounded-lg bg-pink-50 text-pink-400"
                >
                  <i class="bi bi-credit-card-2-front"></i>
                </span>

                <input
                  v-model="form.postalCode"
                  type="text"
                  class="w-full rounded-xl border border-pink-100 bg-gradient-to-r from-pink-50/40 to-blue-50/30 py-3 pl-14 pr-4 text-sm text-[#263b58] outline-none transition focus:border-pink-300 focus:bg-white focus:ring-4 focus:ring-pink-50"
                />

              </div>
            </div>

          </div>

          <!-- ================= BUTTONS ================= -->
          <div
            class="mt-9 flex flex-col-reverse gap-3 sm:flex-row sm:justify-end"
          >

            <!-- Discard -->
            <button
              type="button"
              class="rounded-xl border border-blue-200 bg-white px-8 py-3 text-sm font-semibold text-blue-500 transition hover:border-pink-200 hover:bg-pink-50 hover:text-pink-500"
            >
              Discard Changes
            </button>

            <!-- Save -->
            <button
              type="button"
              @click="saveChanges"
              class="rounded-xl bg-gradient-to-r from-blue-500 via-blue-400 to-pink-400 px-8 py-3 text-sm font-semibold text-white shadow-[0_6px_20px_rgba(80,130,230,0.22)] transition hover:-translate-y-0.5 hover:shadow-[0_8px_24px_rgba(220,130,190,0.25)]"
            >
              <i class="bi bi-save2 mr-2"></i>
              Save Changes
            </button>

          </div>

        </div>

        <!-- ================= LOGIN & PASSWORD ================= -->
        <div v-else-if="activeMenu === 'password'">

          <h1 class="text-2xl font-bold text-[#17365f]">
            Login & Password
          </h1>

          <p class="mt-1 text-sm text-[#8a9ab4]">
            Manage your login information and password
          </p>

          <div
            class="mt-8 rounded-2xl border border-blue-100 bg-gradient-to-r from-blue-50/70 via-white to-pink-50/60 p-6"
          >
            <div
              class="flex h-12 w-12 items-center justify-center rounded-xl bg-gradient-to-br from-blue-100 to-pink-100 text-blue-500"
            >
              <i class="bi bi-shield-lock text-lg"></i>
            </div>

            <h3 class="mt-4 font-semibold text-[#29466d]">
              Password Settings
            </h3>

            <p class="mt-1 text-sm text-[#8192aa]">
              Password settings can be added here.
            </p>
          </div>

        </div>

      </main>
    </div>
  </div>
</template>