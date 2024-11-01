<template>
    <v-row no-gutters>
        <v-col cols="3" align-self="start" class = "pl-2 pr-2 pt-2 pb-2">
            <v-img 
                v-if="attraction" 
                :src="attractionImage"
                aspect-ratio="16/9"
                class="elevation-5"></v-img>
            <p v-else>Loading image...</p>
        </v-col>
        <v-col v-if="attraction" cols="6" class="pl-2 pr-2 pt-2 pb-2">
            <div class="white elevation-5 pa-6 bg-grey-darken-4">
            <div>
                <h1>{{ attraction.name }}</h1>
                <p>Country: {{ attraction.country }}</p>
                <p>City: {{ attraction.city }}</p>
                <p>Location: {{ attraction.location }}</p>
                <p>Description:</p>
                <p>{{ attraction.description }}</p>
            </div>
            <h2 class="reviews-title">Reviews</h2>
            <div v-if="reviews">
                <v-infinite-scroll
                    mode="manual"
                    @load="load"
                >
                    <template v-for="(review, index) in reviews" :key="review.review_id">
                        <div class="pl-2 pr-2 pt-2 pb-2">
                            <Review
                                v-model:id="review.review_id"
                                v-model:user="review.user.login"
                                v-model:publicationDate="review.publicationDate"
                                v-model:rating="review.rating"
                                v-model:contents="review.contents"
                            ></Review>
                        </div>
                    </template>
                    <template v-if="reviews.length == 0">
                        <v-alert
                            text="There are currently no reviews for this attraction"
                            type="info"
                            color="cyan"
                            class="text-white"
                        ></v-alert>
                    </template>
                </v-infinite-scroll>
            </div>
            <div v-else>
                This attraction has no reviews
            </div>
            </div>
        </v-col>
        <v-col cols="3" class = "pl-2 pr-2 pt-2 pb-2">
            <AddReview @publish-event="reloadReviews"></AddReview>
            <AddToLists></AddToLists>
        </v-col>
    </v-row>
</template>
  
<script>
import AttractionService from '@/services/AttractionService';
import Review from './Review.vue';
import AddReview from './AddReview.vue';
import AddToLists from './AddToLists.vue';
export default {
    data () {
        return {
            attraction: null,
            reviews: null
        }
    },
    components: {
        Review,
        AddReview,
        AddToLists
    },
    async mounted () {
    //TODO: zrobic try catche i errory
        const attractionId = this.$route.params.attractionId
        this.attraction = (await AttractionService.getAttractionById(attractionId)).data
        this.reviews = (await AttractionService.getAttractionReviews(attractionId, 0)).data
    },
    computed: {
        attractionImage() { return `http://localhost:8080/rating-attractions/attractions/${this.$route.params.attractionId}/image` }
    },
    methods: {
        async reloadReviews() {
            this.reviews = (await AttractionService.getAttractionReviews(this.$route.params.attractionId)).data
        },
        async load({ done }) {
            const response = (await AttractionService.getAttractionReviews(this.$route.params.attractionId, this.reviews.length)).data
            if(response.length == 0) { 
                done('empty')
                return
            }
            this.reviews.push(...response)

            done('ok')
        }
    }
}
</script>
  
<style scoped>
.reviews-title {
    position: relative;
    padding-bottom: 8px; 
    margin-bottom: 16px; 
}

.reviews-title::after {
    content: '';
    position: absolute;
    left: 0;
    bottom: 0;
    width: 100%;
    height: 2px; 
    background-color: white; 
}
</style>
  