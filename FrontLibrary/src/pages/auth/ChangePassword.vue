<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { ChangePassword } from "../../service/autherService";

const router = useRouter();

const currentPassword = ref("");
const newPassword = ref("");
const confirmPassword = ref("");

const loading = ref(false);
const errorMessage = ref("");
const successMessage = ref("");

async function handleChangePassword() {
    errorMessage.value = "";
    successMessage.value = "";

    if (!currentPassword.value) {
        errorMessage.value = "Please enter your current password.";
        return;
    }

    if (!newPassword.value) {
        errorMessage.value = "Please enter your new password.";
        return;
    }

    if (newPassword.value.length < 6) {
        errorMessage.value =
            "Password must be at least 6 characters.";
        return;
    }

    if (!confirmPassword.value) {
        errorMessage.value =
            "Please confirm your new password.";
        return;
    }

    if (newPassword.value !== confirmPassword.value) {
        errorMessage.value =
            "New password and confirm password do not match.";
        return;
    }

    if (currentPassword.value === newPassword.value) {
        errorMessage.value =
            "New password must be different from your current password.";
        return;
    }

    loading.value = true;

    try {
        const response = await ChangePassword(
            currentPassword.value,
            newPassword.value,
            confirmPassword.value
        );

        successMessage.value =
            response?.message ||
            "Password changed successfully.";

        currentPassword.value = "";
        newPassword.value = "";
        confirmPassword.value = "";

    } catch (error) {
        errorMessage.value =
            error?.message ||
            "Failed to change password.";

    } finally {
        loading.value = false;
    }
}


// =============================
// CLOSE MODAL
// =============================

function closeModal() {
    if (loading.value) {
        return;
    }

    currentPassword.value = "";
    newPassword.value = "";
    confirmPassword.value = "";
    errorMessage.value = "";
    successMessage.value = "";

    router.back();
}


// =============================
// FORGOT PASSWORD
// =============================

function goToForgotPassword() {
    router.push("/forgot-password");
}
</script>


<template>
    <!-- CHANGE PASSWORD MODAL -->
    <div
        class="fixed inset-0 z-50 flex items-center justify-center px-4"
    >


        <!-- CHANGE PASSWORD CARD -->
        <div
            class="relative z-10 w-full max-w-md"
        >

            <div
                class="bg-white rounded-2xl shadow-xl p-8"
            >

                <!-- HEADER -->
                <div class="relative mb-8">

                    <h2
                        class="text-2xl text-center font-bold text-slate-800"
                    >
                        Change Password
                    </h2>

                    <p
                        class="text-center text-sm text-slate-500 mt-2"
                    >
                        Update your password to keep your account secure.
                    </p>

                    <!-- CLOSE BUTTON -->
                    <button
                        type="button"
                        @click="closeModal"
                        :disabled="loading"
                        class="absolute right-0 top-0
                                text-slate-400 hover:text-slate-700
                                transition duration-200"
                        aria-label="Close"
                    >
                        <i class="bi bi-x-lg text-lg"></i>
                    </button>

                </div>


                <!-- SUCCESS MESSAGE -->
                <div
                    v-if="successMessage"
                    class="mb-4 p-3 rounded-lg
                            bg-green-100 border border-green-300
                            text-green-700 text-sm"
                >
                    {{ successMessage }}
                </div>


                <!-- ERROR MESSAGE -->
                <div
                    v-if="errorMessage"
                    class="mb-4 p-3 rounded-lg
                            bg-red-100 border border-red-300
                            text-red-700 text-sm"
                >
                    {{ errorMessage }}
                </div>


                <!-- FORM -->
                <form
                    @submit.prevent="handleChangePassword"
                >

                    <!-- CURRENT PASSWORD -->
                    <div class="mb-5">

                        <label
                            for="currentPassword"
                            class="block text-sm font-medium
                                   text-slate-700 mb-2"
                        >
                            Current Password
                        </label>

                        <input
                            id="currentPassword"
                            v-model="currentPassword"
                            type="password"
                            placeholder="Enter current password"
                            required
                            minlength="6"
                            autocomplete="current-password"
                            class="w-full px-4 py-3
                                   border border-slate-300
                                   rounded-lg
                                   focus:outline-none
                                   focus:ring-2
                                   focus:ring-slate-400
                                   focus:border-slate-400
                                   transition"
                        />

                    </div>


                    <!-- NEW PASSWORD -->
                    <div class="mb-5">

                        <label
                            for="newPassword"
                            class="block text-sm font-medium
                                   text-slate-700 mb-2"
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
                            autocomplete="new-password"
                            class="w-full px-4 py-3
                                   border border-slate-300
                                   rounded-lg
                                   focus:outline-none
                                   focus:ring-2
                                   focus:ring-slate-400
                                   focus:border-slate-400
                                   transition"
                        />

                    </div>


                    <!-- CONFIRM PASSWORD -->
                    <div class="mb-8">

                        <label
                            for="confirmPassword"
                            class="block text-sm font-medium
                                   text-slate-700 mb-2"
                        >
                            Confirm New Password
                        </label>

                        <input
                            id="confirmPassword"
                            v-model="confirmPassword"
                            type="password"
                            placeholder="Confirm new password"
                            required
                            minlength="6"
                            autocomplete="new-password"
                            class="w-full px-4 py-3
                                   border border-slate-300
                                   rounded-lg
                                   focus:outline-none
                                   focus:ring-2
                                   focus:ring-slate-400
                                   focus:border-slate-400
                                   transition"
                        />

                    </div>

                    <!-- CHANGE PASSWORD BUTTON -->
                    <button
                        type="submit"
                        :disabled="loading"
                        class="w-full
                                bg-indigo-600
                                hover:bg-indigo-700
                                disabled:bg-slate-400
                                disabled:cursor-not-allowed
                                text-white
                                font-semibold
                                py-3
                                mb-5
                                rounded-lg
                                transition
                                duration-200"
                    >
                        {{
                            loading
                                ? "Changing Password..."
                                : "Change Password"
                        }}
                    </button>
                    <!-- FORGOT PASSWORD -->
                    <div
                        class="text-center "
                    >

                        <span
                            class="text-sm text-slate-500"
                        >
                            Did you forget your password?
                        </span>

                        <button
                            type="button"
                            @click="goToForgotPassword"
                            class="ml-1 text-sm font-semibold
                                    text-indigo-600
                                    hover:text-indigo-700
                                    hover:underline
                                    transition"
                        >
                            Forgot Password
                        </button>

                    </div>

                </form>

            </div>

        </div>

    </div>
</template>