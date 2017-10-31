package app.user

import db.Repository
import db.RepositoryContract
import java.util.Date

class UserRepository(repository: Repository) : RepositoryContract by repository {

    override fun update(entity: Any): Boolean {
        require(entity is User, { "UserRepository ==> The entity must be an User object!" })
        entity as User
        entity.updatedAt = Date()
        val operations = getDatastore().createUpdateOperations(User::class.java)
            .set("name", entity.name)
            .set("lastName", entity.lastName)
            .set("email", entity.email)
            .set("phone", entity.phone)
            .set("password", entity.password)
            .set("logged", entity.logged)
            .set("authToken", entity.authToken)
            .set("createdAt", entity.createdAt)
            .set("updatedAt", entity.updatedAt)
        return getDatastore().update(entity, operations).updatedExisting
    }
}