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
            <v-btn @click="navigateTo({name: 'lists'})">
                Lists
            </v-btn>
            <v-btn v-if="!isUserLoggedIn" flat @click="navigateTo({name: 'login'})">
                Login
            </v-btn>
            <v-btn v-if="!isUserLoggedIn" flat @click="navigateTo({name: 'register'})">
                Sign up
            </v-btn>
            <v-menu v-if="isUserLoggedIn">
                <template v-slot:activator="{ props }">
                    <v-btn icon="mdi-account" variant="text" v-bind="props"></v-btn>
                </template>
                <v-list>
                    <v-list-item
                        v-for="(item, i) in filteredItems" 
                        :key="i"
                        :value="item"
                    >
                        <v-list-item-title @click="menuOptions(i)">{{ item.title }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-menu>
            <v-btn v-if="isUserLoggedIn" flat @click="logout">
                Log Out
            </v-btn>
        </v-toolbar-items>
    </v-toolbar>
</template>
  
<script>
import { useUserStore } from '@/stores/userStore'
export default {
    data: () => ({
        items: [
            { title: 'Profile' },
            { title: 'Settings' },
            { title: 'Admin Panel' }
        ]
    }),
    computed: {
        userStore: () => useUserStore(),
        isUserLoggedIn() {
            return this.userStore.isUserLoggedIn
        },
        filteredItems() {
            return this.items.filter(item => {
                return item.title != 'Admin Panel' || this.userStore.user.admin
            })
        }
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        },
        logout () {
            this.userStore.logout()
            this.$router.push({name: "home"})
        },
        menuOptions(i) {
            switch(i) {
                case 0:
                    this.navigateTo({
                        name: 'profile',
                        params: {
                            userId: this.userStore.user.user_id
                            }
                    })
                    break
                case 1:
                    this.navigateTo({
                        name: 'profileSettings',
                        params: {
                            userId: this.userStore.user.user_id
                        }
                    })
                    break
                case 2:
                    this.navigateTo({
                        name: 'adminPanel'
                    })
                    break
            }
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
  