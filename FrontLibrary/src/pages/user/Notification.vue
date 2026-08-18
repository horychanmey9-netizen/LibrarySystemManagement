<template>
  <div class="min-h-screen bg-slate-50 text-slate-900">

    <!-- ================= PAGE ================= -->
    <section class="w-full px-4 py-6 sm:px-6 lg:px-8">

      <!-- Header -->
      <div
        class="mb-6 flex flex-col gap-4
               sm:flex-row sm:items-center
               sm:justify-between"
      >

        <div>

          <div class="flex items-center gap-3">

            <h1
              class="text-2xl font-bold text-slate-900
                     sm:text-3xl"
            >
              Notifications
            </h1>

            <!-- Unread -->
            <span
              v-if="unreadCount > 0"
              class="rounded-full bg-red-500
                     px-2.5 py-1 text-xs
                     font-bold text-white"
            >
              {{ unreadCount }} new
            </span>

          </div>

          <p class="mt-1 text-sm text-slate-400">
            Your library messages and important updates.
          </p>

        </div>


        <!-- Mark All -->
        <button
          v-if="unreadCount > 0"
          @click="markAllAsRead"
          class="w-fit rounded-xl border
                 border-slate-200 bg-white
                 px-4 py-2.5 text-sm font-medium
                 text-slate-600 transition
                 hover:bg-slate-50
                 hover:text-blue-600"
        >
          ✓ Mark all as read
        </button>

      </div>


      <!-- ================= MAIL CONTAINER ================= -->
      <div
        class="overflow-hidden rounded-2xl
               border border-slate-200 bg-white
               shadow-sm"
      >

        <!-- ================= TOOLBAR ================= -->
        <div
          class="flex flex-col gap-4
                 border-b border-slate-200
                 px-4 py-4 sm:px-5
                 lg:flex-row
                 lg:items-center
                 lg:justify-between"
        >

          <!-- Search -->
          <div class="relative w-full lg:max-w-md">

            <svg
              class="absolute left-4 top-1/2
                     h-5 w-5 -translate-y-1/2
                     text-slate-400"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="m21 21-4.35-4.35m2.35-5.65a8 8 0 1 1-16 0 8 8 0 0 1 16 0Z"
              />
            </svg>

            <input
              v-model="search"
              type="text"
              placeholder="Search notifications..."
              class="w-full rounded-xl
                     border border-slate-200
                     bg-slate-50 py-3 pl-11 pr-4
                     text-sm outline-none transition
                     focus:border-blue-400
                     focus:ring-2 focus:ring-blue-100"
            />

          </div>


          <!-- Filter -->
          <div
            class="flex w-full gap-2
                   overflow-x-auto lg:w-auto"
          >

            <button
              v-for="item in filters"
              :key="item.value"
              @click="changeFilter(item.value)"
              class="whitespace-nowrap rounded-xl
                     px-4 py-2.5 text-sm
                     font-medium transition"
              :class="
                filter === item.value
                  ? 'bg-blue-600 text-white shadow-sm'
                  : 'bg-slate-50 text-slate-500 hover:bg-slate-100'
              "
            >
              {{ item.label }}

              <span
                v-if="
                  item.value === 'unread' &&
                  unreadCount > 0
                "
              >
                ({{ unreadCount }})
              </span>

            </button>

          </div>

        </div>


        <!-- ================= LIST HEADER ================= -->
        <div
          class="hidden border-b border-slate-100
                 bg-slate-50 px-5 py-3
                 text-xs font-semibold
                 uppercase tracking-wide
                 text-slate-400
                 md:flex md:items-center md:gap-4"
        >

          <div class="w-6"></div>

          <div class="w-6"></div>

          <div class="w-48">
            Sender
          </div>

          <div class="flex-1">
            Subject
          </div>

          <div class="w-28 text-right">
            Date
          </div>

          <div class="w-6"></div>

        </div>


        <!-- ================= LIST ================= -->
        <div>

          <div
            v-for="mail in displayedMails"
            :key="mail.id"
            @click="openMail(mail)"
            class="group cursor-pointer
                   border-b border-slate-100
                   transition last:border-b-0"
            :class="
              mail.read
                ? 'bg-white hover:bg-slate-50'
                : 'bg-blue-50/50 hover:bg-blue-50'
            "
          >

            <div
              class="flex flex-col gap-3
                     px-4 py-4 sm:px-5
                     md:flex-row
                     md:items-center md:gap-4"
            >

              <!-- Checkbox -->
              <div
                class="hidden w-6 shrink-0 md:block"
                @click.stop
              >

                <input
                  v-model="mail.selected"
                  type="checkbox"
                  class="h-4 w-4 rounded
                         border-slate-300
                         text-blue-600
                         focus:ring-blue-500"
                />

              </div>


              <!-- Star -->
              <button
                @click.stop="toggleStar(mail)"
                class="hidden w-6 shrink-0
                       text-xl transition md:block"
                :class="
                  mail.starred
                    ? 'text-yellow-400'
                    : 'text-slate-300 hover:text-yellow-400'
                "
              >
                ★
              </button>


              <!-- ================= MOBILE ================= -->
              <div
                class="flex items-start gap-3 md:hidden"
              >

                <!-- Icon -->
                <div
                  class="flex h-10 w-10 shrink-0
                         items-center justify-center
                         rounded-xl text-lg"
                  :class="typeIconClass(mail.type)"
                >
                  {{ typeIcon(mail.type) }}
                </div>


                <div class="min-w-0 flex-1">

                  <div
                    class="flex items-start
                           justify-between gap-2"
                  >

                    <h3
                      class="truncate text-sm"
                      :class="
                        mail.read
                          ? 'font-medium text-slate-600'
                          : 'font-bold text-slate-900'
                      "
                    >
                      {{ mail.subject }}
                    </h3>

                    <button
                      @click.stop="toggleStar(mail)"
                      class="text-lg"
                      :class="
                        mail.starred
                          ? 'text-yellow-400'
                          : 'text-slate-300'
                      "
                    >
                      ★
                    </button>

                  </div>


                  <p
                    class="mt-1 line-clamp-2
                           text-sm text-slate-400"
                  >
                    {{ mail.message }}
                  </p>


                  <div
                    class="mt-2 flex items-center gap-2"
                  >

                    <span
                      class="rounded-md px-2 py-0.5
                             text-[10px] font-semibold"
                      :class="typeClass(mail.type)"
                    >
                      {{ mail.type }}
                    </span>

                    <span class="text-xs text-slate-400">
                      {{ mail.date }}
                    </span>

                  </div>

                </div>

              </div>


              <!-- ================= DESKTOP ================= -->

              <!-- Sender -->
              <div
                class="hidden w-48 shrink-0
                       truncate md:block"
                :class="
                  mail.read
                    ? 'font-medium text-slate-600'
                    : 'font-bold text-slate-900'
                "
              >
                {{ mail.sender }}
              </div>


              <!-- Subject -->
              <div
                class="hidden min-w-0 flex-1
                       md:block"
              >

                <div class="flex items-center gap-2">

                  <span
                    class="truncate"
                    :class="
                      mail.read
                        ? 'font-medium text-slate-600'
                        : 'font-bold text-slate-900'
                    "
                  >
                    {{ mail.subject }}
                  </span>

                  <span
                    class="shrink-0 rounded-md
                           px-2 py-0.5
                           text-[10px] font-semibold"
                    :class="typeClass(mail.type)"
                  >
                    {{ mail.type }}
                  </span>

                </div>


                <p
                  class="mt-0.5 truncate text-sm"
                  :class="
                    mail.read
                      ? 'text-slate-400'
                      : 'text-slate-500'
                  "
                >
                  {{ mail.message }}
                </p>

              </div>


              <!-- Date -->
              <div
                class="hidden w-28 shrink-0
                       text-right text-xs
                       md:block"
                :class="
                  mail.read
                    ? 'text-slate-400'
                    : 'font-bold text-slate-700'
                "
              >
                {{ mail.date }}
              </div>


              <!-- Delete -->
              <button
                @click.stop="deleteMail(mail)"
                class="hidden w-6 shrink-0
                       text-slate-300
                       transition
                       hover:text-red-500
                       md:group-hover:block"
              >
                🗑
              </button>

            </div>

          </div>


          <!-- ================= EMPTY ================= -->
          <div
            v-if="displayedMails.length === 0"
            class="px-6 py-20 text-center"
          >

            <div class="mb-4 text-5xl">
              🔔
            </div>

            <h3
              class="text-lg font-bold text-slate-700"
            >
              No notifications
            </h3>

            <p class="mt-1 text-sm text-slate-400">
              You're all caught up.
            </p>

          </div>

        </div>


        <!-- ================= LOAD MORE ================= -->
        <div
          v-if="
            displayedMails.length <
            filteredMails.length
          "
          class="border-t border-slate-100
                 p-4 text-center"
        >

          <button
            @click="loadMore"
            :disabled="loading"
            class="rounded-xl bg-slate-50
                   px-5 py-2.5 text-sm
                   font-medium text-slate-600
                   transition
                   hover:bg-blue-50
                   hover:text-blue-600
                   disabled:cursor-not-allowed
                   disabled:opacity-50"
          >
            {{ loading ? "Loading..." : "Load more" }}
          </button>

        </div>


        <!-- End -->
        <div
          v-else-if="filteredMails.length > 0"
          class="border-t border-slate-100
                 py-5 text-center
                 text-xs text-slate-400"
        >
          You've reached the end.
        </div>

      </div>

    </section>


    <!-- ================================================= -->
    <!-- DETAIL MODAL -->
    <!-- ================================================= -->

    <Transition name="modal">

      <div
        v-if="selectedMail"
        class="fixed inset-0 z-50
               flex items-center justify-center
               bg-slate-950/40 p-4 sm:p-6"
        @click.self="closeMail"
      >

        <div
          class="flex max-h-[90vh] w-full max-w-2xl
                 flex-col overflow-hidden
                 rounded-2xl bg-white shadow-2xl"
        >

          <!-- Header -->
          <div
            class="flex items-center
                   justify-between border-b
                   border-slate-200
                   px-5 py-4 sm:px-6"
          >

            <div
              class="flex min-w-0
                     items-center gap-3"
            >

              <button
                @click="closeMail"
                class="flex h-9 w-9 shrink-0
                       items-center justify-center
                       rounded-lg text-slate-500
                       transition hover:bg-slate-100"
              >
                ←
              </button>

              <h2
                class="truncate font-bold
                       text-slate-900"
              >
                {{ selectedMail.subject }}
              </h2>

            </div>


            <button
              @click="deleteMail(selectedMail)"
              class="flex h-9 w-9 shrink-0
                     items-center justify-center
                     rounded-lg text-slate-400
                     transition
                     hover:bg-red-50
                     hover:text-red-500"
            >
              🗑
            </button>

          </div>


          <!-- Body -->
          <div class="overflow-y-auto">

            <div class="p-5 sm:p-6">

              <!-- Sender -->
              <div
                class="mb-7 flex items-center gap-4"
              >

                <div
                  class="flex h-12 w-12
                         shrink-0 items-center
                         justify-center
                         rounded-full
                         bg-blue-100 text-xl"
                >
                  📚
                </div>

                <div>

                  <h3
                    class="font-bold text-slate-900"
                  >
                    {{ selectedMail.sender }}
                  </h3>

                  <p
                    class="text-xs text-slate-400"
                  >
                    library@library.com
                  </p>

                </div>

                <div
                  class="ml-auto text-xs
                         text-slate-400"
                >
                  {{ selectedMail.date }}
                </div>

              </div>


              <!-- Type -->
              <span
                class="rounded-md px-2.5 py-1
                       text-xs font-semibold"
                :class="typeClass(selectedMail.type)"
              >
                {{ selectedMail.type }}
              </span>


              <!-- Subject -->
              <h2
                class="mt-5 text-2xl
                       font-bold text-slate-900"
              >
                {{ selectedMail.subject }}
              </h2>


              <!-- Message -->
              <div
                class="mt-5 leading-7
                       text-slate-600"
              >

                <p>
                  {{ selectedMail.message }}
                </p>

                <p class="mt-5">
                  Please check your library account
                  for more information. If you have
                  any questions, please contact
                  the library.
                </p>

                <p class="mt-8">
                  Thank you,<br />

                  <span
                    class="font-semibold
                           text-slate-900"
                  >
                    Library System
                  </span>
                </p>

              </div>

            </div>

          </div>


          <!-- Footer -->
          <div
            class="flex flex-col-reverse gap-3
                   border-t border-slate-200
                   px-5 py-4
                   sm:flex-row
                   sm:items-center
                   sm:justify-between
                   sm:px-6"
          >

            <button
              @click="toggleRead(selectedMail)"
              class="rounded-xl border
                     border-slate-200
                     px-4 py-2.5
                     text-sm font-medium
                     text-slate-600
                     transition
                     hover:bg-slate-50"
            >
              {{
                selectedMail.read
                  ? "Mark unread"
                  : "Mark as read"
              }}
            </button>


            <button
              @click="closeMail"
              class="rounded-xl bg-blue-600
                     px-5 py-2.5 font-medium
                     text-white transition
                     hover:bg-blue-700"
            >
              Close
            </button>

          </div>

        </div>

      </div>

    </Transition>

  </div>
