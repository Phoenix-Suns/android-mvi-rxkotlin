package self.tranluunghia.mvirx.data.api.service

import io.reactivex.rxjava3.core.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import self.tranluunghia.mvirx.data.model.GithubList
import self.tranluunghia.mvirx.data.model.response.GithubRepoResponse
import self.tranluunghia.mvirx.data.model.response.GithubUserResponse

interface GithubWS {
    @GET("users/{username}")
    fun getGitHubUserDetail(@Path("username") userName: String) : Observable<GithubUserResponse>

    @GET("search/repositories")
    fun getRepoList(
        @Query("q") keyWork: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ) : Observable<GithubList<GithubRepoResponse>>

}