import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.sql.*


class SheetsDB {

    private val urlMySQL = "jdbc:mysql://localhost:3306/sheets_db?serverTimezone=UTC"
    private val tableName = "sheetsPosts"
    private val user =
    private val password =


    private var connection: Connection? = null


    init {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver")
            connection = DriverManager.getConnection(urlMySQL, user, password)
            createTable()

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
            file MEDIUMBLOB NOT NULL,
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
        comment: String,
        fileStream: FileInputStream
    ) {
        val statement = connection!!.prepareStatement(
            """
            INSERT INTO $tableName (name, instrument, difficulty, comment, file) Values(?, ?, ?, ?, ?)
            """.trimIndent()
        )
        statement.setString(1, name)
        statement.setString(2, instrument)
        statement.setString(3, difficulty)
        statement.setString(4, comment)
        statement.setBlob(5, fileStream)
        statement.executeUpdate()
    }

    fun getPosts(): String {
        val table = connection!!.createStatement()
        val result = table.executeQuery(
            """
SELECT JSON_ARRAYAGG(
               JSON_MERGE(
                       json_object('id', id),
                       json_object('name', name),
                       json_object('instrument', instrument),
                       json_object('difficulty', difficulty),
                       json_object('comment', comment)
                   )
           )
FROM $tableName;
            """.trimIndent()
        )
        result.next()
        return result.getString(1) ?: ""
    }

    fun getFile(id: String): File? {
        val result = connection!!.createStatement().executeQuery(
            """
                SELECT name,file FROM sheetsposts WHERE id=$id;
            """.trimIndent()
        )
        if (result.next()) {
            val filename = "${result.getString(1)}.pdf"
            val blobStream = result.getBlob(2).binaryStream
            val fos =
                FileOutputStream(filename)
            var temp = blobStream.read()
            while (temp != -1) {
                fos.write(temp)
                temp = blobStream.read()
            }

            blobStream.close()
            fos.close()
            return File(filename)
        }
        return null
    }
}