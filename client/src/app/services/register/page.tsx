import './page.css';
import ServiceRegisterForm from './components/ServiceRegisterForm';
import { getServerSession } from 'next-auth';
import { authOptions } from '@/app/api/auth/[...nextauth]/route';
import { redirect } from 'next/navigation';

export default async function Register() {
  const session = await getServerSession(authOptions);
  if (!session) {
    redirect('/auth/login');
  }

  return (
    <main className="max-w-3xl mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">서비스 등록</h1>
      <ServiceRegisterForm />
    </main>
  );
}
