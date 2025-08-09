package shachar.afeka.course.volunteers.models

import shachar.afeka.course.volunteers.interfaces.IRecord
import shachar.afeka.course.volunteers.interfaces.IRecordBuilder
import java.util.Date

data class User(
    override val id: String?,
    override val name: String?,
    val email: String?,
    val phone: String?,
    val residence: String?,
    override val createdAt: Date?,
    override val updatedAt: Date?
) : IRecord {
    class Builder(
        override var id: String? = null,
        override var name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var residence: String? = null,
        override var createdAt: Date? = null,
        override var updatedAt: Date? = null
    ) : IRecordBuilder<User> {
        override fun id(uid: String?) = apply { this.id = uid }
        override fun name(name: String?) = apply { this.name = name }
        fun email(email: String?) = apply { this.email = email }
        fun phone(phone: String?) = apply { this.phone = phone }
        fun residence(residence: String?) = apply { this.residence = residence }
        override fun createdAt(createdAt: Date?) = apply { this.createdAt = createdAt }
        override fun updatedAt(updatedAt: Date?) = apply { this.updatedAt = updatedAt }
        override fun build() = User(id, name, email, phone, residence, createdAt, updatedAt)
    }
}
