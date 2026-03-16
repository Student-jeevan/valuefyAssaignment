import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { portfolioApi, clientApi } from '../api';

const Portfolio = () => {
  const { clientId } = useParams();
  const [portfolio, setPortfolio] = useState(null);
  const [client, setClient] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!clientId) return;

    Promise.all([
      clientApi.getById(clientId),
      portfolioApi.get(clientId)
    ])
    .then(([clientRes, portfolioRes]) => {
      setClient(clientRes.data);
      setPortfolio(portfolioRes.data);
      setLoading(false);
    })
    .catch(err => {
      setError('Failed to load portfolio data.');
      setLoading(false);
      console.error(err);
    });
  }, [clientId]);

  if (loading) return <div className="text-center mt-4">Loading portfolio...</div>;
  if (error || !portfolio || !client) return <div className="text-center mt-4 text-error">{error || 'Client not found'}</div>;

  return (
    <div className="portfolio-page">
      <div className="flex justify-between items-center mb-4">
        <div>
          <Link to="/" className="text-secondary" style={{ fontSize: '0.875rem' }}>&larr; Back to Dashboard</Link>
          <h1 className="mt-4">{client.clientName}'s Portfolio</h1>
        </div>
        <div className="text-right">
          <div className="text-secondary" style={{ fontSize: '0.875rem' }}>Total Portfolio Value</div>
          <div style={{ fontSize: '2rem', fontWeight: 'bold', color: 'var(--primary)' }}>
            ₹{portfolio.portfolioValue.toLocaleString(undefined, { minimumFractionDigits: 2 })}
          </div>
        </div>
      </div>

      <div className="card mb-4">
        <div className="flex justify-between items-center mb-4">
          <h2 style={{ margin: 0 }}>Current Holdings</h2>
          <Link to={`/rebalance/${clientId}`} className="btn-primary">
            Quick Rebalance
          </Link>
        </div>
        
        <table>
          <thead>
            <tr>
              <th>Fund Name</th>
              <th>Status</th>
              <th className="text-right">Current Value</th>
              <th className="text-right">Current %</th>
              <th className="text-right">Target %</th>
            </tr>
          </thead>
          <tbody>
            {portfolio.items.map(item => (
              <tr key={item.fundId}>
                <td>
                  <div style={{ fontWeight: '500' }}>{item.fundName}</div>
                  <div className="text-secondary" style={{ fontSize: '0.75rem' }}>{item.fundId}</div>
                </td>
                <td>
                  {item.isModelFund ? (
                    <span className="badge badge-info">Model Fund</span>
                  ) : (
                    <span className="badge badge-warning">Non-Model</span>
                  )}
                </td>
                <td className="text-right">₹{item.currentValue.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                <td className="text-right">{item.currentPct.toFixed(2)}%</td>
                <td className="text-right">
                  {item.targetPct !== null ? `${item.targetPct.toFixed(2)}%` : '--'}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Portfolio;
