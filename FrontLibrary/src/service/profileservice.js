const API_URL = "http://localhost:8080/api/profile";

const getToken = () => {
  return sessionStorage.getItem("token");
};

/**
 * Get current user profile
 */
export const getProfile = async () => {
  const token = getToken();

  const response = await fetch(`${API_URL}/getProfile`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  let result = null;

  try {
    result = await response.json();
  } catch {
    result = null;
  }

  if (!response.ok) {
    throw new Error(
      result?.msg ||
        result?.message ||
        "Failed to get profile"
    );
  }

  return result;
};

/**
 * Create profile
 */
export const createProfile = async (
  profileData,
  imageFile = null
) => {
  const token = getToken();

  const formData = new FormData();

  formData.append("phone", profileData.phone || "");
  formData.append("gender", profileData.gender || "");
  formData.append(
    "dateOfBirth",
    profileData.dateOfBirth || ""
  );
  formData.append(
    "address",
    profileData.address || ""
  );

  if (imageFile) {
    formData.append("image", imageFile);
  }

  const response = await fetch(
    `${API_URL}/createProfile`,
    {
      method: "POST",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    }
  );

  let result = null;

  try {
    result = await response.json();
  } catch {
    result = null;
  }

  if (!response.ok) {
    throw new Error(
      result?.msg ||
        result?.message ||
        "Failed to create profile"
    );
  }

  return result;
};

/**
 * Update profile
 */
export const updateProfile = async (
  profileData,
  imageFile = null
) => {
  const token = getToken();

  const formData = new FormData();

  formData.append("phone", profileData.phone || "");
  formData.append("gender", profileData.gender || "");
  formData.append(
    "dateOfBirth",
    profileData.dateOfBirth || ""
  );
  formData.append(
    "address",
    profileData.address || ""
  );

  if (imageFile) {
    formData.append("image", imageFile);
  }

  const response = await fetch(
    `${API_URL}/updateProfile`,
    {
      method: "PUT",
      headers: {
        Authorization: `Bearer ${token}`,
      },
      body: formData,
    }
  );

  let result = null;

  try {
    result = await response.json();
  } catch {
    result = null;
  }

  if (!response.ok) {
    throw new Error(
      result?.msg ||
        result?.message ||
        "Failed to update profile"
    );
  }

  return result;
};