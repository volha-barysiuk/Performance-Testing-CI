package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
         rampConcurrentUsers(5).to(100).during(30.minutes)
      ).protocols(httpProtocol)
   )


}
