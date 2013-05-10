import util.parsing.json.JSON
import io.Source
import java.awt.geom.Path2D
import java.lang.{Double=>JDouble}

trait Country {
  def get(lat:Double, lng:Double):String
}

case class CountryPolygon(poly: Path2D.Double, name: String)

case class CountryPolygons(filename: String) extends Country {

  private def getWorldPolygons(filename: String) = Source.fromFile(filename).mkString

  private def parsePoly(coord:List[List[List[(Double,Double)]]]): Path2D.Double = {
    val mypoly = new Path2D.Double()
    val first = coord.head.head
    val (x,y) = (first(0).asInstanceOf[JDouble],first(1).asInstanceOf[JDouble])
    mypoly.moveTo(x,y)
    coord.head.tail.foreach(t => mypoly.lineTo(t(0).asInstanceOf[JDouble], t(1).asInstanceOf[JDouble]))
    mypoly
  }

  private def parseJSON(json:String) = JSON.parseFull(json) match {
    case m:Some[Map[String,Any]] =>
      m.get("features") match {
        case list:List[Map[String,Any]] =>
          list.map { country =>

            // get name of the country
            val myname = country("properties") match {
              case info:Map[String,String] => info("name").mkString.replace(" ", "_") // for United_Arab_Emirates & the like
            }

            // get geometry
            val mygeom = country("geometry") match {
              case info:Map[String,Any] =>
                info("type").toString match {

                  case "MultiPolygon" =>
                    info("coordinates") match {
                      case coord:List[List[List[List[(Double,Double)]]]] => coord.map( cc => parsePoly(cc))
                      case _ => throw new Exception("unknown coords")
                    }

                  case "Polygon" =>
                   info("coordinates") match {
                    case coord:List[List[List[(Double,Double)]]] => List(parsePoly(coord))
                    case _ => throw new Exception("unknown coords")
                  }
                }
            }
            mygeom.map( m => CountryPolygon(m,myname))
          }
      }
  }
  lazy val poly = parseJSON(getWorldPolygons(filename)) match {
    case cp:List[List[CountryPolygon]] => cp.flatten
  }

  def get(lat:Double, lng:Double):String = {
    // each poly is a list of (lng,lat), NOT (lat,lng) => hence we swap latlng below
    poly.find{ cp => cp.poly.contains(lng, lat)} match {
      case cp:Some[CountryPolygon] => cp.get.name
      case None => "Not Found!"
    }
  }
}

object CountryPolygons {
  lazy val cp = CountryPolygons("countries.geo.json")
  def get(lat:Double, lng:Double) = cp.get(lat, lng)
  def main(args:Array[String]):Unit = println(cp.get(args(0).toDouble, args(1).toDouble))
}
