<script setup>
import ListService from '@/services/ListService';
import { onMounted, ref } from 'vue';
import { useUserStore } from '@/stores/userStore';
import ListComponent from './ListComponent.vue';

const tab = defineModel('tab')
const items = ref([])
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
</script>

<template>
    <v-infinite-scroll
        mode="manual"
        @load="load"
    >
        <template v-for="(item, index) in items" :key="item">
            <div class="pr-4 pl-4 pb-2">
                <list-component
                    v-model:listId="item.list_id"
                    v-model:name="item.name"
                    v-model:description="item.description"
                    v-model:user="item.user"
                    v-model:imagesUrls="item.imagesUrls"    
                ></list-component>
            </div>
        </template>
        <template v-slot:empty>
            <span>No more items!</span>
        </template>
    </v-infinite-scroll>
</template>

<style scoped>
.link{
    cursor: pointer;
    display: inline-block;
}
</style>