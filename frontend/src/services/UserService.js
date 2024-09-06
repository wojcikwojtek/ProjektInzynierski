import Api from "./Api";

export default {
    getLists(id) {
        return Api().get('users/' + id + '/lists')
    },
    getStats(id) {
        return Api().get('users/' + id + '/stats')
    }
}