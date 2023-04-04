package simulations

import config.BaseHelpers._
import io.gatling.core.Predef._
import scenarios.AddProductToCart._


class ShopizerPerfTest extends Simulation{


   setUp(
      scnProceedToCheckout.inject(atOnceUsers(System.getProperty("ShopizerUsers", "1").toInt))
    ).protocols(httpProtocol)


}