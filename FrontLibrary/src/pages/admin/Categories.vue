<template>
  <div class="min-h-screen bg-gray-50 p-4 sm:p-6">

    <!-- =====================================================
         HEADER
    ====================================================== -->
    <div
      class="flex flex-col gap-4 md:flex-row md:items-center md:justify-between mb-6"
    >
      <div>
        <h1 class="text-2xl font-bold text-gray-800">
          Category Management
        </h1>

        <p class="text-gray-500 mt-1">
          Manage book categories in the library system
        </p>
      </div>

      <!-- Add Button -->
      <button
        type="button"
        @click="openAddModal"
        class="inline-flex items-center justify-center gap-2 px-5 py-2.5
               bg-blue-600 hover:bg-blue-700 text-white font-medium
               rounded-lg shadow-sm transition"
      >
        <span class="text-lg">+</span>
        Add Category
      </button>
    </div>


    <!-- =====================================================
         SUCCESS MESSAGE
    ====================================================== -->
    <div
      v-if="successMessage"
      class="mb-5 flex items-center justify-between gap-4
             rounded-lg border border-green-200 bg-green-50
             px-4 py-3 text-green-700"
    >
      <div class="flex items-center gap-2">
        <span class="font-semibold">✓</span>
        <span>{{ successMessage }}</span>
      </div>

      <button
        type="button"
        @click="successMessage = ''"
        class="text-green-700 hover:text-green-900 text-lg"
      >
        ×
      </button>
    </div>


    <!-- =====================================================
         ERROR MESSAGE
    ====================================================== -->
    <div
      v-if="errorMessage"
      class="mb-5 flex items-center justify-between gap-4
             rounded-lg border border-red-200 bg-red-50
             px-4 py-3 text-red-700"
    >
      <div class="flex items-center gap-2">
        <span class="font-semibold">!</span>
        <span>{{ errorMessage }}</span>
      </div>

      <button
        type="button"
        @click="errorMessage = ''"
        class="text-red-700 hover:text-red-900 text-lg"
      >
        ×
      </button>
    </div>


    <!-- =====================================================
         SEARCH + TOTAL
    ====================================================== -->
    <div
      class="bg-white rounded-xl shadow-sm border border-gray-200
             p-4 mb-6"
    >
      <div
        class="flex flex-col gap-4 md:flex-row md:items-center
               md:justify-between"
      >

        <!-- Search -->
        <div class="relative w-full md:max-w-md">

          <span
            class="absolute left-3 top-1/2 -translate-y-1/2
                   text-gray-400"
          >
            🔍
          </span>

          <input
            v-model="searchQuery"
            type="text"
            placeholder="Search category..."
            class="w-full rounded-lg border border-gray-300
                   py-2.5 pl-10 pr-4
                   text-gray-700
                   focus:border-blue-500
                   focus:ring-2 focus:ring-blue-100
                   outline-none transition"
          />
        </div>

        <!-- Total -->
        <div class="text-sm text-gray-500">
          Total Categories:
          <span class="font-semibold text-gray-800">
            {{ filteredCategories.length }}
          </span>
        </div>
      </div>
    </div>


    <!-- =====================================================
         LOADING
    ====================================================== -->
    <div
      v-if="loading"
      class="bg-white rounded-xl border border-gray-200
             shadow-sm p-10 text-center"
    >
      <div
        class="mx-auto mb-4 h-10 w-10 rounded-full
               border-4 border-gray-200
               border-t-blue-600 animate-spin"
      ></div>

      <p class="text-gray-500">
        Loading categories...
      </p>
    </div>


    <!-- =====================================================
         EMPTY STATE
    ====================================================== -->
    <div
      v-else-if="filteredCategories.length === 0"
      class="bg-white rounded-xl border border-gray-200
             shadow-sm p-10 text-center"
    >
      <div class="text-5xl mb-4">
        📂
      </div>

      <h2 class="text-lg font-semibold text-gray-800">
        No categories found
      </h2>

      <p class="text-gray-500 mt-1">
        {{
          searchQuery
            ? "Try another search keyword."
            : "Create your first category."
        }}
      </p>

      <button
        v-if="!searchQuery"
        type="button"
        @click="openAddModal"
        class="mt-5 px-5 py-2.5 rounded-lg
               bg-blue-600 hover:bg-blue-700
               text-white font-medium transition"
      >
        + Add Category
      </button>
    </div>


    <!-- =====================================================
         CATEGORY TABLE
    ====================================================== -->
    <div
      v-else
      class="bg-white rounded-xl border border-gray-200
             shadow-sm overflow-hidden"
    >

      <!-- Desktop Table -->
      <div class="hidden md:block overflow-x-auto">

        <table class="w-full text-left">

          <thead class="bg-gray-50 border-b border-gray-200">

            <tr>
              <th
                class="px-6 py-4 text-xs font-semibold
                       text-gray-500 uppercase tracking-wider"
              >
                #
              </th>

              <th
                class="px-6 py-4 text-xs font-semibold
                       text-gray-500 uppercase tracking-wider"
              >
                Category Name
              </th>

              <th
                class="px-6 py-4 text-xs font-semibold
                       text-gray-500 uppercase tracking-wider"
              >
                ID
              </th>

              <th
                class="px-6 py-4 text-xs font-semibold
                       text-gray-500 uppercase tracking-wider
                       text-right"
              >
                Actions
              </th>
            </tr>

          </thead>


          <tbody class="divide-y divide-gray-100">

            <tr
              v-for="(category, index) in filteredCategories"
              :key="category.id"
              class="hover:bg-gray-50 transition"
            >

              <!-- Number -->
              <td class="px-6 py-4 text-sm text-gray-500">
                {{ index + 1 }}
              </td>


              <!-- Category -->
              <td class="px-6 py-4">

                <div class="flex items-center gap-3">

                  <div
                    class="h-10 w-10 rounded-lg
                           bg-blue-100 text-blue-600
                           flex items-center justify-center
                           font-semibold"
                  >
                    {{ getCategoryInitial(category.name) }}
                  </div>

                  <div>
                    <p class="font-medium text-gray-800">
                      {{ category.name }}
                    </p>

                    <p class="text-xs text-gray-400">
                      Book Category
                    </p>
                  </div>

                </div>

              </td>


              <!-- ID -->
              <td class="px-6 py-4 text-sm text-gray-500">
                #{{ category.id }}
              </td>


              <!-- Actions -->
              <td class="px-6 py-4">

                <div class="flex justify-end gap-2">

                  <!-- Edit -->
                  <button
                    type="button"
                    @click="openEditModal(category)"
                    class="px-3 py-2 rounded-lg
                           bg-blue-50 text-blue-600
                           hover:bg-blue-100
                           font-medium text-sm transition"
                  >
                    Edit
                  </button>

                  <!-- Delete -->
                  <button
                    type="button"
                    @click="openDeleteModal(category)"
                    class="px-3 py-2 rounded-lg
                           bg-red-50 text-red-600
                           hover:bg-red-100
                           font-medium text-sm transition"
                  >
                    Delete
                  </button>

                </div>

              </td>

            </tr>

          </tbody>

        </table>

      </div>


      <!-- ===================================================
           MOBILE CARDS
      ==================================================== -->
      <div class="md:hidden divide-y divide-gray-100">

        <div
          v-for="(category, index) in filteredCategories"
          :key="category.id"
          class="p-4"
        >

          <div class="flex items-center justify-between gap-3">

            <div class="flex items-center gap-3 min-w-0">

              <div
                class="h-11 w-11 shrink-0 rounded-lg
                       bg-blue-100 text-blue-600
                       flex items-center justify-center
                       font-semibold"
              >
                {{ getCategoryInitial(category.name) }}
              </div>

              <div class="min-w-0">

                <p class="font-medium text-gray-800 truncate">
                  {{ category.name }}
                </p>

                <p class="text-xs text-gray-400 mt-1">
                  #{{ category.id }}
                </p>

              </div>

            </div>

            <span class="text-xs text-gray-400 shrink-0">
              {{ index + 1 }}
            </span>

          </div>


          <div class="flex gap-2 mt-4">

            <button
              type="button"
              @click="openEditModal(category)"
              class="flex-1 px-3 py-2 rounded-lg
                     bg-blue-50 text-blue-600
                     hover:bg-blue-100
                     font-medium text-sm transition"
            >
              Edit
            </button>

            <button
              type="button"
              @click="openDeleteModal(category)"
              class="flex-1 px-3 py-2 rounded-lg
                     bg-red-50 text-red-600
                     hover:bg-red-100
                     font-medium text-sm transition"
            >
              Delete
            </button>

          </div>

        </div>

      </div>

    </div>


    <!-- =====================================================
         ADD / EDIT MODAL
    ====================================================== -->
    <div
      v-if="showFormModal"
      class="fixed inset-0 z-50 flex items-center
             justify-center p-4"
    >

      <!-- Overlay -->
      <div
        class="absolute inset-0 bg-black/50"
        @click="closeFormModal"
      ></div>


      <!-- Modal -->
      <div
        class="relative w-full max-w-md
               bg-white rounded-xl shadow-xl"
      >

        <!-- Modal Header -->
        <div
          class="flex items-center justify-between
                 px-6 py-4 border-b border-gray-200"
        >

          <div>
            <h2 class="text-lg font-semibold text-gray-800">
              {{ isEditMode ? "Edit Category" : "Add Category" }}
            </h2>

            <p class="text-sm text-gray-500 mt-1">
              {{
                isEditMode
                  ? "Update category information"
                  : "Create a new book category"
              }}
            </p>
          </div>

          <button
            type="button"
            @click="closeFormModal"
            class="text-gray-400 hover:text-gray-700
                   text-2xl leading-none"
          >
            ×
          </button>

        </div>


        <!-- Form -->
        <form
          @submit.prevent="submitCategory"
          class="p-6"
        >

          <label
            for="categoryName"
            class="block text-sm font-medium
                   text-gray-700 mb-2"
          >
            Category Name
          </label>

          <input
            id="categoryName"
            v-model="form.name"
            type="text"
            placeholder="Enter category name"
            maxlength="100"
            autocomplete="off"
            class="w-full rounded-lg border border-gray-300
                   px-4 py-2.5
                   text-gray-700
                   focus:border-blue-500
                   focus:ring-2 focus:ring-blue-100
                   outline-none transition"
            :class="{
              'border-red-400 focus:border-red-500':
                formError
            }"
          />

          <p
            v-if="formError"
            class="mt-2 text-sm text-red-600"
          >
            {{ formError }}
          </p>


          <!-- Buttons -->
          <div class="flex justify-end gap-3 mt-6">

            <button
              type="button"
              @click="closeFormModal"
              class="px-4 py-2.5 rounded-lg
                     border border-gray-300
                     text-gray-700
                     hover:bg-gray-50
                     font-medium transition"
            >
              Cancel
            </button>

            <button
              type="submit"
              :disabled="saving"
              class="px-5 py-2.5 rounded-lg
                     bg-blue-600 hover:bg-blue-700
                     disabled:bg-blue-300
                     text-white font-medium
                     transition"
            >
              <span v-if="saving">
                Saving...
              </span>

              <span v-else>
                {{ isEditMode ? "Update Category" : "Create Category" }}
              </span>
            </button>

          </div>

        </form>

      </div>

    </div>


    <!-- =====================================================
         DELETE MODAL
    ====================================================== -->
    <div
      v-if="showDeleteModal"
      class="fixed inset-0 z-50 flex items-center
             justify-center p-4"
    >

      <!-- Overlay -->
      <div
        class="absolute inset-0 bg-black/50"
        @click="closeDeleteModal"
      ></div>


      <!-- Modal -->
      <div
        class="relative w-full max-w-md
               bg-white rounded-xl shadow-xl p-6"
      >

        <!-- Icon -->
        <div
          class="mx-auto h-14 w-14 rounded-full
                 bg-red-100 text-red-600
                 flex items-center justify-center
                 text-2xl"
        >
          !
        </div>


        <div class="text-center mt-4">

          <h2 class="text-lg font-semibold text-gray-800">
            Delete Category?
          </h2>

          <p class="text-gray-500 mt-2">
            Are you sure you want to delete
            <span class="font-semibold text-gray-700">
              "{{ selectedCategory?.name }}"
            </span>
            ?
          </p>

          <p class="text-sm text-red-500 mt-2">
            This action cannot be undone.
          </p>

        </div>


        <!-- Buttons -->
        <div class="flex justify-center gap-3 mt-6">

          <button
            type="button"
            @click="closeDeleteModal"
            class="px-5 py-2.5 rounded-lg
                   border border-gray-300
                   text-gray-700
                   hover:bg-gray-50
                   font-medium transition"
          >
            Cancel
          </button>

          <button
            type="button"
            @click="confirmDelete"
            :disabled="deleting"
            class="px-5 py-2.5 rounded-lg
                   bg-red-600 hover:bg-red-700
                   disabled:bg-red-300
                   text-white font-medium transition"
          >
            <span v-if="deleting">
              Deleting...
            </span>

            <span v-else>
              Delete
            </span>
          </button>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>
