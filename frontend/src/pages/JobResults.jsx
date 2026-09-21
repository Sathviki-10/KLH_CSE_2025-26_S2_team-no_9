import React, { useState, useEffect } from 'react';
import { useSearchParams } from 'react-router-dom';
import { searchJobs } from '../services/api';
import { LOCATIONS, JOB_TYPES } from '../utils/constants';
import JobCard from '../components/JobCard';
import './JobResults.css';

const JobResults = ({ user }) => {
  const [searchParams] = useSearchParams();
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [total, setTotal] = useState(0);
  const [sort, setSort] = useState('relevance');
  const [showFilters, setShowFilters] = useState(false);
  const [data, setData] = useState(null);
  const limit = 12;

  const [filters, setFilters] = useState({
    skills: '',
    experience: '',
    job_type: '',
    salary: '',
    company: ''
  });

  useEffect(() => {
    fetchJobs();
  }, [searchParams, page, sort, filters]);

  const fetchJobs = async () => {
    try {
      setLoading(true);
      const params = {
        q: searchParams.get('q') || '',
        location: searchParams.get('location') || '',
        skills: filters.skills,
        experience: filters.experience,
        job_type: filters.job_type,
        salary: filters.salary,
        company: filters.company,
        sort,
        page,
        limit
      };
      const result = await searchJobs(params);
      setData(result);
      setJobs(result.jobs || []);
      setTotal(result.total || 0);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleFilterChange = (key, value) => {
    setFilters(prev => ({ ...prev, [key]: value }));
    setPage(1);
  };

  const clearFilters = () => {
    setFilters({
      skills: '',
      experience: '',
      job_type: '',
      salary: '',
      company: ''
    });
    setPage(1);
  };

  const totalPages = Math.ceil(total / limit);

  return (
    <div className="job-results">
      <div className="job-results-header">
        <div>
          <h1>
            {searchParams.get('q') ? `Results for "${searchParams.get('q')}"` : 'Browse Jobs'}
            {searchParams.get('location') && ` in ${searchParams.get('location')}`}
          </h1>
          <p>{total} jobs found</p>
        </div>
        <div className="job-results-controls">
          <select value={sort} onChange={(e) => { setSort(e.target.value); setPage(1); }}>
            <option value="relevance">Sort by Relevance</option>
            <option value="salary">Sort by Salary</option>
            <option value="experience">Sort by Experience</option>
          </select>
          <button className="btn-filters" onClick={() => setShowFilters(!showFilters)}>
            Filters
          </button>
        </div>
      </div>

      {showFilters && (
        <div className="filters-panel">
          <div className="filter-group">
            <label>Skills</label>
            <input
              type="text"
              placeholder="e.g. Python, React"
              value={filters.skills}
              onChange={(e) => handleFilterChange('skills', e.target.value)}
            />
          </div>
          <div className="filter-group">
            <label>Experience</label>
            <input
              type="text"
              placeholder="e.g. 3-5 years"
              value={filters.experience}
              onChange={(e) => handleFilterChange('experience', e.target.value)}
            />
          </div>
          <div className="filter-group">
            <label>Job Type</label>
            <select value={filters.job_type} onChange={(e) => handleFilterChange('job_type', e.target.value)}>
              <option value="">All</option>
              {JOB_TYPES.map(t => <option key={t} value={t}>{t}</option>)}
            </select>
          </div>
          <div className="filter-group">
            <label>Salary Range</label>
            <input
              type="text"
              placeholder="e.g. 15-25"
              value={filters.salary}
              onChange={(e) => handleFilterChange('salary', e.target.value)}
            />
          </div>
          <div className="filter-group">
            <label>Company</label>
            <input
              type="text"
              placeholder="Company name"
              value={filters.company}
              onChange={(e) => handleFilterChange('company', e.target.value)}
            />
          </div>
          <button className="btn-clear-filters" onClick={clearFilters}>
            Clear Filters
          </button>
        </div>
      )}

      {loading ? (
        <div className="loading-state">
          <div className="spinner"></div>
          <p>Searching jobs...</p>
        </div>
      ) : (
        <>
          {data?.is_fallback && (
            <div className="fallback-notice">
              <p>{data?.message || 'Showing related jobs based on your search.'}</p>
            </div>
          )}
          <div className="job-grid">
            {jobs.map(job => (
              <JobCard key={job.id} job={job} />
            ))}
          </div>
          {totalPages > 1 && (
            <div className="pagination">
              <button
                disabled={page === 1}
                onClick={() => setPage(p => p - 1)}
              >
                Previous
              </button>
              <span>Page {page} of {totalPages}</span>
              <button
                disabled={page === totalPages}
                onClick={() => setPage(p => p + 1)}
              >
                Next
              </button>
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default JobResults;
