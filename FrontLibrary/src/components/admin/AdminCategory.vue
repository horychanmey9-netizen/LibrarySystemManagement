<template>
  <div class="min-h-screen bg-gray-50 p-6">

    <!-- =================================================
         HEADER
    ================================================== -->

    <div class="flex items-center justify-between mb-6">

      <div>
        <h1 class="text-2xl font-bold text-gray-800">
          Category Management
        </h1>

        <p class="text-gray-500 mt-1">
          Manage book categories
        </p>
      </div>

      <!-- Add Category -->

      <button
        @click="openAddModal"
        class="flex items-center gap-2
               px-4 py-2.5
               bg-blue-600
               text-white
               rounded-lg
               hover:bg-blue-700
               transition"
      >
        <i class="bi bi-plus-lg"></i>

        <span>
          Add Category
        </span>
      </button>

    </div>


    <!-- =================================================
         ERROR MESSAGE
    ================================================== -->

    <div
      v-if="error"
      class="mb-6 p-4 rounded-lg
             bg-red-50 border border-red-200
             text-red-600 flex items-center gap-3"
    >
      <i class="bi bi-exclamation-circle"></i>

      <span>
        {{ error }}
      </span>

      <button
        @click="fetchCategories"
        class="ml-auto text-sm font-medium
               underline hover:no-underline"
      >
        Retry
      </button>
    </div>


    <!-- =================================================
         STATISTICS
    ================================================== -->

    <div
      class="grid grid-cols-1 sm:grid-cols-2
             lg:grid-cols-4 gap-4 mb-6"
    >

      <!-- Total Categories -->

      <div
        class="bg-white rounded-xl
               border border-gray-100
               shadow-sm p-5"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Total Categories
            </p>

            <h2
              class="text-2xl font-bold
                     text-gray-800 mt-1"
            >
              {{ categories.length }}
            </h2>

          </div>

          <div
            class="w-11 h-11 rounded-lg
                   bg-blue-50
                   text-blue-600
                   flex items-center justify-center"
          >
            <i class="bi bi-grid text-xl"></i>
          </div>

        </div>

      </div>


      <!-- Total Books -->

      <div
        class="bg-white rounded-xl
               border border-gray-100
               shadow-sm p-5"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Total Books
            </p>

            <h2
              class="text-2xl font-bold
                     text-gray-800 mt-1"
            >
              {{ totalBooks }}
            </h2>

          </div>

          <div
            class="w-11 h-11 rounded-lg
                   bg-green-50
                   text-green-600
                   flex items-center justify-center"
          >
            <i class="bi bi-book text-xl"></i>
          </div>

        </div>

      </div>

    </div>


    <!-- =================================================
         LOADING
    ================================================== -->

    <div
      v-if="loading"
      class="bg-white rounded-xl
             border border-gray-100
             shadow-sm p-12 text-center"
    >

      <div
        class="w-10 h-10
               border-4 border-gray-200
               border-t-blue-600
               rounded-full
               animate-spin
               mx-auto"
      ></div>

      <p class="mt-4 text-gray-500">
        Loading categories...
      </p>

    </div>


    <!-- =================================================
         CATEGORY TABLE
    ================================================== -->

    <div
      v-else-if="categories.length > 0"
      class="bg-white rounded-xl
             border border-gray-100
             shadow-sm overflow-hidden"
    >

      <div class="overflow-x-auto">

        <table class="w-full">

          <!-- TABLE HEADER -->

          <thead
            class="bg-gray-50
                   border-b border-gray-100"
          >

            <tr>

              <!-- ID -->

              <th
                class="px-6 py-4
                       text-left
                       text-xs
                       font-semibold
                       text-gray-500
                       uppercase
                       tracking-wider"
              >
                #
              </th>


              <!-- CATEGORY -->

              <th
                class="px-6 py-4
                       text-left
                       text-xs
                       font-semibold
                       text-gray-500
                       uppercase
                       tracking-wider"
              >
                Category
              </th>


              <!-- BOOKS -->

              <th
                class="px-6 py-4
                       text-left
                       text-xs
                       font-semibold
                       text-gray-500
                       uppercase
                       tracking-wider"
              >
                Books
              </th>


              <!-- CREATED -->

              <th
                class="px-6 py-4
                       text-left
                       text-xs
                       font-semibold
                       text-gray-500
                       uppercase
                       tracking-wider"
              >
                Created
              </th>


              <!-- ACTIONS -->

              <th
                class="px-6 py-4
                       text-left
                       text-xs
                       font-semibold
                       text-gray-500
                       uppercase
                       tracking-wider"
              >
                Actions
              </th>

            </tr>

          </thead>


          <!-- TABLE BODY -->

          <tbody class="divide-y divide-gray-100">

            <tr
              v-for="category in categories"
              :key="category.id"
              class="hover:bg-gray-50
                     transition duration-150"
            >

              <!-- ID -->

              <td
                class="px-6 py-4
                       text-sm text-gray-500
                       whitespace-nowrap"
              >
                #{{ category.id }}
              </td>


              <!-- CATEGORY -->

              <td class="px-6 py-4">

                <div class="flex items-center gap-3">

                  <div
                    class="w-10 h-10
                           rounded-lg
                           bg-blue-50
                           text-blue-600
                           flex items-center
                           justify-center"
                  >
                    <i class="bi bi-folder"></i>
                  </div>

                  <div>

                    <p
                      class="font-semibold
                             text-gray-800"
                    >
                      {{ category.name }}
                    </p>

                    <p
                      class="text-xs
                             text-gray-400
                             mt-0.5"
                    >
                      Book Category
                    </p>

                  </div>

                </div>

              </td>


              <!-- BOOKS -->

              <td class="px-6 py-4">

                <span
                  class="inline-flex
                         items-center
                         px-3 py-1
                         rounded-full
                         text-sm
                         font-semibold"
                  :class="getCategoryColor(category.name)"
                >
                  {{ getBookCount(category) }}
                </span>

              </td>


              <!-- CREATED -->

              <td
                class="px-6 py-4
                       text-sm text-gray-500
                       whitespace-nowrap"
              >
                {{ formatDate(category.createdAt) }}
              </td>


              <!-- ACTIONS -->

              <td class="px-6 py-4">

                <div
                  class="flex items-center gap-2"
                >

                  <!-- EDIT -->

                  <button
                    @click="editCategory(category)"
                    class="w-8 h-8
                           flex items-center
                           justify-center
                           rounded-lg
                           bg-blue-50
                           text-blue-600
                           hover:bg-blue-100
                           transition"
                    title="Edit"
                  >
                    <i class="bi bi-pencil"></i>
                  </button>


                  <!-- DELETE -->

                  <button
                    @click="deleteCategory(category.id)"
                    class="w-8 h-8
                           flex items-center
                           justify-center
                           rounded-lg
                           bg-red-50
                           text-red-600
                           hover:bg-red-100
                           transition"
                    title="Delete"
                  >
                    <i class="bi bi-trash"></i>
                  </button>

                </div>

              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>


    <!-- =================================================
         EMPTY STATE
    ================================================== -->

    <div
      v-else
      class="bg-white rounded-xl
             border border-gray-100
             p-12 text-center"
    >

      <i
        class="bi bi-folder-x
               text-5xl text-gray-300"
      ></i>

      <h3
        class="text-lg font-semibold
               text-gray-700 mt-4"
      >
        No categories found
      </h3>

      <p class="text-gray-500 mt-1">
        Add your first book category.
      </p>

      <button
        @click="openAddModal"
        class="mt-4 px-4 py-2
               bg-blue-600
               text-white
               rounded-lg
               hover:bg-blue-700"
      >
        Add Category
      </button>

    </div>


    <!-- =================================================
         MODAL
    ================================================== -->

    <div
      v-if="showModal"
      class="fixed inset-0
             bg-black/40
             flex items-center justify-center
             z-50 p-4"
      @click.self="closeModal"
    >

      <div
        class="bg-white
               w-full max-w-md
               rounded-xl
               shadow-xl
               p-6"
      >

        <!-- MODAL HEADER -->

        <div
          class="flex items-center
                 justify-between mb-5"
        >

          <div>

            <h2
              class="text-xl font-bold
                     text-gray-800"
            >
              {{
                isEditing
                  ? "Edit Category"
                  : "Add Category"
              }}
            </h2>

            <p
              class="text-sm
                     text-gray-500 mt-1"
            >
              {{
                isEditing
                  ? "Update category information"
                  : "Create a new book category"
              }}
            </p>

          </div>


          <button
            @click="closeModal"
            class="w-8 h-8
                   flex items-center
                   justify-center
                   rounded-lg
                   text-gray-400
                   hover:bg-gray-100
                   hover:text-gray-600"
          >
            <i class="bi bi-x-lg"></i>
          </button>

        </div>


        <!-- =================================================
             CATEGORY NAME
        ================================================== -->

        <div class="mb-6">

          <label
            class="block
                   text-sm
                   font-medium
                   text-gray-700
                   mb-2"
          >
            Category Name
          </label>

          <input
            v-model="form.name"
            type="text"
            placeholder="Enter category name"
            maxlength="100"
            @keyup.enter="saveCategory"
            class="w-full
                   px-4 py-2.5
                   border border-gray-200
                   rounded-lg
                   focus:outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          />

          <p
            v-if="formError"
            class="text-sm
                   text-red-500
                   mt-2"
          >
            {{ formError }}
          </p>

        </div>


        <!-- =================================================
             BUTTONS
        ================================================== -->

        <div
          class="flex justify-end
                 gap-3"
        >

          <!-- CANCEL -->

          <button
            @click="closeModal"
            :disabled="saving"
            class="px-4 py-2
                   border border-gray-200
                   rounded-lg
                   text-gray-600
                   hover:bg-gray-50
                   disabled:opacity-50"
          >
            Cancel
          </button>


          <!-- SAVE -->

          <button
            @click="saveCategory"
            :disabled="saving"
            class="px-4 py-2
                   bg-blue-600
                   text-white
                   rounded-lg
                   hover:bg-blue-700
                   disabled:opacity-50
                   flex items-center gap-2"
          >

            <i
              v-if="saving"
              class="bi bi-arrow-repeat animate-spin"
            ></i>

            <span>
              {{
                saving
                  ? "Saving..."
                  : isEditing
                    ? "Update"
                    : "Save"
              }}
            </span>

          </button>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from "vue";


