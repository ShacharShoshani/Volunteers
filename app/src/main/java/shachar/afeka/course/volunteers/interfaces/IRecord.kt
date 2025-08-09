package shachar.afeka.course.volunteers.interfaces

import java.util.Date

interface IRecord {
    val id: String?
    val name: String?
    val createdAt: Date?
    val updatedAt: Date?
}