<template>
  <div class="actions-card">

    <!-- =========================
         Action Info
    ========================== -->

    <div class="action-info">

      <div class="icon">
        <i class="bi bi-bar-chart-line"></i>
      </div>

      <div>
        <h3>Export Reports</h3>

        <p>
          Download the current report data for further analysis.
        </p>
      </div>

    </div>


    <!-- =========================
         Export Buttons
    ========================== -->

    <div class="action-buttons">

      <!-- Export PDF -->
      <button
        class="export-btn pdf-btn"
        type="button"
        @click="exportPDF"
      >
        <i class="bi bi-file-earmark-pdf"></i>
        <span>Export PDF</span>
      </button>


      <!-- Export Excel -->
      <button
        class="export-btn excel-btn"
        type="button"
        @click="exportExcel"
      >
        <i class="bi bi-file-earmark-spreadsheet"></i>
        <span>Export Excel</span>
      </button>

    </div>

  </div>
</template>


<script setup>

// ========================================
// Export PDF
// ========================================

const exportPDF = () => {

  console.log("Export PDF");

  alert(
    "PDF export will be connected to the backend."
  );

};


// ========================================
// Export Excel
// ========================================

const exportExcel = () => {

  console.log("Export Excel");

  alert(
    "Excel export will be connected to the backend."
  );

};

</script>


<style scoped>

/* ========================================
   Actions Card
======================================== */

.actions-card {

  margin-top: 24px;

  background: #ffffff;

  border: 1px solid #e5e7eb;

  border-radius: 14px;

  padding: 20px 24px;

  display: flex;

  justify-content: space-between;

  align-items: center;

  gap: 20px;

  box-shadow:
    0 2px 8px
    rgba(0, 0, 0, 0.04);

}


/* ========================================
   Action Info
======================================== */

.action-info {

  display: flex;

  align-items: center;

  gap: 14px;

}


/* ========================================
   Icon
======================================== */

.icon {

  width: 45px;

  height: 45px;

  min-width: 45px;

  border-radius: 10px;

  background: #eff6ff;

  display: flex;

  align-items: center;

  justify-content: center;

  color: #2563eb;

  font-size: 20px;

}


/* ========================================
   Title
======================================== */

.action-info h3 {

  margin: 0;

  color: #111827;

  font-size: 16px;

  font-weight: 600;

}


/* ========================================
   Description
======================================== */

.action-info p {

  margin: 4px 0 0;

  color: #6b7280;

  font-size: 12px;

  line-height: 1.5;

}


/* ========================================
   Action Buttons
======================================== */

.action-buttons {

  display: flex;

  gap: 10px;

}


/* ========================================
   Export Button
======================================== */

.export-btn {

  border: none;

  padding: 10px 16px;

  border-radius: 8px;

  font-size: 13px;

  font-weight: 600;

  cursor: pointer;

  display: flex;

  align-items: center;

  justify-content: center;

  gap: 8px;

  transition: 0.2s;

}


/* ========================================
   Button Icons
======================================== */

.export-btn i {

  font-size: 15px;

}


/* ========================================
   PDF Button
======================================== */

.pdf-btn {

  background: #fee2e2;

  color: #b91c1c;

}


.pdf-btn:hover {

  background: #fecaca;

}


/* ========================================
   Excel Button
======================================== */

.excel-btn {

  background: #dcfce7;

  color: #15803d;

}


.excel-btn:hover {

  background: #bbf7d0;

}


/* ========================================
   General Hover
======================================== */

.export-btn:hover {

  transform: translateY(-1px);

}


.export-btn:active {

  transform: translateY(0);

}


/* ========================================
   Responsive
======================================== */

@media (max-width: 700px) {

  .actions-card {

    flex-direction: column;

    align-items: stretch;

  }


  .action-buttons {

    width: 100%;

  }


  .export-btn {

    flex: 1;

  }

}


@media (max-width: 450px) {

  .action-info {

    align-items: flex-start;

  }


  .action-buttons {

    flex-direction: column;

  }


  .export-btn {

    width: 100%;

  }

}

</style>