import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.ObjectMapper
import com.mongodb.MongoClient
import com.mongodb.MongoClientOptions
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.bson.types.ObjectId
import org.mongodb.morphia.Datastore
import org.mongodb.morphia.Morphia
import org.mongodb.morphia.annotations.Entity
import org.mongodb.morphia.annotations.Field
import org.mongodb.morphia.annotations.Id
import org.mongodb.morphia.annotations.Index
import org.mongodb.morphia.annotations.Indexes
import org.mongodb.morphia.annotations.PrePersist
import spark.Request
import spark.Response
import spark.ResponseTransformer
import spark.Route
import spark.Spark
import java.security.InvalidParameterException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import kotlin.reflect.KClass

object Main {

    @JvmStatic
    fun main(args: Array<String>) {
        Spark.post("/users", Route { request, response ->
            // Transformando o JSON recebido na requisição em um objeto User.
            val user = request.bodyAsObject(User::class)

            // Adicionando mias informações ao usuário.
            user.id = ObjectId().toString()
            user.logged = true
            user.lastSignInAt = Date()
            user.createdAt = Date()

            // Salvando usuário no banco de dados.
            Repository().save(user)

            // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
            return@Route response.render(user, 200)
        }, DataParser())
    }
}

/***********************************************************************************************************************
 *                                        Classe que representa o modelo de usuário                                    *
 **********************************************************************************************************************/
// Modelo de dado que representa o usuário.
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

/***********************************************************************************************************************
 *                            Funções que formatam transformam a resposta da API em JSON                               *
 **********************************************************************************************************************/
// Classe que converte objetos em JSON.
class DataParser : ResponseTransformer {

    val mapper by lazy { ObjectMapper() }

    override fun render(model: Any): String {
        return if (model is Model) toJson(model.body) else "{}"
    }

    fun toJson(model: Any): String {
        try {
            mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            return mapper.writeValueAsString(model)
        } catch (e: Exception) {
            return "{}"
        }
    }

    fun <T : Any> toObject(json: String, kClass: KClass<out T>): T {
        try {
            mapper.dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            return mapper.readValue(json, kClass.java)
        } catch (e: Exception) {
            return kClass.java.newInstance()
        }
    }
}

// Modelo de dados utilizado como resposta para o usuário.
data class Model(var body: Any, @JsonIgnore var status: Int)

/***********************************************************************************************************************
 *                                                  Funções estendidas                                                 *
 **********************************************************************************************************************/

// Função que monta o objeto que será retornado pela API como resposta a ação do usuário.
fun Response.render(body: Any, status: Int): Model {
    type("application/json")
    status(status)
    return Model(body, status)
}

// Função que extrai a informação da requisição recebida pelo servidor.
fun <T : Any> Request.bodyAsObject(kClass: KClass<T>) = body().toObject(kClass)

// Função que converte json para objeto.
fun <T : Any> String.toObject(kClass: KClass<out T>) = DataParser().toObject(this, kClass)

/***********************************************************************************************************************
 *                                           Configurações do Banco de Dados                                           *
 **********************************************************************************************************************/
// Classe que realiza a conexão com o banco de dados.
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

// Classe que executa operações no banco de dados.
class Repository {

    val dataStore by lazy { DataBaseConnection().createDatastore() }

    fun save(entity: Any) {
        if (entity::class == Any::class)
            throw InvalidParameterException("entity: Any can't be a direct instance of Any class")
        else
            dataStore.save(entity)
    }
}

