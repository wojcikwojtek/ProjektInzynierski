import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import Register from '../components/Register.vue'
import Login from '../components/Login.vue'
import Attractions from '@/components/Attractions.vue'
import ViewAttraction from '@/components/ViewAttraction.vue'
import ViewComments from '@/components/ViewComments.vue'
import ViewProfile from '@/components/ViewProfile.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
      path: '/attractions',
      name: 'attractions',
      component: Attractions
    },
    {
      path: '/attractions/:attractionId',
      name: 'attraction',
      component: ViewAttraction
    },
    {
      path: '/attractions/:attractionId/reviews/:reviewId',
      name: 'reviewComments',
      component: ViewComments
    },
    {
      path: '/users/:userId',
      name: 'profile',
      component: ViewProfile
    },
    {
      path: '/about',
      name: 'about',
      // route level code-splitting
      // this generates a separate chunk (About.[hash].js) for this route
      // which is lazy-loaded when the route is visited.
      component: () => import('../views/AboutView.vue')
    }
  ]
})

export default router
