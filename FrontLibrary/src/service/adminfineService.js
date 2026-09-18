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
     Handle Error
  ========================= */

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


  /* =========================
     No Content
  ========================= */

  if (response.status === 204) {
    return null;
  }


  /* =========================
     Read Response
  ========================= */

  const text = await response.text();

  return text ? JSON.parse(text) : null;
}


/* =========================
   Get All Fines
   Backend:
   GET /api/fine/getAllFine
========================= */

export async function getFines() {

  return await request(`${API_URL}/getAllFine`, {
    method: "GET"
  });

}


/* =========================
   Get Fine By ID
   Backend:
   GET /api/fine/getFinebyId/{id}
========================= */

export async function getFineById(id) {

  return await request(`${API_URL}/getFinebyId/${id}`, {
    method: "GET"
  });

}


/* =========================
   Create Fine
   Backend:
   POST /api/fine/createFine
========================= */

export async function createFine(data) {

  return await request(`${API_URL}/createFine`, {
    method: "POST",
    body: JSON.stringify(data)
  });

}


/* =========================
   Update Fine
   Backend:
   PUT /api/fine/updateFine/{id}
========================= */

export async function updateFine(id, data) {

  return await request(`${API_URL}/updateFine/${id}`, {
    method: "PUT",
    body: JSON.stringify(data)
  });

}


/* =========================
   Mark Fine As Paid
   Backend:
   PUT /api/fine/payFine/{id}/pay
========================= */

export async function markFineAsPaid(id) {

  return await request(`${API_URL}/payFine/${id}/pay`, {
    method: "PUT"
  });

}


/* =========================
   Delete Fine
   Backend:
   DELETE /api/fine/deleteFine/{id}
========================= */

export async function deleteFine(id) {

  return await request(`${API_URL}/deleteFine/${id}`, {
    method: "DELETE"
  });

}


/* =========================
   Get Fine Summary
   Backend:
   GET /api/fine/getsummary
========================= */

export async function getFineSummary() {

  return await request(`${API_URL}/getsummary`, {
    method: "GET"
  });

}