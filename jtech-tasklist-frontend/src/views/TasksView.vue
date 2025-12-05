<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useTaskStore } from '@/stores/taskStore'
import TaskCreateModal from '@/components/TaskCreateModal.vue'
import TaskEditModal from '@/components/TaskEditModal.vue'
import ConfirmModal from '@/components/ConfirmModal.vue'
import type { Task } from '@/services/TaskApiService'

const taskStore = useTaskStore()
const showCreateModal = ref(false)
const showEditModal = ref(false)
const showConfirmDelete = ref(false)
const selectedTask = ref<Task | null>(null)
const taskToDelete = ref<string | null>(null)
const filter = ref<'ALL' | 'PENDING' | 'IN_PROGRESS' | 'COMPLETED'>('ALL')
const currentPage = ref(1)
const itemsPerPage = 5

onMounted(async () => {
  await taskStore.fetchTasks()
})

const filteredTasks = computed(() => {
  if (filter.value === 'ALL') {
    return taskStore.tasks
  }
  return taskStore.tasks.filter(task => task.status === filter.value)
})

const paginatedTasks = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage
  const end = start + itemsPerPage
  return filteredTasks.value.slice(start, end)
})

const totalPages = computed(() => {
  return Math.ceil(filteredTasks.value.length / itemsPerPage)
})

const hasNextPage = computed(() => currentPage.value < totalPages.value)
const hasPrevPage = computed(() => currentPage.value > 1)

function nextPage() {
  if (hasNextPage.value) {
    currentPage.value++
  }
}

function prevPage() {
  if (hasPrevPage.value) {
    currentPage.value--
  }
}

function goToPage(page: number) {
  currentPage.value = page
}

// Reseta para primeira página ao mudar filtro
function setFilter(newFilter: typeof filter.value) {
  filter.value = newFilter
  currentPage.value = 1
}

function handleDeleteTask(id: string) {
  taskToDelete.value = id
  showConfirmDelete.value = true
}

async function confirmDelete() {
  if (taskToDelete.value) {
    await taskStore.deleteTask(taskToDelete.value)
    taskToDelete.value = null
  }
  showConfirmDelete.value = false
}

function cancelDelete() {
  taskToDelete.value = null
  showConfirmDelete.value = false
}

function openEditModal(task: Task) {
  selectedTask.value = task
  showEditModal.value = true
}

function getStatusBadgeClass(status: string) {
  const classes: Record<string, string> = {
    PENDING: 'badge-pending',
    IN_PROGRESS: 'badge-progress',
    COMPLETED: 'badge-completed',
  }
  return classes[status] || 'badge-pending'
}

function getStatusLabel(status: string) {
  const labels: Record<string, string> = {
    PENDING: 'Pendente',
    IN_PROGRESS: 'Em Progresso',
    COMPLETED: 'Concluída',
  }
  return labels[status] || status
}
</script>

