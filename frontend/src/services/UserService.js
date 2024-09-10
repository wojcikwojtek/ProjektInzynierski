import Api from "./Api";

export default {
    getUser(id) {
        return Api().get('users/' + id)
    },
    getReviews(id) {
        return Api().get('users/' + id + '/reviews')
    },
    getLists(id) {
        return Api().get('users/' + id + '/lists')
    },
    getStats(id) {
        return Api().get('users/' + id + '/stats')
    },
    findUser(login) {
        return Api().get('users/' + login + '/login')
    }
}