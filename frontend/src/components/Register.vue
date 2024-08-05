<template>
  <v-layout column>
    <v-flex xs6 offset xs-3>
      <div class="white elevation-2">
        <v-toolbar flat dense class="bg-cyan">
          <v-toolbar-title>Register</v-toolbar-title>
        </v-toolbar>
        <div class="pl-4 pr-4 pt-2 pb-2">
          <v-text-field v-model="login" label="login"></v-text-field>
          <v-text-field v-model="password" label="password"></v-text-field>
          <v-text-field v-model="email" label="email"></v-text-field>
          <div class="error"v-html="error"></div><br>
          <v-btn class="bg-cyan" @click="register">Register</v-btn>
        </div>
      </div>
    </v-flex>
  </v-layout>
</template>

<script>
import AuthenticationService from '@/services/AuthenticationService'
export default {
  data () {
    return {
      login: '',
      password: '',
      email: '',
      error: null
    }
  },
  methods: {
    async register () {
      try {
        const response = await AuthenticationService.register({
          login: this.login,
          password: this.password,
          email: this.email
        })
        console.log(response.data)
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
