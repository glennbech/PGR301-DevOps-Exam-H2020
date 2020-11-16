package no.devops.exam

import io.micrometer.core.instrument.MeterRegistry
import io.restassured.RestAssured
import io.restassured.RestAssured.given
import no.devops.exam.db.MonsterRepository
import no.devops.exam.db.MonsterService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct

@ActiveProfiles("test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class RestAPITest {

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var monsterService: MonsterService

    @Autowired
    private lateinit var monsterRepository: MonsterRepository

    @Autowired
    private lateinit var meterRegistry: MeterRegistry



    @PostConstruct
    fun init() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.basePath = "/api/monsters"
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
    }

    @BeforeEach
    fun initTest() {
        monsterRepository.deleteAll()
    }


    @Test
    fun testGetMonster() {
        val id = "MetalgarurumonBar"
        monsterService.registerNewMonster(id)

        given().get("/$id")
                .then()
                .statusCode(200)
    }

    @Test
    fun testCreateMonster() {
        val id = "MetalgarurumonBar"
        val counter = meterRegistry.counter("counter.meterNotFound")
        assertEquals(counter.count(), 0.0)

        given().put("/$id")
                .then()
                .statusCode(201)

        assertTrue(monsterRepository.existsById(id))
    }
}