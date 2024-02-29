<template>
  <div>
    <div class="github-homepage">
      <div>
        <HeadBar></HeadBar>
      </div>
      <div class="content">
        <h1 class="artistic-text">Let's Build from here</h1>
      </div>
    </div>
    <div>
      <AboutUs></AboutUs>
    </div>
  </div>
</template>

<style scoped>
.github-homepage {
  min-height: 60vh;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 50px;
}

.content {
  text-align: center;
  margin-top: 50px;
}

h1 {
  font-size: 2rem;
}

.artistic-text {
  font-family: 'Your Artistic Font', cursive;
  font-size: 3rem;
  color: white;
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
  background-size: cover;
  display: inline-block;
  padding: 10px 20px;
}

.footer {
  background-color: #f0f0f0;
  padding: 20px;
  margin-top: 20px;
}

h2 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}
</style>

<script>

import HeadBar from '../components/HeadBar.vue'
import AboutUs from '../components/AboutUs.vue'

export default {
  components: {
    HeadBar,
    AboutUs
  },
  created() {
    document.title = this.$route.meta.title || 'MutHub';
  }
};
</script>