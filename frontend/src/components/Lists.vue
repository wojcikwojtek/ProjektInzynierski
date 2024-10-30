<template>
    <v-row no-gutters>
        <v-col cols="2" class="pa-2">
        </v-col>
        <v-col cols="8" class="pa-4">
            <div class="white elevation-5 bg-grey-darken-4">
                <v-toolbar density="compact" class="bg-cyan text-white">
                    <v-tabs
                        v-model="tab"
                    >
                        <v-tab value="one">Most Popular</v-tab>
                        <v-tab value="two">Newest</v-tab>
                        <v-tab value="three">Friends lists</v-tab>
                    </v-tabs>
                    <v-spacer></v-spacer>
                    <v-toolbar-items>
                        <v-btn
                            append-icon="mdi-plus"
                            @click="navigateTo({
                                name: 'listCreate'
                            })"
                        >
                            Create new
                        </v-btn>
                    </v-toolbar-items>
                </v-toolbar>
                <v-tabs-window v-model="tab">
                    <v-tabs-window-item value="one">
                        <ListInfiniteScroll
                            v-model:tab="tab"
                        ></ListInfiniteScroll>
                    </v-tabs-window-item>
                    <v-tabs-window-item value="two">
                        <ListInfiniteScroll
                            v-model:tab="tab"
                        ></ListInfiniteScroll>
                    </v-tabs-window-item>
                    <v-tabs-window-item value="three" v-if="userStore.isUserLoggedIn">
                        <ListInfiniteScroll
                            v-model:tab="tab"
                        ></ListInfiniteScroll>
                    </v-tabs-window-item>
                </v-tabs-window>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import { useUserStore } from '@/stores/userStore';
import ListInfiniteScroll from './ListInfiniteScroll.vue';

export default {
    data() {
        return {
            tab: null
        }
    },
    components: {
        ListInfiniteScroll
    },
    computed: {
        userStore: () => useUserStore()
    },
    methods: {
        navigateTo(route) {
            this.$router.push(route)
        }
    }
}
</script>

<style scoped>
</style>