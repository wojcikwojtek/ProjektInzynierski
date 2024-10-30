<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="reviews" class="pa-2">
            <div class="white elevation-5 pa-3 bg-grey-darken-4" style="min-height: 85vh;">
            <div v-for="element in reviews" :key="element.review.review_id" class="pa-1">
                <v-card
                    class="mx-auto"
                    variant="outlined"
                    hover
                    @click="navigateTo({
                        name: 'reviewComments',
                        params: {
                            attractionId: element.attractionId,
                            reviewId: element.review.review_id
                        }
                    })"
                >
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title>{{ element.attractionName }}</v-card-title>
                            <v-card-subtitle>
                                <v-rating
                                    v-model="element.review.rating"
                                    half-increments
                                    readonly
                                    size="small"
                                    density="compact"
                                    color="cyan"
                                ></v-rating>
                            </v-card-subtitle>
                            <v-card-text>{{ element.review.contents }}</v-card-text>
                        </div>
                        <v-avatar 
                            class="ma-3 align-self-center"
                            rounded="0"
                            size="150"
                        >
                            <v-img :src="getAttractionImgUrl(element.attractionId)"></v-img>
                        </v-avatar>
                    </div>
                </v-card>
            </div>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import UserService from '@/services/UserService';
export default {
    data() {
        return {
            reviews: null
        }
    },
    async mounted() {
        this.reviews = (await UserService.getReviews(this.$route.params.userId)).data
    },
    methods: {
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