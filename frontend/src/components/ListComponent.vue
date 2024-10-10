<script setup>
import { useRouter } from 'vue-router';

const listId = defineModel('listId')
const name = defineModel('name')
const description = defineModel('description')
const user = defineModel('user')
const imagesUrls = defineModel('imagesUrls')
const router = useRouter()

function navigateTo(route) {
    router.push(route)
}

function getProfilePicUrl(id) {
    return `http://localhost:8080/rating-attractions/users/${id}/profilepic`
}

function getAttractionImgUrl(id) {
    return `http://localhost:8080/rating-attractions/attractions/${id}/image`
}
</script>

<template>
    <v-card
        variant="outlined"
        hover
        @click="navigateTo({
            name: 'list',
            params: {
                listId: listId
             }
        })"
    >
        <div class="d-flex flex-no-wrap justify-space-between">
            <div>
                <v-card-title>{{ name }}</v-card-title>
                <v-card-subtitle>
                    <div class="link" 
                        @click.stop="navigateTo({
                            name: 'profile',
                            params: {
                                userId: user.user_id
                            }
                        })">
                        <v-avatar size="20">
                            <v-img :src=getProfilePicUrl(user.user_id)></v-img>
                        </v-avatar>
                        <span class="pa-1">{{ user.login }}</span>
                    </div>
                </v-card-subtitle>
                <v-card-text>{{ description }}</v-card-text>
            </div>
            <div style="width: 130px;" class="pr-4 pb-2">
                <v-row align="center" no-gutters>
                    <v-col
                        v-for="n in 4"
                        :key="n"
                        cols="6"
                    >   
                        <v-avatar
                            rounded="0"
                            class="pl-2 pt-2"
                            size="60"
                        >
                            <v-img v-if="n-1 >= imagesUrls.length" :src="getAttractionImgUrl(0)"></v-img>
                            <v-img v-else :src="getAttractionImgUrl(imagesUrls[n-1])"></v-img>
                        </v-avatar>
                    </v-col>
                </v-row>
            </div>
        </div>
    </v-card>
</template>

<style scoped>
.link{
    cursor: pointer;
    display: inline-block;
}
</style>