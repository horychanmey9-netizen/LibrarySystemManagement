<template>
  <div
    class="min-h-screen bg-slate-100 flex items-center justify-center px-4 py-10"
  >
    <div class="w-full max-w-md">

      <!-- LOGIN CARD -->
      <div class="bg-white rounded-2xl shadow-xl p-8">

        <!-- Title -->
        <h2 class="text-2xl text-center font-bold text-slate-800 mb-6">
          Login
        </h2>

        <!-- Error Message -->
        <div
          v-if="error"
          class="mb-5 bg-red-50 border border-red-200
                 text-red-600 rounded-xl p-3 text-sm"
        >
          {{ error }}
        </div>

        <!-- Login Form -->
        <form @submit.prevent="login" class="space-y-5">

          <!-- EMAIL -->
          <div>
            <label
              class="block text-sm font-medium text-slate-700 mb-2"
            >
              Email
            </label>

            <input
              v-model="form.email"
              type="email"
              placeholder="Enter your email"
              autocomplete="email"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl
                     outline-none focus:ring-2 focus:ring-indigo-500
                     focus:border-indigo-500 transition"
            />
          </div>

          <!-- PASSWORD -->
          <div>
            <div class="flex justify-between mb-2">

              <label
                class="text-sm font-medium text-slate-700"
              >
                Password
              </label>

              <a
                href="#"
                class="text-sm text-indigo-600 hover:text-indigo-700"
              >
                Forgot password?
              </a>

            </div>

            <input
              v-model="form.password"
              type="password"
              placeholder="Enter your password"
              autocomplete="current-password"
              class="w-full px-4 py-3 border border-slate-300 rounded-xl
                     outline-none focus:ring-2 focus:ring-indigo-500
                     focus:border-indigo-500 transition"
            />
          </div>

          <!-- REMEMBER ME -->
          <div class="flex items-center gap-2">

            <input
              v-model="form.remember"
              type="checkbox"
              class="w-4 h-4 accent-indigo-600"
            />

            <label class="text-sm text-slate-600">
              Remember me
            </label>

          </div>

          <!-- LOGIN BUTTON -->
          <button
            type="submit"
            :disabled="loading"
            class="w-full py-3 bg-indigo-600 hover:bg-indigo-700
                   disabled:bg-indigo-300
                   text-white font-semibold rounded-xl transition
                   shadow-md hover:shadow-lg"
          >
            {{ loading ? "Signing In..." : "Sign In" }}
          </button>

        </form>

        <!-- REGISTER -->
        <p class="text-center text-sm text-slate-500 mt-6">

          Don't have an account?

          <router-link
            to="/register"
            class="text-indigo-600 font-semibold hover:underline"
          >
            Create account
          </router-link>

        </p>

      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from "vue";
import { useRouter } from "vue-router";

import { Login } from "../../service/autherService";

const router = useRouter();

// =========================
// FORM
// =========================
const form = reactive({
  email: "",
  password: "",
  remember: false,
});

// =========================
// STATE
// =========================
const loading = ref(false);
const error = ref("");

// =========================
// LOGIN
// =========================
const login = async () => {
  error.value = "";

  // =========================
  // VALIDATION
  // =========================
  if (!form.email) {
    error.value = "Please enter your email.";
    return;
  }

  if (!form.password) {
    error.value = "Please enter your password.";
    return;
  }

  try {
    loading.value = true;

    // =========================
    // CALL BACKEND
    // =========================
    const response = await Login(
      form.email,
      form.password
    );

    console.log("Login response:", response);

    // =========================
    // GET LOGIN DATA
    // =========================
    const token = response?.data?.token;
    const user = response?.data?.userResponse;

    console.log("Token:", token);
    console.log("User:", user);

    // =========================
    // CHECK DATA
    // =========================
    if (!token || !user) {
      throw new Error(
        "Invalid login response from server."
      );
    }

    // =========================
    // CLEAR OLD LOGIN DATA
    // =========================
    sessionStorage.removeItem("token");
    sessionStorage.removeItem("role");
    sessionStorage.removeItem("user");

    // =========================
    // SAVE LOGIN DATA
    // =========================
    sessionStorage.setItem(
      "token",
      token
    );

    sessionStorage.setItem(
      "role",
      user.role
    );

    sessionStorage.setItem(
      "user",
      JSON.stringify(user)
    );

    // =========================
    // CHECK STORAGE
    // =========================
    console.log(
      "Saved user:",
      JSON.parse(sessionStorage.getItem("user"))
    );

    // =========================
    // REDIRECT BY ROLE
    // =========================
    if (user.role === "ADMIN") {
      router.push("/admin/dashboard");
    } else {
      router.push("/user");
    }

  } catch (err) {
    console.error("Login error:", err);

    // =========================
    // ACCOUNT NOT FOUND
    // =========================
    if (err.status === 404) {
      error.value =
        "Account not found. Please register first.";

      setTimeout(() => {
        router.push("/register");
      }, 1200);

      return;
    }

    // =========================
    // ACCOUNT NOT ACTIVE
    // =========================
    if (err.status === 403) {
      error.value =
        "Your account is not verified. Please verify OTP.";

      return;
    }

    // =========================
    // WRONG EMAIL / PASSWORD
    // =========================
    if (err.status === 401) {
      error.value =
        "Incorrect email or password.";

      return;
    }

    // =========================
    // OTHER ERROR
    // =========================
    error.value =
      err.message ||
      "Login failed. Please try again.";

  } finally {
    loading.value = false;
  }
};
</script>