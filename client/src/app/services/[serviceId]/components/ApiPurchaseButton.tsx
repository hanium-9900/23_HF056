import { useState, useEffect } from 'react';
import ApiPurchaseModal from './ApiPurchaseModal';
import { api, ServiceResponse } from '@/api';

export default function ApiPurchaseButton({ service }: { service: ServiceResponse }) {
  const [opened, setOpened] = useState(false);
  const [buy, setBuy]=useState(false);
  const [sevicekey, setKey]=useState('');

  
    

  function purchase() {
    // [TODO] 구매 요청 처리
    api.services.purchaseService(Number(service.id))
      .then(response => {
        setBuy(response.data);
      })
      .catch(error => {
        console.error(error);
      });
    console.log(service.id);
    alert(buy);
    if(buy){
    const key=api.services.getServiceKey(Number(service.id));
    console.log(key);
  }
    setOpened(false);
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
