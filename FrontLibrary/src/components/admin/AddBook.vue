<template>
  <div class="add-book-page">

    <!-- ================= HEADER ================= -->
    <div class="page-header">
      <div>
        <h1>Add New Book</h1>
        <p>Add a new book to the library</p>
      </div>

      <button
        type="button"
        class="back-btn"
        @click="closePage"
      >
        <i class="bi bi-arrow-left"></i>
        Back
      </button>
    </div>


    <!-- ================= ERROR ================= -->
    <div
      v-if="errorMessage"
      class="error-message"
    >
      <i class="bi bi-exclamation-circle"></i>
      {{ errorMessage }}
    </div>


    <!-- ================= SUCCESS ================= -->
    <div
      v-if="successMessage"
      class="success-message"
    >
      <i class="bi bi-check-circle"></i>
      {{ successMessage }}
    </div>


    <!-- ================= FORM ================= -->
    <form
      class="book-form"
      @submit.prevent="submitBook"
    >

      <!-- TITLE -->
      <div class="form-group">
        <label>
          Book Title
          <span>*</span>
        </label>

        <input
          v-model="form.title"
          type="text"
          placeholder="Enter book title"
          required
        />
      </div>


      <!-- AUTHOR -->
      <div class="form-group">
        <label>
          Author
          <span>*</span>
        </label>

        <input
          v-model="form.author"
          type="text"
          placeholder="Enter author name"
          required
        />
      </div>


      <!-- ISBN -->
      <div class="form-group">
        <label>
          ISBN
          <span>*</span>
        </label>

        <input
          v-model="form.isbn"
          type="text"
          placeholder="Enter ISBN"
          required
        />
      </div>


      <!-- CATEGORY -->
      <div class="form-group">
        <label>
          Category
          <span>*</span>
        </label>

        <select
          v-model="form.categoryId"
          :disabled="loadingCategories"
          required
        >
          <option value="">
            {{
              loadingCategories
                ? "Loading categories..."
                : "Select category"
            }}
          </option>

          <option
            v-for="category in categories"
            :key="category.id"
            :value="category.id"
          >
            {{ category.name }}
          </option>
        </select>

        <small
          v-if="loadingCategories"
          class="help-text"
        >
          Loading categories...
        </small>

        <small
          v-if="
            !loadingCategories &&
            categories.length === 0
          "
          class="warning-text"
        >
          No categories found.
        </small>
      </div>


      <!-- QUANTITY -->
      <div class="form-group">
        <label>
          Quantity
          <span>*</span>
        </label>

        <input
          v-model.number="form.qty"
          type="number"
          min="1"
          placeholder="Enter quantity"
          required
        />
      </div>


      <!-- PAGES -->
      <div class="form-group">
        <label>
          Pages
        </label>

        <input
          v-model.number="form.pages"
          type="number"
          min="1"
          placeholder="Enter number of pages"
        />
      </div>


      <!-- LANGUAGE -->
      <div class="form-group">
        <label>
          Language
        </label>

        <input
          v-model="form.language"
          type="text"
          placeholder="e.g. English"
        />
      </div>


      <!-- STATUS -->
      <div class="form-group">
        <label>
          Status
          <span>*</span>
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


      <!-- DESCRIPTION -->
      <div class="form-group full-width">
        <label>
          Description
        </label>

        <textarea
          v-model="form.description"
          rows="5"
          placeholder="Enter book description"
        ></textarea>
      </div>


      <!-- IMAGE -->
      <div class="form-group full-width">

        <label>
          Book Image
          <span>*</span>
        </label>

        <input
          type="file"
          accept="image/*"
          @change="handleImageChange"
          required
        />

        <small class="help-text">
          Please select a book cover image.
        </small>


        <!-- IMAGE PREVIEW -->
        <div
          v-if="imagePreview"
          class="image-preview"
        >
          <img
            :src="imagePreview"
            alt="Book Preview"
          />

          <button
            type="button"
            class="remove-image"
            @click="removeImage"
          >
            <i class="bi bi-x"></i>
          </button>
        </div>

      </div>


      <!-- CATEGORY PREVIEW -->
      <div
        v-if="selectedCategory"
        class="category-preview"
      >

        <div class="category-icon">
          {{
            getInitial(
              selectedCategory.name
            )
          }}
        </div>

        <div>
          <p class="preview-label">
            Selected Category
          </p>

          <p class="preview-name">
            {{ selectedCategory.name }}
          </p>
        </div>

      </div>


      <!-- BUTTONS -->
      <div class="form-actions">

        <button
          type="button"
          class="cancel-btn"
          @click="closePage"
          :disabled="saving"
        >
          Cancel
        </button>

        <button
          type="submit"
          class="save-btn"
          :disabled="saving"
        >

          <i
            v-if="saving"
            class="bi bi-arrow-repeat spinning"
          ></i>

          {{
            saving
              ? "Saving..."
              : "Save Book"
          }}

        </button>

      </div>

    </form>

  </div>
