import React, { useEffect, useState } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { rebalanceApi } from '../api';

const Rebalance = () => {
  const { clientId } = useParams();
  const navigate = useNavigate();
  const [rebalance, setRebalance] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!clientId) return;
    rebalanceApi.getPreview(clientId)
      .then(res => {
        setRebalance(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Failed to calculate rebalance.');
        setLoading(false);
        console.error(err);
      });
  }, [clientId]);

  const handleSave = () => {
    if (!clientId) return;
    setSaving(true);
    rebalanceApi.save(clientId)
      .then(() => {
        alert('Rebalance session saved successfully!');
        navigate(`/history/${clientId}`);
      })
      .catch(err => {
        alert('Failed to save rebalance session.');
        setSaving(false);
        console.error(err);
      });
  };

  if (loading) return <div className="text-center mt-4">Calculating rebalance recommendations...</div>;
  if (error || !rebalance) return <div className="text-center mt-4 text-error">{error || 'Data not found'}</div>;

  return (
    <div className="rebalance-page">
      <div className="mb-4">
        <Link to={`/portfolio/${clientId}`} className="text-secondary" style={{ fontSize: '0.875rem' }}>&larr; Back to Portfolio</Link>
        <h1 className="mt-4">Rebalance Recommendations</h1>
      </div>

      <div className="flex gap-4 mb-4" style={{ flexWrap: 'wrap' }}>
        <div className="card" style={{ flex: 1, minWidth: '250px', borderLeft: '4px solid var(--info)' }}>
          <div className="text-secondary" style={{ fontSize: '0.875rem' }}>Total amount to BUY</div>
          <div className="text-success" style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>
            ₹{rebalance.totalToBuy.toLocaleString(undefined, { minimumFractionDigits: 2 })}
          </div>
        </div>
        <div className="card" style={{ flex: 1, minWidth: '250px', borderLeft: '4px solid var(--error)' }}>
          <div className="text-secondary" style={{ fontSize: '0.875rem' }}>Total amount to SELL</div>
          <div className="text-error" style={{ fontSize: '1.5rem', fontWeight: 'bold' }}>
            ₹{rebalance.totalToSell.toLocaleString(undefined, { minimumFractionDigits: 2 })}
          </div>
        </div>
        <div className="card" style={{ flex: 1, minWidth: '250px', borderLeft: '4px solid var(--primary)' }}>
          <div className="text-secondary" style={{ fontSize: '0.875rem' }}>Net cash required</div>
          <div style={{ fontSize: '1.5rem', fontWeight: 'bold', color: 'var(--text-primary)' }}>
            ₹{rebalance.netCashNeeded.toLocaleString(undefined, { minimumFractionDigits: 2 })}
          </div>
        </div>
      </div>

      <div className="card mb-4">
        <div className="flex justify-between items-center mb-4">
          <h2 style={{ margin: 0 }}>Adjustment Plan</h2>
          <button 
            className="btn-primary" 
            onClick={handleSave}
            disabled={saving}
          >
            {saving ? 'Saving...' : 'Execute & Save Session'}
          </button>
        </div>

        <table>
          <thead>
            <tr>
              <th>Fund Name</th>
              <th className="text-center">Action</th>
              <th className="text-right">Amount</th>
              <th className="text-right">Current %</th>
              <th className="text-right">Target %</th>
              <th className="text-right">Drift</th>
            </tr>
          </thead>
          <tbody>
            {rebalance.recommendations.map(rec => (
              <tr key={rec.fundId}>
                <td>
                  <div style={{ fontWeight: '500' }}>{rec.fundName}</div>
                  <div className="text-secondary" style={{ fontSize: '0.75rem' }}>{rec.fundId}</div>
                </td>
                <td className="text-center">
                  <span className={`badge badge-${
                    rec.action === 'BUY' ? 'success' : 
                    rec.action === 'SELL' ? 'error' : 
                    rec.action === 'REVIEW' ? 'warning' : 'info'
                  }`}>
                    {rec.action}
                  </span>
                </td>
                <td className="text-right">
                  {rec.amount > 0 ? `₹${rec.amount.toLocaleString(undefined, { minimumFractionDigits: 2 })}` : '--'}
                </td>
                <td className="text-right">{rec.currentPct.toFixed(2)}%</td>
                <td className="text-right">{rec.targetPct.toFixed(2)}%</td>
                <td className={`text-right ${rec.drift > 0 ? 'text-success' : rec.drift < 0 ? 'text-error' : ''}`}>
                  {rec.drift > 0 ? '+' : ''}{rec.drift.toFixed(2)}%
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Rebalance;
