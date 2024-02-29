<template>
    <div id="login" style="background-color: #f5f5f5; padding: 20px; display: flex; justify-content: center; align-items: center; height: 60vh;">
      <div style="background-color: #ffffff; padding: 40px; border-radius: 10px; box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); max-width: 400px; width: 100%;">
        <h1 style="color: #2196F3; text-align: center; margin-bottom: 20px; font-size: 24px;">登录</h1>
        <form @submit.prevent="login" style="color: #666666;">
          <div style="margin-bottom: 20px;">
            <label for="username" style="display: block; margin-bottom: 5px;">用户名：</label>
            <input type="text" id="username" v-model="username" style="width: 100%; padding: 10px; border: 1px solid #cccccc; border-radius: 5px;">
          </div>
          <div style="margin-bottom: 20px;">
            <label for="password" style="display: block; margin-bottom: 5px;">密码：</label>
            <input type="password" id="password" v-model="password" style="width: 100%; padding: 10px; border: 1px solid #cccccc; border-radius: 5px;">
          </div>
          <button type="submit" style="background-color: #2196F3; color: #ffffff; border: none; padding: 10px 20px; cursor: pointer; border-radius: 5px; display: block; width: 100%;">登录</button>
        </form>
        <button type="submit" style="background-color: #ffffff; color: #2196F3; border: none; padding: 10px 20px; cursor: pointer; border-radius: 5px; display: block; width: 100%;" @click="goBack">返回</button>
      </div>
    </div>
    <div>
      <AboutUs></AboutUs>
    </div>
</template>


<script>
import axios from 'axios';
import AboutUs from '@/components/AboutUs.vue';
import Cookies from 'js-cookie';
export default {
    components:{
        AboutUs
    },
    data() {
        return {
            username: '',
            password: ''
        };
    },
    methods: {

        login() {
            if (this.username === '') {
                alert('请输入用户名');
                return;
            }

            if (this.password === '') {
                alert('请输入密码');
                return;
            }
            axios
                .post(`${process.env.VUE_APP_API_BASE_URL}/login`, {
                username: this.username,
                password: this.password
                })
                .then(response => {
                const { status, data } = response;
                if (status === 200) {
                    if (data.success) {
                    alert('登陆成功');
                    Cookies.set('token', data.token);
                    } else {
                    alert('登陆失败，请检查用户名和密码');
                    }
                } else {
                    alert(`请求失败，状态码：${status}`);
                }
                })
                .catch(error => {
                alert('请求失败，请重试');
                console.error(error);
                });
        },
        goBack(){
            this.$router.push('/');
        }
    }
};
</script>

<style scoped>
h1 {
    font-size: 24px;
    margin-bottom: 20px;
}

label {
    display: block;
    margin-bottom: 10px;
}

input {
    width: 200px;
    padding: 5px;
    margin-bottom: 10px;
}

button {
    padding: 10px 20px;
}

</style>