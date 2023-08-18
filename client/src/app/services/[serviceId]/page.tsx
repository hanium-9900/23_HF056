import './page.css';
import ApiPurchaseButton from './components/ApiPurchaseButton';
import Link from 'next/link';

import { api } from '@/api';
import ServiceDeleteButton from './components/ServiceDeleteButton';
import ApiSpecificationSelector from './components/ApiSpecificationSelector';
import { getServerSession } from 'next-auth';

export default async function ServiceInfoPage({ params }: { params: { serviceId: string } }) {
  const session = await getServerSession();

  const serviceId = Number(params.serviceId);
  const { data: service } = await api.services.show(serviceId);

  const isMine = session?.user.email === service.user.email;
  let serviceKey: string | null = null;
  if (session && !isMine) {
    try {
      const { data } = await api.services.getServiceKey(serviceId);
      serviceKey = data;
    } catch (e) {
      console.log((e as any).response.data)
    }
  }

  return (
    <main className="container xl:max-w-5xl mx-auto py-10 px-3">
      {/* 서비스 정보 */}
      <div className="mb-8">
        <div className="mb-8">
          <div className="text-blue-500 mb-1">{service.category}</div>
          <div className="flex items-center justify-between text-3xl font-bold">
            <span>{service.title}</span>
            <span className="text-blue-500">{service.price.toLocaleString()} &#8361;</span>
          </div>
        </div>
        <div className="flex items-center justify-between">
          <span className="text-gray-500 break-keep mr-6">{service.description}</span>
          {session && (
            (!isMine && !serviceKey) ? (
              <ApiPurchaseButton service={service} />
            ) : (
              <button
                className="btn btn-form shrink-0"
                disabled={true}
              >
                구매함
              </button>
            )
          )}
        </div>
      </div>
      {/* 서비스 관리 버튼 */}
      {isMine && (
        <div className="flex items-center gap-3 mb-16">
          <Link href={`/services/${params.serviceId}/edit`} className="py-2 px-7 rounded-full border-2 border-blue-500 text-blue-500 font-bold">
            수정
          </Link>
          <ServiceDeleteButton serviceId={serviceId} />
        </div>
      )}
      {/* 서비스 키 */}
      {serviceKey && (
        <div className='flex items-center mb-16'>
          <span className="font-bold mr-3 shrink-0">서비스 키</span>
          <input type='text' value={serviceKey} readOnly={true} />
        </div>
      )}
      <ApiSpecificationSelector service={service} />
    </main>
  );
}
