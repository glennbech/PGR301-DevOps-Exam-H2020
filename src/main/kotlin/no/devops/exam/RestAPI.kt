package no.devops.exam

import io.micrometer.core.instrument.Counter
import io.micrometer.core.instrument.DistributionSummary
import io.micrometer.core.instrument.MeterRegistry
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.devops.exam.db.Monster
import no.devops.exam.db.MonsterRepository
import no.devops.exam.db.MonsterService
import no.devops.exam.dto.MonsterDto
import org.springframework.beans.factory.annotation.Autowired
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
        private var meterRegistry: MeterRegistry
) {

    private val creationCounter = Counter.builder("counter.metersCreated").description("Meters created").register(meterRegistry)
    private val notFoundCreation = Counter.builder("counter.meterNotFound").description("Meter not found").register(meterRegistry)
    private val monsterRaritySummary = DistributionSummary.builder("distribution.monsterRarity")
            .description("MonsterRarity Distribution")
            .baseUnit("rarity")
            .publishPercentiles(0.3, 0.5, 0.95)
            .register(meterRegistry)

    @GetMapping("/api/monsters")
    fun listMonsters(): ResponseEntity<List<Monster>> {
        val monsters = monsterRepository.findAll()

        return ResponseEntity.status(200).body(monsters.toList())
    }


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

    @ApiOperation("Create a new monster, with given ID")
    @PutMapping(path = ["/{monsterId}"])
    fun createMonster(
            @PathVariable("monsterId") monsterId: String
    ): ResponseEntity<Void> {
        val ok = monsterService.registerNewMonster(monsterId)
        creationCounter.increment()
        meterRegistry.gauge("TestGauge2", 3)
        return if (!ok) ResponseEntity.status(400).build()
        else ResponseEntity.status(201).build()

    }

    @PostMapping(path = ["/tx"], consumes = ["application/json"], produces = ["application/json"])
    fun addMember(@RequestBody monster: Monster) {
        meterRegistry.counter("count3", "currency", monster.monsterId).increment()
    }
}
