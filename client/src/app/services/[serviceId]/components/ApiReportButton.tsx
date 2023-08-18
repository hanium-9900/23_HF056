'use client';

import { useState, useEffect, useRef } from 'react';
import ApiPurchaseModal from './ApiPurchaseModal';
import { api, ServiceResponse } from '@/api';
import { toast } from 'react-toastify';
import Modal from '@/app/components/Modal';
import axios from 'axios';

export default function ApiReportButton({ service }: { service: ServiceResponse }) {
  const [opened, setOpened] = useState(false);
  const contentRef = useRef<HTMLTextAreaElement>(null);

  async function report() {
    const content = contentRef.current?.value;
    if (!content?.trim()) {
      toast.error('신고 내용을 입력해주세요!');
      return;
    }

    try {
      await api.services.claim(service.id, content);

      toast.success('신고가 접수되었습니다!');
      setOpened(false);
    } catch (e) {
      if (axios.isAxiosError(e)) {
        toast.error('신고 접수에 실패했습니다! (로그 확인)');
        console.error(e.response?.data);
      }
    }
  }

  return (
    <>
      <button
        className="btn btn-danger shrink-0"
        onClick={() => {
          setOpened(opened => !opened);
        }}
      >
        신고
      </button>
      <Modal opened={opened} setOpened={setOpened}>
        <div className="font-bold text-3xl text-red-500 text-center mb-6">
          <div>서비스 신고</div>
        </div>
        <div className="text-center mb-4">
          <div className="font-bold text-xl mb-4">“{service.title}”</div>
          <div className="font-bold mb-2">신고</div>
          <textarea ref={contentRef} placeholder="내용을 입력해주세요." rows={10}></textarea>
        </div>
        <div className="flex justify-center items-center gap-6">
          <button type="button" className="btn btn-secondary-outline" onClick={() => setOpened(false)}>
            취소
          </button>
          <button
            type="button"
            className="btn btn-danger"
            onClick={() => {
              report();
            }}
          >
            신고
          </button>
        </div>
      </Modal>
    </>
  );
}
