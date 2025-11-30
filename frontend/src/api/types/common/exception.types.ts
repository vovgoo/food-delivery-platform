export interface ExceptionResponse {
  timestamp: string;
  statusCode: number;
  error: string;
  body?: any;
  path: string;
}
