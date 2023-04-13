package api

import config.BaseHelpers.httpProtocol.check
import config.BaseHelpers.shopizerBaseUrl
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object cart {

    def addToCart(sku: String = "table1", quantity: Integer): ChainBuilder = {
      exec(
        http("Add product to cart")
          .post(shopizerBaseUrl + "/api/v1/cart/?store=DEFAULT")
          .body(StringBody(s"""{"attributes":[],"product":"$sku","quantity":1}""")).asJson
          .check(jsonPath("$.code").saveAs("cartCode"))
          .check(regex(s"refSku.*$sku").exists)
          .check(jsonPath("$.quantity").is(s"$quantity"))
          .check(jsonPath("$.quantity").saveAs("quantityActual"))
        )
        .exec(
          http("Options: Add product to cart")
            .options(shopizerBaseUrl + "/api/v1/cart/?store=DEFAULT")
        )
    }

    def viewCart(code: String, quantity: String): ChainBuilder = {
    exec(
      http("View Cart")
        .get(shopizerBaseUrl + s"/api/v1/cart/$code?lang=en")
        .check(jsonPath("$.code").is(s"$code"))
        .check(jsonPath("$.quantity").is(s"$quantity"))
        //.check(jsonPath("$.displayTotal").is(s"$$$total"))
    )
      .exec(
        http("Options: View Cart")
          .options(shopizerBaseUrl + s"/api/v1/cart/$code?lang=en")
      )
  }

  def updateCart(code: String, sku: String, quantity: String): ChainBuilder = {
    exec(
      http("Add new product to Cart")
        .put(shopizerBaseUrl + s"/api/v1/cart/$code?lang=en")
        .body(StringBody(s"""{"attributes":[],"product":"$sku","quantity":1}""")).asJson
        .check(regex(s"refSku.*$sku").exists)
        .check(jsonPath("$.quantity").is(s"$quantity"))
        .check(jsonPath("$.quantity").saveAs("quantityActual"))
    )
      .exec(
        http("Options: Add product to Cart")
          .options(shopizerBaseUrl + s"/api/v1/cart/$code?lang=en")
      )
  }

  def viewTotal(code: String): ChainBuilder = {
    exec(
      http("View Cart Total Price")
        .get(shopizerBaseUrl + s"/api/v1/cart/$code/total/")
        .check(status.is(200))
        .check(jsonPath("$.total").exists)
    )
      .exec(
        http("Options: Total Price")
          .options(shopizerBaseUrl + s"/api/v1/cart/$code/total/")
      )
  }

  def viewShipping(): ChainBuilder = {
    exec(
      http("View Shipping Details")
        .get(shopizerBaseUrl + "/api/v1/shipping/country?store=DEFAULT&lang=en")
        .check(status.is(200))
        .check(jsonPath("$..zones").exists)
    )
      .exec(
        http("Options: Shipping Details")
          .options(shopizerBaseUrl + "/api/v1/shipping/country?store=DEFAULT&lang=en")
      )
  }

  def cartShipping(code: String): ChainBuilder = {
    exec(
      http("Get Cart Shipping")
        .get(shopizerBaseUrl + s"/api/v1/cart/$code/shipping")
        .silent
    )
      .exec(
        http("Options: Cart Shipping")
          .options(shopizerBaseUrl + s"/api/v1/cart/$code/shipping")
      )
  }

  def getZones(): ChainBuilder = {
    exec(
      http("Get Zones")
        .get(shopizerBaseUrl + "/api/v1/zones/?code=")
        .check(status.is(200))
        .check(bodyString.is("[]"))
    )
      .exec(
        http("Options: Zones")
          .options(shopizerBaseUrl + "/api/v1/zones/?code=")
      )
  }

  def getConfig(): ChainBuilder = {
    exec(
      http("Get Config")
        .get(shopizerBaseUrl + "/api/v1/config/")
        .check(status.is(200))
        .check(jsonPath("$.displayShipping").is("true"))
    )
      .exec(
        http("Options: Config")
          .options(shopizerBaseUrl + "/api/v1/config/")
      )
  }
}
