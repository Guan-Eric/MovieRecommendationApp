import React, {useEffect, useState} from 'react';
import { useNavigate } from 'react-router-dom';
import GenRecModal from '../components/GenRecModal';
import ConfirmModal from '../components/ConfirmModal';
import RatingModal from '../components/RatingModal';
import './ToWatchPage.css';
import { useAuth } from '../context/AuthContext';


function ToWatchPage() {
    const [genRecModalOpen, setGenRecModalOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [ratingOpen, setRatingOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const [movies, setMovies] = useState([]);
    const navigate = useNavigate();
    const {logout} = useAuth();


    useEffect(() => {
        fetch('http://localhost:8080/towatch', {
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

    const handleGenerateRecommendations = () => {
        console.log('Opening Generate Recommendations Modal');
        setGenRecModalOpen(true);
    };

    const goToRecommendations = () => {
        console.log('Navigating to Recommendations Page');
        navigate('/recommendations');
    };

    const handleRemoveMovie = (movie) => {
        console.log('Preparing to remove:', movie.title);
        setSelectedMovie(movie);
        setConfirmOpen(true);
    };

    const confirmRemoval = () => {
        // TO IMPLEMENT
        console.log('Removing:', selectedMovie.title);  
        setConfirmOpen(false);
    };

    const handleRating = (movie) => {
        console.log('Preparing to rate:', movie.title);
        setSelectedMovie(movie);
        setRatingOpen(true);
    };

    const confirmRating = (movie, rating) => {
        // TO IMPLEMENT
        console.log('Rated:', movie.title, 'Rating:', rating); 
        setRatingOpen(false);
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">
                <button onClick={() => navigate('/recommendations')} className="nav-button nav-button-active">To Watch
                </button>
                <button onClick={() => navigate('/seen')} className="nav-button nav-button-inactive">Seen</button>
                <button onClick={() => navigate('/avoid')} className="nav-button nav-button-inactive">To Avoid</button>
            </div>
            <div className="logout-container">
                <button onClick={logout} className="logout-button">Logout</button>
            </div>
            <div className="movie-list">
                {movies.map(movie => (
                    <div key={movie.id} className="movie-card">
                        <div className="movie-info">
                            <h3>{movie.title}</h3>
                            <p>{movie.description}</p>
                        </div>
                        <div className="movie-actions">
                            <button className="button green-button" onClick={() => handleRating(movie)}>✔️</button>
                            <button className="button red-button" onClick={() => handleRemoveMovie(movie)}>✖️</button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="fixed-bottom-container">
                <button onClick={handleGenerateRecommendations} className="recommend-button">Generate Recommendations
                </button>
            </div>
            {genRecModalOpen && <GenRecModal isOpen={genRecModalOpen} onClose={() => setGenRecModalOpen(false)}
                                             onGenerate={goToRecommendations}/>}
            <ConfirmModal
                isOpen={confirmOpen}
                onClose={() => setConfirmOpen(false)}
                onConfirm={confirmRemoval}
            >
                Are you sure you want to remove this movie?
            </ConfirmModal>
            <RatingModal
                isOpen={ratingOpen}
                onClose={() => setRatingOpen(false)}
                onConfirm={confirmRating}
                movie={selectedMovie}
                headerText="Have you seen the movie? If so, please submit a rating below."
                paragraphText="Your rating will be used by the AI to make better recommendations in the future."
            />
        </div>
    );

}

export default ToWatchPage;

