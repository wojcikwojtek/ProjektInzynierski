<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="listInfo" class="pa-2">
            <div v-for="list in listInfo" :key="list.list_id" class="pa-1">
                <v-card
                    variant="outlined"
                    hover
                    @click="navigateTo({
                        name: 'list',
                        params: {
                            listId: list.list_id
                        }
                    })"
                >
                    <div class="d-flex flex-no-wrap justify-space-between">
                        <div>
                            <v-card-title>{{ list.name }}</v-card-title>
                            <v-card-subtitle>
                                <div class="link" 
                                    @click.stop="navigateTo({
                                        name: 'profile',
                                        params: {
                                            userId: list.user.user_id
                                        }
                                    })">
                                        <v-avatar size="20">
                                            <v-img :src=getProfilePicUrl(list.user.user_id)></v-img>
                                        </v-avatar>
                                        <span class="pa-1">{{ list.user.login }}</span>
                                </div>
                            </v-card-subtitle>
                            <v-card-text>{{ list.description }}</v-card-text>
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
                                        <v-img v-if="n-1 >= list.imagesUrls.length" :src="getAttractionImgUrl(0)"></v-img>
                                        <v-img v-else :src="getAttractionImgUrl(list.imagesUrls[n-1])"></v-img>
                                    </v-avatar>
                                </v-col>
                            </v-row>
                        </div>
                    </div>
                </v-card>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import UserService from '@/services/UserService';

export default {
    data () {
        return {
            listInfo: null
        }
    },
    async mounted() {
        this.listInfo = (await UserService.getLists(this.$route.params.userId)).data
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        },
        getProfilePicUrl(id) {
            return `http://localhost:8080/rating-attractions/users/${id}/profilepic`
        },
        getAttractionImgUrl(id) {
            return `http://localhost:8080/rating-attractions/attractions/${id}/image`
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