'use client'

import 'antd/dist/antd.css'
import 'json-schema-editor-visual/dist/main.css'
import './JsonSchemaEditor.css'

// @ts-ignore
import schemaEditor from '@leslieliu/react-jsonschema-editor/dist/main.js';

const SchemaEditor = schemaEditor({});

export default function JsonSchemaEditor({ onChange }: { onChange: (value: any) => void }) {
  return (
    <SchemaEditor
      showEditor={false}
      isMock={false}
      data={JSON.stringify({
        title: "루트",
        type: "object",
        properties: []
      })}
      // @ts-ignore
      onChange={(e) => {
        onChange(e)
      }}
    />
  )
}
