import UserService from '@/services/UserService'
import { defineStore } from 'pinia'

export const useUserStore = defineStore('userStore', {
    state: () => ({ user: null, isUserLoggedIn: false }),
    actions: {
        setUser(user) {
            this.user = user
            this.isUserLoggedIn = user ? true : false
            localStorage.setItem('userId', user.user_id)
        },
        logout() {
            this.user = null
            this.isUserLoggedIn = false
            localStorage.removeItem('userId')
        },
        async initializeUser() {
            const storedUserId = localStorage.getItem('userId')
            if(storedUserId) {
                this.user = (await UserService.getUser(storedUserId)).data
                this.isUserLoggedIn = this.user ? true : false
            }
        }
    }
})