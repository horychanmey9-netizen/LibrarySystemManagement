<template>
  <div class="min-h-screen bg-slate-100 flex items-center justify-center px-4">
    <div class="w-full max-w-md">

      <!-- Logo / Header -->
      <div class="text-center mb-8">

        <div
          class="mx-auto w-16 h-16 rounded-2xl bg-indigo-600
                 flex items-center justify-center text-white text-3xl shadow-lg"
        >
          🔐
        </div>

        <h1 class="text-3xl font-bold text-slate-800 mt-4">
          Verify Your Email
        </h1>

        <p class="text-slate-500 mt-2">
          We sent a verification code to
        </p>

        <p class="font-semibold text-slate-700 mt-1 break-all">
          {{ email }}
        </p>

      </div>

      <!-- OTP Card -->
      <div class="bg-white rounded-2xl shadow-xl p-8">

        <h2 class="text-xl font-bold text-slate-800 text-center">
          Enter OTP Code
        </h2>

        <p class="text-sm text-slate-500 text-center mt-2">
          Enter the 6-digit code from your email.
        </p>

        <!-- OTP Inputs -->
        <div class="flex justify-center gap-2 sm:gap-3 mt-8">

          <input
            v-for="(digit, index) in otp"
            :key="index"
            :ref="(el) => setInputRef(el, index)"
            v-model="otp[index]"
            type="text"
            inputmode="numeric"
            autocomplete="one-time-code"
            maxlength="1"
            class="w-11 h-14 sm:w-12 sm:h-14 text-center text-xl
                   font-bold border border-slate-300 rounded-xl
                   outline-none focus:ring-2 focus:ring-indigo-500
                   focus:border-indigo-500"
            @input="handleInput(index)"
            @keydown.backspace="handleBackspace(index)"
          />

        </div>

        <!-- Error -->
        <p
          v-if="error"
          class="text-red-500 text-sm text-center mt-4"
        >
          {{ error }}
        </p>

        <!-- Verify Button -->
        <button
          @click="verifyOtp"
          :disabled="loading || otp.join('').length !== 6"
          class="w-full mt-8 py-3 bg-indigo-600
                 hover:bg-indigo-700 disabled:bg-indigo-300
                 disabled:cursor-not-allowed
                 text-white font-semibold rounded-xl transition"
        >
          {{ loading ? "Verifying..." : "Verify OTP" }}
        </button>

        <!-- Resend -->
        <div class="text-center mt-6">

          <p class="text-sm text-slate-500">
            Didn't receive the code?
          </p>

          <button
            @click="resendOtp"
            :disabled="countdown > 0 || resendLoading"
            class="mt-2 text-indigo-600 font-semibold
                   hover:text-indigo-700
                   disabled:text-slate-400
                   disabled:cursor-not-allowed"
          >
            {{
              resendLoading
                ? "Sending..."
                : countdown > 0
                  ? `Resend OTP in ${countdown}s`
                  : "Resend OTP"
            }}
          </button>

        </div>

        <!-- Back -->
        <div class="text-center mt-5">

          <router-link
            to="/register"
            class="text-sm text-slate-500 hover:text-indigo-600"
          >
            ← Back to Register
          </router-link>

        </div>

      </div>

    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from "vue";
import { useRouter } from "vue-router";

const router = useRouter();

/* =========================
   EMAIL
========================= */
const email = ref(
  sessionStorage.getItem("otpEmail") || ""
);

/* =========================
   OTP
========================= */
const otp = ref(["", "", "", "", "", ""]);

const inputRefs = ref([]);

/* =========================
   STATES
========================= */
const loading = ref(false);
const resendLoading = ref(false);
const error = ref("");

/* =========================
   COUNTDOWN
========================= */
const countdown = ref(60);
let timer = null;

/* =========================
   INPUT REF
========================= */
const setInputRef = (el, index) => {
  if (el) {
    inputRefs.value[index] = el;
  }
};

/* =========================
   HANDLE INPUT
========================= */
const handleInput = (index) => {
  otp.value[index] = otp.value[index]
    .replace(/\D/g, "")
    .slice(0, 1);

  error.value = "";

  // Move to next input
  if (
    otp.value[index] &&
    index < otp.value.length - 1
  ) {
    inputRefs.value[index + 1]?.focus();
  }
};

/* =========================
   HANDLE BACKSPACE
========================= */
const handleBackspace = (index) => {
  if (!otp.value[index] && index > 0) {
    inputRefs.value[index - 1]?.focus();
  }
};

/* =========================
   VERIFY OTP
========================= */
const verifyOtp = async () => {
  const code = otp.value.join("");

  // Check email
  if (!email.value) {
    error.value = "Email is missing. Please register again.";
    return;
  }

  // Check OTP
  if (code.length !== 6) {
    error.value = "Please enter the 6-digit OTP.";
    return;
  }

  loading.value = true;
  error.value = "";

  try {
    const response = await fetch(
      "http://localhost:8080/api/auth/verify-otp",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email.value,
          otp: code,
        }),
      }
    );

    if (!response.ok) {
      let message = "Invalid or expired OTP.";

      try {
        const data = await response.json();

        if (data.message) {
          message = data.message;
        }
      } catch {
        // Ignore JSON parsing error
      }

      throw new Error(message);
    }

    // Remove temporary email
    sessionStorage.removeItem("otpEmail");

    // Stop countdown
    if (timer) {
      clearInterval(timer);
      timer = null;
    }

    alert("Account verified successfully!");

    // Go to Login
    router.push("/login");

  } catch (err) {
    error.value =
      err?.message || "Something went wrong. Please try again.";
  } finally {
    loading.value = false;
  }
};

/* =========================
   RESEND OTP
========================= */
const resendOtp = async () => {
  if (countdown.value > 0 || resendLoading.value) {
    return;
  }

  if (!email.value) {
    error.value = "Email is missing. Please register again.";
    return;
  }

  resendLoading.value = true;
  error.value = "";

  try {
    const response = await fetch(
      "http://localhost:8080/api/auth/resend-otp",
      {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          email: email.value,
        }),
      }
    );

    if (!response.ok) {
      let message = "Failed to resend OTP.";

      try {
        const data = await response.json();

        if (data.message) {
          message = data.message;
        }
      } catch {
        // Ignore JSON parsing error
      }

      throw new Error(message);
    }

    // Clear OTP
    otp.value = ["", "", "", "", "", ""];

    // Start countdown
    startCountdown();

    // Focus first input
    setTimeout(() => {
      inputRefs.value[0]?.focus();
    }, 50);

  } catch (err) {
    error.value =
      err?.message || "Failed to resend OTP. Please try again.";
  } finally {
    resendLoading.value = false;
  }
};

/* =========================
   COUNTDOWN
========================= */
const startCountdown = () => {
  // Clear previous timer
  if (timer) {
    clearInterval(timer);
  }

  countdown.value = 60;

  timer = setInterval(() => {
    countdown.value--;

    if (countdown.value <= 0) {
      clearInterval(timer);
      timer = null;
    }
  }, 1000);
};

/* =========================
   ON MOUNTED
========================= */
onMounted(() => {
  if (!email.value) {
    error.value = "Email is missing. Please register again.";
  }

  startCountdown();

  setTimeout(() => {
    inputRefs.value[0]?.focus();
  }, 100);
});

/* =========================
   CLEANUP
========================= */
onBeforeUnmount(() => {
  if (timer) {
    clearInterval(timer);
    timer = null;
  }
});
</script>