package no.devops.exam

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import no.devops.exam.db.MonsterService
import no.devops.exam.dto.MonsterDto
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
        private val monsterService: MonsterService
) {

    @ApiOperation("Retrieve Monster information on a specific monster")
    @GetMapping(path = ["/{monsterId}"])
    fun getMonsterinfo(
            @PathVariable("monsterId") monsterid: String
    ): ResponseEntity<MonsterDto> {

        val monster = monsterService.findByIdEager(monsterid)
        if (monster == null) {
            return ResponseEntity.notFound().build()
        }
        return ResponseEntity.status(200).body(DtoConverter.transform(monster))
    }

    @ApiOperation("Create a new monster, with given ID")
    @PutMapping(path = ["/{monsterId}"])
    fun createMonster(
            @PathVariable("monsterId") monsterId: String
    ): ResponseEntity<Void> {
        val ok = monsterService.registerNewMonster(monsterId)
        return if (!ok) ResponseEntity.status(400).build()
        else ResponseEntity.status(201).build()
    }
}
