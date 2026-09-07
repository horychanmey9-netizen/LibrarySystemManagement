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

  if (!response.ok) {

    let message = `Request failed: ${response.status}`;

    try {
      const errorData = await response.json();

      message =
        errorData.message ||
        errorData.error ||
        message;

    } catch {
      // Response is not JSON
    }

    throw new Error(message);
  }

  /*
   * DELETE / update endpoint may not return JSON.
   */
  if (response.status === 204) {
    return null;
  }

  const text = await response.text();

  return text ? JSON.parse(text) : null;
}


/* =========================
   Get All Fines
========================= */

export async function getFines() {

  return await request(`${API_URL}/getData`, {
    method: "GET"
  });

}


/* =========================
   Get Fine By ID
========================= */

export async function getFineById(id) {

  return await request(`${API_URL}/getById/${id}`, {
    method: "GET"
  });

}


/* =========================
   Mark Fine As Paid
========================= */

export async function markFineAsPaid(id) {

  return await request(`${API_URL}/mark-paid/${id}`, {
    method: "PUT"
  });

}


/* =========================
   Delete Fine
========================= */

export async function deleteFine(id) {

  return await request(`${API_URL}/delete/${id}`, {
    method: "DELETE"
  });

}