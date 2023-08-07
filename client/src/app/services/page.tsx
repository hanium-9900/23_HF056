import ServiceList from './components/ServiceList';
import CategoryLinks from './components/CategoryLinks';

export default async function Page() {
  return (
    <main className="max-w-7xl mx-auto py-14 px-4">
      <h1 className="text-3xl font-bold text-center mb-12">서비스 목록</h1>

      <CategoryLinks />
      <ServiceList />
    </main>
  );
}
