<template>
    <v-layout column fill-height class="d-flex align-center justify-center">
        <div class="white elevation-5 w-33">
          <v-toolbar flat dense class="bg-cyan text-white">
            <v-toolbar-title>Login</v-toolbar-title>
          </v-toolbar>
          <div class="pl-4 pr-4 pt-2 pb-2">
            <form name="rate-attractions-form">
                <v-text-field v-model="username" label="login"></v-text-field>
                <v-text-field v-model="password" label="password" type="password"></v-text-field>
                <div class="d-flex justify-center">
                    <div class="error"v-html="error"></div><br>
                </div>
                <div class="d-flex justify-center">
                    <v-btn class="bg-cyan text-white" @click="login">Login</v-btn>
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
        username: '',
        password: '',
        error: null
        }
    },
    computed: {
        store: () => useUserStore()
    },
    methods: {
      async login () {
        if(this.username == '' || this.password == '') {
          this.error = "Fill out all fields"
          return
        }
        try {
          const response = await AuthenticationService.login({
            login: this.username,
            password: this.password,
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
  