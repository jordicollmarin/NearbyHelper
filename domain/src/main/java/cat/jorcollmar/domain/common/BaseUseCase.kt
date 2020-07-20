package cat.jorcollmar.domain.common

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Consumer
import io.reactivex.internal.functions.Functions
import io.reactivex.internal.observers.ConsumerSingleObserver
import io.reactivex.internal.observers.LambdaObserver

abstract class BaseUseCase<TYPE, PARAMS>(
    val schedulers: SchedulersFacade
) {

    internal val disposables = CompositeDisposable()

    fun dispose() {
        disposables.dispose()
    }

    protected abstract fun build(params: PARAMS): TYPE

    abstract class RxObservableUseCase<RESULT, PARAMS>(schedulersFacade: SchedulersFacade) :
        BaseUseCase<Observable<RESULT>, PARAMS>(schedulersFacade) {

        fun execute(success: Consumer<RESULT>, error: Consumer<Throwable>, params: PARAMS) {
            disposables.add(
                build(params)
                    .subscribeOn(schedulers.getIo())
                    .observeOn(schedulers.getAndroidMainThread())
                    .subscribeWith(
                        LambdaObserver<RESULT>(
                            success,
                            error,
                            Functions.EMPTY_ACTION,
                            Functions.emptyConsumer()
                        )
                    )
            )
        }
    }

    abstract class RxSingleUseCase<RESULT, PARAMS>(schedulersFacade: SchedulersFacade) :
        BaseUseCase<Single<RESULT>, PARAMS>(schedulersFacade) {

        fun execute(success: Consumer<RESULT>, error: Consumer<Throwable>, params: PARAMS) {
            disposables.add(
                build(params)
                    .subscribeOn(schedulers.getIo())
                    .observeOn(schedulers.getAndroidMainThread())
                    .subscribeWith(ConsumerSingleObserver<RESULT>(success, error))
            )
        }
    }
}