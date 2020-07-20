package cat.jorcollmar.domain

import cat.jorcollmar.domain.common.BaseUseCase
import cat.jorcollmar.domain.common.SchedulersFacade
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.CountDownLatch
import java.util.concurrent.atomic.AtomicReference

open class BaseUseCaseTest {
    private val testScheduler = TestScheduler()
    private val countDownLatch = CountDownLatch(1)

    val schedulersFacade: SchedulersFacade = SchedulersFacadeTest(testScheduler)

    @Throws(Exception::class)
    fun <TYPE, PARAMS> captureResultForUseCase(
        singleUseCase: BaseUseCase.RxSingleUseCase<TYPE, PARAMS>? = null,
        observableUseCase: BaseUseCase.RxObservableUseCase<TYPE, PARAMS>? = null,
        params: PARAMS
    ): TYPE {
        val value = AtomicReference<TYPE>()
        val errorConsumer = Consumer<Throwable> { countDownLatch.countDown() }
        val successConsumer = Consumer<TYPE> {
            value.set(it)
            countDownLatch.countDown()
        }

        singleUseCase?.execute(successConsumer, errorConsumer, params)
            ?: (observableUseCase?.execute(
                successConsumer,
                errorConsumer,
                params
            ))

        testScheduler.triggerActions()
        countDownLatch.await()

        return value.get()
    }

    @Throws(Exception::class)
    fun <TYPE, PARAMS> captureErrorForUseCase(
        singleUseCase: BaseUseCase.RxSingleUseCase<TYPE, PARAMS>? = null,
        observableUseCase: BaseUseCase.RxObservableUseCase<TYPE, PARAMS>? = null,
        params: PARAMS
    ): Throwable {
        val value = AtomicReference<Throwable>()
        val errorConsumer = Consumer<Throwable> {
            value.set(it)
            countDownLatch.countDown()
        }
        val successConsumer = Consumer<TYPE> { countDownLatch.countDown() }

        singleUseCase?.execute(successConsumer, errorConsumer, params)
            ?: (observableUseCase?.execute(
                successConsumer,
                errorConsumer,
                params
            ))

        testScheduler.triggerActions()
        countDownLatch.await()

        return value.get()
    }
}