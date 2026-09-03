const API_URL = "http://localhost:8080/api/book";


// =====================================================
// GET ALL BOOKS
// =====================================================

export async function getBooks() {

  const token = sessionStorage.getItem("token");

  const response = await fetch(
    API_URL,
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

    const contentType =
      response.headers.get("content-type");

    if (
      contentType?.includes("application/json")
    ) {

      result = await response.json();

    } else {

      result = await response.text();

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


  // Backend:
  //
  // {
  //   msg: "...",
  //   status: 200,
  //   data: [...]
  // }


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
// IMPORTANT:
// AddBook.vue calls:
//
// createBook(bookRequest, imageFile.value)
//
// So this function MUST accept imageFile
// as the second parameter.
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
  // Backend expects:
  //
  // private Long categoryId;
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
  // Backend expects:
  //
  // private Integer qty;
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
  //
  // IMPORTANT:
  // Backend expects:
  //
  // @RequestParam(
  //   value = "file",
  //   required = false
  // )
  // MultipartFile file
  //
  // Therefore FormData key MUST be "file".
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
  // POST REQUEST
  // ===================================================

  const response =
    await fetch(
      API_URL,
      {
        method: "POST",

        headers: {

          // DO NOT SET Content-Type HERE.
          //
          // Browser automatically creates:
          // multipart/form-data; boundary=...
          //
          "Authorization":
            `Bearer ${token}`

        },

        body:
          formData
      }
    );


  // ===================================================
  // READ RESPONSE
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

export async function updateBook(
  id,
  book
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
  // Only append if a NEW image is selected.
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
    "=========================================="
  );


  // ===================================================
  // PUT REQUEST
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
      "UPDATE BOOK STATUS:",
      response.status
    );


    console.error(
      "UPDATE BOOK BACKEND ERROR:",
      result
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
    "UPDATE BOOK SUCCESS:",
    result
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
// If another file imports:
//
// deleteBook()
//
// it will still work.
// =====================================================

export async function deleteBook(
  id
) {

  return deleteBookById(
    id
  );

}