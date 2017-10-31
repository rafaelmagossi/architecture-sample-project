import app.user.User
import app.user.UserRepository
import config.api.DataParser
import config.bodyAsObject
import config.render
import db.Repository
import org.bson.types.ObjectId
import spark.Route
import spark.Spark
import java.util.Date

object Main {

    @JvmStatic
    fun main(args: Array<String>) {

        val userRepository by lazy { UserRepository(Repository()) }

        // Função que registrar um novo usuário.
        Spark.post("/users", Route { request, response ->
            // Transformando o JSON recebido na requisição em um objeto User.
            val user = request.bodyAsObject(User::class)

            // Adicionando mias informações ao usuário.
            user.id = ObjectId().toString()
            user.logged = true
            user.lastSignInAt = Date()
            user.createdAt = Date()

            // Salvando usuário no banco de dados.
            userRepository.save(user)

            // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
            return@Route response.render(user, 201)
        }, DataParser())

        // Função de atualiza um usuário já existente.
        Spark.put("/users", Route { request, response ->
             // Transformando o JSON recebido na requisição em um objeto User.
            val user = request.bodyAsObject(User::class)

            if (user.id.isBlank()) {
                // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
                return@Route response.render("Usuário invalido", 422)
            } else {
                // Informando a data de atualização do usuário.
                user.updatedAt = Date()

                //Atualizando o usuário no banco de dados.
                userRepository.update(user)

                // Respondendo ao computador/pessoa que realizou a requisição ao servidor.
                return@Route response.render("Usuário atualizado", 200)
            }
        }, DataParser())
    }
}
