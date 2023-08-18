'use client'

import { useState, useEffect } from 'react';
import ApiPurchaseModal from './ApiPurchaseModal';
import { api, ServiceResponse } from '@/api';
import { toast } from 'react-toastify';
import { useRouter } from 'next/navigation';

export default function ApiPurchaseButton({ service }: { service: ServiceResponse }) {
  const router = useRouter();

  const [opened, setOpened] = useState(false);
  const [buy, setBuy] = useState(false);
  const [sevicekey, setKey] = useState('');

  async function purchase() {
    // [TODO] 구매 요청 처리
    api.services.purchaseService(Number(service.id))
      .then(response => {
        setBuy(response.data);
        console.log(service.id);
        console.log(buy);
        if (buy) {
          const key = api.services.getServiceKey(Number(service.id));
          console.log(key);
        }
        setOpened(false);
        toast.success('구매가 완료되었습니다!')
        router.refresh();
      })
      .catch(error => {
        console.error(error);
      });
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
