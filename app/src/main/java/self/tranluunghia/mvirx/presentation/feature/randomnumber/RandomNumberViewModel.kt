package self.tranluunghia.mvirx.presentation.feature.randomnumber
import android.R
import android.os.Handler
import android.os.Looper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import self.tranluunghia.mvirx.core.basemvi.BaseMVIViewModel
import javax.inject.Inject


@HiltViewModel
class RandomNumberViewModel @Inject constructor(

) : BaseMVIViewModel<RandomNumberContract.Event, RandomNumberContract.State, RandomNumberContract.Effect>() {

    override fun createInitialState() = RandomNumberContract.State(RandomNumberContract.RandomNumberState.Idle)

    override fun handleEvents(viewEvent: RandomNumberContract.Event) {
        when (viewEvent) {
            is RandomNumberContract.Event.OnRandomNumberClicked -> { generateRandomNumber() }
            is RandomNumberContract.Event.OnShowToastClicked -> {
                setEffect { RandomNumberContract.Effect.ShowToast }
            }
        }
    }

    /**
     * Generate a random number
     */
    private fun generateRandomNumber() {
        // Set Loading
        setState { copy(randomNumberState = RandomNumberContract.RandomNumberState.Loading) }

        // Add delay for simulate network call
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(
            {
                try {
                    val random = (0..10).random()
                    if (random % 2 == 0) {
                        // If error happens set state to Idle
                        // If you want create a Error State and use it
                        setState { copy(randomNumberState = RandomNumberContract.RandomNumberState.Idle) }
                        throw RuntimeException("Number is even")
                    }
                    // Update state
                    setState {
                        copy(
                            randomNumberState = RandomNumberContract.RandomNumberState.Success(
                                number = random
                            )
                        )
                    }
                } catch (exception: Exception) {
                    // Show error
                    setEffect { RandomNumberContract.Effect.ShowToast }
                }
            }, 1000
        )
    }
}
