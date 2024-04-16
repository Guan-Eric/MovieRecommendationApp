import React, { useState, useEffect } from 'react';
import './RatingModal.css';

function RatingModal({ isOpen, onClose, onConfirm, movie }) {
    const [rating, setRating] = useState(0);
    
    useEffect(() => {
        if (!isOpen) {
            setRating(0);  // Reset rating to 0 when modal is closed
        }
    }, [isOpen]);  // Trigger on isOpen change

    const handleRating = (rate) => {
        setRating(rate);
    };

    const StarRating = () => {
        return (
            <div style={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
                {[...Array(5)].map((star, index) => {
                    index += 1;
                    return (
                        <button
                            key={index}
                            className={index <= rating ? 'on' : 'off'}
                            onClick={() => handleRating(index)}
                            style={{ border: 'none', padding: '0', fontSize: '24px', backgroundColor: 'transparent', cursor: 'pointer' }}
                        >
                            &#9733; {/* Star Unicode Character */}
                        </button>
                    );
                })}
            </div>
        );
    };

    if (!isOpen) return null;

    return (
        <div className="modal-backdrop">
            <div className="modal-content">
                <h4>Have you seen the movie? If so, please submit a rating below.</h4>
                <p>Your rating will be used by the AI to make better reccomendations in the future.</p>
                <StarRating />
                <div className="modal-actions">
                    <button onClick={() => onConfirm(movie, rating)} className="modal-button confirm">Confirm</button>
                    <button onClick={() => {
                        onClose();
                        setRating(0);  // Also reset rating when manually closing the modal
                    }} className="modal-button cancel">Close</button>
                </div>
            </div>
        </div>
    );
}

export default RatingModal;

