import scala.io.StdIn.readLine
import java.sql.PreparedStatement
import java.sql.{Connection, DriverManager}


object Main {
  var connection: Connection = null
  def main(args: Array[String]) {
    println("========== Welcome to Sta$h ==========")
    var addMore = "Y"
    do{
      val inputCategory = readLine("enter category: ")
      val inputAmount = readLine("enter amount: ")

      val currentDate = java.time.LocalDate.now.toString

      dbinsert(inputAmount, inputCategory, currentDate)

      addMore = readLine("would you like to add more expenses? [Y/N]: ")
    }while(addMore == "Y")
    val viewExp = readLine("would you like to view your expenses?[Y/N] ")
    if(viewExp == "Y"){
      viewDB()
    }else{
      println("thank you!")
    }

  }
  //function to view Database
  def viewDB() {
    val statement = connection.createStatement

    val rs = statement.executeQuery("SELECT * FROM usersample")

    println("=================================")
    println("amount | categories |    date    |")
    println("=================================")
    while (rs.next) {
      val amount = rs.getString("amount")
      val categories = rs.getString("categories")
      val date = rs.getString("date")

      println("%7s| %11s| %10s | ".format(amount, categories, date))
    }

    statement.close()
  }

  //function to insert records to Database
  def dbinsert(category: String, amount: String, date: String) {
    // connect to the database named "demodatabase" on port 3306 of localhost
    val url = "jdbc:mysql://localhost:3306/demodatabase"
    val driver = "com.mysql.cj.jdbc.Driver"
    val username = "root"
    val password = "root"

    try {
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)


      val insertSql = """
                        |INSERT INTO usersample (amount, categories, date)
                        |VALUES (?,?,?)
                      """.stripMargin

      val preparedStmt: PreparedStatement = connection.prepareStatement(insertSql)

      preparedStmt.setString (1, category)
      preparedStmt.setString (2, amount)
      preparedStmt.setString (3, date)
      preparedStmt.execute
      preparedStmt.close()

    } catch {
      case e: Exception => e.printStackTrace
    }

  }
}