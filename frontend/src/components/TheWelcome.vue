<template>
    <v-row no-gutters>
        <v-col cols="2" class="pa-2">
        </v-col>
        <v-col cols="8" class="pa-2">
            <MapView></MapView>
        </v-col>
    </v-row>
</template>

<script>
import MapView from './MapView.vue';

export default {
    components: {
        MapView
    }
}
</script>

<style scoped>
</style>