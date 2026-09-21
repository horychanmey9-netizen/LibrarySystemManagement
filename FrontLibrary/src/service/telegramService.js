const API_URL = "http://localhost:8080/api/telegram";

const getToken = () => {
    return sessionStorage.getItem("token");
};


export const getTelegramStatus = async () => {
    const token = getToken();

    const response = await fetch(`${API_URL}/status`, {
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

    console.log("GET TELEGRAM STATUS:", result);

    if (!response.ok) {
        throw new Error(
        result?.msg ||
            result?.message ||
            "Failed to get Telegram status"
        );
    }

    return result;
    };

    /**
     * Generate Telegram connection code
     */
    export const connectTelegram = async () => {
    const token = getToken();

    const response = await fetch(`${API_URL}/connect`, {
        method: "POST",
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

    console.log("CONNECT TELEGRAM RESPONSE:", result);

    if (!response.ok) {
        throw new Error(
        result?.msg ||
            result?.message ||
            "Failed to connect Telegram"
        );
    }

    return result;
    };

    /**
     * Disconnect Telegram
     */
    export const disconnectTelegram = async () => {
    const token = getToken();

    const response = await fetch(`${API_URL}/disconnect`, {
        method: "DELETE",
        headers: {
        Authorization: `Bearer ${token}`,
        },
    });

    let result = null;

    try {
        result = await response.text();
    } catch {
        result = null;
    }

    console.log("DISCONNECT TELEGRAM RESPONSE:", result);

    if (!response.ok) {
        throw new Error(
        result || "Failed to disconnect Telegram"
        );
    }

    return result;
};