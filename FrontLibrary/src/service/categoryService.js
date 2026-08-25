const API_URL = "http://localhost:8080/api/category";

// =====================================================
// GET TOKEN
// =====================================================

const getToken = () => {
  return sessionStorage.getItem("token");
};

// =====================================================
// GET AUTH HEADERS
// =====================================================

const getHeaders = () => {
  const token = getToken();

  return {
    "Content-Type": "application/json",
    Authorization: `Bearer ${token}`,
  };
};

// =====================================================
// GET ALL CATEGORIES
// =====================================================

export const getCategories = async () => {
  try {
    const response = await fetch(API_URL, {
      method: "GET",
      headers: getHeaders(),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(
        result?.msg ||
        result?.message ||
        "Failed to fetch categories"
      );
    }

    return result;
  } catch (error) {
    console.error("Get categories error:", error);
    throw error;
  }
};

// =====================================================
// GET CATEGORY BY ID
// =====================================================

export const getCategoryById = async (id) => {
  try {
    const response = await fetch(
      `${API_URL}/${id}`,
      {
        method: "GET",
        headers: getHeaders(),
      }
    );

    const result = await response.json();

    if (!response.ok) {
      throw new Error(
        result?.msg ||
        result?.message ||
        "Failed to fetch category"
      );
    }

    return result;
  } catch (error) {
    console.error("Get category by ID error:", error);
    throw error;
  }
};

// =====================================================
// CREATE CATEGORY
// =====================================================

export const createCategory = async (categoryData) => {
  try {
    const response = await fetch(API_URL, {
      method: "POST",
      headers: getHeaders(),
      body: JSON.stringify({
        name: categoryData.name,
      }),
    });

    const result = await response.json();

    if (!response.ok) {
      throw new Error(
        result?.msg ||
        result?.message ||
        "Failed to create category"
      );
    }

    return result;
  } catch (error) {
    console.error("Create category error:", error);
    throw error;
  }
};

// =====================================================
// UPDATE CATEGORY
// =====================================================

export const updateCategory = async (id, categoryData) => {
  try {
    const response = await fetch(
      `${API_URL}/${id}`,
      {
        method: "PUT",
        headers: getHeaders(),
        body: JSON.stringify({
          name: categoryData.name,
        }),
      }
    );

    const result = await response.json();

    if (!response.ok) {
      throw new Error(
        result?.msg ||
        result?.message ||
        "Failed to update category"
      );
    }

    return result;
  } catch (error) {
    console.error("Update category error:", error);
    throw error;
  }
};

// =====================================================
// DELETE CATEGORY
// =====================================================

export const deleteCategory = async (id) => {
  try {
    const response = await fetch(
      `${API_URL}/${id}`,
      {
        method: "DELETE",
        headers: getHeaders(),
      }
    );

    const result = await response.json();

    if (!response.ok) {
      throw new Error(
        result?.msg ||
        result?.message ||
        "Failed to delete category"
      );
    }

    return result;
  } catch (error) {
    console.error("Delete category error:", error);
    throw error;
  }
};