// =====================================================
// CATEGORY SERVICE
// =====================================================

import {
  getCategories,
  createCategory,
  updateCategory,
  deleteCategory as deleteCategoryApi
} from "../../service/categoryService";


// =====================================================
// STATE
// =====================================================

const categories = ref([]);

const loading = ref(false);

const saving = ref(false);

const error = ref("");


// =====================================================
// MODAL
// =====================================================

const showModal = ref(false);

const isEditing = ref(false);


// =====================================================
// FORM
// =====================================================

const form = ref({
  id: null,
  name: ""
});

const formError = ref("");


// =====================================================
// TOTAL BOOKS
// =====================================================

const totalBooks = computed(() => {

  return categories.value.reduce(
    (total, category) => {

      return total + getBookCount(category);

    },
    0
  );

});


// =====================================================
// ON MOUNTED
// =====================================================

onMounted(() => {

  fetchCategories();

});


// =====================================================
// FETCH CATEGORIES
// =====================================================

async function fetchCategories() {

  loading.value = true;

  error.value = "";

  try {

    const result = await getCategories();

    console.log(
      "CATEGORY API RESPONSE:",
      result
    );


    /*
     * Backend response may be:
     *
     * {
     *   status: 200,
     *   msg: "...",
     *   data: [...]
     * }
     *
     * OR directly:
     *
     * [...]
     */

    const data =
      result?.data ?? result;


    if (Array.isArray(data)) {

      categories.value = data;

    } else {

      categories.value = [];

      console.warn(
        "Category data is not an array:",
        data
      );

    }

  } catch (err) {

    console.error(
      "Fetch categories error:",
      err
    );

    error.value =
      err.message ||
      "Failed to load categories.";

  } finally {

    loading.value = false;

  }

}


