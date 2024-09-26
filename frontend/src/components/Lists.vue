<template>
    <v-row no-gutters>
        <v-col cols="1" class="pa-2">
        </v-col>
        <v-col cols="10" class="pa-4">
            <v-sheet border="md">
                <v-toolbar density="compact" class="bg-cyan text-white">
                    <v-tabs
                        v-model="tab"
                    >
                        <v-tab value="one">Most Popular</v-tab>
                        <v-tab value="two">Newest</v-tab>
                        <v-tab value="three">Friends lists</v-tab>
                    </v-tabs>
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
                    <v-tabs-window-item value="three">
                        Dupa3
                    </v-tabs-window-item>
                </v-tabs-window>
            </v-sheet>
        </v-col>
    </v-row>
</template>

<script>
import ListInfiniteScroll from './ListInfiniteScroll.vue';

export default {
    data() {
        return {
            tab: null
        }
    },
    components: {
        ListInfiniteScroll
    }
}
</script>

<style scoped>
</style>