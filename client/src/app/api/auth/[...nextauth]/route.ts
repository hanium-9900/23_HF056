import NextAuth, { NextAuthOptions, User } from 'next-auth';
import CredentialsProvider from 'next-auth/providers/credentials';
import axios from 'axios';
import { api } from '@/api';

export const authOptions: NextAuthOptions = {
  // Configure one or more authentication providers
  providers: [
    CredentialsProvider({
      // The name to display on the sign in form (e.g. 'Sign in with...')
      name: 'Credentials',
      // The credentials is used to generate a suitable form on the sign in page.
      // You can specify whatever fields you are expecting to be submitted.
      // e.g. domain, username, password, 2FA token, etc.
      // You can pass any HTML attribute to the <input> tag through the object.
      credentials: {
        email: { label: 'Email', type: 'email', placeholder: 'jsmith' },
        password: { label: 'Password', type: 'password' },
      },
      async authorize(credentials, req) {
        try {
          const { data: token } = await api.auth.login({
            email: credentials?.email!,
            password: credentials?.password!,
          });

          const { data: user } = await api.auth.me(token);

          return { ...user, token: token };
        } catch (e) {
          if (axios.isAxiosError(e)) {
            if (e.response?.data.message) {
              throw new Error(e.response?.data.message);
            }
          }
          throw new Error('Unknown error');
        }
      },
    }),
  ],
  pages: {
    signIn: '/auth/login',
  },
  callbacks: {
    async jwt({ token, user, account }) {
      if (account && user) {
        return {
          ...token,
          token: user.token,
        };
      }

      return token;
    },

    async session({ session, token }) {
      session.user.token = token.token;

      return session;
    },
  },
};

const handler = NextAuth(authOptions);

export { handler as GET, handler as POST };
