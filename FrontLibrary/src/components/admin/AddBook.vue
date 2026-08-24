<template>
  <div class="add-book-page">

    <!-- ================= HEADER ================= -->

    <div class="page-header">

      <div>

        <h1>
          Add New Book
        </h1>

        <p>
          Add a new book to the library collection
        </p>

      </div>

    </div>


    <!-- ================= FORM CARD ================= -->

    <div class="form-card">

      <form @submit.prevent="saveBook">


        <!-- ================= ROW 1 ================= -->

        <div class="form-row">

          <!-- Title -->
          <div class="form-group">

            <label>
              Title <span>*</span>
            </label>

            <input
              v-model="form.title"
              type="text"
              placeholder="Enter book title"
              required
            />

          </div>


          <!-- Category -->
          <div class="form-group">

            <label>
              Category <span>*</span>
            </label>

            <select
              v-model="form.category"
              required
            >

              <option
                value=""
                disabled
              >
                Select category
              </option>

              <option value="Programming">
                Programming
              </option>

              <option value="Database">
                Database
              </option>

              <option value="Networking">
                Networking
              </option>

              <option value="Security">
                Security
              </option>

              <option value="Web Development">
                Web Development
              </option>

              <option value="Business">
                Business
              </option>

              <option value="Other">
                Other
              </option>

            </select>

          </div>

        </div>


        <!-- ================= ROW 2 ================= -->

        <div class="form-row">

          <!-- Publisher -->
          <div class="form-group">

            <label>
              Publisher
            </label>

            <input
              v-model="form.publisher"
              type="text"
              placeholder="Enter publisher"
            />

          </div>


          <!-- Quantity -->
          <div class="form-group">

            <label>
              Quantity <span>*</span>
            </label>

            <input
              v-model.number="form.quantity"
              type="number"
              min="1"
              placeholder="Enter quantity"
              required
            />

          </div>

        </div>


        <!-- ================= ROW 3 ================= -->

        <div class="form-row">

          <!-- Author -->
          <div class="form-group">

            <label>
              Author <span>*</span>
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
            </label>

            <input
              v-model="form.isbn"
              type="text"
              placeholder="Enter ISBN"
            />

          </div>

        </div>


        <!-- ================= ROW 4 ================= -->

        <div class="form-row">

          <!-- Published Year -->
          <div class="form-group">

            <label>
              Published Year
            </label>

            <input
              v-model.number="form.publishedYear"
              type="number"
              min="1000"
              :max="currentYear"
              placeholder="e.g. 2024"
            />

          </div>


          <!-- Status -->
          <div class="form-group">

            <label>
              Status <span>*</span>
            </label>

            <select
              v-model="form.status"
              required
            >

              <option value="Available">
                Available
              </option>

              <option value="Unavailable">
                Unavailable
              </option>

            </select>

          </div>

        </div>


        <!-- ================= DESCRIPTION ================= -->

        <div class="form-group full-width">

          <label>
            Description
          </label>

          <textarea
            v-model="form.description"
            rows="5"
            placeholder="Enter book description..."
          ></textarea>

        </div>


        <!-- ================= BOOK COVER ================= -->

        <div class="form-group full-width">

          <label>
            Book Cover
          </label>


          <div class="file-upload">

            <input
              id="book-cover"
              type="file"
              accept="image/*"
              @change="handleFile"
            />


            <label
              for="book-cover"
              class="choose-file-btn"
            >
              Choose File
            </label>


            <span class="file-name">

              {{ fileName || "No file chosen" }}

            </span>

          </div>


          <!-- Preview -->
          <div
            v-if="preview"
            class="image-preview"
          >

            <img
              :src="preview"
              alt="Book Cover Preview"
            />

          </div>

        </div>


        <!-- ================= BUTTONS ================= -->

        <div class="form-actions">

          <button
            type="button"
            class="cancel-btn"
            @click="cancel"
          >
            Cancel
          </button>


          <button
            type="submit"
            class="save-btn"
          >
            Save Book
          </button>

        </div>

      </form>

    </div>

  </div>
</template>


<script setup>
import {
  reactive,
  ref,
  onUnmounted
} from "vue";


// =====================================================
// EVENTS
// =====================================================

const emit = defineEmits([
  "close",
  "saved"
]);


// =====================================================
// CURRENT YEAR
// =====================================================

const currentYear = new Date().getFullYear();


// =====================================================
// FORM
// =====================================================

const form = reactive({

  title: "",

  category: "",

  publisher: "",

  quantity: 1,

  author: "",

  isbn: "",

  publishedYear: "",

  status: "Available",

  description: "",

  bookCover: null

});


// =====================================================
// FILE
// =====================================================

const fileName = ref("");

const preview = ref("");


// =====================================================
// HANDLE FILE
// =====================================================

function handleFile(event) {

  const file = event.target.files[0];

  if (!file) {
    return;
  }


  // Store file
  form.bookCover = file;


  // Store file name
  fileName.value = file.name;


  // Remove old preview
  if (preview.value) {

    URL.revokeObjectURL(
      preview.value
    );

  }


  // Create new preview
  preview.value =
    URL.createObjectURL(file);
}


// =====================================================
// SAVE BOOK
// =====================================================

