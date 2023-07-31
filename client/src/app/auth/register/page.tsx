import { getServerSession } from 'next-auth';
import RegisterForm from './RegisterForm';
import { redirect } from 'next/navigation';

export default async function Register() {
  const session = await getServerSession();
  if (session && session.user) {
    redirect('/');
  }

  return (
    <main className="max-w-md mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">회원가입</h1>
      <RegisterForm></RegisterForm>
    </main>
  );
}
