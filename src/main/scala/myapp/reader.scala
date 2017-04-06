package reader

import scala.io.Source


object readCSV {

def fileCSV (filename: String): Array[String] ={

      val fileLines = io.Source.fromFile(filename)("UTF-8").getLines.drop(1).toArray

val nuovo = for(i <- fileLines) yield { if (i.takeRight(1) == ","){
i.concat("<empty>")}
else 
i
}
return (nuovo)
}
}

object splitter {

def stringSplt (stringa: String) : Array[String]  = {

val arr = stringa.split(",")


return (arr)


}
}

object filler {

def fillerAdj ( vettore: Array[String]) : Array[String] ={
val myVect = for (i<- vettore) yield {
  
  if (i == null || i.isEmpty) "<empty>" else i

}
return (myVect)
}
}

