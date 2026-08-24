<template>
  <tr
    class="border-b border-gray-100 hover:bg-gray-50 transition"
  >

    <!-- =========================
         ID
    ========================== -->
    <td class="px-6 py-4 text-gray-600">
      #{{ item.id }}
    </td>


    <!-- =========================
         USER
    ========================== -->
    <td class="px-6 py-4">

      <div class="flex items-center gap-3">

        <!-- Avatar -->
        <div
          class="w-9 h-9 rounded-full
                 bg-blue-100 text-blue-600
                 flex items-center justify-center
                 font-semibold"
        >
          {{ getInitial(item.user) }}
        </div>

        <!-- User Info -->
        <div>

          <p class="font-medium text-gray-800">
            {{ item.user }}
          </p>

          <p class="text-xs text-gray-400">
            {{ item.email }}
          </p>

        </div>

      </div>

    </td>


    <!-- =========================
         BOOK
    ========================== -->
    <td class="px-6 py-4">

      <span class="font-medium text-gray-800">
        {{ item.book }}
      </span>

    </td>


    <!-- =========================
         BORROW DATE
    ========================== -->
    <td class="px-6 py-4 text-gray-600">
      {{ item.borrowDate }}
    </td>


    <!-- =========================
         DUE DATE
    ========================== -->
    <td class="px-6 py-4">

      <span
        :class="
          item.status === 'Late'
            ? 'text-red-600 font-medium'
            : 'text-gray-600'
        "
      >
        {{ item.dueDate }}
      </span>

    </td>


    <!-- =========================
         RETURN DATE
    ========================== -->
    <td class="px-6 py-4 text-gray-600">

      {{ item.returnDate || "Not returned" }}

    </td>


    <!-- =========================
         FINE
    ========================== -->
    <td class="px-6 py-4">

      <span
        :class="
          Number(item.fine) > 0
            ? 'text-red-600 font-medium'
            : 'text-green-600 font-medium'
        "
      >

        ${{ Number(item.fine || 0).toFixed(2) }}

      </span>

    </td>


    <!-- =========================
         STATUS
    ========================== -->
    <td class="px-6 py-4">

      <span
        class="px-3 py-1 rounded-full
               text-xs font-medium"
        :class="getStatusClass(item.status)"
      >

        {{ item.status }}

      </span>

    </td>


    <!-- =========================
         ACTION
    ========================== -->
    <td class="px-6 py-4">

      <div class="flex justify-center gap-2">

        <!-- VIEW -->
        <button
          type="button"
          @click="emit('view', item)"
          class="w-9 h-9 flex items-center
                 justify-center rounded-lg
                 bg-gray-100 text-gray-600
                 hover:bg-gray-200 transition"
          title="View"
        >

          <i class="bi bi-eye"></i>

        </button>


        <!-- DELETE -->
        <button
          type="button"
          @click="emit('delete', item.id)"
          class="w-9 h-9 flex items-center
                 justify-center rounded-lg
                 bg-red-50 text-red-600
                 hover:bg-red-100 transition"
          title="Delete"
        >

          <i class="bi bi-trash"></i>

        </button>

      </div>

    </td>

  </tr>
</template>


<script setup>

// ========================================
// PROPS
// ========================================

defineProps({

  item: {
    type: Object,
    required: true
  }

})


// ========================================
// EMIT
// ========================================

const emit = defineEmits([
  "view",
  "delete"
])


// ========================================
// USER INITIAL
// ========================================

const getInitial = (name) => {

  if (!name) {
    return "U"
  }

  return name
    .trim()
    .charAt(0)
    .toUpperCase()

}


// ========================================
// STATUS CLASS
// ========================================

const getStatusClass = (status) => {

  if (status === "Returned") {

    return "bg-green-100 text-green-700"

  }


  if (status === "Late") {

    return "bg-red-100 text-red-700"

  }


  if (status === "Borrowed") {

    return "bg-blue-100 text-blue-700"

  }


  return "bg-gray-100 text-gray-700"

}

</script>   