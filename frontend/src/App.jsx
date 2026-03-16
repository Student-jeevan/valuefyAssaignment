import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Layout from './components/Layout';
import Dashboard from './pages/Dashboard';
import Portfolio from './pages/Portfolio';
import Rebalance from './pages/Rebalance';
import History from './pages/History';
import ModelPortfolioEditor from './pages/ModelPortfolioEditor';
import './App.css';

function App() {
  return (
    <Router>
      <Layout>
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/portfolio/:clientId" element={<Portfolio />} />
          <Route path="/rebalance/:clientId" element={<Rebalance />} />
          <Route path="/history/:clientId" element={<History />} />
          <Route path="/model-portfolio" element={<ModelPortfolioEditor />} />
        </Routes>
      </Layout>
    </Router>
  );
}

export default App;
