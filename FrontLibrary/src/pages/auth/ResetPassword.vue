<template>
    <div
        class="min-h-screen bg-slate-100 flex items-center justify-center px-4 py-10"
    >
        <div class="w-full max-w-md">

        <!-- RESET PASSWORD CARD -->
        <div class="bg-white rounded-2xl shadow-xl p-8">

            <!-- TITLE -->
            <h2
            class="text-2xl text-center font-bold text-slate-800 mb-2"
            >
            Reset Password
            </h2>

            <p class="text-center text-slate-500 text-sm mb-6">
            Enter your new password below.
            </p>

            <!-- SUCCESS MESSAGE -->
            <div
            v-if="successMessage"
            class="mb-4 p-3 rounded-lg bg-green-100 border border-green-300 text-green-700 text-sm"
            >
            {{ successMessage }}
            </div>

            <!-- ERROR MESSAGE -->
            <div
            v-if="errorMessage"
            class="mb-4 p-3 rounded-lg bg-red-100 border border-red-300 text-red-700 text-sm"
            >
            {{ errorMessage }}
            </div>

            <!-- NO TOKEN -->
            <div v-if="!token">

            <div
                class="p-4 rounded-lg bg-red-50 border border-red-200 text-red-600 text-sm text-center"
            >
                Invalid or missing reset token.
            </div>

            <button
                type="button"
                @click="goToLogin"
                class="w-full mt-5 bg-slate-800 hover:bg-slate-900 text-white font-semibold py-3 rounded-lg transition duration-200"
            >
                Back to Login
            </button>

            </div>

            <!-- RESET PASSWORD FORM -->
            <form
            v-else
            @submit.prevent="resetPassword"
            >

            <!-- NEW PASSWORD -->
            <div class="mb-5">

                <label
                for="newPassword"
                class="block text-sm font-medium text-slate-700 mb-2"
                >
                New Password
                </label>

                <input
                id="newPassword"
                v-model="newPassword"
                type="password"
                placeholder="Enter new password"
                required
                minlength="6"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg
                        focus:outline-none focus:ring-2 focus:ring-slate-400
                        focus:border-slate-400 transition"
                />

            </div>

            <!-- CONFIRM PASSWORD -->
            <div class="mb-6">

                <label
                for="confirmPassword"
                class="block text-sm font-medium text-slate-700 mb-2"
                >
                Confirm Password
                </label>

                <input
                id="confirmPassword"
                v-model="confirmPassword"
                type="password"
                placeholder="Confirm new password"
                required
                minlength="6"
                class="w-full px-4 py-3 border border-slate-300 rounded-lg
                        focus:outline-none focus:ring-2 focus:ring-slate-400
                        focus:border-slate-400 transition"
                />

            </div>

            <!-- RESET BUTTON -->
            <button
                type="submit"
                :disabled="loading"
                class="w-full bg-slate-800 hover:bg-slate-900
                    disabled:bg-slate-400 disabled:cursor-not-allowed
                    text-white font-semibold py-3 rounded-lg
                    transition duration-200"
            >
                {{ loading ? "Changing Password..." : "Reset Password" }}
            </button>

            </form>

            <!-- LOGIN LINK -->
            <div class="text-center mt-6">

            <button
                type="button"
                @click="goToLogin"
                class="text-sm text-slate-600 hover:text-slate-900 font-medium"
            >
                ← Back to Login
            </button>

            </div>

        </div>

        </div>
    </div>
    </template>


    <script setup>
    import { ref } from "vue";
    import { useRoute, useRouter } from "vue-router";

    const route = useRoute();
    const router = useRouter();


    // =============================
    // GET TOKEN FROM URL
    // =============================

    const token = route.query.token;


    // =============================
    // FORM DATA
    // =============================

    const newPassword = ref("");
    const confirmPassword = ref("");


    // =============================
    // MESSAGE
    // =============================

    const successMessage = ref("");
    const errorMessage = ref("");


    // =============================
    // LOADING
    // =============================

    const loading = ref(false);


    // =============================
    // RESET PASSWORD
    // =============================

    const resetPassword = async () => {

    // Clear previous messages
    successMessage.value = "";
    errorMessage.value = "";

    // Check token
    if (!token) {
        errorMessage.value = "Invalid or missing reset token.";
        return;
    }

    // Check password
    if (newPassword.value !== confirmPassword.value) {
        errorMessage.value =
        "New password and confirm password do not match.";
        return;
    }

    // Check password length
    if (newPassword.value.length < 6) {
        errorMessage.value =
        "Password must be at least 6 characters.";
        return;
    }

    loading.value = true;

    try {

        const response = await fetch(
        "http://localhost:8080/api/auth/reset-password",
        {
            method: "POST",

            headers: {
            "Content-Type": "application/json"
            },

            body: JSON.stringify({
            token: token,
            newPassword: newPassword.value,
            confirmPassword: confirmPassword.value
            })
        }
        );


        const data = await response.json();


        // =============================
        // ERROR RESPONSE
        // =============================

        if (!response.ok) {

        errorMessage.value =
            data.message ||
            data.msg ||
            "Failed to reset password.";

        return;
        }


        // =============================
        // SUCCESS
        // =============================

        successMessage.value =
        data.message ||
        data.msg ||
        "Password has been changed successfully.";

        // Clear password fields
        newPassword.value = "";
        confirmPassword.value = "";


        // Go back to login after 2 seconds
        setTimeout(() => {
        router.push("/login");
        }, 2000);


    } catch (error) {

        console.error("Reset password error:", error);

        errorMessage.value =
        "Cannot connect to the server. Please try again.";

    } finally {

        loading.value = false;

    }
    };


    // =============================
    // GO TO LOGIN
    // =============================

    const goToLogin = () => {
    router.push("/login");
    };
</script>