<template>
    <v-row no-gutters>
        <v-col cols="2" class = "pl-2 pr-2 pt-2 pb-2">
            <!--Miejsce na zdjecie profilowe-->
        </v-col>
        <v-col v-if="stats" cols="8" class = "pl-2 pr-2 pt-2 pb-2">
            <div class="d-flex mb-6">
                <span class="text-h2 ma-2 pa-2 me-auto">{{ userStore.user.login }}</span>
                <span class="text-h4 ma-2 pa-2">{{ stats.reviewCount }} reviews</span>
                <span class="text-h4 ma-2 pa-2">{{ stats.listCount }} lists</span>
            </div>
            <div>
                <h3 class="title">Recently reviewed</h3>
                <v-row no-gutters>
                    <v-col v-for="review in stats.recentlyReviewed" :key="review.review.review_id" cols="3" class="pa-1">
                        <!--Zmienic url recenzji na attraction/id/review/id i dodac przycisk do zobaczenia atrakcji-->
                        <v-img 
                            :src="review.imageUrl" 
                            class="hover-image"
                            @click="navigateTo({
                                name: 'reviewComments',
                                params: {
                                    reviewId: review.review.review_id
                                }
                            })">
                        </v-img>
                        <div class="d-flex justify-center">
                            <v-rating
                                v-model="review.review.rating"
                                half-increments
                                readonly
                                size="small"
                                density="compact"
                                color="cyan"
                            ></v-rating>
                        </div>
                    </v-col>
                </v-row>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import { useUserStore } from '@/stores/userStore';
import UserService from '@/services/UserService';
export default {
    data () {
        return {
            stats: null
        }
    },
    computed: {
        userStore: () => useUserStore()
    },
    async mounted() {
        this.stats = (await UserService.getStats(this.userStore.user.user_id)).data
    },
    methods: {
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
.hover-image {
  transition: border 0.3s ease; 
  cursor: pointer; 
}

.hover-image:hover {
  border: 4px solid cyan; 
}
</style>