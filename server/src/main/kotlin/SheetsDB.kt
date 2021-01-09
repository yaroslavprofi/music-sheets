import java.sql.*

class SheetsDB {

    private val urlMySQL = "jdbc:mysql://localhost:3306/sheets_db?serverTimezone=UTC"
    private val tableName = "sheets_posts";
    private val user =
    private val password =


    private var connection: Connection? = null


    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(urlMySQL, user, password)

            createTable()
            println("create table sheet_posts")


        } catch (e: SQLException) {
            println(e.message)
            e.stackTrace
        }
    }

    private fun createTable() {
        val table = connection!!.createStatement()
        table.execute(
            """               
            CREATE TABLE IF NOT EXISTS $tableName
            (
            id BIGINT AUTO_INCREMENT NOT NULL,
            name TEXT NOT NULL,
            instrument TEXT NOT NULL,
            difficulty TEXT NOT NULL,
            comment TEXT NOT NULL,
            date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
            PRIMARY KEY (id)
            );
            """
        )
    }

    fun post(
        name: String,
        instrument: String,
        difficulty: String,
        comment: String
    ) {
        val statement = connection!!.prepareStatement(
            """
            INSERT INTO $tableName (name, instrument, difficulty, comment) Values(?, ?, ?, ?)
            """.trimIndent()
        )
        statement.setString(1, name)
        statement.setString(2, instrument)
        statement.setString(3, difficulty)
        statement.setString(4, comment)
        statement.execute()
    }

    fun getPosts(): String {
        val table = connection!!.createStatement()
        val result = table.executeQuery(
            """
SELECT JSON_ARRAYAGG(
               JSON_MERGE(
                       json_object('name', name),
                       json_object('instrument', instrument),
                       json_object('difficulty', difficulty),
                       json_object('comment', comment)
                   )
           )
FROM sheets_posts;
            """.trimIndent()
        )
        result.next()
        val ans = result.getString(1)
        return ans ?: ""
    }
}
