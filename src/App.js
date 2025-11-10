import React, { useState, useEffect } from "react";
import axios from "axios";
import { Bar } from "react-chartjs-2";
import {
  Chart as ChartJS,
  CategoryScale,
  LinearScale,
  BarElement,
  Title,
  Tooltip,
  Legend,
} from "chart.js";
import "./App.css";

ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

function App() {
  const [url, setUrl] = useState("");
  const [keyword, setKeyword] = useState("");
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [darkMode, setDarkMode] = useState(false);

  useEffect(() => {
    const savedMode = localStorage.getItem("darkMode");
    if (savedMode) setDarkMode(JSON.parse(savedMode));
  }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError("");
    setResult(null);

    try {
      const res = await axios.get("http://localhost:8080/api/analyze", {
        params: { url, keyword },
      });
      setResult(res.data);
    } catch (err) {
      console.error(err);
      setError("⚠️ Error connecting to backend or invalid URL");
    } finally {
      setLoading(false);
    }
  };

  const highlightKeyword = (text, keywordString) => {
    if (!keywordString || !text) return text;
    const keywords = keywordString
      .split(",")
      .map((k) => k.trim())
      .filter(Boolean);
    let highlightedText = text;
    keywords.forEach((k) => {
      const regex = new RegExp(`(${k})`, "gi");
      highlightedText = highlightedText.replace(
        regex,
        `<mark class='highlight'>$1</mark>`
      );
    });
    return <span dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };

  const getChartData = () => {
    if (!result?.keywordCounts) return null;
    const labels = Object.keys(result.keywordCounts);
    const counts = Object.values(result.keywordCounts);
    const densities = result.keywordDensity
      ? Object.values(result.keywordDensity)
      : [];

    return {
      labels,
      datasets: [
        {
          label: "Keyword Count",
          data: counts,
          backgroundColor: darkMode
            ? "rgba(0, 191, 255, 0.6)"
            : "rgba(54, 162, 235, 0.6)",
          borderColor: darkMode
            ? "rgba(0, 191, 255, 1)"
            : "rgba(54, 162, 235, 1)",
          borderWidth: 2,
          borderRadius: 10,
        },
        {
          label: "Keyword Density (%)",
          data: densities,
          backgroundColor: darkMode
            ? "rgba(255, 193, 7, 0.6)"
            : "rgba(255, 206, 86, 0.6)",
          borderColor: darkMode
            ? "rgba(255, 193, 7, 1)"
            : "rgba(255, 206, 86, 1)",
          borderWidth: 2,
          borderRadius: 10,
        },
      ],
    };
  };

  const chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    animation: { duration: 1200, easing: "easeOutQuart" },
    plugins: {
      legend: {
        position: "top",
        labels: {
          color: darkMode ? "#f5f5f5" : "#003e9c",
          font: { size: 15, family: "Poppins, sans-serif", weight: "600" },
        },
      },
      title: {
        display: true,
        text: "Keyword Frequency vs Density",
        color: darkMode ? "#ffffff" : "#003e9c",
        font: { size: 20, weight: "700", family: "Poppins, sans-serif" },
        padding: { top: 20, bottom: 20 },
      },
      tooltip: {
        backgroundColor: darkMode ? "rgba(25,25,25,0.9)" : "rgba(255,255,255,0.9)",
        titleColor: darkMode ? "#fff" : "#000",
        bodyColor: darkMode ? "#ddd" : "#1c1c1c",
        borderColor: darkMode ? "#0dcaf0" : "#007bff",
        borderWidth: 1,
        titleFont: { family: "Poppins", size: 14, weight: "600" },
        bodyFont: { family: "Poppins", size: 13 },
      },
    },
    scales: {
      x: {
        grid: { color: darkMode ? "rgba(255,255,255,0.08)" : "rgba(0,0,0,0.05)" },
        ticks: { color: darkMode ? "#eee" : "#333", font: { family: "Poppins", size: 13 } },
      },
      y: {
        grid: { color: darkMode ? "rgba(255,255,255,0.08)" : "rgba(0,0,0,0.05)" },
        ticks: { color: darkMode ? "#eee" : "#333", font: { family: "Poppins", size: 13 } },
      },
    },
  };

  return (
    <div className={`app-container ${darkMode ? "dark-mode" : ""}`}>
      {/* Header */}
      <div className="header-row" style={{ display: "flex", justifyContent: "center", alignItems: "center", gap: "12px" }}>
        <h1 className="animated-title" style={{ fontFamily: "Poppins, sans-serif" }}>
          Web Scraper Analyzer
        </h1>
        <button
          className="mode-toggle"
          onClick={() => {
            const newMode = !darkMode;
            setDarkMode(newMode);
            localStorage.setItem("darkMode", JSON.stringify(newMode));
          }}
          title={darkMode ? "Switch to Light Mode" : "Switch to Dark Mode"}
        >
          {darkMode ? "☀️" : "🌙"}
        </button>
      </div>

      {/* Input form */}
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          placeholder="Enter website URL..."
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          required
        />
        <input
          type="text"
          placeholder="Enter keywords (comma separated)..."
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit" disabled={loading}>
          {loading ? "Analyzing..." : "Analyze"}
        </button>
      </form>

      {error && <p className="error">{error}</p>}

      {/* ✅ Result Display */}
      {result && (
        <div className="result">
          <h2>Analysis Result</h2>
          <p><strong>Title:</strong> {highlightKeyword(result.title, keyword)}</p>
          <p><strong>Description:</strong> {result.description}</p>

          <h3>Keyword Counts</h3>
          {result.keywordCounts ? (
            <>
              <ul>
                {Object.entries(result.keywordCounts).map(([word, count], idx) => (
                  <li key={idx}><strong>{word}</strong>: {count}</li>
                ))}
              </ul>

              {result.keywordDensity && (
                <div className="density-section">
                  <h3>Keyword Density (%)</h3>
                  <ul>
                    {Object.entries(result.keywordDensity).map(([word, density], idx) => (
                      <li key={idx}><strong>{word}</strong>: {density.toFixed(2)}%</li>
                    ))}
                  </ul>
                </div>
              )}

              {/* ✅ Summary */}
              <div className="summary-box">
                <p><strong>Total Keywords:</strong> {Object.values(result.keywordCounts).reduce((a, b) => a + b, 0)}</p>
                <p><strong>Average Density:</strong> {(
                  Object.values(result.keywordDensity).reduce((a, b) => a + b, 0) /
                  Object.keys(result.keywordDensity).length
                ).toFixed(2)}%</p>
              </div>

              {/* ✅ Chart */}
              {Object.keys(result.keywordCounts).length > 0 && (
                <div className="chart-container">
                  <Bar data={getChartData()} options={chartOptions} />
                </div>
              )}

              {/* ✅ Nested URLs */}
              {result.nestedUrls && (
                <div className="nested-section">
                  <h3>Nested URLs</h3>
                  <ul>
                    {result.nestedUrls.split("\n").map((link, idx) => (
                      link.trim() && (
                        <li key={idx}>
                          <a href={link} target="_blank" rel="noopener noreferrer">
                            {link.length > 80 ? link.substring(0, 80) + "..." : link}
                          </a>
                        </li>
                      )
                    ))}
                  </ul>
                </div>
              )}

              {/* ✅ Download CSV */}
              {result.gcpFileUrl && (
                <button
                  onClick={() => window.open(result.gcpFileUrl, "_blank")}
                  className="download-btn"
                >
                  ⬇️ Download CSV Report from Google Cloud
                </button>
              )}
            </>
          ) : (
            <p>No keywords found.</p>
          )}
        </div>
      )}
    </div>
  );
}

export default App;
