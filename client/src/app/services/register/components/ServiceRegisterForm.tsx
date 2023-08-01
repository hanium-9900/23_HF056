'use client';

import { useEffect, useState } from 'react';
import { ApiInfo, ServiceInfo } from '../types';
import ApiSpecificationInput from './ApiSpecificationInput';
import axios from 'axios';
import { api } from '@/api';
import { useRouter } from 'next/navigation';

export default function ServiceRegisterForm() {
  const router = useRouter();

  const [info, setInfo] = useState<ServiceInfo>({
    title: '',
    description: '',
    price: -1,
    key: '',
  });
  const [apiList, setApiList] = useState<ApiInfo[]>([]);

  function updateInfo(data: Partial<ServiceInfo>) {
    setInfo({ ...info, ...data });
  }

  function addApi() {
    setApiList([
      ...apiList,
      {
        host: '',
        method: 'GET',
        path: '',
        description: '',
        headers: [],
        requestParameters: [],
        responseParameters: [],
        errorCodes: [],
      },
    ]);
  }

  function removeApi(idx: number) {
    const updatedApiList = [...apiList];
    updatedApiList.splice(idx, 1);

    setApiList(updatedApiList);
  }

  function updateApi(idx: number, api: Partial<ApiInfo>) {
    const updatedApiList = [...apiList];
    updatedApiList[idx] = { ...updatedApiList[idx], ...api };

    setApiList(updatedApiList);
  }

  const submitForm = async (event: { preventDefault: () => void }) => {
    event.preventDefault();

    // [TODO] 서버쪽 required 가 int라서 bool -> int 매핑 과정이 필요.
    // 추후 서버쪽 required 타입 변경 검토.
    const serviceData = {
      ...info,
      apis: apiList.map(api => ({
        ...api,
        headers: api.headers.map(header => ({
          ...header,
          required: 1,
        })),
        requestParameters: api.requestParameters.map(requestParameter => ({
          ...requestParameter,
          required: 1,
        })),
        responseParameters: api.responseParameters.map(responseParameter => ({
          ...responseParameter,
          required: 1,
        })),
      })),
    };

    if (serviceData.title.trim() === '' || serviceData.price === -1 || serviceData.key.trim() === '') {
      alert('서비스 기본 정보(제목, 가격, Key)를 모두 입력해주세요!');
      return;
    }
    for (const api of serviceData.apis) {
      if (api.host.trim() === '' || api.path.trim() === '') {
        alert('API 기본 정보(Host, Path)를 모두 입력해주세요!');
        return;
      }
    }

    try {
      const { data } = await api.services.register(serviceData);

      alert(`서비스 등록이 완료되었습니다!`);

      router.replace(`/services/${data.id}`);
    } catch (e) {
      if (axios.isAxiosError(e)) {
        if (e.response?.status === 400) {
          alert('명세와 실제 API가 일치하지 않습니다!');
        } else if (e.response?.status === 500) {
          alert('서버 오류가 발생했습니다!\n(status: 500)');
        } else {
          alert('알 수 없는 Axios 오류가 발생했습니다!');
        }
      } else {
        alert('알 수 없는 오류가 발생했습니다!');
      }
    }
  };

  useEffect(() => {
    addApi();
  }, []);

  return (
    <form className="rounded border border-slate-300 p-7" onSubmit={e => e.preventDefault()}>
      {/* 서비스 명세 */}
      <label className="block mb-6">
        <div className="font-bold mb-2">서비스 이름</div>
        <input type="text" placeholder="서비스 이름을 입력하세요" onChange={e => updateInfo({ title: e.target.value })} required />
      </label>
      <label className="block mb-6">
        <div className="font-bold mb-2">서비스 설명</div>
        <textarea rows={5} placeholder="서비스 설명을 입력하세요" onChange={e => updateInfo({ description: e.target.value })} required></textarea>
      </label>
      <label className="block mb-6">
        <div className="font-bold mb-2">서비스 가격</div>
        <div className="flex items-center">
          <input type="number" placeholder="30000" onChange={e => updateInfo({ price: Number.parseInt(e.target.value) })} required />
          <span className="ml-3">원</span>
        </div>
      </label>
      <label className="block mb-6">
        <div className="font-bold mb-2">API 키</div>
        <input type="password" onChange={e => updateInfo({ key: e.target.value })} required />
        <div className="text-xs text-blue-500 mt-1">API 키는 요청 시 X-API-KEY 헤더에 담아 보내집니다.</div>
      </label>
      {/* API 명세 */}
      <div className="block mb-6">
        <div className="font-bold mb-12">API 명세</div>

        <hr className="mb-12" />

        {/* API 목록 */}
        {apiList.map((api, idx) => (
          <ApiSpecificationInput key={idx} no={idx} api={api} removeApi={removeApi} onChange={updatedApi => updateApi(idx, updatedApi)} />
        ))}
      </div>

      <div className="flex justify-end mb-12">
        <button className="btn btn-form" onClick={addApi}>
          API 추가
        </button>
      </div>

      <hr className="mb-12" />

      <div className="flex justify-end">
        <button className="btn btn-form" onClick={submitForm}>
          서비스 등록
        </button>
      </div>
    </form>
  );
}
