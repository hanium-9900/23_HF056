import Link from 'next/link';

export default function Home() {
  return (
    <main className="py-10 text-center">
      <div className="text-xl mb-10">메인페이지(미구현)</div>
      <div className="flex flex-col gap-3 text-blue-500 ">
        <Link href="/services/register">- 서비스 등록 페이지</Link>
        <Link href="/services/1">- 서비스 조회 페이지(id=1)</Link>
      </div>
    </main>
  );
}