function saveBook() {

  console.log(
    "Book data:",
    form
  );


  // -----------------------------------------------
  // Later:
  // Send form data to Backend API
  // -----------------------------------------------


  alert(
    "Book saved successfully!"
  );


  // Tell Books.vue that book was saved
  emit("saved");
}


// =====================================================
// CANCEL
// =====================================================

function cancel() {

  // Tell Books.vue to close AddBook
  emit("close");

}


// =====================================================
// CLEANUP PREVIEW
// =====================================================

onUnmounted(() => {

  if (preview.value) {

    URL.revokeObjectURL(
      preview.value
    );

  }

});
</script>

<style scoped>

/* =====================================================
   PAGE
===================================================== */

.add-book-page {

  min-height: calc(100vh - 70px);

  padding: 30px;

  box-sizing: border-box;

  background: #f8fafc;

}


/* =====================================================
   HEADER
===================================================== */

.page-header {

  margin-bottom: 25px;

}


.page-header h1 {

  margin: 0;

  font-size: 28px;

  font-weight: 700;

  color: #111827;

}


.page-header p {

  margin: 6px 0 0;

  font-size: 14px;

  color: #6b7280;

}


/* =====================================================
   FORM CARD
===================================================== */

.form-card {

  max-width: 1000px;

  background: white;

  border: 1px solid #e5e7eb;

  border-radius: 12px;

  padding: 30px;

  box-sizing: border-box;

  box-shadow:
    0 2px 8px
    rgba(0, 0, 0, 0.04);

}


/* =====================================================
   FORM ROW
===================================================== */

.form-row {

  display: grid;

  grid-template-columns:
    1fr 1fr;

  gap: 22px;

  margin-bottom: 20px;

}


/* =====================================================
   FORM GROUP
===================================================== */

.form-group {

  display: flex;

  flex-direction: column;

  gap: 8px;

}


.full-width {

  margin-bottom: 20px;

}


/* =====================================================
   LABEL
===================================================== */

.form-group label {

  font-size: 14px;

  font-weight: 600;

  color: #374151;

}


.form-group label span {

  color: #ef4444;

}


/* =====================================================
   INPUT / SELECT / TEXTAREA
===================================================== */

.form-group input,
.form-group select,
.form-group textarea {

  width: 100%;

  box-sizing: border-box;

  padding: 11px 13px;

  border: 1px solid #d1d5db;

  border-radius: 7px;

  outline: none;

  background: white;

  color: #374151;

  font-size: 14px;

  transition: 0.2s;

}


.form-group input {

  height: 44px;

}


.form-group select {

  height: 44px;

  cursor: pointer;

}


.form-group textarea {

  resize: vertical;

  min-height: 120px;

}


/* =====================================================
   FOCUS
===================================================== */

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {

  border-color: #2563eb;

  box-shadow:
    0 0 0 3px
    rgba(37, 99, 235, 0.08);

}


/* =====================================================
   PLACEHOLDER
===================================================== */

.form-group input::placeholder,
.form-group textarea::placeholder {

  color: #9ca3af;

}


/* =====================================================
   FILE UPLOAD
===================================================== */

.file-upload {

  display: flex;

  align-items: center;

  gap: 12px;

}


/* Hide file input */

.file-upload input[type="file"] {

  display: none;

}


/* Choose File */

.choose-file-btn {

  display: inline-flex !important;

  align-items: center;

  justify-content: center;

  padding: 10px 16px;

  background: #f3f4f6;

  border: 1px solid #d1d5db;

  border-radius: 7px;

  color: #374151 !important;

  font-size: 14px !important;

  font-weight: 500 !important;

  cursor: pointer;

  transition: 0.2s;

}


.choose-file-btn:hover {

  background: #e5e7eb;

}


/* File Name */

.file-name {

  font-size: 13px;

  color: #6b7280;

}


/* =====================================================
   IMAGE PREVIEW
===================================================== */

.image-preview {

  margin-top: 15px;

}


.image-preview img {

  width: 100px;

  height: 130px;

  object-fit: cover;

  border-radius: 6px;

  border: 1px solid #e5e7eb;

}


/* =====================================================
   FORM ACTIONS
===================================================== */

.form-actions {

  display: flex;

  justify-content: flex-end;

  gap: 12px;

  margin-top: 30px;

  padding-top: 20px;

  border-top: 1px solid #e5e7eb;

}


/* =====================================================
   CANCEL
===================================================== */

.cancel-btn {

  padding: 11px 20px;

  border: 1px solid #d1d5db;

  border-radius: 7px;

  background: white;

  color: #374151;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s;

}


.cancel-btn:hover {

  background: #f3f4f6;

}


/* =====================================================
   SAVE
===================================================== */

.save-btn {

  padding: 11px 22px;

  border: none;

  border-radius: 7px;

  background: #2563eb;

  color: white;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

  transition: 0.2s;

}


.save-btn:hover {

  background: #1d4ed8;

}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 768px) {

  .add-book-page {

    padding: 20px;

  }


  .form-card {

    padding: 20px;

  }


  .form-row {

    grid-template-columns: 1fr;

    gap: 18px;

  }


  .file-upload {

    flex-wrap: wrap;

  }


  .form-actions {

    flex-direction: column-reverse;

  }


  .cancel-btn,
  .save-btn {

    width: 100%;

  }

}

</style>