import { ServiceResponse } from '@/api';

export default function ApiSpecification({ api }: { api: ServiceResponse['apis'][number] }) {
  return (
    <div>
      {/* API 헤더 정보 */}
      <div className="mb-6">
        <div className="font-bold mb-2">헤더</div>
        <table>
          <thead>
            <tr className="bg-slate-50 border-slate-300 border">
              <th scope="col" className="w-1/12">
                필수
              </th>
              <th scope="col" className="w-2/12">
                헤더명
              </th>
              <th scope="col" className="w-7/12">
                설명
              </th>
            </tr>
          </thead>
          <tbody>
            {api.headers.map((header, idx) => (
              <tr key={idx}>
                <td>{header.required ? 'required' : 'optional'}</td>
                <td>{header.key}</td>
                <td>{header.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* API 요청 파라미터 정보 */}
      <div className="mb-6">
        <div className="font-bold mb-2">요청 파라미터</div>
        <table>
          <thead>
            <tr className="bg-slate-50 border-slate-300 border">
              <th scope="col" className="w-1/12">
                필수
              </th>
              <th scope="col" className="w-2/12">
                변수명
              </th>
              <th scope="col" className="w-2/12">
                타입
              </th>
              <th scope="col" className="w-7/12">
                설명
              </th>
            </tr>
          </thead>
          <tbody>
            {api.requestParameters.map((param, idx) => (
              <tr key={idx}>
                <td>{param.required ? 'required' : 'optional'}</td>
                <td>{param.key}</td>
                <td>{param.type}</td>
                <td>{param.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* API 응답 파라미터 정보 */}
      <div className="mb-6">
        <div className="font-bold mb-2">응답 파라미터</div>
        <table>
          <thead>
            <tr className="bg-slate-50 border-slate-300 border">
              <th scope="col" className="w-1/12">
                필수
              </th>
              <th scope="col" className="w-2/12">
                변수명
              </th>
              <th scope="col" className="w-2/12">
                타입
              </th>
              <th scope="col" className="w-7/12">
                설명
              </th>
            </tr>
          </thead>
          <tbody>
            {api.responseParameters.map((param, idx) => (
              <tr key={idx}>
                <td>{param.required ? 'required' : 'optional'}</td>
                <td>{param.key}</td>
                <td>{param.type}</td>
                <td>{param.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
      {/* API 에러 코드 정보 */}
      <div className="mb-6">
        <div className="font-bold mb-2">에러 코드</div>
        <table>
          <thead>
            <tr className="bg-slate-50 border-slate-300 border">
              <th scope="col" className="w-2/12">
                코드
              </th>
              <th scope="col" className="w-10/12">
                설명
              </th>
            </tr>
          </thead>
          <tbody>
            {api.errorCodes.map((errorCode, idx) => (
              <tr key={idx}>
                <td>{errorCode.statusCode}</td>
                <td>{errorCode.description}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
