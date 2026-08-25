const API_URL = "http://localhost:8080/api/borrower";


// ==============================
// Get Token
// ==============================

function getToken() {
  return sessionStorage.getItem("token");
}


// ==============================
// Get All Borrowings
// ==============================

export async function getBorrowings() {

  const token = getToken();

  console.log("Borrowing API Token:", token);

  if (!token) {
    throw new Error("Authentication token not found. Please login again.");
  }

  const response = await fetch(
    `${API_URL}/getData`,
    {
      method: "GET",

      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }
  );

  if (!response.ok) {
    throw new Error(
      `Failed to fetch borrowings: ${response.status}`
    );
  }

  const result = await response.json();

  console.log("Borrowing API Response:", result);

  return result.data || [];
}


// ==============================
// Get Borrowing By ID
// ==============================

export async function getBorrowingById(id) {

  const token = getToken();

  if (!token) {
    throw new Error("Authentication token not found. Please login again.");
  }

  const response = await fetch(
    `${API_URL}/getById/${id}`,
    {
      method: "GET",

      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }
  );

  if (!response.ok) {
    throw new Error(
      `Failed to fetch borrowing: ${response.status}`
    );
  }

  const result = await response.json();

  return result.data;
}


// ==============================
// Update Borrowing
// ==============================

export async function updateBorrowing(
  id,
  borrowingData
) {

  const token = getToken();

  if (!token) {
    throw new Error("Authentication token not found. Please login again.");
  }

  const response = await fetch(
    `${API_URL}/update/${id}`,
    {
      method: "PUT",

      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      },

      body: JSON.stringify(borrowingData)
    }
  );

  if (!response.ok) {
    throw new Error(
      `Failed to update borrowing: ${response.status}`
    );
  }

  const result = await response.json();

  return result.data;
}


// ==============================
// Delete Borrowing
// ==============================

export async function deleteBorrowing(id) {

  const token = getToken();

  if (!token) {
    throw new Error("Authentication token not found. Please login again.");
  }

  const response = await fetch(
    `${API_URL}/delete/${id}`,
    {
      method: "DELETE",

      headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${token}`
      }
    }
  );

  if (!response.ok) {
    throw new Error(
      `Failed to delete borrowing: ${response.status}`
    );
  }

  return await response.json();
}