import React from 'react';
import './ConfirmModal.css';  

function ConfirmModal({ isOpen, onClose, onConfirm, children }) {
  if (!isOpen) return null;

  return (
    <div className="modal-backdrop">
      <div className="modal-content">
        <h4>{children}</h4>
        <div className="modal-actions">
          <button onClick={onConfirm} className="modal-button confirm">Yes</button>
          <button onClick={onClose} className="modal-button cancel">No</button>
        </div>
      </div>
    </div>
  );
}

export default ConfirmModal;

