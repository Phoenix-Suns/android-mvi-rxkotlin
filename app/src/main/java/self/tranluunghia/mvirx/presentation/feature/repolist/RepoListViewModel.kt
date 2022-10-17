package self.tranluunghia.mvirx.presentation.feature.repolist
import android.util.Log
import dagger.hilt.android.lifecycle.HiltViewModel
import self.tranluunghia.mvirx.core.basemvi.BaseMVIViewModel
import self.tranluunghia.mvirx.core.entity.DataState
import self.tranluunghia.mvirx.core.helper.extention.subscribeOnMainThread
import self.tranluunghia.mvirx.domain.model.Paging
import self.tranluunghia.mvirx.domain.usecase.GetRepoListUseCase
import self.tranluunghia.mvirx.domain.usecase.GetUserDetailUseCase
import javax.inject.Inject

@HiltViewModel
class RepoListViewModel @Inject constructor(
    private val getUserDetailUseCase: GetUserDetailUseCase,
    private val getRepoListUseCase: GetRepoListUseCase
) : BaseMVIViewModel<RepoListContract.Event, RepoListContract.State, RepoListContract.Effect>() {

    private var searchKey: String = ""
    private var repoListPaging: Paging = Paging()

    override fun handleEvents(viewEvent: RepoListContract.Event) {
        when (viewEvent) {
            is RepoListContract.Event.GetList -> {
                getListRepo(viewEvent.searchKey)
            }
        }
    }

    private fun getListRepo(searchKey: String) {
        this.searchKey = searchKey

        val observable = getRepoListUseCase.invoke(
            GetRepoListUseCase.Params(
                perPage = repoListPaging.perPage, page = repoListPaging.page, keyWork = searchKey
            )
        ).subscribeOnMainThread()
            .subscribe({ dataState ->

                when (dataState.status) {
                    DataState.Status.SUCCESS -> {
                        setState { RepoListContract.State.RepoState(
                            isLoading = false, error = null, data = dataState.data
                        ) }
                    }
                    DataState.Status.ERROR -> {
                        Log.e(tag, dataState.message ?: "")
                        setState { RepoListContract.State.RepoState(
                            isLoading = false, error = dataState.message, data = null
                        ) }
                    }
                    DataState.Status.LOADING -> {
                        setState { RepoListContract.State.RepoState(
                            isLoading = true, error = null, data = null
                        ) }
                    }
                }
            }, { error ->
                setState { RepoListContract.State.RepoState(
                    isLoading = false, error = error.message, data = null
                ) }
            })
        disposables.add(observable)
    }

    private fun getUserInfo() {
        /*ioScope.launch {
            getUserDetailUseCase.invoke(GetUserDetailUseCase.Params("ahmedrizwan"))
                .collect { dataState ->
                    when (dataState.status) {
                        DataState.Status.SUCCESS -> {
                            setState { RepoListContract.State.ShowUserInfo(dataState.data!!) }
                        }
                        DataState.Status.ERROR -> {

                        }
                        DataState.Status.LOADING -> {

                        }
                    }
                }
        }*/
    }

    override fun createInitialState() = RepoListContract.State.Idle
}
