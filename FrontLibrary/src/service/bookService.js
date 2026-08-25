const API_URL = "http://localhost:8080/api/book";


// =====================================================
// GET ALL BOOKS
// =====================================================

export async function getBooks() {

  const token = sessionStorage.getItem("token");

  const response = await fetch(API_URL, {
    method: "GET",

    headers: {
      "Authorization": `Bearer ${token}`,
      "Content-Type": "application/json"
    }
  });

  const text = await response.text();

  let data;

  try {
    data = text ? JSON.parse(text) : null;
  } catch (error) {
    console.error("Invalid JSON response:", text);
    throw new Error(text || "Invalid server response");
  }

  if (!response.ok) {
    throw new Error(
      data?.msg ||
      data?.message ||
      `Failed to get books (${response.status})`
    );
  }

  console.log("BOOK API RESPONSE:", data);

  return data;
}


// =====================================================
// CREATE BOOK
// Backend:
// POST /api/book
// Content-Type: multipart/form-data
// =====================================================

export async function createBook(bookRequest, imageFile) {

  const token = sessionStorage.getItem("token");

  const formData = new FormData();


  // ===================================================
  // BOOK REQUEST
  // ===================================================

  formData.append(
    "categoryId",
    bookRequest.categoryId
  );

  formData.append(
    "title",
    bookRequest.title
  );

  formData.append(
    "qty",
    bookRequest.qty
  );

  formData.append(
    "description",
    bookRequest.description || ""
  );

  formData.append(
    "author",
    bookRequest.author
  );

  formData.append(
    "pages",
    bookRequest.pages || ""
  );

  formData.append(
    "isbn",
    bookRequest.isbn || ""
  );

  formData.append(
    "language",
    bookRequest.language || ""
  );

  formData.append(
    "status",
    bookRequest.status || "Available"
  );


  // ===================================================
  // IMAGE
  // ===================================================

  if (imageFile) {

    formData.append(
      "file",
      imageFile
    );

  }


  console.log(
    "CREATE BOOK REQUEST:",
    bookRequest
  );

  console.log(
    "CREATE BOOK IMAGE:",
    imageFile
  );


  const response = await fetch(
    API_URL,
    {
      method: "POST",

      headers: {
        "Authorization": `Bearer ${token}`
      },

      body: formData
    }
  );


  const text = await response.text();

  let data;

  try {

    data = text
      ? JSON.parse(text)
      : null;

  } catch (error) {

    console.error(
      "CREATE BOOK INVALID RESPONSE:",
      text
    );

    throw new Error(
      text ||
      "Server returned an invalid response"
    );

  }


  if (!response.ok) {

    throw new Error(
      data?.msg ||
      data?.message ||
      `Failed to create book (${response.status})`
    );

  }


  console.log(
    "CREATE BOOK RESPONSE:",
    data
  );


  return data;

}


// =====================================================
// UPDATE BOOK
// Backend:
// PUT /api/book/{id}
// =====================================================

export async function updateBook(
  id,
  bookRequest,
  imageFile = null
) {

  const token = sessionStorage.getItem("token");

  const formData = new FormData();


  formData.append(
    "categoryId",
    bookRequest.categoryId
  );

  formData.append(
    "title",
    bookRequest.title
  );

  formData.append(
    "qty",
    bookRequest.qty
  );

  formData.append(
    "description",
    bookRequest.description || ""
  );

  formData.append(
    "author",
    bookRequest.author
  );

  formData.append(
    "pages",
    bookRequest.pages || ""
  );

  formData.append(
    "isbn",
    bookRequest.isbn || ""
  );

  formData.append(
    "language",
    bookRequest.language || ""
  );

  formData.append(
    "status",
    bookRequest.status || "Available"
  );


  if (imageFile) {

    formData.append(
      "file",
      imageFile
    );

  }


  console.log(
    "UPDATE BOOK REQUEST:",
    bookRequest
  );


  const response = await fetch(
    `${API_URL}/${id}`,
    {
      method: "PUT",

      headers: {
        "Authorization": `Bearer ${token}`
      },

      body: formData
    }
  );


  const text = await response.text();

  let data;

  try {

    data = text
      ? JSON.parse(text)
      : null;

  } catch (error) {

    console.error(
      "UPDATE BOOK INVALID RESPONSE:",
      text
    );

    throw new Error(
      text ||
      "Server returned an invalid response"
    );

  }


  if (!response.ok) {

    throw new Error(
      data?.msg ||
      data?.message ||
      `Failed to update book (${response.status})`
    );

  }


  console.log(
    "UPDATE BOOK RESPONSE:",
    data
  );


  return data;

}


// =====================================================
// DELETE BOOK
// Backend:
// DELETE /api/book/{id}
// =====================================================

export async function deleteBookById(id) {

  const token = sessionStorage.getItem("token");


  console.log(
    "DELETE BOOK ID:",
    id
  );


  const response = await fetch(
    `${API_URL}/${id}`,
    {
      method: "DELETE",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );


  const text = await response.text();

  let data = null;


  try {

    data = text
      ? JSON.parse(text)
      : null;

  } catch (error) {

    console.error(
      "DELETE BOOK INVALID RESPONSE:",
      text
    );

    throw new Error(
      text ||
      "Server returned an invalid response"
    );

  }


  if (!response.ok) {

    throw new Error(
      data?.msg ||
      data?.message ||
      `Failed to delete book (${response.status})`
    );

  }


  console.log(
    "DELETE BOOK RESPONSE:",
    data
  );


  return data;

}