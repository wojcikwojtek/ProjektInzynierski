<template>
    <v-row no-gutters>
        <v-col cols="2" class="pa-2">
        </v-col>
        <v-col v-if="recentReviews" cols="8" class="pa-2">
            <h2 class="title">Friends recent activity</h2>
            <v-slide-group
                v-model="recentReviews"
                class="pa-4"
                show-arrows
            >
                <v-slide-group-item
                    v-for="element in recentReviews"
                    :key="element.review.review_id"
                >
                <div class="pr-2">
                    <v-img 
                        :src="getAttractionImgUrl(element.attractionId)"
                        :width="220"
                        aspect-ratio="16/9"
                        class="hover-image"
                        @click="navigateTo({
                            name: 'reviewComments',
                            params: {
                                attractionId: element.attractionId,
                                reviewId: element.review.review_id
                            }
                        })"
                    ></v-img>
                    <div class="d-flex justify-center">
                        <v-rating
                            v-model="element.review.rating"
                            half-increments
                            readonly
                            size="small"
                            density="compact"
                            color="cyan"
                        ></v-rating>
                    </div>
                </div>
                </v-slide-group-item>
            </v-slide-group>
        </v-col>
    </v-row>
</template>

<script>
import UserService from '@/services/UserService';
import { useUserStore } from '@/stores/userStore';

export default {
    data() {
        return {
            recentReviews: null
        }
    },
    computed: {
        userStore: () => useUserStore()
    },
    async mounted() {
        this.recentReviews = (await UserService.getFollowedUsersRecentReviews(this.userStore.user.user_id)).data
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
  border: 4px solid #00BCD4; 
}
</style>