package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
        nothingFor(5),
        rampUsersPerSec(System.getProperty("rampupFrom", "1").toInt) to (System.getProperty("rampupTo", "20").toInt) during (System.getProperty("rampupDuration", "1").toInt minutes),
        constantUsersPerSec(System.getProperty("constantUsers", "20").toInt) during (System.getProperty("constantUsersDuration", "1").toInt minutes),
        rampUsersPerSec(System.getProperty("rampdownFrom", "20").toInt) to (System.getProperty("rampdownTo", "1").toInt) during (System.getProperty("rampdownDuration", "1").toInt minutes)
      ).protocols(httpProtocol)
   )


}
