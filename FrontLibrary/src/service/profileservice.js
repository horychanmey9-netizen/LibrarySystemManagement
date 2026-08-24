const API_URL = "http://localhost:8080/api/profile";

// ========================================
// GET PROFILE
// ========================================
export const getProfile = async () => {
  const token = sessionStorage.getItem("token");

  const response = await fetch(
    `${API_URL}/getProfile`,
    {
      method: "GET",
      headers: {
        Authorization: `Bearer ${token}`,
      },
    }
  );

  const result = await response.json();

  console.log("GET PROFILE:", result);

  if (!response.ok) {
    throw new Error(
      result.msg || "Failed to get profile"
    );
  }

  return result;
};


// ========================================
// CREATE PROFILE
// ========================================
export const createProfile = async (
  profileData,
  imageFile = null
) => {
  const token = sessionStorage.getItem("token");

  const formData = new FormData();

  formData.append(
    "phone",
    profileData.phone || ""
  );

  formData.append(
    "gender",
    profileData.gender || ""
  );

  formData.append(
    "dateOfBirth",
    profileData.dateOfBirth || ""
  );

  formData.append(
    "address",
    profileData.address || ""
  );

  if (imageFile) {
    formData.append(
      "image",
      imageFile
    );
  }

  console.log(
    "CREATE PROFILE URL:",
    `${API_URL}/createProfile`
  );

  console.log(
    "TOKEN:",
    token
  );

  console.log(
    "FORM DATA:"
  );

  for (const [key, value] of formData.entries()) {
    console.log(
      key,
      value
    );
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

  console.log(
    "CREATE PROFILE STATUS:",
    response.status
  );

  const result = await response.json();

  console.log(
    "CREATE PROFILE RESPONSE:",
    result
  );

  if (!response.ok) {
    throw new Error(
      result.msg ||
      "Failed to create profile"
    );
  }

  return result;
};


// ========================================
// UPDATE PROFILE
// ========================================
export const updateProfile = async (
  profileData,
  imageFile = null
) => {
  const token = sessionStorage.getItem("token");

  const formData = new FormData();

  formData.append(
    "phone",
    profileData.phone || ""
  );

  formData.append(
    "gender",
    profileData.gender || ""
  );

  formData.append(
    "dateOfBirth",
    profileData.dateOfBirth || ""
  );

  formData.append(
    "address",
    profileData.address || ""
  );

  if (imageFile) {
    formData.append(
      "image",
      imageFile
    );
  }

  console.log(
    "UPDATE PROFILE URL:",
    `${API_URL}/updateProfile`
  );

  console.log(
    "TOKEN:",
    token
  );

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

  console.log(
    "UPDATE PROFILE STATUS:",
    response.status
  );

  const result = await response.json();

  console.log(
    "UPDATE PROFILE RESPONSE:",
    result
  );

  if (!response.ok) {
    throw new Error(
      result.msg ||
      "Failed to update profile"
    );
  }

  return result;
};