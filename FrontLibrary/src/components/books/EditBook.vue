<template>

  <div class="edit-book-page">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="page-header">

      <div>

        <h1>
          Edit Book
        </h1>

        <p>
          Update book information
        </p>

      </div>


      <button
        type="button"
        class="back-btn"
        @click="goBack"
        :disabled="saving"
      >

        ← Back

      </button>

    </div>


    <!-- =====================================================
         ERROR MESSAGE
    ====================================================== -->

    <div
      v-if="errorMessage"
      class="alert error-message"
    >

      <span class="alert-icon">
        ⚠
      </span>

      <span>
        {{ errorMessage }}
      </span>

    </div>


    <!-- =====================================================
         SUCCESS MESSAGE
    ====================================================== -->

    <div
      v-if="successMessage"
      class="alert success-message"
    >

      <span class="alert-icon">
        ✓
      </span>

      <span>
        {{ successMessage }}
      </span>

    </div>


    <!-- =====================================================
         LOADING
    ====================================================== -->

    <div
      v-if="loading"
      class="loading-box"
    >

      <div class="spinner"></div>

      <span>
        Loading book information...
      </span>

    </div>


    <!-- =====================================================
         FORM
    ====================================================== -->

    <div
      v-else
      class="form-card"
    >

      <form
        @submit.prevent="updateBook"
      >


        <!-- =================================================
             ROW 1
        ================================================== -->

        <div class="form-row">


          <!-- TITLE -->

          <div class="form-group">

            <label>
              Book Title
              <span class="required">*</span>
            </label>

            <input
              v-model.trim="form.title"
              type="text"
              placeholder="Enter book title"
              required
            />

          </div>


          <!-- AUTHOR -->

          <div class="form-group">

            <label>
              Author
              <span class="required">*</span>
            </label>

            <input
              v-model.trim="form.author"
              type="text"
              placeholder="Enter author name"
              required
            />

          </div>

        </div>


        <!-- =================================================
             ROW 2
        ================================================== -->

        <div class="form-row">


          <!-- CATEGORY -->

          <div class="form-group">

            <label>
              Category
              <span class="required">*</span>
            </label>

            <select
              v-model="form.categoryId"
              required
            >

              <option
                value=""
                disabled
              >
                Select Category
              </option>


              <option
                v-for="category in categories"
                :key="category.id"
                :value="String(category.id)"
              >

                {{ category.name }}

              </option>

            </select>


            <small
              v-if="categoryLoading"
              class="helper-text"
            >

              Loading categories...

            </small>


            <small
              v-if="
                !categoryLoading &&
                categories.length === 0
              "
              class="helper-error"
            >

              No categories found.

            </small>

          </div>


          <!-- ISBN -->

          <div class="form-group">

            <label>
              ISBN
            </label>

            <input
              v-model.trim="form.isbn"
              type="text"
              placeholder="Enter ISBN"
            />

          </div>

        </div>


        <!-- =================================================
             ROW 3
        ================================================== -->

        <div class="form-row">


          <!-- QUANTITY -->

          <div class="form-group">

            <label>
              Quantity
              <span class="required">*</span>
            </label>

            <input
              v-model.number="form.quantity"
              type="number"
              min="0"
              placeholder="Enter quantity"
              required
            />

            <small class="helper-text">

              Current quantity:
              {{ form.quantity }}

            </small>

          </div>


          <!-- STATUS -->

          <div class="form-group">

            <label>
              Status
              <span class="required">*</span>
            </label>

            <select
              v-model="form.status"
              required
            >

              <option value="Available">
                Available
              </option>

              <option value="Borrowed">
                Borrowed
              </option>

              <option value="Overdue">
                Overdue
              </option>

            </select>

          </div>

        </div>


        <!-- =================================================
             BOOK IMAGE
        ================================================== -->

        <div class="form-group full">

          <label>
            Book Image
          </label>


          <!-- OLD IMAGE -->

          <div
            v-if="currentImage"
            class="current-image-section"
          >

            <span class="image-label">
              Current Image
            </span>

            <div class="image-preview-wrapper">

              <img
                :src="currentImage"
                alt="Current book image"
                class="current-image"
                @error="handleImageError"
              />

            </div>

          </div>


          <!-- FILE INPUT -->

          <input
            type="file"
            accept="image/*"
            @change="handleImageChange"
          />


          <small class="helper-text">

            Leave empty to keep the current image.

          </small>


          <!-- NEW IMAGE -->

          <div
            v-if="newImage"
            class="new-image-info"
          >

            <span>
              New image selected:
            </span>

            <strong>
              {{ newImage.name }}
            </strong>

          </div>


          <!-- NEW IMAGE PREVIEW -->

          <div
            v-if="newImagePreview"
            class="new-image-preview-wrapper"
          >

            <span class="image-label">
              New Image Preview
            </span>

            <img
              :src="newImagePreview"
              alt="New book image"
              class="new-image-preview"
            />

          </div>

        </div>


        <!-- =================================================
             DESCRIPTION
        ================================================== -->

        <div class="form-group full">

          <label>
            Description
          </label>

          <textarea
            v-model.trim="form.description"
            rows="6"
            placeholder="Enter book description"
          ></textarea>

        </div>


        <!-- =================================================
             BOOK ID
        ================================================== -->

        <div
          v-if="form.id"
          class="book-info"
        >

          <div class="info-item">

            <span class="info-label">
              Book ID
            </span>

            <strong>
              #{{ form.id }}
            </strong>

          </div>


          <div class="info-item">

            <span class="info-label">
              Category ID
            </span>

            <strong>
              {{ form.categoryId || "-" }}
            </strong>

          </div>


          <div class="info-item">

            <span class="info-label">
              Quantity
            </span>

            <strong>
              {{ form.quantity }}
            </strong>

          </div>

        </div>


        <!-- =================================================
             ACTIONS
        ================================================== -->

        <div class="form-actions">

          <button
            type="button"
            class="cancel-btn"
            @click="goBack"
            :disabled="saving"
          >

            Cancel

          </button>


          <button
            type="submit"
            class="save-btn"
            :disabled="
              saving ||
              categoryLoading
            "
          >

            <span
              v-if="saving"
              class="button-content"
            >

              <span class="button-spinner"></span>

              Updating...

            </span>


            <span
              v-else
              class="button-content"
            >

              ✓

              Save Changes

            </span>

          </button>

        </div>

      </form>

    </div>

  </div>

