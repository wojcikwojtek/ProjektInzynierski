import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import '@mdi/font/css/materialdesignicons.css';

import App from './App.vue'
import router from './router'
import { useUserStore } from './stores/userStore'

const vuetify = createVuetify({
    components,
    directives,
    icons: {
        iconfont: 'mdi'
    },
    theme: {
        defaultTheme: 'dark'
    }
})

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(vuetify)

app.mount('#app')

const userStore = useUserStore()
userStore.initializeUser()