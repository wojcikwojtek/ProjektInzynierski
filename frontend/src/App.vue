<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
import PageHeader from '@/components/Header.vue'
</script>

<template>
  <!--
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header>
  -->
  <v-app>
    <!--drobne problemy z cieniem boxa na loginie i rejestracji przez dodanie v-main-->
    <PageHeader />
    <RouterView />
  </v-app>
</template>

<style scoped>

</style>