</template>


<script setup>

import {
  ref,
  watch,
  onMounted,
  onBeforeUnmount
} from "vue";


import {
  updateBook as updateBookApi
} from "../../service/bookService.js";


import {
  getCategories
} from "../../service/categoryService.js";


// =====================================================
// PROPS
// =====================================================

const props = defineProps({

  book: {

    type: Object,

    default: null

  }

});


// =====================================================
// EVENTS
// =====================================================

const emit = defineEmits([

  "close",

  "updated"

]);


// =====================================================
// DATA
// =====================================================

const categories =
  ref([]);


const loading =
  ref(false);


const categoryLoading =
  ref(false);


const saving =
  ref(false);


const errorMessage =
  ref("");


const successMessage =
  ref("");


// =====================================================
// NEW IMAGE
// =====================================================

const newImage =
  ref(null);


const newImagePreview =
  ref("");


// =====================================================
// CURRENT IMAGE
// =====================================================

const currentImage =
  ref("");


// =====================================================
// FORM
// =====================================================

const form =
  ref({

    id: null,

    title: "",

    description: "",

    author: "",

    categoryId: "",

    isbn: "",

    quantity: 0,

    status: "Available"

  });


// =====================================================
// RESET FORM
// =====================================================

function resetForm() {

  form.value = {

    id: null,

    title: "",

    description: "",

    author: "",

    categoryId: "",

    isbn: "",

    quantity: 0,

    status: "Available"

  };


  newImage.value =
    null;


  newImagePreview.value =
    "";


  currentImage.value =
    "";

}


