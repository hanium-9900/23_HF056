import { Service } from '../../register/types';

export default function ApiPurchaseModal({
  opened,
  service,
  onCancel,
  onPurchase,
}: {
  opened: boolean;
  service: Service;
  onCancel: () => void;
  onPurchase: () => void;
}) {
  if (!opened) return null;
  return (
    <div className="fixed top-0 left-0 w-full h-full flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white rounded-xl py-6 px-12 text-center">
        <h1 className="text-3xl font-bold mb-6">서비스 구매</h1>
        <div className="mb-6">
          <div className="text-2xl text-blue-500 font-bold mb-3">{service.price.toLocaleString('ko-KR')} &#8361;</div>
          <div className="text-lg">정말 구매하시겠습니까?</div>
        </div>
        <div className="flex items-center justify-center gap-3">
          <button className="btn btn-secondary" onClick={onCancel}>
            취소
          </button>
          <button className="btn btn-form" onClick={onPurchase}>
            구매
          </button>
        </div>
      </div>
    </div>
  );
}
