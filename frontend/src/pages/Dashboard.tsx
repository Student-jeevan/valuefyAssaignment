import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { clientApi } from '../api';
import { Client } from '../types';

const Dashboard: React.FC = () => {
  const [clients, setClients] = useState<Client[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    clientApi.getAll()
      .then(response => {
        setClients(response.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Failed to load clients. Please ensure the backend is running.');
        setLoading(false);
        console.error(err);
      });
  }, []);

  if (loading) return <div className="text-center mt-4">Loading clients...</div>;
  if (error) return <div className="text-center mt-4 text-error">{error}</div>;

  return (
    <div className="dashboard">
      <header className="mb-4">
        <h1>Client Dashboard</h1>
        <p className="text-secondary">Select a client to manage their portfolio rebalancing.</p>
      </header>

      <div className="card">
        <table>
          <thead>
            <tr>
              <th>Client Name</th>
              <th>Client ID</th>
              <th className="text-right">Total Invested</th>
              <th className="text-center">Actions</th>
            </tr>
          </thead>
          <tbody>
            {clients.map(client => (
              <tr key={client.clientId}>
                <td><strong>{client.clientName}</strong></td>
                <td>{client.clientId}</td>
                <td className="text-right">
                  ${client.totalInvested?.toLocaleString(undefined, { minimumFractionDigits: 2 })}
                </td>
                <td className="text-center">
                  <div className="flex gap-2 justify-center">
                    <Link to={`/portfolio/${client.clientId}`} className="btn-primary" style={{ fontSize: '0.875rem' }}>
                      View Portfolio
                    </Link>
                    <Link to={`/history/${client.clientId}`} className="btn-outline" style={{ fontSize: '0.875rem' }}>
                      History
                    </Link>
                  </div>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Dashboard;
