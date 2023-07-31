'use client';

import { signOut } from 'next-auth/react';

export default function LogoutButton() {
  return (
    <button onClick={() => signOut()} className="bg-blue-500 text-white text-sm font-bold rounded-full py-3 px-5">
      로그아웃
    </button>
  );
}
