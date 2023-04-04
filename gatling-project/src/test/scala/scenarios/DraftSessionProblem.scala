package scenarios

import config.BaseHelpers._
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object DraftSessionProblem {

  val chairRand = csv("runTimeData/chairs.csv").random
  val tableRand = csv("runTimeData/tables.csv").random
  val chairsAll = csv("runTimeData/chairs.csv").circular


  def scnDraft1: ScenarioBuilder = {
    scenario("Home")
      .group("Home") {
        exec(api.generic.healthCheck)
          .exec(api.generic.category())
          .exec(api.generic.store())
      }
  }

  def scnDraft2: ScenarioBuilder = {
    scenario("Table")
      .group("Tables") {
        exec(api.generic.healthCheck)
          .feed(this.tableRand, 1)
          .exec(api.generic.category(catIdTables))
          .exec(api.products.productPrice("${tableId}", "$${tablePrice}"))
      }

      .group("Click") {
        exec(api.products.productDetails("${tableId}", "${tableSku}"))
          .exec(api.products.productReview("${tableId}"))
          .exec(api.products.productPrice("${tableId}", "$${tablePrice}"))
      }

      .group("Add to Cart") {
        exec(api.cart.addToCart("${tableSku}", 1))
          .exec(api.cart.viewCart("${cartCode}", "${quantityActual}"))
          //.exec{session =>session.set("cartCode", "#{cartCode}")}
          //.exec(_.set("cartCode", "OlgaTest"))
          //.exec(_.set("cartCode", "#{cartCode}"))
      }
  }

  def scnDraft3: ScenarioBuilder = {
    scenario("Chair")
      .group("Chairs") {
        exec(api.products.productsAll())
          .repeat(3)(
            feed(this.chairsAll)
              .exec(api.products.productPrice("${chairId}", "$${chairPrice}"))
          )
      }

      .group("Add to Cart") {
        feed(this.chairRand)
        .exec(api.cart.updateCart("#{cartCode}", "${chairSku}", "2"))
          .exec(api.cart.viewCart("#{cartCode}", "${quantityActual}"))
      }
  }

}
