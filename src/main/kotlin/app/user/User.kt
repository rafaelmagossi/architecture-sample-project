package app.user

import org.bson.types.ObjectId
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import java.util.Date
import java.util.UUID

@Entity(noClassnameStored = true)
@Indexes(Index(value = "id", fields = arrayOf(Field(value = "id"))))
data class User(
    @Id var id: String = "",
    var name: String = "",
    var lastName: String = "",
    var email: String = "",
    var phone: String = "",
    var password: String = "",
    var logged: Boolean = false,
    var authToken: String = "",
    var role: String = "",
    var createdAt: Date = Date(0),
    var lastSignInAt: Date = Date(0),
    var updatedAt: Date = Date(0)
) {

    @PrePersist
    fun prePersist() {
        id = ObjectId().toString()
        logged = true
        authToken = UUID.randomUUID().toString()
        createdAt = Date()
        lastSignInAt = Date()
    }
}
