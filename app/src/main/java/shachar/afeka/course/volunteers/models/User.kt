package shachar.afeka.course.volunteers.models

import java.util.Date

data class User(
    val id: String?,
    val name: String?,
    val email: String?,
    val phone: String?,
    val residence: String?,
    val createdAt: Date?,
    val updatedAt: Date?
) {
    class Builder(
        var id: String? = null,
        var name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var residence: String? = null,
        var createdAt: Date? = null,
        var updatedAt: Date? = null
    ) {
        fun id(uid: String?) = apply { this.id = uid }
        fun name(name: String?) = apply { this.name = name }
        fun email(email: String?) = apply { this.email = email }
        fun phone(phone: String?) = apply { this.phone = phone }
        fun residence(residence: String?) = apply { this.residence = residence }
        fun createdAt(createdAt: Date?) = apply { this.createdAt = createdAt }
        fun updatedAt(updatedAt: Date?) = apply { this.updatedAt = updatedAt }
        fun build() = User(id, name, email, phone, residence, createdAt, updatedAt)
    }
}
