import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

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
        <form onSubmit={handleSubmit}>
            <label>
                Username:
                <input type="text" value={username} onChange={(e) => setUsername(e.target.value)}/>
            </label>
            <label>
                Password:
                <input type="password" value={password} onChange={(e) => setPassword(e.target.value)}/>
            </label>
            <label>
                API Key:
                <input type="apiKey" value={apikey} onChange={(e) => setApikey(e.target.value)}/>
            </label>
            <button type="submit">Signup</button>

            <p>Already have an account? <a href="/login">Login</a></p>

        </form>
    );
}

export default SignupPage;