import { ref, computed, onMounted } from "vue";

import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory,
} from "../../service/categoryService";


// =====================================================
// STATE
// =====================================================

const categories = ref([]);

const loading = ref(false);
const saving = ref(false);
const deleting = ref(false);

const searchQuery = ref("");

const successMessage = ref("");
const errorMessage = ref("");


// =====================================================
// MODAL STATE
// =====================================================

const showFormModal = ref(false);
const showDeleteModal = ref(false);

const isEditMode = ref(false);

const selectedCategory = ref(null);


// =====================================================
// FORM
// =====================================================

const form = ref({
  name: "",
});

const formError = ref("");


// =====================================================
// FETCH CATEGORIES
// =====================================================

const fetchCategories = async () => {
  loading.value = true;
  errorMessage.value = "";

  try {
    const result = await getCategories();

    console.log("CATEGORY API RESPONSE:", result);

    /*
      Backend response could be:

      [
        {
          id: 1,
          name: "Programming"
        }
      ]

      OR

      {
        data: [
          {
            id: 1,
            name: "Programming"
          }
        ]
      }

      OR

      {
        categories: [...]
      }
    */

    if (Array.isArray(result)) {
      categories.value = result;
    } else if (Array.isArray(result?.data)) {
      categories.value = result.data;
    } else if (Array.isArray(result?.categories)) {
      categories.value = result.categories;
    } else {
      categories.value = [];
    }

  } catch (error) {

    console.error("Fetch categories error:", error);

    errorMessage.value =
      error?.message || "Failed to load categories.";

  } finally {
    loading.value = false;
  }
};


