<template>
    <v-row no-gutters>
        <v-col cols="3" claas="pa-2"></v-col>
        <v-col cols="6" class="pa-2">
            <div class="white elevation-5 pa-6">
                <h1>Create a new list</h1>
                <br>
                <v-form v-model="valid">
                    <p class="pb-3">Name</p>
                    <v-text-field v-model="name" label="Name" :rules="rules"></v-text-field>
                    <p class="pb-3">Description</p>
                    <v-textarea 
                        v-model="description"
                        row-height="20"
                        rows="2"
                        label="Description"
                        auto-grow
                        :rules="rules"
                    ></v-textarea>
                    <p class="pb-3">Add attractions</p>
                    <v-sheet 
                        border 
                        rounded 
                        color="grey-lighten-4"
                        class="d-flex align-center justify-center flex-wrap mx-auto"
                    >
                        <draggable
                            :list="attractions"
                            :disabled="!enabled"
                            item-key="position"
                            class="list-group"
                            ghost-class="ghost"
                            :move="checkMove"
                            @start="dragging = true"
                            @end="dragging = false"
                        >
                            <template #item="{ element }">
                                <div class="list-group-item" :class="{ 'not-draggable': !enabled }">
                                    <div class="pt-2 pr-2 pl-2">
                                        <v-card variant="outlined" max-height="150" class="bg-white">
                                            <div class="d-flex flex-no-wrap justify-space-between">
                                                <div>
                                                    <v-card-title>{{ element.name }}</v-card-title>
                                                    <v-card-subtitle>{{ element.country }}-{{ element.city }}-{{ element.location }}</v-card-subtitle>
                                                    <v-card-text>{{ element.description }}</v-card-text>
                                                </div>
                                                <v-avatar 
                                                    class="ma-3"
                                                    rounded="0"
                                                    size="125"
                                                >
                                                    <v-img :src="getAttractionImgUrl(element.attraction_id)"></v-img>
                                                </v-avatar>
                                            </div>
                                        </v-card>
                                    </div>
                                </div>
                            </template>
                        </draggable>
                        <div @click="dialog=true" class="open-dialog">
                            <h3>Click to add attraction</h3>
                            <div class="d-flex justify-center">
                                <v-icon icon="mdi-plus-circle-outline" size="x-large"></v-icon>
                            </div>
                        </div>
                    </v-sheet>
                    <div class="d-flex justify-center pt-3">
                        <v-btn class="bg-cyan text-white" @click="submit">Submit</v-btn>
                    </div>
                </v-form>
                <v-dialog
                    v-model="dialog"
                    width="auto"
                >
                    <v-sheet
                        class="mx-auto pa-3"
                        width="500"
                        rounded="lg"
                    >
                        <v-text-field 
                            append-inner-icon="mdi-magnify"
                            density="comfortable"
                            label="search attractions"
                            variant="solo"
                            single line
                            v-model="searchedTerm"
                            @click:append-inner="searchAttractions"
                            @keyup.enter="searchAttractions"
                        >
                        </v-text-field>
                        <div v-for="result in searchResults" :key="result.attraction_id" class="pa-2">
                            <v-card variant="outlined" max-height="150" hover>
                                <div class="d-flex flex-no-wrap justify-space-between"
                                    @click="addAttraction(result)"
                                >
                                    <div>
                                        <v-card-title>{{ result.name }}</v-card-title>
                                        <v-card-subtitle>{{ result.country }}-{{ result.city }}-{{ result.location }}</v-card-subtitle>
                                        <v-card-text>{{ result.description }}</v-card-text>
                                    </div>
                                    <v-avatar 
                                        class="ma-3"
                                        rounded="0"
                                        size="125"
                                    >
                                        <v-img :src="getAttractionImgUrl(result.attraction_id)"></v-img>
                                    </v-avatar>
                                </div>
                            </v-card>
                        </div>
                    </v-sheet>
                </v-dialog>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import AttractionService from '@/services/AttractionService';
import ListService from '@/services/ListService';
import { useUserStore } from '@/stores/userStore';
import draggable from 'vuedraggable'
export default {
    data() {
        return {
            valid: false,
            dialog: false,
            name: '',
            description: '',
            attractions: [],
            searchedTerm: '',
            searchResults: null,
            enabled: true,
            dragging: false,
            rules: [
                value => {
                    if(value) return true
                    return `Don't leave an empty field`
                }
            ]
        }
    },
    components: {
        draggable
    },
    computed: {
        userStore: () => useUserStore()
    },
    methods: {
        async searchAttractions() {
            if(this.searchedTerm == '') return
            const response = await AttractionService.search(this.searchedTerm)
            this.searchResults = response.data
        },
        getAttractionImgUrl(id) {
            return `http://localhost:8080/rating-attractions/attractions/${id}/image`
        },
        addAttraction(result) {
            if(!this.attractions.includes(result)) {
                this.attractions.push(result)
            }
            this.dialog = false
        },
        checkMove(e) {
            window.console.log("Future index: " + e.draggedContext.futureIndex);
        },
        async submit() {
            if(!this.valid) {
                return
            }
            const attractionIds = []
            this.attractions.forEach(attraction => {
                attractionIds.push(attraction.attraction_id)
            })
            const response = await ListService.createList({
                name: this.name,
                description: this.description,
                userId: this.userStore.user.user_id,
                attractionIds: attractionIds
            })
            this.$router.push({
                name: 'list',
                params: {
                    listId: response.data.list_id
                }
            })
        }
    }
}
</script>

<style scoped>
.open-dialog:hover{
    cursor: pointer;
}
.ghost {
  opacity: 0.5;
  background: #c8ebfb;
}

.not-draggable {
  cursor: no-drop;
}
</style>