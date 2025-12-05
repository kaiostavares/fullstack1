import { ref } from 'vue'
import type { ToastType } from '@/components/ToastNotification.vue'

interface ToastState {
  show: boolean
  message: string
  type: ToastType
}

const state = ref<ToastState>({
  show: false,
  message: '',
  type: 'info'
})

let timeoutId: ReturnType<typeof setTimeout> | null = null

export function useToast() {
  const showToast = (message: string, type: ToastType = 'info', duration = 3000) => {
    // Limpa timeout anterior se existir
    if (timeoutId) {
      clearTimeout(timeoutId)
    }

    state.value = {
      show: true,
      message,
      type
    }

    timeoutId = setTimeout(() => {
      state.value.show = false
    }, duration)
  }

  const hideToast = () => {
    state.value.show = false
    if (timeoutId) {
      clearTimeout(timeoutId)
      timeoutId = null
    }
  }

  const success = (message: string, duration?: number) => {
    showToast(message, 'success', duration)
  }

  const error = (message: string, duration?: number) => {
    showToast(message, 'error', duration)
  }

  const warning = (message: string, duration?: number) => {
    showToast(message, 'warning', duration)
  }

  const info = (message: string, duration?: number) => {
    showToast(message, 'info', duration)
  }

  return {
    state,
    showToast,
    hideToast,
    success,
    error,
    warning,
    info
  }
}
