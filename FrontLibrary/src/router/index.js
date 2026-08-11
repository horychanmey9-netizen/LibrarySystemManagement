import { createRouter, createWebHistory } from "vue-router";

import UserLayout from "../layouts/UserLayout.vue";

import Home from "../pages/user/Home.vue";
import BrowseBooks from "../pages/user/BrowerBooks.vue";
import MyBorrowings from "../pages/user/MyBorrowings.vue";
import MyFines from "../pages/user/MyFines.vue";
import Notification from "../pages/user/Notification.vue";
import Profile from "../pages/user/Profile.vue";

const routes = [
  {
    path: "/",
    component: UserLayout,
    children: [
      { path: "", name: "Home", component: Home },
      { path: "browse-books", name: "BrowseBooks", component: BrowseBooks },
      { path: "my-borrowings", name: "MyBorrowings", component: MyBorrowings },
      { path: "my-fines", name: "MyFines", component: MyFines },
      { path: "notifications", name: "Notification", component: Notification },
      { path: "profile", name: "Profile", component: Profile }
    ]
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;