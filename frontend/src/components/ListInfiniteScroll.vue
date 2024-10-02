<script setup>
import ListService from '@/services/ListService';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useUserStore } from '@/stores/userStore';

const tab = defineModel('tab')
const items = ref([])
const placeholderImageUrl = ref("https://t3.ftcdn.net/jpg/02/68/55/60/360_F_268556012_c1WBaKFN5rjRxR2eyV33znK4qnYeKZjm.jpg")
const router = useRouter()
const userStore = useUserStore()

onMounted(async () => {
    if(tab.value == 'one') {
        items.value = (await ListService.getTenMostPopular(0)).data
    } else if(tab.value == 'two') {
        items.value = (await ListService.getTenNewest(0)).data
    } else if(tab.value == 'three') {
        items.value = (await ListService.getTenFriendsLists(0, userStore.user.user_id)).data
    }
})

async function load({done}) {
    var response = []
    if(tab.value == 'one') {
        response = (await ListService.getTenMostPopular(items.value.length)).data
    } else if(tab.value == 'two') {
        response = ((await ListService.getTenNewest(items.value.length)).data)
    } else if(tab.value == 'three') {
        response = (await ListService.getTenFriendsLists(items.value.length, userStore.user.user_id)).data
    }
    if(response.length == 0) { 
        done('empty')
        return
    }
    items.value.push(...response)

    done('ok')
}

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
    <v-infinite-scroll
        mode="manual"
        @load="load"
    >
        <template v-for="(item, index) in items" :key="item">
            <div class="pr-4 pl-4 pb-2">
                <v-card
                    variant="outlined"
                    hover
                    @click="navigateTo({
                        name: 'list',
                        params: {
                            listId: item.list_id
                        }
                    })"
                >
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title>{{ item.name }}</v-card-title>
                            <v-card-subtitle>
                                <div class="link" 
                                    @click.stop="navigateTo({
                                        name: 'profile',
                                        params: {
                                            userId: item.user.user_id
                                        }
                                    })">
                                        <v-avatar size="20">
                                            <v-img :src=getProfilePicUrl(item.user.user_id)></v-img>
                                        </v-avatar>
                                        <span class="pa-1">{{ item.user.login }}</span>
                                </div>
                            </v-card-subtitle>
                            <v-card-text>{{ item.description }}</v-card-text>
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
                                        <v-img v-if="n-1 >= item.imagesUrls.length" :src="getAttractionImgUrl(0)"></v-img>
                                        <v-img v-else :src="getAttractionImgUrl(item.imagesUrls[n-1])"></v-img>
                                    </v-avatar>
                                </v-col>
                            </v-row>
                        </div>
                    </div>
                </v-card>
            </div>
        </template>
        <template v-slot:empty>
            <v-alert type="warning">No more items!</v-alert>
        </template>
    </v-infinite-scroll>
</template>

<style scoped>
.link{
    cursor: pointer;
    display: inline-block;
}
</style>