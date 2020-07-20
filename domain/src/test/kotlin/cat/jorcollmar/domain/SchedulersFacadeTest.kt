package cat.jorcollmar.domain

import cat.jorcollmar.domain.common.SchedulersFacade
import io.reactivex.Scheduler
import io.reactivex.schedulers.TestScheduler

class SchedulersFacadeTest(
    private val scheduler: TestScheduler
) : SchedulersFacade {

    override fun getIo(): Scheduler = scheduler

    override fun getNewThread(): Scheduler = scheduler

    override fun getAndroidMainThread(): Scheduler = scheduler

}