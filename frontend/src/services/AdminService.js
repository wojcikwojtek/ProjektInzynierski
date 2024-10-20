import Api from "./Api";

export default {
    getSuggestions() {
        return Api().get('/admin/suggestions')
    },
    addSuggestion(data) {
        return Api().post('/admin/addsuggestion', data)
    },
    approveSuggestion(data) {
        return Api().post(`admin/suggestion/approve?suggestionId=${data}`)
    },
    discardSuggestion(data) {
        return Api().post(`admin/suggestion/discard?suggestionId=${data}`)
    }
}