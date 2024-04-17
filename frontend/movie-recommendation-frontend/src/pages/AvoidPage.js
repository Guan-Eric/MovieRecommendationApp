import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfirmModal from '../components/ConfirmModal';
import RatingModal from "../components/RatingModal";
import './ToWatchPage.css';
import './RatingModal.css';

const initialMovies = [
  { id: 2, title: 'Interstellar' },
  { id: 3, title: 'The Dark Knight' },
  { id: 4, title: 'The Matrix' },
  { id: 6, title: 'Titanic' },
];

function StarRating({ rating }) {
    return (
        <div style={{ display: 'flex', justifyContent: 'center', width: '100%' }}>
            {[...Array(5)].map((_, index) => {
                index += 1;
                return (
                    <span
                        key={index}
                        className={index <= rating ? 'on' : 'off'}
                        style={{ padding: '0', fontSize: '24px', cursor: 'default' }} 
                    >
                        &#9733; {/* Star Unicode Character */}
                    </span>
                );
            })}
        </div>
    );
}


function SeenPage() {
    const [movies, setMovies] = useState(initialMovies);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [ratingModalOpen, setRatingModalOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const navigate = useNavigate();

    const handleRemoveMovie = (e, movie) => {
        e.stopPropagation();  // Prevents triggering the card click event
        console.log('Preparing to remove:', movie.title);
        setSelectedMovie(movie);
        setConfirmOpen(true);
    };

    const handleCardClick = (movie) => {
        setSelectedMovie(movie);
        setRatingModalOpen(true);
    };

    const updateMovieRating = (movie, newRating) => {
        setMovies(currentMovies => currentMovies.map(m => m.id === movie.id ? { ...m, rating: newRating } : m));
        setRatingModalOpen(false);
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">
              <button onClick={() => navigate('/to-watch')} className="nav-button nav-button-inactive">To Watch</button>
              <button onClick={() => navigate('/seen')} className="nav-button nav-button-inactive">Seen</button>
              <button onClick={() => navigate('/avoid')} className="nav-button nav-button-active">To Avoid</button>
            </div>
            <div className="movie-list">
                {movies.map(movie => (
                    <div key={movie.id} className="movie-card" onClick={() => handleCardClick(movie)}>
                        <div className="movie-info">
                            <h3>{movie.title}</h3>
                            <StarRating rating={movie.rating} />
                        </div>
                        <div className="movie-actions">
                            <button className="button red-button" onClick={(e) => handleRemoveMovie(e, movie)}>✖️</button>
                        </div>
                    </div>
                ))}
            </div>
            <ConfirmModal
                isOpen={confirmOpen}
                onClose={() => setConfirmOpen(false)}
                onConfirm={() => {
                    console.log('Removing:', selectedMovie.title);
                    setMovies(currentMovies => currentMovies.filter(m => m.id !== selectedMovie.id));
                    setConfirmOpen(false);
                }}
            >
                Are you sure you want to remove this movie?
            </ConfirmModal>
        <RatingModal
            isOpen={ratingModalOpen}
            onClose={() => setRatingModalOpen(false)}
            onConfirm={updateMovieRating}
            movie={selectedMovie}
            headerText="Would you like to change your rating?"
            paragraphText="Select the new star rating you would like to assign to this movie."
        />
        </div>
    );
}

export default AvoidPage;

