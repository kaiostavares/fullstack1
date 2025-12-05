import HttpFactory from './HttpFactory'

export type TaskStatus = 'PENDING' | 'IN_PROGRESS' | 'COMPLETED'

export interface Task {
  id: string
  name: string
  description: string
  status: TaskStatus
  createdAt: string
  updatedAt: string
}

interface PageResponse<T> {
  content: T[]
  pageable?: unknown
  totalPages?: number
  totalElements?: number
  last?: boolean
  size?: number
  number?: number
}

export interface CreateTaskRequest {
  name: string
  description: string
  status: TaskStatus
}

export interface UpdateTaskRequest {
  name: string
  description: string
  status: TaskStatus
}

class TaskApiService {
  private http: HttpFactory

  constructor() {
    const baseURL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8081/api/v1'
    this.http = new HttpFactory({
      baseURL,
    })
  }

  async getTasks(): Promise<Task[]> {
    try {
      const response = await this.http.get<PageResponse<Task> | Task[]>('/tasks?size=1000')
      // Se vier uma resposta paginada do Spring
      if (response && typeof response === 'object' && 'content' in response && Array.isArray(response.content)) {
        return response.content
      }
      // Se vier um array direto
      if (Array.isArray(response)) {
        return response
      }
      return []
    } catch (error) {
      // Se for 404, retorna array vazio (não há tarefas cadastradas)
      if ((error as { response?: { status?: number } })?.response?.status === 404) {
        return []
      }
      console.error('Erro ao buscar tasks:', error)
      throw error
    }
  }

  async getTaskById(id: string): Promise<Task> {
    try {
      return await this.http.get<Task>(`/tasks/${id}`)
    } catch (error) {
      console.error(`Erro ao buscar task ${id}:`, error)
      throw error
    }
  }

  async createTask(task: CreateTaskRequest): Promise<Task> {
    try {
      return await this.http.post<Task>('/tasks', task)
    } catch (error) {
      console.error('Erro ao criar task:', error)
      throw error
    }
  }

  async updateTask(id: string, task: UpdateTaskRequest): Promise<Task> {
    try {
      return await this.http.put<Task>(`/tasks/${id}`, task)
    } catch (error) {
      console.error('Erro ao atualizar task:', error)
      throw error
    }
  }

  async deleteTask(id: string): Promise<void> {
    try {
      await this.http.delete(`/tasks/${id}`)
    } catch (error) {
      console.error(`Erro ao deletar task ${id}:`, error)
      throw error
    }
  }
}

export default new TaskApiService()
