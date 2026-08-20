const API_URL = "http://localhost:8080/api/auth";

// =========================
// HELPER: READ RESPONSE
// =========================
async function readResponse(response) {
  const contentType = response.headers.get("content-type") || "";

  if (contentType.includes("application/json")) {
    return await response.json();
  }

  return await response.text();
}

// =========================
// REGISTER
// =========================
export async function Register(name, email, password) {
  const response = await fetch(`${API_URL}/register`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify({
      name,
      email,
      password,
    }),
  });

  const data = await readResponse(response);

  if (!response.ok) {
    const message =
      typeof data === "string"
        ? data
        : data?.message || "Register failed";

    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return data;
}

// =========================
// LOGIN
// =========================
export async function Login(email, password) {
  const response = await fetch(`${API_URL}/login`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify({
      email,
      password,
    }),
  });

  const data = await readResponse(response);

  if (!response.ok) {
    const message =
      typeof data === "string"
        ? data
        : data?.message || "Login failed";

    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return data;
}

// =========================
// VERIFY OTP
// =========================
export async function VerifyOTP(email, otp) {
  const response = await fetch(`${API_URL}/verify-otp`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify({
      email,
      otp,
    }),
  });

  const data = await readResponse(response);

  if (!response.ok) {
    const message =
      typeof data === "string"
        ? data
        : data?.message || "Invalid or expired OTP";

    const error = new Error(message);
    error.status = response.status;
    throw error;
  }

  return data;
}