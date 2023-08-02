import { useState } from 'react';
import { ErrorCode } from '../types';

export default function ApiErrorCodesInput({ errorCodes, onChange }: { errorCodes: ErrorCode[], onChange: (errorCodes: ErrorCode[]) => void }) {
  const [value, setValue] = useState<ErrorCode[]>(errorCodes);

  /**
   * add new error code input
   */
  function addErrorCode() {
    for (const { statusCode } of value) {
      if (statusCode === -1) {
        alert('먼저 코드를 모두 입력해주세요');
        return;
      }
    }

    const updatedErrorCodes = [...value];
    updatedErrorCodes.push({ statusCode: -1, description: '' });

    setValue(updatedErrorCodes);
  }

  /**
   * update error code by index
   */
  function updateErrorCode(idx: number, data: Partial<ErrorCode>) {
    const updatedErrorCodes = [...value];
    updatedErrorCodes[idx] = { ...updatedErrorCodes[idx], ...data };

    setValue(updatedErrorCodes);

    onChange(updatedErrorCodes);
  }

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">에러 코드</div>
      <div>
        {value.map((code, idx) => (
          <div key={idx} className="flex mb-4">
            <input
              className="mr-3"
              type="text"
              placeholder="상태 코드 (ex: 404)"
              onChange={e => {
                updateErrorCode(idx, { statusCode: Number.parseInt(e.target.value) });
              }}
              value={code.statusCode.toString()}
            />
            <input
              type="text"
              placeholder="설명"
              onChange={e => {
                updateErrorCode(idx, { description: e.target.value });
              }}
              value={code.description}
            />
          </div>
        ))}
        <div className="flex justify-end">
          <button className="btn btn-form-outline" onClick={() => addErrorCode()}>
            에러 코드 추가
          </button>
        </div>
      </div>
    </label>
  );
}
