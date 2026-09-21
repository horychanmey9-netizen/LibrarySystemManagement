
<template>

  <div class="add-book-page">

    <!-- =====================================================
         HEADER
    ====================================================== -->

    <div class="page-header">

      <div>
        <h2>Add New Book</h2>

        <p>
          Add a new book to the library collection.
        </p>
      </div>

      <button
        type="button"
        class="back-btn"
        @click="handleCancel"
      >
        <i class="bi bi-arrow-left"></i>
        Back
      </button>

    </div>


    <!-- =====================================================
         ERROR MESSAGE
    ====================================================== -->

    <div
      v-if="errorMessage"
      class="alert-message error"
    >

      <i class="bi bi-exclamation-circle-fill"></i>

      <span>
        {{ errorMessage }}
      </span>

    </div>


    <!-- =====================================================
         SUCCESS MESSAGE
    ====================================================== -->

    <div
      v-if="successMessage"
      class="alert-message success"
    >

      <i class="bi bi-check-circle-fill"></i>

      <span>
        {{ successMessage }}
      </span>

    </div>


    <!-- =====================================================
         FORM
    ====================================================== -->

    <form
      class="book-form"
      @submit.prevent="submitForm"
    >

      <!-- ===================================================
           BOOK INFORMATION
      ==================================================== -->

      <section class="form-card">

        <div class="section-header">

          <div class="section-icon">
            <i class="bi bi-book"></i>
          </div>

          <div>

            <h3>
              Book Information
            </h3>

            <p>
              Enter the basic information about the book.
            </p>

          </div>

        </div>


        <!-- =================================================
             FORM GRID
        ================================================== -->

        <div class="form-grid">

          <!-- BOOK TITLE -->

          <div class="form-group">

            <label for="title">

              Book Title

              <span>*</span>

            </label>

            <input
              id="title"
              v-model="form.title"
              type="text"
              placeholder="Enter book title"
              required
            />

          </div>


          <!-- AUTHOR -->

          <div class="form-group">

            <label for="author">

              Author

              <span>*</span>

            </label>

            <input
              id="author"
              v-model="form.author"
              type="text"
              placeholder="Enter author name"
              required
            />

          </div>


          <!-- ISBN -->

          <div class="form-group">

            <label for="isbn">

              ISBN

              <span>*</span>

            </label>

            <input
              id="isbn"
              v-model="form.isbn"
              type="text"
              placeholder="Enter ISBN"
              required
            />

          </div>


          <!-- CATEGORY -->

          <div class="form-group">

            <label for="category">

              Category

              <span>*</span>

            </label>

            <select
              id="category"
              v-model="form.categoryId"
              required
            >

              <option value="">
                Select category
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
              class="field-hint"
            >
              Loading categories...
            </small>


            <small
              v-else-if="categories.length === 0"
              class="field-hint warning"
            >
              No categories found.
            </small>

          </div>


          <!-- LANGUAGE -->

          <div class="form-group">

            <label for="language">

              Language

              <span>*</span>

            </label>

            <select
              id="language"
              v-model="form.language"
              required
            >

              <option value="">
                Select language
              </option>

              <option value="Khmer">
                Khmer
              </option>

              <option value="French">
                French
              </option>

              <option value="English">
                English
              </option>

            </select>

          </div>


          <!-- QUANTITY -->

          <div class="form-group">

            <label for="qty">

              Quantity

              <span>*</span>

            </label>

            <input
              id="qty"
              v-model.number="form.qty"
              type="number"
              min="1"
              placeholder="Enter quantity"
              required
            />

          </div>


          <!-- PAGES -->

          <div class="form-group">

            <label for="pages">
              Pages
            </label>

            <input
              id="pages"
              v-model.number="form.pages"
              type="number"
              min="1"
              placeholder="Enter number of pages"
            />

          </div>


          <!-- PUBLICATION DATE -->

          <div class="form-group">

            <label for="publishYear">

              Publication Date

              <span>*</span>

            </label>

            <input
              id="publishYear"
              v-model="form.publishYear"
              type="date"
              :min="minPublicationDate"
              :max="maxPublicationDate"
              required
            />

            <small class="field-hint">
              Example: 2026-09-21
            </small>

          </div>

        </div>

      </section>


      <!-- ===================================================
           BOOK DESCRIPTION
      ==================================================== -->

      <section class="form-card">

        <div class="section-header">

          <div class="section-icon">
            <i class="bi bi-card-text"></i>
          </div>

          <div>

            <h3>
              Description
            </h3>

            <p>
              Add a short description about the book.
            </p>

          </div>

        </div>


        <div class="form-group">

          <label for="description">
            Description
          </label>

          <textarea
            id="description"
            v-model="form.description"
            rows="6"
            placeholder="Enter book description..."
          ></textarea>

        </div>

      </section>


      <!-- ===================================================
           BOOK IMAGE
      ==================================================== -->

      <section class="form-card">

        <div class="section-header">

          <div class="section-icon">
            <i class="bi bi-image"></i>
          </div>

          <div>

            <h3>
              Book Cover
            </h3>

            <p>
              Upload a cover image for the book.
            </p>

          </div>

        </div>


        <div class="image-upload-area">

          <!-- IMAGE PREVIEW -->

          <div
            v-if="imagePreview"
            class="image-preview"
          >

            <img
              :src="imagePreview"
              alt="Book cover preview"
            />


            <button
              type="button"
              class="remove-image-btn"
              @click="removeImage"
            >

              <i class="bi bi-x-lg"></i>

            </button>

          </div>


          <!-- UPLOAD -->

          <label
            v-else
            for="bookImage"
            class="upload-box"
          >

            <div class="upload-icon">

              <i class="bi bi-cloud-arrow-up"></i>

            </div>


            <h4>
              Upload Book Cover
            </h4>


            <p>
              Click to choose an image
            </p>


            <span>
              PNG, JPG or JPEG
            </span>

          </label>


          <input
            id="bookImage"
            ref="fileInput"
            type="file"
            accept="image/png,image/jpeg,image/jpg"
            hidden
            @change="handleImageChange"
          />

        </div>

      </section>


      <!-- ===================================================
           FORM ACTIONS
      ==================================================== -->

      <div class="form-actions">

        <button
          type="button"
          class="cancel-btn"
          :disabled="submitting"
          @click="handleCancel"
        >

          Cancel

        </button>


        <button
          type="submit"
          class="submit-btn"
          :disabled="submitting"
        >

          <i
            v-if="submitting"
            class="bi bi-arrow-repeat spin"
          ></i>

          <i
            v-else
            class="bi bi-plus-lg"
          ></i>


          {{
            submitting
              ? "Adding Book..."
              : "Add Book"
          }}

        </button>

      </div>

    </form>

  </div>

