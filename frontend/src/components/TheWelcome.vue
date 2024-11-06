<template>
    <v-row no-gutters>
        <v-col cols="2" class="pa-2">
        </v-col>
        <v-col cols="8" class="pa-2" style="position: relative;">
            <MapView></MapView>
            <div class="map-overlay-text text-center">
                <h1>Welcome to RateAttractions!</h1>
                <RouterLink to="/register" class="text-cyan">Sign Up</RouterLink>
                <span> to unlock all the features</span><br/>
                <span>If you already have an account </span>
                <RouterLink to="/login" class="text-cyan">log in</RouterLink>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import { RouterLink } from 'vue-router';
import MapView from './MapView.vue';

export default {
    components: {
        MapView
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        }
    }
}
</script>

<style scoped>
.map-overlay-text {
    position: absolute;
    top: 50%; 
    left: 50%; 
    width: 70%;
    transform: translate(-50%, -50%); 
    color: white; 
    font-size: 20px; 
    background-color: rgba(33, 33, 33, 0.5); 
    padding: 5px 10px; 
    border-radius: 5px; 
}
</style>