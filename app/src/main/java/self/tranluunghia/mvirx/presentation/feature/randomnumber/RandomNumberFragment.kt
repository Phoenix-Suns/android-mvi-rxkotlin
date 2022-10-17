package self.tranluunghia.mvirx.presentation.feature.randomnumber

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import self.tranluunghia.mvirx.R
import self.tranluunghia.mvirx.core.basemvi.BaseMVIFragment
import self.tranluunghia.mvirx.core.helper.extention.gone
import self.tranluunghia.mvirx.core.helper.extention.visible
import self.tranluunghia.mvirx.databinding.FragmentRandomNumberBinding
import self.tranluunghia.mvirx.presentation.feature.repolist.RepoListFragment

// MVI Architecture with Kotlin Flows and Channels
// https://proandroiddev.com/writing-reactive-apps-with-mvi-f7de70739d59
@AndroidEntryPoint
class RandomNumberFragment : BaseMVIFragment<RandomNumberViewModel, FragmentRandomNumberBinding>() {

    companion object {
        fun newInstance() = RandomNumberFragment()
    }

    override fun layout(): Int = R.layout.fragment_random_number
    override fun viewModelClass(): Class<RandomNumberViewModel> = RandomNumberViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setUpViews() {
        super.setUpViews()

    }

    override fun setEvents() {
        super.setEvents()

        binding.btRandomNumber.setOnClickListener {
            viewModel.setEvent(RandomNumberContract.Event.OnRandomNumberClicked)
        }

        binding.btShowToast.setOnClickListener {
            viewModel.setEvent(RandomNumberContract.Event.OnShowToastClicked)
        }

        binding.btGotoRepoList.setOnClickListener {

            val repoListFragment = RepoListFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainerView, repoListFragment).
                addToBackStack(repoListFragment::class.simpleName).commit()
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

    private fun handleStates(state: RandomNumberContract.State) {
        when (state.randomNumberState) {
            is RandomNumberContract.RandomNumberState.Idle -> {
                showLoading(false)
            }
            is RandomNumberContract.RandomNumberState.Loading -> {
                showLoading(true)
            }
            is RandomNumberContract.RandomNumberState.Success -> {
                showLoading(false)
                binding.tvNumber.text = state.randomNumberState.number.toString()
            }
        }
    }

    private fun handleEffect(effect: RandomNumberContract.Effect) {
        when (effect) {
            is RandomNumberContract.Effect.ShowToast -> {
                showLoading(false)
                // Simple method that shows a toast
                Toast.makeText(context, "Error, number is even", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(isShow: Boolean) {
        if (isShow) {
            binding.progressBar.visible()
            binding.tvNumber.gone()
        } else {
            binding.progressBar.gone()
            binding.tvNumber.visible()
        }
    }
}