// =====================================================
// CLEAN PREVIEW
// =====================================================

function clearNewImagePreview() {

  if (
    newImagePreview.value
  ) {

    URL.revokeObjectURL(
      newImagePreview.value
    );

  }


  newImagePreview.value =
    "";

}


// =====================================================
// LOAD BOOK
// =====================================================

function loadBook(book) {

  if (!book) {

    resetForm();

    return;

  }


  console.log(
    "================================"
  );

  console.log(
    "EDIT BOOK DATA:",
    book
  );

  console.log(
    "================================"
  );


  // ===================================================
  // CATEGORY
  // ===================================================

  let categoryId = "";


  if (
    book.categoryId !==
      undefined &&
    book.categoryId !== null &&
    book.categoryId !== ""
  ) {

    categoryId =
      String(
        book.categoryId
      );

  }

  else if (
    book.category &&
    book.category.id !==
      undefined &&
    book.category.id !== null
  ) {

    categoryId =
      String(
        book.category.id
      );

  }


  // ===================================================
  // QUANTITY
  // Backend uses qty
  // Frontend form uses quantity
  // ===================================================

  let quantity = 0;


  if (
    book.qty !== undefined &&
    book.qty !== null
  ) {

    quantity =
      Number(book.qty);

  }

  else if (
    book.quantity !==
      undefined &&
    book.quantity !== null
  ) {

    quantity =
      Number(book.quantity);

  }


  if (
    Number.isNaN(quantity)
  ) {

    quantity = 0;

  }


  // ===================================================
  // IMAGE
  // ===================================================

  const image =
    book.image ||
    book.imageUrl ||
    "";


  // ===================================================
  // SET FORM
  // ===================================================

  form.value = {

    id:
      book.id ??
      null,

    title:
      book.title ??
      "",

    description:
      book.description ??
      "",

    author:
      book.author ??
      "",

    categoryId:
      categoryId,

    isbn:
      book.isbn ??
      "",

    quantity:
      quantity,

    status:
      book.status ??
      "Available"

  };


  // ===================================================
  // SET CURRENT IMAGE
  // ===================================================

  currentImage.value =
    image;


  // ===================================================
  // RESET NEW IMAGE
  // ===================================================

  newImage.value =
    null;

  clearNewImagePreview();


  // ===================================================
  // DEBUG
  // ===================================================

  console.log(
    "EDIT FORM:",
    form.value
  );

  console.log(
    "OLD IMAGE:",
    currentImage.value
  );

  console.log(
    "OLD CATEGORY:",
    form.value.categoryId
  );

  console.log(
    "OLD QUANTITY:",
    form.value.quantity
  );

}


// =====================================================
// IMAGE CHANGE
// =====================================================

function handleImageChange(event) {

  const file =
    event.target.files?.[0];


  if (!file) {

    newImage.value =
      null;

    clearNewImagePreview();

    return;

  }


  // ===================================================
  // VALIDATE TYPE
  // ===================================================

  if (
    !file.type.startsWith("image/")
  ) {

    errorMessage.value =
      "Please select a valid image file.";

    event.target.value =
      "";

    newImage.value =
      null;

    clearNewImagePreview();

    return;

  }


  // ===================================================
  // VALIDATE SIZE
  // ===================================================

  const maxSize =
    5 * 1024 * 1024;


  if (
    file.size > maxSize
  ) {

    errorMessage.value =
      "Image size must be less than 5MB.";

    event.target.value =
      "";

    newImage.value =
      null;

    clearNewImagePreview();

    return;

  }


  // ===================================================
  // SET NEW IMAGE
  // ===================================================

  errorMessage.value =
    "";

  newImage.value =
    file;


  clearNewImagePreview();


  newImagePreview.value =
    URL.createObjectURL(
      file
    );


  console.log(
    "NEW IMAGE:",
    file
  );

}


