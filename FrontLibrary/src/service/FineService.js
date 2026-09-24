// =====================================================
// FINE SERVICE
// =====================================================

const API_URL = "http://localhost:8080/api/fine";


// =====================================================
// GET TOKEN
// =====================================================

function getToken() {

  return sessionStorage.getItem("token");

}


// =====================================================
// GET HEADERS
// =====================================================

function getHeaders() {

  const token = getToken();

  return {
    "Content-Type": "application/json",
    "Authorization": `Bearer ${token}`
  };

}


// =====================================================
// HANDLE RESPONSE
// =====================================================

async function handleResponse(response) {

  let result = null;

  try {

    result = await response.json();

  } catch (error) {

    result = null;

  }


  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.error ||
      `Request failed with status ${response.status}`
    );

  }


  return result;

}


// =====================================================
// GET ALL FINES
// =====================================================
// User + Admin
//
// GET /api/fine/getAllFine
// =====================================================

export async function getFines() {

  try {

    const response =
      await fetch(
        `${API_URL}/getAllFine`,
        {
          method: "GET",
          headers: getHeaders()
        }
      );


    const result =
      await handleResponse(response);


    return result?.data || [];

  } catch (error) {

    console.error(
      "Get fines error:",
      error
    );

    throw error;

  }

}


// =====================================================
// GET MY FINES
// =====================================================
// User
//
// Backend currently returns all fines.
// This function filters by logged-in userId.
// =====================================================

export async function getMyFines() {

  try {

    const fines =
      await getFines();


    // -----------------------------------------------
    // Get user from sessionStorage
    // -----------------------------------------------

    let userId = null;


    const userData =
      sessionStorage.getItem("user");


    if (userData) {

      try {

        const user =
          JSON.parse(userData);


        userId =
          user?.id ||
          user?.userId;

      } catch (error) {

        console.warn(
          "Cannot parse user data:",
          error
        );

      }

    }


    // -----------------------------------------------
    // Fallback userId
    // -----------------------------------------------

    if (!userId) {

      userId =
        sessionStorage.getItem("userId");

    }


    // -----------------------------------------------
    // No user ID
    // -----------------------------------------------

    if (!userId) {

      console.warn(
        "User ID not found."
      );

      return [];

    }


    // -----------------------------------------------
    // Filter current user's fines
    // -----------------------------------------------

    return fines.filter(

      fine =>
        Number(fine?.userId) ===
        Number(userId)

    );

  } catch (error) {

    console.error(
      "Get my fines error:",
      error
    );

    throw error;

  }

}


// =====================================================
// GET FINE BY ID
// =====================================================
// GET /api/fine/getFinebyId/{id}
// =====================================================

export async function getFineById(id) {

  if (!id) {

    throw new Error(
      "Fine ID is required."
    );

  }


  try {

    const response =
      await fetch(
        `${API_URL}/getFinebyId/${id}`,
        {
          method: "GET",
          headers: getHeaders()
        }
      );


    const result =
      await handleResponse(response);


    return result?.data || null;

  } catch (error) {

    console.error(
      "Get fine by ID error:",
      error
    );

    throw error;

  }

}


// =====================================================
// CREATE FINE
// =====================================================
// ADMIN
//
// POST /api/fine/createFine
// =====================================================

export async function createFine(fineData) {

  try {

    const response =
      await fetch(
        `${API_URL}/createFine`,
        {
          method: "POST",

          headers: getHeaders(),

          body: JSON.stringify(
            fineData
          )

        }
      );


    const result =
      await handleResponse(response);


    return result?.data || null;

  } catch (error) {

    console.error(
      "Create fine error:",
      error
    );

    throw error;

  }

}


// =====================================================
// UPDATE FINE
// =====================================================
// ADMIN
//
// PUT /api/fine/updateFine/{id}
// =====================================================

export async function updateFine(
  id,
  fineData
) {

  if (!id) {

    throw new Error(
      "Fine ID is required."
    );

  }


  try {

    const response =
      await fetch(
        `${API_URL}/updateFine/${id}`,
        {
          method: "PUT",

          headers: getHeaders(),

          body: JSON.stringify(
            fineData
          )

        }
      );


    const result =
      await handleResponse(response);


    return result?.data || null;

  } catch (error) {

    console.error(
      "Update fine error:",
      error
    );

    throw error;

  }

}


// =====================================================
// DELETE FINE
// =====================================================
// ADMIN
//
// DELETE /api/fine/deleteFine/{id}
// =====================================================

export async function deleteFine(id) {

  if (!id) {

    throw new Error(
      "Fine ID is required."
    );

  }


  try {

    const response =
      await fetch(
        `${API_URL}/deleteFine/${id}`,
        {
          method: "DELETE",

          headers: getHeaders()
        }
      );


    const result =
      await handleResponse(response);


    return result;

  } catch (error) {

    console.error(
      "Delete fine error:",
      error
    );

    throw error;

  }

}


// =====================================================
// GET FINE SUMMARY
// =====================================================
// ADMIN
//
// GET /api/fine/getsummary
// =====================================================

export async function getFineSummary() {

  try {

    const response =
      await fetch(
        `${API_URL}/getsummary`,
        {
          method: "GET",
          headers: getHeaders()
        }
      );


    const result =
      await handleResponse(response);


    return result?.data || null;

  } catch (error) {

    console.error(
      "Get fine summary error:",
      error
    );

    throw error;

  }

}