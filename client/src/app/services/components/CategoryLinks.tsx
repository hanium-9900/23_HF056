'use client';

import { categories } from '@/config/category';
import Link from 'next/link';
import { useSearchParams } from 'next/navigation';

export default function CategoryLinks() {
  const query = useSearchParams();
  const active = 'border-blue-500 bg-blue-500 text-white hover:border-blue-600 hover:bg-blue-600';
  const inactive = 'border-slate-400 bg-white text-slate-400 hover:border-slate-500 hover:text-slate-500';

  return (
    <div className="flex items-center flex-wrap justify-center gap-5 mb-8">
      <Link
        href="/services"
        className={`rounded-full border-2 font-bold transition-colors text-xl py-3 px-10 ` + (query.get('category') === null ? active : inactive)}
      >
        전체
      </Link>
      {categories.map(category => (
        <Link
          href={`/services?category=${category}`}
          key={category}
          className={`rounded-full border-2 font-bold transition-colors text-xl py-3 px-10 ` + (query.get('category') === category ? active : inactive)}
        >
          {category}
        </Link>
      ))}
    </div>
  );
}
