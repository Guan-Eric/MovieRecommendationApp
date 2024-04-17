import React from 'react';
import './GenRecModal.css';

function GenRecModal({ isOpen, onClose, onGenerate }) {
  if (!isOpen) return null;

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
        <button className="modal-close-btn" onClick={onClose}>&times;</button>
        <div className="modal-header">Generate Recommendations</div>
        <div className="modal-body">
          <label>Genre:
            <select>
              <option value="sci-fi">Sci-Fi</option>
              <option value="action">Action</option>
              <option value="comedy">Comedy</option>
              <option value="drama">Drama</option>
              <option value="fantasy">Fantasy</option>
              <option value="horror">Horror</option>
              <option value="romance">Romance</option>
              <option value="thriller">Thriller</option>
              <option value="western">Western</option>
              <option value="animation">Animation</option>
              <option value="documentary">Documentary</option>
            </select>
          </label>
          <br />
          <label>Popularity:
            <input type="range" min="1" max="100" />
          </label>
        </div>
        <div className="modal-actions">
          <button onClick={() => {
            console.log("Generate Recommendations button clicked");
            onGenerate();
          }} className="modal-button confirm">Generate</button>
          <button onClick={onClose} className="modal-button cancel">Close</button>
        </div>
      </div>
    </div>
  );
}

export default GenRecModal;

