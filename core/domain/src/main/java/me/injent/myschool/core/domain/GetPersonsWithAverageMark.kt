package me.injent.myschool.core.domain

import kotlinx.coroutines.flow.first
import me.injent.myschool.core.data.repository.MarkRepository
import me.injent.myschool.core.data.repository.PersonRepository
import me.injent.myschool.core.domain.model.PersonWithAverageMark
import me.injent.myschool.core.model.Period
import javax.inject.Inject

/**
 * A use case which obtains a list of persons base data with their average mark by subject or at all
 * sorted by input parameter.
 */
class GetPersonsWithAverageMark @Inject constructor(
    private val personRepository: PersonRepository,
    private val markRepository: MarkRepository
) {
    /**
     * Returns a list of persons base data and their average mark.
     *
     * @param sortBy - the field used to sort the persons. Default NONE = no sorting.
     */
    suspend operator fun invoke(
        period: Period,
        sortBy: PersonWithAverageMarkSort = PersonWithAverageMarkSort.MAX
    ): List<PersonWithAverageMark> {
        val persons = personRepository.persons.first()
            .map { person ->
                PersonWithAverageMark(
                    personId = person.personId,
                    personName = person.shortName,
                    avatarUrl = person.avatarUrl,
                    averageMarkValue = markRepository.getPersonAverageMarkValue(
                        personId = person.personId,
                        dateStart = period.dateStart,
                        dateFinish = period.dateFinish
                    )
                )
            }
        return when (sortBy) {
            PersonWithAverageMarkSort.NONE -> persons
            PersonWithAverageMarkSort.MAX -> {
                persons.sortedByDescending(PersonWithAverageMark::averageMarkValue)
            }
            PersonWithAverageMarkSort.MIN -> {
                persons.sortedBy(PersonWithAverageMark::averageMarkValue)
            }
        }
    }
}

enum class PersonWithAverageMarkSort {
    NONE,
    MAX,
    MIN
}