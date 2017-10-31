import app.user.User
import config.api.DataParser
import config.api.bodyAsObject
import config.api.render
import db.Repository
import org.bson.types.ObjectId
import spark.Route
import spark.Spark
import java.util.Date

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