</template>


<script setup>

import {
  computed,
  onMounted,
  ref,
  onBeforeUnmount
} from "vue";


import {
  createBook
} from "../../service/bookService";


import {
  getCategories
} from "../../service/categoryService";



/* =========================================================
   EMITS
========================================================= */

const emit = defineEmits([
  "saved",
  "close"
]);



/* =========================================================
   FORM
========================================================= */

const form = ref({

  title: "",

  author: "",

  isbn: "",

  categoryId: "",

  qty: 1,

  pages: null,

  publishYear: "",

  language: "",

  description: "",

  /*
   * Status is automatically Available.
   * It is not displayed in the form.
   */

  status: "Available"

});



/* =========================================================
   STATE
========================================================= */

const categories = ref([]);

const loadingCategories = ref(false);

const submitting = ref(false);

const errorMessage = ref("");

const successMessage = ref("");

const selectedImage = ref(null);

const imagePreview = ref("");

const fileInput = ref(null);



/* =========================================================
   DATE LIMITS
========================================================= */

const today = new Date();


const currentDate = computed(() => {

  const year =
    today.getFullYear();


  const month =
    String(
      today.getMonth() + 1
    ).padStart(2, "0");


  const day =
    String(
      today.getDate()
    ).padStart(2, "0");


  return `${year}-${month}-${day}`;

});


