package self.tranluunghia.mvirx.presentation.feature.repolist

import self.tranluunghia.mvirx.core.basemvi.BaseMVIContract
import self.tranluunghia.mvirx.domain.model.GithubRepo
import self.tranluunghia.mvirx.domain.model.GithubUser

sealed class RepoListContract {
    sealed class Event: BaseMVIContract.BaseEvent {
        data class GetList(val searchKey: String) : Event()
    }

    sealed class State: BaseMVIContract.BaseState {
        object Idle : State()

        data class ShowUserInfo(val userInfo: GithubUser) : State()
        data class RepoState(
            val isLoading: Boolean = false,
            val error: String? = null,
            val data: List<GithubRepo>? = null
        ) : State()
    }

    sealed class Effect: BaseMVIContract.BaseEffect {
        object ShowToast : Effect()
    }
}