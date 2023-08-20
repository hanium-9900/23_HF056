import './globals.css';
import { Noto_Sans_KR as NotoSansKR } from 'next/font/google';
import AuthProvider from './components/AuthProvider';
import Header from './components/Header';
import ToastProvider from './components/ToastProvider';

const notoSansKR = NotoSansKR({ weight: ['100', '300', '400', '500', '700', '900'], subsets: ['latin'], preload: false });

export const metadata = {
  title: '데이터 유통 API 플랫폼',
  description: '자동화된 명세 검수 기반 데이터 유통 API 플랫폼',
};

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="ko">
      <body>
        <AuthProvider>
          <ToastProvider>
            {/* @ts-expect-error Server Component */}
            <Header></Header>
            {children}
          </ToastProvider>
        </AuthProvider>
      </body>
    </html>
  );
}
