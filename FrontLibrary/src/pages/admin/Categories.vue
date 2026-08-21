<template>
  <div class="p-6 bg-gray-50 min-h-screen">

    <!-- ================= HEADER ================= -->
    <div class="flex items-center justify-between mb-6">
      <div>
        <h1 class="text-2xl font-bold text-gray-800">
          Category Management
        </h1>

        <p class="text-gray-500 mt-1">
          Manage book categories
        </p>
      </div>

      <!-- Add Category Button -->
      <button
        @click="openAddModal"
        class="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition"
      >
        <i class="bi bi-plus-lg"></i>
        Add Category
      </button>
    </div>


    <!-- ================= CATEGORY TABLE ================= -->
    <div class="bg-white rounded-xl shadow-sm overflow-hidden">

      <div class="overflow-x-auto">

        <table class="w-full text-left">

          <!-- Table Header -->
          <thead class="bg-gray-50 border-b border-gray-200">
            <tr>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                ID
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Category Name
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Description
              </th>

              <th class="px-6 py-4 text-sm font-semibold text-gray-600">
                Books
              </th>

              <th
                class="px-6 py-4 text-sm font-semibold text-gray-600 text-center"
              >
                Action
              </th>

            </tr>
          </thead>


          <!-- Table Body -->
          <tbody>

            <tr
              v-for="category in categories"
              :key="category.id"
              class="border-b border-gray-100 hover:bg-gray-50 transition"
            >

              <!-- ID -->
              <td class="px-6 py-4 text-gray-600">
                #{{ category.id }}
              </td>


              <!-- Category Name -->
              <td class="px-6 py-4">

                <div class="font-medium text-gray-800">
                  {{ category.name }}
                </div>

              </td>


              <!-- Description -->
              <td class="px-6 py-4 text-gray-500">
                {{ category.description }}
              </td>


              <!-- Books -->
              <td class="px-6 py-4 text-gray-600">
                {{ category.books }}
              </td>


              <!-- Actions -->
              <td class="px-6 py-4">

                <div class="flex justify-center gap-2">

                  <!-- Edit -->
                  <button
                    @click="editCategory(category)"
                    class="w-9 h-9 flex items-center justify-center
                           rounded-lg bg-blue-50 text-blue-600
                           hover:bg-blue-100 transition"
                    title="Edit"
                  >
                    <i class="bi bi-pencil"></i>
                  </button>


                  <!-- Delete -->
                  <button
                    @click="deleteCategory(category.id)"
                    class="w-9 h-9 flex items-center justify-center
                           rounded-lg bg-red-50 text-red-600
                           hover:bg-red-100 transition"
                    title="Delete"
                  >
                    <i class="bi bi-trash"></i>
                  </button>

                </div>

              </td>

            </tr>


            <!-- Empty -->
            <tr v-if="categories.length === 0">

              <td
                colspan="5"
                class="px-6 py-10 text-center text-gray-500"
              >
                No categories found.
              </td>

            </tr>

          </tbody>

        </table>

      </div>

    </div>


    <!-- ================= ADD / EDIT MODAL ================= -->
    <div
      v-if="showModal"
      class="fixed inset-0 bg-black/40 flex items-center justify-center z-50"
    >

      <div class="bg-white w-full max-w-md rounded-xl shadow-xl p-6">

        <!-- Modal Header -->
        <div class="flex items-center justify-between mb-5">

          <h2 class="text-xl font-bold text-gray-800">
            {{ isEditing ? 'Edit Category' : 'Add Category' }}
          </h2>

          <button
            @click="closeModal"
            class="text-gray-400 hover:text-gray-600"
          >
            <i class="bi bi-x-lg"></i>
          </button>

        </div>


        <!-- ================= CATEGORY NAME ================= -->
        <div class="mb-4">

          <label class="block text-sm font-medium text-gray-700 mb-2">
            Category Name
          </label>

          <input
            v-model="form.name"
            type="text"
            placeholder="Enter category name"
            class="w-full px-4 py-2.5 border border-gray-200 rounded-lg
                   focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

        </div>


        <!-- ================= DESCRIPTION ================= -->
        <div class="mb-4">

          <label class="block text-sm font-medium text-gray-700 mb-2">
            Description
          </label>

          <textarea
            v-model="form.description"
            rows="3"
            placeholder="Enter description"
            class="w-full px-4 py-2.5 border border-gray-200 rounded-lg
                   focus:outline-none focus:ring-2 focus:ring-blue-500"
          ></textarea>

        </div>


        <!-- ================= NUMBER OF BOOKS ================= -->
        <div class="mb-6">

          <label class="block text-sm font-medium text-gray-700 mb-2">
            Number of Books
          </label>

          <input
            v-model.number="form.books"
            type="number"
            min="0"
            placeholder="Enter number of books"
            class="w-full px-4 py-2.5 border border-gray-200 rounded-lg
                   focus:outline-none focus:ring-2 focus:ring-blue-500"
          />

        </div>


        <!-- ================= BUTTONS ================= -->
        <div class="flex justify-end gap-3">

          <!-- Cancel -->
          <button
            @click="closeModal"
            class="px-4 py-2 border border-gray-200 rounded-lg
                   text-gray-600 hover:bg-gray-50"
          >
            Cancel
          </button>


          <!-- Save / Update -->
          <button
            @click="saveCategory"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg
                   hover:bg-blue-700"
          >
            {{ isEditing ? 'Update' : 'Save' }}
          </button>

        </div>

      </div>

    </div>

  </div>
