package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
         rampConcurrentUsers(40).to(1000).during(15.minutes)
      ).protocols(httpProtocol)
   )


}
