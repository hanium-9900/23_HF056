'use client';

import { ServiceResponse, api } from "@/api";
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
          <div className="text-center text-2xl">
            서비스가 없습니다.
          </div>
        )}
        {loading && (
          <div className="text-center text-2xl font-bold">
            로딩중..
          </div>
        )}

      </div>
    </>
  )
}
