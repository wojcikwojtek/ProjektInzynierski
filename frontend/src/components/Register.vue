<template>
    <div>
        <h1>Register</h1>

        <input type="text" name="login" v-model="login" placeholder="login"><br>
        <input type="password" name="password" v-model="password" placeholder="password"><br>
        <input type="email" name="email" v-model="email" placeholder="email"><br>
        <button @click="register">Register</button>
    </div>
</template>

<script>
import AuthenticationService from '@/services/AuthenticationService'
export default {
  data () {
    return {
      login: '',
      password: '',
      email: ''
    }
  },
  methods: {
    async register () {
      const response = await AuthenticationService.register({
        login: this.login,
        password: this.password,
        email: this.email
      })
      console.log(response.data)
    }
  }
}
</script>

<style scoped>

</style>