</template>


<script>
const STORAGE_KEY = "library_notifications";

const defaultNotifications = [
  {
    id: 1,
    sender: "Library System",
    subject: "Your book is due soon",
    message:
      "A Brief History of Time is due in 2 days. Please return or renew the book before the due date.",
    type: "Borrowing",
    date: "10:24 AM",
    read: false,
    starred: true,
    selected: false,
  },

  {
    id: 2,
    sender: "Library System",
    subject: "Fine payment reminder",
    message:
      "You have an outstanding fine of $8.50. Please make a payment to avoid additional charges.",
    type: "Fine",
    date: "9:10 AM",
    read: false,
    starred: false,
    selected: false,
  },

  {
    id: 3,
    sender: "Library System",
    subject: "Book successfully borrowed",
    message:
      "You have successfully borrowed Clean Code. Your due date is August 25, 2026.",
    type: "Borrowing",
    date: "Yesterday",
    read: false,
    starred: false,
    selected: false,
  },

  {
    id: 4,
    sender: "Library System",
    subject: "Book returned successfully",
    message:
      "Sapiens has been successfully returned. Thank you for returning the book on time.",
    type: "Return",
    date: "Yesterday",
    read: true,
    starred: false,
    selected: false,
  },

  {
    id: 5,
    sender: "Library System",
    subject: "New books are available",
    message:
      "A new collection of technology books has been added to the library.",
    type: "Library",
    date: "Aug 10",
    read: true,
    starred: true,
    selected: false,
  },

  {
    id: 6,
    sender: "Library System",
    subject: "Fine payment received",
    message:
      "Your payment of $3.00 for Clean Code has been successfully received.",
    type: "Fine",
    date: "Aug 09",
    read: true,
    starred: false,
    selected: false,
  },

  {
    id: 7,
    sender: "Library System",
    subject: "Book overdue",
    message:
      "The book Atomic Habits is overdue. Please return the book as soon as possible.",
    type: "Overdue",
    date: "Aug 08",
    read: false,
    starred: false,
    selected: false,
  },

  {
    id: 8,
    sender: "Library System",
    subject: "Library maintenance notice",
    message:
      "The library system will be under maintenance this weekend from 10:00 PM to 12:00 AM.",
    type: "System",
    date: "Aug 07",
    read: true,
    starred: false,
    selected: false,
  },

  {
    id: 9,
    sender: "Library System",
    subject: "Welcome to the library",
    message:
      "Welcome to our library system. You can now browse, borrow, and manage your books.",
    type: "System",
    date: "Aug 01",
    read: true,
    starred: false,
    selected: false,
  },
];


