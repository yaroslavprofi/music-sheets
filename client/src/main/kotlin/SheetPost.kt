import kotlinx.html.*
import org.w3c.dom.HTMLElement

class SheetPost(
    private val name: String,
    private val instrument: String,
    private val difficulty: String,
    private val comment: String
) {

    fun TagConsumer<HTMLElement>.render() {
        article(classes = "sheet-example") {
            img { src = "test_img.jpg" }
            div(classes = "sheet-info") {
                span(classes = "name") { +name }
                span(classes = "instrument") { +instrument}
                span(classes = "difficulty") { +difficulty}
                span(classes = "comment") { +comment }
            }
        }
    }
}