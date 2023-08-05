import { api } from "@/api";
import { getServerSession } from "next-auth";
import Link from "next/link";
import { redirect } from "next/navigation";
import ApiLimit from "./components/ApiLimit";
import PieGraph from './components/PieGraph';

async function getDatas(serviceId: number) {
  const p1 = api.services.show(serviceId).then(res => res.data);
  const p2 = api.services.errorLogs(serviceId).then(res => res.data);
  const p3 = api.services.statistics(serviceId, new Date().getMonth() + 1).then(res => res.data);
  const p4 = api.services.usage(serviceId).then(res => res.data);

  const [service, errorLogs, statistics, usage] = await Promise.all([p1, p2, p3, p4]);

  return {
    service,
    errorLogs,
    statistics,
    usage,
  };
}

export default async function Page({ params }: { params: { serviceId: string } }) {
  const serviceId = Number.parseInt(params.serviceId);
  if (Number.isNaN(serviceId)) {
    redirect('/');
  }

  const { service, errorLogs, statistics, usage } = await getDatas(serviceId);
  const session = await getServerSession();
  if (session?.user?.email !== service.user.email) {
    redirect('/')
  }

  return (
    <main className="container mx-auto py-10">
      <div className="mb-7">
        <Link href={`/services/${service.id}`} className=" font-bold text-lg text-blue-500 mb-2">{service.title}</Link>
        <div className="font-bold text-2xl">서비스 사용량 개요</div>
      </div>

      <pre>
        <code>
          {JSON.stringify(errorLogs, undefined, 2)}
        </code>
      </pre>
      <pre>
        <code>
          {JSON.stringify(statistics, undefined, 2)}
        </code>
      </pre>
      <pre>
        <code>
          {JSON.stringify(usage, undefined, 2)}
        </code>
      </pre>
      <div className="mb-7">
        <PieGraph statistics={statistics} />
      </div>
      <div className="bg-gray-300 py-32 text-center mb-7">
        여기에 차트
      </div>
      {service.apis.map(api => (
        <ApiLimit key={api.id} api={api} usage={usage.find(u => u.id = api.id)} />
      ))
      }
      <pre>
        <code>{JSON.stringify(service, undefined, 2)}</code>
      </pre>
    </main >

  );
}
