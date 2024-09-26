<script setup>
import ListService from '@/services/ListService';
import { onMounted, ref } from 'vue';
import { useRouter } from 'vue-router';

const tab = defineModel('tab')
const items = ref([])
const router = useRouter()

onMounted(async () => {
    if(tab.value == 'one') {
        items.value = (await ListService.getTenMostPopular(0)).data
    } else if(tab.value == 'two') {
        items.value = (await ListService.getTenNewest(0)).data
    }
})

async function load({done}) {
    const response = (await ListService.getTenMostPopular(items.value.length)).data
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
</script>

<template>
    <v-infinite-scroll
        mode="manual"
        @load="load"
    >
        <template v-for="(item, index) in items" :key="item" class="pa-2">
            <v-card
                :title="item.name"
                variant="outlined"
                hover
                @click="navigateTo({
                    name: 'list',
                    params: {
                        listId: item.list_id
                    }
                })"
            >
                <template v-slot:subtitle>
                    <div class="link" 
                        @click="navigateTo({
                            name: 'profile',
                            params: {
                                userId: item.user.user_id
                            }
                        })">
                        <v-icon color="primary" icon="mdi-account"></v-icon>
                        <span class="pa-1">{{ item.user.login }}</span>
                    </div>
                </template>
                <v-card-text>{{ item.description }}</v-card-text>
            </v-card>
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