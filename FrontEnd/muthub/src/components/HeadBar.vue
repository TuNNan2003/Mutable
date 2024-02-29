<template>
    <nav class="navbar">
        <div class="logo"><img src="../../public/icon.png" alt="Logo" style="width: 40px; height: 40px;"> MutHub</div>
        <div class="buttons">
            <button class="primary-button">Button 1</button>
            <button class="secondary-button">Button 2</button>
            <button class="secondary-button">Button 3</button>
            <button class="secondary-button">Button 4</button>
        </div>
        <div class="search-bar">
            <input type="text" placeholder="Search" class="modern-input">
            <button class="primary-button" style="margin-right: 10px;">Search</button>
            <button v-if="!token" class="primary-button" @click="goToLogin">Login</button>
            <div v-else class="profile-circle">{{ token }}</div>
        </div>
    </nav>
</template>

<script>
import Cookies from 'js-cookie';

export default {
  name: 'head-bar',
  computed: {
    token() {
      return Cookies.get('token');
    }
  },
  methods: {
    goToLogin() {
      this.$router.push('/login');
    }
  }
}
</script>

<style>
.navbar {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px;
  background-color: #f0f0f0;
  padding: 0 20px;
}

.logo {
  font-weight: bold;
}

.buttons {
  display: flex;
}

.buttons button {
  margin-left: 10px;
}

.primary-button {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.secondary-button {
  background-color: white;
  color: #2196F3;
  border: 1px solid #2196F3;
  padding: 10px 20px;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.secondary-button:hover {
  background-color: #2196F3;
  color: white;
}

.search-bar {
  display: flex;
  align-items: center;
}

.modern-input {
  padding: 10px;
  border: none;
  border-radius: 4px;
  background-color: #f0f0f0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  font-size: 14px;
  transition: box-shadow 0.3s;
}

.modern-input:focus {
  outline: none;
  box-shadow: 0 0 8px rgba(33, 150, 243, 0.5);
}
</style>