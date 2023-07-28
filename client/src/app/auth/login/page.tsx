'use client';

import './page.css';
import Link from 'next/link';
import { FormEvent, useRef } from 'react';
import axios from 'axios';

export default function Register() {
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const email = emailRef.current?.value;
    const password = passwordRef.current?.value;

    if (!email || !password) {
      alert('이메일과 비밀번호를 입력해주세요');
      return;
    }

    const credentials = {
      email,
      password,
    };

    alert(JSON.stringify(credentials, undefined, 2));

    const { data } = await axios.post('http://3.34.215.14:8080/users/login', credentials);

    alert(data);
  }

  return (
    <main className="max-w-md mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">로그인</h1>
      <form onSubmit={e => submit(e)}>
        <div className="mb-4">
          <label htmlFor="email" className="inline-block font-bold mb-2">
            이메일
          </label>
          <input ref={emailRef} id="email" type="email" name="email" required />
        </div>
        <div className="mb-4">
          <label htmlFor="password" className="inline-block font-bold mb-2">
            비밀번호
          </label>
          <input ref={passwordRef} id="password" type="password" name="password" required />
        </div>
        <div className="flex justify-end">
          <Link href="/auth/register" className="btn btn-form-outline mr-3">
            회원가입
          </Link>
          <button className="btn btn-form">로그인</button>
        </div>
      </form>
    </main>
  );
}
