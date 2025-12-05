<script setup lang="ts">
import { computed } from 'vue'

export type ToastType = 'success' | 'error' | 'warning' | 'info'

interface Props {
  message: string
  type?: ToastType
  show: boolean
}

const props = withDefaults(defineProps<Props>(), {
  type: 'info'
})

const emit = defineEmits<{
  close: []
}>()

const typeClasses = computed(() => {
  const classes = {
    success: 'bg-green-50 border-green-200 text-green-800',
    error: 'bg-red-50 border-red-200 text-red-800',
    warning: 'bg-yellow-50 border-yellow-200 text-yellow-800',
    info: 'bg-blue-50 border-blue-200 text-blue-800'
  }
  return classes[props.type]
})

const iconClasses = computed(() => {
  const classes = {
    success: 'text-green-600',
    error: 'text-red-600',
    warning: 'text-yellow-600',
    info: 'text-blue-600'
  }
  return classes[props.type]
})

const iconName = computed(() => {
  const icons = {
    success: 'check_circle',
    error: 'error',
    warning: 'warning',
    info: 'info'
  }
  return icons[props.type]
})
</script>

<template>
  <Transition
    enter-active-class="transition ease-out duration-300"
    enter-from-class="opacity-0 translate-y-2"
    enter-to-class="opacity-100 translate-y-0"
    leave-active-class="transition ease-in duration-200"
    leave-from-class="opacity-100 translate-y-0"
    leave-to-class="opacity-0 translate-y-2"
  >
    <div
      v-if="show"
      :class="[
        'fixed top-4 right-4 z-50 flex items-center gap-3 px-4 py-3 rounded-lg border shadow-lg max-w-md',
        typeClasses
      ]"
    >
      <span class="material-icons" :class="iconClasses">{{ iconName }}</span>
      <p class="flex-1 text-sm font-medium">{{ message }}</p>
      <button
        @click="emit('close')"
        class="material-icons text-gray-500 hover:text-gray-700 transition-colors"
      >
        close
      </button>
    </div>
  </Transition>
</template>