// =====================================================
// IMAGE ERROR
// =====================================================

function handleImageError() {

  console.warn(
    "Failed to load current book image:",
    currentImage.value
  );

}


// =====================================================
// FETCH CATEGORIES
// =====================================================

async function fetchCategories() {

  categoryLoading.value =
    true;


  try {

    const response =
      await getCategories();


    console.log(
      "CATEGORY RESPONSE:",
      response
    );


    let data = [];


    // Backend:
    // [...]
    if (
      Array.isArray(response)
    ) {

      data =
        response;

    }


    // Backend:
    // { data: [...] }
    else if (
      Array.isArray(
        response?.data
      )
    ) {

      data =
        response.data;

    }


    // Backend:
    // { data: { data: [...] } }
    else if (
      Array.isArray(
        response?.data?.data
      )
    ) {

      data =
        response.data.data;

    }


    categories.value =
      data;


    console.log(
      "CATEGORIES:",
      categories.value
    );


    // =================================================
    // IMPORTANT:
    // Do NOT change categoryId here.
    // loadBook() already loaded old category.
    // =================================================

  }
  catch (error) {

    console.error(
      "Failed to load categories:",
      error
    );


    errorMessage.value =
      error?.message ||
      "Failed to load categories.";

  }
  finally {

    categoryLoading.value =
      false;

  }

}


// =====================================================
// UPDATE BOOK
// =====================================================

async function updateBook() {

  errorMessage.value =
    "";

  successMessage.value =
    "";


  // ===================================================
  // VALIDATION
  // ===================================================

  if (!form.value.id) {

    errorMessage.value =
      "Book ID is missing.";

    return;

  }


  if (
    !form.value.title ||
    !form.value.title.trim()
  ) {

    errorMessage.value =
      "Book title is required.";

    return;

  }


  if (
    !form.value.author ||
    !form.value.author.trim()
  ) {

    errorMessage.value =
      "Author is required.";

    return;

  }


  if (
    !form.value.categoryId
  ) {

    errorMessage.value =
      "Please select a category.";

    return;

  }


  if (
    form.value.quantity ===
      null ||
    form.value.quantity ===
      undefined ||
    Number(form.value.quantity) <
      0
  ) {

    errorMessage.value =
      "Quantity cannot be negative.";

    return;

  }


  // ===================================================
  // START SAVING
  // ===================================================

  saving.value =
    true;


  try {

    // =================================================
    // PAYLOAD
    //
    // IMPORTANT:
    // Backend BookRequest uses:
    //
    // qty
    //
    // NOT quantity
    // =================================================

    const payload = {

      title:
        form.value.title.trim(),

      description:
        form.value.description
          ? form.value.description.trim()
          : "",

      author:
        form.value.author.trim(),

      isbn:
        form.value.isbn
          ? form.value.isbn.trim()
          : "",

      categoryId:
        Number(
          form.value.categoryId
        ),

      qty:
        Number(
          form.value.quantity
        ),

      status:
        form.value.status,

      // Only new image.
      // If null -> old image remains.
      file:
        newImage.value ||
        null

    };


    // =================================================
    // DEBUG
    // =================================================

    console.log(
      "================================"
    );

    console.log(
      "UPDATE BOOK ID:",
      form.value.id
    );

    console.log(
      "TITLE:",
      payload.title
    );

    console.log(
      "AUTHOR:",
      payload.author
    );

    console.log(
      "CATEGORY ID:",
      payload.categoryId
    );

    console.log(
      "QUANTITY:",
      payload.qty
    );

    console.log(
      "STATUS:",
      payload.status
    );

    console.log(
      "OLD IMAGE:",
      currentImage.value
    );

    console.log(
      "NEW IMAGE:",
      payload.file
    );

    console.log(
      "================================"
    );


    // =================================================
    // CALL API
    // =================================================

    const response =
      await updateBookApi(
        form.value.id,
        payload
      );


    console.log(
      "UPDATE BOOK RESPONSE:",
      response
    );


    // =================================================
    // SUCCESS
    // =================================================

    successMessage.value =
      "Book updated successfully!";


    // Backend:
    //
    // {
    //   msg: "...",
    //   status: 200,
    //   data: {...}
    // }

    const updatedBook =
      response?.data ||
      response;


    emit(
      "updated",
      updatedBook
    );


    // =================================================
    // CLOSE
    // =================================================

    setTimeout(() => {

      emit(
        "close"
      );

    }, 800);

  }
  catch (error) {

    console.error(
      "UPDATE BOOK ERROR:",
      error
    );


    errorMessage.value =
      error?.message ||
      "Failed to update book.";

  }
  finally {

    saving.value =
      false;

  }

}


