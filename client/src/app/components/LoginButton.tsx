'use client';

// import { signIn } from 'next-auth/react';
import Link from 'next/link';

export default function LoginButton() {
  return (
    <Link href="/auth/login" className="bg-blue-500 text-white text-sm font-bold rounded-full py-3 px-5">
      로그인
    </Link>
    // <button onClick={() => signIn()} className="bg-blue-500 text-white text-sm font-bold rounded-full py-3 px-5">
    //   로그인
    // </button>
  );
}
