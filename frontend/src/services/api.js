import axios from 'axios';
import { API_BASE } from '../utils/constants';

const api = axios.create({
  baseURL: API_BASE,
  timeout: 10000,
});

export const searchJobs = async (params) => {
  const response = await api.get('/api/jobs/search', { params });
  return response.data;
};

export const getJob = async (id) => {
  const response = await api.get(`/api/jobs/${id}`);
  return response.data;
};

export const getJobs = async (page = 1, limit = 20) => {
  const response = await api.get('/api/jobs', { params: { page, limit } });
  return response.data;
};

export const saveJob = async (userId, jobId) => {
  const response = await api.post(`/api/jobs/${userId}/save?jobId=${jobId}`);
  return response.data;
};

export const unsaveJob = async (userId, jobId) => {
  const response = await api.delete(`/api/jobs/${userId}/unsave/${jobId}`);
  return response.data;
};

export const getSavedJobs = async (userId) => {
  const response = await api.get(`/api/jobs/user/${userId}/saved`);
  return response.data;
};

export const getUser = async (userId) => {
  const response = await api.get(`/api/users/${userId}`);
  return response.data;
};

export const createUser = async (userData) => {
  const response = await api.post('/api/users', userData);
  return response.data;
};

export const login = async (email, password) => {
  const response = await api.post('/api/auth/login', { email, password });
  return response.data;
};

export const getRecommendations = async (userId) => {
  const response = await api.get(`/api/recommendations?user_id=${userId}`);
  return response.data;
};

export default api;
