<template>

  <div class="card category-card">

    <!-- =================================================
         HEADER
    ================================================== -->

    <div class="card-header">

      <div>

        <h3>
          Book Categories
        </h3>

        <p>
          Books by category
        </p>

      </div>

    </div>


    <!-- =================================================
         CONTENT
    ================================================== -->

    <div class="category-content">

      <!-- =================================================
           DONUT
      ================================================== -->

      <div
        class="donut"
        :style="donutStyle"
      >

        <div class="donut-center">

          <strong>
            {{ totalBooks }}
          </strong>

          <span>
            Books
          </span>

        </div>

      </div>


      <!-- =================================================
           CATEGORY LIST
      ================================================== -->

      <div class="category-list">

        <!-- CATEGORY -->

        <div
          v-for="category in categoryData"
          :key="category.id || category.name"
          class="category-row"
        >

          <div class="category-name">

            <span
              class="dot"
              :class="category.color"
            ></span>

            {{ category.name }}

          </div>


          <strong>
            {{ category.value }}
          </strong>

        </div>


        <!-- =================================================
             LOADING
        ================================================== -->

        <div
          v-if="loading"
          class="message"
        >

          <i class="bi bi-arrow-repeat"></i>

          Loading categories...

        </div>


        <!-- =================================================
             ERROR
        ================================================== -->

        <div
          v-if="!loading && error"
          class="message error"
        >

          {{ error }}

        </div>


        <!-- =================================================
             EMPTY
        ================================================== -->

        <div
          v-if="
            !loading &&
            !error &&
            categoryData.length === 0
          "
          class="message"
        >

          No categories found.

        </div>

      </div>

    </div>

  </div>

</template>


<script setup>

import {
  ref,
  computed,
  onMounted
} from "vue";


import {
  getCategories
} from "../../service/categoryService";


import {
  getBooks
} from "../../service/bookService";


// ==================================================
// DATA
// ==================================================

const categoryData =
  ref([]);


const books =
  ref([]);


const loading =
  ref(false);


const error =
  ref("");


// ==================================================
// COLORS
// ==================================================

const colors = [

  "purple",

  "blue",

  "green",

  "orange",

  "gray"

];


// ==================================================
// GET CATEGORIES
// ==================================================

const fetchCategories = async () => {

  loading.value = true;

  error.value = "";


  try {

    // =================================================
    // FETCH CATEGORIES
    // =================================================

    const categoryResponse =
      await getCategories();


    console.log(
      "CATEGORY API RESPONSE:",
      categoryResponse
    );


    let categories = [];


    if (
      Array.isArray(categoryResponse)
    ) {

      categories =
        categoryResponse;

    }

    else if (
      Array.isArray(
        categoryResponse?.data
      )
    ) {

      categories =
        categoryResponse.data;

    }


    // =================================================
    // FETCH BOOKS
    // =================================================

    const bookResponse =
      await getBooks();


    console.log(
      "BOOK API RESPONSE:",
      bookResponse
    );


    if (
      Array.isArray(bookResponse)
    ) {

      books.value =
        bookResponse;

    }

    else if (
      Array.isArray(
        bookResponse?.data
      )
    ) {

      books.value =
        bookResponse.data;

    }

    else {

      books.value = [];

    }


    console.log(
      "BOOKS FOR CATEGORY:",
      books.value
    );


    // =================================================
    // BUILD CATEGORY DATA
    // =================================================

    categoryData.value =
      categories.map(
        (category, index) => {

          /*
           * Count books belonging
           * to this category.
           */

          const count =
            books.value.filter(
              book => {

                // ---------------------------------------
                // BOOK CATEGORY AS OBJECT
                // ---------------------------------------

                if (
                  book.category &&
                  typeof book.category === "object"
                ) {

                  return (
                    Number(
                      book.category.id
                    ) ===
                    Number(category.id)
                  );

                }


                // ---------------------------------------
                // BOOK CATEGORY ID
                // ---------------------------------------

                if (
                  book.categoryId !== undefined &&
                  book.categoryId !== null
                ) {

                  return (
                    Number(
                      book.categoryId
                    ) ===
                    Number(category.id)
                  );

                }


                // ---------------------------------------
                // BOOK CATEGORY NAME
                // ---------------------------------------

                if (
                  typeof book.category ===
                  "string"
                ) {

                  return (
                    book.category
                      .toLowerCase()
                      .trim() ===
                    String(
                      category.name
                    )
                      .toLowerCase()
                      .trim()
                  );

                }


                return false;

              }
            ).length;


          return {

            id:
              category.id,

            name:
              category.name ||
              "Others",

            value:
              count,

            color:
              colors[
                index %
                colors.length
              ]

          };

        }
      );


    console.log(
      "CATEGORY DATA:",
      categoryData.value
    );

  }


  catch (err) {

    console.error(
      "Failed to fetch category data:",
      err
    );


    error.value =
      err?.message ||
      "Failed to load categories.";


    categoryData.value =
      [];

  }


  finally {

    loading.value =
      false;

  }

};


// ==================================================
// TOTAL BOOKS
// ==================================================

const totalBooks =
  computed(() => {

    /*
     * Use actual books count.
     *
     * This is better than adding
     * category counts because a book
     * could potentially not have a
     * valid category.
     */

    return books.value.length;

  });


