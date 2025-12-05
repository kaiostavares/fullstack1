<template>
  <div class="min-h-screen bg-gray-50 flex items-center justify-center px-4">
    <div class="max-w-md w-full text-center">
      <div class="mb-8">
        <span class="material-icons text-9xl text-gray-300">search_off</span>
      </div>
      
      <h1 class="text-6xl font-bold text-gray-900 mb-4">404</h1>
      <h2 class="text-2xl font-semibold text-gray-700 mb-4">Página não encontrada</h2>
      
      <p class="text-gray-600 mb-8">
        A página que você está procurando não existe ou foi movida.
      </p>

      <div class="flex flex-col sm:flex-row gap-4 justify-center">
        <RouterLink
          to="/"
          class="inline-flex items-center justify-center gap-2 px-6 py-3 bg-green-600 text-white rounded-lg font-medium hover:bg-green-700 transition-all shadow-lg hover:shadow-xl"
        >
          <span class="material-icons">home</span>
          Ir para Home
        </RouterLink>
        
        <RouterLink
          to="/tasks"
          class="inline-flex items-center justify-center gap-2 px-6 py-3 bg-white text-gray-700 border-2 border-gray-300 rounded-lg font-medium hover:border-gray-400 transition-all"
        >
          <span class="material-icons">checklist</span>
          Ver Tasks
        </RouterLink>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { RouterLink } from 'vue-router'
</script>
