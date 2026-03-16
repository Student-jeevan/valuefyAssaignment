export interface Client {
  clientId: string;
  clientName: string;
  totalInvested: number;
}

export interface ModelFund {
  fundId: string;
  fundName: string;
  assetClass: string;
  allocationPct: number;
}

export interface PortfolioItem {
  fundId: string;
  fundName: string;
  currentValue: number;
  currentPct: number;
  targetPct: number | null;
  isModelFund: boolean;
}

export interface PortfolioResponse {
  clientId: string;
  portfolioValue: number;
  items: PortfolioItem[];
}

export interface RebalanceRecommendation {
  fundId: string;
  fundName: string;
  currentPct: number;
  targetPct: number;
  drift: number;
  action: string;
  amount: number;
  postRebalancePct: number;
  isModelFund: boolean;
}

export interface RebalanceResponse {
  clientId: string;
  portfolioValue: number;
  recommendations: RebalanceRecommendation[];
  totalToBuy: number;
  totalToSell: number;
  netCashNeeded: number;
}

export interface RebalanceSession {
  sessionId: number;
  clientId: string;
  createdAt: string;
  portfolioValue: number;
  totalToBuy: number;
  totalToSell: number;
  netCashNeeded: number;
  status: string;
}
