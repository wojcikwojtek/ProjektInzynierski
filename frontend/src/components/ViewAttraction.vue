<template>
    <v-row no-gutters>
        <v-col cols="3" align-self="start" class = "pl-2 pr-2 pt-2 pb-2">
            <v-img v-if="attraction" :src="attraction.imageUrl"></v-img>
            <p v-else>Loading image...</p>
        </v-col>
        <v-col v-if="attraction" cols="6" class="pl-2 pr-2 pt-2 pb-2">
            <h1 >{{ attraction.name }}</h1>
            <p>Country: {{ attraction.country }}</p>
            <p>City: {{ attraction.city }}</p>
            <p>Location: {{ attraction.location }}</p>
            <p>Description:</p>
            <p>{{ attraction.description }}</p>
        </v-col>
    </v-row>
</template>
  
<script>
import AttractionService from '@/services/AttractionService';

export default {
    data () {
        return {
            attraction: null
        }
    },
    async beforeMount () {
        const attractionId = this.$route.params.attractionId
        this.attraction = (await AttractionService.getAttractionById(attractionId)).data
        console.log(this.attraction)
    }
}
</script>
  
<style scoped>
</style>
  