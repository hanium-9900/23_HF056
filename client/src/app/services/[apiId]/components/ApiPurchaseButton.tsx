import { useState } from 'react';
import ApiPurchaseModal from './ApiPurchaseModal';
import { Service } from '../../register/types';

export default function ApiPurchaseButton({ service }: { service: Service }) {
  const [opened, setOpened] = useState(false);

  function purchase() {
    // [TODO] 구매 요청 처리
    alert('미구현');
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