// =====================================================
// BACK
// =====================================================

function goBack() {

  if (
    saving.value
  ) {

    return;

  }


  emit(
    "close"
  );

}


// =====================================================
// WATCH BOOK
// =====================================================

watch(

  () =>
    props.book,

  (newBook) => {

    if (newBook) {

      loadBook(
        newBook
      );

    }
    else {

      resetForm();

    }

  },

  {
    immediate: true

  }

);


// =====================================================
// MOUNT
// =====================================================

onMounted(
  async () => {

    loading.value =
      true;


    try {

      await fetchCategories();


      if (
        props.book
      ) {

        loadBook(
          props.book
        );

      }
      else {

        errorMessage.value =
          "Book information not found.";

      }

    }
    catch (error) {

      console.error(
        "EDIT BOOK LOAD ERROR:",
        error
      );

      errorMessage.value =
        error?.message ||
        "Failed to load book information.";

    }
    finally {

      loading.value =
        false;

    }

  }
);


// =====================================================
// BEFORE UNMOUNT
// =====================================================

onBeforeUnmount(() => {

  clearNewImagePreview();

});

</script>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.edit-book-page {

  min-height:
    calc(100vh - 70px);

  padding:
    30px;

  background:
    #f8f9fc;

  font-family:
    Arial,
    sans-serif;

  box-sizing:
    border-box;

}


/* =====================================================
   HEADER
===================================================== */

.page-header {

  display:
    flex;

  align-items:
    center;

  justify-content:
    space-between;

  gap:
    20px;

  margin-bottom:
    25px;

}


.page-header h1 {

  margin:
    0;

  color:
    #172033;

  font-size:
    28px;

  font-weight:
    700;

}


.page-header p {

  margin:
    6px 0 0;

  color:
    #7b8497;

  font-size:
    14px;

}


/* =====================================================
   BACK BUTTON
===================================================== */

.back-btn {

  display:
    flex;

  align-items:
    center;

  gap:
    6px;

  padding:
    11px 18px;

  border:
    1px solid #dfe3eb;

  border-radius:
    8px;

  background:
    white;

  color:
    #667085;

  font-size:
    14px;

  font-weight:
    600;

  cursor:
    pointer;

  transition:
    0.2s ease;

}


.back-btn:hover {

  background:
    #f1f2f6;

  border-color:
    #cfd4df;

}


.back-btn:disabled {

  opacity:
    0.6;

  cursor:
    not-allowed;

}


/* =====================================================
   ALERT
===================================================== */

.alert {

  display:
    flex;

  align-items:
    center;

  gap:
    10px;

  margin-bottom:
    20px;

  padding:
    13px 16px;

  border-radius:
    8px;

  font-size:
    14px;

}


.error-message {

  border:
    1px solid #fecaca;

  background:
    #fef2f2;

  color:
    #dc2626;

}


.success-message {

  border:
    1px solid #bbf7d0;

  background:
    #f0fdf4;

  color:
    #16a34a;

}


.alert-icon {

  font-weight:
    700;

}


