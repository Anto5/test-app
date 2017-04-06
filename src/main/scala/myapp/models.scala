package models




object stringsToHtml {
def aggregator ( astrings : Array[String], bstrings : Array[String], cstrings : Array[String] ) :StringBuilder= {
val b = new StringBuilder
b ++= "<p>"
for (i <- astrings){
b++=i + "<br/>"
}
b++="</p>"
b ++= "<p>"
for (i <- bstrings){
b++=i + "<br/>"
}
b++="</p>"
b ++= "<p>"
for (i <- cstrings){
b++=i + "<br/>"
}
b++="</p>"
return (b)
}
}

object autoComposer{
def composer ( brText : String,  colonna:  List[String] ) : String = {
for (i <- colonna){
val inter =  brText.intersect(i)
if (inter == brText)
  return (i)
}
return (brText)
}
}