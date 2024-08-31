import Api from "@/services/Api"

export default {
    addComment(data) {
        return Api().post('/comments', data)
    }
}