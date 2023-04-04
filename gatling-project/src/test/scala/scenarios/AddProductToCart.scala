package scenarios

import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object AddProductToCart {

  val chairRand = csv("runTimeData/chairs.csv").random
  val tableRand = csv("runTimeData/tables.csv").random
  val chairsAll = csv("runTimeData/chairs.csv").circular


  val homePage =
    group("Home Page"){
      exec(api.generic.healthCheck)
        .exec(api.generic.boxesContent)
        .exec(api.products.productGroups())
        .exec(api.generic.pageContent())
        .exec(api.generic.category())
        .exec(api.generic.store())
        .feed(tableRand, 1)
        .exec(api.products.productPrice("${tableId}", "$${tablePrice}"))
        .repeat(3)(
          feed(chairsAll)
            .exec(api.products.productPrice("${chairId}", "$${chairPrice}"))
        )
        .exec(api.products.productPrice("${tableId}", "$${tablePrice}"))
        .repeat(3)(
          feed(chairsAll)
            .exec(api.products.productPrice("${chairId}", "$${chairPrice}"))
        )
    }

  val addTableToCart =
    group("Navigate to Tables Tab") {
      exec(api.generic.healthCheck)
        .exec(api.generic.boxesContent)
        .exec(api.products.productsAll())
        .exec(api.generic.store())
        .exec(api.generic.pageContent())
        .exec(api.generic.category())
        .exec(api.generic.category(catIdTables))
        .exec(api.products.productPrice("${tableId}", "$${tablePrice}"))
        .exec(api.generic.manufacturers())

    }

      .group("Click Table") {
        exec(api.generic.healthCheck)
          .exec(api.generic.boxesContent)
          .exec(api.products.productDetails("${tableId}", "${tableSku}"))
          .exec(api.products.productReview("${tableId}"))
          .exec(api.generic.store())
          .exec(api.generic.pageContent())
          .exec(api.generic.category())
          .exec(api.products.productPrice("${tableId}", "$${tablePrice}"))
      }

      .group("Add Table to Cart") {
        exec(api.cart.addToCart("${tableSku}", 1))
          .exec(api.cart.viewCart("${cartCode}", "${quantityActual}"))
      }

  val cartCode = "${cartCode}"

  val addChairToCart =
    group("Navigate to Chairs Tab") {
      exec(api.generic.healthCheck)
        .exec(api.generic.boxesContent)
        .exec(api.products.productsAll())
        .exec(api.generic.store())
        .exec(api.generic.category())
        .exec(api.generic.pageContent())
        .repeat(3)(
          feed(chairsAll)
            .exec(api.products.productPrice("${chairId}", "$${chairPrice}"))
        )
        .exec(api.generic.category(catIdTables))
        .exec(api.generic.manufacturers(catIdChairs))
        .exec(api.generic.variants(catIdChairs))
    }

      .group("Click Chair") {
        feed(chairRand, 1)
          .exec(api.generic.healthCheck)
          .exec(api.generic.boxesContent)
          .exec(api.products.productReview("${chairId}"))
          .exec(api.products.productDetails("${chairId}", "${chairSku}"))
          .exec(api.generic.store())
          .exec(api.generic.pageContent())
          .exec(api.generic.category())
          .exec(api.products.productPrice("${chairId}", "$${chairPrice}"))
      }

      .group("Add Chair to Cart") {
        exec(api.cart.updateCart(cartCode, "${chairSku}", "2"))
          .exec(api.cart.viewCart(cartCode, "${quantityActual}"))
      }


  val proceedToCheckout =
    doIf(session => session("quantityActual").as[Int]> 0) {
      group("View Cart") {
        exec(api.generic.healthCheck)
          .exec(api.generic.boxesContent)
          .repeat(2)(
            exec(api.cart.viewCart(cartCode, "${quantityActual}"))
          )
          .exec(api.generic.store())
          .exec(api.generic.pageContent())
          .exec(api.generic.category())
      }

        .group("Proceed to Cart") {
          exec(api.generic.healthCheck)
            .exec(api.generic.boxesContent)
            .exec(api.cart.viewShipping())
            .repeat(2)(
              exec(api.cart.viewCart(cartCode, "${quantityActual}"))
            )
            .exec(api.cart.getConfig())
            .exec(api.cart.getZones())
            .exec(api.cart.viewTotal(cartCode))
            .exec(api.generic.store())
            .exec(api.generic.pageContent())
            .exec(api.generic.category())
            .exec(api.cart.cartShipping(cartCode))
        }
    }

  val scnProceedToCheckout: ScenarioBuilder = scenario("Proceed to Checkout")
    .exec(flushHttpCache)
    .exec(flushCookieJar)
    .exitBlockOnFail(
      exec(homePage)
        .exec(ThinkTimer)
        .exec(addTableToCart)
        .exec(ThinkTimer)
        .randomSwitch(50.0 ->
          exec(addChairToCart)
            .exec(ThinkTimer))
        .randomSwitch(30.0 ->
          exec(proceedToCheckout)
            .exec(ThinkTimer))
    )
}
