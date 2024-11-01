<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="suggestions" class="pa-2">
            <div class="white elevation-5 pa-6 bg-grey-darken-4">
                <v-expansion-panels>
                    <v-expansion-panel
                        color="cyan"
                    >
                        <v-expansion-panel-title class="text-white">
                            Suggested Attractions
                        </v-expansion-panel-title>
                        <v-expansion-panel-text>
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
                            <div v-if="suggestions.length == 0">
                                <v-alert
                                    text="There are currently no suggestions"
                                    type="info"
                                    color="cyan"
                                    class="text-white"
                                ></v-alert>
                            </div>
                        </v-expansion-panel-text>
                    </v-expansion-panel>
                    <v-expansion-panel
                        color="cyan"
                        class="mt-4"
                    >
                        <v-expansion-panel-title class="text-white">
                            Reported Reviews
                        </v-expansion-panel-title>
                        <v-expansion-panel-text>
                            <div v-for="review in reportedReviews" :key="review" class="pt-2 pr-2 pl-2">
                                <Review 
                                    v-model:id="review.review_id"
                                    v-model:user="review.user.login"
                                    v-model:publicationDate="review.publicationDate"
                                    v-model:rating="review.rating"
                                    v-model:contents="review.contents"
                                    @reload="reload"
                                ></Review>
                            </div>
                            <div v-if="reportedReviews.length == 0">
                                <v-alert
                                    text="There are currently no reported reviews"
                                    type="info"
                                    color="cyan"
                                    class="text-white"
                                ></v-alert>
                            </div>
                        </v-expansion-panel-text>
                    </v-expansion-panel>
                    <v-expansion-panel
                        color="cyan"
                        class="mt-4"
                    >
                        <v-expansion-panel-title class="text-white">
                            Reported Comments
                        </v-expansion-panel-title>
                        <v-expansion-panel-text>
                            <div v-for="comment in reportedComments" :key="comment" class="pt-2 pr-2 pl-2">
                                <v-card 
                                    class="mx-auto"
                                    variant="outlined"
                                    :subtitle="comment.publicationDate"
                                >
                                    <template v-slot:prepend>
                                        <v-avatar size="40">
                                            <v-img :src=getProfilePicUrl(comment.user.user_id)></v-img>
                                        </v-avatar>
                                    </template>
                                    <template v-slot:title>
                                        <span class="link" @click="navigateTo({
                                            name: 'profile',
                                            params: {
                                                userId: comment.user.user_id
                                            }
                                        })">{{ comment.user.login }}</span>
                                    </template>
                                    <v-card-text>{{ comment.contents }}</v-card-text>
                                    <v-card-actions>
                                        <v-btn icon="mdi-check" color="green" @click="allowComment(comment.comment_id)"></v-btn>
                                        <v-btn icon="mdi-close" color="red" @click="blockComment(comment.comment_id)"></v-btn>
                                        <v-btn color="red" text="Block user" @click=""></v-btn>
                                    </v-card-actions>
                                </v-card>
                            </div>
                            <div v-if="reportedComments.length == 0">
                                <v-alert
                                    text="There are currently no reported comments"
                                    type="info"
                                    color="cyan"
                                    class="text-white"
                                ></v-alert>
                            </div>
                        </v-expansion-panel-text>
                    </v-expansion-panel>
                </v-expansion-panels>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import AdminService from '@/services/AdminService';
import Review from './Review.vue';

export default {
    data() {
        return {
            suggestions: null,
            reportedReviews: null,
            reportedComments: null
        }
    },
    components: {
        Review
    },
    async mounted() {
        this.suggestions = (await AdminService.getSuggestions()).data
        this.reportedReviews = (await AdminService.getReportedReviews()).data
        this.reportedComments = (await AdminService.getReportedComments()).data
    },
    methods: {
        getSuggestionImgUrl(id) {
            return `http://localhost:8080/rating-attractions/admin/suggestion/${id}/image`
        },
        getProfilePicUrl(id) {
            return `http://localhost:8080/rating-attractions/users/${id}/profilepic`
        },
        async approve(id) {
            const response = await AdminService.approveSuggestion(id)
            this.suggestions = (await AdminService.getSuggestions()).data
        },
        async discard(id) {
            const response = await AdminService.discardSuggestion(id)
            this.suggestions = (await AdminService.getSuggestions()).data
        },
        async allowComment(id) {
            const respone = await AdminService.allowComment(id)
            this.reportedComments = (await AdminService.getReportedComments()).data
        },
        async blockComment(id) {
            const respone = await AdminService.blockComment(id)
            this.reportedComments = (await AdminService.getReportedComments()).data
        },
        async reload() {
            this.reportedReviews = (await AdminService.getReportedReviews()).data
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
    background-color: white; 
}
</style>