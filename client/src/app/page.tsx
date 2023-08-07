import Link from 'next/link';

export default function Home() {
  return (
    <main className="py-10 text-center">
      <div className="text-xl mb-10">메인페이지(미구현)</div>
      <div className="flex flex-col gap-3 text-blue-500 ">
        <Link href="/services/register">- 서비스 등록 페이지</Link>
        <Link href="/services">- 서비스 목록 페이지</Link>
        <Link href="/services/10">- 서비스 조회 페이지(id=10, dummy)</Link>
        <Link href="/dashboard">- 대시보드 페이지</Link>
        <Link href="/dashboard/10">- 대시보드(사용량 조회) 페이지(id=10, dummy)</Link>
      </div>
    </main>
  );
}