/* =====================================================
   LOADING
===================================================== */

.loading-box {

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  gap:
    12px;

  min-height:
    250px;

  background:
    white;

  border:
    1px solid #e5e8ef;

  border-radius:
    12px;

  color:
    #667085;

  font-size:
    14px;

}


.spinner {

  width:
    20px;

  height:
    20px;

  border:
    3px solid #e5e7eb;

  border-top-color:
    #5b3df5;

  border-radius:
    50%;

  animation:
    spin 0.8s linear infinite;

}


@keyframes spin {

  from {

    transform:
      rotate(0deg);

  }

  to {

    transform:
      rotate(360deg);

  }

}


/* =====================================================
   FORM CARD
===================================================== */

.form-card {

  width:
    100%;

  max-width:
    1000px;

  box-sizing:
    border-box;

  padding:
    30px;

  background:
    white;

  border:
    1px solid #e5e8ef;

  border-radius:
    12px;

  box-shadow:
    0 2px 5px
    rgba(0, 0, 0, 0.02);

}


/* =====================================================
   FORM ROW
===================================================== */

.form-row {

  display:
    grid;

  grid-template-columns:
    1fr 1fr;

  gap:
    20px;

  margin-bottom:
    20px;

}


/* =====================================================
   FORM GROUP
===================================================== */

.form-group {

  display:
    flex;

  flex-direction:
    column;

  gap:
    8px;

}


.form-group.full {

  margin-bottom:
    20px;

}


/* =====================================================
   LABEL
===================================================== */

.form-group label {

  color:
    #344054;

  font-size:
    14px;

  font-weight:
    600;

}


.required {

  color:
    #ef4444;

  margin-left:
    2px;

}


/* =====================================================
   INPUT / SELECT / TEXTAREA
===================================================== */

.form-group input,
.form-group select,
.form-group textarea {

  width:
    100%;

  box-sizing:
    border-box;

  padding:
    12px 14px;

  border:
    1px solid #dfe3eb;

  border-radius:
    8px;

  outline:
    none;

  background:
    white;

  color:
    #172033;

  font-family:
    Arial,
    sans-serif;

  font-size:
    14px;

  transition:
    0.2s ease;

}


.form-group input::placeholder,
.form-group textarea::placeholder {

  color:
    #98a2b3;

}


.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {

  border-color:
    #5b3df5;

  box-shadow:
    0 0 0 3px
    rgba(91, 61, 245, 0.08);

}


/* =====================================================
   FILE INPUT
===================================================== */

.form-group input[type="file"] {

  padding:
    10px;

  cursor:
    pointer;

}


.form-group input[type="file"]::file-selector-button {

  margin-right:
    10px;

  padding:
    8px 12px;

  border:
    none;

  border-radius:
    6px;

  background:
    #5b3df5;

  color:
    white;

  cursor:
    pointer;

}


/* =====================================================
   SELECT
===================================================== */

.form-group select {

  cursor:
    pointer;

}


/* =====================================================
   TEXTAREA
===================================================== */

.form-group textarea {

  resize:
    vertical;

  min-height:
    130px;

}


/* =====================================================
   HELPER TEXT
===================================================== */

.helper-text {

  color:
    #667085;

  font-size:
    12px;

}


.helper-error {

  color:
    #ef4444;

  font-size:
    12px;

}


/* =====================================================
   CURRENT IMAGE
===================================================== */

.current-image-section {

  display:
    flex;

  flex-direction:
    column;

  gap:
    8px;

  margin-bottom:
    8px;

}


.image-label {

  color:
    #667085;

  font-size:
    12px;

  font-weight:
    600;

}


.image-preview-wrapper {

  width:
    150px;

  height:
    190px;

  overflow:
    hidden;

  border:
    1px solid #e5e7eb;

  border-radius:
    8px;

  background:
    #f8f9fc;

}


.current-image {

  width:
    100%;

  height:
    100%;

  object-fit:
    cover;

}


