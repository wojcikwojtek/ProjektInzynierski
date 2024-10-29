import Api from "./Api";

export default {
    getUser(id) {
        return Api().get('users/' + id)
    },
    getReviews(id) {
        return Api().get('users/' + id + '/reviews')
    },
    getLists(id) {
        return Api().get('users/' + id + '/lists')
    },
    getStats(id) {
        return Api().get('users/' + id + '/stats')
    },
    findUser(login) {
        return Api().get('users/' + login + '/login')
    },
    isUserFollowing(id, followedUserId) {
        return Api().get('users/' + id + '/isfollowing/' + followedUserId)
    },
    getFollowers(id, userId) {
        return Api().get('users/' + id + '/getfollowers/' + userId)
    },
    getFollowing(id, userId) {
        return Api().get('users/' + id + '/getfollowing/' + userId)
    },
    getFollowedUsersRecentReviews(id) {
        return Api().get('users/' + id + '/recentreviews')
    },
    getProfilePicture(id) {
        return Api().get('users/' + id + '/profilepic')
    },
    getRecommendations(id) {
        return Api().get('users/' + id + '/recommendations')
    },
    search(id, name) {
        return Api().get('users/' + id + '/search/' + name)
    },
    followUser(data) {
        return Api().post('users/follow', data)
    },
    updateUser(data) {
        return Api().put('users/update', data)
    },
    unfollowUser(id, followedUserId) {
        return Api().delete('users/' + id + '/unfollow/' + followedUserId)
    }
}