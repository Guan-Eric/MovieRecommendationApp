import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import RatingModal from '../components/RatingModal'; // Make sure the path is correct
import './ToWatchPage.css';

function RecommendationsPage() {
    const location = useLocation();
    const navigate = useNavigate();

    // Enhance movie data with a fallback ID right from the beginning
    const initialMovies = location.state?.movies.map(movie => ({
        ...movie,
        id: movie.id || `fallback-${Math.random().toString(16).slice(2)}`
    })) || [];

    const [recommendations, setRecommendations] = useState(initialMovies);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);

    const postMovie = async (movie, status, rating = null) => {
        try {
            const response = await fetch('http://localhost:8080/addmovie', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                credentials: 'include',
                body: JSON.stringify({
                    movieName: movie.movieName,
                    description: movie.description,
                    date: movie.date,
                    statusName: status,
                    rating: rating,
                }),
            });

            const data = await response.json();
            console.log('Response:', data);
            if (response.ok) {
                handleRemoveMovie(movie.id);
            } else {
                throw new Error(data.message || "Failed to update movie status");
            }
        } catch (error) {
            console.error('Error:', error);
        } finally {
            closeModal(); // Ensure modal is closed
        }
    };

    const handleRemoveMovie = (movieId) => {
        setRecommendations(prevRecommendations => 
            prevRecommendations.filter(movie => movie.id !== movieId)
        );
    };

    const addToWatch = movie => postMovie(movie, 'towatch');
    const addToAvoid = movie => postMovie(movie, 'tonotwatch');
    const addToSeen = movie => {
        setSelectedMovie(movie);  // Set the current movie for rating
        setIsModalOpen(true);     // Open the rating modal
    };

    const handleRatingConfirm = (movie, rating) => {
        postMovie(movie, 'seen', rating);
    };

    const closeModal = () => {
        setIsModalOpen(false);
        setSelectedMovie(null);  // Clear the selected movie
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">AI-Generated Recommendations</div>
            <div className="movie-list">
                {recommendations.map(movie => (
                    <div key={movie.id} className="movie-card">
                        <div className="movie-info">
                            <h3>{movie.movieName}</h3>
                            <p>{movie.description}</p>
                            <p>Release Year: {movie.date}</p>
                        </div>
                        <div className="movie-actions">
                            <button className="button green-button" onClick={() => addToWatch(movie)}>Want to watch</button>
                            <button className="button red-button" onClick={() => addToAvoid(movie)}>Don't want to watch</button>
                            <button className="button" onClick={() => addToSeen(movie)}>Already seen</button>
                        </div>
                    </div>
                ))}
            </div>
            {selectedMovie && (
                <RatingModal
                    isOpen={isModalOpen}
                    onClose={closeModal}
                    onConfirm={handleRatingConfirm}
                    movie={selectedMovie}
                    headerText="Rate This Movie"
                    paragraphText="Select your rating by clicking the stars:"
                />
            )}
            <div className="fixed-bottom-container">
                <button onClick={() => navigate("/")}>Go Back</button>
            </div>
        </div>
    );
}

export default RecommendationsPage;
