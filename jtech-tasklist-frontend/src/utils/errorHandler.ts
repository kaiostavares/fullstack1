import type { AxiosError } from 'axios'

interface ApiErrorResponse {
  message?: string
  error?: string
  errors?: string[]
  detail?: string
}

export function extractErrorMessage(error: unknown): string {
  if (!error) return 'Erro desconhecido'

  // Se for AxiosError
  const axiosError = error as AxiosError<ApiErrorResponse>
  
  if (axiosError.response?.data) {
    const data = axiosError.response.data
    
    // Tenta extrair a mensagem em diferentes formatos comuns de API
    if (data.message) return data.message
    if (data.error) return data.error
    if (data.detail) return data.detail
    if (data.errors && Array.isArray(data.errors) && data.errors.length > 0) {
      return data.errors.join(', ')
    }
  }

  // Se tiver mensagem de status HTTP
  if (axiosError.response?.statusText) {
    return `${axiosError.response.status}: ${axiosError.response.statusText}`
  }

  // Se for Error comum
  if (error instanceof Error) {
    return error.message
  }

  // Fallback
  return 'Erro ao processar requisição'
}
