<template>
    <div class="min-h-screen bg-slate-100 flex items-center justify-center px-4">
        <div class="w-full max-w-md">

        <!-- Login Card -->
        <div class="bg-white rounded-2xl shadow-xl p-8">

            <h2 class="text-2xl text-center font-bold text-slate-800 mb-6">
            Login
            </h2>

            <form @submit.prevent="login" class="space-y-5">

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
                <div class="flex justify-between mb-2">
                <label class="text-sm font-medium text-slate-700">
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
                class="w-full px-4 py-3 border border-slate-300 rounded-xl
                        outline-none focus:ring-2 focus:ring-indigo-500
                        focus:border-indigo-500 transition"
                />
            </div>

            <!-- Remember -->
            <div class="flex items-center gap-2">
                <input
                type="checkbox"
                v-model="form.remember"
                class="w-4 h-4 accent-indigo-600"
                />

                <label class="text-sm text-slate-600">
                Remember me
                </label>
            </div>

            <!-- Button -->
            <button
                type="submit"
                class="w-full py-3 bg-indigo-600 hover:bg-indigo-700
                    text-white font-semibold rounded-xl transition
                    shadow-md hover:shadow-lg"
            >
                Sign In
            </button>

            </form>

            <!-- Register -->
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
    import { reactive } from "vue";

    const form = reactive({
    email: "",
    password: "",
    remember: false
    });

    const login = () => {
    console.log("Login:", form);

  // Connect API here
};
</script>