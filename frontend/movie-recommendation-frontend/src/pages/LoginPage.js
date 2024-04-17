import React, { useState } from 'react';
import { useAuth } from '../context/AuthContext';

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
        <form onSubmit={handleSubmit}>
            <label>Username:<input type="text" value={username} onChange={(e) => setUsername(e.target.value)} /></label>
            <label>Password:<input type="password" value={password} onChange={(e) => setPassword(e.target.value)} /></label>
            <button type="submit">Login</button>
            <p>Don't have an account? <a href="/signup">Sign up</a></p>
        </form>

    );
}

export default LoginPage;
