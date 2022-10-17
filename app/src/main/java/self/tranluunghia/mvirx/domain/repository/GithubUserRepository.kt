package self.tranluunghia.mvirx.domain.repository

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import self.tranluunghia.mvirx.core.entity.DataState
import self.tranluunghia.mvirx.domain.model.GithubRepo
import self.tranluunghia.mvirx.domain.model.GithubUser

interface GithubUserRepository {
    fun getUserDetail(username: String): Observable<DataState<GithubUser>>
    fun getRepoList(keyWork: String, page: Int, perPage: Int): Observable<DataState<List<GithubRepo>>>
}