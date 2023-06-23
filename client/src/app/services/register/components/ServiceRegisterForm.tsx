'use client';

import { useEffect, useState } from 'react';
import { ApiInfo, ServiceInfo } from '../types';
import ApiSpecificationInput from './ApiSpecificationInput';
import axios from 'axios';

export default function ServiceRegisterForm() {
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

    console.log(serviceData);

    try {
      const { data } = await axios.post('http://localhost:8080/services', serviceData, {
        headers: {
          'X-AUTH-TOKEN': localStorage.getItem('temp_token')!,
        },
      });

      alert(`전송 완료!\n${data}`);
    } catch (e) {
      console.error(e);
      alert(e);
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
        <input type="text" placeholder="서비스 이름을 입력하세요" onChange={e => updateInfo({ title: e.target.value })} />
      </label>
      <label className="block mb-6">
        <div className="font-bold mb-2">서비스 설명</div>
        <textarea rows={5} placeholder="서비스 설명을 입력하세요" onChange={e => updateInfo({ description: e.target.value })}></textarea>
      </label>
      <label className="block mb-6">
        <div className="font-bold mb-2">서비스 가격</div>
        <div className="flex items-center">
          <input type="number" placeholder="30000" onChange={e => updateInfo({ price: Number.parseInt(e.target.value) })} />
          <span className="ml-3">원</span>
        </div>
      </label>
      <label className="block mb-6">
        <div className="font-bold mb-2">API 키</div>
        <input type="password" onChange={e => updateInfo({ key: e.target.value })} />
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
