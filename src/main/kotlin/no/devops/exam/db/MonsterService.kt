package no.devops.exam.db

import org.springframework.data.jpa.repository.Lock
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.LockModeType

@Repository
interface MonsterRepository : CrudRepository<Monster, String> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Monster m WHERE m.monsterId = :id")
    fun lockedFind(@Param("id") monsterID: String): Monster?

}

@Service
@Transactional
class MonsterService(
        private val monsterRepository: MonsterRepository
) {

    fun findByIdEager(monsterId: String): Monster? {
        val monster = monsterRepository.findById(monsterId).orElse(null)
        if (monster != null) {
            monster.monsterId
        }
        return monster
    }

    fun registerNewMonster(monsterID: String): Boolean {

        if (monsterRepository.existsById(monsterID)) {
            return false
        }

        val monster = Monster()
        monster.monsterId = monsterID
        monsterRepository.save(monster)
        return true
    }

    private fun validateMonster(monsterID: String) {
        if (!monsterRepository.existsById(monsterID)) {
            throw IllegalArgumentException("Monster $monsterID does not exist!")
        }
    }

}