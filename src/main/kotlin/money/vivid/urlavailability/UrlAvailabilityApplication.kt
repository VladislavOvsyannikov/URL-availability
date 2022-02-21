package money.vivid.urlavailability

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class UrlAvailabilityApplication

@SuppressWarnings("SpreadOperator")
fun main(args: Array<String>) {
    runApplication<UrlAvailabilityApplication>(*args)
}
