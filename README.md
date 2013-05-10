reversegeo:  Convert (latitude,longitude) to Country
==========
Usage: CountryPolygons provides a get(lat,lng) API. Access via commond-line args, REPL, or programatically.

    $ scalac CountryPolygons.scala 
    $ scala CountryPolygons 37.8 -122.0
    United_States_of_America

    $ scala -cp .
    scala> def lookup = CountryPolygons.get _
    lookup: (Double, Double) => String
    
    scala> val calgary = (51.0,-114.0)
    calgary: (Double, Double) = (51.0,-114.0)
    
    scala> val prudhoebay = (70.0,-148.0)
    prudhoebay: (Double, Double) = (70.0,-148.0)
    
    scala> val sanjoseCR = (10.0,-84.0)
    sanjoseCR: (Double, Double) = (10.0,-84.0)
    
    scala> val bangalore = (12.9,77.5)
    bangalore: (Double, Double) = (12.9,77.5)
    
    scala> val miami = (25.8,-80.2)
    miami: (Double, Double) = (25.8,-80.2)
    
    scala> val cities = List(miami,bangalore,calgary,sanjoseCR,prudhoebay)
    cities: List[(Double, Double)] = List((25.8,-80.2), (12.9,77.5), (51.0,-114.0), (10.0,-84.0), (70.0,-148.0))
    
    scala> cities.map( c => lookup(c._1, c._2)) 
    res17: List[String] = List(United_States_of_America, India, Canada, Costa_Rica, United_States_of_America)
