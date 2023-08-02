import { useState } from 'react';
import { RequestParameter } from '../types';
import { toast } from 'react-toastify';

export default function ApiRequestParametersInput({
  requestParameters,
  onChange,
}: {
  requestParameters: RequestParameter[];
  onChange: (requestParameters: RequestParameter[]) => void;
}) {
  const [value, setValue] = useState<RequestParameter[]>(requestParameters);

  /**
   * add new request parameter input
   */
  function addParameter() {
    for (const { key } of value) {
      if (key.trim() === '') {
        toast.error('먼저 파라미터명을 모두 입력해주세요!');
        return;
      }
    }

    const updatedRequestParameters = [...value];
    updatedRequestParameters.push({ key: '', type: 'string', description: '', required: false });

    setValue(updatedRequestParameters);
  }

  /**
   * update request parameter by index
   */
  function updateParameter(idx: number, data: Partial<RequestParameter>) {
    const updatedRequestParameters = [...value];
    updatedRequestParameters[idx] = { ...updatedRequestParameters[idx], ...data };

    setValue(updatedRequestParameters);

    onChange(updatedRequestParameters);
  }

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">요청 파라미터</div>
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