</template>


<script setup>

import {
  ref,
  computed,
  onMounted,
  onBeforeUnmount
} from "vue";

import {
  getCategories
} from "../../service/categoryService";

import {
  createBook
} from "../../service/bookService";


// =====================================================
// EMITS
// =====================================================

const emit = defineEmits([
  "close",
  "saved"
]);


// =====================================================
// CATEGORIES
// =====================================================

const categories = ref([]);

const loadingCategories = ref(false);


// =====================================================
// FORM
// =====================================================

const form = ref({

  title: "",

  author: "",

  isbn: "",

  categoryId: "",

  qty: 1,

  pages: null,

  language: "",

  description: "",

  status: "Available"

});


// =====================================================
// IMAGE
// =====================================================

const imageFile = ref(null);

const imagePreview = ref("");


// =====================================================
// STATE
// =====================================================

const saving = ref(false);

const errorMessage = ref("");

const successMessage = ref("");


// =====================================================
// FETCH CATEGORIES
// =====================================================

async function fetchCategories() {

  loadingCategories.value = true;

  errorMessage.value = "";

  try {

    const response =
      await getCategories();

    console.log(
      "CATEGORY RESPONSE:",
      response
    );


    if (
      response &&
      Array.isArray(response.data)
    ) {

      categories.value =
        response.data;

    } else if (
      Array.isArray(response)
    ) {

      categories.value =
        response;

    } else {

      categories.value = [];

    }


    console.log(
      "CATEGORIES:",
      categories.value
    );

  } catch (error) {

    console.error(
      "Fetch categories error:",
      error
    );

    errorMessage.value =
      "Failed to load categories.";

  } finally {

    loadingCategories.value = false;

  }

}


// =====================================================
// SELECTED CATEGORY
// =====================================================

const selectedCategory =
  computed(() => {

    if (!form.value.categoryId) {
      return null;
    }

    return categories.value.find(
      category =>
        String(category.id) ===
        String(form.value.categoryId)
    );

  });


// =====================================================
// CATEGORY INITIAL
// =====================================================

function getInitial(name) {

  if (!name) {
    return "?";
  }

  return name
    .trim()
    .charAt(0)
    .toUpperCase();

}


// =====================================================
// IMAGE CHANGE
// =====================================================

function handleImageChange(event) {

  const file =
    event.target.files?.[0];

  if (!file) {

    imageFile.value = null;

    imagePreview.value = "";

    return;

  }


  // Check image
  if (!file.type.startsWith("image/")) {

    errorMessage.value =
      "Please select a valid image file.";

    event.target.value = "";

    return;

  }


  // Optional size check
  if (file.size > 5 * 1024 * 1024) {

    errorMessage.value =
      "Image size must be less than 5MB.";

    event.target.value = "";

    return;

  }


  errorMessage.value = "";

  imageFile.value = file;


  // Create preview
  imagePreview.value =
    URL.createObjectURL(file);


  console.log(
    "BOOK IMAGE:",
    imageFile.value
  );

}


// =====================================================
// REMOVE IMAGE
// =====================================================

function removeImage() {

  if (imagePreview.value) {

    URL.revokeObjectURL(
      imagePreview.value
    );

  }

  imageFile.value = null;

  imagePreview.value = "";

}


// =====================================================
// SUBMIT BOOK
// =====================================================

