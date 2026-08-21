
<template>
  <div
    ref="notificationWrapper"
    class="relative"
  >

    <!-- ================= BELL BUTTON ================= -->
    <button
      @click="toggleDropdown"
      class="relative flex h-10 w-10 items-center justify-center
             rounded-xl text-slate-600 transition
             hover:bg-slate-100 hover:text-blue-600"
      aria-label="Notifications"
    >

      <!-- Bell -->
      <svg
        class="h-6 w-6"
        fill="none"
        stroke="currentColor"
        viewBox="0 0 24 24"
      >
        <path
          stroke-linecap="round"
          stroke-linejoin="round"
          stroke-width="2"
          d="M15 17h5l-1.4-1.4A2 2 0 0 1 18 14.2V11a6 6 0 1 0-12 0v3.2a2 2 0 0 1-.6 1.4L4 17h5m6 0a3 3 0 0 1-6 0m6 0H9"
        />
      </svg>

      <!-- Unread Count -->
      <span
        v-if="unreadCount > 0"
        class="absolute -right-0.5 -top-0.5 flex
               min-h-4 min-w-4 items-center justify-center
               rounded-full bg-red-500 px-1 text-[9px]
               font-bold leading-none text-white"
      >
        {{ unreadCount > 9 ? "9+" : unreadCount }}
      </span>
    </button>


    <!-- ================= DROPDOWN ================= -->
    <Transition name="notification-dropdown">

      <div
        v-if="showDropdown"
        class="absolute right-0 top-12 z-50
               w-[calc(100vw-2rem)] max-w-[360px]
               overflow-hidden rounded-2xl border
               border-slate-200 bg-white shadow-xl"
      >

        <!-- Header -->
        <div
          class="flex items-center justify-between
                 border-b border-slate-100 px-4 py-4"
        >

          <div>
            <h3 class="text-sm font-bold text-slate-900">
              Notifications
            </h3>

            <p class="mt-0.5 text-xs text-slate-400">
              {{ unreadCount }} unread
            </p>
          </div>

          <button
            v-if="unreadCount > 0"
            @click.stop="markAll"
            class="text-xs font-semibold text-blue-600
                   transition hover:text-blue-700"
          >
            Mark all read
          </button>

        </div>


        <!-- List -->
        <div class="max-h-[360px] overflow-y-auto">

          <div
            v-for="notification in recentNotifications"
            :key="notification.id"
            @click="openNotification(notification)"
            class="cursor-pointer border-b border-slate-100
                   px-4 py-3 transition last:border-b-0
                   hover:bg-slate-50"
            :class="
              notification.read
                ? 'bg-white'
                : 'bg-blue-50/50'
            "
          >

            <div class="flex gap-3">

              <!-- Icon -->
              <div
                class="flex h-10 w-10 shrink-0 items-center
                       justify-center rounded-xl text-lg"
                :class="typeIconClass(notification.type)"
              >
                {{ typeIcon(notification.type) }}
              </div>


              <!-- Content -->
              <div class="min-w-0 flex-1">

                <div class="flex items-start gap-2">

                  <h4
                    class="min-w-0 flex-1 truncate text-sm"
                    :class="
                      notification.read
                        ? 'font-medium text-slate-600'
                        : 'font-bold text-slate-900'
                    "
                  >
                    {{ notification.subject }}
                  </h4>

                  <span
                    v-if="!notification.read"
                    class="mt-1.5 h-2 w-2 shrink-0
                           rounded-full bg-blue-600"
                  ></span>

                </div>

                <p
                  class="mt-1 line-clamp-2 text-xs
                         leading-5 text-slate-400"
                >
                  {{ notification.message }}
                </p>

                <div class="mt-1.5 flex items-center gap-2">

                  <span
                    class="rounded-md px-1.5 py-0.5
                           text-[9px] font-semibold"
                    :class="typeClass(notification.type)"
                  >
                    {{ notification.type }}
                  </span>

                  <span class="text-[10px] text-slate-400">
                    {{ notification.date }}
                  </span>

                </div>

              </div>

            </div>

          </div>


          <!-- Empty -->
          <div
            v-if="recentNotifications.length === 0"
            class="px-5 py-12 text-center"
          >
            <div class="text-4xl">
              🔔
            </div>

            <h4 class="mt-3 text-sm font-bold text-slate-700">
              No notifications
            </h4>

            <p class="mt-1 text-xs text-slate-400">
              You're all caught up.
            </p>
          </div>

        </div>


        <!-- Footer -->
        <div class="border-t border-slate-100 p-3">

          <button
            @click="viewAll"
            class="w-full rounded-xl bg-slate-50
                   px-4 py-2.5 text-sm font-semibold
                   text-slate-600 transition
                   hover:bg-blue-50 hover:text-blue-600"
          >
            View all notifications
          </button>

        </div>

      </div>

    </Transition>

  </div>
