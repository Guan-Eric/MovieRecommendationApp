import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import './Sidebar.css';
import { useAuth } from '../context/AuthContext';

function Sidebar() {
    const [isOpen, setIsOpen] = useState(false);
    const auth = useAuth();
    const navigate = useNavigate();

    const toggleSidebar = () => setIsOpen(!isOpen);
    const handleLogout = () => {
        auth.logout();
        navigate('/login');
    };

    return (
        <div>
            <button onClick={toggleSidebar} className={`toggle-button ${isOpen ? 'open' : ''}`}>
                {isOpen ? '✖' : '☰'}
            </button>
            <div className={`sidebar ${isOpen ? 'open' : ''}`}>
                <div className="sidebar-content">
                    <p> Settings</p>
                </div>
                <div className="sidebar-bottom">
                    <button onClick={handleLogout} className="sidebar-logout-button">Logout</button>
                </div>
            </div>
        </div>
    );
}

export default Sidebar;
