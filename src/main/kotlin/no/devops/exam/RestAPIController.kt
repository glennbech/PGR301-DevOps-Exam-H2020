package no.devops.exam

import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.devops.exam.db.*
import no.devops.exam.dto.MonsterDto
import no.devops.exam.dto.RarityDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
class RestAPIController(
        @Autowired
        private val monsterRepository: MonsterRepository,
        @Autowired
        private var meterRegistry: MeterRegistry,
        @Autowired
        private var monsterRarityRepository: MonsterRarityRepository
) {

    private val creationCounter = Counter.builder("counter.metersCreated").description("Meters created").register(meterRegistry)
    private val notFoundCreation = Counter.builder("counter.meterNotFound").description("Meter not found").register(meterRegistry)
    private val monsterRaritySummary = DistributionSummary.builder("distribution.monsterRarity")
            .description("MonsterRarity Distribution")
            .baseUnit("rarity")
            .publishPercentiles(0.3, 0.5, 0.95)
            .register(meterRegistry)


    @GetMapping("/monsters")
    @Timed("List monsters", longTask = true)
    fun listMonsters(): ResponseEntity<List<Monster>> {
        val monsters = monsterRepository.findAll()

        return ResponseEntity.status(200).body(monsters.toList())
    }

    @PostMapping("/monsters")
    @Timed
    fun createMonster(): ResponseEntity<Monster> {
        val monster = monsterRepository.save(Monster())

        creationCounter.increment()

        return ResponseEntity.ok(monster)
    }

    @PostMapping("/monsters/{monsterId}/rarities")
    @Timed
    fun createRarities(@PathVariable("monsterId") monsterId: UUID, @RequestBody rarityVariables: RarityDto): ResponseEntity<Monster> {

        val monster = monsterRepository.findByIdOrNull(monsterId)
        if (monster == null) {
            notFoundCreation.increment()
            return ResponseEntity.status(404).body(null)
        }

        monsterRarityRepository.save(MonsterRarity(
                rarityVariables.endValue,
                rarityVariables.startValue,
                rarityVariables.rarity,
                monster
        ))
        monsterRaritySummary.record(rarityVariables.rarity.toDouble())

        return ResponseEntity.ok(monsterRepository.findByIdOrNull(monsterId)!!)
    }


    @GetMapping("/monsters/{monsterId}/rarities")
    @Timed
    fun getRarities(@PathVariable("monsterId") monsterId: UUID): ResponseEntity<List<MonsterRarity>> {

        val monster = monsterRepository.findByIdOrNull(monsterId)
        if (monster == null) {
            notFoundCreation.increment()
            return ResponseEntity.status(404).body(null)
        }
        return ResponseEntity.ok(monster.rarity.toList())
    }

}
