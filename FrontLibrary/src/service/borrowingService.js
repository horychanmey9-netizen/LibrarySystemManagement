const API_URL = "http://localhost:8080/api/borrower";


// =====================================================
// GET ALL BORROWINGS
// ADMIN / USER
// GET /api/borrower/getData
// =====================================================

export async function getBorrowings() {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/getData`,
    {
      method: "GET",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );

  const result = await response.json();

  console.log(
    "GET BORROWINGS RESPONSE:",
    result
  );

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch borrowings (${response.status})`
    );

  }

  if (Array.isArray(result?.data)) {
    return result.data;
  }

  if (Array.isArray(result)) {
    return result;
  }

  return [];
}


// =====================================================
// GET MY BORROWINGS
// USER
//
// NOTE:
// Backend currently returns all data from /getData.
// Filtering by current user should be done in backend later.
// =====================================================

export async function getMyBorrowings() {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/getData`,
    {
      method: "GET",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );

  const result = await response.json();

  console.log(
    "GET MY BORROWINGS RESPONSE:",
    result
  );

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch my borrowings (${response.status})`
    );

  }

  if (Array.isArray(result?.data)) {
    return result.data;
  }

  if (Array.isArray(result)) {
    return result;
  }

  return [];
}


// =====================================================
// GET BORROWING BY ID
// GET /api/borrower/getById/{id}
// =====================================================

export async function getBorrowingById(id) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/getById/${id}`,
    {
      method: "GET",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );

  const result = await response.json();

  console.log(
    "GET BORROWING BY ID RESPONSE:",
    result
  );

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch borrowing (${response.status})`
    );

  }

  return result?.data ?? result;
}


// =====================================================
// CREATE BORROWING
// ADMIN
// POST /api/borrower/create
// =====================================================

export async function createBorrowing(payload) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/create`,
    {
      method: "POST",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },

      body: JSON.stringify(payload)
    }
  );

  const result = await response.json();

  console.log(
    "CREATE BORROWING RESPONSE:",
    result
  );

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to create borrowing (${response.status})`
    );

  }

  return result?.data ?? result;
}


// =====================================================
// UPDATE BORROWING
// ADMIN
// PUT /api/borrower/update/{id}
// =====================================================

export async function updateBorrowing(
  id,
  payload
) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/update/${id}`,
    {
      method: "PUT",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      },

      body: JSON.stringify(payload)
    }
  );

  const result = await response.json();

  console.log(
    "UPDATE BORROWING RESPONSE:",
    result
  );

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to update borrowing (${response.status})`
    );

  }

  return result?.data ?? result;
}


// =====================================================
// DELETE BORROWING
// ADMIN
// DELETE /api/borrower/delete/{id}
// =====================================================

export async function deleteBorrowing(id) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/delete/${id}`,
    {
      method: "DELETE",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );

  const result = await response.json();

  console.log(
    "DELETE BORROWING RESPONSE:",
    result
  );

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to delete borrowing (${response.status})`
    );

  }

  return result;
}