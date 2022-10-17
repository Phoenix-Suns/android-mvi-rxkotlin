package self.tranluunghia.mvirx.domain.usecase

import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.flow.Flow
import self.tranluunghia.mvirx.core.basemvi.UseCase
import self.tranluunghia.mvirx.core.entity.DataState
import self.tranluunghia.mvirx.domain.model.GithubUser
import self.tranluunghia.mvirx.domain.repository.GithubUserRepository
import javax.inject.Inject

class GetUserDetailUseCase @Inject constructor(
    private val githubUserRepository: GithubUserRepository
) : UseCase<GetUserDetailUseCase.Params, Observable<DataState<GithubUser>>> {

    override fun invoke(params: Params): Observable<DataState<GithubUser>> =
        githubUserRepository.getUserDetail(params.username)

    class Params(val username: String)
}