'use client';

import Link from 'next/link';
import { FormEvent, useRef } from 'react';
import { signIn } from 'next-auth/react';
import { useRouter, useSearchParams } from 'next/navigation';
import { toast } from 'react-toastify';

export default function LoginForm() {
  const searchParams = useSearchParams();
  const router = useRouter();

  const emailRef = useRef<HTMLInputElement>(null);
  const passwordRef = useRef<HTMLInputElement>(null);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    const email = emailRef.current?.value;
    const password = passwordRef.current?.value;

    if (!email || !password) {
      toast.error('이메일과 비밀번호를 입력해주세요');
      return;
    }

    const loadingToastId = toast.loading('로그인 중입니다..');
    const result = await signIn('credentials', {
      email: emailRef.current.value,
      password: passwordRef.current.value,
      redirect: false,
    });
    if (result?.error) {
      if (result.error === 'User Not Found') {
        toast.update(loadingToastId, { render: '존재하지 않는 이메일입니다!', type: 'error', autoClose: 3000, isLoading: false });
      } else if (result.error === 'Wrong Password') {
        toast.update(loadingToastId, { render: '비밀번호가 틀렸습니다!', type: 'error', autoClose: 3000, isLoading: false });
      } else {
        toast.update(loadingToastId, { render: '알 수 없는 오류가 발생했습니다!', type: 'error', autoClose: 3000, isLoading: false });
      }
    } else {
      toast.update(loadingToastId, { render: '로그인되었습니다!', type: 'success', autoClose: 3000, isLoading: false });

      router.replace(searchParams.get('callbackUrl') || '/');
      router.refresh();
    }
  }

  return (
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
  );
}
