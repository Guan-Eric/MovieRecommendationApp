import React, { createContext, useContext, useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';

const AuthContext = createContext();

export function useAuth() {
    return useContext(AuthContext);
}

export const AuthProvider = ({ children }) => {
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const navigate = useNavigate();

    useEffect(() => {
        const verifyAuth = async () => {
            const response = await fetch('/checkUserAuthentication', {
                credentials: 'include', // ensure cookies are sent
                headers: { 'Content-Type': 'application/json' }
            });
            if (response.ok) {
                const result = await response.json();
                setIsAuthenticated(result.isAuthenticated);
            } else {
                setIsAuthenticated(false);
            }
        };

        verifyAuth();
    }, [navigate]);



    const login = (userData) => {
        console.log("Logged in user:", userData);
        setIsAuthenticated(true);
        navigate('/to-watch'); // Navigate to 'To Watch Page' on login
    };

    const logout = async () => {
        const response = await fetch('/logout', {
            method: 'GET',
            credentials: 'include',
        });
        if (response.ok) {
            setIsAuthenticated(false);
            navigate('/login');
        } else {
            console.error('Logout failed');
        }
    };


    const value = {
        isAuthenticated,
        login,
        logout
    };

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};
