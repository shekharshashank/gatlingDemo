package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import java.net.URLEncoder


class ThrottlingSimulationWithId extends Simulation{
  val httpProtocol = http
    .baseUrl("") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .header("x-api-key","")
    .header("X-Request-ID","GatlingSimulation-"+System.currentTimeMillis() )
    .header("x-gw-ims-org-id","")
    .contentTypeHeader("application/json")
    .authorizationHeader("Bearer <bearer token here>")

  val myIdentifiersFeeder = csv("src/test/resources/data/global-schema-id.csv").random.convert{
    case ("globalSchemaId", string) => URLEncoder.encode(string)
  }

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .feed(myIdentifiersFeeder)
    .exec { session =>
      session
    }
    .exec(http("search global schema by id")
      .get("/global/schemas/${globalSchemaId}"))

  ///throttlingsimulationwithid-20190827085633807
  //18K rpm
  /*
  ---- Global Information --------------------------------------------------------
> request count                                     135000 (OK=134987 KO=13    )
> min response time                                     11 (OK=11     KO=11    )
> max response time                                   7120 (OK=7120   KO=42    )
> mean response time                                    20 (OK=20     KO=17    )
> std deviation                                         29 (OK=29     KO=8     )
> response time 50th percentile                         17 (OK=17     KO=14    )
> response time 75th percentile                         20 (OK=20     KO=17    )
> response time 90th percentile                         23 (OK=23     KO=27    )
> response time 95th percentile                         28 (OK=28     KO=35    )
> mean requests/sec                                149.834 (OK=149.819 KO=0.014 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                        134970 (100%)
> 800 ms < t < 1200 ms                                  13 (  0%)
> t > 1200 ms                                            4 (  0%)
> failed                                                13 (  0%)

  *
  */

  setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
    reachRps(600) in (20 seconds),
    holdFor(15 minute),
    jumpToRps(50),
    holdFor(15 minute)
  )
}
