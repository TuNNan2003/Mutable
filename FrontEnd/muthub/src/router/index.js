import { createRouter, createWebHistory } from 'vue-router';

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../view/MainPage.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../view/LoginPage.vue')
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router;