<template>
  <div class="min-h-screen bg-gray-50 py-12 px-4">
    <!-- Header -->
    <div class="max-w-6xl mx-auto mb-12">
      <div class="flex justify-between items-center mb-8">
        <div class="flex items-center gap-3">
          <span class="material-icons text-4xl text-purple-600">checklist</span>
          <h1 class="text-4xl font-bold text-gray-900">Gerenciador de Tasks</h1>
        </div>
        <button
          @click="showCreateModal = true"
          class="btn-primary flex items-center gap-2"
        >
          <span class="material-icons">add</span>
          Nova Task
        </button>
      </div>

      <!-- Stats -->
      <div class="grid grid-cols-1 md:grid-cols-4 gap-4">
        <div class="stat-card">
          <div class="flex items-center justify-between">
            <div>
              <p class="opacity-90 text-sm">Total de Tasks</p>
              <p class="text-4xl font-bold mt-2">{{ taskStore.taskCount }}</p>
            </div>
            <span class="material-icons text-6xl opacity-20">list</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="flex items-center justify-between">
            <div>
              <p class="opacity-90 text-sm">Pendentes</p>
              <p class="text-4xl font-bold mt-2">{{ taskStore.tasksByStatus.PENDING.length }}</p>
            </div>
            <span class="material-icons text-6xl opacity-20">schedule</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="flex items-center justify-between">
            <div>
              <p class="opacity-90 text-sm">Em Progresso</p>
              <p class="text-4xl font-bold mt-2">{{ taskStore.tasksByStatus.IN_PROGRESS.length }}</p>
            </div>
            <span class="material-icons text-6xl opacity-20">autorenew</span>
          </div>
        </div>

        <div class="stat-card">
          <div class="flex items-center justify-between">
            <div>
              <p class="opacity-90 text-sm">Concluídas</p>
              <p class="text-4xl font-bold mt-2">{{ taskStore.tasksByStatus.COMPLETED.length }}</p>
            </div>
            <span class="material-icons text-6xl opacity-20">check_circle</span>
          </div>
        </div>
      </div>
    </div>

    <!-- Filters -->
    <div class="max-w-6xl mx-auto mb-8">
      <div class="flex gap-2 flex-wrap">
        <button
          v-for="status in ['ALL', 'PENDING', 'IN_PROGRESS', 'COMPLETED']"
          :key="status"
          @click="setFilter(status as any)"
          :class="[
            'px-4 py-2 rounded-lg font-medium transition-all',
            filter === status
              ? 'bg-green-600 text-white shadow-lg'
              : 'bg-white text-gray-700 border border-gray-200 hover:border-gray-400'
          ]"
        >
          {{ status === 'ALL' ? 'Todas' : getStatusLabel(status) }}
        </button>
      </div>
    </div>

    <!-- Main Content -->
    <div class="max-w-6xl mx-auto">
      <!-- Loading State -->
      <div v-if="taskStore.loading && taskStore.tasks.length === 0" class="flex flex-col items-center justify-center py-20">
        <div class="w-12 h-12 border-4 border-gray-300 border-t-green-600 rounded-full animate-spin mb-4"></div>
        <p class="text-gray-600 font-medium">Carregando tasks...</p>
      </div>

      <!-- Tasks List -->
      <div v-else-if="filteredTasks.length > 0" class="space-y-4">
        <div
          v-for="task in paginatedTasks"
          :key="task.id"
          class="card p-6 hover:shadow-lg hover:-translate-y-1 transition-all"
        >
          <!-- Task Header -->
          <div class="flex justify-between items-start mb-4">
            <div class="flex-1">
              <h3 class="text-xl font-semibold text-gray-900 mb-2">{{ task.name }}</h3>
              <p class="text-gray-600">{{ task.description }}</p>
            </div>
            <span :class="['badge', getStatusBadgeClass(task.status)]">
              {{ getStatusLabel(task.status) }}
            </span>
          </div>

          <!-- Task Meta -->
          <div class="flex gap-6 mb-6 text-sm text-gray-500 flex-wrap">
            <div class="flex items-center gap-2" v-if="task.createdAt">
              <span class="material-icons text-base">calendar_today</span>
              Criada: {{ new Date(task.createdAt).toLocaleDateString('pt-BR') }}
            </div>
            <div class="flex items-center gap-2" v-if="task.updatedAt">
              <span class="material-icons text-base">update</span>
              Atualizada: {{ new Date(task.updatedAt).toLocaleDateString('pt-BR') }}
            </div>
          </div>

          <!-- Task Actions -->
          <div class="flex gap-3">
            <button
              @click="openEditModal(task)"
              class="flex items-center gap-2 px-4 py-2 bg-blue-600 text-white rounded-lg font-medium hover:bg-blue-700 transition-all"
            >
              <span class="material-icons text-base">edit</span>
              Editar
            </button>
            <button
              @click="handleDeleteTask(task.id)"
              class="flex items-center gap-2 px-4 py-2 bg-red-600 text-white rounded-lg font-medium hover:bg-red-700 transition-all"
            >
              <span class="material-icons text-base">delete</span>
              Deletar
            </button>
          </div>
        </div>

        <!-- Pagination Controls -->
        <div v-if="totalPages > 1" class="mt-8 flex justify-center items-center gap-2">
          <button
            @click="prevPage"
            :disabled="!hasPrevPage"
            class="px-4 py-2 rounded-lg font-medium transition-all flex items-center gap-1 disabled:opacity-50 disabled:cursor-not-allowed bg-white border border-gray-200 hover:border-gray-400 text-gray-700 disabled:hover:border-gray-200"
          >
            <span class="material-icons text-base">chevron_left</span>
            Anterior
          </button>

          <div class="flex gap-1">
            <button
              v-for="page in totalPages"
              :key="page"
              @click="goToPage(page)"
              :class="[
                'w-10 h-10 rounded-lg font-medium transition-all',
                currentPage === page
                  ? 'bg-green-600 text-white shadow-lg'
                  : 'bg-white text-gray-700 border border-gray-200 hover:border-gray-400'
              ]"
            >
              {{ page }}
            </button>
          </div>

          <button
            @click="nextPage"
            :disabled="!hasNextPage"
            class="px-4 py-2 rounded-lg font-medium transition-all flex items-center gap-1 disabled:opacity-50 disabled:cursor-not-allowed bg-white border border-gray-200 hover:border-gray-400 text-gray-700 disabled:hover:border-gray-200"
          >
            Próxima
            <span class="material-icons text-base">chevron_right</span>
          </button>
        </div>

        <!-- Pagination Info -->
        <div class="mt-4 text-center text-sm text-gray-600">
          Mostrando {{ (currentPage - 1) * itemsPerPage + 1 }} - {{ Math.min(currentPage * itemsPerPage, filteredTasks.length) }} de {{ filteredTasks.length }} tarefas
        </div>
      </div>

      <!-- Empty State -->
      <div v-else class="flex flex-col items-center justify-center py-20">
        <span class="material-icons text-6xl text-gray-300 mb-4">inbox</span>
        <p class="text-xl text-gray-600 font-medium mb-8">Nenhuma task encontrada</p>
        <button @click="showCreateModal = true" class="btn-primary flex items-center gap-2">
          <span class="material-icons">add</span>
          Criar primeira task
        </button>
      </div>
    </div>

    <!-- Modals -->
    <TaskCreateModal
      v-if="showCreateModal"
      @close="showCreateModal = false"
    />
    <TaskEditModal
      v-if="showEditModal && selectedTask"
      :task="selectedTask"
      @close="showEditModal = false"
    />
    <ConfirmModal
      :show="showConfirmDelete"
      title="Excluir Tarefa"
      message="Tem certeza que deseja excluir esta tarefa? Esta ação não pode ser desfeita."
      confirm-text="Excluir"
      cancel-text="Cancelar"
      type="danger"
      @confirm="confirmDelete"
      @cancel="cancelDelete"
    />
  </div>
</template>

