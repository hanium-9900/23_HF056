import './page.css';
import Link from 'next/link';

export default function Register() {
  return (
    <main className="max-w-md mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">회원가입</h1>
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
        <div className="mb-4">
          <label htmlFor="password_confirm" className="inline-block font-bold mb-2">
            비밀번호 확인
          </label>
          <input id="password_confirm" type="password" name="password_confirm" required />
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
