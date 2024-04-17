import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfirmModal from '../components/ConfirmModal';
import './ToWatchPage.css';

function AvoidPage() {
    const [movies, setMovies] = useState([]);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/tonotwatch',{
            credentials: 'include'
        })
            .then(response => response.json())
            .then(data => setMovies(data.map(item => ({
                id: item.movie.id,
                title: item.movie.movieName,
                description: item.movie.description,
                posterUrl: item.movie.posterUrl,
                trailerUrl: item.movie.trailerUrl,
                genres: item.movie.genres,
                status: item.status.statusName,
                rating: item.rating
            }))))
            .catch(error => console.error('Error fetching movies:', error));
    }, []);

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
                            <p>{movie.description}</p>
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