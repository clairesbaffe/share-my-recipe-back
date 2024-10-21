import com.example.application.port.output.UserRepositoryPort
import com.example.domain.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import org.mindrot.jbcrypt.BCrypt

data class RegisterRequest(val username: String, val password: String)
data class RegisterResponse(val message: String)

fun Route.registerController() {
    val userRepository: UserRepositoryPort by inject()

    post("/register") {
        val registerRequest = call.receive<RegisterRequest>()
        val existingUser = userRepository.findByUsername(registerRequest.username)

        if (existingUser != null) {
            call.respond(HttpStatusCode.Conflict, "Nom d'utilisateur déjà pris")
        } else {
            val passwordHash = BCrypt.hashpw(registerRequest.password, BCrypt.gensalt())
            val newUser = User(
                id = 0,
                username = registerRequest.username,
                passwordHash = passwordHash,
                roles = listOf("ROLE_USER")
            )
            userRepository.save(newUser)
            call.respond(RegisterResponse("Inscription réussie"))
        }
    }
}