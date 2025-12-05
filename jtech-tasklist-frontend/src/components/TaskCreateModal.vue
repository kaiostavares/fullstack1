<script setup lang="ts">
import { ref } from 'vue'
import type { CreateTaskRequest } from '@/services/TaskApiService'
import { useTaskStore } from '@/stores/taskStore'
import { useToast } from '@/composables/useToast'

const taskStore = useTaskStore()
const toast = useToast()
const emit = defineEmits<{
  close: []
}>()

const form = ref<CreateTaskRequest>({
  name: '',
  description: '',
  status: 'PENDING',
})

const isSubmitting = ref(false)

async function handleSubmit() {
  if (!form.value.name || !form.value.description) {
    toast.warning('Por favor, preencha todos os campos')
    return
  }

  isSubmitting.value = true
  try {
    await taskStore.createTask(form.value)
    emit('close')
  } catch {
    // Erro já tratado no store
  } finally {
    isSubmitting.value = false
  }
}
</script>

<template>
  <div class="fixed inset-0 bg-black/50 flex items-center justify-center z-50" @click.self="$emit('close')">
    <div class="bg-white rounded-lg shadow-xl max-w-md w-full mx-4 max-h-screen overflow-y-auto">
      <div class="sticky top-0 bg-white border-b border-gray-200 px-6 py-4 flex justify-between items-center">
        <div class="flex items-center gap-2">
          <span class="material-icons text-blue-600">add_circle</span>
          <h2 class="text-xl font-bold text-gray-900">Criar Nova Task</h2>
        </div>
        <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700 text-2xl leading-none font-bold">×</button>
      </div>

      <form @submit.prevent="handleSubmit" class="p-6 space-y-4">
        <div>
          <label for="name" class="block text-sm font-medium text-gray-700 mb-2">Nome da Task *</label>
          <input id="name" v-model="form.name" type="text" placeholder="Digite o nome da task" maxlength="50" required autocomplete="off" data-lpignore="true" data-form-type="other" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent" />
          <div class="mt-1 text-xs text-gray-500">{{ form.name.length }}/50 caracteres</div>
        </div>

        <div>
          <label for="description" class="block text-sm font-medium text-gray-700 mb-2">Descrição *</label>
          <textarea id="description" v-model="form.description" placeholder="Digite a descrição da task" rows="4" maxlength="500" required autocomplete="off" data-lpignore="true" data-form-type="other" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"></textarea>
          <div class="mt-1 text-xs text-gray-500">{{ form.description.length }}/500 caracteres</div>
        </div>

        <div>
          <label for="status" class="block text-sm font-medium text-gray-700 mb-2">Status</label>
          <select id="status" v-model="form.status" class="w-full px-4 py-2 border border-gray-300 rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500 focus:border-transparent">
            <option value="PENDING">Pendente</option>
            <option value="IN_PROGRESS">Em Progresso</option>
            <option value="COMPLETED">Concluída</option>
          </select>
        </div>

        <div class="flex gap-3 pt-4">
          <button type="button" @click="$emit('close')" class="flex-1 px-4 py-2 border border-gray-300 text-gray-700 rounded-lg font-medium hover:bg-gray-50 transition-all">Cancelar</button>
          <button type="submit" :disabled="isSubmitting" class="flex-1 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed transition-all flex items-center justify-center gap-2">
            <span v-if="!isSubmitting" class="material-icons text-base">check</span>
            <span>{{ isSubmitting ? 'Criando...' : 'Criar Task' }}</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
