import { createRouter, createWebHistory } from "vue-router";

// Layouts
import UserLayout from "../layouts/UserLayout.vue";
import AdminLayout from "../layouts/AdminLayout.vue";

// User Page
import Home from "../pages/user/Home.vue";
import BrowseBooks from "../pages/user/BrowerBooks.vue";
import MyBorrowings from "../pages/user/MyBorrowings.vue";
import MyFines from "../pages/user/MyFines.vue";
import Notification from "../pages/user/Notification.vue";
import Profile from "../pages/user/Profile.vue";


// Admin Pages
import AdminDashboard from "../pages/admin/AdminDashboard.vue";
import Books from "../pages/admin/Books.vue";
import AddBook from "../pages/admin/AddBook.vue";
// import BookEdit from "../pages/admin/BookEdit.vue";
import Categories from "../pages/admin/Categories.vue";
import Borrowings from "../pages/admin/Borrowings.vue";
import Returns from "../pages/admin/Returns.vue";
import Users from "../pages/admin/Users.vue";
import Roles from "../pages/admin/Roles.vue";
import Reports from "../pages/admin/Reports.vue";
import Fines from "../pages/admin/Fines.vue";
import AdminNotification from "../pages/admin/AdminNotification.vue";
import AdminProfile from "../pages/admin/AdminProfile.vue";

const routes = [
  // User Rout
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
  },


  // Admin Rout
  {
    path: "/admin",
    component: AdminLayout,
    children: [
      {
        path: "dashboard",
        name: "AdminDashboard",
        component: AdminDashboard
      },
      {
        path: "books",
        name: "AdminBooks",
        component: Books,
      },
      {
        path: "books/add",
        name: "AddBook",
        component: AddBook
      },
      // {
      //   path: "books/edit/:id",
      //   name: "BookEdit",
      //   component: BookEdit
      // },
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
        component: Users
      },
      {
        path: "fines",
        name: "AdminFines",
        component: Fines
      },
      {
        path: "profile",
        name: "AdminProfile",
        component: AdminProfile,
      },
      {
        path: "roles",
        name: "Roles",
        component: Roles,
      },
      {
        path: "reports",
        name: "Reports",
        component: Reports,
      },
      {
        path: "notifications",
        name: "AdminNotification",
        component: AdminNotification,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;



