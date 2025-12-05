<script setup lang="ts">
interface Props {
  show: boolean
  title?: string
  message: string
  confirmText?: string
  cancelText?: string
  type?: 'danger' | 'warning' | 'info'
}

withDefaults(defineProps<Props>(), {
  title: 'Confirmação',
  confirmText: 'Confirmar',
  cancelText: 'Cancelar',
  type: 'warning'
})

const emit = defineEmits<{
  confirm: []
  cancel: []
}>()
</script>

<template>
  <Transition
    enter-active-class="transition ease-out duration-200"
    enter-from-class="opacity-0"
    enter-to-class="opacity-100"
    leave-active-class="transition ease-in duration-150"
    leave-from-class="opacity-100"
    leave-to-class="opacity-0"
  >
    <div
      v-if="show"
      class="fixed inset-0 bg-black/50 flex items-center justify-center z-50"
      @click.self="emit('cancel')"
    >
      <Transition
        enter-active-class="transition ease-out duration-200"
        enter-from-class="opacity-0 scale-95"
        enter-to-class="opacity-100 scale-100"
        leave-active-class="transition ease-in duration-150"
        leave-from-class="opacity-100 scale-100"
        leave-to-class="opacity-0 scale-95"
      >
        <div
          v-if="show"
          class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4 overflow-hidden"
        >
          <div class="p-6">
            <div class="flex items-start gap-4">
              <div
                :class="[
                  'shrink-0 w-10 h-10 rounded-full flex items-center justify-center',
                  type === 'danger' ? 'bg-red-100' : type === 'warning' ? 'bg-yellow-100' : 'bg-blue-100'
                ]"
              >
                <span
                  class="material-icons"
                  :class="[
                    type === 'danger' ? 'text-red-600' : type === 'warning' ? 'text-yellow-600' : 'text-blue-600'
                  ]"
                >
                  {{ type === 'danger' ? 'error' : type === 'warning' ? 'warning' : 'info' }}
                </span>
              </div>
              <div class="flex-1">
                <h3 class="text-lg font-semibold text-gray-900 mb-2">{{ title }}</h3>
                <p class="text-gray-600">{{ message }}</p>
              </div>
            </div>
          </div>

          <div class="bg-gray-50 px-6 py-4 flex gap-3 justify-end">
            <button
              @click="emit('cancel')"
              class="px-4 py-2 border border-gray-300 text-gray-700 rounded-lg font-medium hover:bg-gray-100 transition-all"
            >
              {{ cancelText }}
            </button>
            <button
              @click="emit('confirm')"
              :class="[
                'px-4 py-2 rounded-lg font-medium transition-all',
                type === 'danger'
                  ? 'bg-red-600 text-white hover:bg-red-700'
                  : type === 'warning'
                  ? 'bg-yellow-600 text-white hover:bg-yellow-700'
                  : 'bg-blue-600 text-white hover:bg-blue-700'
              ]"
            >
              {{ confirmText }}
            </button>
          </div>
        </div>
      </Transition>
    </div>
  </Transition>
</template>
