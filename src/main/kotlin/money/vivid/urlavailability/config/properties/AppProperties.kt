package money.vivid.urlavailability.config.properties

import javax.validation.constraints.Positive

class AppProperties(

    @field:Positive
    var timeout: Int = 0,

    @field:Positive
    var pageSize: Int = 0

)
