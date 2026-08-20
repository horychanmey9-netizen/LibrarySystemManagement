import { createRouter, createWebHistory } from "vue-router";


import UserLayout from "../layouts/UserLayout.vue";

import Home from "../pages/user/Home.vue";
import BrowseBooks from "../pages/user/BrowseBooks.vue";
import MyBorrowings from "../pages/user/MyBorrowings.vue";
import MyFines from "../pages/user/MyFines.vue";
import Notification from "../pages/user/Notification.vue";
import Profile from "../pages/user/Profile.vue";


import Login from "../pages/auth/login.vue";
import Register from "../pages/auth/Register.vue"
import OTPForm from "../pages/auth/OTPForm.vue";

const routes = [
  {
    path: "/login",
    name: "Login",
    component: Login

  },
  {
    path: "/register",
    name: "Register",
    component: Register

  },
  {
    path: "/otp",
    name: "OTPForm",
    component: OTPForm

  },
  {
    path: "/user",
    component: UserLayout,
     meta: {
      requiresAuth: true,
      role: "USER",
    },
    children: [
  {
    path: "",
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
]
  },
   {
    path: "/:pathMatch(.*)*",
    redirect: "/login",
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});
router.beforeEach((to, from, next) => {
  const token = sessionStorage.getItem("token");
  const role = sessionStorage.getItem("role");

  // Not logged in → login
  if (to.meta.requiresAuth && !token) {
    return next("/login");
  }

  // Already logged in → don't allow login page
  if (to.path === "/login" && token) {
    if (role === "USER") {
      return next("/user");
    }

    if (role === "ADMIN") {
      return next("/admin/dashboard");
    }

    localStorage.clear();
    return next("/login");
  }

  // Check role
  if (to.meta.role && role !== to.meta.role) {
    if (role === "USER") {
      return next("/user");
    }

    if (role === "ADMIN") {
      return next("/admin/dashboard");
    }

    sessionStorage.clear();
    return next("/login");
  }

  next();
});
export default router;