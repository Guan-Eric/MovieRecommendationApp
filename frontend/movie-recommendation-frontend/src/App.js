import React from 'react';
import { BrowserRouter as Router, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import LoginPage from "./pages/LoginPage";
import SignupPage from "./pages/SignupPage";
import ToWatchPage from './pages/ToWatchPage';
import SeenPage from './pages/SeenPage';
import AvoidPage from './pages/AvoidPage';
import RecommendationsPage from './pages/RecommendationsPage';
import './App.css';

const PrivateRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();
    return isAuthenticated ? children : <Navigate to="/login" />;
};

const PublicRoute = ({ children }) => {
    const { isAuthenticated } = useAuth();
    return !isAuthenticated ? children : <Navigate to="/to-watch" />;
};


const handleLogin = (userData) => {
    console.log("Logged in user:", userData);
};

function App() {
    return (
        <Router>
            <AuthProvider>
                <Routes>
                    <Route path="/login" element={<PublicRoute><LoginPage onLogin={handleLogin} /></PublicRoute>} />
                    <Route path="/signup" element={<PublicRoute><SignupPage /></PublicRoute>} />
                    <Route path="/" element={<PrivateRoute><ToWatchPage /></PrivateRoute>} />
                    <Route path="/to-watch" element={<PrivateRoute><ToWatchPage /></PrivateRoute>} />
                    <Route path="/seen" element={<PrivateRoute><SeenPage /></PrivateRoute>} />
                    <Route path="/avoid" element={<PrivateRoute><AvoidPage /></PrivateRoute>} />
                    <Route path="/recommendations" element={<PrivateRoute><RecommendationsPage /></PrivateRoute>} />
                </Routes>
            </AuthProvider>
        </Router>
    );
}


export default App;
