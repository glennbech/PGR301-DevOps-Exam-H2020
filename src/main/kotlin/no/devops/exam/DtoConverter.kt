package no.devops.exam

import no.devops.exam.db.Monster
import no.devops.exam.dto.MonsterDto

object DtoConverter {

    fun transform(monster: Monster): MonsterDto {

        return MonsterDto().apply {
            monsterId = monster.monsterId
        }
    }
}