'use client'

import { useState } from "react";
import JsonSchemaEditor from "../components/JsonSchemaEditor";
import JsonSchemaSpec from "../components/JsonSchemaSpec";

export default function Page() {
  const [schema, setSchema] = useState<any>(null)

  return (
    <div className="max-w-4xl mx-auto">
      <div className="mb-6">
        <JsonSchemaEditor onChange={(e) => {
          const schema = JSON.parse(e)
          setSchema(schema)
        }} />
      </div>
      {schema && (
        <div>
          <div className="mb-2 text-xl font-bold">결과 Schema</div>
          <pre className="p-2 rounded bg-gray-200">
            <code>{JSON.stringify(schema, undefined, 2)}</code>
          </pre>
          <JsonSchemaSpec schema={schema} />
        </div>
      )}
    </div >
  )
}
