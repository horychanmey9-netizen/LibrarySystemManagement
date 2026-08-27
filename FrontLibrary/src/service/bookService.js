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

  let result = null;

  try {
    result = await response.json();
  } catch (e) {
    result = null;
  }

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch books (${response.status})`
    );

  }

  console.log("BOOK API RESPONSE:", result);

  // Backend:
  // {
  //   msg: "...",
  //   status: 200,
  //   data: [...]
  // }

  if (Array.isArray(result)) {
    return result;
  }

  if (Array.isArray(result?.data)) {
    return result.data;
  }

  return [];

}


// =====================================================
// GET BOOK BY ID
// =====================================================

export async function getBookById(id) {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/${id}`,
    {
      method: "GET",

      headers: {
        "Authorization": `Bearer ${token}`,
        "Content-Type": "application/json"
      }
    }
  );

  let result = null;

  try {
    result = await response.json();
  } catch (e) {
    result = null;
  }

  if (!response.ok) {

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to fetch book (${response.status})`
    );

  }

  console.log("BOOK BY ID RESPONSE:", result);

  return result?.data ?? result;

}


// =====================================================
// CREATE BOOK
// =====================================================

export async function createBook(book) {

  const token = sessionStorage.getItem("token");

  const formData = new FormData();


  // ===================================================
  // BASIC DATA
  // ===================================================

  formData.append(
    "title",
    book.title ?? ""
  );

  formData.append(
    "description",
    book.description ?? ""
  );

  formData.append(
    "author",
    book.author ?? ""
  );

  formData.append(
    "isbn",
    book.isbn ?? ""
  );


  // ===================================================
  // CATEGORY
  // ===================================================

  if (
    book.categoryId !== null &&
    book.categoryId !== undefined &&
    book.categoryId !== ""
  ) {

    formData.append(
      "categoryId",
      String(book.categoryId)
    );

  }


  // ===================================================
  // QUANTITY
  // Backend expects qty
  // ===================================================

  formData.append(
    "qty",
    String(
      book.qty ??
      book.quantity ??
      0
    )
  );


  // ===================================================
  // PAGES
  // ===================================================

  if (
    book.pages !== null &&
    book.pages !== undefined &&
    book.pages !== ""
  ) {

    formData.append(
      "pages",
      String(book.pages)
    );

  }


  // ===================================================
  // LANGUAGE
  // ===================================================

  if (book.language) {

    formData.append(
      "language",
      book.language
    );

  }


  // ===================================================
  // STATUS
  // ===================================================

  if (book.status) {

    formData.append(
      "status",
      book.status
    );

  }


  // ===================================================
  // IMAGE
  // ===================================================

  if (
    book.file &&
    book.file instanceof File
  ) {

    formData.append(
      "file",
      book.file
    );

  }


  // ===================================================
  // DEBUG
  // ===================================================

  console.log(
    "CREATE BOOK FORM DATA:"
  );

  for (
    const [key, value]
    of formData.entries()
  ) {

    console.log(
      key,
      value
    );

  }


  // ===================================================
  // REQUEST
  // ===================================================

  const response = await fetch(
    API_URL,
    {
      method: "POST",

      headers: {
        "Authorization":
          `Bearer ${token}`
      },

      body: formData
    }
  );


  let result = null;

  try {

    result =
      await response.json();

  } catch (e) {

    result = null;

  }


  if (!response.ok) {

    console.error(
      "CREATE BOOK BACKEND ERROR:",
      result
    );

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to create book (${response.status})`
    );

  }


  console.log(
    "CREATE BOOK SUCCESS:",
    result
  );


  return result;

}


// =====================================================
// UPDATE BOOK
// =====================================================

export async function updateBook(id, book) {

  const token =
    sessionStorage.getItem("token");

  const formData =
    new FormData();


  // ===================================================
  // BASIC DATA
  // ===================================================

  formData.append(
    "title",
    book.title ?? ""
  );

  formData.append(
    "description",
    book.description ?? ""
  );

  formData.append(
    "author",
    book.author ?? ""
  );

  formData.append(
    "isbn",
    book.isbn ?? ""
  );


  // ===================================================
  // CATEGORY
  // Backend expects categoryId
  // ===================================================

  if (
    book.categoryId !== null &&
    book.categoryId !== undefined &&
    book.categoryId !== ""
  ) {

    formData.append(
      "categoryId",
      String(book.categoryId)
    );

  }


  // ===================================================
  // QUANTITY
  // Backend expects qty
  // ===================================================

  formData.append(
    "qty",
    String(
      book.qty ??
      book.quantity ??
      0
    )
  );


  // ===================================================
  // PAGES
  // ===================================================

  if (
    book.pages !== null &&
    book.pages !== undefined &&
    book.pages !== ""
  ) {

    formData.append(
      "pages",
      String(book.pages)
    );

  }


  // ===================================================
  // LANGUAGE
  // ===================================================

  if (book.language) {

    formData.append(
      "language",
      book.language
    );

  }


  // ===================================================
  // STATUS
  // ===================================================

  if (book.status) {

    formData.append(
      "status",
      book.status
    );

  }


  // ===================================================
  // NEW IMAGE
  //
  // IMPORTANT:
  // Only append file if user selected NEW image.
  //
  // If file is missing:
  // Backend will keep old image.
  // ===================================================

  if (
    book.file &&
    book.file instanceof File
  ) {

    formData.append(
      "file",
      book.file
    );

  }


  // ===================================================
  // DEBUG
  // ===================================================

  console.log(
    "================================"
  );

  console.log(
    "UPDATE BOOK ID:",
    id
  );

  console.log(
    "UPDATE BOOK FORM DATA:"
  );

  for (
    const [key, value]
    of formData.entries()
  ) {

    console.log(
      key,
      value
    );

  }

  console.log(
    "================================"
  );


  // ===================================================
  // REQUEST
  // DO NOT SET Content-Type
  // ===================================================

  const response =
    await fetch(
      `${API_URL}/${id}`,
      {
        method: "PUT",

        headers: {
          "Authorization":
            `Bearer ${token}`
        },

        body: formData
      }
    );


  // ===================================================
  // RESPONSE
  // ===================================================

  let result = null;

  try {

    result =
      await response.json();

  } catch (e) {

    result = null;

  }


  // ===================================================
  // ERROR
  // ===================================================

  if (!response.ok) {

    console.error(
      "UPDATE BOOK BACKEND ERROR:",
      result
    );

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to update book (${response.status})`
    );

  }


  // ===================================================
  // SUCCESS
  // ===================================================

  console.log(
    "UPDATE BOOK SUCCESS:",
    result
  );


  return result;

}


// =====================================================
// DELETE BOOK
// =====================================================

export async function deleteBookById(id) {

  const token =
    sessionStorage.getItem("token");


  const response =
    await fetch(
      `${API_URL}/${id}`,
      {
        method: "DELETE",

        headers: {
          "Authorization":
            `Bearer ${token}`,
          "Content-Type":
            "application/json"
        }
      }
    );


  let result = null;

  try {

    result =
      await response.json();

  } catch (e) {

    result = null;

  }


  if (!response.ok) {

    console.error(
      "DELETE BOOK ERROR:",
      result
    );

    throw new Error(
      result?.message ||
      result?.msg ||
      `Failed to delete book (${response.status})`
    );

  }


  console.log(
    "DELETE BOOK SUCCESS:",
    result
  );


  return result;

}


// =====================================================
// ALIAS
// If some file imports deleteBook()
// =====================================================

export async function deleteBook(id) {

  return deleteBookById(id);

}