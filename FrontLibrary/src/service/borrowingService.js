const API_URL = "https://librarymanagementsystem-ukyq.onrender.com/api/borrower";


// =====================================================
// GET TOKEN
// =====================================================

function getToken() {
  return sessionStorage.getItem("token");
}


// =====================================================
// PARSE RESPONSE
// Supports:
// - JSON response
// - Plain text response
// =====================================================

async function parseResponse(response) {
  const contentType =
    response.headers.get("content-type") || "";

  // -----------------------------------------------
  // JSON response
  // -----------------------------------------------

  if (contentType.includes("application/json")) {
    try {
      return await response.json();
    } catch (error) {
      return null;
    }
  }

  // -----------------------------------------------
  // Plain text response
  // -----------------------------------------------

  try {
    const text = await response.text();

    return text || null;
  } catch (error) {
    return null;
  }
}


// =====================================================
// GET ERROR MESSAGE
// =====================================================

function getErrorMessage(result, response, fallback) {

  // Backend returns plain text
  if (typeof result === "string") {
    return (
      result.trim() ||
      fallback
    );
  }


  // Backend returns JSON
  if (result && typeof result === "object") {

    return (
      result.message ||
      result.msg ||
      result.error ||
      result.data?.message ||
      fallback
    );

  }


  return (
    `${fallback} (${response.status})`
  );
}


// =====================================================
// GET RESPONSE DATA
// =====================================================

function getResponseData(result) {

  // If backend returns:
  // "some text"
  if (typeof result === "string") {
    return result;
  }


  // If backend returns:
  // { data: {...} }
  if (
    result &&
    typeof result === "object" &&
    result.data !== undefined
  ) {
    return result.data;
  }


  // If backend directly returns object
  return result;
}


// =====================================================
// GET ALL BORROWINGS - ADMIN
// =====================================================

export async function getBorrowings() {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/getData`,
    {
      method: "GET",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to fetch borrowings"
      )
    );

  }


  // Backend directly returns array
  if (Array.isArray(result)) {
    return result;
  }


  // Backend returns { data: [] }
  if (
    result &&
    Array.isArray(result.data)
  ) {
    return result.data;
  }


  return [];
}


// =====================================================
// GET MY BORROWINGS - USER
// =====================================================

export async function getMyBorrowings() {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/getData`,
    {
      method: "GET",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to fetch my borrowings"
      )
    );

  }


  // Direct array
  if (Array.isArray(result)) {
    return result;
  }


  // { data: [] }
  if (
    result &&
    Array.isArray(result.data)
  ) {
    return result.data;
  }


  return [];
}


// =====================================================
// GET BORROWING BY ID
// =====================================================

export async function getBorrowingById(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/getById/${id}`,
    {
      method: "GET",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to fetch borrowing"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// CREATE BORROW REQUEST - USER
//
// PENDING
//
// Important:
// Same user cannot borrow the same book again
// while existing status is:
// - PENDING
// - BORROWED
// - RETURN_REQUESTED
// - OVERDUE
//
// User can borrow again after:
// - RETURNED
// - REJECTED
// =====================================================

export async function createBorrowing(payload) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/create`,
    {
      method: "POST",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      },

      body: JSON.stringify(payload)
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "CREATE BORROWING RESPONSE:",
    result
  );


  // -----------------------------------------------
  // Error
  // -----------------------------------------------

  if (!response.ok) {

    const message =
      getErrorMessage(
        result,
        response,
        "Failed to create borrow request"
      );


    throw new Error(message);

  }


  // -----------------------------------------------
  // Success
  // -----------------------------------------------

  return getResponseData(result);
}


// =====================================================
// ACCEPT BORROWING - ADMIN
//
// PENDING -> BORROWED
//
// Backend:
// book.qty - 1
// =====================================================

export async function acceptBorrowing(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/accept/${id}`,
    {
      method: "PUT",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "ACCEPT BORROWING RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to accept borrowing"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// REJECT BORROWING - ADMIN
//
// PENDING -> REJECTED
//
// Book quantity does NOT change.
// =====================================================

export async function rejectBorrowing(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/reject/${id}`,
    {
      method: "PUT",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "REJECT BORROWING RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to reject borrowing"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// REQUEST RETURN BOOK - USER
//
// BORROWED -> RETURN_REQUESTED
//
// Important:
// Book quantity is NOT increased here.
// =====================================================

export async function returnBook(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/return/${id}`,
    {
      method: "PUT",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "RETURN BOOK RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to request return"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// ACCEPT RETURN - ADMIN
//
// RETURN_REQUESTED -> RETURNED
//
// Backend:
// book.qty + 1
// returnDate = today
// =====================================================

export async function acceptReturn(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/accept-return/${id}`,
    {
      method: "PUT",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "ACCEPT RETURN RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to accept return"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// REJECT RETURN - ADMIN
//
// RETURN_REQUESTED -> BORROWED
//
// NOTE:
// This endpoint must exist in backend:
//
// PUT /api/borrower/reject-return/{id}
// =====================================================

export async function rejectReturn(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/reject-return/${id}`,
    {
      method: "PUT",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "REJECT RETURN RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to reject return"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// UPDATE BORROWING - ADMIN
// =====================================================

export async function updateBorrowing(
  id,
  payload
) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/update/${id}`,
    {
      method: "PUT",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      },

      body: JSON.stringify(payload)
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "UPDATE BORROWING RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to update borrowing"
      )
    );

  }


  return getResponseData(result);
}


// =====================================================
// DELETE BORROWING - ADMIN
// =====================================================

export async function deleteBorrowing(id) {

  const token = getToken();


  const response = await fetch(
    `${API_URL}/delete/${id}`,
    {
      method: "DELETE",

      headers: {
        Authorization:
          `Bearer ${token}`,

        "Content-Type":
          "application/json"
      }
    }
  );


  const result =
    await parseResponse(response);


  console.log(
    "DELETE BORROWING RESPONSE:",
    result
  );


  if (!response.ok) {

    throw new Error(
      getErrorMessage(
        result,
        response,
        "Failed to delete borrowing"
      )
    );

  }


  return result;
}