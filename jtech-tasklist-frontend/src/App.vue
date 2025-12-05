<script setup lang="ts">
import { RouterLink, RouterView } from 'vue-router'
import ToastNotification from '@/components/ToastNotification.vue'
import { useToast } from '@/composables/useToast'

const { state, hideToast } = useToast()
</script>

<template>
  <header>
    <div class="wrapper">
      <h1 class="title">Task Manager</h1>

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/tasks">Tasks</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
  
  <ToastNotification
    :show="state.show"
    :message="state.message"
    :type="state.type"
    @close="hideToast"
  />
</template>

<style scoped>
header {
  background-color: white;
  border-bottom: 1px solid #e5e7eb;
  padding: 1rem 0;
  position: sticky;
  top: 0;
  z-index: 40;
}

.wrapper {
  max-width: 6xl;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}

.title {
  font-size: 1.5rem;
  font-weight: bold;
  margin: 0;
  color: #1f2937;
}

nav {
  display: flex;
  gap: 2rem;
  align-items: center;
}

nav a {
  padding: 0.5rem 1rem;
  border-radius: 0.375rem;
  font-weight: 500;
  color: #6b7280;
  text-decoration: none;
  transition: all 0.3s;
}

nav a:hover {
  color: #1f2937;
  background-color: #f3f4f6;
}

nav a.router-link-active {
  color: #2563eb;
  background-color: #eff6ff;
}

@media (max-width: 768px) {
  .wrapper {
    flex-direction: column;
    gap: 1rem;
  }

  nav {
    width: 100%;
    justify-content: center;
  }

  .title {
    font-size: 1.25rem;
  }
}
</style>