// =====================================================
// FILTER CATEGORY
// =====================================================

const filteredCategories = computed(() => {

  const keyword = searchQuery.value
    .trim()
    .toLowerCase();

  if (!keyword) {
    return categories.value;
  }

  return categories.value.filter((category) => {

    const name = String(
      category?.name || ""
    ).toLowerCase();

    return name.includes(keyword);

  });
});


// =====================================================
// CATEGORY INITIAL
// =====================================================

const getCategoryInitial = (name) => {

  if (!name) {
    return "?";
  }

  return String(name)
    .trim()
    .charAt(0)
    .toUpperCase();
};


// =====================================================
// OPEN ADD MODAL
// =====================================================

const openAddModal = () => {

  isEditMode.value = false;

  selectedCategory.value = null;

  form.value = {
    name: "",
  };

  formError.value = "";

  showFormModal.value = true;
};


// =====================================================
// OPEN EDIT MODAL
// =====================================================

const openEditModal = (category) => {

  isEditMode.value = true;

  selectedCategory.value = category;

  form.value = {
    name: category?.name || "",
  };

  formError.value = "";

  showFormModal.value = true;
};


// =====================================================
// CLOSE FORM MODAL
// =====================================================

const closeFormModal = () => {

  if (saving.value) {
    return;
  }

  showFormModal.value = false;

  formError.value = "";

  selectedCategory.value = null;

  form.value = {
    name: "",
  };
};


