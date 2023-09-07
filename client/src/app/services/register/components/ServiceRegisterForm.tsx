'use client';

import { useEffect, useState } from 'react';
import { ApiInfo, ServiceInfo } from '../types';
import ApiSpecificationInput from './ApiSpecificationInput';
import axios from 'axios';
import { api, ServiceResponse } from '@/api';
import { useRouter } from 'next/navigation';
import Modal from '@/app/components/Modal';
import { toast } from 'react-toastify';
import { categories } from '@/config/category';

interface ServiceRegisterFormProps {
  service?: ServiceResponse;
}

export default function ServiceRegisterForm({ service }: ServiceRegisterFormProps) {
  const router = useRouter();

  const [modalOpened, setModalOpened] = useState(false);
  const [info, setInfo] = useState<ServiceInfo>({
    category: service?.category || '기타',
    title: service?.title || '',
    description: service?.description || '',
    price: service?.price === undefined ? -1 : service.price, // bugfix: price가 0일 경우 -1이 되는 문제 수정
    key: service?.key || '',
  });
  const [apiList, setApiList] = useState<ApiInfo[]>(
    service?.apis?.map(api => ({
      host: api.host || '',
      method: api.method || 'GET',
      path: api.path || '',
      description: api.description || '',
      limitation: api.limitation === undefined ? -1 : api.limitation, // bugfix: limitation 0일 경우 -1이 되는 문제 수정
      headers:
        api.headers?.map(h => ({
          key: h.key,
          description: h.description,
          required: h.required === 1,
        })) || [],
      requestParameters: api.requestParameters || '',
      responseParameters: api.responseParameters || '',
      errorCodes:
        api.errorCodes?.map(ec => ({
          statusCode: ec.statusCode,
          description: ec.description,
        })) || [],
    })) || []
  );

  function updateInfo(data: Partial<ServiceInfo>) {
    setInfo({ ...info, ...data });
  }

  function addApi() {
    setApiList([
      ...apiList,
      {
        host: '',
        method: 'GET',
        path: '',
        description: '',
        limitation: -1,
        headers: [],
        requestParameters: '',
        responseParameters: '',
        errorCodes: [],
      },
    ]);
  }

  function removeApi(idx: number) {
    const updatedApiList = [...apiList];
    updatedApiList.splice(idx, 1);

    setApiList(updatedApiList);
  }

  function updateApi(idx: number, api: Partial<ApiInfo>) {
    const updatedApiList = [...apiList];
    updatedApiList[idx] = { ...updatedApiList[idx], ...api };

    setApiList(updatedApiList);
  }

  const openSubmitModal = () => {
    if (info.title.trim() === '' || info.price === -1 || info.key.trim() === '') {
      toast.error('서비스 기본 정보(제목, 가격, Key)를 모두 입력해주세요!');
      return;
    }
    if (apiList.length === 0) {
      toast.error('API를 최소 1개 이상 등록해주세요!');
      return;
    }
    for (const api of apiList) {
      if (api.host.trim() === '' || api.path.trim() === '') {
        toast.error('API 기본 정보(Host, Path)를 모두 입력해주세요!');
        return;
      }
      if (api.limitation === -1) {
        toast.error('API 일일 호출 제한 횟수를 입력해주세요!');
        return;
      }
    }

    setModalOpened(() => true);
  };

  const submitForm = async (event: { preventDefault: () => void }) => {
    event.preventDefault();

    // [TODO] 서버쪽 required 가 int라서 bool -> int 매핑 과정이 필요.
    // 추후 서버쪽 required 타입 변경 검토.
    const serviceData = {
      ...info,
      apis: apiList.map(api => ({
        ...api,
        headers: api.headers.map(header => ({
          ...header,
          required: 1,
        })),
        requestParameters: api.requestParameters,
        responseParameters: api.responseParameters,
      })),
    };

    if (!service) {
      try {
        const { data } = await api.services.register(serviceData);

        toast.success(`서비스 등록이 완료되었습니다!`);

        router.replace(`/services/${data.id}`);
        router.refresh();
      } catch (e) {
        if (axios.isAxiosError(e)) {
          if (e.response?.status === 400) {
            toast.error('명세와 실제 API가 일치하지 않습니다!');
          } else if (e.response?.status === 500) {
            toast.error('서버 오류가 발생했습니다!\n(status: 500)');
          } else {
            toast.error('알 수 없는 Axios 오류가 발생했습니다!');
          }
        } else {
          toast.error('알 수 없는 오류가 발생했습니다!');
        }
      }
    } else {
      try {
        await api.services.update(service.id, serviceData);

        toast.success('서비스 수정이 완료되었습니다!');

        router.replace(`/services/${service.id}`);
        router.refresh();
      } catch (e) {
        if (axios.isAxiosError(e)) {
          if (e.response?.status === 400) {
            toast.error('명세와 실제 API가 일치하지 않습니다!');
          } else if (e.response?.status === 500) {
            toast.error('서버 오류가 발생했습니다!\n(status: 500)');
          } else {
            toast.error('알 수 없는 Axios 오류가 발생했습니다!');
          }
        } else {
          toast.error('알 수 없는 오류가 발생했습니다!');
        }
      }
    }
  };

  useEffect(() => {
    if (!service) {
      addApi();
    }
  }, []);

  return (
    <>
      <form
        className="rounded border border-slate-300 p-7"
        onSubmit={e => {
          e.preventDefault();
          openSubmitModal();
        }}
      >
        {/* 서비스 명세 */}
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 이름</div>
          <select onChange={e => updateInfo({ category: e.target.value })} value={info.category} required>
            {categories.map(category => (
              <option key={category} value={category}>
                {category}
              </option>
            ))}
          </select>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 이름</div>
          <input type="text" placeholder="서비스 이름을 입력하세요" onChange={e => updateInfo({ title: e.target.value })} value={info.title} required />
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 설명</div>
          <textarea
            rows={5}
            placeholder="서비스 설명을 입력하세요"
            onChange={e => updateInfo({ description: e.target.value })}
            value={info.description}
            required
          ></textarea>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">서비스 가격</div>
          <div className="flex items-center">
            <input type="number" placeholder="30000" onChange={e => updateInfo({ price: Number.parseInt(e.target.value) })} value={info.price} required />
            <span className="ml-3">원</span>
          </div>
        </label>
        <label className="block mb-6">
          <div className="font-bold mb-2">API 키</div>
          <input type="password" onChange={e => updateInfo({ key: e.target.value })} value={info.key} required />
          <div className="text-xs text-blue-500 mt-1">API 키는 요청 시 X-API-KEY 헤더에 담아 보내집니다.</div>
        </label>
        {/* API 명세 */}
        <div className="block mb-6">
          <div className="font-bold mb-12">API 명세</div>

          <hr className="mb-12" />

          {/* API 목록 */}
          {apiList.map((api, idx) => (
            <ApiSpecificationInput key={idx} no={idx} api={api} removeApi={removeApi} onChange={updatedApi => updateApi(idx, updatedApi)} />
          ))}
        </div>

        <div className="flex justify-end mb-12">
          <button type="button" className="btn btn-form" onClick={addApi}>
            API 추가
          </button>
        </div>

        <hr className="mb-12" />

        <div className="flex justify-end">
          <button type="submit" className="btn btn-form">
            서비스 {service ? '수정' : '등록'}
          </button>
        </div>
      </form>
      <Modal opened={modalOpened} setOpened={setModalOpened}>
        <div className="font-bold text-3xl text-center mb-6">
          <div>서비스 {service ? '수정' : '등록'}</div>
        </div>
        <div className="text-center mb-4">
          <div className="font-bold text-xl mb-2">“{info.title}”</div>
          <div className="font-bold text-blue-500 font-xl mb-2">{Intl.NumberFormat('ko-KR').format(info.price)}원</div>
          <div className="font-light">
            <span className="text-blue-500 font-bold">{apiList.length}</span>개의 API 포함
          </div>
        </div>
        <div className="flex justify-center items-center gap-6">
          <button
            type="button"
            className="btn btn-secondary-outline"
            onClick={() => {
              setModalOpened(false);
            }}
          >
            취소
          </button>
          <button
            type="button"
            className="btn btn-form"
            onClick={e => {
              submitForm(e);
              setModalOpened(false);
            }}
          >
            {service ? '수정' : '등록'}
          </button>
        </div>
      </Modal>
    </>
  );
}
