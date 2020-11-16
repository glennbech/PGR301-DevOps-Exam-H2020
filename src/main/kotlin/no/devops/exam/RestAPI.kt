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


@Api(value = "/api/monsters", description = "List of monsters")
@RequestMapping(
        path = ["/api/monsters"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)

@RestController
class RestAPI(
        @Autowired
        private val monsterService: MonsterService,

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

    @Timed("List monsters", longTask = true)
    @GetMapping("/monsters")
    fun listMonsters(): ResponseEntity<List<Monster>> {
        val monsters = monsterRepository.findAll()

        return ResponseEntity.status(200).body(monsters.toList())
    }

    @Timed
    @ApiOperation("Retrieve Monster information on a specific monster")
    @GetMapping(path = ["/{monsterId}"])
    fun getMonsterInfo(
            @PathVariable("monsterId") monsterid: String
    ): ResponseEntity<MonsterDto> {

        val monster = monsterService.findByIdEager(monsterid)
        if (monster == null) {
            notFoundCreation.increment()
            return ResponseEntity.notFound().build()
        }
        meterRegistry.gauge("TestGauge", 3)
        return ResponseEntity.status(200).body(DtoConverter.transform(monster))
    }

    @Timed
    @ApiOperation("Create a new monster, with given ID")
    @PutMapping(path = ["/{monsterId}"])
    fun createMonster(
            @PathVariable("monsterId") monsterId: String
    ): ResponseEntity<Void> {
        val ok = monsterService.registerNewMonster(monsterId)
        creationCounter.increment()
        meterRegistry.gauge("TestGauge2", 5)
        return if (!ok) ResponseEntity.status(400).build()
        else ResponseEntity.status(201).build()
    }

   /* @PostMapping("/monsters")
    @Timed
    fun createMonsterPOST(): ResponseEntity<Monster> {

        val monster = monsterRepository.save(Monster())

        creationCounter.increment()

        return ResponseEntity.ok(monster)
    }*/

    @Timed
    @PostMapping(path = ["/monster"], consumes = ["application/json"], produces = ["application/json"])
    fun addMember(@RequestBody monster: Monster) {
        meterRegistry.counter("count3", "currency", monster.monsterId).increment()
        meterRegistry.gauge("TestGauge", 4)
    }

    @PostMapping("/monsters/{monsterId}/rarities")
    @Timed
    fun createRarity(@PathVariable("monsterId") monsterId: String, @RequestBody rarityVariables: RarityDto): ResponseEntity<Monster> {

        val monster = monsterRepository.findByIdOrNull(monsterId)
        if (monster == null) {
            notFoundCreation.increment()
            return ResponseEntity.status(404).body(null)
        }

        monsterRarityRepository.save(MonsterRarity(
                rarityVariables.rarity,
                rarityVariables.startValue,
                rarityVariables.endValue,
                monster
        ))
        monsterRaritySummary.record(rarityVariables.rarity.toDouble())

        return ResponseEntity.ok(monsterRepository.findByIdOrNull(monsterId)!!)

    }

    /*@GetMapping("/monsters/{monsterId}/rarities")
    @Timed
    fun getRarities(@PathVariable("monsterId") monsterId: String): ResponseEntity<List<MonsterRarity>> {

        val monster = monsterRepository.findByIdOrNull(monsterId)
        if (monster == null) {
            notFoundCreation.increment()
            return ResponseEntity.status(404).body(null)
        }
        return ResponseEntity.ok(monster.rarity.toList())
    }*/

}
