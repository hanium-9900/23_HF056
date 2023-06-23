export interface ServiceInfo {
  title: string;
  description: string;
  price: number;
  key: string;
}

export interface ApiInfo {
  host: string;
  method: string;
  path: string;
  description: string;
  headers: Header[];
  requestParameters: RequestParameter[];
  responseParameters: ResponseParameter[];
  errorCodes: ErrorCode[];
}

export interface Header {
  key: string;
  description: string;
  required: boolean;
}

export interface RequestParameter {
  key: string;
  type: string; // [TODO] enum-like type
  description: string;
  required: boolean;
}

export interface ResponseParameter {
  key: string;
  type: string; // [TODO] enum-like type
  description: string;
  required: boolean;
}

export interface ErrorCode {
  key: string;
  description: string;
}

export interface Service extends ServiceInfo {
  apis: ApiInfo[];
}
