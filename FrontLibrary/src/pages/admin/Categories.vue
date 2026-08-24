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
         STATISTICS
    ================================================== -->

    <div
      class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4
             gap-4 mb-6"
    >

      <!-- Total Categories -->

      <div
        class="bg-white rounded-xl border border-gray-100
               shadow-sm p-5"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Total Categories
            </p>

            <h2
              class="text-2xl font-bold text-gray-800 mt-1"
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
        class="bg-white rounded-xl border border-gray-100
               shadow-sm p-5"
      >

        <div class="flex items-center justify-between">

          <div>

            <p class="text-sm text-gray-500">
              Total Books
            </p>

            <h2
              class="text-2xl font-bold text-gray-800 mt-1"
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
         CATEGORY TABLE
    ================================================== -->

    <div
      v-if="categories.length > 0"
      class="bg-white rounded-xl border border-gray-100
             shadow-sm overflow-hidden"
    >

      <div class="overflow-x-auto">

        <table class="w-full">

          <!-- TABLE HEADER -->

          <thead class="bg-gray-50 border-b border-gray-100">

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


              <!-- DESCRIPTION -->

              <th
                class="px-6 py-4
                       text-left
                       text-xs
                       font-semibold
                       text-gray-500
                       uppercase
                       tracking-wider"
              >
                Description
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

            <AdminCategory
              v-for="category in categories"
              :key="category.id"
              :category="category"
              @edit="editCategory"
              @delete="deleteCategory"
            />

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

        <!-- Modal Header -->

        <div
          class="flex items-center
                 justify-between mb-5"
        >

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
             NAME
        ================================================== -->

        <div class="mb-4">

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
            class="w-full
                   px-4 py-2.5
                   border border-gray-200
                   rounded-lg
                   focus:outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          />

        </div>


        <!-- =================================================
             DESCRIPTION
        ================================================== -->

        <div class="mb-4">

          <label
            class="block
                   text-sm
                   font-medium
                   text-gray-700
                   mb-2"
          >
            Description
          </label>


          <textarea
            v-model="form.description"
            rows="3"
            placeholder="Enter description"
            class="w-full
                   px-4 py-2.5
                   border border-gray-200
                   rounded-lg
                   focus:outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          ></textarea>

        </div>


        <!-- =================================================
             NUMBER OF BOOKS
        ================================================== -->

        <div class="mb-6">

          <label
            class="block
                   text-sm
                   font-medium
                   text-gray-700
                   mb-2"
          >
            Number of Books
          </label>


          <input
            v-model.number="form.books"
            type="number"
            min="0"
            placeholder="Enter number of books"
            class="w-full
                   px-4 py-2.5
                   border border-gray-200
                   rounded-lg
                   focus:outline-none
                   focus:ring-2
                   focus:ring-blue-500"
          />

        </div>


        <!-- =================================================
             BUTTONS
        ================================================== -->

        <div class="flex justify-end gap-3">

          <!-- Cancel -->

          <button
            @click="closeModal"
            class="px-4 py-2
                   border border-gray-200
                   rounded-lg
                   text-gray-600
                   hover:bg-gray-50"
          >
            Cancel
          </button>


          <!-- Save / Update -->

          <button
            @click="saveCategory"
            class="px-4 py-2
                   bg-blue-600
                   text-white
                   rounded-lg
                   hover:bg-blue-700"
          >
            {{ isEditing ? "Update" : "Save" }}
          </button>

        </div>

      </div>

    </div>

  </div>

</template>


<script setup>

import { ref, computed } from "vue";


// =====================================================
// COMPONENT
// =====================================================

import AdminCategory
  from "../../components/admin/AdminCategory.vue";


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

  name: "",

  description: "",

  books: 0

});


// =====================================================
// CATEGORY DATA
// =====================================================

const categories = ref([

  {
    id: 1,
    name: "Fiction",
    description: "Fictional stories and novels",
    books: 120
  },

  {
    id: 2,
    name: "Science",
    description: "Science and technology books",
    books: 85
  },

  {
    id: 3,
    name: "History",
    description: "Historical books and documents",
    books: 64
  },

  {
    id: 4,
    name: "Programming",
    description: "Programming and software development",
    books: 95
  },

  {
    id: 5,
    name: "Business",
    description: "Business and management books",
    books: 42
  }

]);


// =====================================================
// TOTAL BOOKS
// =====================================================

const totalBooks = computed(() => {

  return categories.value.reduce(
    (total, category) => {

      return total + Number(category.books || 0);

    },
    0
  );

});


// =====================================================
// OPEN ADD
// =====================================================

function openAddModal() {

  isEditing.value = false;

  form.value = {

    id: null,

    name: "",

    description: "",

    books: 0

  };

  showModal.value = true;

}


// =====================================================
// EDIT
// =====================================================

function editCategory(category) {

  isEditing.value = true;

  form.value = {

    ...category

  };

  showModal.value = true;

}


// =====================================================
// SAVE / UPDATE
// =====================================================

function saveCategory() {

  const name = form.value.name.trim();


  // ===================================================
  // VALIDATION
  // ===================================================

  if (!name) {

    alert("Please enter category name.");

    return;

  }


  if (Number(form.value.books) < 0) {

    alert("Number of books cannot be negative.");

    return;

  }


  // ===================================================
  // EDIT
  // ===================================================

  if (isEditing.value) {

    const index =
      categories.value.findIndex(
        category =>
          category.id === form.value.id
      );


    if (index !== -1) {

      categories.value[index] = {

        ...form.value,

        name,

        books: Number(form.value.books)

      };

    }

  }


  // ===================================================
  // ADD
  // ===================================================

  else {

    const newId =
      categories.value.length > 0
        ? Math.max(
            ...categories.value.map(
              category => category.id
            )
          ) + 1
        : 1;


    categories.value.push({

      id: newId,

      name,

      description:
        form.value.description.trim(),

      books: Number(form.value.books)

    });

  }


  closeModal();

}


// =====================================================
// DELETE
// =====================================================

function deleteCategory(id) {

  const confirmed = window.confirm(
    "Are you sure you want to delete this category?"
  );


  if (!confirmed) {

    return;

  }


  categories.value =
    categories.value.filter(
      category =>
        category.id !== id
    );

}


// =====================================================
// CLOSE
// =====================================================

function closeModal() {

  showModal.value = false;

}

</script>