</template>


<script setup>
import { ref } from 'vue'


// MODAL

const showModal = ref(false)

const isEditing = ref(false)


// FORM

const form = ref({
  id: null,
  name: '',
  description: '',
  books: 0
})


// CATEGORY DATA

const categories = ref([
  {
    id: 1,
    name: 'Fiction',
    description: 'Fictional stories and novels',
    books: 120
  },

  {
    id: 2,
    name: 'Science',
    description: 'Science and technology books',
    books: 85
  },

  {
    id: 3,
    name: 'History',
    description: 'Historical books and documents',
    books: 64
  },

  {
    id: 4,
    name: 'Programming',
    description: 'Programming and software development',
    books: 95
  },

  {
    id: 5,
    name: 'Business',
    description: 'Business and management books',
    books: 42
  }
])

// OPEN ADD MODAL

const openAddModal = () => {

  isEditing.value = false

  form.value = {
    id: null,
    name: '',
    description: '',
    books: 0
  }

  showModal.value = true
}

// EDIT CATEGORY

const editCategory = (category) => {

  isEditing.value = true

  form.value = {
    ...category
  }

  showModal.value = true
}

// SAVE / UPDATE CATEGORY

const saveCategory = () => {

  // Validate category name
  if (!form.value.name.trim()) {

    alert('Please enter category name.')

    return
  }

  // Validate number of books
  if (form.value.books < 0) {

    alert('Number of books cannot be negative.')

    return
  }

  // ================= EDIT =================

  if (isEditing.value) {

    const index = categories.value.findIndex(
      category => category.id === form.value.id
    )

    if (index !== -1) {

      categories.value[index] = {
        ...form.value,
        books: Number(form.value.books)
      }
    }
  }

  // ================= ADD =================

  else {

    const newCategory = {

      id:
        categories.value.length > 0
          ? Math.max(
              ...categories.value.map(category => category.id)
            ) + 1
          : 1,

      name: form.value.name,

      description: form.value.description,

      books: Number(form.value.books)

    }

    categories.value.push(newCategory)

  }

  // Close modal
  closeModal()
}

// DELETE CATEGORY

const deleteCategory = (id) => {

  const confirmed = confirm(
    'Are you sure you want to delete this category?'
  )

  if (!confirmed) return
  categories.value = categories.value.filter(
    category => category.id !== id
  )
}

// CLOSE MODAL

const closeModal = () => {

  showModal.value = false

}
</script>