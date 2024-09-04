import Api from "./Api";

export default {
    addToList(data) {
        return Api().post('lists/add', data)
    } 
}