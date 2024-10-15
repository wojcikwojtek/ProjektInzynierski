import Api from "./Api";

export default {
    getSuggestions() {
        return Api().get('/admin/suggestions')
    },
    addSuggestion(data) {
        return Api().post('/admin/addsuggestion', data)
    }
}