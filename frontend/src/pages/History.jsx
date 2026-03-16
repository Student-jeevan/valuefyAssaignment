import React, { useEffect, useState } from 'react';
import { useParams, Link } from 'react-router-dom';
import { rebalanceApi } from '../api';

const History = () => {
  const { clientId } = useParams();
  const [sessions, setSessions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!clientId) return;
    rebalanceApi.getHistory(clientId)
      .then(res => {
        setSessions(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Failed to load history.');
        setLoading(false);
        console.error(err);
      });
  }, [clientId]);

  if (loading) return <div className="text-center mt-4">Loading rebalance history...</div>;
  if (error) return <div className="text-center mt-4 text-error">{error}</div>;

  return (
    <div className="history-page">
      <div className="mb-4">
        <Link to={`/portfolio/${clientId}`} className="text-secondary" style={{ fontSize: '0.875rem' }}>&larr; Back to Portfolio</Link>
        <h1 className="mt-4">Rebalance History</h1>
      </div>

      <div className="card">
        {sessions.length === 0 ? (
          <div className="text-center py-4">No past sessions found for this client.</div>
        ) : (
          <table>
            <thead>
              <tr>
                <th>Date & Time</th>
                <th className="text-right">Portfolio Value</th>
                <th className="text-right">Total Buy</th>
                <th className="text-right">Total Sell</th>
                <th className="text-right">Net Cash</th>
                <th className="text-center">Status</th>
              </tr>
            </thead>
            <tbody>
              {sessions.map(session => (
                <tr key={session.sessionId}>
                  <td>
                    <div style={{ fontWeight: '500' }}>
                      {new Date(session.createdAt).toLocaleDateString()}
                    </div>
                    <div className="text-secondary" style={{ fontSize: '0.75rem' }}>
                      {new Date(session.createdAt).toLocaleTimeString()}
                    </div>
                  </td>
                  <td className="text-right">₹{session.portfolioValue.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                  <td className="text-right text-success">₹{session.totalToBuy.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                  <td className="text-right text-error">₹{session.totalToSell.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                  <td className="text-right">₹{session.netCashNeeded.toLocaleString(undefined, { minimumFractionDigits: 2 })}</td>
                  <td className="text-center">
                    <span className={`badge badge-${
                      session.status === 'APPLIED' ? 'success' : 
                      session.status === 'DISMISSED' ? 'error' : 'info'
                    }`}>
                      {session.status}
                    </span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        )}
      </div>
    </div>
  );
};

export default History;
