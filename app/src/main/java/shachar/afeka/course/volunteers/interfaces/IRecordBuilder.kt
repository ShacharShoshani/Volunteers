package shachar.afeka.course.volunteers.interfaces

import java.util.Date

interface IRecordBuilder<T> {
    var id: String?
    var name: String?
    var createdAt: Date?
    var updatedAt: Date?

    fun id(uid: String?): IRecordBuilder<T>
    fun name(name: String?): IRecordBuilder<T>
    fun createdAt(createdAt: Date?): IRecordBuilder<T>
    fun updatedAt(updatedAt: Date?): IRecordBuilder<T>
    fun build(): T
}