import Api from "@/services/Api"

export default {
    uploadFile(data) {
        return Api().post('file/upload', data, {
            headers: {
                'Content-Type': 'multipart/form-data',
            }
        })
    }
}