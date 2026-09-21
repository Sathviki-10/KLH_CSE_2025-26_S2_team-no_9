import React, { useState } from 'react';
import './TextAnalytics.css';

const TextAnalytics = () => {
  const [activeTab, setActiveTab] = useState('pattern');
  const [text, setText] = useState('The quick brown fox jumps over the lazy dog. The fox is clever.');
  const [pattern, setPattern] = useState('fox');
  const [maxDistance, setMaxDistance] = useState(2);
  const [docA, setDocA] = useState('The quick brown fox jumps over the lazy dog.');
  const [docB, setDocB] = useState('A fast brown fox leaps over a sleepy dog.');
  const [patterns, setPatterns] = useState('fox,dog,quick');
  const [number, setNumber] = useState('9973');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);

  const callApi = async (endpoint, body) => {
    setLoading(true);
    setResult(null);
    try {
      const res = await fetch(`http://localhost:8080/api/text${endpoint}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body)
      });
      const data = await res.json();
      setResult(data);
    } catch (e) {
      setResult({ error: e.message });
    } finally {
      setLoading(false);
    }
  };

  const runPatternSearch = () => callApi('/pattern-search', { text, pattern });
  const runFuzzyMatch = () => callApi('/fuzzy-match', { text, pattern, maxDistance });
  const runSimilarity = () => callApi('/similarity', { docA, docB });
  const runSuffix = () => callApi('/suffix-analysis', { text });
  const runMultiPattern = () => {
    const list = patterns.split(',').map(s => s.trim()).filter(Boolean);
    callApi('/multi-pattern', { text, patterns: list });
  };
  const runPrimality = () => callApi('/primality', { number: parseInt(number) || 0 });

  const renderResult = () => {
    if (loading) return <div className="tah-loading">Running algorithm...</div>;
    if (!result) return <div className="tah-placeholder">Run an algorithm to see results here.</div>;
    if (result.error) return <div className="tah-error">{result.error}</div>;
    return (
      <div className="tah-result">
        <h3>Result</h3>
        <pre>{JSON.stringify(result, null, 2)}</pre>
      </div>
    );
  };

  return (
    <div className="text-analytics">
      <div className="tah-header">
        <h1>TextHack Engine</h1>
        <p>Advanced text algorithms: KMP, Z-function, Rabin-Karp, Aho-Corasick, Suffix Arrays, LCP, Suffix Automaton, Edit Distance, Primality Testing</p>
      </div>

      <div className="tah-tabs">
        {['pattern', 'fuzzy', 'similarity', 'suffix', 'multipattern', 'primality'].map(tab => (
          <button key={tab} className={`tah-tab ${activeTab === tab ? 'active' : ''}`} onClick={() => setActiveTab(tab)}>
            {tab === 'pattern' && 'Pattern Search'}
            {tab === 'fuzzy' && 'Fuzzy Match'}
            {tab === 'similarity' && 'Similarity'}
            {tab === 'suffix' && 'Suffix Analysis'}
            {tab === 'multipattern' && 'Multi-Pattern'}
            {tab === 'primality' && 'Primality'}
          </button>
        ))}
      </div>

      <div className="tah-panel">
        {activeTab === 'pattern' && (
          <div className="tah-form">
            <label>Text</label>
            <textarea value={text} onChange={e => setText(e.target.value)} rows={4} />
            <label>Pattern</label>
            <input value={pattern} onChange={e => setPattern(e.target.value)} />
            <button onClick={runPatternSearch}>Run KMP / Z / Rabin-Karp</button>
          </div>
        )}
        {activeTab === 'fuzzy' && (
          <div className="tah-form">
            <label>Text</label>
            <textarea value={text} onChange={e => setText(e.target.value)} rows={4} />
            <label>Pattern</label>
            <input value={pattern} onChange={e => setPattern(e.target.value)} />
            <label>Max Edit Distance</label>
            <input type="number" value={maxDistance} onChange={e => setMaxDistance(parseInt(e.target.value) || 0)} />
            <button onClick={runFuzzyMatch}>Run Fuzzy Match</button>
          </div>
        )}
        {activeTab === 'similarity' && (
          <div className="tah-form">
            <label>Document A</label>
            <textarea value={docA} onChange={e => setDocA(e.target.value)} rows={3} />
            <label>Document B</label>
            <textarea value={docB} onChange={e => setDocB(e.target.value)} rows={3} />
            <button onClick={runSimilarity}>Run Similarity</button>
          </div>
        )}
        {activeTab === 'suffix' && (
          <div className="tah-form">
            <label>Text</label>
            <textarea value={text} onChange={e => setText(e.target.value)} rows={4} />
            <button onClick={runSuffix}>Run Suffix Analysis</button>
          </div>
        )}
        {activeTab === 'multipattern' && (
          <div className="tah-form">
            <label>Text</label>
            <textarea value={text} onChange={e => setText(e.target.value)} rows={4} />
            <label>Patterns (comma separated)</label>
            <input value={patterns} onChange={e => setPatterns(e.target.value)} />
            <button onClick={runMultiPattern}>Run Aho-Corasick</button>
          </div>
        )}
        {activeTab === 'primality' && (
          <div className="tah-form">
            <label>Number</label>
            <input type="number" value={number} onChange={e => setNumber(e.target.value)} />
            <button onClick={runPrimality}>Test Primality</button>
          </div>
        )}
      </div>

      <div className="tah-output">
        {renderResult()}
      </div>
    </div>
  );
};

export default TextAnalytics;
