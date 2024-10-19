<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="suggestions" class="pa-2">
            <h2 class="title">Suggested Attractions</h2>
            <div v-for="suggestion in suggestions" :key="suggestion" class="pt-2 pr-2 pl-2">
                <v-card variant="outlined">
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title class="text-h4">{{ suggestion.name }}</v-card-title>
                            <v-card-subtitle>{{ suggestion.country }}-{{ suggestion.city }}-{{ suggestion.location }}</v-card-subtitle>
                            <v-card-text>{{ suggestion.description }}</v-card-text>
                            <v-card-actions>
                                <v-btn icon="mdi-check" color="green" @click="approve(suggestion.suggestion_id)"></v-btn>
                                <v-btn icon="mdi-close" color="red" @click="discard(suggestion.suggestion_id)"></v-btn>
                            </v-card-actions>
                        </div>
                        <v-avatar 
                            class="ma-3"
                            rounded="0"
                            size="125"
                        >
                            <v-img :src="getSuggestionImgUrl(suggestion.suggestion_id)"></v-img>
                        </v-avatar>
                    </div>
                </v-card>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import AdminService from '@/services/AdminService';

export default {
    data() {
        return {
            suggestions: null
        }
    },
    async mounted() {
        this.suggestions = (await AdminService.getSuggestions()).data
    },
    methods: {
        getSuggestionImgUrl(id) {
            return `http://localhost:8080/rating-attractions/admin/suggestion/${id}/image`
        },
        async approve(id) {
            const response = await AdminService.approveSuggestion(id)
            this.suggestions = (await AdminService.getSuggestions()).data
        },
        async discard(id) {
            const response = await AdminService.discardSuggestion(id)
            this.suggestions = (await AdminService.getSuggestions()).data
        }
    }
}
</script>

<style scoped>
.title {
    position: relative;
    padding-bottom: 8px; 
    margin-bottom: 16px; 
}

.title::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 2px; 
    background-color: #000; 
}
</style>