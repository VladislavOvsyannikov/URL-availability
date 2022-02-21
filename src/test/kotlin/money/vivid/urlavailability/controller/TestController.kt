package money.vivid.urlavailability.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/tests")
class TestController {

    @SuppressWarnings("EmptyFunctionBlock")
    @RequestMapping("/test", method = [RequestMethod.HEAD])
    fun test() {
    }

    @GetMapping("/sleep")
    fun sleep() {
        Thread.sleep(500)
    }

}