export default {
  name: "Notification",

  data() {
    return {
      search: "",
      filter: "all",

      displayLimit: 6,

      selectedMail: null,

      loading: false,

      filters: [
        {
          label: "All",
          value: "all",
        },
        {
          label: "Unread",
          value: "unread",
        },
        {
          label: "Read",
          value: "read",
        },
        {
          label: "Starred",
          value: "starred",
        },
      ],

      mails: [],
    };
  },


  computed: {

    filteredMails() {

      const keyword =
        this.search
          .toLowerCase()
          .trim();

      return this.mails.filter(mail => {

        const matchesSearch =
          !keyword ||
          mail.sender
            .toLowerCase()
            .includes(keyword) ||
          mail.subject
            .toLowerCase()
            .includes(keyword) ||
          mail.message
            .toLowerCase()
            .includes(keyword);

        let matchesFilter = true;

        if (this.filter === "unread") {
          matchesFilter = !mail.read;
        }

        if (this.filter === "read") {
          matchesFilter = mail.read;
        }

        if (this.filter === "starred") {
          matchesFilter = mail.starred;
        }

        return (
          matchesSearch &&
          matchesFilter
        );
      });
    },


    displayedMails() {

      return this.filteredMails.slice(
        0,
        this.displayLimit
      );
    },


    unreadCount() {

      return this.mails.filter(
        mail => !mail.read
      ).length;
    },
  },


  // ==================================================
  // METHODS
  // ==================================================

  methods: {

    // ============================
    // LOAD NOTIFICATIONS
    // ============================
    loadNotifications() {

      const saved =
        localStorage.getItem(STORAGE_KEY);

      if (saved) {

        this.mails =
          JSON.parse(saved);

      } else {

        this.mails =
          JSON.parse(
            JSON.stringify(
              defaultNotifications
            )
          );

        this.saveNotifications();
      }
    },


    // ============================
    // SAVE NOTIFICATIONS
    // ============================
    saveNotifications() {

      localStorage.setItem(
        STORAGE_KEY,
        JSON.stringify(this.mails)
      );
    },


    // ============================
    // CHANGE FILTER
    // ============================
    changeFilter(value) {

      this.filter = value;

      this.displayLimit = 6;
    },


    // ============================
    // OPEN MAIL FROM NOTIFICATION PAGE
    // ============================
    openMail(mail) {

      this.selectedMail = mail;

      mail.read = true;

      this.saveNotifications();
    },


    // ============================
    // OPEN MAIL FROM HEADER
    // ============================
    openNotificationFromQuery() {

      const notificationId =
        this.$route.query.id;

      // No ID in URL
      if (!notificationId) {
        return;
      }

      const mail = this.mails.find(
        item =>
          String(item.id) ===
          String(notificationId)
      );

      // Notification not found
      if (!mail) {
        return;
      }

      // Open Detail Modal
      this.selectedMail = mail;

      // Mark as read
      mail.read = true;

      this.saveNotifications();
    },


    // ============================
    // CLOSE MAIL
    // ============================
    closeMail() {

      this.selectedMail = null;
    },


    // ============================
    // TOGGLE READ
    // ============================
    toggleRead(mail) {

      mail.read = !mail.read;

      this.saveNotifications();
    },


    // ============================
    // MARK ALL AS READ
    // ============================
    markAllAsRead() {

      this.mails.forEach(mail => {
        mail.read = true;
      });

      this.saveNotifications();
    },


    // ============================
    // STAR
    // ============================
    toggleStar(mail) {

      mail.starred =
        !mail.starred;

      this.saveNotifications();
    },


    // ============================
    // DELETE
    // ============================
    deleteMail(mail) {

      const confirmed =
        window.confirm(
          "Delete this notification?"
        );

      if (!confirmed) {
        return;
      }

      const index =
        this.mails.findIndex(
          item => item.id === mail.id
        );

      if (index !== -1) {

        this.mails.splice(
          index,
          1
        );
      }

      if (
        this.selectedMail &&
        this.selectedMail.id === mail.id
      ) {
        this.selectedMail = null;
      }

      this.saveNotifications();
    },


    // ============================
    // LOAD MORE
    // ============================
    loadMore() {

      if (this.loading) {
        return;
      }

      this.loading = true;

      setTimeout(() => {

        this.displayLimit += 6;

        this.loading = false;

      }, 300);
    },


    // ============================
    // TYPE CLASS
    // ============================
    typeClass(type) {

      const classes = {

        Borrowing:
          "bg-blue-50 text-blue-600",

        Fine:
          "bg-red-50 text-red-500",

        Return:
          "bg-emerald-50 text-emerald-600",

        Library:
          "bg-purple-50 text-purple-600",

        System:
          "bg-slate-100 text-slate-600",

        Overdue:
          "bg-red-50 text-red-600",

        DueSoon:
          "bg-orange-50 text-orange-600",
      };

      return (
        classes[type] ||
        "bg-slate-100 text-slate-600"
      );
    },


    // ============================
    // TYPE ICON
    // ============================
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


    // ============================
    // TYPE ICON CLASS
    // ============================
    typeIconClass(type) {

      const classes = {

        Borrowing:
          "bg-blue-50 text-blue-600",

        Fine:
          "bg-red-50 text-red-600",

        Return:
          "bg-emerald-50 text-emerald-600",

        Library:
          "bg-purple-50 text-purple-600",

        System:
          "bg-slate-100 text-slate-600",

        Overdue:
          "bg-red-50 text-red-600",

        DueSoon:
          "bg-orange-50 text-orange-600",
      };

      return (
        classes[type] ||
        "bg-slate-100 text-slate-600"
      );
    },
  },


  // ==================================================
  // MOUNTED
  // ==================================================

  mounted() {

    // 1. Load notifications first
    this.loadNotifications();

    // 2. Check if notification came from Header
    this.openNotificationFromQuery();

    // 3. Listen for localStorage changes
    window.addEventListener(
      "storage",
      this.loadNotifications
    );
  },


  // ==================================================
  // BEFORE UNMOUNT
  // ==================================================

  beforeUnmount() {

    window.removeEventListener(
      "storage",
      this.loadNotifications
    );
  },
};
</script>


<style scoped>
.modal-enter-active,
.modal-leave-active {
  transition: opacity 0.2s ease;
}

.modal-enter-active > div,
.modal-leave-active > div {
  transition: transform 0.2s ease;
}

.modal-enter-from,
.modal-leave-to {
  opacity: 0;
}

.modal-enter-from > div,
.modal-leave-to > div {
  transform: scale(0.96);
}
</style>