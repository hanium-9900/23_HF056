'use client';

import './page.css';
import Link from 'next/link';
import { FormEvent, useRef } from 'react';
import axios from 'axios';

export default function Register() {
  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);
  const passwordConfirmRef = useRef<HTMLInputElement>(null);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const email = emailRef.current?.value;
    const password = passwordRef.current?.value;
    const passwordConfirm = passwordConfirmRef.current?.value;

    if (!email || !password) {
      alert('이메일과 비밀번호를 입력해주세요');
      return;
    }
    if (password !== passwordConfirm) {
      alert('비밀번호가 일치하지 않습니다');
      return;
    }

    const credentials = {
      email,
      password,
    };

    alert(JSON.stringify(credentials, undefined, 2));

    const { data } = await axios.post('http://3.34.215.14:8080/users/join', credentials);

    alert(data);
  }

  return (
    <main className="max-w-md mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">회원가입</h1>
      <form onSubmit={submit}>
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
        <div className="mb-4">
          <label htmlFor="password_confirm" className="inline-block font-bold mb-2">
            비밀번호 확인
          </label>
          <input ref={passwordConfirmRef} id="password_confirm" type="password" name="password_confirm" required />
        </div>
        <div className="flex justify-end">
          <Link href="/auth/login" className="btn btn-form-outline mr-3">
            로그인
          </Link>
          <button className="btn btn-form">회원가입</button>
        </div>
      </form>
    </main>
  );
}
