package money.vivid.urlavailability.config

import money.vivid.urlavailability.service.SettingService
import money.vivid.urlavailability.service.UrlAvailabilityService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.SchedulingConfigurer
import org.springframework.scheduling.config.ScheduledTaskRegistrar
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.concurrent.Executors

@Configuration
@EnableScheduling
class DynamicSchedulingConfig(
    val settingService: SettingService,
    val urlAvailabilityService: UrlAvailabilityService
) : SchedulingConfigurer {

    override fun configureTasks(taskRegistrar: ScheduledTaskRegistrar) {
        taskRegistrar.setScheduler(Executors.newSingleThreadScheduledExecutor())
        taskRegistrar.addTriggerTask(
            { urlAvailabilityService.checkAvailability() },
            { triggerContext ->
                val lastScheduledExecutionTime = triggerContext.lastScheduledExecutionTime() ?: Date()
                val nextExecutionTime = lastScheduledExecutionTime.toInstant()
                    .plus(settingService.period().toLong(), ChronoUnit.MINUTES)
                Date.from(nextExecutionTime)
            }
        )
    }

}
