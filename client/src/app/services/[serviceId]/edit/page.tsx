import { api } from '@/api';
import ServiceRegisterForm from '@/app/services/register/components/ServiceRegisterForm';
import { getServerSession } from 'next-auth';
import Link from 'next/link';
import { redirect } from 'next/navigation';

export default async function Register({ params }: { params: { serviceId: string } }) {
  const serviceId = Number.parseInt(params.serviceId);
  if (Number.isNaN(serviceId)) {
    redirect('/');
  }

  const { data: service } = await api.services.show(Number.parseInt(params.serviceId));

  const session = await getServerSession();
  if (session?.user?.email !== service.user.email) {
    redirect(`/services/${params.serviceId}`);
  }

  return (
    <main className="max-w-3xl mx-auto py-14">
      <div className="text-center mb-3">
        <Link className="text-blue-500 text-lg font-bold text-center" href={`/services/${service.id}`}>
          {service.title}
        </Link>
      </div>
      <h1 className="text-3xl font-bold text-center mb-7">서비스 수정</h1>
      <ServiceRegisterForm service={service} />
    </main>
  );
}
