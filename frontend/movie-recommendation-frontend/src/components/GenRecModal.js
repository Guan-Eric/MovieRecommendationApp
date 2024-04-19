import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './GenRecModal.css';

function GenRecModal({ isOpen, onClose, onGenerate }) {
  const navigate = useNavigate(); // Hook for navigation
  const [loading, setLoading] = useState(false);  // State to manage loading status

  const options = {
  Genre: ["Action", "Comedy", "Drama", "Horror", "Science Fiction", "Romance", "Thriller", "Mystery", "Adventure", "Fantasy", "Crime", "Documentary", "Animation", "Historical", "Musical"],
  Mood: ["In the mood for laughs", "Feeling adventurous", "Ready to be spooked", "Looking for a cry", "Want to think", "Need inspiration", "Feeling nostalgic", "Escape reality", "Crave excitement", "Enjoy complexity", "Desire romance", "Family time", "Mind-bending", "Light entertainment", "Seeking thrills"],
 "Viewer Impact": ["Thought-provoking", "Emotionally resonant", "Escapist", "Inspirational", "Educational", "Nostalgic", "Uplifting", "Heartbreaking", "Motivational", "Anxiety-inducing", "Mind-bending", "Satisfying conclusion", "Stress-relieving", "Fear-inducing", "Empowering"],
  Complexity: ["Mind-twisting", "Straightforward", "Layered storytelling", "Symbolic", "Philosophical", "Psychological", "Meta-narrative", "Non-linear", "Allegorical", "Satirical", "Surreal", "Realistic", "Fantastical", "Paranormal", "Historical re-enactment"],
  Pacing: ["Fast-paced", "Moderate pace", "Slow-paced", "Epic scope", "Intensely suspenseful", "Leisurely and lyrical", "Steadily unfolding", "Highly dynamic", "Variable pace", "Flash-paced", "Meditative", "Pulse-pounding", "Gripping", "Relaxed", "Breakneck"],
  Era: ["Silent film era (1890s–1920s)", "Golden Age of Hollywood (1930s–1950s)", "New Hollywood (1960s–1980s)", "Blockbuster Era (1980s–2000s)", "Modern Cinema (2000s–Present)", "Pre-Code Hollywood (early 1930s)", "Post-War Hollywood (1940s–1950s)", "Digital Age (1990s–Present)", "Indie Film Surge (1990s–2000s)", "Streaming Era (2010s–Present)", "International Cinema (1960s–Present)", "Experimental Film (1960s–1980s)", "Blaxploitation Era (1970s)", "Rise of Asian Cinema (2000s–Present)", "Revival of Musicals (2000s–Present)"],
  Theme: ["Love and relationships", "Identity and self-discovery", "Family dynamics", "Social justice", "Power and corruption", "Friendship and loyalty", "Redemption and forgiveness", "Survival and resilience", "Coming to terms with mortality", "Technology and society", "Environmentalism", "War and conflict", "Freedom and oppression", "Dreams and aspirations", "The human condition"],
  Plot: ["Twist-filled", "Hero's journey", "Mystery-solving", "Thought-provoking", "Coming-of-age", "Revenge", "Survival", "Redemption", "Quest", "Conspiracy", "Time-travel", "Psychological thriller", "Heist", "Courtroom drama", "Political intrigue"],
  Setting: ["Contemporary", "Historical", "Futuristic", "Urban", "Rural", "International", "Outer space", "Fantasy world", "Post-apocalyptic", "War-torn", "Suburban", "Small town", "Underwater"],
  "Visuals/Effects": ["Stunning visuals", "Groundbreaking effects", "Practical effects", "CGI-heavy", "Cinematic spectacle", "Immersive world-building", "Realistic", "Surreal"],
  Soundtrack: ["Orchestral score", "Pop/rock soundtrack", "Ambient/electronic score", "Jazz/blues soundtrack", "Hip-hop/rap soundtrack", "Classical music", "Original songs", "Iconic theme song"],
  "Directing Style": ["Visual flair", "Emotionally resonant", "Visually stunning", "Experimental", "Character-driven", "Dialogue-driven", "Non-linear storytelling", "Surreal", "Minimalist", "Epic", "Naturalistic", "Satirical", "Neo-noir", "Poetic", "High-concept"],
  "Target Audience": ["General audience", "Family-friendly", "Teen", "Adult", "Mature", "Kids", "Young adults", "Senior citizens", "Women-centric", "Men-centric", "LGBTQ+ themes", "Culturally specific", "Educational", "Special interest", "Horror enthusiasts"],
  Length: ["Short (under 90 minutes)", "Standard (90-120 minutes)", "Long (over 120 minutes)", "Epic (over 3 hours)"],
  Ratings: ["Highly-rated", "Critically acclaimed", "Audience favorite", "Cult classic", "Award-winning", "Hidden gem", "Mixed reviews", "Polarizing", "Overlooked", "Sleeper hit", "Box office success", "Indie darling"],
  "Content Format": ["Movie", "Series", "Mini-series", "Documentary", "Short film"],
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
    setLoading(true);  // Set loading to true
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
      navigate('/recommendations', { state: { movies: data } });
    } catch (error) {
      console.error("Failed to send data:", error);
    } finally {
      setLoading(false);  // Set loading to false regardless of success or failure
    }
  };

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
      <div className="modal-header">What are you feeling?</div>
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
          <button onClick={handleSubmit} disabled={loading} className="modal-button confirm">
            {loading ? 'Generating...' : 'Generate'}
          </button>
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
