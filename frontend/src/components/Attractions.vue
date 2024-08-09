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
    <div v-for="attraction in this.attractions" class="d-flex mx-auto w-50">
        <v-row justify="center" dense>
            <v-col cols="12">
                <v-card variant="outlined" max-height="150" link @click="navigateTo({name: 'home'})">
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title class="text-h4">{{ attraction.name }}</v-card-title>
                            <v-card-subtitle>{{ attraction.country }}-{{ attraction.city }}-{{ attraction.location }}</v-card-subtitle>
                            <v-card-text>{{ attraction.description }}</v-card-text>
                        </div>
                        <v-avatar 
                            class="ma-3"
                            rounded="0"
                            size="125"
                        >
                            <v-img src="https://www.polsl.pl/wp-content/uploads/2023/08/miniatura_4.jpg"></v-img>
                        </v-avatar>
                    </div>
                </v-card>
            </v-col>
        </v-row>
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
            const response = await AttractionService.search(this.searchedTerm)
            this.attractions = response.data
            console.log(response.data)
        },
        navigateTo(route) {
            this.$router.push(route)
        }
    }
}
</script>
  
<style scoped>
</style>
  