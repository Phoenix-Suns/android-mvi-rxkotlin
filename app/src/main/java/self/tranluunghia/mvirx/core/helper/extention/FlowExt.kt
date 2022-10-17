package self.tranluunghia.mvirx.core.helper.extention

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.f8fit.f8.core.helper.retrofit.RetrofitErrorUtils
import io.reactivex.rxjava3.core.Observable
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import retrofit2.HttpException
import retrofit2.Retrofit
import self.tranluunghia.mvirx.core.entity.DataState
import self.tranluunghia.mvirx.core.entity.ErrorResponse
import self.tranluunghia.mvirx.core.entity.ErrorType
import self.tranluunghia.mvirx.core.helper.NetworkHelper

fun <T> Flow<T>.launchWhenStartedUntilStopped(owner: LifecycleOwner) {
    if (owner.lifecycle.currentState == Lifecycle.State.DESTROYED)
        return
    else
        owner.lifecycle.addObserver(LifecycleBoundObserver(this))
}

private class LifecycleBoundObserver constructor(
    private val flow: Flow<*>
) : DefaultLifecycleObserver {
    private var job: Job? = null

    override fun onStart(owner: LifecycleOwner) {
        job = flow.launchIn(owner.lifecycleScope)
    }

    override fun onStop(owner: LifecycleOwner) {
        cancelJob()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        owner.lifecycle.removeObserver(this)
        cancelJob()
    }

    @Suppress("NOTHING_TO_INLINE")
    private inline fun cancelJob() {
        job?.cancel()
        job = null
    }
}

fun <Response, Result> repositoryExecutor(
    context: Context,
    retrofit: Retrofit,
    apiCall: (suspend () -> Response)? = null,
    transform: (suspend (Response) -> Result),
    emitCache: (suspend () -> Result?)? = null,
): Flow<DataState<Result>> = flow {
    if (!NetworkHelper.isInternetConnected(context))
        emit(DataState.noInternet<Nothing>())
    else {
        emit(DataState.loading())

        try {
            emitCache?.invoke()?.let {
                emit(DataState.success(it))
            }

            /*if (apiCall != null && transform != null) {
                val dataResponse = apiCall.invoke()
                if (dataResponse.isSuccess()) {
                    val result = transform.invoke(dataResponse.data)
                    emit(DataState.success(result))
                } else{
                    val errorData = transformError?.invoke(dataResponse.data)
                    emit(
                        DataState.error(
                            dataResponse.status.detail,
                            errorData,
                            AppEnum.ErrorType.API,
                            dataResponse.status.code,
                        )
                    )}
            }*/

            if (apiCall != null && transform != null) {
                val dataResponse = apiCall.invoke()
                val result = transform.invoke(dataResponse)
                emit(DataState.success(result))
            }

        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = RetrofitErrorUtils.getErrorBodyAs(
                    retrofit,
                    ErrorResponse::class.java,
                    e.response()
                )
                emit(
                    DataState.error(
                        e.message(),
                        null,
                        ErrorType.SERVER,
                        e.code(),
                        e,
                        errorResponse
                    )
                )
            } else
                emit(DataState.unexpectedError(null, e))
        }
    }
}

fun <Response, Result> repositoryRxExecutor(
    context: Context,
    retrofit: Retrofit,
    apiCall: (() -> Response)? = null,
    transform: ((Response) -> Observable<Result>)? = null,
    //emitCache: (suspend () -> Result?)? = null,
): Observable<DataState<Result>> = Observable.create { emit ->
    if (!NetworkHelper.isInternetConnected(context))
        emit.onNext(DataState.noInternet<Nothing>())
    else {
        emit.onNext(DataState.loading())

        try {
            /*emitCache?.invoke()?.let {
                emit.onNext(DataState.success(it))
            }*/

            if (apiCall != null && transform != null) {
                val dataResponse = apiCall.invoke()
                val result = transform.invoke(dataResponse)
                DataState.success(result).data?.subscribe {
                    emit.onNext(DataState.success(it))
                }
            }

        } catch (e: Exception) {
            if (e is HttpException) {
                val errorResponse = RetrofitErrorUtils.getErrorBodyAs(
                    retrofit,
                    ErrorResponse::class.java,
                    e.response()
                )
                emit.onNext(
                    DataState.error(
                        e.message(),
                        null,
                        ErrorType.SERVER,
                        e.code(),
                        e,
                        errorResponse
                    )
                )
            } else
                emit.onNext(DataState.unexpectedError(null, e))
        }
    }
}