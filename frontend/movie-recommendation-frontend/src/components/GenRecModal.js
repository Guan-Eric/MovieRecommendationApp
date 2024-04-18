import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './GenRecModal.css';

function GenRecModal({ isOpen, onClose, onGenerate }) {
  const navigate = useNavigate(); // Hook for navigation

  const options = {
    Genre: ["Action", "Comedy", "Drama", "Horror", "Science Fiction", "Romance", "Thriller", "Mystery", "Adventure", "Fantasy", "Crime", "Documentary", "Animation", "Historical", "Musical"],
    Plot: ["Twist-filled", "Hero's journey", "Mystery-solving", "Thought-provoking", "Coming-of-age", "Revenge", "Survival", "Redemption", "Quest", "Conspiracy", "Time-travel", "Psychological thriller", "Heist", "Courtroom drama", "Political intrigue"],
    "Directing Style": ["Visual flair", "Emotionally resonant", "Visually stunning", "Experimental", "Character-driven", "Dialogue-driven", "Non-linear storytelling", "Surreal", "Minimalist", "Epic", "Naturalistic", "Satirical", "Neo-noir", "Poetic", "High-concept"],
    Ratings: ["Highly-rated", "Critically acclaimed", "Audience favorite", "Cult classic", "Award-winning", "Hidden gem", "Mixed reviews", "Polarizing", "Overlooked", "Sleeper hit", "Box office success", "Indie darling"],
    Length: ["Short (under 90 minutes)", "Standard (90-120 minutes)", "Long (over 120 minutes)", "Epic (over 3 hours)"],
    Setting: ["Contemporary", "Historical", "Futuristic", "Urban", "Rural", "International", "Outer space", "Fantasy world", "Post-apocalyptic", "War-torn", "Suburban", "Small town", "Underwater"],
    "Visuals/Effects": ["Stunning visuals", "Groundbreaking effects", "Practical effects", "CGI-heavy", "Cinematic spectacle", "Immersive world-building", "Realistic", "Surreal"],
    Soundtrack: ["Orchestral score", "Pop/rock soundtrack", "Ambient/electronic score", "Jazz/blues soundtrack", "Hip-hop/rap soundtrack", "Classical music", "Original songs", "Iconic theme song"],
    Theme: ["Love and relationships", "Identity and self-discovery", "Family dynamics", "Social justice", "Power and corruption", "Friendship and loyalty", "Redemption and forgiveness", "Survival and resilience", "Coming to terms with mortality", "Technology and society", "Environmentalism", "War and conflict", "Freedom and oppression", "Dreams and aspirations", "The human condition"]
  };

  const [selections, setSelections] = useState(Object.keys(options).reduce((acc, key) => ({ ...acc, [key]: "Any" }), {}));
  const [showOptionsModal, setShowOptionsModal] = useState(false);
  const [currentOptions, setCurrentOptions] = useState([]);
  const [currentKey, setCurrentKey] = useState("");

  if (!isOpen) return null;

  const handleOptionClick = (key) => {
    setCurrentOptions(["Any", ...options[key]]);
    setCurrentKey(key);
    setShowOptionsModal(true);
  };

  const handleOptionSelect = (option) => {
    setSelections(prev => ({ ...prev, [currentKey]: option }));
    setShowOptionsModal(false);
  };

  const handleCloseOptionsModal = () => {
    setShowOptionsModal(false);
  };

  const handleSubmit = async () => {
    const postData = Object.entries(selections)
      .filter(([key, value]) => value !== "Any")
      .map(([key, value]) => `${key}: ${value}`);

    try {
      const response = await fetch('http://localhost:8080/generate', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        credentials: 'include',
        body: JSON.stringify(postData),
      });
      const data = await response.json();
      onGenerate(data);
      navigate('/recommendations', { state: { movies: data } }); // Navigate with state
    } catch (error) {
      console.error("Failed to send data:", error);
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
        <div className="modal-header">Generate Recommendations</div>
        <div className="modal-body">
          {Object.keys(options).map((key) => (
            <div key={key}>
              <label>{key}:
                <button onClick={() => handleOptionClick(key)} className="option-button">
                  {selections[key]}
                </button>
              </label>
              <br />
            </div>
          ))}
        </div>
        <div className="modal-actions">
          <button onClick={handleSubmit} className="modal-button confirm">Generate</button>
          <button onClick={onClose} className="modal-button cancel">Close</button>
        </div>
      </div>
      {showOptionsModal && (
        <div className="options-modal-content">
          <button className="modal-close-btn" onClick={handleCloseOptionsModal}>&times;</button>
          {currentOptions.map(option => (
            <button key={option} onClick={() => handleOptionSelect(option)} className="modal-option-item">
              {option}
            </button>
          ))}
        </div>
      )}
    </div>
  );
}

export default GenRecModal;

