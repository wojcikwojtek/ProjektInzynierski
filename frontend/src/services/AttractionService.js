import Api from "@/services/Api"

export default {
    search (attractionName) {
        return Api().get('/attractions/search/' + attractionName)
    },
    getAttractionById (id) {
        return Api().get('/attractions/' + id)
    },
    getAttractionReviews (id) {
        return Api().get('/attractions/'+id+'/reviews')
    }
}