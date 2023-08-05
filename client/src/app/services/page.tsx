import { api } from '@/api';
import Link from 'next/link';
import { categories } from '@/config/category'

export default async function ServiceList() {
  const { data: services } = await api.services.list();

  return (
    <main className="max-w-7xl mx-auto py-14 px-4">
      <h1 className="text-3xl font-bold text-center mb-12">서비스 목록</h1>

      <div className="flex items-center flex-wrap justify-center gap-5 mb-8">
        <button className="rounded-full border-2 border-blue-500 bg-blue-500 text-white font-bold transition-colors hover:border-blue-600 hover:bg-blue-600 text-xl py-3 px-10">
          전체
        </button>
        {categories.map(category => (
          <button key={category} className="rounded-full border-2 border-slate-400 bg-white text-slate-400 font-bold transition-colors hover:border-slate-500 hover:text-slate-500 text-xl py-3 px-10">
            {category}
          </button>
        ))}
      </div>

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
    </main>
  );
}
