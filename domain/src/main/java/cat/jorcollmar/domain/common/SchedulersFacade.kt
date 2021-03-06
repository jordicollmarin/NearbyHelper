package cat.jorcollmar.domain.common

import io.reactivex.Scheduler

interface SchedulersFacade {
    fun getIo(): Scheduler
    fun getNewThread(): Scheduler
    fun getAndroidMainThread(): Scheduler
}