import './page.css';
import Link from 'next/link';

export default function Register() {
  return (
    <main className="max-w-md mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">로그인</h1>
      <form>
        <div className="mb-4">
          <label htmlFor="email" className="inline-block font-bold mb-2">
            이메일
          </label>
          <input id="email" type="email" name="email" required />
        </div>
        <div className="mb-4">
          <label htmlFor="password" className="inline-block font-bold mb-2">
            비밀번호
          </label>
          <input id="password" type="password" name="password" required />
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
