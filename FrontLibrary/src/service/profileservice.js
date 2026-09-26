const API_URL = "https://librarymanagementsystem-ukyq.onrender.com/api/profile";

const getToken = () => {
  const token = sessionStorage.getItem("token");
  if (!token) {
    throw new Error("Authentication token not found. Please log in again.");
  }
  return token;
};

/**
 * Safely parse response body (handles JSON, plain text, and empty responses)
 */
const parseResponseBody = async (response) => {
  const text = await response.text();
  if (!text) return null;

  try {
    return JSON.parse(text);
  } catch {
    return text;
  }
};

/**
 * Extract meaningful error message from backend response
 */
const extractErrorMessage = (result, response, fallbackMsg) => {
  if (typeof result === "string" && result.trim()) {
    if (result.startsWith("<") || result.includes("<!DOCTYPE")) {
      return response.statusText
        ? `${fallbackMsg}: ${response.status} ${response.statusText}`
        : `${fallbackMsg} (Status: ${response.status})`;
    }
    return result;
  }

  if (result && typeof result === "object") {
    return result.msg || result.message || result.error || fallbackMsg;
  }

  return response.statusText
    ? `${fallbackMsg}: ${response.status} ${response.statusText}`
    : `${fallbackMsg} (Status: ${response.status})`;
};

/**
 * Build FormData safely for profile creation / updates
 */
const buildProfileFormData = (profileData, imageFile) => {
  const formData = new FormData();

  const name = profileData.fullName || profileData.name;
  if (name && name.trim()) {
    formData.append("name", name.trim());
  }

  if (profileData.phone) {
    formData.append("phone", profileData.phone.trim());
  }

  if (profileData.gender && profileData.gender.trim()) {
    formData.append("gender", profileData.gender.trim());
  }

  if (profileData.dateOfBirth && profileData.dateOfBirth.trim()) {
    formData.append("dateOfBirth", profileData.dateOfBirth.trim());
  }

  if (profileData.address) {
    formData.append("address", profileData.address.trim());
  }

  if (imageFile) {
    formData.append("image", imageFile);
  }

  return formData;
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

  const result = await parseResponseBody(response);

  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to get profile")
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
  const formData = buildProfileFormData(profileData, imageFile);

  const response = await fetch(`${API_URL}/createProfile`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: formData,
  });

  const result = await parseResponseBody(response);

  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to create profile")
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
  const formData = buildProfileFormData(profileData, imageFile);

  const response = await fetch(`${API_URL}/updateProfile`, {
    method: "PUT",
    headers: {
      Authorization: `Bearer ${token}`,
    },
    body: formData,
  });

  const result = await parseResponseBody(response);

  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to update profile")
    );
  }

  return result;
};

export default {
  getProfile,
  createProfile,
  updateProfile,
};