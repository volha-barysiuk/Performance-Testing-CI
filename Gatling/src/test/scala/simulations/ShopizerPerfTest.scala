package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._

import scala.concurrent.duration.DurationInt


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(
         rampConcurrentUsers(10).to(20).during(1.minutes),
         constantConcurrentUsers(20).during(1.minutes)
      ).protocols(httpProtocol)
   )


}
