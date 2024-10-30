<template>
  <v-layout column fill-height class="d-flex align-center justify-center">
      <div class="white elevation-5 bg-grey-darken-4 w-33">
        <v-toolbar flat dense class="bg-cyan text-white">
          <v-toolbar-title>Register</v-toolbar-title>
        </v-toolbar>
        <div class="pl-4 pr-4 pt-2 pb-2">
            <form name="rate-attractions-form">
            <v-text-field v-model="login" label="login"></v-text-field>
            <v-text-field v-model="password" label="password" type="password"></v-text-field>
            <v-text-field v-model="email" label="email"></v-text-field>
            <div class="d-flex justify-center">
              <div class="error"v-html="error"></div><br>
            </div>
            <div class="d-flex justify-center">
              <v-btn class="bg-cyan text-white" @click="register">Register</v-btn>
            </div>
          </form>
        </div>
      </div>
  </v-layout>
</template>

<script>
import AuthenticationService from '@/services/AuthenticationService'
import { useUserStore } from '@/stores/userStore'
export default {
  data () {
    return {
      login: '',
      password: '',
      email: '',
      error: null
    }
  },
  computed: {
    store: () => useUserStore()
  },
  methods: {
    async register () {
      if(this.login == '' || this.password == '' || this.email == '') {
        this.error = "Fill out all fields"
        return
      }
      try {
        const response = await AuthenticationService.register({
          login: this.login,
          password: this.password,
          email: this.email
        })
        const user = response.data
        this.store.setUser(user)
      } catch (error) {
        this.error = error.response.data
      }
    }
  }
}
</script>

<style scoped>
.error {
  color: red;
}
</style>
