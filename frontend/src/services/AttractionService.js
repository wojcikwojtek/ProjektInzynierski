import Api from "@/services/Api"

export default {
    search (attractionName) {
        return Api().get('/attractions/search/' + attractionName)
    }
}