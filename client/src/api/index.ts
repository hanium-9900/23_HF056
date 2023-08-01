import { Service } from '@/app/services/register/types';
import axios from './axios';

interface ServiceListItemResponse {
  id: number;
  title: string;
  description: string;
  price: number;
  [key: string]: any;
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
    update(service: any) {
      // 수정
    },
    /**
     * 서비스 목록 조회
     */
    list() {
      return axios.get<ServiceListItemResponse[]>('/services');
    },
    /**
     * 서비스 상세 조회
     */
    show(serviceId: number) {
      return axios.get(`/services/${serviceId}`);
    },
    /**
     * 서비스 신고
     */
    claim(serviceId: number, content: string) {
      // 신고
    },
  },
  /**
   * 사용량
   */
  limitations: {
    /**
     * 사용량 조회
     */
    show(serviceId: number) {
      // 조회
    },
    /**
     * 사용량 제한 설정
     */
    update(serviceId: number, limitation: any) {
      // 수정/설정
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
