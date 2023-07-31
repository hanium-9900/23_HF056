import axios from './axios';

export const api = {
  auth: {
    register(credentials: { email: string; password: string }) {
      return axios.post('/users/join', credentials);
    },
    login(credentials: { email: string; password: string }) {
      return axios.post<string>('/users/login', credentials);
    },
    me(token?: string) {
      if (token) {
        return axios.get<{ id: string; email: string }>('/users/me', {
          headers: {
            'X-AUTH-TOKEN': token,
          },
        });
      } else {
        return axios.get<{ id: string; email: string }>('/users/me');
      }
    },
  },
  services: {
    register(service: any) {
      return axios.post('/services', service);
    },
  },
};
