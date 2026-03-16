import React, { useEffect, useState } from 'react';
import { modelPortfolioApi } from '../api';

const ModelPortfolioEditor = () => {
  const [funds, setFunds] = useState([]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  useEffect(() => {
    modelPortfolioApi.get()
      .then(res => {
        setFunds(res.data);
        setLoading(false);
      })
      .catch(err => {
        setError('Failed to load model portfolio.');
        setLoading(false);
        console.error(err);
      });
  }, []);

  const handleAllocationChange = (fundId, value) => {
    const numValue = parseFloat(value) || 0;
    setFunds(prev => prev.map(f => f.fundId === fundId ? { ...f, allocationPct: numValue } : f));
    setSuccess(false);
    setError(null);
  };

  const totalAllocation = funds.reduce((sum, f) => sum + f.allocationPct, 0);

  const handleSave = () => {
    if (Math.abs(totalAllocation - 100) > 0.01) {
      setError(`Total allocation must sum to exactly 100%. Current sum: ${totalAllocation.toFixed(2)}%`);
      return;
    }

    setSaving(true);
    setError(null);
    setSuccess(false);

    modelPortfolioApi.update(funds)
      .then(() => {
        setSuccess(true);
        setSaving(false);
      })
      .catch(err => {
        const msg = err.response?.data?.message || 'Failed to update model portfolio.';
        setError(msg);
        setSaving(false);
      });
  };

  if (loading) return <div className="text-center mt-4">Loading model portfolio...</div>;
  if (error) return <div className="text-center mt-4 text-error">{error}</div>;

  return (
    <div className="model-editor-page">
      <header className="mb-4">
        <h1>Model Portfolio Editor</h1>
        <p className="text-secondary">Define the target asset allocation. The sum must equal exactly 100%.</p>
      </header>

      <div className="card">
        <div className="flex justify-between items-center mb-6">
          <h2 style={{ margin: 0 }}>Allocation Strategy</h2>
          <div className="flex gap-4 items-center">
            <div className={`badge ${Math.abs(totalAllocation - 100) < 0.01 ? 'badge-success' : 'badge-error'}`} style={{ padding: '0.6rem 1.2rem', fontSize: '1rem' }}>
              Total Allocation: {totalAllocation.toFixed(2)}%
            </div>
            <button 
              className="btn-primary" 
              onClick={handleSave} 
              disabled={saving || Math.abs(totalAllocation - 100) > 0.01}
              style={{ padding: '0.75rem 2rem', fontWeight: 'bold' }}
            >
              {saving ? 'Processing...' : 'Recalculate & Save'}
            </button>
          </div>
        </div>

        {error && (
          <div className="badge badge-error mb-4" style={{ width: '100%', padding: '1rem', textAlign: 'left', display: 'block' }}>
            <strong>Configuration Error:</strong> {error}
          </div>
        )}
        
        {success && (
          <div className="badge badge-success mb-4" style={{ width: '100%', padding: '1rem', textAlign: 'left', display: 'block' }}>
            <strong>Success:</strong> Model strategy updated. Rebalance recommendations have been recalculated for all clients.
          </div>
        )}

        <div className="table-container">
          <table>
            <thead>
              <tr>
                <th>Fund Name</th>
                <th>Asset Class</th>
                <th className="text-right" style={{ width: '250px' }}>Target Allocation (%)</th>
              </tr>
            </thead>
            <tbody>
              {funds.map(fund => (
                <tr key={fund.fundId}>
                  <td>
                    <div style={{ fontWeight: '600', color: 'var(--primary)' }}>{fund.fundName}</div>
                    <div className="text-secondary" style={{ fontSize: '0.8rem' }}>{fund.fundId}</div>
                  </td>
                  <td>
                    <span className="badge badge-info" style={{ fontSize: '0.7rem' }}>{fund.assetClass}</span>
                  </td>
                  <td className="text-right">
                    <div className="flex items-center justify-end gap-2">
                      <input 
                        type="number" 
                        step="0.01"
                        min="0"
                        max="100"
                        value={fund.allocationPct}
                        onChange={(e) => handleAllocationChange(fund.fundId, e.target.value)}
                        style={{
                          width: '120px',
                          padding: '0.6rem',
                          borderRadius: 'var(--radius-sm)',
                          border: '1px solid var(--border)',
                          textAlign: 'right',
                          fontWeight: '500',
                          fontSize: '1rem'
                        }}
                      />
                      <span className="text-secondary">%</span>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="mt-6 p-4 bg-light border-radius-sm" style={{ border: '1px dashed var(--border)' }}>
        <p className="text-secondary" style={{ fontSize: '0.875rem', margin: 0 }}>
          <span style={{ fontWeight: 'bold', color: 'var(--text-primary)' }}>Architect's Note:</span> Changes to model fund allocations will immediately affect drift calculations across the entire advisor dashboard. Ensure that the total allocation remains at 100.00% to maintain portfolio integrity.
        </p>
      </div>
    </div>
  );
};

export default ModelPortfolioEditor;
