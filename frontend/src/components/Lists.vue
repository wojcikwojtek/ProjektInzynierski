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
            </v-sheet>
        </v-col>
    </v-row>
</template>

<script>
export default {
    data() {
        return {
            tab: null
        }
    }
}
</script>

<style scoped>
</style>