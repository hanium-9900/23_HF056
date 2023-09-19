'use client';

interface JsonSchemaProperty {
  title?: string;
  description?: string;
  default?: any;
}

interface JsonSchemaInteger extends JsonSchemaProperty {
  type: 'integer';
  minimum?: number;
  maximum?: number;
  exclusiveMinimum?: number;
  exclusiveMaximum?: number;
  enum?: number[];
  enumDesc?: string;
}

interface JsonSchemaNumber extends JsonSchemaProperty {
  type: 'number';
  minimum?: number;
  maximum?: number;
  exclusiveMinimum?: number;
  exclusiveMaximum?: number;
  enum?: number[];
  enumDesc?: string;
}

interface JsonSchemaString extends JsonSchemaProperty {
  type: 'string';
  minLength?: number;
  maxLength?: number;
  pattern?: string;
  enum?: string[];
  enumDesc?: string;
  format?: string;
}

interface JsonSchemaArray extends JsonSchemaProperty {
  type: 'array';
  minItems?: number;
  maxItems?: number;
  uniqueItems?: boolean;
  items: JsonSchemaRoot;
}

interface JsonSchemaBoolean extends JsonSchemaProperty {
  type: 'boolean';
}

interface JsonSchemaNull extends JsonSchemaProperty {
  type: 'null';
}

interface JsonSchemaObject extends JsonSchemaProperty {
  type: 'object';
  properties: {
    [key: string]: JsonSchemaRoot;
  };
  required?: string[];
}

// [TODO] 루트 스키마 Object 아니어도 되도록 수정
// type JsonSchemaRoot = JsonSchemaObject;
type JsonSchemaRoot = JsonSchemaInteger | JsonSchemaNumber | JsonSchemaString | JsonSchemaObject | JsonSchemaArray | JsonSchemaBoolean | JsonSchemaNull;

function getArrayDisplayName(schema: JsonSchemaArray): string {
  if (schema.items.type === 'object') {
    return `${schema.items.title || '(이름 없음)'}[]`
  } else if (schema.items.type === 'array') {
    return getArrayDisplayName(schema.items) + '[]'
  } else {
    return `${schema.items.type}[]`
  }
}

function JsonSchemaRow({ schema, required = false, propertyKey }: { schema: JsonSchemaRoot, required?: boolean, propertyKey: string }) {
  const displayName = schema.title || propertyKey;

  let displayType: string = schema.type;
  if (schema.type === 'object') {
    displayType = `${displayName}`
  } else if (schema.type === 'array') {
    displayType = getArrayDisplayName(schema)
  }

  return (
    <tr>
      <td>{required ? 'O' : 'X'}</td>
      <td>{propertyKey}</td>
      <td>{schema.title ?? '(이름 없음)'}</td>
      <td>{displayType}</td>
      <td>{schema.description ?? '(설명 없음)'}</td>
    </tr>
  )
}

function JsonSchemaTable({ schema, required = false, propertyKey = '응답' }: { schema: JsonSchemaObject, required?: boolean, propertyKey?: string }) {
  return (
    <>
      <div className="mb-7">
        <div className="mb-3">
          <div className="text-xl font-bold">{propertyKey}</div>
          <div className="text-sm text-gray-500">{schema.description}</div>
        </div>
        <table>
          <thead>
            <tr className="bg-slate-50 border-slate-300 border">
              <th>필수</th>
              <th>키</th>
              <th>이름</th>
              <th>타입</th>
              <th>설명</th>
            </tr>
          </thead>
          <tbody>
            {Object.keys(schema.properties).map(key => (
              <JsonSchemaRow key={key} schema={schema.properties[key]} required={schema.required?.includes(key)} propertyKey={key} />
            ))}
          </tbody>
        </table>
      </div>
      {/* [TODO] 다연속 Array 제대로 재귀적으로 처리 */}
      {Object.keys(schema.properties).filter(key => {
        const prop = schema.properties[key];
        return (
          prop.type === 'object'
          || (prop.type === 'array' && prop.items.type === 'object')
        )
      }).map<[string, JsonSchemaObject]>(key => {
        const prop = schema.properties[key] as JsonSchemaObject | JsonSchemaArray;
        if (prop.type === 'array') {
          return [getArrayDisplayName(prop).replaceAll('[]', ''), prop.items as JsonSchemaObject];
        }
        return [key, prop];
      }).map(([key, obj]) => (
        <JsonSchemaTable schema={obj} propertyKey={obj.title || key} key={key} required={schema.required?.includes(key)} />
      ))}
    </>
  )
}

export default function JsonSchemaSpec({ schema }: { schema: JsonSchemaRoot }) {
  if (schema.type === 'object') {
    return (
      <div>
        <JsonSchemaTable schema={schema} required={true} propertyKey={schema.title} />
      </div>
    );
  } else {
    return (
      <div>아직 지원하지 않는 Schema(루트가 Object가 아닌 경우)</div>
    )
  }
}
