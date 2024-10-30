<template>
    <v-row no-gutters>
        <v-col cols="3" class="pa-2">
        </v-col>
        <v-col cols="6" class="pa-2">
            <div class="white elevation-5 pa-6 bg-grey-darken-4">
                <v-snackbar
                    v-model="snackbar"
                    color="green"
                >
                    <p>Succesfully updated user profile</p>
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
                <h1>Update your profile</h1>
                <br>
                <v-form v-model="valid">
                    <p class="pb-3">Change username</p>
                    <v-text-field v-model="newLogin" label="new username"
                        :rules="newLoginRules"></v-text-field>
                    <p class="pb-3">Change email</p>
                    <v-text-field v-model="newEmail" label="new email"
                        :rules="newEmailRules"></v-text-field>
                    <p class="pb-3">Change password</p>
                    <v-text-field v-model="newPassword" label="new password" type="password"></v-text-field>
                    <p v-if="newPassword != ''" class="pb-3">Change password</p>
                    <v-text-field v-if="newPassword != ''" 
                        v-model="repeatPassword" label="repeat password" type="password"
                        :rules="repeatPasswordRules"></v-text-field>
                    <p class="pb-3">Change profile picture</p>
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
import UserService from '@/services/UserService';
import FileService from '@/services/FileService';

export default {
    data() {
        return {
            valid: false,
            newLogin: '',
            usedLogin: false,
            newLoginRules: [
                value => {
                    if(!this.usedLogin || value) return true
                    return 'Login already exists'
                }
            ],
            newEmail: '',
            usedEmail: false,
            newEmailRules: [
            value => {
                    if(!this.usedEmail || value) return true
                    return 'Email already in use'
                }
            ],
            newPassword: '',
            repeatPassword: '',
            repeatPasswordRules: [
                value => {
                    if(value == this.newPassword) return true
                    return 'Passwords are not the same'
                }
            ],
            selectedFile: null,
            snackbar: false
        }
    },
    methods: {
        async submit() {
            if(this.newPassword != '' && this.valid == false) {
                return
            }
            try {
                const response = await UserService.updateUser({
                    userId: this.$route.params.userId,
                    login: this.newLogin,
                    password: this.newPassword,
                    email: this.newEmail
                })
                this.snackbar = true
                this.usedLogin = false
                this.usedEmail = false
                this.newLogin = ''
                this.newEmail = ''
                this.newPassword = ''
                this.repeatPassword = ''
                if(!this.selectedFile) {
                    return
                }
                const formData = new FormData()
                formData.append('file', this.selectedFile)
                formData.append('id', this.$route.params.userId)
                FileService.uploadFile(formData)
                this.selectedFile = null
            } catch(error) {
                this.snackbar = false
                if(error.response.data == 'Username already exists') {
                    this.usedLogin = true
                } else if(error.response.data == 'Email already in use') {
                    this.usedEmail = true
                }
                return
            }
        }
    }
}
</script>

<style scoped>
</style>