async function submitBook() {

  errorMessage.value = "";

  successMessage.value = "";


  // ===================================================
  // VALIDATION
  // ===================================================

  if (!form.value.title.trim()) {

    errorMessage.value =
      "Book title is required.";

    return;

  }


  if (!form.value.author.trim()) {

    errorMessage.value =
      "Author is required.";

    return;

  }


  if (!form.value.isbn.trim()) {

    errorMessage.value =
      "ISBN is required.";

    return;

  }


  if (!form.value.categoryId) {

    errorMessage.value =
      "Please select a category.";

    return;

  }


  if (
    !form.value.qty ||
    Number(form.value.qty) < 1
  ) {

    errorMessage.value =
      "Quantity must be at least 1.";

    return;

  }


  if (
    form.value.pages !== null &&
    form.value.pages !== "" &&
    Number(form.value.pages) < 1
  ) {

    errorMessage.value =
      "Pages must be at least 1.";

    return;

  }


  // Backend currently requires image
  if (!imageFile.value) {

    errorMessage.value =
      "Book image is required.";

    return;

  }


  // ===================================================
  // START SAVING
  // ===================================================

  saving.value = true;


  try {

    // =================================================
    // CREATE REQUEST DATA
    // =================================================

    const bookRequest = {

      categoryId:
        Number(
          form.value.categoryId
        ),

      title:
        form.value.title.trim(),

      qty:
        Number(form.value.qty),

      description:
        form.value.description.trim(),

      author:
        form.value.author.trim(),

      pages:
        form.value.pages
          ? Number(form.value.pages)
          : null,

      isbn:
        form.value.isbn.trim(),

      language:
        form.value.language.trim(),

      status:
        form.value.status

    };


    console.log(
      "BOOK REQUEST TO BACKEND:",
      bookRequest
    );

    console.log(
      "BOOK IMAGE:",
      imageFile.value
    );


    // =================================================
    // CALL BACKEND
    // =================================================

    const response =
      await createBook(
        bookRequest,
        imageFile.value
      );


    console.log(
      "CREATE BOOK RESPONSE:",
      response
    );


    // =================================================
    // SUCCESS
    // =================================================

    successMessage.value =
      "Book created successfully!";


    // =================================================
    // GET CREATED BOOK
    // =================================================

    const createdBook =
      response?.data || response;


    // Add category information
    const newBook = {

      ...createdBook,

      categoryId:
        Number(
          form.value.categoryId
        ),

      category:
        selectedCategory.value?.name || "",

      quantity:
        Number(form.value.qty)

    };


    console.log(
      "NEW BOOK:",
      newBook
    );


    // =================================================
    // SEND TO PARENT
    // =================================================

    emit(
      "saved",
      newBook
    );


  } catch (error) {

    console.error(
      "Save book error:",
      error
    );


    // =================================================
    // ERROR HANDLING
    // =================================================

    if (
      error?.response?.data
    ) {

      const backendError =
        error.response.data;


      if (
        typeof backendError ===
        "string"
      ) {

        errorMessage.value =
          backendError;

      } else {

        errorMessage.value =
          backendError.msg ||
          backendError.message ||
          "Failed to create book.";

      }

    } else {

      errorMessage.value =
        error?.message ||
        "Failed to create book.";

    }

  } finally {

    saving.value = false;

  }

}


// =====================================================
// CLOSE
// =====================================================

function closePage() {

  emit("close");

}


// =====================================================
// LOAD
// =====================================================

onMounted(() => {

  fetchCategories();

});


onBeforeUnmount(() => {

  if (imagePreview.value) {

    URL.revokeObjectURL(
      imagePreview.value
    );

  }

});

</script>


<style scoped>

.add-book-page {

  min-height: calc(100vh - 70px);

  padding: 30px;

  background: #f8fafc;

  box-sizing: border-box;

}


/* =====================================================
   HEADER
===================================================== */

.page-header {

  display: flex;

  justify-content: space-between;

  align-items: center;

  margin-bottom: 25px;

}

.page-header h1 {

  margin: 0;

  font-size: 28px;

  font-weight: 700;

  color: #172033;

}

.page-header p {

  margin: 6px 0 0;

  color: #7b8497;

  font-size: 14px;

}


/* =====================================================
   BACK
===================================================== */

.back-btn {

  display: flex;

  align-items: center;

  gap: 8px;

  padding: 10px 18px;

  border: 1px solid #dfe3eb;

  background: white;

  color: #374151;

  border-radius: 8px;

  cursor: pointer;

  font-weight: 600;

}

.back-btn:hover {

  background: #f3f4f6;

}


/* =====================================================
   ERROR
===================================================== */

.error-message {

  display: flex;

  align-items: center;

  gap: 8px;

  margin-bottom: 20px;

  padding: 13px 16px;

  background: #fef2f2;

  color: #dc2626;

  border: 1px solid #fecaca;

  border-radius: 8px;

}


/* =====================================================
   SUCCESS
===================================================== */

.success-message {

  display: flex;

  align-items: center;

  gap: 8px;

  margin-bottom: 20px;

  padding: 13px 16px;

  background: #ecfdf5;

  color: #15803d;

  border: 1px solid #bbf7d0;

  border-radius: 8px;

}


/* =====================================================
   FORM
===================================================== */

