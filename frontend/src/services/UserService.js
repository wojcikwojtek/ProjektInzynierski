import Api from "./Api";

export default {
    getLists(id) {
        return Api().get('users/' + id + '/lists')
    }
}