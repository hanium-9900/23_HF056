'use client';

import { api, ServiceResponse } from '@/api';
import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

export default function ServiceDeleteButton({ serviceId }: { serviceId: number }) {
  const router = useRouter();

  async function deleteService() {
    if (confirm('정말 삭제하시겠습니까?')) { // [TODO] 컨펌 Modal로 대체
      if (Number.isNaN(serviceId)) return;

      try {
        await api.services.delete(serviceId);

        toast.success(`서비스가 삭제되었습니다.`)
        router.replace('/services');
        router.refresh();
      } catch (e) {
        toast.success(`서비스 삭제 중 알 수 없는 오류가 발생했습니다!`)

        console.error(e);
      }
    }
  }

  return (
    <button onClick={() => deleteService()} className="py-2 px-7 rounded-full border-2 border-red-500 text-red-500 font-bold">
      삭제
    </button>
  )
}