.book-form {

  display: grid;

  grid-template-columns:
    1fr
    1fr;

  gap: 22px;

  padding: 30px;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 12px;

  box-shadow:
    0 2px 8px
    rgba(0, 0, 0, 0.04);

}

.form-group {

  display: flex;

  flex-direction: column;

}

.form-group.full-width {

  grid-column: 1 / -1;

}

.form-group label {

  margin-bottom: 8px;

  font-size: 14px;

  font-weight: 600;

  color: #374151;

}

.form-group label span {

  color: #dc2626;

}


/* =====================================================
   INPUT
===================================================== */

.form-group input,
.form-group select,
.form-group textarea {

  width: 100%;

  box-sizing: border-box;

  padding: 11px 13px;

  border:
    1px solid
    #d1d5db;

  border-radius: 8px;

  background: white;

  color: #374151;

  outline: none;

  font-size: 14px;

  transition: 0.2s;

}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {

  border-color: #2563eb;

  box-shadow:
    0 0 0 3px
    rgba(37, 99, 235, 0.1);

}

.form-group textarea {

  resize: vertical;

}


/* =====================================================
   HELP
===================================================== */

.help-text {

  margin-top: 6px;

  color: #6b7280;

  font-size: 12px;

}

.warning-text {

  margin-top: 6px;

  color: #dc2626;

  font-size: 12px;

}


/* =====================================================
   IMAGE PREVIEW
===================================================== */

.image-preview {

  position: relative;

  width: 180px;

  height: 240px;

  margin-top: 15px;

  border:
    1px solid
    #e5e7eb;

  border-radius: 10px;

  overflow: hidden;

  background: #f8fafc;

}

.image-preview img {

  width: 100%;

  height: 100%;

  object-fit: cover;

}

.remove-image {

  position: absolute;

  top: 8px;

  right: 8px;

  width: 32px;

  height: 32px;

  display: flex;

  align-items: center;

  justify-content: center;

  border: none;

  border-radius: 50%;

  background: #dc2626;

  color: white;

  cursor: pointer;

  font-size: 16px;

}

.remove-image:hover {

  background: #b91c1c;

}


/* =====================================================
   CATEGORY PREVIEW
===================================================== */

.category-preview {

  grid-column: 1 / -1;

  display: flex;

  align-items: center;

  gap: 12px;

  padding: 14px;

  background: #eff6ff;

  border:
    1px solid
    #bfdbfe;

  border-radius: 10px;

}

.category-icon {

  width: 42px;

  height: 42px;

  display: flex;

  align-items: center;

  justify-content: center;

  border-radius: 8px;

  background: #2563eb;

  color: white;

  font-weight: 700;

}

.preview-label {

  margin: 0;

  font-size: 12px;

  color: #6b7280;

}

.preview-name {

  margin: 3px 0 0;

  font-size: 15px;

  font-weight: 600;

  color: #1f2937;

}


/* =====================================================
   ACTIONS
===================================================== */

.form-actions {

  grid-column: 1 / -1;

  display: flex;

  justify-content: flex-end;

  gap: 12px;

  padding-top: 20px;

  border-top:
    1px solid
    #e5e7eb;

}

.cancel-btn,
.save-btn {

  padding: 11px 22px;

  border-radius: 8px;

  font-weight: 600;

  cursor: pointer;

}

.cancel-btn {

  border:
    1px solid
    #d1d5db;

  background: white;

  color: #374151;

}

.cancel-btn:hover {

  background: #f3f4f6;

}

.save-btn {

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 8px;

  min-width: 130px;

  border: none;

  background: #2563eb;

  color: white;

}

.save-btn:hover {

  background: #1d4ed8;

}

.save-btn:disabled {

  background: #93c5fd;

  cursor: not-allowed;

}


/* =====================================================
   SPINNING
===================================================== */

.spinning {

  animation:
    spin 1s linear infinite;

}

@keyframes spin {

  from {
    transform: rotate(0deg);
  }

  to {
    transform: rotate(360deg);
  }

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 768px) {

  .add-book-page {

    padding: 20px;

  }

  .page-header {

    align-items: flex-start;

    gap: 15px;

  }

  .book-form {

    grid-template-columns: 1fr;

    padding: 20px;

  }

  .form-group.full-width,
  .category-preview,
  .form-actions {

    grid-column: 1;

  }

}

@media (max-width: 500px) {

  .page-header {

    flex-direction: column;

  }

  .back-btn {

    width: 100%;

    justify-content: center;

  }

  .form-actions {

    flex-direction: column;

  }

  .cancel-btn,
  .save-btn {

    width: 100%;

  }

}

</style>