const minPublicationDate =
  "1900-01-01";


const maxPublicationDate =
  computed(() => {

    return currentDate.value;

  });



/* =========================================================
   FETCH CATEGORIES
========================================================= */

async function fetchCategories() {

  loadingCategories.value = true;

  try {

    const response =
      await getCategories();


    let categoryData = [];


    if (
      Array.isArray(response)
    ) {

      categoryData =
        response;

    }

    else if (
      Array.isArray(
        response?.data
      )
    ) {

      categoryData =
        response.data;

    }

    else if (
      Array.isArray(
        response?.data?.data
      )
    ) {

      categoryData =
        response.data.data;

    }

    else if (
      Array.isArray(
        response?.result
      )
    ) {

      categoryData =
        response.result;

    }


    categories.value =
      categoryData;

  }

  catch (error) {

    console.error(
      "Failed to fetch categories:",
      error
    );


    categories.value = [];


    errorMessage.value =
      "Failed to load categories. Please try again.";

  }

  finally {

    loadingCategories.value =
      false;

  }

}



/* =========================================================
   IMAGE CHANGE
========================================================= */

function handleImageChange(event) {

  const file =
    event.target.files?.[0];


  if (!file) {

    return;

  }


  errorMessage.value = "";


  const allowedTypes = [

    "image/jpeg",

    "image/jpg",

    "image/png"

  ];


  if (
    !allowedTypes.includes(
      file.type
    )
  ) {

    errorMessage.value =
      "Please select a PNG, JPG, or JPEG image.";


    event.target.value = "";


    return;

  }


  const maxSize =
    5 * 1024 * 1024;


  if (
    file.size > maxSize
  ) {

    errorMessage.value =
      "Image size must be less than 5MB.";


    event.target.value = "";


    return;

  }


  selectedImage.value =
    file;


  if (
    imagePreview.value
  ) {

    URL.revokeObjectURL(
      imagePreview.value
    );

  }


  imagePreview.value =
    URL.createObjectURL(
      file
    );

}



/* =========================================================
   REMOVE IMAGE
========================================================= */

function removeImage() {

  if (
    imagePreview.value
  ) {

    URL.revokeObjectURL(
      imagePreview.value
    );

  }


  selectedImage.value =
    null;


  imagePreview.value =
    "";


  if (
    fileInput.value
  ) {

    fileInput.value.value =
      "";

  }

}



/* =========================================================
   VALIDATE FORM
========================================================= */

function validateForm() {

  errorMessage.value = "";


  /* TITLE */

  if (
    !form.value.title.trim()
  ) {

    errorMessage.value =
      "Please enter the book title.";

    return false;

  }


  /* AUTHOR */

  if (
    !form.value.author.trim()
  ) {

    errorMessage.value =
      "Please enter the author name.";

    return false;

  }


  /* ISBN */

  if (
    !form.value.isbn.trim()
  ) {

    errorMessage.value =
      "Please enter the ISBN.";

    return false;

  }


  /* CATEGORY */

  if (

    form.value.categoryId === ""

    ||

    form.value.categoryId === null

    ||

    form.value.categoryId === undefined

  ) {

    errorMessage.value =
      "Please select a category.";

    return false;

  }


  /* LANGUAGE */

  if (
    !form.value.language
  ) {

    errorMessage.value =
      "Please select a language.";

    return false;

  }


  /* QUANTITY */

  if (

    form.value.qty === null

    ||

    form.value.qty === undefined

    ||

    Number(
      form.value.qty
    ) < 1

  ) {

    errorMessage.value =
      "Quantity must be at least 1.";

    return false;

  }


  /* PAGES */

  if (

    form.value.pages !== null

    &&

    form.value.pages !== ""

    &&

    Number(
      form.value.pages
    ) < 1

  ) {

    errorMessage.value =
      "Pages must be greater than 0.";

    return false;

  }


  /* PUBLICATION DATE */

  if (
    !form.value.publishYear
  ) {

    errorMessage.value =
      "Please select the publication date.";

    return false;

  }


  if (

    !/^\d{4}-\d{2}-\d{2}$/.test(
      form.value.publishYear
    )

  ) {

    errorMessage.value =
      "Please select a valid publication date.";

    return false;

  }


  if (

    form.value.publishYear >
    maxPublicationDate.value

  ) {

    errorMessage.value =
      "Publication date cannot be in the future.";

    return false;

  }


  return true;

}



