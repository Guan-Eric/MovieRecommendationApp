import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './ToWatchPage.css'; 

function RecommendationsPage() {
    const [recommendations, setRecommendations] = useState([
        { id: 4, title: 'Avatar', description: 'A paraplegic Marine dispatched to the moon Pandora on a unique mission becomes torn between following his orders and protecting the world he feels is his home.', genre: 'Fantasy' },
        { id: 5, title: 'Gravity', description: 'Two astronauts work together to survive after an accident leaves them stranded in space.', genre: 'Sci-Fi' },
        { id: 6, title: 'Joker', description: 'In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society. He then embarks on a downward spiral of revolution and bloody crime.', genre: 'Drama' },
    ]);
    const navigate = useNavigate();

    const handleRemoveMovie = (movieId) => {
        const updatedRecommendations = recommendations.filter(movie => movie.id !== movieId);
        setRecommendations(updatedRecommendations);
    };

    const addToWatch = movie => {
        console.log('Add to To Watch:', movie.title);
        handleRemoveMovie(movie.id);
    };

    const addToAvoid = movie => {
        console.log('Add to To Avoid:', movie.title);
        handleRemoveMovie(movie.id);
    };

    const addToSeen = movie => {
        console.log('Add to Already Seen:', movie.title);
        handleRemoveMovie(movie.id);
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">Recommendations</div>
            <div className="movie-list">
                {recommendations.map(movie => (
                    <div key={movie.id} className="movie-card">
                        <div className="movie-info">
                            <h3>{movie.title}</h3>
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
