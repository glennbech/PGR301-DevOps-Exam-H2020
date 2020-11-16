package no.devops.exam

import groovy.transform.AutoImplement
import io.micrometer.core.instrument.MeterRegistry
import no.devops.exam.db.Monster
import no.devops.exam.db.MonsterRarity
import no.devops.exam.db.MonsterRarityRepository
import no.devops.exam.db.MonsterRepository
import no.devops.exam.dto.RarityDto
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.annotation.DirtiesContext
import org.springframework.web.bind.annotation.RestController


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
internal class RestAPIControllerTest {
    @Autowired
    private lateinit var restApiController: RestAPIController

    @Autowired
    private lateinit var monsterRepository: MonsterRepository

    @Autowired
    private lateinit var monsterRarityRepository: MonsterRarityRepository

    @Autowired
    private lateinit var meterRegistry: MeterRegistry

    @Test
    fun listMonsters() {
        assertThat(restApiController.listMonsters().body).isNotNull.isEmpty()

        monsterRepository.save(Monster())

        assertThat(restApiController.listMonsters().body).isNotNull.hasSize(1);
    }

    @Test
    fun createMonster() {
        assertThat(monsterRepository.count()).isEqualTo(0)

        val counter = meterRegistry.counter("counter.metersCreated")

        assertThat(counter.count()).isEqualTo(0.0)
        restApiController.createMonster()
        assertThat(counter.count()).isEqualTo(1.0)

        assertThat(monsterRepository.count()).isEqualTo(1)
    }

    @Test
    fun createRarities() {
        var monster = monsterRepository.save(Monster())
        assertThat(monster.rarity).isEmpty()

        val counter = meterRegistry.counter("counter.meterNotFound")

        assertThat(counter.count()).isEqualTo(0.0)
        restApiController.createRarities(monster.monsterId!!, RarityDto(25.0F, 25.2F, 50))
        assertThat(counter.count()).isEqualTo(0.0)

        monster = monsterRepository.findByIdOrNull(monster.monsterId!!)!!

        assertThat(monster)
                .hasFieldOrPropertyWithValue("monsterId", monster.monsterId)
        assertThat(monster.rarity)
                .hasSize(1)
    }

    @Test
    fun getRarities() {
        val monster = monsterRepository.save(Monster())
        assertThat(monster.rarity).isEmpty()
        val counter = meterRegistry.counter("counter.meterNotFound")

        assertThat(counter.count()).isEqualTo(0.0)
        assertThat(restApiController.getRarities(monster.monsterId!!).body).isEmpty()
        assertThat(counter.count()).isEqualTo(0.0)

        monsterRarityRepository.save(MonsterRarity(25.0F, 25.2F, 50, monster))

        assertThat(restApiController.getRarities(monster.monsterId!!).body).hasSize(1)
    }
}