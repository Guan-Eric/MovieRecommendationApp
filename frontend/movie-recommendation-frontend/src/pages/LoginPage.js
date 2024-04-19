import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './LoginPage.css'

function LoginPage() {
    const { login } = useAuth();
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const handleSubmit = async (event) => {
        event.preventDefault();
        if (!username || !password) {
            alert("Username and password are required.");
            return;
        }

        const response = await fetch('/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName: username, userPassword: password })
        });
        if (response.ok) {
            const data = await response.json();
            login(data); // Assuming login updates context and redirects
        } else {
            const errorData = await response.json();
            alert("Failed to login: " + errorData.error);
        }
    };


    return (
        <div className="login-wrapper">
            <h2 className="form-title">Movie Recommendation App</h2>
            <form className="login-form" onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="username">Username:</label>
                    <input id="username" type="text" className="form-control" value={username}
                           onChange={(e) => setUsername(e.target.value)}/>
                </div>
                <div className="form-group">
                    <label htmlFor="password">Password:</label>
                    <input id="password" type="password" className="form-control" value={password}
                           onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <button type="submit" className="btn login-btn">Login</button>
                <p className="signup-link">Don't have an account? <a href="/signup">Sign up</a></p>
            </form>
        </div>
    );
}

export default LoginPage;
