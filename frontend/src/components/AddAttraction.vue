<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" v-if="countries" class="pa-2">
            <v-snackbar
                v-model="snackbar"
                color="green"
            >
                <p>Succesfully suggested an attraction</p>
                <template v-slot:actions>
                    <v-btn
                        color="red"
                        variant="text"
                        @click="snackbar = false"
                    >   
                        Close
                    </v-btn>
                </template>
            </v-snackbar>
            <div class="white elevation-5 pa-6 bg-grey-darken-4">
                <h1>Suggest a new attraction</h1>
                <br>
                <v-form v-model="valid">
                    <p class="pb-3">Name</p>
                    <v-text-field v-model="name" label="Name"
                        :rules="rules"></v-text-field>
                    <p class="pb-3">Country</p>
                    <v-autocomplete
                        v-model="country"
                        :items="countries"
                        item-title="name"
                        item-value="name"
                        label="Country"
                        :rules="rules"
                    ></v-autocomplete>
                    <p class="pb-3">City</p>
                    <v-text-field v-model="city" label="City"
                        :rules="rules"></v-text-field>
                    <p class="pb-3">Location</p>
                    <v-text-field v-model="location" label="Location"
                        :rules="rules"></v-text-field>
                    <p class="pb-3">Description</p>
                    <v-textarea 
                        v-model="description"
                        row-height="20"
                        rows="2"
                        label="Description"
                        auto-grow
                        :rules="rules"
                    ></v-textarea>
                    <p class="pb-3">Add image</p>
                    <v-file-input label="upload image" prepend-icon="mdi-camera" accept="image/jpeg"
                        v-model="selectedFile"></v-file-input>
                    <div class="d-flex justify-center">
                        <v-btn class="bg-cyan text-white" @click="submit">Submit</v-btn>
                    </div>
                </v-form>
            </div>
        </v-col>
    </v-row>
</template>

<script>
import AdminService from '@/services/AdminService';
import CountryService from '@/services/CountryService';

export default {
    data() {
        return {
            valid: false,
            countries: null,
            name: '',
            country: '',
            city: '',
            location: '',
            description: '',
            selectedFile: null,
            snackbar: false,
            rules: [
                value => {
                    if(value) return true
                    return `Don't leave an empty field`
                }
            ]
        }
    },
    async mounted() {
        this.countries = (await CountryService.getAllCountries()).data
    },
    methods: {
        async submit() {
            if(!this.valid || !this.selectedFile) return
            try {
                const formData = new FormData()
                const suggestionData = JSON.stringify({
                    name: this.name,
                    country: this.country,
                    city: this.city,
                    location: this.location,
                    description: this.description
                });
                formData.append('suggestion', new Blob([suggestionData], { type: 'application/json' }))
                formData.append('file', this.selectedFile)
                AdminService.addSuggestion(formData)
                this.snackbar = true
                this.name = ''
                this.country = ''
                this.city = ''
                this.location = ''
                this.description = ''
                this.selectedFile = null
            } catch(error) {
                console.log(error)
            }
        }
    }
}
</script>

<style scoped>
</style>