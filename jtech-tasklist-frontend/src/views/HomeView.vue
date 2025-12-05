<script setup lang="ts">
</script>

<template>
  <main class="max-w-6xl mx-auto px-4 py-12">
    <div class="text-center">
      <h1 class="text-5xl font-bold text-gray-900 mb-6">
        Bem-vindo ao Task Manager
      </h1>
      <p class="text-xl text-gray-600 mb-8">
        Organize suas tarefas de forma simples e eficiente
      </p>
      <router-link 
        to="/tasks" 
        class="inline-flex items-center gap-2 px-8 py-3 bg-blue-600 text-white rounded-lg font-semibold hover:bg-blue-700 transition-all"
      >
        <span class="material-icons">checklist</span>
        Ver minhas Tasks
      </router-link>
    </div>
    
    <div class="mt-16 grid grid-cols-1 md:grid-cols-3 gap-8">
      <div class="text-center p-6">
        <div class="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <span class="material-icons text-blue-600 text-3xl">add_task</span>
        </div>
        <h3 class="text-lg font-semibold mb-2">Criar Tasks</h3>
        <p class="text-gray-600">Adicione suas tarefas rapidamente</p>
      </div>
      
      <div class="text-center p-6">
        <div class="w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <span class="material-icons text-green-600 text-3xl">track_changes</span>
        </div>
        <h3 class="text-lg font-semibold mb-2">Acompanhar Progresso</h3>
        <p class="text-gray-600">Gerencie o status das suas tarefas</p>
      </div>
      
      <div class="text-center p-6">
        <div class="w-16 h-16 bg-purple-100 rounded-full flex items-center justify-center mx-auto mb-4">
          <span class="material-icons text-purple-600 text-3xl">check_circle</span>
        </div>
        <h3 class="text-lg font-semibold mb-2">Concluir</h3>
        <p class="text-gray-600">Marque tarefas como concluídas</p>
      </div>
    </div>
  </main>
</template>
