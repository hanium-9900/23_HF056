import Link from "next/link";
import Image from 'next/image'
import { ServiceResponse } from "@/api";

export default function ServiceListRenderer({ services }: { services: ServiceResponse[] }) {
  return (
    <>
      <div className="grid grid-cols-3 gap-5">
        {services.map((service, idx) => (
          <Link
            href={'/services/' + service.id}
            key={service.id}
            className="rounded border border-slate-400 p-6 group transition-all hover:border-slate-500 hover:scale-105 hover:shadow"
          >
            <div className="mb-6">
              <div className="inline-block rounded-full bg-blue-500 text-white text-xs font-bold py-1 px-4 mb-3">{service.category}</div>
              <div className="text-xl font-bold transition-colors group-hover:text-blue-500">{service.title}</div>
            </div>
            <div className="text-sm text-gray-500 text-justify mb-8">{service.description}</div>
            <div className="text-right text-blue-500 font-bold text-xl">
              <span>{Intl.NumberFormat('ko-KR').format(service.price)}</span>원
            </div>
          </Link>
        ))}
      </div>
      <div>
        {services.length === 0 && (
          <div className="flex flex-col items-center">
            <Image className="mb-3" width={64} height={64} src='/block.svg' alt="Empty" />
            <div className="text-center text-gray-400 text-2xl">
              구매한 서비스가 없습니다.
            </div>
          </div>
        )}
      </div>
    </>
  )
}
