import Api from "@/services/Api"

export default {
    likeReview(data) {
        return Api().post('likes/review', data)
    },
    userLikedReview(userId, reviewId) {
        return Api().get('likes/' + userId + '/liked/' + reviewId)
    },
    deleteLikedReview(userId, reviewId) {
        return Api().delete('likes/' + userId + '/' + reviewId)
    }
}