// ==================================================
// DONUT CHART
// ==================================================

const donutStyle =
  computed(() => {

    // -----------------------------------------------
    // EMPTY
    // -----------------------------------------------

    if (
      categoryData.value.length === 0 ||
      totalBooks.value === 0
    ) {

      return {

        background:
          "#e5e7eb"

      };

    }


    let currentPercentage =
      0;


    const sections =
      [];


    categoryData.value.forEach(
      category => {

        const value =
          Number(
            category.value || 0
          );


        /*
         * Skip category with
         * zero books.
         */

        if (value <= 0) {

          return;

        }


        const percentage =
          (
            value /
            totalBooks.value
          ) *
          100;


        const start =
          currentPercentage;


        const end =
          currentPercentage +
          percentage;


        const color =
          getColor(
            category.color
          );


        sections.push(
          `${color} ${start}% ${end}%`
        );


        currentPercentage =
          end;

      }
    );


    /*
     * If no category has books
     */

    if (
      sections.length === 0
    ) {

      return {

        background:
          "#e5e7eb"

      };

    }


    return {

      background:
        `conic-gradient(${sections.join(", ")})`

    };

  });


// ==================================================
// GET COLOR
// ==================================================

const getColor =
  color => {

    const colorMap = {

      purple:
        "#5b3df5",

      blue:
        "#4285f4",

      green:
        "#22a06b",

      orange:
        "#f59e0b",

      gray:
        "#9ca3af"

    };


    return (
      colorMap[color] ||
      colorMap.gray
    );

  };


// ==================================================
// LOAD DATA
// ==================================================

onMounted(() => {

  fetchCategories();

});

</script>


<style scoped>

/* =================================================
   CARD
================================================= */

.card {

  min-width:
    0;

  background:
    white;

  border:
    1px solid #e6e9ef;

  border-radius:
    12px;

  box-shadow:
    0 2px 5px
    rgba(0, 0, 0, 0.02);

  overflow:
    hidden;

}


/* =================================================
   HEADER
================================================= */

.card-header {

  display:
    flex;

  align-items:
    center;

  justify-content:
    space-between;

  gap:
    15px;

  padding:
    20px;

  border-bottom:
    1px solid #f0f1f4;

}


.card-header h3 {

  margin:
    0;

  font-size:
    16px;

  color:
    #111827;

}


.card-header p {

  margin:
    5px 0 0;

  font-size:
    12px;

  color:
    #8a92a3;

}


/* =================================================
   CONTENT
================================================= */

.category-content {

  display:
    flex;

  align-items:
    center;

  gap:
    25px;

  padding:
    25px;

}


/* =================================================
   DONUT
================================================= */

.donut {

  width:
    150px;

  height:
    150px;

  border-radius:
    50%;

  display:
    flex;

  align-items:
    center;

  justify-content:
    center;

  flex-shrink:
    0;

  transition:
    background
    0.4s ease;

}


/* =================================================
   DONUT CENTER
================================================= */

.donut-center {

  width:
    95px;

  height:
    95px;

  border-radius:
    50%;

  background:
    white;

  display:
    flex;

  flex-direction:
    column;

  align-items:
    center;

  justify-content:
    center;

}


.donut-center strong {

  font-size:
    20px;

  color:
    #111827;

}


.donut-center span {

  color:
    #8a92a3;

  font-size:
    11px;

}


/* =================================================
   CATEGORY LIST
================================================= */

.category-list {

  flex:
    1;

  min-width:
    0;

  max-height:
    180px;

  overflow-y:
    auto;

}


/* =================================================
   CATEGORY ROW
================================================= */

.category-row {

  display:
    flex;

  align-items:
    center;

  justify-content:
    space-between;

  gap:
    10px;

  padding:
    8px 0;

  font-size:
    12px;

}


.category-row strong {

  color:
    #111827;

  font-weight:
    600;

}


/* =================================================
   CATEGORY NAME
================================================= */

.category-name {

  display:
    flex;

  align-items:
    center;

  gap:
    8px;

  color:
    #667085;

}


/* =================================================
   DOT
================================================= */

.dot {

  width:
    9px;

  height:
    9px;

  border-radius:
    50%;

  flex-shrink:
    0;

}


.dot.purple {

  background:
    #5b3df5;

}


.dot.blue {

  background:
    #4285f4;

}


.dot.green {

  background:
    #22a06b;

}


.dot.orange {

  background:
    #f59e0b;

}


.dot.gray {

  background:
    #9ca3af;

}


/* =================================================
   MESSAGE
================================================= */

.message {

  padding:
    15px 0;

  font-size:
    12px;

  color:
    #8a92a3;

}


.message i {

  margin-right:
    6px;

}


.message.error {

  color:
    #ef4444;

}


/* =================================================
   SCROLLBAR
================================================= */

.category-list::-webkit-scrollbar {

  width:
    5px;

}


.category-list::-webkit-scrollbar-thumb {

  background:
    #d1d5db;

  border-radius:
    999px;

}


/* =================================================
   RESPONSIVE
================================================= */

@media (max-width: 768px) {

  .category-content {

    flex-direction:
      column;

    align-items:
      center;

  }


  .category-list {

    width:
      100%;

  }

}

</style>