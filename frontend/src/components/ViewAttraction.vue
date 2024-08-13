<template>
    <v-row no-gutters>
        <v-col cols="3" align-self="start" class = "pl-2 pr-2 pt-2 pb-2">
            <v-img v-if="attraction" :src="attraction.imageUrl"></v-img>
            <p v-else>Loading image...</p>
        </v-col>
        <v-col v-if="attraction" cols="6" class="pl-2 pr-2 pt-2 pb-2">
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
                <div v-for="review in this.reviews" :key="review.review_id" class="pl-2 pr-2 pt-2 pb-2">
                    <Review
                        v-model:user="review.user.login"
                        v-model:publicationDate="review.publicationDate"
                        v-model:rating="review.rating"
                        v-model:contents="review.contents"
                    ></Review>
                </div>
            </div>
            <div v-else>
                This attraction has no reviews
            </div>
        </v-col>
        <v-col cols="3" class = "pl-2 pr-2 pt-2 pb-2">
            <AddReview @publish-event="reloadReviews"></AddReview>
            <v-btn block class="bg-cyan text-white">Add to lists</v-btn>
        </v-col>
    </v-row>
</template>
  
<script>
import AttractionService from '@/services/AttractionService';
import Review from './Review.vue';
import AddReview from './AddReview.vue';
export default {
    data () {
        return {
            attraction: null,
            reviews: null
        }
    },
    components: {
        Review,
        AddReview
    },
    async mounted () {
    //TODO: zrobic try catche i errory
        const attractionId = this.$route.params.attractionId
        this.attraction = (await AttractionService.getAttractionById(attractionId)).data
        this.reviews = (await AttractionService.getAttractionReviews(attractionId)).data
    },
    methods: {
        async reloadReviews() {
            this.reviews = (await AttractionService.getAttractionReviews(this.$route.params.attractionId)).data
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
    background-color: #000; 
}
</style>
  