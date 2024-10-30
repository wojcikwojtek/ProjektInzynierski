<template>
    <div class="text-center">
        <v-btn block class="bg-cyan text-white white elevation-5 mb-2" @click="openDialog">
            Add a review
        </v-btn>

        <v-dialog
            v-model="dialog"
            width="auto"
        >
            <v-sheet
                class="text-center mx-auto"
                width="500"
                rounded="lg"
            >
                <v-toolbar flat dense class="bg-cyan text-white" height="50">
                    <v-toolbar-title>Add a review</v-toolbar-title>
                </v-toolbar>
                <div class="pa-4 bg-grey-darken-4">
                    <v-rating
                        v-model="rating"
                        half-increments
                        hover
                        color="cyan"
                    ></v-rating>
                    <div class="text-red" v-if="error" v-html="error"></div>
                    <v-textarea 
                        :rules="[rules.required]"
                        v-model="contents"
                        label="Contents"
                        auto-grow
                    ></v-textarea>
                    <v-btn
                        class="ms-auto bg-cyan text-white"
                        text="Publish"
                        @click="publishReview"
                    ></v-btn>
                </div>
            </v-sheet>
        </v-dialog>
    </div>
</template>

<script>
import { useUserStore } from '@/stores/userStore';
import ReviewService from '@/services/ReviewService';
export default {
    data () {
        return {
            dialog: false,
            rating: 0,
            contents: null,
            error: null,
            rules: {
                required: value => !!value || 'Field is required'
            }
        }
    },
    computed: {
        userStore: () => useUserStore()
    },
    methods: {
        openDialog() {
            if(this.userStore.isUserLoggedIn) {
                this.dialog = true
            } else {
                this.$router.push({name: 'login'})
            }
        },
        async publishReview() {
            if(this.rating == 0) {
                this.error = 'Please choose a rating'
                return
            }
            if(this.contents == null) return
            const response = await ReviewService.addReview({
                userId: this.userStore.user.user_id,
                attractionId: this.$route.params.attractionId,
                rating: this.rating,
                contents: this.contents
            })
            this.$emit('publishEvent')
            this.dialog = false
        }
    }
}
</script>

<style scoped>
</style>