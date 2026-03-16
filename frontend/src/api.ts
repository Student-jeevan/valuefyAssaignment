import axios from 'axios';
import { 
  Client, 
  ModelFund, 
  PortfolioResponse, 
  RebalanceResponse, 
  RebalanceSession 
} from './types';

const api = axios.create({
  baseURL: 'http://localhost:8080',
});

export const clientApi = {
  getAll: () => api.get<Client[]>('/clients'),
  getById: (id: string) => api.get<Client>(`/clients/${id}`),
};

export const portfolioApi = {
  get: (clientId: string) => api.get<PortfolioResponse>(`/portfolio/${clientId}`),
};

export const rebalanceApi = {
  getPreview: (clientId: string) => api.get<RebalanceResponse>(`/rebalance/${clientId}`),
  save: (clientId: string) => api.post<RebalanceSession>('/rebalance/save', { clientId }),
  getHistory: (clientId: string) => api.get<RebalanceSession[]>(`/rebalance/history/${clientId}`),
};

export const modelPortfolioApi = {
  get: () => api.get<ModelFund[]>('/model-portfolio'),
  update: (data: ModelFund[]) => api.put<ModelFund[]>('/model-portfolio', data),
};