/* =====================================================
   NEW IMAGE INFO
===================================================== */

.new-image-info {

  display:
    flex;

  align-items:
    center;

  gap:
    6px;

  padding:
    10px 12px;

  background:
    #f5f3ff;

  border:
    1px solid #ddd6fe;

  border-radius:
    8px;

  color:
    #6d28d9;

  font-size:
    13px;

}


.new-image-info strong {

  overflow:
    hidden;

  text-overflow:
    ellipsis;

  white-space:
    nowrap;

}


/* =====================================================
   NEW IMAGE PREVIEW
===================================================== */

.new-image-preview-wrapper {

  display:
    flex;

  flex-direction:
    column;

  gap:
    8px;

  margin-top:
    8px;

}


.new-image-preview {

  width:
    150px;

  height:
    190px;

  object-fit:
    cover;

  border:
    1px solid #ddd6fe;

  border-radius:
    8px;

}


/* =====================================================
   BOOK INFO
===================================================== */

.book-info {

  display:
    flex;

  align-items:
    center;

  flex-wrap:
    wrap;

  gap:
    25px;

  margin-bottom:
    20px;

  padding:
    12px 15px;

  background:
    #f8f9fc;

  border:
    1px solid #edf0f5;

  border-radius:
    8px;

}


.info-item {

  display:
    flex;

  align-items:
    center;

  gap:
    10px;

}


.info-label {

  color:
    #8a92a3;

  font-size:
    13px;

}


.info-item strong {

  color:
    #344054;

  font-size:
    13px;

}


/* =====================================================
   ACTIONS
===================================================== */

.form-actions {

  display:
    flex;

  align-items:
    center;

  justify-content:
    flex-end;

  gap:
    12px;

  padding-top:
    20px;

  border-top:
    1px solid #edf0f5;

}


/* =====================================================
   CANCEL
===================================================== */

.cancel-btn {

  padding:
    12px 22px;

  border:
    1px solid #dfe3eb;

  border-radius:
    8px;

  background:
    white;

  color:
    #667085;

  font-size:
    14px;

  font-weight:
    600;

  cursor:
    pointer;

  transition:
    0.2s ease;

}


.cancel-btn:hover {

  background:
    #f1f2f6;

}


.cancel-btn:disabled {

  opacity:
    0.6;

  cursor:
    not-allowed;

}


/* =====================================================
   SAVE
===================================================== */

.save-btn {

  min-width:
    145px;

  padding:
    12px 22px;

  border:
    none;

  border-radius:
    8px;

  background:
    #5b3df5;

  color:
    white;

  font-size:
    14px;

  font-weight:
    600;

  cursor:
    pointer;

  transition:
    0.2s ease;

}


.save-btn:hover {

  background:
    #4930d5;

}


.save-btn:disabled {

  opacity:
    0.6;

  cursor:
    not-allowed;

}


/* =====================================================
   BUTTON CONTENT
===================================================== */

.button-content {

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  gap:
    7px;

}


/* =====================================================
   BUTTON SPINNER
===================================================== */

.button-spinner {

  width:
    14px;

  height:
    14px;

  border:
    2px solid
    rgba(255, 255, 255, 0.4);

  border-top-color:
    white;

  border-radius:
    50%;

  animation:
    spin 0.7s linear infinite;

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 768px) {

  .edit-book-page {

    padding:
      20px;

  }


  .page-header {

    flex-direction:
      column;

    align-items:
      flex-start;

  }


  .back-btn {

    width:
      100%;

    justify-content:
      center;

  }


  .form-card {

    padding:
      20px;

  }


  .form-row {

    grid-template-columns:
      1fr;

  }


  .book-info {

    flex-direction:
      column;

    align-items:
      flex-start;

    gap:
      10px;

  }


  .form-actions {

    flex-direction:
      column-reverse;

  }


  .cancel-btn,
  .save-btn {

    width:
      100%;

  }

}

</style>