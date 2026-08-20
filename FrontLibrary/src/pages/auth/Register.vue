<template>
  <div
    class="min-h-screen bg-slate-100 flex items-center justify-center px-4 py-10"
  >
    <div class="w-full max-w-md">

      <!-- Register Card -->
      <div class="bg-white rounded-2xl shadow-xl p-8">

        <h2 class="text-3xl text-center font-bold text-slate-800 mb-6">
          Register
        </h2>

        <form @submit.prevent="handleRegister" class="space-y-5">

          <!-- Full Name -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              Full Name
            </label>

            <input
              v-model="form.name"
              type="text"
              placeholder="Enter your full name"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl
                     outline-none focus:ring-2 focus:ring-indigo-500
                     focus:border-indigo-500 transition"
            />
          </div>

          <!-- Email -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              Email
            </label>

            <input
              v-model="form.email"
              type="email"
              placeholder="Enter your email"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl
                     outline-none focus:ring-2 focus:ring-indigo-500
                     focus:border-indigo-500 transition"
            />
          </div>

          <!-- Password -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              Password
            </label>

            <input
              v-model="form.password"
              type="password"
              placeholder="Create a password"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl
                     outline-none focus:ring-2 focus:ring-indigo-500
                     focus:border-indigo-500 transition"
            />
          </div>

          <!-- Confirm Password -->
          <div>
            <label class="block text-sm font-medium text-slate-700 mb-2">
              Confirm Password
            </label>

            <input
              v-model="form.confirmPassword"
              type="password"
              placeholder="Confirm your password"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl
                     outline-none focus:ring-2 focus:ring-indigo-500
                     focus:border-indigo-500 transition"
            />
          </div>

          <!-- Terms -->
          <div class="flex items-start gap-2">
            <input
              v-model="form.terms"
              type="checkbox"
              class="w-4 h-4 mt-1 accent-indigo-600"
            />

            <p class="text-sm text-slate-600">
              I agree to the

              <a
                href="#"
                class="text-indigo-600 hover:underline"
              >
                Terms & Conditions
              </a>
            </p>
          </div>

          <!-- Error -->
          <div
            v-if="error"
            class="bg-red-50 border border-red-200
                   text-red-600 text-sm rounded-xl p-3"
          >
            {{ error }}
          </div>

          <!-- Success -->
          <div
            v-if="success"
            class="bg-green-50 border border-green-200
                   text-green-600 text-sm rounded-xl p-3"
          >
            {{ success }}
          </div>

          <!-- Button -->
          <button
            type="submit"
            :disabled="loading"
            class="w-full py-3 bg-indigo-600 hover:bg-indigo-700
                   disabled:bg-indigo-300
                   text-white font-semibold rounded-xl transition
                   shadow-md hover:shadow-lg"
          >
            {{ loading ? "Creating Account..." : "Create Account" }}
          </button>

        </form>

        <!-- Login -->
        <p class="text-center text-sm text-slate-500 mt-6">
          Already have an account?

          <router-link
            to="/login"
            class="text-indigo-600 font-semibold hover:underline"
          >
            Sign In
          </router-link>
        </p>

      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";

import { Register } from "../../service/autherService";

const router = useRouter();

// =========================
// FORM
// =========================
const form = reactive({
  name: "",
  email: "",
  password: "",
  confirmPassword: "",
  terms: false,
});

// =========================
// STATE
// =========================
const loading = ref(false);
const error = ref("");
const success = ref("");

// =========================
// REGISTER
// =========================
const handleRegister = async () => {
  error.value = "";
  success.value = "";

  // Name
  if (!form.name.trim()) {
    error.value = "Please enter your full name.";
    return;
  }

  // Email
  if (!form.email.trim()) {
    error.value = "Please enter your email.";
    return;
  }

  // Password
  if (!form.password) {
    error.value = "Please enter your password.";
    return;
  }

  // Confirm Password
  if (form.password !== form.confirmPassword) {
    error.value = "Passwords do not match.";
    return;
  }

  // Terms
  if (!form.terms) {
    error.value = "Please accept Terms & Conditions.";
    return;
  }

  try {
    loading.value = true;

    // Call Backend
    const data = await Register(
      form.name,
      form.email,
      form.password
    );

    console.log("Register response:", data);

    // Save email for OTP page
    sessionStorage.setItem(
      "otpEmail",
      form.email
    );

    success.value =
      "Register successfully! Please check your email for OTP.";

    // Go OTP
    setTimeout(() => {
      router.push("/otp");
    }, 800);

  } catch (err) {
    console.error("Register error:", err);

    error.value =
      err?.message ||
      "Register failed. Please try again.";

  } finally {
    loading.value = false;
  }
};
</script>