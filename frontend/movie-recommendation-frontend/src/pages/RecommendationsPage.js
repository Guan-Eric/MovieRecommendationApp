import React, { useState, useEffect } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import './ToWatchPage.css'; 

function RecommendationsPage() {
    const location = useLocation(); // Hook to access route state
    const navigate = useNavigate();

    // Initialize state with movies from route state or fallback to a default empty array
    const [recommendations, setRecommendations] = useState(location.state?.movies || []);

    // This function is used to remove a movie from the recommendations list
    const handleRemoveMovie = (movieId) => {
        const updatedRecommendations = recommendations.filter(movie => movie.id !== movieId);
        setRecommendations(updatedRecommendations);
    };

    // Function to handle adding a movie to the "To Watch" list
    const addToWatch = movie => {
        console.log('Add to To Watch:', movie.movieName); // Notice the use of movieName
        handleRemoveMovie(movie.id);
    };

    // Function to handle adding a movie to the "To Avoid" list
    const addToAvoid = movie => {
        console.log('Add to To Avoid:', movie.movieName); // Notice the use of movieName
        handleRemoveMovie(movie.id);
    };

    // Function to handle adding a movie to the "Already Seen" list
    const addToSeen = movie => {
        console.log('Add to Already Seen:', movie.movieName); // Notice the use of movieName
        handleRemoveMovie(movie.id);
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">Recommendations</div>
            <div className="movie-list">
                {recommendations.map(movie => (
                    <div key={movie.movieName} className="movie-card">  {/* Using movieName as key */}
                        <div className="movie-info">
                            <h3>{movie.movieName}</h3>
                            <p>{movie.description}</p>
                        </div>
                        <div className="movie-actions">
                            <button className="button green-button" onClick={() => addToWatch(movie)}>Add to To Watch</button>
                            <button className="button red-button" onClick={() => addToAvoid(movie)}>Add to To Avoid</button>
                            <button className="button" onClick={() => addToSeen(movie)}>Add to Already Seen</button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="fixed-bottom-container">
                <button onClick={() => navigate("/")}>Back to To Watch</button>
            </div>
        </div>
    );
}

export default RecommendationsPage;

