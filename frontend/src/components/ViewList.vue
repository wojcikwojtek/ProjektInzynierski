<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="ready" class="pa-2">
            <v-card
                :title="listInfo.name"   
            >
                <template v-slot:subtitle>
                    <div class="link" 
                        @click="navigateTo({
                            name: 'profile',
                            params: {
                                userId: listInfo.user.user_id
                            }
                        })">
                        <v-icon color="primary" icon="mdi-account"></v-icon>
                        <span class="pa-1">{{ listInfo.user.login }}</span>
                    </div>
                </template>
                <v-card-text>{{ listInfo.description }}</v-card-text>
            </v-card>
            <v-divider :thickness="4"></v-divider>
            <draggable
                :list="listEntries"
                :disabled="!enabled"
                item-key="name"
                class="list-group"
                ghost-class="ghost"
                :move="checkMove"
                @start="dragging = true"
                @end="onEnd"
            >
                <template #item="{ element }">
                    <div class="list-group-item" :class="{ 'not-draggable': !enabled }">
                        <v-card
                            variant="outlined"
                        >
                            <div class="d-flex flex-no-wrap justify-space-between">
                                <div>
                                    <v-card-title>{{ element.name }}</v-card-title>
                                    <v-card-subtitle>
                                        {{ element.country }}-{{ element.city }}-{{ element.location }}
                                    </v-card-subtitle>
                                    <v-card-text>
                                        <v-rating
                                            v-if="element.ratingAndId['0']!=0"
                                            v-model="element.ratingAndId['0']"
                                            half-increments
                                            readonly
                                            size="small"
                                            density="compact"
                                            color="cyan"
                                        ></v-rating><br>
                                        <v-btn 
                                            v-if="element.ratingAndId['1']!=0"
                                            class="bg-cyan text-white"
                                            text="Read review"
                                            size="small"
                                            @click="navigateTo({
                                                name: 'reviewComments',
                                                params: {
                                                    attractionId: element.attraction_id,
                                                    reviewId: element.ratingAndId['1']
                                                }
                                            })"></v-btn>
                                    </v-card-text>
                                </div>
                                <v-avatar 
                                    class="ma-3"
                                    rounded="0"
                                    size="125"
                                >
                                    <v-img :src="element.imageUrl"></v-img>
                                </v-avatar>
                            </div>
                        </v-card>
                    </div>
                </template>
            </draggable>
        </v-col>
    </v-row>
</template>

<script>
import ListService from '@/services/ListService';
import UserService from '@/services/UserService';
import draggable from 'vuedraggable'
export default {
    data () {
        return {
            listInfo: null,
            listEntries: null,
            ready: false,
            enabled: true,
            dragging: false
        }
    },
    components: {
        draggable
    },
    async mounted() {
        this.listInfo = (await ListService.getList(this.$route.params.listId)).data
        this.listEntries = (await ListService.getListEntries(this.$route.params.listId)).data
        console.log(this.listEntries)
        const reviews = (await UserService.getReviews(this.listInfo.user.user_id)).data
        this.listEntries = this.listEntries.map(entry => ({
            ...entry,
            ratingAndId: this.findRating(entry.attraction_id, reviews)
        }))
        this.ready = true
    },
    methods: {
        navigateTo(route) {
            console.log(route)
            this.$router.push(route)
        },
        findRating(attractionId, reviews) {
            const review = reviews.find(element => element.attractionId === attractionId);
            return review ? [review.review.rating, review.review.review_id] : [0, 0];
        },
        checkMove(e) {
            window.console.log("Future index: " + e.draggedContext.futureIndex);
        },
        onEnd() {
            this.dragging = false
            console.log(this.listEntries)
        }
    }
}
</script>

<style scoped>
.link{
    cursor: pointer;
    display: inline-block;
}
.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.not-draggable {
  cursor: no-drop;
}
</style>