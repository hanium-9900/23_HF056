import { api } from '@/api';
// import './page.css';
import ServiceRegisterForm from '@/app/services/register/components/ServiceRegisterForm';

export default async function Register({ params }: { params: { serviceId: string } }) {
  const { data: service } = await api.services.show(Number.parseInt(params.serviceId));

  return (
    <main className="max-w-3xl mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">서비스 수정</h1>
      <ServiceRegisterForm service={service} />
    </main>
  );
}
