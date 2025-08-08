package shachar.afeka.course.volunteers.models

import java.util.Date

data class Organization(
    val id: String?,
    val name: String?,
    val about: String?,
    val headquarters: Coordinates?,
    val ownerId: String?,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    class Builder(
        var id: String?,
        var name: String?,
        var about: String?,
        var headquarters: Coordinates?,
        var ownerId: String?,
        var createdAt: Date? = null,
        var updatedAt: Date? = null
    ) {
        fun id(uid: String?) = apply { this.id = uid }
        fun name(name: String?) = apply { this.name = name }
        fun about(about: String?) = apply { this.about = about }
        fun headquarters(headquarters: Coordinates?) = apply { this.headquarters = headquarters }
        fun ownerId(ownerId: String?) = apply { this.ownerId = ownerId }
        fun createdAt(createdAt: Date?) = apply { this.createdAt = createdAt }
        fun updatedAt(updatedAt: Date?) = apply { this.updatedAt = updatedAt }
        fun build() = Organization(id, name, about, headquarters, ownerId, createdAt, updatedAt)
    }
}
