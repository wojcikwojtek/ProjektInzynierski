import { defineStore } from 'pinia'

export const useUserStore = defineStore('userStore', {
    state: () => ({ user: null, isUserLoggedIn: false }),
    actions: {
        setUser(user) {
            this.user = user
            this.isUserLoggedIn = user ? true : false
        }
    }
})