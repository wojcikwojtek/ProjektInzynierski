import Api from "./Api";

export default {
    getList(id) {
        return Api().get('lists/' + id)
    },
    getListEntries(id) {
        return Api().get('lists/' + id + '/attractions')
    },
    addToList(data) {
        return Api().post('lists/add', data)
    } 
}