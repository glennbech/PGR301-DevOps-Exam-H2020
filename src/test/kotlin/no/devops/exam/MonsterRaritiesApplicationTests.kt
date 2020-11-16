package no.devops.exam

import io.micrometer.core.instrument.MeterRegistry
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class MonsterRaritiesApplicationTests {

    @Autowired
    var registry: MeterRegistry? = null

    @Test
    fun contextLoads() {
        registry?.counter("Test")
                ?.increment()
    }
}