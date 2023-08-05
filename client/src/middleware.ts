export { default } from 'next-auth/middleware';

export const config = {
  matcher: ['/services/register', '/dashboard/:path*', '/services/:serviceId/edit'],
};
