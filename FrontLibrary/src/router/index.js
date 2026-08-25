import { createRouter, createWebHistory } from "vue-router";

// Layouts

import UserLayout from "../Layouts/UserLayout.vue";
import AdminLayout from "../Layouts/AdminLayout.vue";

// User Page
import Home from "../pages/user/Home.vue";
import BrowseBooks from "../pages/user/BrowseBooks.vue";
import MyBorrowings from "../pages/user/MyBorrowings.vue";
import MyFines from "../pages/user/MyFines.vue";
import Notification from "../pages/user/Notification.vue";
import UserProfile from "../pages/user/UserProfile.vue";


// Admin Pages
import AdminDashboard from "../pages/admin/AdminDashboard.vue";
import Books from "../pages/admin/Books.vue";
import Borrowings from "../pages/admin/Borrowings.vue";
import Returns from "../pages/admin/Returns.vue";
import Users from "../pages/admin/Users.vue";
import Fines from "../pages/admin/Fines.vue";
import AdminNotification from "../pages/admin/AdminNotification.vue";
import AdminProfile from "../pages/admin/AdminProfile.vue";
import Login from "../pages/auth/login.vue";
import Register from "../pages/auth/Register.vue"
import OTPForm from "../pages/auth/OTPForm.vue";
import Categories from "../pages/admin/Categories.vue";


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
        name: "UserProfile",
        component: UserProfile,
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
        path: "categories",
        name: "AdminCategories",
        component: Categories,
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

  const token =
    sessionStorage.getItem("token");

  const role =
    sessionStorage.getItem("role");


  // =====================================
  // NOT LOGIN
  // =====================================

  if (!token) {

    if (
      to.name === "Login" ||
      to.name === "Register" ||
      to.name === "OTPForm"
    ) {

      return next();

    }

    return next({
      name: "Login"
    });

  }


  // =====================================
  // LOGIN PAGE
  // =====================================

  if (to.name === "Login") {

    if (role === "ADMIN") {

      return next({
        name: "AdminDashboard"
      });

    }


    if (role === "USER") {

      return next({
        name: "Home"
      });

    }


    sessionStorage.clear();

    return next({
      name: "Login"
    });

  }


  // =====================================
  // ADMIN
  // =====================================

  if (role === "ADMIN") {

    /*
     * ADMIN can access:
     *
     * /admin/*
     * /user/*
     *
     * Therefore do NOT redirect ADMIN
     * to dashboard when visiting user pages.
     */

    return next();

  }


  // =====================================
  // USER
  // =====================================

  if (role === "USER") {

    /*
     * USER cannot access ADMIN pages
     */

    if (
      to.path.startsWith("/admin")
    ) {

      return next({
        name: "Home"
      });

    }


    return next();

  }


  // =====================================
  // INVALID ROLE
  // =====================================

  sessionStorage.clear();

  return next({
    name: "Login"
  });

});
export default router;
