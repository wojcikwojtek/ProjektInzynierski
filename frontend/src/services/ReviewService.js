import Api from "@/services/Api"

export default {
    addReview(data) {
        return Api().post('reviews/add', data)
    }
}