import React from 'react';
import Navbar from './Navbar';

const Layout: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  return (
    <>
      <Navbar />
      <main className="container mt-4" style={{ flex: 1, paddingBottom: '2rem' }}>
        {children}
      </main>
      <footer className="mt-4 py-4 border-t text-center text-sm text-secondary">
        <div className="container">
          &copy; {new Date().getFullYear()} Valuefy Portfolio Rebalancing System. Built with React & Spring Boot.
        </div>
      </footer>
      <style>{`
        .border-t { border-top: 1px solid var(--border); }
        .py-4 { padding-top: 1rem; padding-bottom: 1rem; }
        .text-secondary { color: var(--text-secondary); }
        .text-sm { font-size: 0.875rem; }
      `}</style>
    </>
  );
};

export default Layout;
