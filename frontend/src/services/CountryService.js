import Api from "./Api";

export default {
    getAllCountries() {
        return Api().get('/countries')
    }
}