// =====================================================
// GET BOOK COUNT
// =====================================================

function getBookCount(category) {

  if (
    Array.isArray(category?.books)
  ) {

    return category.books.length;

  }


  if (
    category?.bookCount !== undefined &&
    category?.bookCount !== null
  ) {

    return Number(
      category.bookCount
    );

  }


  if (
    category?.booksCount !== undefined &&
    category?.booksCount !== null
  ) {

    return Number(
      category.booksCount
    );

  }


  return 0;

}


// =====================================================
// OPEN ADD MODAL
// =====================================================

function openAddModal() {

  isEditing.value = false;

  formError.value = "";

  form.value = {
    id: null,
    name: ""
  };

  showModal.value = true;

}


// =====================================================
// EDIT CATEGORY
// =====================================================

function editCategory(category) {

  isEditing.value = true;

  formError.value = "";

  form.value = {
    id: category.id,
    name: category.name || ""
  };

  showModal.value = true;

}


// =====================================================
// SAVE CATEGORY
// =====================================================

async function saveCategory() {

  formError.value = "";

  const name =
    form.value.name.trim();


  // ===================================================
  // VALIDATION
  // ===================================================

  if (!name) {

    formError.value =
      "Please enter category name.";

    return;

  }


  if (name.length < 2) {

    formError.value =
      "Category name must be at least 2 characters.";

    return;

  }


  saving.value = true;


  try {

    // =================================================
    // UPDATE
    // =================================================

    if (isEditing.value) {

      await updateCategory(
        form.value.id,
        {
          name
        }
      );

      alert(
        "Category updated successfully."
      );

    }

    // =================================================
    // CREATE
    // =================================================

    else {

      await createCategory({
        name
      });

      alert(
        "Category created successfully."
      );

    }


    // =================================================
    // REFRESH DATA
    // =================================================

    await fetchCategories();


    // =================================================
    // CLOSE
    // =================================================

    closeModal();

  } catch (err) {

    console.error(
      "Save category error:",
      err
    );

    formError.value =
      err.message ||
      "Failed to save category.";

  } finally {

    saving.value = false;

  }

}


