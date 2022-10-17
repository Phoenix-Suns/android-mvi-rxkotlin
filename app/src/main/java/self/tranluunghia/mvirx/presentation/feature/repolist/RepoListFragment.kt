package self.tranluunghia.mvirx.presentation.feature.repolist

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import dagger.hilt.android.AndroidEntryPoint
import self.tranluunghia.mvirx.R
import self.tranluunghia.mvirx.core.basemvi.BaseMVIFragment
import self.tranluunghia.mvirx.core.helper.extention.gone
import self.tranluunghia.mvirx.core.helper.extention.logE
import self.tranluunghia.mvirx.core.helper.extention.singleClick
import self.tranluunghia.mvirx.core.helper.extention.visible
import self.tranluunghia.mvirx.databinding.FragmentRepoListBinding
import self.tranluunghia.mvirx.presentation.feature.adapter.RepoListAdapter

@AndroidEntryPoint
class RepoListFragment : BaseMVIFragment<RepoListViewModel, FragmentRepoListBinding>() {

    companion object {
        fun newInstance() = RepoListFragment()
    }

    override fun layout(): Int = R.layout.fragment_repo_list
    override fun viewModelClass(): Class<RepoListViewModel> = RepoListViewModel::class.java

    val repoListAdapter by lazy { RepoListAdapter(ArrayList()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setUpViews() {
        super.setUpViews()
        binding.recyclerViewRepo.adapter = repoListAdapter
    }

    override fun setEvents() {
        super.setEvents()

        binding.buttonSearch.singleClick {
            val searchKey = binding.editTextSearchKey.text.toString().trim()
            viewModel.setEvent(RepoListContract.Event.GetList(searchKey))
        }

        binding.buttonDestroyView.singleClick {
            //viewModel.viewModelJob.cancel()
            //viewModel.viewModelJob.cancelChildren()
        }
    }

    override fun subscribeUI() {
        super.subscribeUI()

        viewModel.subscribeState(
            { handleStates(it) },
            { Log.e(tag, "" + it.message) }
        )

        viewModel.subscribeEffect(
            { handleEffect(it) },
            { Log.e(tag, "" + it.message) }
        )
    }

    private fun handleStates(state: RepoListContract.State) {
        when (state) {
            is RepoListContract.State.ShowUserInfo -> {
                logE(state.userInfo.name)
            }
            is RepoListContract.State.RepoState -> {
                showLoading(state.isLoading)

                if (state.error != null) {
                    Toast.makeText(context, "Error :" + state.error, Toast.LENGTH_SHORT).show()
                }

                if (state.data != null) {
                    repoListAdapter.updateItems(state.data)
                }
            }
            is RepoListContract.State.Idle -> {

            }
        }
    }

    private fun handleEffect(effect: RepoListContract.Effect) {
        when (effect) {
            is RepoListContract.Effect.ShowToast -> {
                showLoading(false)
                // Simple method that shows a toast
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        if (isShow) {
            binding.progressBarLoading.visible()
            binding.recyclerViewRepo.gone()
        } else {
            binding.progressBarLoading.gone()
            binding.recyclerViewRepo.visible()
        }
    }
}