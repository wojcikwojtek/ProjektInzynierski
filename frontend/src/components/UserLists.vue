<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="listInfo" class="pa-2">
            <div v-for="list in listInfo" :key="list.list_id" class="pa-1">
                <v-card
                    :title="list.name"
                    variant="outlined"
                    hover
                    @click="navigateTo({
                        name: 'list',
                        params: {
                            listId: list.list_id
                        }
                    })"
                >
                    <template v-slot:subtitle>
                        <div class="link" 
                            @click="navigateTo({
                                name: 'profile',
                                params: {
                                    userId: list.user.user_id
                                }
                            })">
                            <v-icon color="primary" icon="mdi-account"></v-icon>
                            <span class="pa-1">{{ list.user.login }}</span>
                        </div>
                    </template>
                    <v-card-text>{{ list.description }}</v-card-text>
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