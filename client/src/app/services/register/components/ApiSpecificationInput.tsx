import { useState } from 'react';
import { ApiInfo } from '../types';

import ApiHeadersInput from './ApiHeadersInput';
import ApiRequestParametersInput from './ApiRequestParametersInput';
import ApiResponseParametersInput from './ApiResponseParametersInput';
import ApiErrorCodesInput from './ApiErrorCodesInput';

export default function ApiSpecificationInput({
  no,
  api,
  removeApi,
  onChange,
}: {
  no: number;
  api: ApiInfo;
  removeApi: (idx: number) => void;
  onChange: (api: ApiInfo) => void;
}) {
  const [specification, setSpecification] = useState<ApiInfo>(api);

  function updateSpecification(data: Partial<ApiInfo>) {
    const updatedSpecification = { ...specification, ...data };

    setSpecification(updatedSpecification);

    onChange(updatedSpecification);
  }

  return (
    <div className="">
      {/* API Host */}
      <label className="flex items-center mb-6">
        <span className="rounded bg-blue-500 text-white shrink-0 py-2 px-4 mr-5">{no + 1}번</span>
        <div className="flex items-center grow mr-3">
          <div className="font-bold mr-3">Host</div>
          <input type="text" placeholder="https://example.com" onChange={e => updateSpecification({ host: e.target.value })} required />
        </div>
        <button className="rounded bg-red-500 text-white shrink-0 py-2 px-4" onClick={e => removeApi(no)}>
          삭제
        </button>
      </label>
      {/* API Endpoint */}
      <div className="flex items-center mb-6">
        <select className="mr-3" onChange={e => updateSpecification({ method: e.target.value })} required>
          <option value="GET">GET</option>
          <option value="POST">POST</option>
          <option value="PUT">PUT</option>
          <option value="PATCH">PATCH</option>
          <option value="DELETE">DELETE</option>
        </select>
        <input type="text" placeholder="/example/api/path" onChange={e => updateSpecification({ path: e.target.value })} required />
      </div>
      {/* Description */}
      <label className="block mb-6">
        <div className="font-bold mb-2">API 설명</div>
        <textarea rows={5} placeholder="API 설명을 입력하세요" onChange={e => updateSpecification({ description: e.target.value })}></textarea>
      </label>
      {/* Headers */}
      <ApiHeadersInput onChange={headers => updateSpecification({ headers })} />
      {/* Request Parameters */}
      <ApiRequestParametersInput onChange={requestParameters => updateSpecification({ requestParameters })} />
      {/* Response Parameters */}
      <ApiResponseParametersInput onChange={responseParameters => updateSpecification({ responseParameters })} />
      {/* Error Code */}
      <ApiErrorCodesInput onChange={errorCodes => updateSpecification({ errorCodes })} />
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
  );
}