// =====================================================
// VALIDATE FORM
// =====================================================

const validateForm = () => {

  formError.value = "";

  const name = form.value.name.trim();

  if (!name) {
    formError.value = "Category name is required.";
    return false;
  }

  if (name.length < 2) {
    formError.value =
      "Category name must be at least 2 characters.";
    return false;
  }

  if (name.length > 100) {
    formError.value =
      "Category name must not exceed 100 characters.";
    return false;
  }

  return true;
};


// =====================================================
// CREATE / UPDATE
// =====================================================

const submitCategory = async () => {

  if (!validateForm()) {
    return;
  }

  saving.value = true;

  errorMessage.value = "";
  successMessage.value = "";

  const categoryData = {
    name: form.value.name.trim(),
  };

  try {

    // =================================================
    // UPDATE
    // =================================================

    if (isEditMode.value) {

      const id = selectedCategory.value?.id;

      if (!id) {
        throw new Error(
          "Category ID is missing."
        );
      }

      const result = await updateCategory(
        id,
        categoryData
      );

      console.log(
        "UPDATE CATEGORY RESPONSE:",
        result
      );

      successMessage.value =
        "Category updated successfully.";

    }

    // =================================================
    // CREATE
    // =================================================

    else {

      const result = await createCategory(
        categoryData
      );

      console.log(
        "CREATE CATEGORY RESPONSE:",
        result
      );

      successMessage.value =
        "Category created successfully.";
    }


    // Close modal
    showFormModal.value = false;

    // Reset
    form.value = {
      name: "",
    };

    selectedCategory.value = null;

    // Refresh list
    await fetchCategories();


    // Auto hide success message
    setTimeout(() => {
      successMessage.value = "";
    }, 3000);

  } catch (error) {

    console.error(
      "Save category error:",
      error
    );

    errorMessage.value =
      error?.message ||
      "Failed to save category.";

  } finally {

    saving.value = false;
  }
};


