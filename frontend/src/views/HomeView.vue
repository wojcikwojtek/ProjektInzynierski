<script setup>
import { useUserStore } from '@/stores/userStore';
import TheWelcome from '../components/TheWelcome.vue'
import TheWelcomeLoggedIn from '@/components/TheWelcomeLoggedIn.vue';

const userStore = useUserStore()
</script>

<template>
  <main>
    <TheWelcome v-if="!userStore.isUserLoggedIn"/>
    <TheWelcomeLoggedIn v-else />
  </main>
</template>
