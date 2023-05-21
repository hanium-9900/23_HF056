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
  parameters: RequestParameter[];
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
  description: string;
  required: boolean;
}

export interface ResponseParameter {
  key: string;
  description: string;
  required: boolean;
}

export interface ErrorCode {
  key: string;
  description: string;
}