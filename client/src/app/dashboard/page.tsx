import { ServiceResponse, api } from '@/api'
import ServiceListRenderer from '../components/ServiceListRenderer'
import axios from 'axios'

export default async function Page() {
  const { data: registeredServices } = await api.services.registeredList()
  // const { data: purchasedServices } = await api.services.purchasedList()

  // [TODO] 서버 API 완료시 제거
  let purchasedServices: ServiceResponse[] = []
  try {
    const { data } = await api.services.purchasedList()
    purchasedServices = data
  } catch (e) {
    purchasedServices = []
  }

  return (
    <main className="max-w-7xl mx-auto py-10 px-3">
      <h1 className="text-3xl font-bold mb-7">대시보드</h1>

      <div className="mb-12">
        <h2 className="text-xl font-bold mb-7">내가 등록한 서비스</h2>
        <ServiceListRenderer isDashboard={true} services={registeredServices} />
      </div>

      <div className="mb-12">
        <h2 className="text-xl font-bold mb-7">내가 구매한 서비스</h2>
        <ServiceListRenderer services={purchasedServices} />
      </div>

    </main>
  )
}
