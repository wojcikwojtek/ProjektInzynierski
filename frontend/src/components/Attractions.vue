<template>
    <div class="d-flex justify-center pl-4 pr-4 pt-6 pb-6">
        <v-text-field 
            append-inner-icon="mdi-magnify"
            density="comfortable"
            max-width="50%"
            label="Search attractions"
            variant="solo"
            single line
            v-model="searchedTerm"
            @click:append-inner="searchAttractions"
        ></v-text-field>
    </div>
    <div class="d-flex justify-center pl-4 pr-4 pt-2 pb-2">
        <div v-for="attraction in this.attractions">
            {{ attraction.name }}<br>
            {{ attraction.country }}<br>
            {{ attraction.city }}<br>
            {{ attraction.location }}<br>
            {{ attraction.description }}<br>
        </div>
    </div>
</template>
  
<script>
import AttractionService from '@/services/AttractionService';

export default {
    data () {
        return {
            searchedTerm: '',
            attractions: []
        }
    },
    methods: {
        async searchAttractions() {
            if(this.searchedTerm == '') return
            try{
                const response = await AttractionService.search(this.searchedTerm)
                this.attractions = response.data
                console.log(response.data)
            } catch(error) {
                console.log(error.response.data)
            }
        }
    }
}
</script>
  
<style scoped>
</style>
  