import { api } from "@/api";
import { getServerSession } from "next-auth";
import Link from "next/link";
import { redirect } from "next/navigation";
import ApiLimit from "./components/ApiLimit";
import PieGraph from './components/PieGraph';
import LineGraph from "./components/LineGraph";
import Image from "next/image";

async function getDatas(serviceId: number) {
  const now = new Date();

  const p1 = api.services.show(serviceId).then(res => res.data);
  const p2 = api.services.errorLogs(serviceId).then(res => res.data);
  const p3 = api.services.statistics(serviceId, now.getFullYear(), now.getMonth() + 1).then(res => res.data);
  const p4 = api.services.usage(serviceId).then(res => res.data);

  let prevMonth = now.getMonth() - 1;
  const prevYear = prevMonth < 0 ? now.getFullYear() - 1 : now.getFullYear();
  prevMonth = prevMonth < 0 ? 12 : prevMonth + 1;
  const p5 = api.services.statistics(serviceId, prevYear, prevMonth).then(res => res.data);

  const [service, errorLogs, statistics, usage, prevStatistics] = await Promise.all([p1, p2, p3, p4, p5]);

  return {
    service,
    errorLogs,
    statistics,
    prevStatistics,
    usage,
  };
}

export default async function Page({ params }: { params: { serviceId: string } }) {
  const serviceId = Number.parseInt(params.serviceId);
  if (Number.isNaN(serviceId)) {
    redirect('/');
  }

  const { service, errorLogs, statistics, usage, prevStatistics } = await getDatas(serviceId);
  const session = await getServerSession();
  if (session?.user?.email !== service.user.email) {
    return (
      <main className="mx-auto py-10 px-3">
        <div className="text-center flex flex-col items-center">
          <Image className="mb-5" width={128} height={128} src='/icon-danger.png' alt="경고" />
          <div className="font-bold text-xl mb-2">접근 권한이 없습니다.</div>
          <div className="font-light">자신이 등록한 서비스의 사용량만 열람할 수 있습니다.</div>
        </div>
      </main>
    )
    redirect('/')
  }

  return (
    <main className="mx-auto py-10 px-3" style={{ width: 1500 }}>
      <div className="mb-7">
        <Link href={`/services/${service.id}`} className=" font-bold text-lg text-blue-500 mb-2">{service.title}</Link>
        <div className="font-bold text-2xl">서비스 사용량 개요</div>
      </div>

      <div className="flex justify-center items-center rounded border mb-7">
        <div className="inline-block" style={{ width: 1000, height: 490 }}>
          <LineGraph statistics={statistics} prevStatistics={prevStatistics} />
        </div>
        <div className="inline-block" style={{ width: 450, height: 490 }}>
          <PieGraph statistics={statistics} />
        </div>
      </div>
      <div className="mb-12">
        {service.apis.map(api => (
          <ApiLimit key={api.id} api={api} usage={usage.find(u => u.id = api.id)} />
        ))}
      </div>
      <div className="mb-6">
        <div className="font-bold text-2xl mb-2">최근 에러 로그</div>
        <table>
          <thead>
            <tr className="bg-slate-50 border-slate-300 border">
              <th scope="col" className="w-1/12">
                Method
              </th>
              <th scope="col" className="w-7/12">
                Path
              </th>
              <th scope="col" className="w-2/12">
                Status
              </th>
              <th scope="col" className="w-2/12">
                Date
              </th>
            </tr>
          </thead>
          <tbody>
            {errorLogs.map((log, idx) => (
              <tr key={idx}>
                <td>{log.method}</td>
                <td>{log.path}</td>
                <td>{log.response_code}</td>
                <td>{log.creation_times}</td>
              </tr>
            ))}
            {errorLogs.length === 0 && (
              <tr>
                <td colSpan={4}>에러 로그가 없습니다.</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </main >
  );
}
