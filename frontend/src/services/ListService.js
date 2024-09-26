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
    getTenMostPopular(index) {
        return Api().get('lists/' + index + '/mostpopular')
    },
    getTenNewest(index) {
        return Api().get('lists/' + index + '/newest')
    },
    addToList(data) {
        return Api().post('lists/add', data)
    },
    editEntries(data) {
        return Api().put('lists/editentries', data)
    },
    deleteEntry(id, attractionId) {
        return Api().delete('lists/' + id + '/entry/' + attractionId)
    }
}