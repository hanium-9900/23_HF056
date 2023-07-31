import './page.css';
import ServiceRegisterForm from './components/ServiceRegisterForm';

export default async function Register() {
  return (
    <main className="max-w-3xl mx-auto py-14">
      <h1 className="text-3xl font-bold text-center mb-7">서비스 등록</h1>
      <ServiceRegisterForm />
    </main>
  );
}
