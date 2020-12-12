package no.devops.exam

import io.micrometer.core.annotation.Timed
import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import no.devops.exam.db.Monster
import no.devops.exam.db.MonsterRarity
import no.devops.exam.db.MonsterRarityRepository
import no.devops.exam.db.MonsterRepository
import no.devops.exam.dto.RarityDto
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import java.util.concurrent.TimeUnit

@RestController
class RestAPIController(
        @Autowired
        private val monsterRepository: MonsterRepository,
        @Autowired
        private var meterRegistry: MeterRegistry,
        @Autowired
        private var monsterRarityRepository: MonsterRarityRepository
) {

    private val logger = LoggerFactory.getLogger(RestAPIController::class.java)

    private val creationCounter = Counter.builder("counter.metersCreated").description("Monsters meters created").register(meterRegistry)
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

        val ms = (Math.random() * 200).toLong()

        TimeUnit.MILLISECONDS.sleep(ms)
        logger.debug("Metric Long task ran for {} ms", ms)

        logger.info("List devices showed {} monster(s)", monsters.count())
        return ResponseEntity.status(200).body(monsters.toList())
    }

    @PostMapping("/monsters")
    @Timed
    fun createMonster(): ResponseEntity<Monster> {
        logger.debug("Created monsters before create: {}", creationCounter.count())
        val monster = monsterRepository.save(Monster())
        logger.info("Created new monster with id: {}", monster.monsterId)

        creationCounter.increment()
        logger.debug("Created monster after createMonster: {}", creationCounter.count())

        return ResponseEntity.ok(monster)
    }

    @PostMapping("/monsters/{monsterId}/rarities")
    @Timed
    fun createRarities(@PathVariable("monsterId") monsterId: UUID, @RequestBody rarityVariables: RarityDto): ResponseEntity<Monster> {

        logger.debug("Searching for monster by id: {}", monsterId)
        val monster = monsterRepository.findByIdOrNull(monsterId)
        if (monster == null) {
            logger.error("Could not find monster by id: {}", monsterId)
            notFoundCreation.increment()
            return ResponseEntity.status(404).body(null)
        }
        logger.info("Monster by id: {} had {} rarities before new", monster.monsterId, monster.rarity.count())

        monsterRarityRepository.save(MonsterRarity(
                rarityVariables.endValue,
                rarityVariables.startValue,
                rarityVariables.rarity,
                monster
        ))
        monsterRaritySummary.record(rarityVariables.rarity.toDouble())
        logger.info("Registered new calculation of {} at minVal: {}, maxVal: {}", rarityVariables.rarity, rarityVariables.startValue, rarityVariables.endValue)

        return ResponseEntity.ok(monsterRepository.findByIdOrNull(monsterId)!!)
    }


    @GetMapping("/monsters/{monsterId}/rarities")
    @Timed
    fun getRarities(@PathVariable("monsterId") monsterId: UUID): ResponseEntity<List<MonsterRarity>> {
        logger.debug("Searching for monster by id: {}", monsterId)
        val monster = monsterRepository.findByIdOrNull(monsterId)
        if (monster == null) {
            logger.error("Could not find monster by id: {}", monsterId)
            notFoundCreation.increment()
            return ResponseEntity.status(404).body(null)
        }
        return ResponseEntity.ok(monster.rarity.toList())
    }
}
