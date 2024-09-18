<template>
    <v-row no-gutters>
        <v-col cols="2" class = "pl-2 pr-2 pt-2 pb-2">
            <!--Miejsce na zdjecie profilowe-->
        </v-col>
        <v-col v-if="stats" cols="7" class = "pl-2 pr-2 pt-2 pb-2">
            <div class="d-flex mb-6">
                <span class="text-h2 ma-2 pa-2 me-auto">{{ this.user.login }}</span>
            </div>
            <div>
                <h3 class="title">Recently reviewed</h3>
                <v-row no-gutters>
                    <v-col v-for="review in stats.recentlyReviewed" :key="review.review.review_id" cols="3" class="pa-1">
                        <v-img 
                            :src="review.imageUrl" 
                            class="hover-image"
                            @click="navigateTo({
                                name: 'reviewComments',
                                params: {
                                    attractionId: review.attractionId,
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
        <v-col cols="3" class = "pl-2 pr-2 pt-2 pb-2">
            <v-card
                class="mx-auto"
            >
            <!--TODO: Wyrównać napisy do lewej a liczby do prawej-->
                <v-list>
                    <v-list-item
                        v-for="(item, i) in items"
                        :key="i"
                        :value="item"
                    >
                        <v-list-item-title @click="menuOptions(i)">{{ item.name }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
import UserService from '@/services/UserService';
export default {
    data () {
        return {
            user: null,
            stats: null,
        }
    },
    async mounted() {
        this.user = (await UserService.getUser(this.$route.params.userId)).data
        this.stats = (await UserService.getStats(this.$route.params.userId)).data
    },
    computed: {
        items() {
            return [
                {
                    name: `Reviews ${this.stats ? this.stats.reviewCount : ''}`,
                },
                {
                    name: `Lists ${this.stats ? this.stats.listCount : ''}`,
                }
            ];
        }
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        },
        menuOptions(i) {
            switch(i) {
                case 0:
                    break
                case 1:
                    this.navigateTo({
                        name: 'userLists',
                        params: {
                            userId: this.$route.params.userId
                        }
                    })
                    break
            }
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