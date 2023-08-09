'use client';

import { useEffect, useState } from 'react';
import './page.css';
import ApiSpecification from './components/ApiSpecification';
import ApiPurchaseButton from './components/ApiPurchaseButton';
import Link from 'next/link';
import { useRouter } from 'next/navigation';

import { api, ServiceResponse } from '@/api';
import { toast } from 'react-toastify';

export default function ServiceInfoPage({ params }: { params: { serviceId: string } }) {
  const router = useRouter();

  const [service, setService] = useState<ServiceResponse | null>(null);
  const [selectedApi, setSelectedApi] = useState(0);

  useEffect(() => {
    api.services.show(Number(params.serviceId))
      .then(response => {
        setService(response.data);
      })
      .catch(error => {
        console.error(error);
      });
  }, [params.serviceId]);

  if (!service) {
    return <div>Loading...</div>;
  }

  async function deleteService() {
    if (confirm('정말 삭제하시겠습니까?')) { // [TODO] 컨펌 Modal로 대체
      const id = Number.parseInt(params.serviceId);
      if (Number.isNaN(id)) return;

      try {
        await api.services.delete(id);

        toast.success(`'${service?.title}' 서비스가 삭제되었습니다.`)
        router.replace('/services');
        router.refresh();
      } catch (e) {
        toast.success(`서비스 삭제 중 알 수 없는 오류가 발생했습니다!`)

        console.error(e);
      }
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
            <span className="text-blue-500">{service.price} &#8361;</span>
          </div>
        </div>
        <div className="flex items-center justify-between">
          <span className="text-gray-500 break-keep mr-6">{service.description}</span>
          <ApiPurchaseButton service={service} />
        </div>
      </div>
      {/* 서비스 관리 버튼 */}
      <div className="flex items-center gap-3 mb-16">
        <Link href={`/services/${params.serviceId}/edit`} className="py-2 px-7 rounded-full border-2 border-blue-500 text-blue-500 font-bold">
          수정
        </Link>
        <button onClick={() => deleteService()} className="py-2 px-7 rounded-full border-2 border-red-500 text-red-500 font-bold">
          삭제
        </button>
      </div>
      {/* API 선택 */}
      <div className="flex items-center mb-6">
        <span className="font-bold mr-3">API 구분</span>
        <select onChange={e => setSelectedApi(Number.parseInt(e.target.value))}>
          {service.apis.map((api, idx) => (
            <option key={idx} value={idx}>
              {api.path}
            </option>
          ))}
        </select>
      </div>
      <ApiSpecification api={service.apis[selectedApi]}></ApiSpecification>
    </main>
  );
}
