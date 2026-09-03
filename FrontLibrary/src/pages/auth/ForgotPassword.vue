<template>
    <div
        class="min-h-screen bg-slate-100 flex items-center justify-center px-4 py-10"
    >
        <div class="w-full max-w-md">
        <div class="bg-white rounded-2xl shadow-xl p-8">

            <!-- TITLE -->
            <h2 class="text-2xl text-center font-bold text-slate-800 mb-6">
            Forgot Password
            </h2>

            <!-- DESCRIPTION -->
            <p class="text-center text-sm text-slate-500 mb-6">
            Enter your email address and we will send you a link to reset your
            password.
            </p>

            <!-- SUCCESS MESSAGE -->
            <div
            v-if="message"
            class="mb-5 bg-green-50 border border-green-200 text-green-600 rounded-xl p-3 text-sm"
            >
            {{ message }}
            </div>

            <!-- ERROR MESSAGE -->
            <div
            v-if="error"
            class="mb-5 bg-red-50 border border-red-200 text-red-600 rounded-xl p-3 text-sm"
            >
            {{ error }}
            </div>

            <!-- FORM -->
            <form @submit.prevent="sendResetLink" class="space-y-5">

            <!-- EMAIL -->
            <div>
                <label
                for="email"
                class="block text-sm font-medium text-slate-700 mb-2"
                >
                Email
                </label>

                <input
                id="email"
                v-model="email"
                type="email"
                placeholder="Enter your email"
                autocomplete="email"
                :disabled="loading"
                class="w-full px-4 py-3 border border-slate-300 rounded-xl
                        outline-none focus:ring-2 focus:ring-indigo-500
                        focus:border-indigo-500 transition
                        disabled:bg-slate-100"
                />
            </div>

            <!-- BUTTON -->
            <button
                type="submit"
                :disabled="loading"
                class="w-full py-3 bg-indigo-600 hover:bg-indigo-700
                    disabled:bg-indigo-300 disabled:cursor-not-allowed
                    text-white font-semibold rounded-xl transition
                    shadow-md hover:shadow-lg"
            >
                {{ loading ? "Sending..." : "Send Reset Link" }}
            </button>

            </form>

            <!-- BACK TO LOGIN -->
            <p class="text-center text-sm text-slate-500 mt-6">
            Remember your password?

            <router-link
                to="/login"
                class="text-indigo-600 font-semibold hover:underline"
            >
                Back to Login
            </router-link>
            </p>

        </div>
        </div>
    </div>
    </template>

    <script setup>
    import { ref } from "vue";

    const email = ref("");
    const loading = ref(false);
    const error = ref("");
    const message = ref("");

    const sendResetLink = async () => {
    // Clear old messages
    error.value = "";
    message.value = "";

    // Check email
    if (!email.value.trim()) {
        error.value = "Please enter your email.";
        return;
    }

    try {
        loading.value = true;

        const response = await fetch(
        "http://localhost:8080/api/auth/forgot-password",
        {
            method: "POST",
            headers: {
            "Content-Type": "application/json",
            },
            body: JSON.stringify({
            email: email.value.trim(),
            }),
        }
        );

        // Read response
        const data = await response.json();

        console.log("Forgot password response:", data);

        // Handle error response
        if (!response.ok) {
        const apiError = new Error(
            data.msg ||
            data.message ||
            "Failed to send reset link."
        );

        apiError.status = response.status;

        throw apiError;
        }

        // Success
        message.value =
        data.msg ||
        "Password reset link has been sent to your email.";

        // Clear email after success
        email.value = "";

    } catch (err) {
        console.error("Forgot password error:", err);

        if (err.status === 404) {
        error.value = "No account found with this email.";
        } else if (err.status === 400) {
        error.value =
            err.message || "Invalid email address.";
        } else if (err.status === 500) {
        error.value =
            "Server error. Please check your backend email configuration.";
        } else {
        error.value =
            err.message ||
            "Failed to send reset link. Please try again.";
        }

    } finally {
        loading.value = false;
    }
    };
</script>