import { useState } from 'react';
import { ResponseParameter } from '../types';

export default function ApiResponseParametersInput({
  responseParameters,
  onChange,
}: {
  responseParameters: ResponseParameter[];
  onChange: (responseParameters: ResponseParameter[]) => void;
}) {
  const [value, setValue] = useState<ResponseParameter[]>(responseParameters);

  /**
   * add new response parameter input
   */
  function addParameter() {
    for (const { key } of value) {
      if (key.trim() === '') {
        alert('먼저 파라미터명을 모두 입력해주세요');
        return;
      }
    }

    const updatedResponseParameters = [...value];
    updatedResponseParameters.push({ key: '', type: 'string', description: '', required: false });

    setValue(updatedResponseParameters);
  }

  /**
   * update request parameter by index
   */
  function updateParameter(idx: number, data: Partial<ResponseParameter>) {
    const updatedResponseParameters = [...value];
    updatedResponseParameters[idx] = { ...updatedResponseParameters[idx], ...data };

    setValue(updatedResponseParameters);

    onChange(updatedResponseParameters);
  }

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">응답 파라미터</div>
      <div>
        {value.map((parameter, idx) => (
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
            <select
              className="mr-3"
              defaultValue={parameter.type}
              onChange={e => {
                updateParameter(idx, { type: e.target.value });
              }}
            >
              <option value="string">String</option>
              <option value="number">Number</option>
              <option value="boolean">Boolean</option>
            </select>
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
          <button type="button" className="btn btn-form-outline" onClick={() => addParameter()}>
            파라미터 추가
          </button>
        </div>
      </div>
    </label>
  );
}
