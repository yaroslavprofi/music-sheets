import io.ktor.application.*
import io.ktor.html.*
import io.ktor.html.HtmlContent
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.html.*
import kotlinx.html.dom.document
import java.io.File

val main_html = File("build/resources/main/main.html").readText()

fun Application.main() {
    routing {
        static("/") {
            resources("/")
        }

        get("/") {
            call.respondText(main_html, ContentType.Text.Html)
        }

        val keyToUrl = mutableMapOf<String, String>()

        get("/upload") {

            val name = call.parameters["name"]!!

            call.respondText(name)

        }
    }
}