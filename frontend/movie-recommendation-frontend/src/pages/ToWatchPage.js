import React, { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import GenRecModal from '../components/GenRecModal';
import ConfirmModal from '../components/ConfirmModal';
import RatingModal from '../components/RatingModal';
import './ToWatchPage.css';
import Sidebar from "../components/Sidebar";
import { useAuth } from '../context/AuthContext';
import { MdDelete } from "react-icons/md";
import { MdRemoveRedEye } from "react-icons/md";


function ToWatchPage() {
    const [movies, setMovies] = useState([]);
    const [loadMovies, setLoadMovies] = useState(false);  // State to trigger re-fetching movies
    const [genRecModalOpen, setGenRecModalOpen] = useState(false);
    const [confirmOpen, setConfirmOpen] = useState(false);
    const [ratingOpen, setRatingOpen] = useState(false);
    const [selectedMovie, setSelectedMovie] = useState(null);
    const navigate = useNavigate();
    const { logout } = useAuth();

    const fetchMovies = () => {
        fetch('http://localhost:8080/towatch', { credentials: 'include' })
            .then(response => response.json())
            .then(data => setMovies(data.map(item => ({
                id: item.movie.id,
                title: item.movie.movieName,
                year: item.movie.date.split('-')[0],  // Extracting just the year from the date
                description: item.movie.description,
                posterUrl: item.movie.posterUrl,
                trailerUrl: item.movie.trailerUrl,
                genres: item.movie.genres,
                status: item.status.statusName,
                rating: item.rating
            }))))
            .catch(error => console.error('Error fetching movies:', error));
    };

    // Initial fetch and re-fetch whenever loadMovies changes
    useEffect(() => {
        fetchMovies();
    }, [loadMovies]);

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
        console.log('Removing:', selectedMovie.title);
        const payload = {
            movieName: selectedMovie.title,
            date: selectedMovie.year
        };

        fetch('http://localhost:8080/removemovie', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(payload),
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to delete the movie');
            }
            return response.json();
        })
        .then(data => {
            console.log('Movie removed successfully:', data);
            setLoadMovies(!loadMovies);  // Toggle to re-fetch movies
        })
        .catch(error => {
            console.error('Error removing movie:', error);
        });

        setConfirmOpen(false);
    };

    const handleRating = (movie) => {
        console.log('Preparing to rate:', movie.title);
        setSelectedMovie(movie);
        setRatingOpen(true);
    };

    const confirmRating = (movie, rating) => {
        console.log('Rated:', movie.title, 'Rating:', rating);
        const payload = {
            movieName: movie.title,
            date: movie.year,
            statusName: "seen",
            rating: rating
        };

        fetch('http://localhost:8080/editmovie', {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
            },
            credentials: 'include',
            body: JSON.stringify(payload),
        })
        .then(response => response.json())
        .then(data => {
            console.log('Movie updated successfully:', data);
            setLoadMovies(!loadMovies);  // Toggle to re-fetch movies
        })
        .catch(error => {
            console.error('Error updating movie:', error);
        });

        setRatingOpen(false);
    };

    return (
        <div className="to-watch-container">
            <div className="to-watch-header">
                <Sidebar />
                <button onClick={() => navigate('/to-watch')} className="nav-button nav-button-active">To Watch</button>
                <button onClick={() => navigate('/seen')} className="nav-button nav-button-inactive">Seen</button>
                <button onClick={() => navigate('/avoid')} className="nav-button nav-button-inactive">To Avoid</button>
            </div>

            <div className="movie-list">
                {movies.map(movie => (
                    <div key={movie.id} className="movie-card">
                        <div className="movie-info">
                            <h3>{movie.title} ({movie.year})</h3>
                            <p>{movie.description}</p>
                        </div>
                        <div className="movie-actions">
                            <button className="button green-button" onClick={() => handleRating(movie)}>
                                <span><MdRemoveRedEye/></span>
                                <span>Seen</span>
                            </button>
                            <button className="button red-button" onClick={() => handleRemoveMovie(movie)}>
                                <span><MdDelete/></span>
                                <span>Remove</span>
                            </button>
                        </div>
                    </div>
                ))}
            </div>
            <div className="fixed-bottom-container">
                <button onClick={handleGenerateRecommendations} className="recommend-button">Generate Recommendations</button>
            </div>
            {genRecModalOpen && <GenRecModal isOpen={genRecModalOpen} onClose={() => setGenRecModalOpen(false)}
                                             onGenerate={goToRecommendations}/>}
            <ConfirmModal
                isOpen={confirmOpen}
                onClose={() => setConfirmOpen(false)}
                onConfirm={confirmRemoval}>
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
