import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

export const clientApi = {
  getAll: () => api.get('/clients'),
  getById: (id) => api.get(`/clients/${id}`),
};

export const portfolioApi = {
  get: (clientId) => api.get(`/portfolio/${clientId}`),
};

export const rebalanceApi = {
  getPreview: (clientId) => api.get(`/rebalance/${clientId}`),
  save: (clientId) => api.post('/rebalance/save', { clientId }),
  getHistory: (clientId) => api.get(`/rebalance/history/${clientId}`),
};

export const modelPortfolioApi = {
  get: () => api.get('/model-portfolio'),
  update: (data) => api.put('/model-portfolio', data),
};
