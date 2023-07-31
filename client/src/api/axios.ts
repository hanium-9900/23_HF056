import axios from 'axios';
import { getSession } from 'next-auth/react';

const instance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_BACKEND_BASEURL,
});

instance.interceptors.request.use(async request => {
  const session = await getSession();

  console.log('calling?', session);

  if (session) {
    request.headers['X-AUTH-TOKEN'] = session.user.token;
    console.log('in axios interceptor: ', request.headers);
  }

  return request;
});

export default instance;
