<template>

  <div
    class="bg-white
           border border-slate-200
           rounded-2xl
           p-6"
  >

    <!-- HEADER -->
    <div class="flex items-center gap-3 mb-6">

      <div
        class="w-10 h-10
               rounded-xl
               bg-blue-50
               flex items-center
               justify-center
               text-blue-600"
      >
        👤
      </div>

      <div>

        <h2
          class="text-lg
                 font-bold
                 text-slate-800"
        >
          Borrower Information
        </h2>

        <p
          class="text-sm
                 text-slate-400"
        >
          Enter the borrower's information
        </p>

      </div>

    </div>


    <!-- NAME -->
    <div>

      <label
        class="block
               text-sm
               font-semibold
               text-slate-700
               mb-2"
      >
        Full Name
        <span class="text-red-500">*</span>
      </label>

      <input
        :value="modelValue.name"
        type="text"
        placeholder="Enter borrower's full name"
        @input="update('name', $event.target.value)"
        class="w-full
               px-4 py-3
               border border-slate-200
               rounded-xl
               outline-none
               focus:border-blue-500
               focus:ring-4
               focus:ring-blue-100
               transition"
      />

    </div>


    <!-- EMAIL -->
    <div class="mt-5">

      <label
        class="block
               text-sm
               font-semibold
               text-slate-700
               mb-2"
      >
        Email
        <span class="text-red-500">*</span>
      </label>

      <input
        :value="modelValue.email"
        type="email"
        placeholder="Enter borrower's email"
        @input="update('email', $event.target.value)"
        class="w-full
               px-4 py-3
               border border-slate-200
               rounded-xl
               outline-none
               focus:border-blue-500
               focus:ring-4
               focus:ring-blue-100
               transition"
      />

    </div>


    <!-- PHONE -->
    <div class="mt-5">

      <label
        class="block
               text-sm
               font-semibold
               text-slate-700
               mb-2"
      >
        Phone
        <span
          class="text-xs
                 text-slate-400
                 font-normal"
        >
          (Optional)
        </span>
      </label>

      <input
        :value="modelValue.phone"
        type="text"
        placeholder="Enter phone number"
        @input="update('phone', $event.target.value)"
        class="w-full
               px-4 py-3
               border border-slate-200
               rounded-xl
               outline-none
               focus:border-blue-500
               focus:ring-4
               focus:ring-blue-100
               transition"
      />

    </div>


    <!-- DATES -->
    <div class="grid grid-cols-1 sm:grid-cols-2 gap-4 mt-5">

      <!-- BORROW DATE -->
      <div>

        <label
          class="block
                 text-sm
                 font-semibold
                 text-slate-700
                 mb-2"
        >
          Borrow Date
          <span class="text-red-500">*</span>
        </label>

        <input
          :value="modelValue.borrowDate"
          type="date"
          @input="update('borrowDate', $event.target.value)"
          class="w-full
                 px-3 py-3
                 border border-slate-200
                 rounded-xl
                 outline-none
                 focus:border-blue-500
                 focus:ring-4
                 focus:ring-blue-100"
        />

      </div>


      <!-- DUE DATE -->
      <div>

        <label
          class="block
                 text-sm
                 font-semibold
                 text-slate-700
                 mb-2"
        >
          Due Date
          <span class="text-red-500">*</span>
        </label>

        <input
          :value="modelValue.dueDate"
          type="date"
          :min="modelValue.borrowDate"
          @input="update('dueDate', $event.target.value)"
          class="w-full
                 px-3 py-3
                 border border-slate-200
                 rounded-xl
                 outline-none
                 focus:border-blue-500
                 focus:ring-4
                 focus:ring-blue-100"
        />

      </div>

    </div>


    <!-- QUANTITY -->
    <div class="mt-5">

      <label
        class="block
               text-sm
               font-semibold
               text-slate-700
               mb-2"
      >
        Quantity
        <span class="text-red-500">*</span>
      </label>


      <div class="flex items-center gap-4">

        <div
          class="flex items-center
                 border border-slate-200
                 rounded-xl
                 overflow-hidden"
        >

          <button
            type="button"
            @click="decrease"
            class="w-11 h-11
                   hover:bg-slate-50
                   text-lg
                   font-bold"
          >
            −
          </button>

          <div
            class="w-12
                   h-11
                   flex items-center
                   justify-center
                   border-x
                   border-slate-200
                   font-semibold"
          >
            {{ modelValue.quantity }}
          </div>

          <button
            type="button"
            @click="increase"
            class="w-11 h-11
                   hover:bg-slate-50
                   text-lg
                   font-bold"
          >
            +
          </button>

        </div>


        <span
          class="text-sm
                 text-slate-500"
        >
          Max available:
          <strong class="text-slate-700">
            {{ maxQuantity }}
          </strong>
        </span>

      </div>

    </div>


    <!-- EMAIL NOTICE -->
    <div
      class="mt-6
             p-4
             rounded-xl
             bg-blue-50
             border border-blue-100"
    >

      <div class="flex gap-3">

        <span class="text-blue-600">
          ℹ️
        </span>

        <p
          class="text-sm
                 text-blue-700
                 leading-6"
        >
          A notification email will be sent to the borrower
          with the borrowing details.
        </p>

      </div>

    </div>

  </div>

</template>


<script>
export default {

  name: "BorrowerForm",

  props: {

    modelValue: {
      type: Object,
      required: true
    },

    maxQuantity: {
      type: Number,
      default: 0
    }

  },

  emits: [
    "update:modelValue"
  ],

  methods: {

    update(field, value) {

      this.$emit("update:modelValue", {
        ...this.modelValue,
        [field]: value
      });

    },

    decrease() {

      if (this.modelValue.quantity <= 1) {
        return;
      }

      this.update(
        "quantity",
        this.modelValue.quantity - 1
      );

    },

    increase() {

      if (this.modelValue.quantity >= this.maxQuantity) {
        return;
      }

      this.update(
        "quantity",
        this.modelValue.quantity + 1
      );

    }

  }

};
</script>