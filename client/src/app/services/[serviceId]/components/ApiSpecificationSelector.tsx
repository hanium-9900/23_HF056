'use client';

import { ServiceResponse } from '@/api';
import ApiSpecification from './ApiSpecification';
import { useState } from 'react';

export default function ApiSpecificationSelector({ service }: { service: ServiceResponse }) {
  const [selectedApi, setSelectedApi] = useState(0);

  return (
    <>
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
      <ApiSpecification api={service.apis[selectedApi]} />
    </>
  );
}
