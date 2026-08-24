<template>
  <div class="edit-book-page">

    <!-- Header -->
    <div class="page-header">
      <div>
        <h1>Edit Book</h1>
        <p>Update book information</p>
      </div>

      <button
        class="back-btn"
        @click="goBack"
      >
        ← Back
      </button>
    </div>


    <!-- Form -->
    <div class="form-card">

      <form @submit.prevent="updateBook">

        <!-- Row 1 -->
        <div class="form-row">

          <div class="form-group">
            <label>Book Title</label>

            <input
              v-model="form.title"
              type="text"
              placeholder="Enter book title"
              required
            />
          </div>


          <div class="form-group">
            <label>Author</label>

            <input
              v-model="form.author"
              type="text"
              placeholder="Enter author"
              required
            />
          </div>

        </div>


        <!-- Row 2 -->
        <div class="form-row">

          <div class="form-group">
            <label>Category</label>

            <select
              v-model="form.category"
              required
            >
              <option value="">
                Select Category
              </option>

              <option
                v-for="category in categories"
                :key="category"
                :value="category"
              >
                {{ category }}
              </option>
            </select>
          </div>


          <div class="form-group">
            <label>ISBN</label>

            <input
              v-model="form.isbn"
              type="text"
              placeholder="Enter ISBN"
            />
          </div>

        </div>


        <!-- Row 3 -->
        <div class="form-row">

          <div class="form-group">
            <label>Quantity</label>

            <input
              v-model.number="form.quantity"
              type="number"
              min="0"
              required
            />
          </div>


          <div class="form-group">
            <label>Status</label>

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


        <!-- Description -->
        <div class="form-group full">

          <label>Description</label>

          <textarea
            v-model="form.description"
            rows="5"
            placeholder="Enter book description"
          ></textarea>

        </div>


        <!-- Buttons -->
        <div class="form-actions">

          <button
            type="button"
            class="cancel-btn"
            @click="goBack"
          >
            Cancel
          </button>

          <button
            type="submit"
            class="save-btn"
          >
            Save Changes
          </button>

        </div>

      </form>

    </div>

  </div>
</template>


<script setup>

import { ref } from "vue";
import { useRoute, useRouter } from "vue-router";


const route = useRoute();
const router = useRouter();


// =====================================================
// CATEGORIES
// =====================================================

const categories = [
  "Programming",
  "Database",
  "Networking",
  "Security",
  "Web Development",
  "Business",
  "Other"
];


// =====================================================
// BOOK DATA
// =====================================================

// Temporary data
// Later this will come from API / database

const books = [
  {
    id: 1,
    title: "Clean Code",
    description:
      "A Handbook of Agile Software Craftsmanship",
    author: "Robert C. Martin",
    category: "Programming",
    isbn: "978-0132350884",
    quantity: 5,
    status: "Available"
  },

  {
    id: 2,
    title: "Java Programming",
    description:
      "Comprehensive Guide",
    author: "John Smith",
    category: "Programming",
    isbn: "978-0321573513",
    quantity: 3,
    status: "Borrowed"
  },

  {
    id: 3,
    title: "Database System Concepts",
    description:
      "Database Management",
    author: "Abraham Silberschatz",
    category: "Database",
    isbn: "978-0078022159",
    quantity: 4,
    status: "Available"
  },

  {
    id: 4,
    title: "Computer Networking",
    description:
      "A Top-Down Approach",
    author: "James F. Kurose",
    category: "Networking",
    isbn: "978-0133594140",
    quantity: 2,
    status: "Borrowed"
  },

  {
    id: 5,
    title: "Web Development",
    description:
      "Modern Web Development",
    author: "David Miller",
    category: "Web Development",
    isbn: "978-1492052203",
    quantity: 6,
    status: "Available"
  },

  {
    id: 6,
    title: "Cyber Security",
    description:
      "Introduction to Security",
    author: "William Stallings",
    category: "Security",
    isbn: "978-0134091305",
    quantity: 2,
    status: "Overdue"
  }
];


// =====================================================
// FIND BOOK
// =====================================================

const bookId = Number(route.params.id);

const book = books.find(
  (item) => item.id === bookId
);


// =====================================================
// FORM
// =====================================================

const form = ref({
  id: book?.id || null,
  title: book?.title || "",
  description: book?.description || "",
  author: book?.author || "",
  category: book?.category || "",
  isbn: book?.isbn || "",
  quantity: book?.quantity || 0,
  status: book?.status || "Available"
});


// =====================================================
// UPDATE BOOK
// =====================================================

function updateBook() {

  if (!book) {
    alert("Book not found.");
    return;
  }


  // Update temporary object
  book.title = form.value.title;
  book.description = form.value.description;
  book.author = form.value.author;
  book.category = form.value.category;
  book.isbn = form.value.isbn;
  book.quantity = form.value.quantity;
  book.status = form.value.status;


  alert("Book updated successfully!");


  // Back to Book Management
  router.push("/admin/books");
}


// =====================================================
// BACK
// =====================================================

function goBack() {
  router.push("/admin/books");
}

</script>


<style scoped>

/* =====================================================
   PAGE
===================================================== */

.edit-book-page {
  min-height: calc(100vh - 70px);
  padding: 30px;
  background: #f8f9fc;
  font-family: Arial, sans-serif;
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
  color: #172033;
}

.page-header p {
  margin-top: 6px;
  color: #7b8497;
  font-size: 14px;
}


/* =====================================================
   BACK BUTTON
===================================================== */

.back-btn {
  padding: 11px 18px;
  border: 1px solid #dfe3eb;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  color: #667085;
  font-weight: 600;
}

.back-btn:hover {
  background: #f1f2f6;
}


/* =====================================================
   FORM CARD
===================================================== */

.form-card {
  background: white;
  border: 1px solid #e5e8ef;
  border-radius: 12px;
  padding: 30px;
  max-width: 1000px;
}


/* =====================================================
   FORM ROW
===================================================== */

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
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

.form-group.full {
  margin-bottom: 20px;
}

.form-group label {
  font-size: 14px;
  font-weight: 600;
  color: #344054;
}


/* =====================================================
   INPUT
===================================================== */

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  box-sizing: border-box;
  padding: 12px 14px;
  border: 1px solid #dfe3eb;
  border-radius: 8px;
  outline: none;
  font-size: 14px;
  font-family: Arial, sans-serif;
}

.form-group input:focus,
.form-group select:focus,
.form-group textarea:focus {
  border-color: #5b3df5;
}


/* =====================================================
   TEXTAREA
===================================================== */

.form-group textarea {
  resize: vertical;
}


/* =====================================================
   ACTIONS
===================================================== */

.form-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  padding-top: 20px;
  border-top: 1px solid #edf0f5;
}


/* =====================================================
   CANCEL
===================================================== */

.cancel-btn {
  padding: 12px 22px;
  border: 1px solid #dfe3eb;
  background: white;
  border-radius: 8px;
  cursor: pointer;
  color: #667085;
  font-weight: 600;
}

.cancel-btn:hover {
  background: #f1f2f6;
}


/* =====================================================
   SAVE
===================================================== */

.save-btn {
  padding: 12px 22px;
  border: none;
  background: #5b3df5;
  color: white;
  border-radius: 8px;
  cursor: pointer;
  font-weight: 600;
}

.save-btn:hover {
  background: #4930d5;
}


/* =====================================================
   RESPONSIVE
===================================================== */

@media (max-width: 768px) {

  .edit-book-page {
    padding: 20px;
  }

  .form-row {
    grid-template-columns: 1fr;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 15px;
  }

  .form-card {
    padding: 20px;
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