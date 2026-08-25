<template>

  <div
    class="category-card"
    @click="$emit('click', category)"
  >

    <!-- Text -->

    <div class="card-text">

      <h3 class="card-title">
        {{ displayName }}
      </h3>

      <p class="card-count">
        {{ getBookCount(category) }} Books
      </p>

    </div>

  </div>

</template>


<script>

export default {

  name: "CategoryCard",


  props: {

    category: {

      type: Object,

      required: true

    }

  },


  emits: [
    "click"
  ],


  computed: {

    // ==================================================
    // DISPLAY NAME
    // ==================================================

    displayName() {

      if (
        this.category.isOther === true
      ) {

        return "Other";

      }

      return this.category.name || "Other";

    }

  },


  methods: {

    // ==================================================
    // BOOK COUNT
    // ==================================================

    getBookCount(category) {

      if (
        category.count !== undefined &&
        category.count !== null
      ) {

        return category.count;

      }


      if (
        category.bookCount !== undefined &&
        category.bookCount !== null
      ) {

        return category.bookCount;

      }


      return 0;

    }

  }

};

</script>


<style scoped>

.category-card {

  display:
    flex;

  align-items:
    center;

  background:
    #fff;

  border:
    1px solid #eee;

  border-radius:
    12px;

  padding:
    0.6rem 1rem;

  flex:
    1;

  min-width:
    150px;

  box-shadow:
    0 1px 3px
    rgba(0, 0, 0, 0.04);

  transition:
    box-shadow 0.2s ease,
    transform 0.2s ease;

  cursor:
    pointer;

}


.category-card:hover {

  box-shadow:
    0 4px 12px
    rgba(0, 0, 0, 0.08);

  transform:
    translateY(-2px);

}


/* ==================================================
   TEXT
================================================== */

.card-text {

  display:
    flex;

  flex-direction:
    column;

}


.card-title {

  font-size:
    0.9rem;

  font-weight:
    600;

  color:
    #1f2937;

  margin:
    0;

}


.card-count {

  font-size:
    0.78rem;

  color:
    #9ca3af;

  margin:
    0.25rem 0 0;

}

</style>