package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import org.apache.http._
import org.apache.http.client._
import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  var response ="";

  val httpProtocol = http
    .baseUrl("") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .header("x-api-key","ess-test-3")
    .header("x-gw-ims-org-id","")
    .contentTypeHeader("application/json")
//    .authorizationHeader(s"Bearer $response")
    .authorizationHeader("Bearer <token>")


  val authRequestScenario = scenario("auth")
    .exec(http("Auth Request")
    .post("<auth url>")
//    .body(ElFileBody("payload.json"))
    .check(jsonPath("$..access_token").saveAs("Auth_Response"))
    .check(status is 200))
    .exec(session => {
       response = session("Auth_Response").as[String]
      println(s"Response body: $response")
      session
    })
    .pause(5 seconds)


  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("global schema list")
      .get("/global/schemas"))
    .exec(http("tenant schema list")
      .get("/tenant/schemas"))

//  setUp(scn.inject(constantUsersPerSec(100) during (15 minutes))).protocols(httpProtocol)

/*
  ================================================================================
  ---- Global Information --------------------------------------------------------
  > request count                                     237058 (OK=237049 KO=9     )
  > min response time                                      9 (OK=20     KO=9     )
  > max response time                                   7346 (OK=7346   KO=17    )
  > mean response time                                    44 (OK=44     KO=14    )
  > std deviation                                         41 (OK=41     KO=2     )
  > response time 50th percentile                         36 (OK=36     KO=14    )
  > response time 75th percentile                         47 (OK=47     KO=15    )
  > response time 90th percentile                         69 (OK=69     KO=16    )
  > response time 95th percentile                         95 (OK=95     KO=17    )
  > mean requests/sec                                123.468 (OK=123.463 KO=0.005 )
  ---- Response Time Distribution ------------------------------------------------
  > t < 800 ms                                        237024 (100%)
  > 800 ms < t < 1200 ms                                  15 (  0%)
  > t > 1200 ms                                           10 (  0%)
  > failed                                                 9 (  0%)
  ---- Errors --------------------------------------------------------------------
  > status.find.in(200,201,202,203,204,205,206,207,208,209,304), f      9 (100.0%)
  ound 504
  basicsimulation-20190826172256387
  ================================================================================

  */
  setUp(
    authRequestScenario.inject(constantConcurrentUsers(1) during(5 seconds))
//    ,
//
//
//    scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
//    reachRps(200) in (120 seconds),
//    holdFor(15 minute),
//    jumpToRps(50),
//    holdFor(15 minute)

  )


}
