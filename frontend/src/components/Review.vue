<script setup>
import { ref, onMounted } from 'vue';
import ReviewService from '@/services/ReviewService';
import LikeService from '@/services/LikeService';
import UserService from '@/services/UserService';
import { useUserStore } from '@/stores/userStore';
import { useRouter, useRoute } from 'vue-router';
import AdminService from '@/services/AdminService';

const id = defineModel('id')
const user = defineModel('user')
const publicationDate = defineModel('publicationDate')
const rating = defineModel('rating')
const contents = defineModel('contents')

const userStore = useUserStore()
const didUserLikeReview = ref(false)
const likeCount = ref(0)
const commentCount = ref(0)
const userId = ref(0)
const profilePic = ref(``)
const router = useRouter()
const route = useRoute()
const currentPath = router.currentRoute.value.path + ""
const emit = defineEmits(['reload'])

onMounted(async () => {
    if(userStore.isUserLoggedIn) {
        didUserLikeReview.value = (await LikeService.userLikedReview(userStore.user.user_id, id.value)).data
    }
    likeCount.value = (await ReviewService.getLikeCount(id.value)).data
    commentCount.value = (await ReviewService.getCommentCount(id.value)).data
    userId.value = (await UserService.findUser(user.value)).data.user_id
    profilePic.value = `http://localhost:8080/rating-attractions/users/${userId.value}/profilepic`
})

async function likeReview() {
    if(didUserLikeReview.value) {
        const response = await LikeService.deleteLikedReview(userStore.user.user_id, id.value)
        didUserLikeReview.value = false
        likeCount.value--
    } else {
        const response = await LikeService.likeReview({
            userId: userStore.user.user_id,
            reviewId: id.value
        })
        didUserLikeReview.value = true
        likeCount.value++
    }
}

async function report() {
    const response = await ReviewService.reportReview(id.value)
}

async function blockReview() {
    const respone = await AdminService.blockReview(id.value)
    emit('reload')
}

async function allowReview() {
    const respone = await AdminService.allowReview(id.value)
    emit('reload')
}

function navigateTo(route) {
    router.push(route)
}
</script>

<template>
    <v-card 
        class="mx-auto"
        variant="outlined"
    >
        <template v-slot:prepend>
            <v-avatar size="40">
                <v-img :src="profilePic"></v-img>
            </v-avatar>
        </template>
        <template v-slot:title>
            <span @click="navigateTo({
                name: 'profile',
                params: {
                    userId: userId
                }
            })" class="link">{{ user }}</span>
        </template>
        <template v-slot:subtitle>
            {{ publicationDate }}
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
        <v-card-text>{{ contents }}
        </v-card-text>
        <v-card-actions>
            <div v-if="!currentPath.includes('admin')">
                <v-btn
                    v-if="userStore.isUserLoggedIn"
                    size="small"
                    :color="didUserLikeReview ? 'red' : 'grey'"
                    icon="mdi-heart"
                    variant="text"
                    @click="likeReview"
                ></v-btn>
                <span v-if="userStore.isUserLoggedIn" class="subheading">{{ likeCount }}</span>
                <v-btn
                    size="small"
                    color="grey"
                    icon="mdi-comment"
                    variant="text"
                    v-if="!currentPath.includes('reviews')"
                    @click="navigateTo({
                        name: 'reviewComments',
                        params: {
                            attractionId: route.params.attractionId,
                            reviewId: id
                        }
                    })"
                ></v-btn>
                <span v-if="!currentPath.includes('reviews')" class="subheading">{{ commentCount }}</span>
                <v-btn
                    v-if="userStore.isUserLoggedIn"
                    size="small"
                    color="grey"
                    icon="mdi-flag"
                    variant="text"
                    @click="report"
                ></v-btn>
            </div>
            <div v-else>
                <v-btn icon="mdi-check" color="green" @click="allowReview"></v-btn>
                <v-btn icon="mdi-close" color="red" @click="blockReview"></v-btn>
                <v-btn color="red" text="Block user" @click=""></v-btn>
            </div>
        </v-card-actions>
    </v-card>
</template>

<style scoped>
.link{
    cursor: pointer;
}
</style>