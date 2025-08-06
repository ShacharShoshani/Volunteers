package shachar.afeka.course.volunteers.models

data class User(val name: String?, val email: String?, val phone: String?, val residence: String?) {
    class Builder(
        var name: String? = null,
        var email: String? = null,
        var phone: String? = null,
        var residence: String? = null
    ) {
        fun name(name: String?) = apply { this.name = name }
        fun email(email: String?) = apply { this.email = email }
        fun phone(phone: String?) = apply { this.phone = phone }
        fun residence(residence: String?) = apply { this.residence = residence }
        fun build() = User(name, email, phone, residence)
    }
}
