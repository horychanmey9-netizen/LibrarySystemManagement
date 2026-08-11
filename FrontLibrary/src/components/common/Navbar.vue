<template>
    <nav class="bg-white border-b border-gray-200 px-4 sm:px-6 py-3">
        <div class="flex items-center justify-between gap-3">

        <!-- Left: Menu + Search -->
        <div class="flex items-center gap-2 min-w-0 flex-1">

            <!-- Menu Button -->
            <button
            class="shrink-0 w-10 h-10 flex items-center justify-center
                    rounded-lg hover:bg-gray-100
                    text-gray-600 transition"
            >
            <svg
                class="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16M4 18h16"
                />
            </svg>
            </button>

            <!-- Search -->
            <div class="relative w-full max-w-xs">
            <svg
                class="absolute left-3 top-1/2 -translate-y-1/2
                    w-5 h-5 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="m21 21-4.35-4.35
                    m2.35-5.65a8 8 0 1 1-16 0
                    8 8 0 0 1 16 0Z"
                />
            </svg>

            <input
                type="text"
                placeholder="Search..."
                class="w-full pl-10 pr-3 py-2.5
                    bg-gray-100 rounded-lg
                    border border-transparent
                    focus:bg-white
                    focus:border-blue-500
                    focus:ring-2 focus:ring-blue-100
                    outline-none transition"
            />
            </div>
        </div>

        <!-- Right -->
        <div class="flex items-center gap-2 sm:gap-5 shrink-0">

            <!-- Notification -->
            <button
            class="relative p-2 text-gray-500
                    hover:text-blue-600
                    hover:bg-gray-100
                    rounded-full transition"
            >
            <svg
                class="w-6 h-6"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M15 17h5l-1.5-1.5
                    A2 2 0 0 1 18 14V11
                    a6 6 0 0 0-12 0v3
                    a2 2 0 0 1-.5 1.5L4 17h5
                    m6 0a3 3 0 0 1-6 0"
                />
            </svg>

            <!-- Notification Badge -->
            <span
                class="absolute top-1 right-1
                    w-2.5 h-2.5 bg-red-500
                    rounded-full border-2 border-white"
            ></span>
            </button>

            <!-- Profile -->
            <div class="flex items-center gap-2 cursor-pointer">

            <!-- Avatar -->
            <img
                src="https://i.pravatar.cc/100?img=12"
                alt="Profile"
                class="w-9 h-9 sm:w-10 sm:h-10
                    rounded-full object-cover
                    border-2 border-gray-200"
            />

            <!-- User Info -->
            <div class="hidden md:block">
                <p class="text-sm font-semibold text-gray-800">
                Admin
                </p>

                <p class="text-xs text-gray-500">
                Administrator
                </p>
            </div>

            <!-- Dropdown Icon -->
            <svg
                class="hidden md:block w-4 h-4 text-gray-400"
                fill="none"
                stroke="currentColor"
                viewBox="0 0 24 24"
            >
                <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="m6 9 6 6 6-6"
                />
            </svg>

            </div>
        </div>

        </div>
    </nav>
</template>