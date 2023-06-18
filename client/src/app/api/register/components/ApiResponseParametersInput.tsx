import { useState } from 'react';
import { ResponseParameter } from '../types';

export default function ApiResponseParametersInput({ onChange }: { onChange: (responseParameters: ResponseParameter[]) => void }) {
  const [responseParameters, setResponseParameters] = useState<ResponseParameter[]>([]);

  /**
   * add new response parameter input
   */
  function addParameter() {
    for (const { key } of responseParameters) {
      if (key.trim() === '') {
        alert('먼저 파라미터명을 모두 입력해주세요');
        return;
      }
    }

    const updatedResponseParameters = [...responseParameters];
    updatedResponseParameters.push({ key: '', type: 'string', description: '', required: false });

    setResponseParameters(updatedResponseParameters);
  }

  /**
   * update request parameter by index
   */
  function updateParameter(idx: number, data: Partial<ResponseParameter>) {
    const updatedResponseParameters = [...responseParameters];
    updatedResponseParameters[idx] = { ...updatedResponseParameters[idx], ...data };

    setResponseParameters(updatedResponseParameters);

    onChange(updatedResponseParameters);
  }

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">응답 파라미터</div>
      <div>
        {responseParameters.map((parameter, idx) => (
          <div key={idx} className="flex mb-4">
            <input
              className="mr-3"
              type="text"
              placeholder="변수명"
              onChange={e => {
                updateParameter(idx, { key: e.target.value });
              }}
              value={parameter.key}
            />
            <input
              className="mr-3"
              type="text"
              placeholder="타입"
              onChange={e => {
                updateParameter(idx, { type: e.target.value });
              }}
              value={parameter.type}
            />
            <input
              type="text"
              placeholder="설명"
              onChange={e => {
                updateParameter(idx, { description: e.target.value });
              }}
              value={parameter.description}
            />
          </div>
        ))}
        <div className="flex justify-end">
          <button className="btn btn-form-outline" onClick={() => addParameter()}>
            파라미터 추가
          </button>
        </div>
      </div>
    </label>
  );
}
