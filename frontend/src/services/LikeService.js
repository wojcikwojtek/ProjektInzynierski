import Api from "@/services/Api"

export default {
    likeReview(data) {
        return Api().post('likes/review', data)
    },
    likeList(data) {
        return Api().post('likes/list', data)
    },
    userLikedReview(userId, reviewId) {
        return Api().get('likes/' + userId + '/likedreview/' + reviewId)
    },
    userLikedList(userId, listId) {
        return Api().get('likes/' + userId + '/likedlist/' + listId)
    },
    deleteLikedReview(userId, reviewId) {
        return Api().delete('likes/' + userId + '/review/' + reviewId)
    },
    deleteLikedList(userId, listId) {
        return Api().delete('likes/' + userId + '/list/' + listId)
    }
}