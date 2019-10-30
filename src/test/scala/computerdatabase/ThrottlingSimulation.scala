package computerdatabase;

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


 class ThrottlingSimulation extends Simulation{
   private val authToken = System.getProperty("AUTH_TOKEN");
   val httpProtocol = http
     .baseUrl("") // Here is the root for all relative URLs
     .acceptHeader("application/json") // Here are the common headers
     .acceptLanguageHeader("en-US,en;q=0.5")
     .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
     .header("x-api-key","")
     .header("x-gw-ims-org-id","")
     .contentTypeHeader("application/json")
     .authorizationHeader("Bearer "+authToken)

   val myIdentifiersFeeder = csv("data/global-schema-id.csv").random

   val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
     .exec(http("global schema list param")
     .get("/global/schemas?limit=10&offset=10"))
     .exec(http("tenant schema list param")
     .get("/tenant/schemas?limit=10&offset=10"))

  /*
   //scenario-1  failed (12.3 k rmp in one minute , scaled up to 4 container)
   setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
     reachRps(500) in (60 seconds),
     holdFor(15 minute),
     jumpToRps(50),
     holdFor(15 minute)
   )

   //scenario-2 failed(high error rate)
   setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
     reachRps(300) in (60 seconds),
     holdFor(15 minute),
     jumpToRps(50),
     holdFor(15 minute)
   )

   //scenario-2 failed(high error rate)
   setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
     reachRps(300) in (120 seconds),
     holdFor(15 minute),
     jumpToRps(50),
     holdFor(15 minute)
   )
*/
   //scenario-3(Passing with 12K rpm )
   //throttlingsimulation-20190826094101458
   /*
   ================================================================================
---- Global Information --------------------------------------------------------
> request count                                     237058 (OK=237055 KO=3     )
> min response time                                     13 (OK=23     KO=13    )
> max response time                                   3184 (OK=3184   KO=13    )
> mean response time                                    56 (OK=56     KO=13    )
> std deviation                                         31 (OK=31     KO=0     )
> response time 50th percentile                         50 (OK=50     KO=13    )
> response time 75th percentile                         62 (OK=62     KO=13    )
> response time 90th percentile                         80 (OK=80     KO=13    )
> response time 95th percentile                        103 (OK=103    KO=13    )
> mean requests/sec                                123.468 (OK=123.466 KO=0.002 )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                        237033 (100%)
> 800 ms < t < 1200 ms                                  14 (  0%)
> t > 1200 ms                                            8 (  0%)
> failed                                                 3 (  0%)
---- Errors --------------------------------------------------------------------
   *
   */

   setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
     reachRps(200) in (120 seconds),
     holdFor(15 minute),
     jumpToRps(50),
     holdFor(15 minute)
   )

   /*
//failed
   //scenario-3(Passing with 7K rpm )
   setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
     reachRps(50) in (120 seconds),
     holdFor(3 minute),
     jumpToRps(200),
     holdFor(10 minute),
     reachRps(500) in(180 seconds),
     holdFor(10 minute)

   )*/

}
