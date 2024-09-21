<template>
    <v-row no-gutters>
        <v-col cols="2" class = "pl-2 pr-2 pt-2 pb-2">
            <!--Miejsce na zdjecie profilowe-->
        </v-col>
        <v-col v-if="stats" cols="7" class = "pl-2 pr-2 pt-2 pb-2">
            <div class="d-flex mb-6">
                <span class="text-h2 ma-2 pa-2 me-auto">{{ this.user.login }}</span>
                <v-btn
                    v-if="userStore.isUserLoggedIn && userStore.user.user_id!=this.$route.params.userId"
                    :color="isUserFollowing ? 'grey' : 'cyan'"
                    class="text-white align-self-center"
                    @click="followUser"
                >{{ followMessage }}</v-btn>
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
import { useUserStore } from '@/stores/userStore';
export default {
    data () {
        return {
            user: null,
            stats: null,
            isUserFollowing: false
        }
    },
    computed: {
        userStore: () => useUserStore(),
        items() {
            return [
                {
                    name: `Reviews ${this.stats ? this.stats.reviewCount : ''}`,
                },
                {
                    name: `Lists ${this.stats ? this.stats.listCount : ''}`,
                },
                {
                    name: `Following ${this.stats ? this.stats.followingCount : ''}`
                },
                {
                    name: `Followers ${this.stats ? this.stats.followersCount : ''}`
                }
            ];
        },
        followMessage() { return this.isUserFollowing ? 'Following' : 'Follow' }
    },
    async mounted() {
        if(this.userStore.isUserLoggedIn) {
            this.isUserFollowing = (await UserService.isUserFollowing(this.userStore.user.user_id, this.$route.params.userId)).data
        }
        this.user = (await UserService.getUser(this.$route.params.userId)).data
        this.stats = (await UserService.getStats(this.$route.params.userId)).data
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        },
        menuOptions(i) {
            switch(i) {
                case 0:
                    this.navigateTo({
                        name: 'userReviews',
                        params: {
                            userId: this.$route.params.userId
                        }
                    })
                    break
                case 1:
                    this.navigateTo({
                        name: 'userLists',
                        params: {
                            userId: this.$route.params.userId
                        }
                    })
                    break
                case 2:
                    break
                case 3:
                    this.navigateTo({
                        name: 'userFollowers',
                        params: {
                            userId: this.$route.params.userId
                        }
                    })
            }
        },
        async followUser() {
            if(this.isUserFollowing) {
                const response = await UserService.unfollowUser(this.userStore.user.user_id, this.$route.params.userId)
            } else {
                const response = await UserService.followUser({
                    userId: this.userStore.user.user_id,
                    followedUserId: this.$route.params.userId
                })
            }
            this.isUserFollowing = !this.isUserFollowing
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