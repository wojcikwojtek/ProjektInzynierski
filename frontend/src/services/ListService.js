import Api from "./Api";

export default {
    getList(id) {
        return Api().get('lists/' + id)
    },
    getListEntries(id) {
        return Api().get('lists/' + id + '/attractions')
    },
    getLikeCount(id) {
        return Api().get('lists/' + id + '/likecount')        
    },
    addToList(data) {
        return Api().post('lists/add', data)
    },
    editEntries(data) {
        return Api().put('lists/editentries', data)
    }
}