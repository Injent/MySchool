package me.injent.myschool.core.model.alias

import me.injent.myschool.core.model.Mark
import me.injent.myschool.core.model.Subject

typealias SingleSubjectAndMarks = Pair<Subject, List<Mark>>
typealias SubjectsAndMarks = List<SingleSubjectAndMarks>

typealias PersonNameAndMark = Pair<String, Float>
typealias PersonNamesAndMark = List<PersonNameAndMark>