package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
         rampConcurrentUsers(System.getProperty("minUsers", "1").toInt).to(System.getProperty("maxUsers", "20").toInt).during(System.getProperty("rampTime", "1").toInt.minutes),
         constantConcurrentUsers(System.getProperty("maxUsers", "20").toInt).during(System.getProperty("constTime", "1").toInt.minutes)
      ).protocols(httpProtocol)
   )


}
