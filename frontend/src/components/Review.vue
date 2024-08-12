<script setup>
const user = defineModel('user')
const publicationDate = defineModel('publicationDate')
const rating = defineModel('rating')
const contents = defineModel('contents')
</script>

<template>
    <v-card 
        class="mx-auto"
        variant="outlined"
        :title="user"
        :subtitle="publicationDate"
    >
        <template v-slot:prepend>
            <v-icon color="primary" icon="mdi-account"></v-icon>
        </template>
        <template v-slot:append>
            <p>{{ rating }}/10</p>
        </template>
        <v-card-text>{{ contents }}</v-card-text>
    </v-card>
</template>

<style scoped>
</style>