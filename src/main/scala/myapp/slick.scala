package slicksupport
import scala.collection.mutable.ArrayBuffer
import scala.collection.mutable.ListBuffer
import scala.collection.immutable.ListMap
import org.scalatra._
import reader._
import scala.slick.driver.MySQLDriver.simple._
import Database.threadLocalSession
import pagecontroller._
import models._




// Definition of the AIRPORTS table
object Airports extends Table[(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)]("AIRPORTS") {
def id = column[String]("ARPT_ID")
def ident = column[String] ("ARPT_IDENT")
def arpt_type = column[String]("ARPT_TYPE")
def name = column[String]("ARPT_NAME")
def latitude_deg = column[String] ("LATITUDE_D")
def longitude_deg = column[String] ("LONGITUDE_D")
def elevation_ft = column[String] ("ELEVATION_FT")
def continent = column[String]("CONTINENT")
def iso_country = column[String]("ISO_COUNTRY")
def municipality = column[String]("MUNICIPALITY")
def scheduled_service = column[String]("SCHEDULED_SRV")
def gps_code = column[String]("GPS_CODE")
def iata_code = column[String]("IATA_CODE")
def local_code = column[String]("LOCAL_CODE")
def home_link = column[String]("HOME_LINK")
def wikipedia_link = column[String]("WIKIPEDIA_LINK")
def keywords = column[String]("KEYWORDS")


def * = id ~ ident ~ arpt_type ~ name ~ latitude_deg ~ longitude_deg ~ elevation_ft ~ continent ~ iso_country ~ municipality ~ scheduled_service ~ gps_code ~ iata_code ~ local_code ~ home_link ~ wikipedia_link ~ keywords
}
 
// Definition of the RUNWAYS table
object Runways extends Table[(String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String, String)]("RUNWAYS") {
def id = column[String]("RNW_ID")
def airport_ref = column[String]("ARPT_REFERECT") 
def airport_ident = column[String]("ARPT_IDENT")
def length_ft = column[String]("LENGTH_FT")
def width_ft = column[String] ("WIDTH_FT")
def surface = column[String]("SURFACE")
def lighted = column[String]("LIGHTED")
def closed = column[String]("CLOSED")
def le_ident = column[String]("LE_IDENT")
def le_latitude_deg = column[String]("LE_LATITUDE_DEG")
def le_longitude_deg = column[String]("LE_LONGITUDE_DEG")
def le_elevation_ft = column[String]("LE_ELEVATION_FT")
def le_heading_degT = column[String]("LE_HEADING_FT")
def le_displaced_threshold_ft = column[String]("LE_DISPLACED_THRESHOLD_FT")
def he_ident = column[String]("HE_IDENT")
def he_latitude_deg = column[String]("HE_LATITUDE_DEG")
def he_longitude_deg = column[String]("HE_LONGITUDE_DEG")
def he_elevation_ft = column[String]("HE_ELEVATION_FT")
def he_heading_degT = column[String]("HE_HEADING_DEGT")
def he_displaced_threshold_ft = column[String]("HE_DISPLACED_THRESHOLD_FT")
def * = id ~ airport_ref ~ airport_ident ~ length_ft ~ width_ft ~ surface ~ lighted ~ closed ~ le_ident ~ le_latitude_deg ~ le_longitude_deg ~ le_elevation_ft ~ le_heading_degT ~ le_displaced_threshold_ft ~ he_ident ~ he_latitude_deg ~ he_longitude_deg ~ he_elevation_ft ~ he_heading_degT ~ he_displaced_threshold_ft

}

// Definition of the Countries table
object Countries extends Table[(String, String, String, String, String, String)]("COUNTRIES") {
def id = column[String]("CNTR_ID") 
def code = column[String]("CODE")
def name = column[String]("CNTR_NAME")
def continent = column[String]("CONTINENT")
def wikipedia_link = column[String]("WIKIPEDIA_LINK")
def keywords = column[String]("KEYWORDS")
def * = id ~ code ~ name ~ continent ~ wikipedia_link ~ keywords

}

  
  





case class SlickApp(db: Database) extends ScalatraServlet with SlickRoutes

trait SlickRoutes extends ScalatraServlet {

  val db: Database

  get("/create-tables") {
    db withSession {
      (Airports.ddl ++ Runways.ddl ++ Countries.ddl).create
    }
  }

