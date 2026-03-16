import React from 'react';
import { NavLink } from 'react-router-dom';

const Navbar = () => {
  return (
    <nav className="navbar">
      <div className="container flex justify-between items-center h-16">
        <NavLink to="/" className="brand flex items-center gap-2">
          <span style={{ fontSize: '1.5rem', fontWeight: 'bold', color: 'var(--primary)' }}>Valuefy</span>
          <span style={{ fontSize: '1.2rem', fontWeight: '500' }}>Rebalance</span>
        </NavLink>
        <div className="flex gap-4">
          <NavLink 
            to="/" 
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Dashboard
          </NavLink>
          <NavLink 
            to="/model-portfolio" 
            className={({ isActive }) => isActive ? 'nav-link active' : 'nav-link'}
          >
            Model Portfolio
          </NavLink>
        </div>
      </div>
      <style>{`
        .navbar {
          background-color: var(--surface);
          border-bottom: 1px solid var(--border);
          position: sticky;
          top: 0;
          z-index: 100;
        }
        .nav-link {
          color: var(--text-secondary);
          font-weight: 500;
          padding: 0.5rem 0.75rem;
          border-radius: var(--radius);
          transition: all 0.2s;
        }
        .nav-link:hover {
          color: var(--primary);
          background-color: rgba(26, 35, 126, 0.05);
        }
        .nav-link.active {
          color: var(--primary);
          background-color: rgba(26, 35, 126, 0.1);
        }
        .brand {
          color: var(--text-primary);
        }
      `}</style>
    </nav>
  );
};

export default Navbar;
