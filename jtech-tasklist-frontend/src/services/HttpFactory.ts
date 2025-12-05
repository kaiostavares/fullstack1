import axios from 'axios'
import type { AxiosInstance, AxiosRequestConfig } from 'axios'

interface HttpFactoryConfig {
  baseURL: string
  timeout?: number
  headers?: Record<string, string>
}

class HttpFactory {
  private instance: AxiosInstance

  constructor(config: HttpFactoryConfig) {
    this.instance = axios.create({
      baseURL: config.baseURL,
      timeout: config.timeout || 10000,
      headers: {
        'Content-Type': 'application/json',
        ...config.headers,
      },
    })

    // Interceptor para requisições
    this.instance.interceptors.request.use(
      (config) => {
        return config
      },
      (error) => {
        return Promise.reject(error)
      }
    )

    // Interceptor para respostas
    this.instance.interceptors.response.use(
      (response) => {
        return response
      },
      (error) => {
        if (error.response?.status === 404) {
          console.error('Recurso não encontrado')
        } else if (error.response?.status === 500) {
          console.error('Erro interno do servidor:', error.response?.data)
        }
        return Promise.reject(error)
      }
    )
  }

  get<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.get<T>(url, config).then((response) => response.data)
  }

  post<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.post<T>(url, data, config).then((response) => response.data)
  }

  put<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.put<T>(url, data, config).then((response) => response.data)
  }

  patch<T>(url: string, data?: unknown, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.patch<T>(url, data, config).then((response) => response.data)
  }

  delete<T>(url: string, config?: AxiosRequestConfig): Promise<T> {
    return this.instance.delete<T>(url, config).then((response) => response.data)
  }
}

export default HttpFactory
