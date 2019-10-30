package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration._
import java.net.URLEncoder


class ThrottlingSimluationTenantWithId extends Simulation{
  val httpProtocol = http
    .baseUrl("") // Here is the root for all relative URLs
    .acceptHeader("application/json") // Here are the common headers
    .acceptLanguageHeader("en-US,en;q=0.5")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")
    .header("x-api-key","")
    .header("X-Request-ID","GatlingSimulation-"+System.currentTimeMillis() )
    .header("x-gw-ims-org-id","")
    .contentTypeHeader("application/json")
    .authorizationHeader("Bearer <token goes here>")

  val myIdentifiersFeeder = csv("src/test/resources/data/tenant-schema-id.csv").random.convert{
    case ("tenantSchemaId", string) => URLEncoder.encode(string)
  }

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .feed(myIdentifiersFeeder)
    .exec { session =>
      session
    }
    .exec(http("search tenant schema by id")
      .get("/tenant/schemas/${tenantSchemaId}"))


  setUp(scn.inject(constantUsersPerSec(150) during (15 minutes))).protocols(httpProtocol).throttle(
    reachRps(300) in (20 seconds),
    holdFor(15 minute),
    jumpToRps(50),
    holdFor(15 minute)
  )
}
