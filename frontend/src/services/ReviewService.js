import Api from "@/services/Api"

export default {
    addReview(data) {
        return Api().post('reviews/add', data)
    },
    getLikeCount(id) {
        return Api().get('reviews/' + id + '/likecount')
    }
}