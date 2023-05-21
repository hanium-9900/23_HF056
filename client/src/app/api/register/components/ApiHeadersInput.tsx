import { useState } from 'react';
import { Header } from '../types';

export default function ApiHeadersInput({ onChange }: { onChange: (headers: Header[]) => void }) {
  const [headers, setHeaders] = useState<Header[]>([]);

  /**
   * add new header input
   */
  function addHeader() {
    for (const { key } of headers) {
      if (key.trim() === '') {
        alert('먼저 헤더명을 모두 입력해주세요');
        return;
      }
    }

    const updatedHeaders = [...headers];
    updatedHeaders.push({ key: '', description: '', required: false });

    setHeaders(updatedHeaders);
  }

  /**
   * update header by index
   */
  function updateHeader(idx: number, data: Partial<Header>) {
    const updatedHeaders = [...headers];
    updatedHeaders[idx] = { ...updatedHeaders[idx], ...data };

    setHeaders(updatedHeaders);

    onChange(updatedHeaders);
  }

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">헤더</div>
      <div>
        {headers.map((parameter, idx) => (
          <div key={idx} className="flex mb-4">
            <input
              className="mr-3"
              type="text"
              placeholder="헤더명"
              onChange={e => {
                updateHeader(idx, { key: e.target.value });
              }}
              value={parameter.key}
            />
            <input
              type="text"
              placeholder="설명"
              onChange={e => {
                updateHeader(idx, { description: e.target.value });
              }}
              value={parameter.description}
            />
          </div>
        ))}
        <div className="flex justify-end">
          <button className="btn btn-form-outline" onClick={() => addHeader()}>
            헤더 추가
          </button>
        </div>
      </div>
    </label>
  );
}
