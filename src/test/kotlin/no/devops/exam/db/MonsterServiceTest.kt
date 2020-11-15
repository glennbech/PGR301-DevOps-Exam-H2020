package no.devops.exam.db

import io.micrometer.core.instrument.MeterRegistry
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

    @BeforeEach
    fun initTest() {
        monsterRepository.deleteAll()
    }

    @Test
    fun createMonster() {
        val id = "WargreymonFoo"
        val counter = meterRegistry.counter("counter.monsterCreated")

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