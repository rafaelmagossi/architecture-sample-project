package db.connection

import app.user.User
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia

class DataBaseConnection() {

    val morphia by lazy { Morphia() }
    val username by lazy { "inteli-dev" }
    val password by lazy { "omega3d" }
    val name by lazy { "inteli-dev-db" }

    fun createDatastore(): Datastore {
        morphia.map(User::class.java)
        return morphia.createDatastore(createMongoClient(), name)
    }

    fun createMongoClient(): MongoClient {
        val serverAddress = ServerAddress("localhost", 27017)
        val credential = MongoCredential.createCredential(username, name, password.toCharArray())
        return MongoClient(serverAddress, listOf(credential), createMongoClientOptions())
    }

    fun createMongoClientOptions(): MongoClientOptions {
        return MongoClientOptions.Builder()
            .connectTimeout(10000)
            .connectionsPerHost(100)
            .build()
    }
}