import axios from 'axios';
import { Session, getServerSession } from 'next-auth';
import { getSession } from 'next-auth/react';
import { authOptions } from '@/app/api/auth/[...nextauth]/authOptions';

const instance = axios.create({
  baseURL: process.env.NEXT_PUBLIC_BACKEND_BASEURL,
});

instance.interceptors.request.use(async request => {
  let session: Session | null = null;
  if (typeof window === 'undefined') {
    session = await getServerSession(authOptions);
  } else {
    session = await getSession();
  }

  if (session) {
    request.headers['X-AUTH-TOKEN'] = session.user.token;
  }

  return request;
});

export default instance;
