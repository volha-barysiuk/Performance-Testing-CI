package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
        nothingFor(5),
        rampUsers(System.getProperty("rampupUsers1", "20").toInt).during(System.getProperty("rampupDuration", "3").toInt minutes),
        rampUsers(System.getProperty("rampupUsers2", "40").toInt).during(System.getProperty("rampupDuration", "3").toInt minutes),
        rampUsers(System.getProperty("rampupUsers3", "50").toInt).during(System.getProperty("rampupDuration", "1").toInt minutes)
      ).protocols(httpProtocol)
   )


}