/* =========================================================
   SUBMIT FORM
========================================================= */

async function submitForm() {

  successMessage.value = "";

  errorMessage.value = "";


  if (
    !validateForm()
  ) {

    return;

  }


  submitting.value = true;


  try {

    const bookRequest = {

      categoryId:
        Number(
          form.value.categoryId
        ),


      title:
        form.value.title.trim(),


      qty:
        Number(
          form.value.qty
        ),


      description:
        form.value.description.trim(),


      author:
        form.value.author.trim(),


      pages:

        form.value.pages !== null

        &&

        form.value.pages !== ""

          ? Number(
              form.value.pages
            )

          : null,


      isbn:
        form.value.isbn.trim(),


      language:
        form.value.language,


      /*
       * Automatically Available.
       * No status input is shown.
       */

      status:
        "Available",


      publishYear:
        String(
          form.value.publishYear
        )

    };


    console.log(
      "========== BOOK REQUEST =========="
    );


    console.log(
      "Book Request:",
      bookRequest
    );


    console.log(
      "Publish Year:",
      bookRequest.publishYear,
      typeof bookRequest.publishYear
    );


    console.log(
      "=================================="
    );


    const response =
      await createBook(
        bookRequest,
        selectedImage.value
      );


    console.log(
      "CREATE BOOK RESPONSE:",
      response
    );


    successMessage.value =
      "Book added successfully.";


    /*
     * Notify parent that book was saved.
     */

    emit(
      "saved",
      {

        ...bookRequest,

        publishYear:
          bookRequest.publishYear,

        image:
          response?.image
          ||

          response?.data?.image
          ||

          ""

      }
    );


    /*
     * Reset form after successful save.
     */

    resetForm();

  }

  catch (error) {

    console.error(
      "Failed to add book:",
      error
    );


    errorMessage.value =
      error?.message
      ||

      error?.response?.data?.message
      ||

      error?.response?.data?.msg
      ||

      "Failed to add book. Please try again.";

  }

  finally {

    submitting.value =
      false;

  }

}



/* =========================================================
   RESET FORM
========================================================= */

function resetForm() {

  form.value = {

    title: "",

    author: "",

    isbn: "",

    categoryId: "",

    qty: 1,

    pages: null,

    publishYear: "",

    language: "",

    description: "",

    status: "Available"

  };


  removeImage();


  errorMessage.value =
    "";

}



/* =========================================================
   BACK / CANCEL
========================================================= */

function handleCancel() {

  /*
   * Do not close while submitting.
   */

  if (
    submitting.value
  ) {

    return;

  }


  /*
   * Tell Books.vue to return
   * to the Book List.
   */

  emit("close");

}



/* =========================================================
   MOUNT
========================================================= */

onMounted(() => {

  fetchCategories();

});



/* =========================================================
   CLEANUP
========================================================= */

onBeforeUnmount(() => {

  if (
    imagePreview.value
  ) {

    URL.revokeObjectURL(
      imagePreview.value
    );

  }

});

</script>


<style scoped>

/* =========================================================
   PAGE
========================================================= */

.add-book-page {

  min-height: 100vh;

  background: #f8fafc;

  box-sizing: border-box;

}



/* =========================================================
   HEADER
========================================================= */

.page-header {

  max-width: 1100px;

  margin: 0 auto 25px;

  display: flex;

  align-items: center;

  justify-content: space-between;

  gap: 20px;

}


.page-header h2 {

  margin: 0;

  color: #0f172a;

  font-size: 28px;

  font-weight: 700;

}


.page-header p {

  margin: 6px 0 0;

  color: #64748b;

  font-size: 14px;

}



/* =========================================================
   BACK BUTTON
========================================================= */

