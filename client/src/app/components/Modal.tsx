import { ReactNode } from 'react';
import { Dialog } from '@headlessui/react';

export default function Modal({ opened, setOpened, children }: { opened: boolean; setOpened: (opened: boolean) => void; children?: ReactNode }) {
  return (
    <Dialog open={opened} onClose={() => setOpened(false)} className="relative z-50">
      {/* The backdrop, rendered as a fixed sibling to the panel container */}
      <div className="fixed inset-0 bg-black/30" aria-hidden="true" />

      {/* Full-screen container to center the panel */}
      <div className="fixed inset-0 flex items-center justify-center p-4">
        {/* The actual dialog panel  */}
        <Dialog.Panel className="mx-auto max-w-sm rounded-3xl border-2 border-slate-300 bg-white py-5 px-10">{children}</Dialog.Panel>
      </div>
    </Dialog>
  );
}
