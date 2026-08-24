<template>

  <tr
    class="hover:bg-gray-50 transition duration-150"
  >

    <!-- =================================================
         ID
    ================================================== -->

    <td
      class="px-6 py-4
             text-sm
             text-gray-500
             whitespace-nowrap"
    >
      #{{ category.id }}
    </td>


    <!-- =================================================
         CATEGORY
    ================================================== -->

    <td class="px-6 py-4">

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

    </td>


    <!-- =================================================
         DESCRIPTION
    ================================================== -->

    <td class="px-6 py-4">

      <p
        class="text-sm
               text-gray-500
               max-w-xs
               truncate"
        :title="category.description"
      >
        {{ category.description || "No description available." }}
      </p>

    </td>


    <!-- =================================================
         BOOKS
    ================================================== -->

    <td class="px-6 py-4">

      <span
        class="inline-flex
               items-center
               px-3 py-1
               rounded-full
               text-sm
               font-semibold"
        :class="categoryColor"
      >
        {{ category.books }}
      </span>

    </td>


    <!-- =================================================
         ACTIONS
    ================================================== -->

    <td class="px-6 py-4">

      <div
        class="flex items-center gap-2"
      >

        <!-- Edit -->

        <button
          @click="$emit('edit', category)"
          class="w-8 h-8
                 flex items-center justify-center
                 rounded-lg
                 bg-blue-50
                 text-blue-600
                 hover:bg-blue-100
                 transition"
          title="Edit"
        >

          <i class="bi bi-pencil"></i>

        </button>


        <!-- Delete -->

        <button
          @click="$emit('delete', category.id)"
          class="w-8 h-8
                 flex items-center justify-center
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

</template>


<script setup>

import { computed } from "vue";


// =====================================================
// PROPS
// =====================================================

const props = defineProps({

  category: {

    type: Object,

    required: true

  }

});


// =====================================================
// EMITS
// =====================================================

defineEmits([

  "edit",

  "delete"

]);


// =====================================================
// CATEGORY COLOR
// =====================================================

const categoryColor = computed(() => {

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
    colors[props.category.name]
    || "bg-gray-50 text-gray-600"
  );

});

</script>