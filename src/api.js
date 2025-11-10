import axios from "axios";

const API_BASE_URL =
  process.env.REACT_APP_BACKEND_URL ||
  (window.location.hostname === "localhost"
    ? "http://localhost:8080/api/analyze"
    : "http://cme_backend:8080/api/analyze");

export const analyzeUrl = async (targetUrl, keywords = "") => {
  try {
    const response = await axios.get(API_BASE_URL, {
      params: {
        url: targetUrl,
        keyword: keywords,
      },
    });
    return response.data;
  } catch (error) {
    console.error("Error connecting to backend:", error);
    throw new Error("Failed to connect to backend service");
  }
};
