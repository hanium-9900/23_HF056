import { Service } from '@/app/services/register/types';
import axios from './axios';

export interface ServiceResponse {
  id: number;
  category: string | undefined; // [TODO] 카테고리 추가 시 undefined 제거
  title: string;
  description: string;
  price: number;
  key: string;
  apis: {
    id: number;
    host: string;
    description: string;
    method: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
    path: string;
    limitation: number;
    headers: {
      id: number;
      required: 0 | 1;
      key: string;
      type: string; // [TODO] enum
      description: string;
    }[];
    requestParameters: {
      id: number;
      required: 0 | 1;
      key: string;
      type: string; // [TODO] enum
      description: string;
    }[];
    responseParameters: {
      id: number;
      required: 0 | 1;
      key: string;
      type: string; // [TODO] enum
      description: string;
    }[];
    errorCodes: {
      id: number;
      statusCode: number;
      description: string;
    }[];
  }[];
  user: {
    id: number;
    email: string;
  };
}

export interface ServiceStatisticsResponse {
  api_id: number;
  method: string;
  path: string;
  month: number;
  day: number;
  response_code: number;
  count: number;
}

export interface ServiceUsageResponse {
  id: number;
  method: string;
  path: string;
  usage_rate: number;
  limitation: number;
}

export interface ErrorLogResponse {
  id: number;
  method: string;
  path: string;
  response_code: number;
  creation_times: string;
}

export const api = {
  /**
   * 회원관리
   */
  auth: {
    /**
     * 회원가입
     *
     * @param credentials 회원가입 정보
     */
    register(credentials: { email: string; password: string }) {
      return axios.post('/users/join', credentials);
    },
    /**
     * 로그인
     *
     * @param credentials 로그인 정보
     */
    login(credentials: { email: string; password: string }) {
      return axios.post<string>('/users/login', credentials);
    },
    /**
     * 사용자 정보 가져오기
     *
     * @param token 토큰 (로그인 과정에만 필요)
     */
    me(token?: string) {
      if (token) {
        return axios.get<{ id: string; email: string }>('/users/me', {
          headers: {
            'X-AUTH-TOKEN': token,
          },
        });
      } else {
        return axios.get<{ id: string; email: string }>('/users/me');
      }
    },
  },
  /**
   * 서비스
   */
  services: {
    /**
     * 서비스 등록
     */
    register(service: any) {
      return axios.post('/services', service);
    },
    /**
     * 서비스 수정
     */
    update(serviceId: number, service: any) {
      return new Promise((res, rej) => {
        alert('서비스 수정 API가 구현되지 않았습니다.');
        rej(null);
      });
    },
    /**
     * 서비스 목록 조회
     */
    list(category?: string) {
      return axios.get<ServiceResponse[]>(`/services${category ? `?category=${category}` : ''}`);
    },
    /**
     * 서비스 상세 조회
     */
    show(serviceId: number) {
      return axios.get<ServiceResponse>(`/services/${serviceId}`);
    },
    /**
     * 서비스 신고
     */
    claim(serviceId: number, content: string) {
      // 신고
    },
    /**
     * 서비스 삭제
     */
    delete(serviceId: number) {
      return axios.delete(`/services/${serviceId}`);
    },
    /**
     * 서비스 사용량 통계
     */
    statistics(serviceId: number, year?: number, month?: number) {
      const calculatedYear = year || new Date().getFullYear();
      const calculatedMonth = month || new Date().getMonth() + 1;

      return axios.get<ServiceStatisticsResponse[]>(`/services/${serviceId}/statistics?year=${calculatedYear}&month=${calculatedMonth}`);
    },
    /**
     * 평균 사용량 퍼센트
     */
    usage(serviceId: number) {
      const now = new Date();
      const year = now.getFullYear();
      const month = now.getMonth() + 1;
      const day = now.getDate();

      return axios.get<ServiceUsageResponse[]>(`/services/${serviceId}/usage-rate?year=${year}&month=${month}&day=${day}`);
    },
    /**
     * 에러 로그
     */
    errorLogs(serviceId: number, limit: number = 10) {
      return axios.get<ErrorLogResponse[]>(`/services/${serviceId}/error-log?limit=${limit}`);
    },
    /**
     * 내가 등록한 서비스 목록 조회
     */
    registeredList() {
      return axios.get<ServiceResponse[]>(`/services/registered`);
    },
    /**
     * 내가 구매한 서비스 목록 조회
     */
    purchasedList() {
      return axios.get<ServiceResponse[]>('/services/purchased');
    },
  },
  /**
   * 에러 로그
   */
  errorLogs: {
    /**
     * 에러 로그 목록 조회
     */
    list(serviceId: number) {
      // 목록 조회
    },
  },
};
