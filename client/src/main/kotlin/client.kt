import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.HTMLButtonElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.HTMLSelectElement
import org.w3c.xhr.XMLHttpRequest
import kotlinx.serialization.*
import kotlinx.serialization.json.*

@Serializable
data class Post(
    val name: String,
    val instrument: String,
    val difficulty: String,
    val comment: String
)

fun loadContent() {

    val http = XMLHttpRequest()

    http.open("GET", "/content")

    http.onload = {
        if (http.status in 200..399) {
            val parsedJson = Json.decodeFromString<List<Post>>(http.responseText)

            for (post in parsedJson) {
                document.getElementById("main")!!.prepend {
                    SheetPost(
                        name = post.name,
                        instrument = post.instrument,
                        difficulty = post.difficulty,
                        comment = post.comment
                    ).run { render() }
                }
            }
        }
    }

    http.send()
}

fun buttonUploadAddEventListener() {
    (document.getElementById("button-upload") as HTMLButtonElement).addEventListener(
        "click", {
            val inputName = document.getElementById("input-name") as HTMLInputElement
            val selectInstrument = document.getElementById("select-instrument") as HTMLSelectElement
            val selectDifficulty = document.getElementById("select-difficulty") as HTMLSelectElement
            val inputComment = document.getElementById("input-comment") as HTMLInputElement

            if (inputName.value.isEmpty()
                || selectInstrument.value.isEmpty()
                || selectDifficulty.value.isEmpty()
            ) {
                window.alert("Incorrect imput 'Sheet name' or 'Instrument'/'Difficulty' not selected")
                return@addEventListener
            }

            val http = XMLHttpRequest()
            http.open(
                "POST", "/upload" +
                        "?name=${inputName.value}" +
                        "&instrument=${selectInstrument.value}" +
                        "&difficulty=${selectDifficulty.value}" +
                        "&comment=${inputComment.value}"
            )
            http.onload = {
                if (http.status in 200..399) {
                    window.location.reload()
                }
            }
            http.send("123")
        }
    )
}

fun main() {
    window.onload = {
        loadContent()
        buttonUploadAddEventListener()
    }
}
