const API_URL = "http://localhost:8080/api/telegram";

/**
 * Retrieve auth token from sessionStorage
 */
const getToken = (required = true) => {
  const token = sessionStorage.getItem("token");
  if (!token && required) {
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
    // Check if the response is an HTML error page (e.g. from server/gateway)
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
 * Network-safe fetch wrapper
 */
const safeFetch = async (url, options) => {
  try {
    return await fetch(url, options);
  } catch (error) {
    throw new Error(
      "Unable to connect to the server. Please check your network or ensure the backend is running."
    );
  }
};

/**
 * Get Telegram connection status
 */
export const getTelegramStatus = async () => {
  const token = getToken(false);

  // If user is not logged in, return default disconnected status gracefully
  if (!token) {
    return {
      connected: false,
      chatId: null,
      username: null,
      connectedAt: null,
    };
  }

  const response = await safeFetch(`${API_URL}/status`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const result = await parseResponseBody(response);
  console.log("GET TELEGRAM STATUS:", result);

  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to get Telegram status")
    );
  }

  return result;
};

/**
 * Generate Telegram connection code
 */
export const connectTelegram = async () => {
  const token = getToken(true);

  const response = await safeFetch(`${API_URL}/connect`, {
    method: "POST",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const result = await parseResponseBody(response);
  console.log("CONNECT TELEGRAM RESPONSE:", result);

  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to connect Telegram")
    );
  }

  return result;
};

/**
 * Disconnect Telegram
 */
export const disconnectTelegram = async () => {
  const token = getToken(true);

  const response = await safeFetch(`${API_URL}/disconnect`, {
    method: "DELETE",
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  const result = await parseResponseBody(response);
  console.log("DISCONNECT TELEGRAM RESPONSE:", result);

  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to disconnect Telegram")
    );
  }

  return result;
};
// Send a message to the user's connected Telegram account
export const sendMessage = async (message) => {
  const token = getToken(true);
  const response = await safeFetch(`${API_URL}/sendMessage`, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Authorization: `Bearer ${token}`,
    },
    body: JSON.stringify({ message })
  });
  const result = await parseResponseBody(response);
  console.log("SEND TELEGRAM MESSAGE RESPONSE:", result);
  if (!response.ok) {
    throw new Error(
      extractErrorMessage(result, response, "Failed to send Telegram message")
    );
  }
  return result;
};

export default {
  getTelegramStatus,
  connectTelegram,
  disconnectTelegram,
  sendMessage,
};