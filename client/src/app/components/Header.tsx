'use client';

import { signIn, signOut, useSession } from 'next-auth/react';
import Link from 'next/link';

export default function Header() {
  const { data: session } = useSession();
  const links = [{ name: '서비스 등록', href: '/services/register' }];

  return (
    <header className="border-b">
      <div className="container mx-auto flex items-center py-2">
        <Link href="/" className="text-lg font-bold py-2 px-3 mr-auto">
          데이터 유통 API 플랫폼
        </Link>
        <nav>
          {links.map(link => (
            <Link key={link.name} href={link.href} className="text-blue-500 text-sm mx-3">
              {link.name}
            </Link>
          ))}
        </nav>
        <span className="inline-block w-px h-7 bg-gray-300 mx-5"></span>
        <nav>
          {session && session.user ? (
            <>
              <button onClick={() => signOut()} className="bg-blue-500 text-white text-sm font-bold rounded-full py-3 px-5">
                로그아웃
              </button>
            </>
          ) : (
            <>
              <button onClick={() => signIn()} className="bg-blue-500 text-white text-sm font-bold rounded-full py-3 px-5">
                로그인
              </button>
              <Link href="/auth/register" className="bg-white text-blue-500 text-sm rounded-full py-3 px-5">
                회원가입
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
}
