<template>
    <v-toolbar prominent :elevation="4" class="bg-cyan text-white">
        <v-toolbar-title class="mr-4">
            <span class="home" @click="navigateTo({name: 'home'})">RateAttractions</span>
        </v-toolbar-title>
        <v-toolbar-items>
            <v-btn flat @click="navigateTo({name: 'attractions'})">
                Browse
            </v-btn>
        </v-toolbar-items>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-spacer></v-spacer>
        <v-toolbar-items>
            <v-btn v-if="!isUserLoggedIn" flat @click="navigateTo({name: 'login'})">
                Login
            </v-btn>
            <v-btn v-if="!isUserLoggedIn" flat @click="navigateTo({name: 'register'})">
                Sign up
            </v-btn>
            <v-btn v-if="isUserLoggedIn" flat @click="logout">
                Log Out
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>
</template>
  
<script>
import { useUserStore } from '@/stores/userStore'
export default {
    computed: {
        userStore: () => useUserStore(),
        isUserLoggedIn() {
            return this.userStore.isUserLoggedIn
        }
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        },
        logout () {
            this.userStore.setUser(null)
            this.$router.push({name: "home"})
        }
    }
}
</script>
  
<style scoped>
.home {
    cursor: pointer;
}

.home:hover {
    color: #E9E
}
</style>
  