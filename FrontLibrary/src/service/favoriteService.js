
const API_URL = "http://localhost:8080/api/favorite";


// =====================================================
// GET FAVORITE BOOKS
// =====================================================

export async function getFavorites() {

  const token = sessionStorage.getItem("token");

  const response = await fetch(API_URL, {
    method: "GET",

    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    }
  });

  if (!response.ok) {
    throw new Error("Failed to get favorite books");
  }

  const result = await response.json();

  return result.data || [];
}


// =====================================================
// ADD FAVORITE
// =====================================================

export async function addFavorite(bookId) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(API_URL, {
    method: "POST",

    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    },

    body: JSON.stringify({
      bookId: bookId
    })
  });

  if (!response.ok) {

    const errorData = await response.json().catch(() => null);

    throw new Error(
      errorData?.msg || "Failed to add favorite"
    );
  }

  const result = await response.json();

  return result.data;
}


// =====================================================
// DELETE FAVORITE
// =====================================================

export async function deleteFavorite(bookId) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/${bookId}`,
    {
      method: "DELETE",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );

  if (!response.ok) {

    const errorData = await response.json().catch(() => null);

    throw new Error(
      errorData?.msg || "Failed to remove favorite"
    );
  }

  return await response.json();
}

