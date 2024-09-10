<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="listInfo && listEntries" class="pa-2">
            <v-card
                variant=""
                :title="listInfo.name"   
            >
                <template v-slot:subtitle>
                    <div class="link" 
                        @click="navigateTo({
                            name: 'profile',
                            params: {
                                userId: listInfo.user.user_id
                            }
                        })">
                        <v-icon color="primary" icon="mdi-account"></v-icon>
                        <span class="pa-1">{{ listInfo.user.login }}</span>
                    </div>
                </template>
                <v-card-text>{{ listInfo.description }}</v-card-text>
            </v-card>
            <v-divider :thickness="4"></v-divider>
            <div v-for="entry in listEntries" class="pa-2">
                <v-card
                    variant="outlined"
                >
                <div class="d-flex flex-no-wrap justify-space-between">
                    <div>
                        <v-card-title>{{ entry.name }}</v-card-title>
                        <v-card-subtitle>{{ entry.country }}-{{ entry.city }}-{{ entry.location }}</v-card-subtitle>
                        <v-card-text>
                            <v-rating
                                v-if="entry.ratingAndId['0']!=0"
                                v-model="entry.ratingAndId['0']"
                                half-increments
                                readonly
                                size="small"
                                density="compact"
                                color="cyan"
                            ></v-rating><br>
                            <v-btn 
                                v-if="entry.ratingAndId['1']!=0"
                                class="bg-cyan text-white"
                                text="Read review"
                                size="small"
                                @click="navigateTo({
                                    name: 'reviewComments',
                                    params: {
                                        attractionId: entry.attraction_id,
                                        reviewId: entry.ratingAndId['1']
                                    }
                                })"></v-btn>
                        </v-card-text>
                    </div>
                    <v-avatar 
                        class="ma-3"
                        rounded="0"
                        size="125"
                    >
                        <v-img :src="entry.imageUrl"></v-img>
                    </v-avatar>
                </div>
                </v-card>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import ListService from '@/services/ListService';
import UserService from '@/services/UserService';
export default {
    data () {
        return {
            listInfo: null,
            listEntries: null
        }
    },
    async mounted() {
        this.listInfo = (await ListService.getList(this.$route.params.listId)).data
        this.listEntries = (await ListService.getListEntries(this.$route.params.listId)).data
        const reviews = (await UserService.getReviews(this.listInfo.user.user_id)).data
        this.listEntries = this.listEntries.map(entry => ({
            ...entry,
            ratingAndId: this.findRating(entry.attraction_id, reviews)
        }))
    },
    methods: {
        navigateTo(route) {
            console.log(route)
            this.$router.push(route)
        },
        findRating(attractionId, reviews) {
            const review = reviews.find(element => element.attractionId === attractionId);
            return review ? [review.review.rating, review.review.review_id] : [0, 0];
        }
    }
}
</script>

<style scoped>
.link{
    cursor: pointer;
    display: inline-block;
}
</style>