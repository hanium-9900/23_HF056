'use client';

import Link from 'next/link';
import { FormEvent, useRef, useState } from 'react';
import axios from 'axios';
import { api } from '@/api';
import { useRouter } from 'next/navigation';
import { toast } from 'react-toastify';

export default function RegisterForm() {
  const router = useRouter();

  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);
  const passwordConfirmRef = useRef<HTMLInputElement>(null);

  const [loading, setLoading] = useState(false);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (loading) return;

    const email = emailRef.current?.value;
    const password = passwordRef.current?.value;
    const passwordConfirm = passwordConfirmRef.current?.value;

    if (!email || !password) {
      toast.error('이메일과 비밀번호를 입력해주세요!');
      return;
    }
    if (password !== passwordConfirm) {
      toast.error('비밀번호가 일치하지 않습니다!');
      return;
    }

    const credentials = {
      email,
      password,
    };

    const loadingToastId = toast.loading('회원가입 중입니다..',)
    try {
      setLoading(() => true);
      await api.auth.register(credentials);

      toast.update(loadingToastId, { render: "회원가입이 완료되었습니다!", type: "success", autoClose: 3000, isLoading: false });
      router.replace('/auth/login');
    } catch (e) {
      if (axios.isAxiosError<{ message: string }>(e)) {
        if (e.response?.data.message === 'Duplicate Email') {
          toast.update(loadingToastId, { render: "이미 존재하는 이메일입니다!", type: "error", autoClose: 3000, isLoading: false });
        } else {
          toast.update(loadingToastId, { render: "알 수 없는 오류가 발생했습니다!", type: "error", autoClose: 3000, isLoading: false });
        }
      } else {
        toast.update(loadingToastId, { render: "알 수 없는 오류가 발생했습니다!", type: "error", autoClose: 3000, isLoading: false });
      }
    } finally {
      setLoading(() => false);
    }
  }

  return (
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
  );
}
