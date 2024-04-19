import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import './SignupPage.css'
import logo from '../assets/movy_the_goat_with_title-nobg.png'


function SignupPage() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [apikey, setApikey] = useState('');
    const { login } = useAuth();

    const handleSubmit = async (event) => {
        event.preventDefault();
        const response = await fetch('/signup', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ userName: username, userPassword: password, apiKey: apikey })
        });
        if (response.ok) {
            const data = await response.json();
            login(data);
        } else {
            const errorData = await response.json();
            alert("Failed to signup: " + errorData.error);
        }
    };

    return (
        <div className="signup-wrapper">
            <img src={logo} alt="Movie Recommendation App Logo" className="logo"/>

            <form className="signup-form" onSubmit={handleSubmit}>
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
                <div className="form-group">
                    <label htmlFor="apikey">API Key:</label>
                    <input id="apikey" type="text" className="form-control" value={apikey}
                           onChange={(e) => setApikey(e.target.value)}/>
                </div>
                <button type="submit" className="btn signup-btn">Signup</button>
                <p className="login-link">Already have an account? <a href="/login">Login</a></p>
            </form>
        </div>
    );
}

export default SignupPage;
