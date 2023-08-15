import { useState } from 'react';
import { toast } from 'react-toastify';
import JsonSchemaEditor from '@/app/components/JsonSchemaEditor';

export default function ApiRequestParametersInput({
  requestParameters,
  onChange,
}: {
  requestParameters: string;
  onChange: (requestParameters: string) => void;
}) {
  const [value, setValue] = useState<string>(requestParameters);

  return (
    <label className="block mb-6">
      <div className="font-bold mb-2">요청 파라미터</div>
      <div onClick={e => e.preventDefault() /** hack: prevent weird 'import json' action in form */}>
        <JsonSchemaEditor value={value} onChange={schema => {
          setValue(schema)
          onChange(schema)
        }} />
      </div>
    </label>
  );
}
