package pagecontroller
import org.scalatra.ScalatraServlet 
import org.scalatra._
import models._
import slicksupport._
import scalate.ScalateSupport




case class PageController extends ScalatraServlet with ScalateSupport { 

  get("/home") { 
  {
<html>
<body>
<h1>My app test page</h1>
<h2>Main menu</h2> <br/><a href="http://localhost:8080/db/search">Query</a><br/>
<a href="http://localhost:8080/db/report">Report</a>
</body>
</html> }
  }

 
  
  
}


