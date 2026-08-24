<template>

  <div
    v-if="user"
    class="modal-overlay"
    @click.self="$emit('close')"
  >

    <div class="modal">

      <!-- =========================
           HEADER
      ========================== -->

      <div class="modal-header">

        <div>

          <h2>
            User Details
          </h2>

          <p>
            View user information
          </p>

        </div>


        <button
          type="button"
          class="close-btn"
          @click="$emit('close')"
        >

          <i class="bi bi-x-lg"></i>

        </button>

      </div>


      <!-- =========================
           PROFILE
      ========================== -->

      <div class="profile-section">

        <div
          class="detail-avatar"
          :class="
            String(user.role).toUpperCase() === 'ADMIN'
              ? 'admin-avatar'
              : 'user-avatar'
          "
        >
          {{ getInitial(user.name) }}
        </div>


        <h3>
          {{ user.name }}
        </h3>


        <span
          class="role-badge"
          :class="String(user.role).toLowerCase()"
        >
          {{ user.role }}
        </span>

      </div>


      <!-- =========================
           DETAILS
      ========================== -->

      <div class="details-grid">

        <!-- ID -->

        <div class="detail-item">

          <span>
            User ID
          </span>

          <strong>
            #{{ user.id }}
          </strong>

        </div>


        <!-- NAME -->

        <div class="detail-item">

          <span>
            Full Name
          </span>

          <strong>
            {{ user.name }}
          </strong>

        </div>


        <!-- EMAIL -->

        <div class="detail-item">

          <span>
            Email
          </span>

          <strong>
            {{ user.email }}
          </strong>

        </div>


        <!-- PHONE -->

        <div class="detail-item">

          <span>
            Phone
          </span>

          <strong>
            {{ user.phone || "-" }}
          </strong>

        </div>


        <!-- ROLE -->

        <div class="detail-item">

          <span>
            Role
          </span>

          <strong>
            {{ user.role }}
          </strong>

        </div>


        <!-- CREATED -->

        <div class="detail-item">

          <span>
            Created Date
          </span>

          <strong>
            {{ user.createdDate }}
          </strong>

        </div>

      </div>


      <!-- =========================
           FOOTER
      ========================== -->

      <div class="modal-actions">

        <button
          type="button"
          class="cancel-btn"
          @click="$emit('close')"
        >
          Close
        </button>

      </div>

    </div>

  </div>

</template>


<script setup>

defineProps({

  user: {
    type: Object,
    default: null
  }

});


defineEmits([
  "close"
]);


/* =========================
   INITIAL
========================= */

function getInitial(name) {

  if (!name) {
    return "?";
  }

  return name
    .charAt(0)
    .toUpperCase();

}

</script>


<style scoped>

/* =========================
   OVERLAY
========================= */

.modal-overlay {

  position: fixed;

  inset: 0;

  z-index: 1000;

  padding: 20px;

  background: rgba(15, 23, 42, 0.45);

  display: flex;

  align-items: center;

  justify-content: center;

}


/* =========================
   MODAL
========================= */

.modal {

  width: 100%;

  max-width: 520px;

  max-height: 90vh;

  overflow-y: auto;

  background: white;

  border-radius: 16px;

  padding: 24px;

  box-shadow:
    0 25px 60px rgba(0, 0, 0, 0.18);

}


/* =========================
   HEADER
========================= */

.modal-header {

  display: flex;

  align-items: flex-start;

  justify-content: space-between;

  padding-bottom: 18px;

  border-bottom: 1px solid #f1f5f9;

}


.modal-header h2 {

  margin: 0;

  color: #1e293b;

  font-size: 20px;

  font-weight: 700;

}


.modal-header p {

  margin: 5px 0 0;

  color: #94a3b8;

  font-size: 13px;

}


/* =========================
   CLOSE
========================= */

.close-btn {

  width: 34px;

  height: 34px;

  border: none;

  border-radius: 8px;

  background: #f8fafc;

  color: #64748b;

  display: flex;

  align-items: center;

  justify-content: center;

  cursor: pointer;

  transition: 0.2s;

}


.close-btn:hover {

  background: #f1f5f9;

  color: #1e293b;

}


/* =========================
   PROFILE
========================= */

.profile-section {

  display: flex;

  flex-direction: column;

  align-items: center;

  padding: 24px 0;

}


.detail-avatar {

  width: 78px;

  height: 78px;

  border-radius: 50%;

  display: flex;

  align-items: center;

  justify-content: center;

  font-size: 28px;

  font-weight: 700;

  margin-bottom: 10px;

}


.admin-avatar {

  background: #ede9fe;

  color: #7c3aed;

}


.user-avatar {

  background: #e0e7ff;

  color: #4f46e5;

}


.profile-section h3 {

  margin: 0 0 8px;

  color: #1e293b;

  font-size: 19px;

}


/* =========================
   ROLE
========================= */

.role-badge {

  display: inline-flex;

  padding: 5px 12px;

  border-radius: 999px;

  font-size: 12px;

  font-weight: 600;

}


.role-badge.admin {

  background: #ede9fe;

  color: #7c3aed;

}


.role-badge.user {

  background: #e0f2fe;

  color: #0369a1;

}


/* =========================
   DETAILS
========================= */

.details-grid {

  display: grid;

  grid-template-columns: 1fr 1fr;

  gap: 12px;

}


.detail-item {

  padding: 14px;

  background: #f8fafc;

  border-radius: 10px;

}


.detail-item span {

  display: block;

  margin-bottom: 5px;

  color: #94a3b8;

  font-size: 12px;

}


.detail-item strong {

  display: block;

  color: #334155;

  font-size: 14px;

  word-break: break-word;

}


/* =========================
   ACTIONS
========================= */

.modal-actions {

  display: flex;

  justify-content: flex-end;

  margin-top: 22px;

  padding-top: 18px;

  border-top: 1px solid #f1f5f9;

}


.cancel-btn {

  height: 40px;

  padding: 0 18px;

  border: 1px solid #d1d5db;

  border-radius: 8px;

  background: white;

  color: #475569;

  font-size: 14px;

  font-weight: 600;

  cursor: pointer;

}


.cancel-btn:hover {

  background: #f8fafc;

}


/* =========================
   MOBILE
========================= */

@media (max-width: 600px) {

  .modal {

    padding: 18px;

  }


  .details-grid {

    grid-template-columns: 1fr;

  }

}

</style>