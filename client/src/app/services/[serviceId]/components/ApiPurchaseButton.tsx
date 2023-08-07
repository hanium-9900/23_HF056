import { useState } from 'react';
import ApiPurchaseModal from './ApiPurchaseModal';
import { Service } from '../../register/types';
import { api, purchase } from '@/api';

export default function ApiPurchaseButton({ service }: { service: Service }) {
  const [opened, setOpened] = useState(false);

  async function purchase() {
    try {
      // [TODO] 구매 요청 처리
      const request = await api.services.purchase();
     
      if(request){
        const request = await api.services.key();
        serviceId: serviceId,
      }
      else{
        alert('구매에 실패했습니다. 다시 시도해주세요.');
        setOpened(false);
      }

     
    } catch (error) {
     
      alert('구매에 실패했습니다. 다시 시도해주세요.');
      console.error(error);
      setOpened(false);
    }
  }

  function cancel() {
    setOpened(false);
  }

  return (
    <>
      <button
        className="btn btn-form shrink-0"
        onClick={() => {
          setOpened(opened => !opened);
        }}
      >
        구매
      </button>
      <ApiPurchaseModal service={service} opened={opened} onCancel={cancel} onPurchase={purchase} />
    </>
  );
}
