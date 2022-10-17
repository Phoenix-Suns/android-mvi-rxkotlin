package self.tranluunghia.mvirx.domain.usecase

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import self.tranluunghia.mvirx.core.basemvi.UseCase
import self.tranluunghia.mvirx.core.entity.DataState
import self.tranluunghia.mvirx.domain.model.GithubRepo
import self.tranluunghia.mvirx.domain.repository.GithubUserRepository
import javax.inject.Inject

class GetRepoListUseCase @Inject constructor(
    private val repository: GithubUserRepository
) : UseCase<GetRepoListUseCase.Params, Observable<DataState<List<GithubRepo>>>> {

    override fun invoke(params: Params): Observable<DataState<List<GithubRepo>>> =
        repository.getRepoList(params.keyWork, params.page, params.perPage)

    class Params(
        val perPage: Int,
        val page: Int,
        val keyWork: String
    )
}