</template>


<script>
import {
  getNotifications,
  markAsRead,
  markAllAsRead,
  notificationUpdatedEvent,
} from "../../service/notificationService";

export default {
  name: "NotificationBell",

  data() {
    return {
      showDropdown: false,
      notifications: [],
    };
  },

  computed: {

    // ===============================
    // UNREAD COUNT
    // ===============================
    unreadCount() {
      return this.notifications.filter(
        notification => !notification.read
      ).length;
    },


    // ===============================
    // RECENT 5
    // ===============================
    recentNotifications() {
      return this.notifications.slice(0, 5);
    },
  },


  methods: {

    // ===============================
    // LOAD
    // ===============================
    loadNotifications() {
      this.notifications = getNotifications();
    },


    // ===============================
    // OPEN NOTIFICATION
    // ===============================
    openNotification(notification) {

      // Mark as read
      markAsRead(notification.id);

      // Close dropdown
      this.showDropdown = false;

      // Go Notification page
      this.$router.push({
        name: "Notification",
        query: {
          id: notification.id,
        },
      });
    },


    // ===============================
    // MARK ALL
    // ===============================
    markAll() {
      markAllAsRead();
    },


    // ===============================
    // VIEW ALL
    // ===============================
    viewAll() {

      this.showDropdown = false;

      this.$router.push({
        name: "Notification",
      });
    },


    // ===============================
    // TOGGLE
    // ===============================
    toggleDropdown() {
      this.showDropdown = !this.showDropdown;
    },


    // ===============================
    // OUTSIDE CLICK
    // ===============================
    handleOutsideClick(event) {

      if (
        this.$refs.notificationWrapper &&
        !this.$refs.notificationWrapper.contains(
          event.target
        )
      ) {
        this.showDropdown = false;
      }
    },


    // ===============================
    // ICON
    // ===============================
    typeIcon(type) {

      const icons = {
        Borrowing: "📚",
        Fine: "💰",
        Return: "↩️",
        Library: "📖",
        System: "⚙️",
        Overdue: "⚠️",
        DueSoon: "⏰",
      };

      return icons[type] || "🔔";
    },


    // ===============================
    // ICON CLASS
    // ===============================
    typeIconClass(type) {

      const classes = {
        Borrowing: "bg-blue-50 text-blue-600",
        Fine: "bg-red-50 text-red-600",
        Return: "bg-emerald-50 text-emerald-600",
        Library: "bg-purple-50 text-purple-600",
        System: "bg-slate-100 text-slate-600",
        Overdue: "bg-red-50 text-red-600",
        DueSoon: "bg-orange-50 text-orange-600",
      };

      return (
        classes[type] ||
        "bg-slate-100 text-slate-600"
      );
    },


    // ===============================
    // TYPE CLASS
    // ===============================
    typeClass(type) {

      const classes = {
        Borrowing: "bg-blue-50 text-blue-600",
        Fine: "bg-red-50 text-red-600",
        Return: "bg-emerald-50 text-emerald-600",
        Library: "bg-purple-50 text-purple-600",
        System: "bg-slate-100 text-slate-600",
        Overdue: "bg-red-50 text-red-600",
        DueSoon: "bg-orange-50 text-orange-600",
      };

      return (
        classes[type] ||
        "bg-slate-100 text-slate-600"
      );
    },


    // ===============================
    // SYNC EVENT
    // ===============================
    syncNotifications() {
      this.loadNotifications();
    },
  },


  mounted() {

    this.loadNotifications();

    // Listen to notification changes
    window.addEventListener(
      notificationUpdatedEvent(),
      this.syncNotifications
    );

    document.addEventListener(
      "click",
      this.handleOutsideClick
    );
  },


  beforeUnmount() {

    window.removeEventListener(
      notificationUpdatedEvent(),
      this.syncNotifications
    );

    document.removeEventListener(
      "click",
      this.handleOutsideClick
    );
  },
};
</script>


<style scoped>
.notification-dropdown-enter-active,
.notification-dropdown-leave-active {
  transition: all 0.2s ease;
}

.notification-dropdown-enter-from,
.notification-dropdown-leave-to {
  opacity: 0;
  transform: translateY(-8px);
}
</style>

