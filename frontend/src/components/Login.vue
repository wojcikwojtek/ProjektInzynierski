<template>
    <v-layout column fill-height class="d-flex align-center justify-center">
        <div class="white elevation-5 bg-grey-darken-4 w-33">
          <v-toolbar flat dense class="bg-cyan text-white">
            <v-toolbar-title>Login</v-toolbar-title>
          </v-toolbar>
          <div class="pl-4 pr-4 pt-2 pb-2">
            <v-form v-model="valid">
                <v-text-field v-model="username" label="login" :rules="rules"></v-text-field>
                <v-text-field v-model="password" label="password" type="password" :rules="rules"></v-text-field>
                <div class="d-flex justify-center" v-if="error">
                    <div class="error"v-html="error"></div><br>
                </div>
                <div class="d-flex justify-center">
                    <v-btn class="bg-cyan text-white" @click="login">Login</v-btn>
                </div>
            </v-form>
            <div class="d-flex justify-center mt-3">
              <span>If you don't have an account </span>
              <RouterLink to="/register" class="text-cyan ml-1">Sign Up</RouterLink>
            </div>
          </div>
        </div>
    </v-layout>
</template>
  
<script>
import AuthenticationService from '@/services/AuthenticationService'
import { useUserStore } from '@/stores/userStore'
import { RouterLink } from 'vue-router';
export default {
    data () {
    return {
      username: '',
      password: '',
      valid: false,
      error: null,
      rules: [
        value => {
          if(value) return true
          return `Don't leave an empty field`
        }
      ]
      }
    },
    computed: {
      store: () => useUserStore()
    },
    methods: {
      async login () {
        if(!this.valid) return
        try {
          const response = await AuthenticationService.login({
            login: this.username,
            password: this.password,
          })
          const user = response.data
          this.store.setUser(user)
          this.$router.go(-1)
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
  