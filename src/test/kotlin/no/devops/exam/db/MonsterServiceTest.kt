package no.devops.exam.db

import io.micrometer.core.instrument.MeterRegistry
import no.devops.exam.RestAPI
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension

@ActiveProfiles("MonsterServiceTest, test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
internal class MonsterServiceTest {

    @Autowired
    private lateinit var monsterService: MonsterService

    @Autowired
    private lateinit var meterRegistry: MeterRegistry

    @Autowired
    private lateinit var monsterRepository: MonsterRepository

    @Autowired
    private lateinit var restApiController: RestAPI

    @BeforeEach
    fun initTest() {
        monsterRepository.deleteAll()
    }

   /* @Test
    fun create() {
        assertThat(monsterRepository.count()).isEqualTo(0)

        val counter = meterRegistry.counter("counter.metersCreated")

        assertThat(counter.count()).isEqualTo(0.0)
        restApiController.createMonsterPOST()
        assertThat(counter.count()).isEqualTo(1.0)

        assertThat(monsterRepository.count()).isEqualTo(1)


    }*/

    @Test
    fun createMonster() {
        val id = "WargreymonFoo"
        //val counter = meterRegistry.counter("counter.monsterCreated")

        assertTrue(monsterService.registerNewMonster(id))
        assertTrue(monsterRepository.existsById(id))
    }

    @Test
    fun testFailCreateMonsterTwice() {
        val id = "WargreymonFoo"
        assertTrue(monsterService.registerNewMonster(id))
        assertFalse(monsterService.registerNewMonster(id))
    }
}