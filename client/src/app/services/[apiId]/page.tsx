'use client';

import { useEffect, useState } from 'react';
import './page.css';
import { Service } from '../register/types';
import ApiSpecification from './components/ApiSpecification';
import ApiPurchaseButton from './components/ApiPurchaseButton';

export default function ServiceInfoPage() {
  // [TEMP] 임시 데이터
  const [service, setService] = useState<Service>({
    title: '대구광역시 행정동별 유동인구',
    price: 30000000,
    description:
      '1500만 사용자의 통신 데이터를 근간으로 대구광역시 지역단위(광역시도, 시군구, 읍면동, 50M구역)별 유동인구 정보로 시간대별, 성별, 연령별로 유동인구 정보를 확인 할 수 있는 상품데이터',
    key: 'asd123asd123asd123',
    apis: [
      {
        host: 'https://example.com',
        method: 'GET',
        description: '테스트 API 1번',
        path: '/example/api1',
        headers: [
          {
            key: '테스트 헤더1',
            description: '테스트 헤더1 설명',
            required: true,
          },
        ],
        requestParameters: [],
        responseParameters: [],
        errorCodes: [],
      },
      {
        host: 'https://example.com',
        method: 'GET',
        description: '테스트 API 2번',
        path: '/example/api2',
        headers: [
          {
            key: '테스트 헤더2',
            description: '테스트 헤더2 설명',
            required: false,
          },
        ],
        requestParameters: [],
        responseParameters: [],
        errorCodes: [],
      },
    ],
  });
  const [selectedApi, setSelectedApi] = useState(0);

  // [TODO] 서버로부터 가져오기

  return (
    <main className="container xl:max-w-5xl mx-auto py-10 px-3">
      {/* 서비스 정보 */}
      <div className="mb-16">
        <div className="mb-8">
          <div className="text-blue-500 mb-1">공간</div>
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
