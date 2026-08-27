const API_URL =
  "http://localhost:8080/api/user";

export async function getUsers() {

  const token =
    sessionStorage.getItem("token");

  const response =
    await fetch(
      `${API_URL}/getData`,
      {
        method: "GET",

        headers: {
          "Content-Type": "application/json",
          Authorization: `Bearer ${token}`
        }
      }
    );

  if (!response.ok) {

    throw new Error(
      `Failed to get users: ${response.status}`
    );

  }

  const result =
    await response.json();

  return result?.data ?? result;
}