.back-btn {

  border: 1px solid #dbeafe;

  background: #ffffff;

  color: #2563eb;

  padding: 10px 17px;

  border-radius: 10px;

  display: flex;

  align-items: center;

  gap: 8px;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s ease;

}


.back-btn:hover {

  background: #eff6ff;

  border-color: #bfdbfe;

}



/* =========================================================
   ALERT
========================================================= */

.alert-message {

  max-width: 1100px;

  margin: 0 auto 20px;

  padding: 13px 16px;

  border-radius: 10px;

  display: flex;

  align-items: center;

  gap: 10px;

  font-size: 14px;

}


.alert-message.error {

  color: #b91c1c;

  background: #fef2f2;

  border: 1px solid #fecaca;

}


.alert-message.success {

  color: #15803d;

  background: #f0fdf4;

  border: 1px solid #bbf7d0;

}



/* =========================================================
   FORM
========================================================= */

.book-form {

  max-width: 1100px;

  margin: 0 auto;

  display: flex;

  flex-direction: column;

  gap: 20px;

}



/* =========================================================
   FORM CARD
========================================================= */

.form-card {

  background: #ffffff;

  border: 1px solid #e2e8f0;

  border-radius: 16px;

  padding: 25px;

  box-shadow:
    0 4px 15px rgba(
      15,
      23,
      42,
      0.04
    );

}



/* =========================================================
   SECTION HEADER
========================================================= */

.section-header {

  display: flex;

  align-items: center;

  gap: 13px;

  margin-bottom: 24px;

  padding-bottom: 18px;

  border-bottom:
    1px solid #f1f5f9;

}


.section-icon {

  width: 42px;

  height: 42px;

  flex-shrink: 0;

  border-radius: 11px;

  display: flex;

  align-items: center;

  justify-content: center;

  background: #eff6ff;

  color: #2563eb;

  font-size: 19px;

}


.section-header h3 {

  margin: 0;

  color: #0f172a;

  font-size: 18px;

  font-weight: 700;

}


.section-header p {

  margin: 3px 0 0;

  color: #64748b;

  font-size: 13px;

}



/* =========================================================
   FORM GRID
========================================================= */

.form-grid {

  display: grid;

  grid-template-columns:
    repeat(
      2,
      minmax(0, 1fr)
    );

  gap: 20px;

}



/* =========================================================
   FORM GROUP
========================================================= */

.form-group {

  display: flex;

  flex-direction: column;

  gap: 7px;

  min-width: 0;

}


.form-group label {

  color: #334155;

  font-size: 14px;

  font-weight: 600;

}


.form-group label span {

  color: #ef4444;

}



/* =========================================================
   INPUT / SELECT / TEXTAREA
========================================================= */

.form-group input,
.form-group select,
.form-group textarea {

  width: 100%;

  box-sizing: border-box;

  border:
    1px solid #cbd5e1;

  border-radius: 10px;

  background: #ffffff;

  color: #0f172a;

  font-family: inherit;

  font-size: 14px;

  outline: none;

  transition:
    border-color 0.2s ease,
    box-shadow 0.2s ease;

}


.form-group input,
.form-group select {

  height: 44px;

  padding: 0 13px;

}


.form-group textarea {

  padding: 12px 13px;

  resize: vertical;

  min-height: 140px;

}


.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {

  border-color: #60a5fa;

  box-shadow:
    0 0 0 3px
    rgba(
      96,
      165,
      250,
      0.12
    );

}


.form-group input::placeholder,
.form-group textarea::placeholder {

  color: #94a3b8;

}


.form-group select:disabled {

  background: #f8fafc;

  color: #64748b;

  cursor: not-allowed;

}



/* =========================================================
   FIELD HINT
========================================================= */

.field-hint {

  color: #94a3b8;

  font-size: 12px;

}


.field-hint.warning {

  color: #d97706;

}



/* =========================================================
   IMAGE UPLOAD
========================================================= */

.image-upload-area {

  width: 100%;

}


.upload-box {

  min-height: 230px;

  border:
    2px dashed #cbd5e1;

  border-radius: 14px;

  display: flex;

  flex-direction: column;

  align-items: center;

  justify-content: center;

  text-align: center;

  cursor: pointer;

  transition: 0.2s ease;

}


