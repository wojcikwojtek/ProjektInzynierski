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
                        <v-avatar size="20">
                            <v-img :src=getProfilePicUrl(listInfo.user.user_id)></v-img>
                        </v-avatar>
                        <span class="pa-1">{{ listInfo.user.login }}</span>
                    </div>
                </template>
                <template v-slot:append v-if="userStore.isUserLoggedIn && listInfo.user.user_id == userStore.user.user_id">
                    <v-menu>
                        <template v-slot:activator="{ props }">
                            <v-btn 
                                icon="mdi-dots-vertical"
                                variant="text"
                                v-bind="props"
                            ></v-btn>
                        </template>
                        <v-list>
                            <v-list-item
                                v-for="(item, i) in items"
                                :key="i"
                                :value="item"
                            >
                                <v-list-item-title @click="menuOptions(i)">{{ item.title }}</v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </v-menu>
                </template>
                <v-card-text>{{ listInfo.description }}</v-card-text>
                <v-card-actions v-if="userStore.isUserLoggedIn">
                    <v-btn
                        size="small"
                        :color="didUserLikeList ? 'red' : 'grey'"
                        icon="mdi-heart"
                        variant="text"
                        @click="likeList"
                    ></v-btn>
                    <span class="subheading">{{ likeCount }}</span>
                </v-card-actions>
            </v-card>
            <v-divider :thickness="4"></v-divider>
            <draggable
                :list="listEntries"
                :disabled="!enabled"
                item-key="position"
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
                            v-if="!element.hidden"
                            hover
                            @click="navigateTo({
                                name: 'attraction',
                                params: {
                                    attractionId: element.attraction.attraction_id
                                }})"
                        >
                            <div class="d-flex flex-no-wrap justify-space-between">
                                <div>
                                    <v-card-title>{{ element.attraction.name }}</v-card-title>
                                    <v-card-subtitle>
                                        {{ element.attraction.country }}-{{ element.attraction.city }}-{{ element.attraction.location }}
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
                                            @click.stop="navigateTo({
                                                name: 'reviewComments',
                                                params: {
                                                    attractionId: element.attraction.attraction_id,
                                                    reviewId: element.ratingAndId['1']
                                                }
                                            })"></v-btn>
                                        <v-btn
                                            v-if="enabled"
                                            class="bg-red text-white"
                                            text="Delete"
                                            size="small"
                                            @click.stop="addToDeleted(element)"></v-btn>
                                    </v-card-text>
                                </div>
                                <v-avatar 
                                    class="ma-3"
                                    rounded="0"
                                    size="125"
                                >
                                    <v-img :src="getAttractionImgUrl(element.attraction.attraction_id)"></v-img>
                                </v-avatar>
                            </div>
                        </v-card>
                    </div>
                </template>
            </draggable>
            <div v-if="enabled" class="d-flex justify-end pa-2">
                <v-btn class="bg-cyan text-white" @click="revertChanges">Return</v-btn>
                <v-btn class="bg-cyan text-white" @click="edit">Save changes</v-btn>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import LikeService from '@/services/LikeService';
import ListService from '@/services/ListService';
import UserService from '@/services/UserService';
import { useUserStore } from '@/stores/userStore';
import draggable from 'vuedraggable'
export default {
    data () {
        return {
            listInfo: null,
            listEntries: null,
            reviews: null,
            ready: false,
            enabled: false,
            dragging: false,
            didUserLikeList: false,
            likeCount: 0,
            items: [
                { title: 'Edit' }
            ],
            deleted: []
        }
    },
    components: {
        draggable
    },
    computed: {
        userStore: () => useUserStore()
    },
    async mounted() {
        if(this.userStore.isUserLoggedIn) {
            this.didUserLikeList = (await LikeService.userLikedList(this.userStore.user.user_id, this.$route.params.listId)).data
            this.likeCount = (await ListService.getLikeCount(this.$route.params.listId)).data
        }
        this.listInfo = (await ListService.getList(this.$route.params.listId)).data
        this.listEntries = (await ListService.getListEntries(this.$route.params.listId)).data
        this.reviews = (await UserService.getReviews(this.listInfo.user.user_id)).data
        this.listEntries = this.listEntries.map(entry => ({
            ...entry,
            ratingAndId: this.findRating(entry.attraction.attraction_id, this.reviews),
            hidden: false
        }))
        this.ready = true
    },
    methods: {
        navigateTo(route) {
            if(this.enabled) return
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
        },
        async likeList() {
            if(this.didUserLikeList) {
                const response = (await LikeService.deleteLikedList(this.userStore.user.user_id, this.$route.params.listId))
                this.didUserLikeList = false
                this.likeCount--
            } else {
                const response = await LikeService.likeList({
                    userId: this.userStore.user.user_id,
                    listId: this.$route.params.listId
                })
                this.didUserLikeList = true
                this.likeCount++
            }
        },
        async edit() {
            this.deleted.forEach(element =>{
                if(this.listEntries.some(obj => obj === element)) {
                    this.deleteAttraction(element.id)
                }
            })
            this.listEntries = this.listEntries.filter(obj => !this.deleted.includes(obj))
            var index = 0
            this.listEntries.forEach(element => {
                element.position = index
                index++
            });
            const response = await ListService.editEntries({
                listId: this.$route.params.listId,
                attractions: this.listEntries
            })
            this.deleted = []
            this.enabled = false
        },
        menuOptions(i) {
            switch(i) {
                case 0:
                    this.enabled = true
                    break
            }
        },
        async deleteAttraction(id) {
            const response = await ListService.deleteEntry(id.list_id, id.attraction_id)
        },
        addToDeleted(element) {
            element.hidden = true
            this.deleted.push(element)
        },
        async revertChanges() {
            this.deleted = []
            this.ready = false
            this.listEntries = (await ListService.getListEntries(this.$route.params.listId)).data
            this.listEntries = this.listEntries.map(entry => ({
                ...entry,
                ratingAndId: this.findRating(entry.attraction.attraction_id, this.reviews),
                hidden: false
            }))
            this.enabled = false
            this.ready = true
        },
        getProfilePicUrl(id) {
            return `http://localhost:8080/rating-attractions/users/${id}/profilepic`
        },
        getAttractionImgUrl(id) {
            return `http://localhost:8080/rating-attractions/attractions/${id}/image`
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