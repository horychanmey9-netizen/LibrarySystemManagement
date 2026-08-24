<template>
  <div
    class="category-card bg-white rounded-xl border border-gray-100
           shadow-sm hover:shadow-md transition duration-200 p-5"
  >

    <!-- ================= TOP ================= -->
    <div class="flex items-start justify-between">

      <!-- Icon -->
      <div
        class="w-12 h-12 rounded-xl flex items-center justify-center"
        :class="categoryColor.bg"
      >
        <i
          :class="[
            'bi',
            categoryIcon,
            categoryColor.text
          ]"
          class="text-xl"
        ></i>
      </div>


      <!-- Actions -->
      <div class="flex gap-2">

        <!-- Edit -->
        <button
          @click="$emit('edit', category)"
          class="w-8 h-8 flex items-center justify-center
                 rounded-lg bg-blue-50 text-blue-600
                 hover:bg-blue-100 transition"
          title="Edit"
        >
          <i class="bi bi-pencil"></i>
        </button>


        <!-- Delete -->
        <button
          @click="$emit('delete', category.id)"
          class="w-8 h-8 flex items-center justify-center
                 rounded-lg bg-red-50 text-red-600
                 hover:bg-red-100 transition"
          title="Delete"
        >
          <i class="bi bi-trash"></i>
        </button>

      </div>

    </div>


    <!-- ================= CATEGORY INFO ================= -->
    <div class="mt-5">

      <h3 class="text-lg font-semibold text-gray-800">
        {{ category.name }}
      </h3>

      <p class="text-sm text-gray-500 mt-1 line-clamp-2">
        {{ category.description || 'No description available.' }}
      </p>

    </div>


    <!-- ================= FOOTER ================= -->
    <div
      class="mt-5 pt-4 border-t border-gray-100
             flex items-center justify-between"
    >

      <span class="text-sm text-gray-500">
        Books
      </span>

      <span
        class="text-lg font-bold"
        :class="categoryColor.text"
      >
        {{ category.books }}
      </span>

    </div>

  </div>
</template>


<script setup>

import { computed } from "vue";


/*
|--------------------------------------------------------------------------
| PROPS
|--------------------------------------------------------------------------
*/

const props = defineProps({

  category: {
    type: Object,
    required: true
  }

});


/*
|--------------------------------------------------------------------------
| EMITS
|--------------------------------------------------------------------------
*/

defineEmits([
  "edit",
  "delete"
]);


/*
|--------------------------------------------------------------------------
| CATEGORY ICON
|--------------------------------------------------------------------------
*/

const categoryIcon = computed(() => {

  const icons = {

    Fiction: "bi-book",

    Science: "bi-flask",

    History: "bi-hourglass-split",

    Programming: "bi-code-slash",

    Business: "bi-briefcase",

    Database: "bi-database",

    Networking: "bi-diagram-3",

    Security: "bi-shield-lock",

    "Web Development": "bi-globe",

    Other: "bi-three-dots"

  };


  return icons[props.category.name] || "bi-book";

});


/*
|--------------------------------------------------------------------------
| CATEGORY COLOR
|--------------------------------------------------------------------------
|
| Important:
| We always return bg + text.
| This prevents:
| Cannot read properties of undefined (reading 'bg')
|
*/

const categoryColor = computed(() => {

  const colors = {

    Fiction: {
      bg: "bg-purple-50",
      text: "text-purple-600"
    },

    Science: {
      bg: "bg-green-50",
      text: "text-green-600"
    },

    History: {
      bg: "bg-yellow-50",
      text: "text-yellow-600"
    },

    Programming: {
      bg: "bg-blue-50",
      text: "text-blue-600"
    },

    Business: {
      bg: "bg-orange-50",
      text: "text-orange-600"
    },

    Database: {
      bg: "bg-cyan-50",
      text: "text-cyan-600"
    },

    Networking: {
      bg: "bg-indigo-50",
      text: "text-indigo-600"
    },

    Security: {
      bg: "bg-red-50",
      text: "text-red-600"
    },

    "Web Development": {
      bg: "bg-pink-50",
      text: "text-pink-600"
    },

    Other: {
      bg: "bg-gray-50",
      text: "text-gray-600"
    }

  };


  return (
    colors[props.category.name] || {
      bg: "bg-gray-50",
      text: "text-gray-600"
    }
  );

});

</script>