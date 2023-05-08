import './page.css';

export default function Register() {
  return (
    <main className="max-w-3xl mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">서비스 등록</h1>
      <form className="rounded border border-slate-300 p-7">
        {/* 서비스 명세 */}
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 이름</div>
          <input type="text" placeholder="서비스 이름을 입력하세요" />
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 설명</div>
          <textarea rows={5} placeholder="서비스 설명을 입력하세요"></textarea>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 가격</div>
          <div className="flex items-center">
            <input type="number" placeholder="30000" />
            <span className="ml-3">원</span>
          </div>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">API 키</div>
          <input type="password" />
        </label>
        {/* API 명세 */}
        <div className="block mb-6">
          <div className="font-bold mb-2">API 명세</div>
          <label className="flex items-center mb-12">
            <div className="font-bold mr-3">Host</div>
            <input type="text" placeholder="https://example.com" />
          </label>

          <hr className="mb-12" />

          {/* API 목록 */}
          <div className="">
            {/* API Endpoint */}
            <div className="flex items-center mb-6">
              <span className="rounded bg-blue-500 text-white shrink-0 py-2 px-4 mr-3">1번</span>
              <select className="mr-3">
                <option value="GET">GET</option>
                <option value="POST">POST</option>
                <option value="PUT">PUT</option>
                <option value="PATCH">PATCH</option>
                <option value="DELETE">DELETE</option>
              </select>
              <input type="text" placeholder="/example/api/path" />
            </div>
            {/* Description */}
            <label className="block mb-6">
              <div className="font-bold mb-2">API 설명</div>
              <textarea rows={5} placeholder="API 설명을 입력하세요"></textarea>
            </label>
            {/* Headers */}
            <label className="block mb-6">
              <div className="font-bold mb-2">헤더</div>
              <div>
                <div className="flex mb-4">
                  <input className="mr-3" type="text" placeholder="헤더명" />
                  <input type="text" placeholder="설명" />
                </div>
                <div className="flex justify-end">
                  <button className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white">
                    헤더 추가 (미구현)
                  </button>
                </div>
              </div>
            </label>
            {/* Request Parameters */}
            <label className="block mb-6">
              <div className="font-bold mb-2">요청 파라미터</div>
              <div>
                <div className="flex mb-4">
                  <input className="mr-3" type="text" placeholder="변수명" />
                  <input type="text" placeholder="설명" />
                </div>
                <div className="flex justify-end">
                  <button className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white">
                    파라미터 추가 (미구현)
                  </button>
                </div>
              </div>
            </label>
            {/* Response Parameters */}
            <label className="block mb-6">
              <div className="font-bold mb-2">응답 파라미터</div>
              <div>
                <div className="flex mb-4">
                  <input className="mr-3" type="text" placeholder="변수명" />
                  <input type="text" placeholder="설명" />
                </div>
                <div className="flex justify-end">
                  <button className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white">
                    파라미터 추가 (미구현)
                  </button>
                </div>
              </div>
            </label>
            {/* Error Code */}
            <label className="block mb-6">
              <div className="font-bold mb-2">에러 코드</div>
              <div>
                <div className="flex mb-4">
                  <input className="mr-3" type="text" placeholder="에러 코드" />
                  <input type="text" placeholder="설명" />
                </div>
                <div className="flex justify-end">
                  <button className="rounded border-2 border-blue-500 bg-white-500 text-blue-500 py-3 px-6 transition-colors hover:bg-blue-500 hover:text-white">
                    에러 코드 추가 (미구현)
                  </button>
                </div>
              </div>
            </label>
            {/* 최대 응답 시간 */}
            <label className="block mb-6">
              <div className="font-bold mb-2">최대 응답 시간</div>
              <div className="flex items-center">
                <input type="number" placeholder="150" />
                <span className="ml-3">ms</span>
              </div>
            </label>
          </div>
        </div>

        <div className="flex justify-end mb-12">
          <button className="rounded bg-blue-500 text-white py-3 px-6">API 추가 (미구현)</button>
        </div>

        <hr className="mb-12" />

        <div className="flex justify-end">
          <button className="rounded bg-blue-500 text-white py-3 px-6">서비스 등록 (미구현)</button>
        </div>
      </form>
    </main>
  );
}