.upload-box:hover {

  border-color: #60a5fa;

  background: #f8fbff;

}


.upload-icon {

  width: 56px;

  height: 56px;

  margin-bottom: 12px;

  border-radius: 50%;

  background: #eff6ff;

  color: #2563eb;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 25px;

}


.upload-box h4 {

  margin: 0;

  color: #334155;

  font-size: 15px;

  font-weight: 600;

}


.upload-box p {

  margin: 7px 0 3px;

  color: #64748b;

  font-size: 13px;

}


.upload-box span {

  color: #94a3b8;

  font-size: 12px;

}



/* =========================================================
   IMAGE PREVIEW
========================================================= */

.image-preview {

  position: relative;

  width: 190px;

  height: 250px;

  margin: 0 auto;

  border-radius: 14px;

  overflow: hidden;

  background: #f1f5f9;

  border:
    1px solid #e2e8f0;

}


.image-preview img {

  width: 100%;

  height: 100%;

  object-fit: cover;

  display: block;

}


.remove-image-btn {

  position: absolute;

  top: 10px;

  right: 10px;

  width: 34px;

  height: 34px;

  border: none;

  border-radius: 50%;

  background:
    rgba(
      15,
      23,
      42,
      0.75
    );

  color: #ffffff;

  display: flex;

  align-items: center;

  justify-content: center;

  cursor: pointer;

  transition: 0.2s ease;

}


.remove-image-btn:hover {

  background: #dc2626;

}



/* =========================================================
   FORM ACTIONS
========================================================= */

.form-actions {

  display: flex;

  justify-content: flex-end;

  gap: 12px;

  padding-bottom: 30px;

}


.cancel-btn,
.submit-btn {

  min-width: 120px;

  height: 44px;

  padding: 0 18px;

  border-radius: 10px;

  font-family: inherit;

  font-size: 14px;

  font-weight: 600;

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 8px;

  cursor: pointer;

  transition: 0.2s ease;

}


.cancel-btn {

  border:
    1px solid #cbd5e1;

  background: #ffffff;

  color: #475569;

}


.cancel-btn:hover:not(:disabled) {

  background: #f8fafc;

  border-color: #94a3b8;

}


.submit-btn {

  border:
    1px solid #2563eb;

  background: #2563eb;

  color: #ffffff;

}


.submit-btn:hover:not(:disabled) {

  background: #1d4ed8;

}


.cancel-btn:disabled,
.submit-btn:disabled {

  opacity: 0.6;

  cursor: not-allowed;

}



/* =========================================================
   SPIN
========================================================= */

.spin {

  animation:
    spin 1s linear infinite;

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



/* =========================================================
   TABLET
========================================================= */

@media (max-width: 768px) {

  .add-book-page {

    padding:
      25px 18px;

  }


  .page-header {

    align-items:
      flex-start;

  }


  .page-header h2 {

    font-size: 24px;

  }


  .form-card {

    padding: 20px;

  }


  .form-grid {

    grid-template-columns:
      repeat(
        2,
        minmax(0, 1fr)
      );

  }

}



/* =========================================================
   MOBILE
========================================================= */

@media (max-width: 550px) {

  .add-book-page {

    padding:
      18px 12px;

  }


  .page-header {

    flex-direction:
      column;

    align-items:
      stretch;

  }


  .back-btn {

    justify-content:
      center;

  }


  .page-header h2 {

    font-size: 22px;

  }


  .form-card {

    padding: 16px;

    border-radius: 13px;

  }


  .section-header {

    margin-bottom: 18px;

  }


  .section-icon {

    width: 38px;

    height: 38px;

  }


  .section-header h3 {

    font-size: 16px;

  }


  .form-grid {

    grid-template-columns: 1fr;

    gap: 16px;

  }


  .form-actions {

    flex-direction:
      column-reverse;

  }


  .cancel-btn,
  .submit-btn {

    width: 100%;

  }


  .image-preview {

    width: 160px;

    height: 215px;

  }

}

</style>
