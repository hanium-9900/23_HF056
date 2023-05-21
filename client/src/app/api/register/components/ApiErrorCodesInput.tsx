import { useState } from 'react';
import { ErrorCode } from '../types';

export default function ApiErrorCodesInput({ onChange }: { onChange: (errorCodes: ErrorCode[]) => void }) {
  const [errorCodes, setErrorCodes] = useState<ErrorCode[]>([]);

  /**
   * add new error code input
   */
  function addErrorCode() {
    for (const { key } of errorCodes) {
      if (key.trim() === '') {
        alert('먼저 코드를 모두 입력해주세요');
        return;
      }
    }

    const updatedErrorCodes = [...errorCodes];
    updatedErrorCodes.push({ key: '', description: '' });

    setErrorCodes(updatedErrorCodes);
  }

  /**
   * update error code by index
   */
  function updateErrorCode(idx: number, data: Partial<ErrorCode>) {
    const updatedErrorCodes = [...errorCodes];
    updatedErrorCodes[idx] = { ...updatedErrorCodes[idx], ...data };

    setErrorCodes(updatedErrorCodes);

    onChange(updatedErrorCodes);
  }

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">에러 코드</div>
      <div>
        {errorCodes.map((code, idx) => (
          <div key={idx} className="flex mb-4">
            <input
              className="mr-3"
              type="text"
              placeholder="에러 코드"
              onChange={e => {
                updateErrorCode(idx, { key: e.target.value });
              }}
              value={code.key}
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
