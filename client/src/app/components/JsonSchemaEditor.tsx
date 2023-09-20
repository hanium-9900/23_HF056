'use client';

import './JsonSchemaEditor.scss';
import dynamic from 'next/dynamic';
import { useEffect, useState } from 'react';

export default function JsonSchemaEditor({ value, onChange }: { value?: string; onChange: (value: any) => void }) {
  const [SchemaEditor, setSchemaEditor] = useState<any>(null);

  useEffect(() => {
    const SchemaEditor = dynamic<any>(
      () =>
        // @ts-ignore
        import('@leslieliu/react-jsonschema-editor/dist/main.js').then(obj => obj.default({})),
      {
        ssr: false,
      }
    );
    setSchemaEditor(() => SchemaEditor);
  }, []);

  if (!SchemaEditor) {
    return <div>loading...</div>;
  } else {
    return (
      <div className="json-schema-editor-container">
        <SchemaEditor
          showEditor={false}
          isMock={false}
          data={
            value ||
            JSON.stringify({
              title: '루트',
              description: 'JSON 최상위 객체',
              type: 'object',
              properties: {},
            })
          }
          // @ts-ignore
          onChange={e => {
            onChange(e);
          }}
        />
      </div>
    );
  }
}
