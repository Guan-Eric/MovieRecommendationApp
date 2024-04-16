import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfirmModal from './ConfirmModal';
import './ToWatchPage.css';

const initialMovies = [
  { id: 2, title: 'Interstellar' },
  { id: 3, title: 'The Dark Knight' },
  { id: 4, title: 'The Matrix' },
  { id: 6, title: 'Titanic' },
];

function AvoidPage() {
    const [movies, setMovies] = useState(initialMovies);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const navigate = useNavigate();

    const handleRemoveMovie = (movie) => {
        console.log('Preparing to remove:', movie.title);
        setSelectedMovie(movie);
        setConfirmOpen(true);
    };

    const confirmRemoval = () => {
        console.log('Removing:', selectedMovie.title);
        setMovies(currentMovies => currentMovies.filter(m => m.id !== selectedMovie.id));
        setConfirmOpen(false);
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
                    <div key={movie.id} className="movie-card">
                        <div className="movie-info">
                            <h3>{movie.title}</h3>
                        </div>
                        <div className="movie-actions">
                            <button className="button red-button" onClick={() => handleRemoveMovie(movie)}>✖️</button>
                        </div>
                    </div>
                ))}
            </div>
            <ConfirmModal
                isOpen={confirmOpen}
                onClose={() => setConfirmOpen(false)}
                onConfirm={confirmRemoval}
            >
                Are you sure you want to remove this movie?
            </ConfirmModal>
        </div>
    );
}

export default AvoidPage;

