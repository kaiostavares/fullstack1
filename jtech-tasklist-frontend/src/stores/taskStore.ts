import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import taskApiService from '@/services/TaskApiService'
import type { Task, CreateTaskRequest, UpdateTaskRequest } from '@/services/TaskApiService'
import { useToast } from '@/composables/useToast'
import { extractErrorMessage } from '@/utils/errorHandler'

export const useTaskStore = defineStore('task', () => {
  const toast = useToast()
  const tasks = ref<Task[]>([])
  const loading = ref(false)
  const error = ref<string | null>(null)

  // Computed
  const taskCount = computed(() => tasks.value.length)
  
  const tasksByStatus = computed(() => ({
    PENDING: tasks.value.filter(t => t.status === 'PENDING'),
    IN_PROGRESS: tasks.value.filter(t => t.status === 'IN_PROGRESS'),
    COMPLETED: tasks.value.filter(t => t.status === 'COMPLETED'),
  }))

  // Actions
  async function fetchTasks() {
    loading.value = true
    error.value = null
    try {
      tasks.value = await taskApiService.getTasks()
    } catch (err) {
      const message = extractErrorMessage(err)
      error.value = message
      toast.error(message)
      // Não limpa tasks, mantém listagem anterior se houver
    } finally {
      loading.value = false
    }
  }

  async function createTask(task: CreateTaskRequest) {
    loading.value = true
    error.value = null
    try {
      const newTask = await taskApiService.createTask(task)
      tasks.value.push(newTask)
      toast.success('Tarefa criada com sucesso!')
      return newTask
    } catch (err) {
      const message = extractErrorMessage(err)
      error.value = message
      toast.error(message)
      throw err
    } finally {
      loading.value = false
    }
  }

  async function updateTask(id: string, task: UpdateTaskRequest) {
    loading.value = true
    error.value = null
    try {
      const updatedTask = await taskApiService.updateTask(id, task)
      const index = tasks.value.findIndex((t) => t.id === id)
      if (index !== -1) {
        tasks.value[index] = updatedTask
      }
      toast.success('Tarefa atualizada com sucesso!')
      return updatedTask
    } catch (err) {
      const message = extractErrorMessage(err)
      error.value = message
      toast.error(message)
      throw err
    } finally {
      loading.value = false
    }
  }

  async function deleteTask(id: string) {
    loading.value = true
    error.value = null
    try {
      await taskApiService.deleteTask(id)
      tasks.value = tasks.value.filter(t => t.id !== id)
      toast.success('Tarefa excluída com sucesso!')
    } catch (err) {
      const message = extractErrorMessage(err)
      error.value = message
      toast.error(message)
      throw err
    } finally {
      loading.value = false
    }
  }

  return {
    // State
    tasks,
    loading,
    error,
    
    // Computed
    taskCount,
    tasksByStatus,
    
    // Actions
    fetchTasks,
    createTask,
    updateTask,
    deleteTask,
  }
})
