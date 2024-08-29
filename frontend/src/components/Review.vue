<script setup>
import { ref, onMounted } from 'vue';
import ReviewService from '@/services/ReviewService';
import LikeService from '@/services/LikeService';
import { useUserStore } from '@/stores/userStore';

const id = defineModel('id')
const user = defineModel('user')
const publicationDate = defineModel('publicationDate')
const rating = defineModel('rating')
const contents = defineModel('contents')

const userStore = useUserStore()
const didUserLikeReview = ref(false)
const likeCount = ref(0)

onMounted(async () => {
    if(userStore.isUserLoggedIn) {
        didUserLikeReview.value = (await LikeService.userLikedReview(userStore.user.user_id, id.value)).data
    }
    likeCount.value = (await ReviewService.getLikeCount(id.value)).data
})

async function likeReview() {
    if(didUserLikeReview.value) {
        const response = await LikeService.deleteLikedReview(userStore.user.user_id, id.value)
        didUserLikeReview.value = false
        likeCount.value = (await ReviewService.getLikeCount(id.value)).data
    } else {
        const response = await LikeService.likeReview({
            userId: userStore.user.user_id,
            reviewId: id.value
        })
        didUserLikeReview.value = true
        likeCount.value = (await ReviewService.getLikeCount(id.value)).data
    }
}
</script>

<template>
    <v-card 
        class="mx-auto"
        variant="outlined"
        :title="user"
        :subtitle="publicationDate"
    >
        <template v-slot:prepend>
            <v-icon color="primary" icon="mdi-account"></v-icon>
        </template>
        <template v-slot:append>
            <v-rating
                v-model="rating"
                half-increments
                readonly
                size="small"
                density="compact"
                color="cyan"
            ></v-rating>
        </template>
        <v-card-text>{{ contents }}</v-card-text>
        <v-card-actions v-if="userStore.isUserLoggedIn">
            <div>
                <v-btn
                    size="small"
                    :color="didUserLikeReview ? 'red' : 'grey'"
                    icon="mdi-heart"
                    variant="text"
                    @click="likeReview"
                ></v-btn>
                <span class="subheading">{{ likeCount }}</span>
            </div>
        </v-card-actions>
    </v-card>
</template>

<style scoped>
</style>