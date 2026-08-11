<template>
    <div class="min-h-screen bg-slate-100 flex items-center justify-center px-4 py-10">

        <div class="w-full max-w-md">

        <!-- Register Card -->
        <div class="bg-white rounded-2xl shadow-xl p-8">

            <h2 class="text-3xl text-center font-bold text-slate-800 mb-6">
            Register
            </h2>

            <form @submit.prevent="register" class="space-y-5">

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
                <a href="#" class="text-indigo-600 hover:underline">
                    Terms & Conditions
                </a>
                </p>
            </div>

            <!-- Button -->
            <button
                type="submit"
                class="w-full py-3 bg-indigo-600 hover:bg-indigo-700
                    text-white font-semibold rounded-xl transition
                    shadow-md hover:shadow-lg"
            >
                Create Account
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
    import { reactive } from "vue";

    const form = reactive({
    name: "",
    email: "",
    password: "",
    confirmPassword: "",
    terms: false
    });

    const register = () => {

    if (form.password !== form.confirmPassword) {
        alert("Passwords do not match!");
        return;
    }

    if (!form.terms) {
        alert("Please accept Terms & Conditions");
        return;
    }

    console.log("Register:", form);

  // Connect API here
};
</script>