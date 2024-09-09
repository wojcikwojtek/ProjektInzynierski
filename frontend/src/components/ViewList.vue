<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="listInfo" class="pa-2">
            <v-card 
                class="mx-auto"
                variant="outlined"
                :subtitle="listInfo.publicationDate"
            >
                <template v-slot:prepend>
                    <v-icon color="primary" icon="mdi-account"></v-icon>
                </template>
                <template v-slot:title>
                    <span class="link">{{ listInfo.user.login }}</span>
                </template>
            </v-card>
        </v-col>
    </v-row>
</template>

<script>
import ListService from '@/services/ListService';
export default {
    data () {
        return {
            listInfo: null,
            listEntries: null
        }
    },
    async mounted() {
        this.listInfo = (await ListService.getList(this.$route.params.listId)).data
        this.listEntries = (await ListService.getListEntries(this.$route.params.listId)).data
    }
}
</script>

<style scoped>
.link{
    cursor: pointer;
}
</style>