// =====================================================
// DELETE CATEGORY
// =====================================================

async function deleteCategory(id) {

  const confirmed =
    window.confirm(
      "Are you sure you want to delete this category?"
    );


  if (!confirmed) {

    return;

  }


  try {

    await deleteCategoryApi(id);

    alert(
      "Category deleted successfully."
    );


    // Refresh from backend

    await fetchCategories();

  } catch (err) {

    console.error(
      "Delete category error:",
      err
    );

    alert(
      err.message ||
      "Failed to delete category."
    );

  }

}


// =====================================================
// CLOSE MODAL
// =====================================================

function closeModal() {

  if (saving.value) {

    return;

  }

  showModal.value = false;

  formError.value = "";

}


// =====================================================
// FORMAT DATE
// =====================================================

function formatDate(date) {

  if (!date) {

    return "-";

  }


  try {

    return new Date(date)
      .toLocaleDateString(
        "en-US",
        {
          year: "numeric",
          month: "short",
          day: "numeric"
        }
      );

  } catch {

    return "-";

  }

}


// =====================================================
// CATEGORY COLOR
// =====================================================

function getCategoryColor(name) {

  const colors = {

    Fiction:
      "bg-purple-50 text-purple-600",

    Science:
      "bg-green-50 text-green-600",

    History:
      "bg-yellow-50 text-yellow-600",

    Programming:
      "bg-blue-50 text-blue-600",

    Business:
      "bg-orange-50 text-orange-600",

    Database:
      "bg-cyan-50 text-cyan-600",

    Networking:
      "bg-indigo-50 text-indigo-600",

    Security:
      "bg-red-50 text-red-600",

    "Web Development":
      "bg-pink-50 text-pink-600",

    Other:
      "bg-gray-50 text-gray-600"

  };


  return (
    colors[name] ||
    "bg-gray-50 text-gray-600"
  );

}

</script>