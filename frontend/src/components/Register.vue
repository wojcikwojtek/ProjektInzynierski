<template>
  <v-layout column fill-height class="d-flex align-center justify-center">
      <div class="white elevation-5 bg-grey-darken-4 w-33">
        <v-toolbar flat dense class="bg-cyan text-white">
          <v-toolbar-title>Register</v-toolbar-title>
        </v-toolbar>
        <div class="pl-4 pr-4 pt-2 pb-2">
          <v-form v-model="valid">
            <v-text-field v-model="login" label="login" :rules="loginRules"></v-text-field>
            <v-text-field v-model="password" label="password" type="password" :rules="passwordRules"></v-text-field>
            <v-text-field v-if="passwordCorrect"
              v-model="repeatedPassword" label="repeat password" type="password" :rules="repeatedPasswordRules"></v-text-field>
            <v-text-field v-model="email" label="email" :rules="emailRules"></v-text-field>
            <div class="d-flex justify-center" v-if="error">
              <div class="error"v-html="error"></div><br>
            </div>
            <div class="d-flex justify-center">
              <v-btn class="bg-cyan text-white" @click="register">Register</v-btn>
            </div>
          </v-form>
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
      valid: false,
      login: '',
      password: '',
      repeatedPassword: '',
      email: '',
      error: null,
      passwordCorrect: false,
      loginRules: [
        value => {
          if(value) return true
          return `Don't leave an empty field`
        }
      ],
      passwordRules: [
        value => {
          if(!value) {
            this.passwordCorrect = false
            return `Don't leave an empty field`
          } 
          const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[\d@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/
          if(passwordRegex.test(value)) {
            this.passwordCorrect = true
            return true
          }
          this.passwordCorrect = false
          return `Password needs at least 8 characters, one uppercase letter, one lowercase letter, one digit or special character`
        }
      ],
      repeatedPasswordRules: [
        value => {
          if(value == this.password) return true
          return 'Passwords are not the same'
        }
      ],
      emailRules: [
        value => {
          if(!value) return `Don't leave an empty field`
          const emailPassword = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
          if(emailPassword.test(value)) return true
          return `Invalid email`
        }
      ]
    }
  },
  computed: {
    store: () => useUserStore()
  },
  methods: {
    async register () {
      if(!this.valid) return
      try {
        const response = await AuthenticationService.register({
          login: this.login,
          password: this.password,
          email: this.email
        })
        const user = response.data
        this.store.setUser(user)
        this.$router.push({name: 'home'})
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
