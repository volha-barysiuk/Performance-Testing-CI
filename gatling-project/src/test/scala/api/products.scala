package api

import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object products {

  def productsAll(): ChainBuilder = {
    exec(
      http("Get all Products")
        .get(shopizerBaseUrl + "/api/v1/products/?&store=DEFAULT&lang=en&page=0&count=15&category=50")
    )
      .exec(
        http("Options: Get all Products")
          .options(shopizerBaseUrl + "/api/v1/products/?&store=DEFAULT&lang=en&page=0&count=15&category=50")
      )
  }

  def productGroups(): ChainBuilder = {
    exec(
      http("Get Products Groups")
        .get(shopizerBaseUrl + "/api/v1/products/group/FEATURED_ITEM?store=DEFAULT&lang=en")
        .check(jsonPath("$.products..price").count.is(productsCount))
    )
      .exec(
        http("Options: Get Products Groups")
          .options(shopizerBaseUrl + "/api/v1/products/group/FEATURED_ITEM?store=DEFAULT&lang=en")
      )
  }

    def productPrice(prodId: String = "1", originalPrice: String): ChainBuilder = {
      exec(
        http("Get Product Price")
          .post(shopizerBaseUrl + s"/api/v1/product/$prodId/price/")
          .body(StringBody("""{"options":[]}""")).asJson
          .check(jsonPath("$.originalPrice").is(s"$$$originalPrice"))
      )
        .exec(
          http("Get Product Price")
            .options(shopizerBaseUrl + s"/api/v1/product/$prodId/price/")
        )
  }

  def productDetails(prodId: String, sku: String): ChainBuilder = {
    exec(
      http("Get Product Details")
        .get(shopizerBaseUrl + s"/api/v1/products/$prodId?lang=en&store=DEFAULT")
        .check(jsonPath("$.sku").is(s"$sku"))
    )
      .exec(
      http("Options: Get Product Details")
        .options(shopizerBaseUrl + s"/api/v1/products/$prodId?lang=en&store=DEFAULT")
    )
  }

  def productReview(prodId: String = "1"): ChainBuilder = {
    exec(
      http("Get Product Reviews")
        .get(shopizerBaseUrl + s"/api/v1/products/$prodId/reviews?store=DEFAULT")
    )
      .exec(
        http("Options: Get Product Reviews")
          .options(shopizerBaseUrl + s"/api/v1/products/$prodId/reviews?store=DEFAULT")
      )
  }


}
