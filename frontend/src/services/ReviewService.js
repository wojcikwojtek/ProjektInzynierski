import Api from "@/services/Api"

export default {
    addReview(data) {
        return Api().post('reviews/add', data)
    },
    reportReview(data) {
        return Api().post(`reviews/report?reviewId=${data}`)
    },
    getReview(id) {
        return Api().get('reviews/' + id)
    },
    getLikeCount(id) {
        return Api().get('reviews/' + id + '/likecount')
    },
    getComments(id) {
        return Api().get('reviews/' + id + '/comments')
    },
    getCommentCount(id) {
        return Api().get('reviews/' + id + '/commentcount')
    }
}