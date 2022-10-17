package self.tranluunghia.mvirx.data.repository

import android.content.Context
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import retrofit2.Retrofit
import self.tranluunghia.mvirx.core.entity.DataState
import self.tranluunghia.mvirx.core.helper.extention.repositoryExecutor
import self.tranluunghia.mvirx.core.helper.extention.repositoryRxExecutor
import self.tranluunghia.mvirx.data.api.service.GithubWS
import self.tranluunghia.mvirx.data.mapper.GithubRepoResponseMapper
import self.tranluunghia.mvirx.data.mapper.GithubUserResponseMapper
import self.tranluunghia.mvirx.domain.model.GithubRepo
import self.tranluunghia.mvirx.domain.model.GithubUser
import self.tranluunghia.mvirx.domain.repository.GithubUserRepository
import javax.inject.Inject

class GithubUserRepositoryImpl @Inject constructor(
    private val context: Context,
    private val retrofit: Retrofit,
    private val githubWS: GithubWS,
    private val githubUserResponseMapper: GithubUserResponseMapper,
    private val githubRepoResponseMapper: GithubRepoResponseMapper
) : GithubUserRepository {

    /*override fun getUserDetail(username: String): Flow<DataState<GithubUser>> = flow {
        val githubUserResponse = githubWS.getGitHubUserDetail(username)
        githubUserResponseMapper.map(githubUserResponse)
    }*/

    override fun getUserDetail(username: String): Observable<DataState<GithubUser>> =
        repositoryRxExecutor(
            context = context,
            retrofit = retrofit,
            apiCall = {
                githubWS.getGitHubUserDetail(username)
            },
            transform = {
                it.map { response ->
                    return@map githubUserResponseMapper.map(response)
                }
            }
        )

    override fun getRepoList(
        keyWork: String,
        page: Int,
        perPage: Int
    ): Observable<DataState<List<GithubRepo>>> =
        repositoryRxExecutor(
            context = context,
            retrofit = retrofit,
            apiCall = {
                githubWS.getRepoList(keyWork, page, perPage)
            },
            transform = { it ->
                it.map { response ->
                    val listRepo = ArrayList<GithubRepo>()
                    response.items.forEach {
                        listRepo.add(githubRepoResponseMapper.map(it))
                    }
                    return@map listRepo
                }
            }
        )
}