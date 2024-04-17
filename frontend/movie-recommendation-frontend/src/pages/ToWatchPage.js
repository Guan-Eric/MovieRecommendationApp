import React, { useState} from 'react';
import { useNavigate } from 'react-router-dom';
import GenRecModal from '../components/GenRecModal';
import ConfirmModal from '../components/ConfirmModal';
import RatingModal from '../components/RatingModal';
import './ToWatchPage.css';
import { useAuth } from '../context/AuthContext';

const hardcodedMovies = [
  { id: 1, title: 'Inception', description: 'A thief who steals corporate secrets through the use of dream-sharing technology.' },
  { id: 2, title: 'Interstellar', description: 'A team of explorers travel through a wormhole in space in an attempt to ensure humanity\'s survival.' },
  { id: 3, title: 'The Dark Knight', description: 'When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.' },
  { id: 4, title: 'The Matrix', description: 'A computer hacker learns from mysterious rebels about the true nature of his reality and his role in the war against its controllers.' },
  { id: 5, title: 'Avatar', description: 'A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.' },
  { id: 6, title: 'Titanic', description: 'A seventeen-year-old aristocrat falls in love with a kind but poor artist aboard the luxurious, ill-fated R.M.S. Titanic.' },
];

function ToWatchPage() {
    const [genRecModalOpen, setGenRecModalOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [ratingOpen, setRatingOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const navigate = useNavigate();
    const {logout} = useAuth();

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
                {hardcodedMovies.map(movie => (
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
            />
        </div>
    );

}

export default ToWatchPage;