  get("/load-data") {
    db withSession {
      // Insert data Countries
     val ioc = reader.readCSV.fileCSV("countries.csv")
	  for(i <- ioc ){
	  val ioi = reader.splitter.stringSplt(i)
	  val ioi2 = reader.filler.fillerAdj(ioi)
	  
	  Countries.insert(ioi2(0),ioi2(1),ioi2(2),ioi2(3),ioi2(4),ioi2(5))}
	  // Insert data Airports
	  val ioa = reader.readCSV.fileCSV("airports.csv")
	  for(i <- ioa) {
	  val ioi = reader.splitter.stringSplt(i)
	  val ioi2 = reader.filler.fillerAdj(ioi)	  

	  Airports.insert(ioi2(0),ioi2(1),ioi2(2),ioi(3),ioi2(4),ioi2(5),ioi2(6),ioi2(7),ioi2(8),ioi2(9),ioi2(10),ioi2(11),ioi2(12),ioi2(13),ioi2(14),ioi2(15),ioi2(16))}
	  // Insert data Runways
	  val ior = reader.readCSV.fileCSV("runways.csv")
	  for(i <- ior) {
	  val ior = reader.splitter.stringSplt(i)
	  val ioi2 = reader.filler.fillerAdj(ior)
	  Runways.insert(ioi2(0), ioi2(1), ioi2(2), ioi2(3), ioi2(4), ioi2(5), ioi2(6), ioi2(7), ioi2(8), ioi2(9), ioi2(10), ioi2(11), ioi2(12), ioi2(13), ioi2(14), ioi2(15), ioi2(16), ioi2(17), ioi2(18), ioi2(19))
	  }
  }
  }

  get("/drop-tables") {
    db withSession {
      (Airports.ddl ++ Runways.ddl ++ Countries.ddl).drop
    }
	
	
  }
  
  get("/report") {
db withSession {
val q = for { c <- Airports } yield c.iso_country
val fin = q.list.map{ case (s1)=> s1 }
val result = fin.groupBy(identity).mapValues(_.size)  
val constr =ListMap(result.toSeq.sortWith(_._1 > _._1):_*)  
val report = constr.take(10)
val stringa: String = report.map { case (s1, s2)=> "The number of airports in " + s1 + " is " + s2} mkString ("\n")  
val stringhe = stringa.split("\n")

val constr2 =ListMap(result.toSeq.sortWith(_._1 < _._1):_*)  
val report2 = constr2.take(10)
val stringas2: String = report2.map { case (s1, s2)=> "The number of airports in " + s1 + " is " + s2} mkString ("\n")  
val stringhes2 = stringas2.split("\n")

 
		   			val first_innerJoin = for {
  (c, s) <- Countries join Airports on (_.code === _.iso_country)
} yield (s)
		   val innerJoin = for {
  (c, s) <- first_innerJoin join Runways on (_.ident === _.airport_ident)
} yield (c.iso_country, s.surface)
val stringa2: String = innerJoin.list.map { case (s1, s2)=> s2 + " is the type of runway  in " + s1} mkString ("\n")
val stringhe2 = stringa2.split("\n").distinct // split in more sentences and remove duplicates
  contentType="text/html"
  models.stringsToHtml.aggregator(stringhe , stringhes2, stringhe2)


  }  
}
  
  
  
    get("/search") {
  contentType="text/html"
 <form action="/db/search" method="post">
    <div><label>txtQuery</label><input type="text" name="txtQuery"></input></div>

    <div><input type="submit"/></div>
</form>
  }
  
 
  
   post("/search") {

 val txtQuery = params("txtQuery")
 val lg = txtQuery.length()

   db withSession {
		  if (lg==2){
		  
		  
		   val q = for { c <- Airports if c.iso_country === txtQuery } yield (c)
			 	  
			val innerJoin = for {
  (c, s) <- q join Runways on (_.ident === _.airport_ident)
} yield (c.name, s.id)
     
      contentType = "text/html"
           innerJoin.list.map { case (s1, s2)=> s1 + " is the aiport for the runway " + s2} mkString "<br />"
		   
		   }
		   else {  
		   val z = for {
  (c, s) <- Countries join Airports on (_.code === _.iso_country)
} yield (c.name)		   
		  
           val fz = z.list.map{ case (s1)=> s1 }
		   
		   val txtSearch = models.autoComposer.composer(txtQuery, fz )
		   val q = for { c <- Countries if c.name === txtSearch } yield (c)
		   			val first_innerJoin = for {
  (c, s) <- q join Airports on (_.code === _.iso_country)
} yield (s)
		   val innerJoin = for {
  (c, s) <- first_innerJoin join Runways on (_.ident === _.airport_ident)
} yield (c.name, s.id)
	   contentType = "text/html"
      innerJoin.list.map { case (s1, s2)=> s1 + " is the aiport for the runway " + s2} mkString "<br />"
		   
		   }
			 

			
     
  }



 
 }
  
  
  
}



