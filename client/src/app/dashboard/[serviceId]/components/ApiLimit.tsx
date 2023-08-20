import { ServiceResponse, ServiceUsageResponse } from '@/api';

export default function ApiLimit({ api, usage }: { api: ServiceResponse['apis'][number]; usage?: ServiceUsageResponse }) {
  const rate = (usage?.usage_rate || 0) * 100;

  return (
    <div className="mb-12">
      {/* 사용량 */}
      <div className="mb-6">
        <div className="font-bold text-xl mb-2">
          [{api.method}] {api.path}
        </div>
        <div className="relative bg-slate-300 rounded-full h-8 mb-2">
          <div
            className="absolute top-0 left-0 h-full font-bold text-sm bg-blue-500 text-white rounded-full flex items-center justify-end"
            style={{ width: `${rate}%` }}
          >
            <div className="pr-3">{rate}%</div>
          </div>
        </div>
        <div className="flex items-center">
          <div>
            <div className="font-bold text-blue-500" style={{ fontSize: 64 }}>
              {rate}%
            </div>
            <div>
              <span className="font-bold text-3xl">사용중</span>
              <span className="text-lg">
                (유저당 <span className="font-bold">{api.limitation?.toLocaleString()}/일</span> 제한)
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
