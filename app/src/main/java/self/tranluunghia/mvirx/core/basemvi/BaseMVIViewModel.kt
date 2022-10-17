package self.tranluunghia.mvirx.core.basemvi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.*
import self.tranluunghia.mvirx.core.entity.ErrorInfo
import self.tranluunghia.mvirx.core.entity.ErrorType
import self.tranluunghia.mvirx.core.helper.SingleLiveEvent
import self.tranluunghia.mvirx.core.helper.extention.subscribeOnMainThread

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseMVIViewModel<
        EVENT : BaseMVIContract.BaseEvent,
        STATE : BaseMVIContract.BaseState,
        EFFECT : BaseMVIContract.BaseEffect> : ViewModel() {

    val tag by lazy { this::class.java.name }

    val disposables = CompositeDisposable()

    // Create Initial State of View
    private val initialState : STATE by lazy { createInitialState() }
    abstract fun createInitialState() : STATE

    // Get Current State
    val currentState: STATE
        get() = uiState.value

    private val event : BehaviorSubject<EVENT> = BehaviorSubject.create()
    private val uiState : BehaviorSubject<STATE> = BehaviorSubject.createDefault(initialState)
    private val effect : BehaviorSubject<EFFECT> = BehaviorSubject.create()


    // Common error
    val loadingEvent = MutableLiveData<Boolean>()
    val errorMessageEvent = SingleLiveEvent<String>()
    val noInternetConnectionEvent = SingleLiveEvent<Unit>()
    val connectTimeoutEvent = SingleLiveEvent<Unit>()
    val tokenExpiredEvent = SingleLiveEvent<Unit>()

    init {
        event.subscribeOnMainThread()
            .subscribe({ event ->
                handleEvents(event)
            }, { error ->
                Log.e(tag, "" + error.message)
            })
    }

    abstract fun handleEvents(viewEvent: EVENT)

    fun setEvent(event: EVENT) {
        this.event.onNext(event)
    }

    /**
     * Set new Ui State
     */
    protected fun setState(reduce: STATE.() -> STATE) {
        val newState = currentState.reduce()
        uiState.onNext(newState)
    }

    /**
     * Set new Effect
     */
    protected fun setEffect(builder: () -> EFFECT) {
        val effectValue = builder()
        effect.onNext(effectValue)
    }


    fun subscribeState(
        onSuccess: ((STATE) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
    ): Disposable {
        val disposable = uiState.subscribeOnMainThread(onSuccess, onError)
        disposables.add(disposable)
        return disposable
    }

    fun subscribeEffect(
        onSuccess: ((EFFECT) -> Unit)? = null,
        onError: ((Throwable) -> Unit)? = null,
    ): Disposable {
        val disposable = effect.subscribeOnMainThread(onSuccess, onError)
        disposables.add(disposable)
        return disposable
    }


    protected fun handleError(
        errorInfo: ErrorInfo?,
        apiErrorMessage: String? = null,
        showPopup: Boolean = false
    ) {
        errorInfo?.let {
            if (errorInfo.errorType == ErrorType.NETWORK)
                noInternetConnectionEvent.call()
            else if (errorInfo.errorType == ErrorType.SERVER && (errorInfo.code == 401 || errorInfo.code == 403))
                tokenExpiredEvent.call()
            else if (errorInfo.errorType == ErrorType.API) {
                Log.e(tag, "API ERROR: $apiErrorMessage")
                if (showPopup) errorMessageEvent.postValue(apiErrorMessage ?: "API error!")
            } else {
                Log.e(tag, errorInfo.exception?.message.toString())
                if (showPopup) errorMessageEvent.postValue(errorInfo.exception?.message)
            }
        }
    }

    override fun onCleared() {
        // Using clear will clear all, but can accept new disposable
        disposables.clear();
        // Using dispose will clear all and set isDisposed = true, so it will not accept any new disposable
        //disposables.dispose();
        super.onCleared()
    }
}