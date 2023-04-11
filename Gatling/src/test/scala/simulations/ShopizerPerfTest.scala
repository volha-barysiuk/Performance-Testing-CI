package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
         rampConcurrentUsers(50).to(350).during(30.minutes)
      ).protocols(httpProtocol)
   )


}
