import { createRouter, createWebHistory } from "vue-router";

// Layouts

import UserLayout from "../layouts/UserLayout.vue";
import AdminLayout from "../layouts/AdminLayout.vue";

// User Page
import Home from "../pages/user/Home.vue";
import BrowseBooks from "../pages/user/BrowseBooks.vue";
import MyBorrowings from "../pages/user/MyBorrowings.vue";
import MyFines from "../pages/user/MyFines.vue";
import Notification from "../pages/user/Notification.vue";
import Profile from "../pages/user/Profile.vue";


// Admin Pages
import AdminDashboard from "../pages/admin/AdminDashboard.vue";
import Books from "../pages/admin/Books.vue";
import AddBook from "../pages/admin/AddBook.vue";
// import BookEdit from "../pages/admin/BookEdit.vue";
import CategoryCard from "../components/books/CategoryCard.vue";
import Borrowings from "../pages/admin/Borrowings.vue";
import Returns from "../pages/admin/Returns.vue";
import Users from "../pages/admin/Users.vue";
import Roles from "../pages/admin/Roles.vue";
import Reports from "../pages/admin/Reports.vue";
import Fines from "../pages/admin/Fines.vue";
import AdminNotification from "../pages/admin/AdminNotification.vue";
import AdminProfile from "../pages/admin/AdminProfile.vue";
import Login from "../pages/auth/login.vue";
import Register from "../pages/auth/Register.vue"
import OTPForm from "../pages/auth/OTPForm.vue";

const routes = [
  // ================= AUTH =================

  {
    path: "/",
    redirect: "/login",
  },

  {
    path: "/login",
    name: "Login",
    component: Login,
  },

  {
    path: "/register",
    name: "Register",
    component: Register,
  },

  {
    path: "/otp",
    name: "OTPForm",
    component: OTPForm,
  },


  // ================= USER =================

  {
  path: "/user",
  component: UserLayout,
  meta: {
    requiresAuth: true,
  },

    children: [
      {
        path: "home",
        name: "Home",
        component: Home,
      },

      {
        path: "browse-books",
        name: "BrowseBooks",
        component: BrowseBooks,
      },

      {
        path: "my-borrowings",
        name: "MyBorrowings",
        component: MyBorrowings,
      },

      {
        path: "my-fines",
        name: "MyFines",
        component: MyFines,
      },

      {
        path: "notifications",
        name: "Notification",
        component: Notification,
      },

      {
        path: "profile",
        name: "Profile",
        component: Profile,
      },
    ],
  },


  // ================= ADMIN =================

  {
    path: "/admin",
    component: AdminLayout,

    meta: {
      requiresAuth: true,
      role: "ADMIN",
    },

    children: [
      {
        path: "dashboard",
        name: "AdminDashboard",
        component: AdminDashboard,
      },

      {
        path: "books",
        name: "AdminBooks",
        component: Books,
      },

      {
        path: "books/add",
        name: "AddBook",
        component: AddBook,
      },

      {
        path: "categories",
        name: "AdminCategories",
        component: CategoryCard,
      },

      {
        path: "borrowings",
        name: "AdminBorrowings",
        component: Borrowings,
      },

      {
        path: "returns",
        name: "AdminReturns",
        component: Returns,
      },

      {
        path: "users",
        name: "AdminUsers",
        component: Users,
      },

      {
        path: "roles",
        name: "AdminRoles",
        component: Roles,
      },

      {
        path: "reports",
        name: "AdminReports",
        component: Reports,
      },

      {
        path: "fines",
        name: "AdminFines",
        component: Fines,
      },

      {
        path: "notifications",
        name: "AdminNotification",
        component: AdminNotification,
      },

      {
        path: "profile",
        name: "AdminProfile",
        component: AdminProfile,
      },
    ],
  },


  // ================= NOT FOUND =================

  {
    path: "/:pathMatch(.*)*",
    redirect: "/login",
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem("token");
  const role = sessionStorage.getItem("role");

  // =====================================
  // NOT LOGIN
  // =====================================
  if (!token) {
    if (
      to.path === "/login" ||
      to.path === "/register" ||
      to.path === "/otp"
    ) {
      return next();
    }

    return next("/login");
  }

  // =====================================
  // LOGIN PAGE
  // =====================================
  if (to.path === "/login") {
    if (role === "ADMIN") {
      return next("/admin/dashboard");
    }

    if (role === "USER") {
      return next("/user");
    }

    sessionStorage.clear();
    return next("/login");
  }

  // =====================================
  // ADMIN
  // Can access /admin AND /user
  // =====================================
  if (role === "ADMIN") {
    return next();
  }

  // =====================================
  // USER
  // Can access /user only
  // =====================================
  if (role === "USER") {
    if (to.path.startsWith("/admin")) {
      return next("/user");
    }

    return next();
  }

  // =====================================
  // INVALID ROLE
  // =====================================
  sessionStorage.clear();
  return next("/login");
});
export default router;
