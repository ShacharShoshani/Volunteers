package shachar.afeka.course.volunteers.models

import shachar.afeka.course.volunteers.interfaces.IRecord
import shachar.afeka.course.volunteers.interfaces.IRecordBuilder
import java.util.Date

data class Volunteering(
    override val id: String?,
    override val name: String?,
    val about: String?,
    val place: Coordinates,
    val schedule: List<Date>,
    val organizationId: String?,
    override val createdAt: Date?,
    override val updatedAt: Date?
) : IRecord {
    class Builder(
        override var id: String? = null,
        override var name: String? = null,
        var about: String? = null,
        var place: Coordinates = Coordinates.Default,
        var schedule: List<Date> = emptyList(),
        var organizationId: String? = null,
        override var createdAt: Date? = null,
        override var updatedAt: Date? = null
    ) : IRecordBuilder<Volunteering> {
        override fun id(uid: String?) = apply { this.id = uid }

        override fun name(name: String?) = apply { this.name = name }

        fun about(about: String?) = apply { this.about = about }
        fun place(place: Coordinates) = apply { this.place = place }
        fun schedule(schedule: List<Date>) = apply { this.schedule = schedule }
        fun organizationId(organizationId: String?) = apply { this.organizationId = organizationId }

        override fun createdAt(createdAt: Date?) =
            apply { this.createdAt = createdAt }

        override fun updatedAt(updatedAt: Date?) =
            apply { this.updatedAt = updatedAt }

        override fun build(): Volunteering =
            Volunteering(id, name, about, place, schedule, organizationId, createdAt, updatedAt)
    }
}