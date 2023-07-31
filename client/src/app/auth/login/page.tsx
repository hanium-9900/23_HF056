import { getServerSession } from 'next-auth';
import LoginForm from './LoginForm';
import { redirect } from 'next/navigation';

export default async function Login() {
  const session = await getServerSession();
  if (session && session.user) {
    redirect('/');
  }

  return (
    <main className="max-w-md mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">로그인</h1>
      <LoginForm></LoginForm>
    </main>
  );
}
