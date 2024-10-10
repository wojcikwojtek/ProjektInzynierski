<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="listInfo" class="pa-2">
            <div v-for="list in listInfo" :key="list.list_id" class="pa-1">
                <ListComponent
                    v-model:listId="list.list_id"
                    v-model:name="list.name"
                    v-model:description="list.description"
                    v-model:user="list.user"
                    v-model:imagesUrls="list.imagesUrls"
                ></ListComponent>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import UserService from '@/services/UserService';
import ListComponent from './ListComponent.vue';

export default {
    data () {
        return {
            listInfo: null
        }
    },
    async mounted() {
        this.listInfo = (await UserService.getLists(this.$route.params.userId)).data
    },
    components: {
        ListComponent
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