<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" class="pa-2">
            <v-list lines="two">
                <v-list-item
                    v-for="element in userList"
                    :key="element.user.user_id"
                >
                    <template v-slot:prepend>
                        <v-avatar size="30">
                            <v-img :src=getProfilePicUrl(element.user.user_id)></v-img>
                        </v-avatar>
                    </template>
                    <template v-slot:title>
                        <span class="link" @click="navigateTo({
                            name: 'profile',
                            params: {
                                userId: element.user.user_id
                            }
                        })">{{ element.user.login }}</span>
                    </template>
                    <template v-slot:append v-if="userStore.isUserLoggedIn && element.user.user_id!=userStore.user.user_id">
                        <v-icon 
                            :color="element.following ? 'cyan' : 'grey'" 
                            :icon="element.following ? 'mdi-check-circle' : 'mdi-plus-circle'"
                            @click="follow(element)"
                        ></v-icon>
                    </template>
                </v-list-item>
                <v-divider></v-divider>
            </v-list>
        </v-col>
    </v-row>
</template>

<script>
import UserService from '@/services/UserService';
import { useUserStore } from '@/stores/userStore';

export default {
    data() {
        return {
            userList: null
        }
    },
    async mounted() {
        const currentPath = this.$router.currentRoute.value.path
        const userId = this.userStore.isUserLoggedIn ? this.userStore.user.user_id : 0
        if(currentPath.includes('followers')) {
            this.userList = (await UserService.getFollowers(this.$route.params.userId, userId)).data
        } else if(currentPath.includes('following')) {
            this.userList = (await UserService.getFollowing(this.$route.params.userId, userId)).data
        }
    },
    computed: {
        userStore: () => useUserStore()
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        },
        async follow(element) {
            if(element.following) {
                const response = await UserService.unfollowUser(this.userStore.user.user_id, element.user.user_id)
            } else {
                const response = await UserService.followUser({
                    userId: this.userStore.user.user_id,
                    followedUserId: element.user.user_id
                })
            }
            element.following = !element.following
        },
        getProfilePicUrl(id) {
            return `http://localhost:8080/rating-attractions/users/${id}/profilepic`
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