<template>
  <div class="fine-card">

    <!-- Book -->
    <div class="book-info">

      <div class="book-cover">
        <img
          v-if="fine.image"
          :src="fine.image"
          :alt="fine.title"
        />

        <div v-else class="no-cover">
          <i class="bi bi-book"></i>
        </div>
      </div>

      <div class="book-details">
        <h3>{{ fine.title }}</h3>

        <p class="author">
          {{ fine.author }}
        </p>

        <p v-if="fine.category" class="category">
          {{ fine.category }}
        </p>

        <p v-if="fine.isbn" class="isbn">
          ISBN: {{ fine.isbn }}
        </p>
      </div>

    </div>


    <!-- Fine Details -->
    <div class="fine-details">

      <div class="detail">
        <span>Due Date</span>
        <strong>{{ fine.dueDate }}</strong>
      </div>

      <div class="detail">
        <span>Returned Date</span>
        <strong>{{ fine.returnedDate }}</strong>
      </div>

      <div class="detail">
        <span>Late</span>
        <strong>{{ fine.lateDays }} days</strong>
      </div>

    </div>


    <!-- Amount -->
    <div class="fine-result">

      <span
        class="status"
        :class="fine.status === 'Paid'
          ? 'paid'
          : 'unpaid'"
      >
        {{ fine.status }}
      </span>

      <span class="amount">
        ${{ Number(fine.amount).toFixed(2) }}
      </span>

    </div>

  </div>
</template>


<script>
export default {
  name: "FineCard",

  props: {
    fine: {
      type: Object,
      required: true
    }
  }
};
</script>


<style scoped>

.fine-card {
  width: 100%;

  display: grid;
  grid-template-columns: 2fr 2fr auto;

  align-items: center;

  gap: 24px;

  padding: 20px;

  box-sizing: border-box;

  background: #ffffff;

  border: 1px solid #e5e7eb;

  border-radius: 14px;

  transition: 0.2s ease;
}

.fine-card:hover {
  transform: translateY(-2px);

  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}


/* =========================
   BOOK
========================= */

.book-info {
  display: flex;

  align-items: center;

  gap: 16px;

  min-width: 0;
}

.book-cover {
  width: 75px;
  height: 100px;

  flex-shrink: 0;

  overflow: hidden;

  border-radius: 8px;

  background: #f1f5f9;
}

.book-cover img {
  width: 100%;
  height: 100%;

  object-fit: cover;
}

.no-cover {
  width: 100%;
  height: 100%;

  display: flex;

  align-items: center;
  justify-content: center;

  color: #94a3b8;
}

.no-cover i {
  font-size: 28px;
}

.book-details {
  min-width: 0;
}

.book-details h3 {
  margin: 0;

  font-size: 16px;

  font-weight: 600;

  color: #1f2937;
}

.author {
  margin: 5px 0 0;

  font-size: 13px;

  color: #64748b;
}

.category {
  margin: 5px 0 0;

  font-size: 12px;

  color: #2563eb;
}

.isbn {
  margin: 4px 0 0;

  font-size: 11px;

  color: #9ca3af;
}


/* =========================
   FINE DETAILS
========================= */

.fine-details {
  display: grid;

  grid-template-columns:
    repeat(3, 1fr);

  gap: 16px;
}

.detail {
  display: flex;

  flex-direction: column;

  gap: 5px;
}

.detail span {
  font-size: 11px;

  color: #9ca3af;
}

.detail strong {
  font-size: 13px;

  font-weight: 500;

  color: #374151;
}


/* =========================
   RESULT
========================= */

.fine-result {
  min-width: 90px;

  display: flex;

  flex-direction: column;

  align-items: flex-end;

  gap: 8px;
}

.status {
  padding: 5px 10px;

  border-radius: 999px;

  font-size: 11px;

  font-weight: 600;
}

.status.unpaid {
  background: #fee2e2;

  color: #dc2626;
}

.status.paid {
  background: #dcfce7;

  color: #16a34a;
}

.amount {
  font-size: 20px;

  font-weight: 700;

  color: #ef4444;
}


/* =========================
   RESPONSIVE
========================= */

@media (max-width: 950px) {

  .fine-card {
    grid-template-columns: 1fr;

    align-items: stretch;
  }

  .fine-result {
    flex-direction: row;

    align-items: center;

    justify-content: space-between;
  }

}


@media (max-width: 600px) {

  .fine-details {
    grid-template-columns: 1fr;
  }

  .fine-card {
    padding: 16px;
  }

  .book-cover {
    width: 65px;
    height: 90px;
  }

}

</style>