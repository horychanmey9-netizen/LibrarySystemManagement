const API_URL = "https://librarymanagementsystem-ukyq.onrender.com/api/book";


// =====================================================
// GET ALL BOOKS
// =====================================================

export async function getBooks() {

  const token =
    sessionStorage.getItem("token");


  const response =
    await fetch(
      API_URL,
      {
        method: "GET",

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

    const contentType =
      response.headers.get(
        "content-type"
      );


    if (
      contentType?.includes(
        "application/json"
      )
    ) {

      result =
        await response.json();

    } else {

      result =
        await response.text();

    }

  } catch (e) {

    result = null;

  }


  if (!response.ok) {

    throw new Error(

      typeof result === "string"

        ? result

        : result?.message ||
          result?.msg ||
          `Failed to fetch books (${response.status})`

    );

  }


  console.log(
    "BOOK API RESPONSE:",
    result
  );


  // ===================================================
  // BACKEND RESPONSE
  //
  // {
  //   msg: "...",
  //   status: 200,
  //   data: [...]
  // }
  // ===================================================

  if (Array.isArray(result)) {

    return result;

  }


  if (
    result &&
    Array.isArray(result.data)
  ) {

    return result.data;

  }


  return [];

}


// =====================================================
// REJECT RETURN
// =====================================================

export async function rejectReturn(id) {

  const token =
    sessionStorage.getItem("token");


  const response =
    await fetch(
      `${API_URL}/reject-return/${id}`,
      {
        method: "PUT",

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

    const contentType =
      response.headers.get(
        "content-type"
      );


    if (
      contentType?.includes(
        "application/json"
      )
    ) {

      result =
        await response.json();

    } else {

      result =
        await response.text();

    }

  } catch (e) {

    result = null;

  }


  if (!response.ok) {

    throw new Error(

      typeof result === "string"

        ? result

        : result?.message ||
          result?.msg ||
          "Failed to reject return."

    );

  }


  return result;

}


// =====================================================
// GET BOOK BY ID
// =====================================================

export async function getBookById(id) {

  const token =
    sessionStorage.getItem("token");


  const response =
    await fetch(
      `${API_URL}/${id}`,
      {
        method: "GET",

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

    const contentType =
      response.headers.get(
        "content-type"
      );


    if (
      contentType?.includes(
        "application/json"
      )
    ) {

      result =
        await response.json();

    } else {

      result =
        await response.text();

    }

  } catch (e) {

    result = null;

  }


  if (!response.ok) {

    throw new Error(

      typeof result === "string"

        ? result

        : result?.message ||
          result?.msg ||
          `Failed to fetch book (${response.status})`

    );

  }


  console.log(
    "BOOK BY ID RESPONSE:",
    result
  );


  return (
    result?.data ??
    result
  );

}


// =====================================================
// CREATE BOOK
// =====================================================

export async function createBook(
  book,
  imageFile
) {

  const token =
    sessionStorage.getItem("token");


  const formData =
    new FormData();


  // ===================================================
  // TITLE
  // ===================================================

  formData.append(
    "title",
    book.title ?? ""
  );


  // ===================================================
  // DESCRIPTION
  // ===================================================

  formData.append(
    "description",
    book.description ?? ""
  );


  // ===================================================
  // AUTHOR
  // ===================================================

  formData.append(
    "author",
    book.author ?? ""
  );


  // ===================================================
  // ISBN
  // ===================================================

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
  // PUBLISH YEAR
  //
  // Date picker:
  //
  // "2026-09-21"
  //
  // Backend:
  //
  // String publishYear
  //
  // IMPORTANT:
  // Keep the complete date.
  // ===================================================

  if (
    book.publishYear !== null &&
    book.publishYear !== undefined &&
    book.publishYear !== ""
  ) {

    formData.append(
      "publishYear",
      String(book.publishYear)
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
    imageFile &&
    imageFile instanceof File
  ) {

    formData.append(
      "file",
      imageFile
    );

  }


  // ===================================================
  // DEBUG
  // ===================================================

  console.log(
    "=========================================="
  );

  console.log(
    "CREATE BOOK FORM DATA:"
  );


  for (
    const [key, value]
    of formData.entries()
  ) {

    if (
      value instanceof File
    ) {

      console.log(
        key,
        {
          name: value.name,
          type: value.type,
          size: value.size
        }
      );

    } else {

      console.log(
        key,
        value
      );

    }

  }


  console.log(
    "IMAGE FILE:",
    imageFile
  );


  console.log(
    "TOKEN EXISTS:",
    !!token
  );


  console.log(
    "=========================================="
  );


  // ===================================================
  // POST
  // ===================================================

  const response =
    await fetch(
      API_URL,
      {
        method: "POST",

        headers: {

          // DO NOT SET Content-Type
          // Browser creates multipart boundary

          "Authorization":
            `Bearer ${token}`

        },

        body:
          formData
      }
    );


  // ===================================================
  // RESPONSE
  // ===================================================

  let result = null;


  const contentType =
    response.headers.get(
      "content-type"
    );


  try {

    if (
      contentType?.includes(
        "application/json"
      )
    ) {

      result =
        await response.json();

    } else {

      result =
        await response.text();

    }

  } catch (e) {

    result = null;

  }


  // ===================================================
  // ERROR
  // ===================================================

  if (!response.ok) {

    console.error(
      "=========================================="
    );

    console.error(
      "CREATE BOOK STATUS:",
      response.status
    );

    console.error(
      "CREATE BOOK BACKEND ERROR:",
      result
    );

    console.error(
      "CREATE BOOK BACKEND ERROR JSON:",
      JSON.stringify(
        result,
        null,
        2
      )
    );

    console.error(
      "=========================================="
    );


    throw new Error(

      typeof result === "string"

        ? result

        : result?.message ||
          result?.msg ||
          `Failed to create book (${response.status})`

    );

  }


  // ===================================================
  // SUCCESS
  // ===================================================

  console.log(
    "=========================================="
  );

  console.log(
    "CREATE BOOK SUCCESS:",
    result
  );

  console.log(
    "=========================================="
  );


  return result;

}


// =====================================================
// UPDATE BOOK
// =====================================================
//
// IMPORTANT:
// This version accepts:
//
// updateBook(id, book, imageFile)
//
// so EditBook.vue can upload a new image.
// =====================================================

export async function updateBook(
  id,
  book,
  imageFile
) {

  const token =
    sessionStorage.getItem("token");


  const formData =
    new FormData();


  // ===================================================
  // TITLE
  // ===================================================

  formData.append(
    "title",
    book.title ?? ""
  );


  // ===================================================
  // DESCRIPTION
  // ===================================================

  formData.append(
    "description",
    book.description ?? ""
  );


  // ===================================================
  // AUTHOR
  // ===================================================

  formData.append(
    "author",
    book.author ?? ""
  );


  // ===================================================
  // ISBN
  // ===================================================

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
  // PUBLISH YEAR
  //
  // Example:
  //
  // "2026-09-21"
  //
  // Keep as String.
  // ===================================================

  if (
    book.publishYear !== null &&
    book.publishYear !== undefined &&
    book.publishYear !== ""
  ) {

    formData.append(
      "publishYear",
      String(book.publishYear)
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
  //
  // Only append when a NEW image is selected.
  // ===================================================

  if (
    imageFile &&
    imageFile instanceof File
  ) {

    formData.append(
      "file",
      imageFile
    );

  }


  // ===================================================
  // DEBUG
  // ===================================================

  console.log(
    "=========================================="
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

    if (
      value instanceof File
    ) {

      console.log(
        key,
        {
          name: value.name,
          type: value.type,
          size: value.size
        }
      );

    } else {

      console.log(
        key,
        value
      );

    }

  }


  console.log(
    "NEW IMAGE FILE:",
    imageFile
  );


  console.log(
    "TOKEN EXISTS:",
    !!token
  );


  console.log(
    "=========================================="
  );


  // ===================================================
  // PUT
  // ===================================================

  const response =
    await fetch(
      `${API_URL}/${id}`,
      {
        method: "PUT",

        headers: {

          // DO NOT SET Content-Type
          // Browser creates multipart boundary

          "Authorization":
            `Bearer ${token}`

        },

        body:
          formData
      }
    );


  // ===================================================
  // RESPONSE
  // ===================================================

  let result = null;


  const contentType =
    response.headers.get(
      "content-type"
    );


  try {

    if (
      contentType?.includes(
        "application/json"
      )
    ) {

      result =
        await response.json();

    } else {

      result =
        await response.text();

    }

  } catch (e) {

    result = null;

  }


  // ===================================================
  // ERROR
  // ===================================================

  if (!response.ok) {

    console.error(
      "=========================================="
    );

    console.error(
      "UPDATE BOOK STATUS:",
      response.status
    );

    console.error(
      "UPDATE BOOK BACKEND ERROR:",
      result
    );

    console.error(
      "UPDATE BOOK BACKEND ERROR JSON:",
      JSON.stringify(
        result,
        null,
        2
      )
    );

    console.error(
      "=========================================="
    );


    throw new Error(

      typeof result === "string"

        ? result

        : result?.message ||
          result?.msg ||
          `Failed to update book (${response.status})`

    );

  }


  // ===================================================
  // SUCCESS
  // ===================================================

  console.log(
    "=========================================="
  );

  console.log(
    "UPDATE BOOK SUCCESS:",
    result
  );

  console.log(
    "=========================================="
  );


  return result;

}


// =====================================================
// DELETE BOOK
// =====================================================

export async function deleteBookById(
  id
) {

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


  // ===================================================
  // RESPONSE
  // ===================================================

  let result = null;


  const contentType =
    response.headers.get(
      "content-type"
    );


  try {

    if (
      contentType?.includes(
        "application/json"
      )
    ) {

      result =
        await response.json();

    } else {

      result =
        await response.text();

    }

  } catch (e) {

    result = null;

  }


  // ===================================================
  // ERROR
  // ===================================================

  if (!response.ok) {

    console.error(
      "DELETE BOOK STATUS:",
      response.status
    );

    console.error(
      "DELETE BOOK ERROR:",
      result
    );

    console.error(
      "DELETE BOOK ERROR JSON:",
      JSON.stringify(
        result,
        null,
        2
      )
    );


    throw new Error(

      typeof result === "string"

        ? result

        : result?.message ||
          result?.msg ||
          `Failed to delete book (${response.status})`

    );

  }


  // ===================================================
  // SUCCESS
  // ===================================================

  console.log(
    "DELETE BOOK SUCCESS:",
    result
  );


  return result;

}


// =====================================================
// ALIAS
// =====================================================

export async function deleteBook(
  id
) {

  return deleteBookById(
    id
  );

}