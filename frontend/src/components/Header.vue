<template>
    <v-toolbar fixed :elevation="4" class="bg-cyan text-white">
        <v-toolbar-title class="mr-4">
            <span class="home" @click="navigateTo({name: 'home'})">RateAttractions</span>
        </v-toolbar-title>
        <!--<v-toolbar-items>
            <v-btn flat>
                Browse
            </v-btn>
        </v-toolbar-items>-->
        <v-spacer></v-spacer>
        <v-toolbar-items v-if="!isUserLoggedIn">
            <v-btn flat @click="navigateTo({name: 'login'})">
                Login
            </v-btn>
            <v-btn flat @click="navigateTo({name: 'register'})">
                Sign up
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>
</template>
  
<script>
import { useUserStore } from '@/stores/userStore'
export default {
    computed: {
        isUserLoggedIn() {
            const store = useUserStore()
            return store.isUserLoggedIn
        }
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
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
  