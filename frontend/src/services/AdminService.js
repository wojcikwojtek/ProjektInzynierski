import Api from "./Api";

export default {
    getSuggestions() {
        return Api().get('/admin/suggestions')
    },
    getReportedReviews() {
        return Api().get('/admin/reported/reviews')
    },
    getReportedComments() {
        return Api().get('/admin/reported/comments')
    },
    addSuggestion(data) {
        return Api().post('/admin/addsuggestion', data)
    },
    approveSuggestion(data) {
        return Api().post(`/admin/suggestion/approve?suggestionId=${data}`)
    },
    discardSuggestion(data) {
        return Api().post(`/admin/suggestion/discard?suggestionId=${data}`)
    },
    blockReview(data) {
        return Api().post(`/admin/review/block?reviewId=${data}`)
    },
    allowReview(data) {
        return Api().post(`/admin/review/allow?reviewId=${data}`)
    },
    blockComment(data) {
        return Api().post(`/admin/comment/block?commentId=${data}`)
    },
    allowComment(data) {
        return Api().post(`/admin/comment/allow?commentId=${data}`)
    }
}