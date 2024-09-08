<template>
    <v-row no-gutters>
        <v-col cols="3">
            <v-btn 
                class="bg-cyan text-white ma-2 pa-2"
                text="Go to attraction"
                @click="navigateTo({ 
                    name: 'attraction', 
                    params: {
                        attractionId: this.$route.params.attractionId
                        }
                })"
            ></v-btn>
        </v-col>
        <v-col cols="6" v-if="review" class="pl-2 pr-2 pt-2 pb-2">
            <div>
                <Review
                    v-model:id="review.review_id"
                    v-model:user="review.user.login"
                    v-model:publicationDate="review.publicationDate"
                    v-model:rating="review.rating"
                    v-model:contents="review.contents"
                ></Review>
            </div>
            <div v-if="isUserLoggedIn">
                <h2 class="title">Add comment</h2>
                <v-textarea 
                    :rules="[rules.required]"
                    v-model="contents"
                    row-height="20"
                    rows="2"
                    label="Contents"
                    variant="outlined"
                    auto-grow
                ></v-textarea>
                <div class="d-flex justify-end">
                    <v-btn
                        class="ms-auto bg-cyan text-white"
                        text="Publish"
                        @click="publishComment"
                    ></v-btn>
                </div>
            </div>
            <h2 class="title">Comments</h2>
            <div v-for="comment in this.comments" :key="comment.comment_id" class="pl-2 pr-2 pt-2 pb-2">
                <v-card 
                    class="mx-auto"
                    variant="outlined"
                    :title="comment.user.login"
                    :subtitle="comment.publicationDate"
                >
                    <template v-slot:prepend>
                        <v-icon color="primary" icon="mdi-account"></v-icon>
                    </template>
                    <v-card-text>{{ comment.contents }}</v-card-text>
                </v-card>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import ReviewService from '@/services/ReviewService';
import Review from './Review.vue';
import { useUserStore } from '@/stores/userStore';
import CommentService from '@/services/CommentService';
import AttractionService from '@/services/AttractionService';

export default {
    data () {
        return {
            review: null,
            comments: null,
            contents: null,
            imageUrl: null,
            rules: {
                required: value => !!value || 'Field is required'
            }
        }
    },
    computed: {
        userStore: () => useUserStore(),
        isUserLoggedIn() {
            return this.userStore.isUserLoggedIn
        },
        reviewId() {
            return this.$route.params.reviewId
        }
    },
    components: {
        Review
    },
    async mounted () {
    //TODO: zrobic try catche i errory
        this.review = (await ReviewService.getReview(this.reviewId)).data
        this.comments = (await ReviewService.getComments(this.reviewId)).data
        this.imageUrl = (await AttractionService.getAttractionImage(this.route.params.attractionId)).data
    },
    methods: {
        async reloadComments() {
            this.comments = (await ReviewService.getComments(this.reviewId)).data
        },
        async publishComment() {
            if(this.contents == null) {
                return
            }
            const respone = await CommentService.addComment({
                contents: this.contents,
                userId: this.userStore.user.user_id,
                reviewId: this.reviewId
            })
            this.contents = null
            this.reloadComments()
        },
        navigateTo(route) {
            this.$router.push(route)
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