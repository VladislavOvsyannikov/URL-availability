package money.vivid.urlavailability.config

import money.vivid.urlavailability.config.properties.AppProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.validation.annotation.Validated

@Configuration
class PropertiesConfig {

    @Bean
    @Validated
    @ConfigurationProperties("properties")
    fun appProperties(): AppProperties {
        return AppProperties()
    }

}
