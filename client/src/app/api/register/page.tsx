'use client';

import { useEffect, useState } from 'react';
import './page.css';
import { headers } from 'next/dist/client/components/headers';
import { error } from 'console';

interface ServiceInfo {
  name: string;
  description: string;
  price: number;
  key: string;
}

interface ApiInfo {
  id: number;
  host: string;
  method: string;
  path: string;
  description: string;
  headers: {
    key: string;
    description: string;
    required: boolean;
  }[];
  parameters: {
    key: string;
    description: string;
    required: boolean;
  }[];
  responseParameters: {
    key: string;
    description: string;
    required: boolean;
  }[];
  errorCodes: {
    key: string;
    description: string;
  }[];
}

let lastApiId = 0;

export default function Register() {
  const [serviceInfo, setServiceInfo] = useState<ServiceInfo>({
    name: '',
    description: '',
    price: -1,
    key: '',
  });
  const [apiList, setApiList] = useState<ApiInfo[]>([]);

  const submitForm = (event: { preventDefault: () => void }) => {
    event.preventDefault();

    const data = {
      ...serviceInfo,
      apis: apiList,
    };

    console.log(data);
  };

  const addHeader = (apiId: number) => {
    console.log('add header', apiId);

    //
    const keys = apiList.find(api => api.id === apiId)?.headers.map(header => header.key);
    if (!keys || keys.includes('')) {
      alert('먼저 헤더명을 모두 입력해주세요');
      return;
    }

    const updatedApiList = [...apiList].map(api => {
      if (api.id === apiId) {
        return {
          ...api,
          headers: [...api.headers, { key: '', description: '', required: false }],
        };
      } else {
        return api;
      }
    });

    setApiList(updatedApiList);
  };

  const addParameter = (apiId: number) => {
    console.log('add parameter', apiId);

    //
    const keys = apiList.find(api => api.id === apiId)?.parameters.map(parameter => parameter.key);
    if (!keys || keys.includes('')) {
      alert('먼저 파라미터명을 모두 입력해주세요');
      return;
    }

    const updatedApiList = [...apiList].map(api => {
      if (api.id === apiId) {
        return {
          ...api,
          parameters: [...api.parameters, { key: '', description: '', required: false }],
        };
      } else {
        return api;
      }
    });

    setApiList(updatedApiList);
  };

  const addResponseParameter = (apiId: number) => {
    console.log('add response parameter', apiId);

    //
    const keys = apiList.find(api => api.id === apiId)?.responseParameters.map(responseParameter => responseParameter.key);
    if (!keys || keys.includes('')) {
      alert('먼저 응답 파라미터명을 모두 입력해주세요');
      return;
    }

    const updatedApiList = [...apiList].map(api => {
      if (api.id === apiId) {
        return {
          ...api,
          responseParameters: [...api.responseParameters, { key: '', description: '', required: false }],
        };
      } else {
        return api;
      }
    });

    setApiList(updatedApiList);
  };

  const addErrorCode = (apiId: number) => {
    console.log('add error code', apiId);

    //
    const keys = apiList.find(api => api.id === apiId)?.errorCodes.map(errorCode => errorCode.key);
    if (!keys || keys.includes('')) {
      alert('먼저 에러 코드룰 모두 입력해주세요');
      return;
    }

    const updatedApiList = [...apiList].map(api => {
      if (api.id === apiId) {
        return {
          ...api,
          errorCodes: [...api.errorCodes, { key: '', description: '' }],
        };
      } else {
        return api;
      }
    });

    setApiList(updatedApiList);
  };

  const addApi = () => {
    console.log('add api');

    //
    setApiList([
      ...apiList,
      {
        id: ++lastApiId,
        host: '',
        method: 'GET',
        path: '',
        description: '',
        headers: [],
        parameters: [],
        responseParameters: [],
        errorCodes: [],
      },
    ]);
  };

  const updateApiInfo = (apiId: number, info: Partial<ApiInfo>) => {
    console.log('update api info', apiId, info);

    //
    const updatedApiList = [...apiList].map(api => {
      if (api.id === apiId) {
        return {
          ...api,
          ...info,
        };
      } else {
        return api;
      }
    });

    setApiList(updatedApiList);
  };

  useEffect(() => {
    addApi();
  }, []);

  return (
    <main className="max-w-3xl mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">서비스 등록</h1>
      <form className="rounded border border-slate-300 p-7" onSubmit={e => e.preventDefault()}>
        {/* 서비스 명세 */}
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 이름</div>
          <input type="text" placeholder="서비스 이름을 입력하세요" onChange={e => setServiceInfo({ ...serviceInfo, name: e.target.value })} />
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 설명</div>
          <textarea rows={5} placeholder="서비스 설명을 입력하세요" onChange={e => setServiceInfo({ ...serviceInfo, description: e.target.value })}></textarea>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 가격</div>
          <div className="flex items-center">
            <input type="number" placeholder="30000" onChange={e => setServiceInfo({ ...serviceInfo, price: Number.parseInt(e.target.value) })} />
            <span className="ml-3">원</span>
          </div>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">API 키</div>
          <input type="password" onChange={e => setServiceInfo({ ...serviceInfo, key: e.target.value })} />
        </label>
        {/* API 명세 */}
        <div className="block mb-6">
          <div className="font-bold mb-12">API 명세</div>

          <hr className="mb-12" />

          {/* API 목록 */}
          {apiList.map((api, idx) => (
            <div key={api.id} className="">
              {/* API Host */}
              <label className="flex items-center mb-6">
                <span className="rounded bg-blue-500 text-white shrink-0 py-2 px-4 mr-5">{idx + 1}번</span>
                <div className="flex items-center grow">
                  <div className="font-bold mr-3">Host</div>
                  <input type="text" placeholder="https://example.com" onChange={e => updateApiInfo(api.id, { host: e.target.value })} />
                </div>
              </label>
              {/* API Endpoint */}
              <div className="flex items-center mb-6">
                <select className="mr-3" onChange={e => updateApiInfo(api.id, { method: e.target.value })}>
                  <option value="GET">GET</option>
                  <option value="POST">POST</option>
                  <option value="PUT">PUT</option>
                  <option value="PATCH">PATCH</option>
                  <option value="DELETE">DELETE</option>
                </select>
                <input type="text" placeholder="/example/api/path" onChange={e => updateApiInfo(api.id, { path: e.target.value })} />
              </div>
              {/* Description */}
              <label className="block mb-6">
                <div className="font-bold mb-2">API 설명</div>
                <textarea rows={5} placeholder="API 설명을 입력하세요" onChange={e => updateApiInfo(api.id, { description: e.target.value })}></textarea>
              </label>
              {/* Headers */}
              <label className="block mb-6">
                <div className="font-bold mb-2">헤더</div>
                <div>
                  {api.headers.map((header, idx) => (
                    <div key={`idx-${header.key}`} className="flex mb-4">
                      <input className="mr-3" type="text" placeholder="헤더명" />
                      <input type="text" placeholder="설명" />
                    </div>
                  ))}
                  <div className="flex justify-end">
                    <button
                      className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white"
                      onClick={() => addHeader(api.id)}
                    >
                      헤더 추가 (미구현)
                    </button>
                  </div>
                </div>
              </label>
              {/* Request Parameters */}
              <label className="block mb-6">
                <div className="font-bold mb-2">요청 파라미터</div>
                <div>
                  {api.parameters.map((parameter, idx) => (
                    <div key={`idx-${parameter.key}`} className="flex mb-4">
                      <input className="mr-3" type="text" placeholder="변수명" />
                      <input type="text" placeholder="설명" />
                    </div>
                  ))}
                  <div className="flex justify-end">
                    <button
                      className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white"
                      onClick={() => addParameter(api.id)}
                    >
                      파라미터 추가 (미구현)
                    </button>
                  </div>
                </div>
              </label>
              {/* Response Parameters */}
              <label className="block mb-6">
                <div className="font-bold mb-2">응답 파라미터</div>
                <div>
                  {api.responseParameters.map((responseParameters, idx) => (
                    <div key={`idx-${responseParameters.key}`} className="flex mb-4">
                      <input className="mr-3" type="text" placeholder="변수명" />
                      <input type="text" placeholder="설명" />
                    </div>
                  ))}
                  <div className="flex justify-end">
                    <button
                      className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white"
                      onClick={() => addResponseParameter(api.id)}
                    >
                      파라미터 추가 (미구현)
                    </button>
                  </div>
                </div>
              </label>
              {/* Error Code */}
              <label className="block mb-6">
                <div className="font-bold mb-2">에러 코드</div>
                <div>
                  {api.errorCodes.map((errorCode, idx) => (
                    <div key={`idx-${errorCode.key}`} className="flex mb-4">
                      <input className="mr-3" type="text" placeholder="에러 코드" />
                      <input type="text" placeholder="설명" />
                    </div>
                  ))}
                  <div className="flex justify-end">
                    <button
                      className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white"
                      onClick={() => addErrorCode(api.id)}
                    >
                      에러 코드 추가 (미구현)
                    </button>
                  </div>
                </div>
              </label>
              {/* 최대 응답 시간 */}
              <label className="block mb-12">
                <div className="font-bold mb-2">최대 응답 시간</div>
                <div className="flex items-center">
                  <input type="number" placeholder="150" />
                  <span className="ml-3">ms</span>
                </div>
              </label>
              <hr className="mb-12" />
            </div>
          ))}
        </div>

        <div className="flex justify-end mb-12">
          <button className="rounded bg-blue-500 text-white py-3 px-6" onClick={addApi}>
            API 추가 (미구현)
          </button>
        </div>

        <hr className="mb-12" />

        <div className="flex justify-end">
          <button className="rounded bg-blue-500 text-white py-3 px-6" onClick={submitForm}>
            서비스 등록 (미구현)
          </button>
        </div>
      </form>
    </main>
  );
}
