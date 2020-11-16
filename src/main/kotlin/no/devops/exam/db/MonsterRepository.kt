package no.devops.exam.db

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface MonsterRepository : CrudRepository<Monster, UUID> {

}