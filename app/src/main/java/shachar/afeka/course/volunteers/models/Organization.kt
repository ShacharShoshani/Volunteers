package shachar.afeka.course.volunteers.models

import shachar.afeka.course.volunteers.interfaces.IRecord
import shachar.afeka.course.volunteers.interfaces.IRecordBuilder
import java.util.Date

data class Organization(
    override val id: String?,
    override val name: String?,
    val about: String?,
    val headquarters: Coordinates,
    val ownerId: String?,
    override val createdAt: Date? = null,
    override val updatedAt: Date? = null
) : IRecord {
    class Builder(
        override var id: String? = null,
        override var name: String? = null,
        var about: String? = null,
        var headquarters: Coordinates = Coordinates.Default,
        var ownerId: String? = null,
        override var createdAt: Date? = null,
        override var updatedAt: Date? = null
    ) : IRecordBuilder<Organization> {
        override fun id(uid: String?) = apply { this.id = uid }

        override fun name(name: String?) = apply { this.name = name }

        fun about(about: String?) = apply { this.about = about }
        fun headquarters(headquarters: Coordinates) = apply { this.headquarters = headquarters }
        fun ownerId(ownerId: String?) = apply { this.ownerId = ownerId }

        override fun createdAt(createdAt: Date?) =
            apply { this.createdAt = createdAt }

        override fun updatedAt(updatedAt: Date?) =
            apply { this.updatedAt = updatedAt }

        override fun build(): Organization =
            Organization(id, name, about, headquarters, ownerId, createdAt, updatedAt)

    }
}