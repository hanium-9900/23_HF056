'use client';

import { ServiceResponse, api } from "@/api";
import Image from "next/image";
import Link from "next/link";
import { useSearchParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function ServiceList() {
  const query = useSearchParams();

  const [services, setServices] = useState([] as ServiceResponse[]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    async function getServices() {
      setLoading(true);

      const category = query.get('category');
      try {

        const { data } = await api.services.list(category ? category : undefined);

        setServices(data);
      } catch (e) {
        throw e;
      } finally {
        setLoading(false);
      }
    }
    getServices();
  }, [query]);

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
        {!loading && services.length === 0 && (
          <div className="flex flex-col items-center">
            <Image className="mb-3" width={64} height={64} src='/block.svg' alt="Empty" />
            <div className="text-center text-gray-400 text-2xl">
              서비스가 없습니다.
            </div>
          </div>
        )}
        {loading && (
          <div className="flex justify-center">
            <div>
              <svg aria-hidden="true" className="w-16 h-16 mr-2 text-gray-200 animate-spin fill-blue-500" viewBox="0 0 100 101" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M100 50.5908C100 78.2051 77.6142 100.591 50 100.591C22.3858 100.591 0 78.2051 0 50.5908C0 22.9766 22.3858 0.59082 50 0.59082C77.6142 0.59082 100 22.9766 100 50.5908ZM9.08144 50.5908C9.08144 73.1895 27.4013 91.5094 50 91.5094C72.5987 91.5094 90.9186 73.1895 90.9186 50.5908C90.9186 27.9921 72.5987 9.67226 50 9.67226C27.4013 9.67226 9.08144 27.9921 9.08144 50.5908Z" fill="currentColor" />
                <path d="M93.9676 39.0409C96.393 38.4038 97.8624 35.9116 97.0079 33.5539C95.2932 28.8227 92.871 24.3692 89.8167 20.348C85.8452 15.1192 80.8826 10.7238 75.2124 7.41289C69.5422 4.10194 63.2754 1.94025 56.7698 1.05124C51.7666 0.367541 46.6976 0.446843 41.7345 1.27873C39.2613 1.69328 37.813 4.19778 38.4501 6.62326C39.0873 9.04874 41.5694 10.4717 44.0505 10.1071C47.8511 9.54855 51.7191 9.52689 55.5402 10.0491C60.8642 10.7766 65.9928 12.5457 70.6331 15.2552C75.2735 17.9648 79.3347 21.5619 82.5849 25.841C84.9175 28.9121 86.7997 32.2913 88.1811 35.8758C89.083 38.2158 91.5421 39.6781 93.9676 39.0409Z" fill="currentFill" />
              </svg>
              <span className="sr-only">Loading...</span>
            </div>
          </div>
        )}

      </div>
    </>
  )
}
