import { createRouter, createWebHistory } from "vue-router";

// =====================================================
// LAYOUTS
// =====================================================

import UserLayout from "../Layouts/UserLayout.vue";
import AdminLayout from "../Layouts/AdminLayout.vue";

// =====================================================
// USER PAGES
// =====================================================

import Home from "../pages/user/Home.vue";
import BrowseBooks from "../pages/user/BrowseBooks.vue";
import MyBorrowings from "../pages/user/MyBorrowings.vue";
import MyFines from "../pages/user/MyFines.vue";
import SettingUser from "../pages/user/SettingUser.vue";
import UserProfile from "../pages/user/UserProfile.vue";

// =====================================================
// ADMIN PAGES
// =====================================================

import AdminDashboard from "../pages/admin/AdminDashboard.vue";
import Books from "../pages/admin/Books.vue";
import Borrower from "../pages/admin/Borrower.vue";
import Returns from "../pages/admin/Returns.vue";
import Users from "../pages/admin/Users.vue";
import Fines from "../pages/admin/Fines.vue";
import AdminNotification from "../pages/admin/AdminNotification.vue";
import AdminProfile from "../pages/admin/AdminProfile.vue";
import Categories from "../pages/admin/Categories.vue";
import Borrowing from "../pages/admin/Borrowing.vue";

// =====================================================
// AUTH PAGES
// =====================================================

import Login from "../pages/auth/login.vue";
import Register from "../pages/auth/Register.vue";
import OTPForm from "../pages/auth/OTPForm.vue";
import ForgotPassword from "../pages/auth/ForgotPassword.vue";
import ResetPassword from "../pages/auth/ResetPassword.vue";


// =====================================================
// ROUTES
// =====================================================

const routes = [

  // ===================================================
  // ROOT
  // ===================================================

  {
    path: "/",
    redirect: "/login",
  },

  // ===================================================
  // AUTH
  // ===================================================

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
  { 
    path: "/forgot-password", 
    name: "ForgotPassword", 
    component: ForgotPassword, 
  }, 
  {
    path: '/reset-password',
    name: "ResetPassword",
    component: ResetPassword
  },

  // ===================================================
  // USER
  // ===================================================

  {
    path: "/user",
    component: UserLayout,

    meta: {
      requiresAuth: true,
    },

    children: [

      // /user
      // -> /user/home
      {
        path: "",
        redirect: {
          name: "Home",
        },
      },

      // /user/home
      {
        path: "home",
        name: "Home",
        component: Home,
      },

      // /user/browse-books
      {
        path: "browse-books",
        name: "BrowseBooks",
        component: BrowseBooks,
      },

      // /user/my-borrowings
      {
        path: "my-borrowings",
        name: "MyBorrowings",
        component: MyBorrowings,
      },

      // /user/my-fines
      {
        path: "my-fines",
        name: "MyFines",
        component: MyFines,
      },

      // /user/notifications
      {
        path: "setting",
        name: "Setting",
        component: SettingUser,
      },

      // /user/profile
      {
        path: "profile",
        name: "UserProfile",
        component: UserProfile,
      },
    ],
  },

  // ===================================================
  // ADMIN
  // ===================================================

  {
    path: "/admin",
    component: AdminLayout,

    meta: {
      requiresAuth: true,
      role: "ADMIN",
    },

    children: [

      // /admin
      // -> /admin/dashboard
      {
        path: "",
        redirect: {
          name: "AdminDashboard",
        },
      },

      // /admin/dashboard
      {
        path: "dashboard",
        name: "AdminDashboard",
        component: AdminDashboard,
      },

      // /admin/books
      {
        path: "books",
        name: "AdminBooks",
        component: Books,
      },

      // /admin/categories
      {
        path: "categories",
        name: "AdminCategories",
        component: Categories,
      },

      // /admin/borrowing
      {
        path: "borrowing",
        name: "AdminBorrowing",
        component: Borrowing,
      },

      // /admin/borrower
      {
        path: "borrower",
        name: "AdminBorrower",
        component: Borrower,
      },

      // /admin/returns
      {
        path: "returns",
        name: "AdminReturns",
        component: Returns,
      },

      // /admin/users
      {
        path: "users",
        name: "AdminUsers",
        component: Users,
      },

      // /admin/fines
      {
        path: "fines",
        name: "AdminFines",
        component: Fines,
      },

      // /admin/notifications
      {
        path: "notifications",
        name: "AdminNotification",
        component: AdminNotification,
      },

      // /admin/profile
      {
        path: "profile",
        name: "AdminProfile",
        component: AdminProfile,
      },
    ],
  },

  // ===================================================
  // NOT FOUND
  // ===================================================

  {
    path: "/:pathMatch(.*)*",
    redirect: "/login",
  },
];

// =====================================================
// CREATE ROUTER
// =====================================================

const router = createRouter({
  history: createWebHistory(),
  routes,

  // Optional:
  // When navigating back/forward, keep scroll at top
  scrollBehavior() {
    return {
      top: 0,
    };
  },
});

// =====================================================
// AUTHENTICATION GUARD
// =====================================================

router.beforeEach((to, from, next) => {

  // ---------------------------------------------------
  // GET AUTH DATA
  // ---------------------------------------------------

  const token = sessionStorage.getItem("token");
  const role = sessionStorage.getItem("role");

  // ---------------------------------------------------
  // DEBUG
  // ---------------------------------------------------

  console.log("=================================");
  console.log("ROUTER GUARD");
  console.log("From:", from.fullPath);
  console.log("To:", to.fullPath);
  console.log("Token:", token ? "EXISTS" : "NONE");
  console.log("Role:", role);
  console.log("=================================");

  // ===================================================
  // PUBLIC ROUTES
  // ===================================================

  const publicRoutes = [ 
    "Login", 
    "Register", 
    "OTPForm", 
    "ForgotPassword",
    "ResetPassword"
  ];

  // ===================================================
  // NOT LOGGED IN
  // ===================================================

  if (!token) {

    // Allow public pages
    if (publicRoutes.includes(to.name)) {
      return next();
    }

    // Any protected page -> Login
    return next({
      name: "Login",
    });
  }

  // ===================================================
  // LOGGED IN
  // ===================================================

  // ---------------------------------------------------
  // ADMIN
  // ---------------------------------------------------

  if (role === "ADMIN") {

    // If ADMIN tries to open Login
    if (to.name === "Login") {

      return next({
        name: "AdminDashboard",
      });
    }

    // ADMIN can access admin and user pages
    return next();
  }

  // ---------------------------------------------------
  // USER
  // ---------------------------------------------------

  if (role === "USER") {

    // If USER tries to open Login
    if (to.name === "Login") {

      return next({
        name: "Home",
      });
    }

    // USER cannot access ADMIN
    if (to.path.startsWith("/admin")) {

      return next({
        name: "Home",
      });
    }

    // USER can access user pages
    return next();
  }

  // ===================================================
  // INVALID ROLE
  // ===================================================

  console.warn("Invalid role:", role);

  sessionStorage.clear();

  return next({
    name: "Login",
  });
});

// =====================================================
// EXPORT
// =====================================================

export default router;