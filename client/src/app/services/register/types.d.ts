export interface ServiceInfo {
  category: string;
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
  limitation: number;
  headers: Header[];
  requestParameters: string;
  responseParameters: string;
  errorCodes: ErrorCode[];
}

export interface Header {
  key: string;
  description: string;
  required: boolean;
}

export interface ErrorCode {
  statusCode: number;
  description: string;
}

export interface Service extends ServiceInfo {
  apis: ApiInfo[];
}