// =====================================================
// OPEN DELETE MODAL
// =====================================================

const openDeleteModal = (category) => {

  selectedCategory.value = category;

  showDeleteModal.value = true;
};


// =====================================================
// CLOSE DELETE MODAL
// =====================================================

const closeDeleteModal = () => {

  if (deleting.value) {
    return;
  }

  showDeleteModal.value = false;

  selectedCategory.value = null;
};


// =====================================================
// CONFIRM DELETE
// =====================================================

const confirmDelete = async () => {

  const id = selectedCategory.value?.id;

  if (!id) {

    errorMessage.value =
      "Category ID is missing.";

    return;
  }

  deleting.value = true;

  errorMessage.value = "";
  successMessage.value = "";

  try {

    const result = await deleteCategory(id);

    console.log(
      "DELETE CATEGORY RESPONSE:",
      result
    );

    successMessage.value =
      "Category deleted successfully.";

    showDeleteModal.value = false;

    selectedCategory.value = null;

    // Refresh categories
    await fetchCategories();

    setTimeout(() => {
      successMessage.value = "";
    }, 3000);

  } catch (error) {

    console.error(
      "Delete category error:",
      error
    );

    errorMessage.value =
      error?.message ||
      "Failed to delete category.";

  } finally {

    deleting.value = false;
  }
};


// =====================================================
// INITIAL LOAD
// =====================================================

onMounted(() => {
  fetchCategories();
});
</script>