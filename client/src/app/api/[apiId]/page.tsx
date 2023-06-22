import './page.css';

export default function ServiceInfoPage() {
  return (
    <main className="container xl:max-w-5xl mx-auto py-10 px-3">
      {/* 상단 서비스 정보 */}
      <div className="mb-16">
        <div className="mb-8">
          <div className="text-blue-500 mb-1">공간</div>
          <div className="flex items-center justify-between text-3xl font-bold">
            <span>대구광역시 행정동별 유동인구</span>
            <span className="text-blue-500">30,000,000 &#8361;</span>
          </div>
        </div>
        <div className="flex items-center justify-between">
          <span className="text-gray-500 break-keep mr-6">
            1500만 사용자의 통신 데이터를 근간으로 대구광역시 지역단위(광역시도, 시군구, 읍면동, 50M구역)별 유동인구 정보로 시간대별, 성별, 연령별로 유동인구
            정보를 확인 할 수 있는 상품데이터
          </span>
          <button className="btn btn-form shrink-0">구매</button>
        </div>
      </div>
      {/* API 선택 */}
      <div className="flex items-center mb-6">
        <span className="font-bold mr-3">API 구분</span>
        <select>
          <option value="/example/api1">/example/api1</option>
          <option value="/example/api2">/example/api2</option>
          <option value="/example/api3">/example/api3</option>
          <option value="/example/api4">/example/api4</option>
        </select>
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
            <tr>
              <td>required</td>
              <td>variable_1</td>
              <td>String</td>
              <td>변수 1 설명입니다.</td>
            </tr>
            <tr>
              <td>required</td>
              <td>variable_2</td>
              <td>String</td>
              <td>변수 2 설명입니다.</td>
            </tr>
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
            <tr>
              <td>required</td>
              <td>variable_3</td>
              <td>Number</td>
              <td>변수 3 설명입니다.</td>
            </tr>
            <tr>
              <td>required</td>
              <td>variable_4</td>
              <td>Number</td>
              <td>변수 4 설명입니다.</td>
            </tr>
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
            <tr>
              <td>401</td>
              <td>인증되지 않음.</td>
            </tr>
            <tr>
              <td>403</td>
              <td>해당 리소스에 대한 권한이 없음.</td>
            </tr>
            <tr>
              <td>404</td>
              <td>해당 리소스가 없음.</td>
            </tr>
            <tr>
              <td>500</td>
              <td>서버 내부 에러 발생.</td>
            </tr>
          </tbody>
        </table>
      </div>
    </main>
  );
}
