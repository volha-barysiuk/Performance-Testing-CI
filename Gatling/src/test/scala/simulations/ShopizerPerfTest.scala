package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
         rampConcurrentUsers(1).to(10).during(1.minutes)
      ).protocols(httpProtocol)
   )


}
