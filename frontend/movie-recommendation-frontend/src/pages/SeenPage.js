import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import ConfirmModal from '../components/ConfirmModal';
import RatingModal from '../components/RatingModal';
import GenRecModal from '../components/GenRecModal';
import './ToWatchPage.css';
import '../components/RatingModal.css';


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
    const [movies, setMovies] = useState([]);
    const [genRecModalOpen, setGenRecModalOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [ratingModalOpen, setRatingModalOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const navigate = useNavigate();

    useEffect(() => {
        fetch('http://localhost:8080/seen', {credentials: 'include'})
            .then(response => response.json())
            .then(data => setMovies(data.map(item => ({
                id: item.movie.id,
                title: item.movie.movieName,
                year: item.movie.date,
                description: item.movie.description,
                posterUrl: item.movie.posterUrl,
                trailerUrl: item.movie.trailerUrl,
                genres: item.movie.genres,
                status: item.status.statusName,
                rating: item.rating
            }))))
            .catch(error => console.error('Error fetching movies:', error));
    }, []);

    const handleGenerateRecommendations = () => {
        console.log('Opening Generate Recommendations Modal');
        setGenRecModalOpen(true);
    };

    const goToRecommendations = () => {
        console.log('Navigating to Recommendations Page');
        navigate('/recommendations');
    };

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

    const updateMovieRating = (newRating) => {
        console.log('Updating Rating for:', selectedMovie.title, 'to', newRating);
        // Update the rating locally for immediate feedback
        setMovies(currentMovies => currentMovies.map(m => m.id === selectedMovie.id ? { ...m, rating: newRating } : m));
        setRatingModalOpen(false);
        // Optionally: Send a PUT/POST request to your backend to update the rating in the database
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">
              <button onClick={() => navigate('/to-watch')} className="nav-button nav-button-inactive">To Watch</button>
              <button onClick={() => navigate('/seen')} className="nav-button nav-button-active">Seen</button>
              <button onClick={() => navigate('/avoid')} className="nav-button nav-button-inactive">To Avoid</button>
            </div>
            <div className="movie-list">
                {movies.map(movie => (
                    <div key={movie.id} className="movie-card" onClick={() => handleCardClick(movie)}>
                        <div className="movie-info">
                            <h3>{movie.title} ({movie.year})</h3>
                            <StarRating rating={movie.rating} />
                        </div>
                        <div className="movie-actions">
                            <button className="button red-button" onClick={(e) => handleRemoveMovie(e, movie)}>✖️</button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="fixed-bottom-container">
                <button onClick={handleGenerateRecommendations} className="recommend-button">Generate Recommendations</button>
            </div>
            {genRecModalOpen && <GenRecModal isOpen={genRecModalOpen} onClose={() => setGenRecModalOpen(false)} onGenerate={goToRecommendations} />}
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

export default SeenPage;