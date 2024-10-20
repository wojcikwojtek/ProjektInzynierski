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
                    <div class="link" @click="navigateTo({
                        name: 'profile',
                        params: {
                            userId: element.review.user.user_id
                        }
                    })">
                        <v-avatar size="20" class="mr-1 mb-1">
                            <v-img :src=getProfilePicUrl(element.review.user.user_id)></v-img>
                        </v-avatar>
                        <span class="pb-1">{{ element.review.user.login }}</span>
                    </div>
                    <v-responsive
                        class="hover-image"
                        :aspect-ratio="16/9"
                        style="position: relative; width: 220px; height: 124px;"
                    >
                        <v-img 
                        :src="getAttractionImgUrl(element.attractionId)"
                        cover
                        @click="navigateTo({
                            name: 'reviewComments',
                            params: {
                                attractionId: element.attractionId,
                                reviewId: element.review.review_id
                            }
                        })"
                        ></v-img>
                        <div class="image-footer">
                            {{ element.attractionName }}
                        </div>
                    </v-responsive>
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
        },
        getProfilePicUrl(id) {
            return `http://localhost:8080/rating-attractions/users/${id}/profilepic`
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

.image-footer {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  text-align: center;
  padding: 10px;
  font-size: 12px;
  opacity: 0;
  transform: translateY(100%);
  transition: transform 0.3s ease, opacity 0.3s ease;
}

.hover-image:hover .image-footer {
  opacity: 1;
  transform: translateY(0);
}

.link{
    cursor: pointer;
    display: inline-block;
}
</style>