import LogoutButton from './LogoutButton';
import LoginButton from './LoginButton';
import Link from 'next/link';
import { getServerSession } from 'next-auth';
import Image from 'next/image';

export default async function Header() {
  const session = await getServerSession();
  const links = [
    { name: '서비스 등록', href: '/services/register' },
    { name: '서비스 목록', href: '/services' },
  ];

  return (
    <header className="border-b">
      <div className="container mx-auto flex items-center py-2">
        <Link href="/" className="inline-flex items-center text-lg font-bold py-2 px-3 mr-auto">
          <Image className='mr-2' src="/logo.svg" width={28} height={28} alt='데이터 유통 API 플랫폼' />
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
              <span className="bg-white text-black font-bold text-sm rounded-full py-3 px-5">{session.user.email}</span>
              <LogoutButton></LogoutButton>
            </>
          ) : (
            <>
              <LoginButton></LoginButton>
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
