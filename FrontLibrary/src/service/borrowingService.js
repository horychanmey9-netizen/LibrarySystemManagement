const API_URL = "http://localhost:8080/api/borrower";

function getToken() {
  return sessionStorage.getItem("token");
}

// ================================
// GET ALL BORROWINGS - ADMIN
// ================================
export async function getBorrowings() {
  const token = getToken();

  const response = await fetch(`${API_URL}/getData`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch borrowings (${response.status})`
    );
  }

  if (Array.isArray(result)) {
    return result;
  }

  if (Array.isArray(result?.data)) {
    return result.data;
  }

  return [];
}

// ================================
// GET MY BORROWINGS - USER
// ================================
export async function getMyBorrowings() {
  const token = getToken();

  const response = await fetch(`${API_URL}/getData`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch my borrowings (${response.status})`
    );
  }

  if (Array.isArray(result)) {
    return result;
  }

  if (Array.isArray(result?.data)) {
    return result.data;
  }

  return [];
}

// ================================
// GET BORROWING BY ID
// ================================
export async function getBorrowingById(id) {
  const token = getToken();

  const response = await fetch(`${API_URL}/getById/${id}`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch borrowing (${response.status})`
    );
  }

  return result?.data ?? result;
}

// ================================
// CREATE BORROW REQUEST - USER
// ================================
export async function createBorrowing(payload) {
  const token = getToken();

  const response = await fetch(`${API_URL}/create`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  const result = await response.json();

  console.log("CREATE BORROWING RESPONSE:", result);

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to create borrow request (${response.status})`
    );
  }

  return result?.data ?? result;
}

// ================================
// ACCEPT BORROWING - ADMIN
// ================================
export async function acceptBorrowing(id) {
  const token = getToken();

  const response = await fetch(`${API_URL}/accept/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  const result = await response.json();

  console.log("ACCEPT BORROWING RESPONSE:", result);

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to accept borrowing (${response.status})`
    );
  }

  return result?.data ?? result;
}

// ================================
// REJECT BORROWING - ADMIN
// ================================
export async function rejectBorrowing(id) {
  const token = getToken();

  const response = await fetch(`${API_URL}/reject/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  const result = await response.json();

  console.log("REJECT BORROWING RESPONSE:", result);

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to reject borrowing (${response.status})`
    );
  }

  return result?.data ?? result;
}

// ================================
// UPDATE BORROWING
// ================================
export async function updateBorrowing(id, payload) {
  const token = getToken();

  const response = await fetch(`${API_URL}/update/${id}`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
    body: JSON.stringify(payload),
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to update borrowing (${response.status})`
    );
  }

  return result?.data ?? result;
}

// ================================
// DELETE BORROWING
// ================================
export async function deleteBorrowing(id) {
  const token = getToken();

  const response = await fetch(`${API_URL}/delete/${id}`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
    },
  });

  const result = await response.json();

  if (!response.ok) {
    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to delete borrowing (${response.status})`
    );
  }

  return result;
}