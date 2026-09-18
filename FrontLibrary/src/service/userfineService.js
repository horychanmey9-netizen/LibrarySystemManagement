const API_URL = "http://localhost:8080/api/fine";

/* =========================
   Get Token
========================= */

function getToken() {
  return sessionStorage.getItem("token");
}

/* =========================
   Request Helper
========================= */

async function request(url, options = {}) {
  const token = getToken();

  if (!token) {
    throw new Error("Authentication token not found.");
  }

  const response = await fetch(url, {
    ...options,

    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
      ...(options.headers || {})
    }
  });

  /* =========================
     Handle HTTP Error
  ========================= */

  if (!response.ok) {
    let message = "";

    // Try to get error message from backend
    try {
      const errorData = await response.json();
      message = errorData?.message || "";
    } catch (error) {
      // Response is not JSON
    }

    if (response.status === 401) {
      throw new Error(
        message || "Your session has expired. Please login again."
      );
    }

    if (response.status === 403) {
      throw new Error(
        message || "You do not have permission to view fines."
      );
    }

    if (response.status === 404) {
      throw new Error(
        message || "Fine API endpoint was not found."
      );
    }

    throw new Error(
      message || `Request failed with status ${response.status}`
    );
  }

  /* =========================
     JSON Response
  ========================= */

  // Handle empty response
  const text = await response.text();

  if (!text) {
    return null;
  }

  try {
    return JSON.parse(text);
  } catch (error) {
    throw new Error("Invalid JSON response from server.");
  }
}

/* =========================
   Get My Fines
========================= */

export async function getMyFines() {
  const result = await request(
    `${API_URL}/getAllFine`,
    {
      method: "GET"
    }
  );

  /*
   * Backend response:
   *
   * {
   *   message: "Get fines successfully",
   *   status: 200,
   *   data: [...]
   * }
   *
   * OR
   *
   * [...]
   */

  if (result && result.data !== undefined) {
    return Array.isArray(result.data)
      ? result.data
      : [];
  }

  return Array.isArray(result)
    ? result
    : [];
}

/* =========================
   Export
========================= */

export default {
  getMyFines
};