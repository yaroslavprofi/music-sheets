import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import org.w3c.dom.*


class SheetPost(
    private val id: String,
    private val name: String,
    private val instrument: String,
    private val difficulty: String,
    private val comment: String
) {

    fun TagConsumer<HTMLElement>.render() {
        article(classes = "sheet-example") {
            div(classes = "sheet-info") {
                a(classes = "link", href = "/download?id=${this@SheetPost.id}") {
                    img { src = "test_img.jpg" }
                    type = "download"
                    span(classes = "name") { +name }
                }
                span(classes = "instrument") { +instrument }
                span(classes = "difficulty") { +difficulty }
                span(classes = "comment") { +comment }
            }
        }
    }
}