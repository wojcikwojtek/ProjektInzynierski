<template>
    <v-btn block class="bg-cyan text-white text-center" @click="openDialog">
        Add to lists
    </v-btn>

    <v-dialog 
        v-model="dialog"
        width="auto"
    >
        <v-sheet
            class="mx-auto"
            width="500"
            rounded="lg"
        >
            <v-toolbar flat dense class="bg-cyan text-white text-center" height="50">
                <v-toolbar-title>Add to lists</v-toolbar-title>
            </v-toolbar>
            <div class="pl-1 pr-1 pt-4 pb-4">
                <div v-for="list in this.lists" :key="list.list_id">
                    <v-card
                        class="mx-auto pb-1"
                        variant="outlined"
                        :title="list.name"
                        :prepend-icon="list.selected ? 'mdi-check' : ' '"
                        hover
                        @click="check(list)"
                    >
                        <template v-slot:prepend>
                            <v-icon v-if="list.selected" color="success" icon="mdi-check"></v-icon>
                        </template>
                        <template v-slot:append>
                            {{ list.size }} entries
                        </template>
                    </v-card>
                </div>
                <div class="d-flex justify-center pt-3">
                    <v-btn class="bg-cyan text-white text-center" @click="add">
                        Add
                    </v-btn>
                </div>
            </div>
        </v-sheet>
    </v-dialog>
</template>

<script>
import { useUserStore } from '@/stores/userStore';
import UserService from '@/services/UserService';
import ListService from '@/services/ListService';
export default {
    data () {
        return {
            dialog: false,
            lists: null
        }
    },
    computed: {
        userStore: () => useUserStore()
    },
    /*
    async mounted() {
    //TODO: zrobic try catche i errory
        if(!this.userStore.isUserLoggedIn) {
            return
        }
        const response = await UserService.getLists(this.userStore.user.user_id);
        this.lists = response.data.map(list => ({
            ...list,
            selected: false
        }));
    },
    */
    methods: {
        async openDialog() {
            if(this.userStore.isUserLoggedIn) {
                this.dialog = true
                const response = await UserService.getLists(this.userStore.user.user_id);
                this.lists = response.data.map(list => ({
                    ...list,
                    selected: false
                }));
            } else {
                this.$router.push({name: 'login'})
            }
        },
        check(list) {
            list.selected = !list.selected
        },
        async add() {
            if(this.lists == null || this.lists == []) {
                return
            }
            this.lists.forEach(list => {
                if(list.selected) {
                    try {
                        const response = ListService.addToList({
                            listId: list.list_id,
                            attractionId: this.$route.params.attractionId
                        })
                    } catch(error) {
                        console.log(error.response.data)
                    }
                    list.selected = false
                }
            });
            this.dialog = false
        }
    }
}
</script>

<style scoped>
</style>