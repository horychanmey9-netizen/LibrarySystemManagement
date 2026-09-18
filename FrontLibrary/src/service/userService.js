const API_URL = "http://localhost:8080/api/user";

/* =================================================
   GET USERS
================================================= */

export async function getUsers() {
  const token = sessionStorage.getItem("token");

  if (!token) {
    throw new Error("Authentication token not found.");
  }

  const response = await fetch(`${API_URL}/getData`, {
    method: "GET",

    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
  });

  if (!response.ok) {
    let message = `Failed to get users: ${response.status}`;

    try {
      const result = await response.json();
      message = result?.message || message;
    } catch {
      // Ignore invalid JSON response
    }

    throw new Error(message);
  }

  const result = await response.json();

  return result?.data ?? result;
}


/* =================================================
   DEACTIVATE USER
================================================= */

export async function deactivateUser(id) {
  const token = sessionStorage.getItem("token");

  if (!token) {
    throw new Error("Authentication token not found.");
  }

  const response = await fetch(
    `${API_URL}/deactivate/${id}`,
    {
      method: "PATCH",

      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!response.ok) {
    let message = `Failed to deactivate user: ${response.status}`;

    try {
      const result = await response.json();
      message = result?.message || message;
    } catch {
      // Ignore invalid JSON response
    }

    throw new Error(message);
  }

  return await response.json();
}


/* =================================================
   ACTIVATE USER
================================================= */

export async function activateUser(id) {
  const token = sessionStorage.getItem("token");

  if (!token) {
    throw new Error("Authentication token not found.");
  }

  const response = await fetch(
    `${API_URL}/activate/${id}`,
    {
      method: "PATCH",

      headers: {
        "Content-Type": "application/json",
        Authorization: `Bearer ${token}`,
      },
    }
  );

  if (!response.ok) {
    let message = `Failed to activate user: ${response.status}`;

    try {
      const result = await response.json();
      message = result?.message || message;
    } catch {
      // Ignore invalid JSON response
    }

    throw new Error(message);
  }

  return await response.json();
}


/* =================================================
   DEFAULT EXPORT
================================================= */

export default {
  getUsers,
  deactivateUser,
  activateUser,
};