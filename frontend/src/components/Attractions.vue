<template>
    <div class="d-flex justify-center pl-4 pr-4 pt-6 pb-6">
        <v-select
            v-model="value"
            max-width="10%"
            variant="solo"
            density="comfortable"
            label="Select"
            :items="items"
        ></v-select>
        <v-text-field 
            append-inner-icon="mdi-magnify"
            density="comfortable"
            max-width="50%"
            :label="searchLabel"
            variant="solo"
            single line
            v-model="searchedTerm"
            @click:append-inner="search"
            @keyup.enter="search"
        >
        </v-text-field>
    </div>
    <div v-for="result in this.results" class="d-flex mx-auto w-50 pt-2">
        <v-row justify="center" dense>
            <v-col cols="12">
                <v-card variant="outlined" max-height="150" link v-if="value==items[0]"
                    @click="navigateTo({
                        name: 'attraction', 
                        params: {
                            attractionId: result.attraction_id
                            }
                        })">
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title class="text-h4">{{ result.name }}</v-card-title>
                            <v-card-subtitle>{{ result.country }}-{{ result.city }}-{{ result.location }}</v-card-subtitle>
                            <v-card-text>{{ result.description }}</v-card-text>
                        </div>
                        <v-avatar 
                            class="ma-3"
                            rounded="0"
                            size="125"
                        >
                            <v-img :src="getAttractionImgUrl(result.attraction_id)"></v-img>
                        </v-avatar>
                    </div>
                </v-card>
                <ListComponent v-if="value==items[1]"
                    v-model:listId="result.list_id"
                    v-model:name="result.name"
                    v-model:description="result.description"
                    v-model:user="result.user"
                    v-model:imagesUrls="result.imagesUrls"
                ></ListComponent>
            </v-col>
        </v-row>
    </div>
</template>
  
<script>
import AttractionService from '@/services/AttractionService';
import ListService from '@/services/ListService';
import ListComponent from './ListComponent.vue';

export default {
    data () {
        return {
            searchedTerm: '',
            results: [],
            items: ['Attractions', 'Lists', 'Users'],
            value: 'Attractions'
        }
    },
    computed: {
        searchLabel() {
            if(this.value == this.items[0]) return 'Search attractions, country or city'
            else if(this.value == this.items[1]) return 'Search lists'
            else if(this.value == this.items[2]) return 'Search users'
        }
    },
    components: {
        ListComponent
    },
    watch: {
        value(newValue, oldValue) {
            this.results = []
            this.searchedTerm = ''
        }
    },
    methods: {
        search() {
            if(this.value == this.items[0]) {
                this.searchAttractions()
            } else if(this.value == this.items[1]) {
                this.searchLists()
            }
        },
        async searchAttractions() {
            if(this.searchedTerm == '') return
            const response = await AttractionService.search(this.searchedTerm)
            this.results = response.data
        },
        async searchLists() {
            if(this.searchedTerm == '') return
            this.results = (await ListService.search(this.searchedTerm)).data
        },
        navigateTo(route) {
            this.$router.push(route)
        },
        getAttractionImgUrl(id) {
            return `http://localhost:8080/rating-attractions/attractions/${id}/image`
        }
    }
}
</script>
  
<style scoped>
</style>
  