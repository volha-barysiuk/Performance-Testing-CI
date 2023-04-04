package api

import config.BaseHelpers.shopizerBaseUrl
import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef._

object generic {

  def healthCheck: ChainBuilder = {
    exec(
      http("Health Check")
        .get(shopizerBaseUrl + "/actuator/health/ping")
    )
      .exec(
        http("Options: Health Check")
          .options(shopizerBaseUrl + "/actuator/health/ping")
      )
  }

  def category(cat: String = ""): ChainBuilder = {
    exec(
      http("Get Category Details")
        .get(shopizerBaseUrl + s"/api/v1/category/$cat?count=20&page=0&store=DEFAULT&lang=en")
    )
      .exec(
        http("Options: Category Details")
          .options(shopizerBaseUrl + s"/api/v1/category/$cat?count=20&page=0&store=DEFAULT&lang=en")
      )
  }

  def pageContent(): ChainBuilder = {
    exec(
      http("Get Page Content")
        .get(shopizerBaseUrl + "/api/v1/content/pages/?page=0&count=20&store=DEFAULT&lang=en")
    )
      .exec(
        http("Options: Page Content")
          .options(shopizerBaseUrl + "/api/v1/content/pages/?page=0&count=20&store=DEFAULT&lang=en")
      )
  }

  def boxesContent: ChainBuilder = {
    exec(
      http("Get Boxes Content")
        .get(shopizerBaseUrl + "/api/v1/content/boxes/headerMessage/?lang=en")
        .silent
    )
      .exec(
        http("Options: Boxes Content")
          .options(shopizerBaseUrl + "/api/v1/content/boxes/headerMessage/?lang=en")
      )
  }

  def store(): ChainBuilder = {
    exec(
      http("Get Store")
        .get(shopizerBaseUrl + "/api/v1/store/DEFAULT")
        .check(jsonPath("$.name").is("Default store"))
        .check(jsonPath("$.currency").is("CAD"))
    )
      .exec(
        http("Options: Store")
          .options(shopizerBaseUrl + "/api/v1/store/DEFAULT")
      )
  }

  def manufacturers(cat: String = "50"): ChainBuilder = {
    exec(
      http("Get Category Manufacturers")
        .get(shopizerBaseUrl + s"/api/v1/category/$cat/manufacturers/?store=DEFAULT&lang=en")
    )
      .exec(
        http("Options: Category Manufacturers")
          .options(shopizerBaseUrl + s"/api/v1/category/$cat/manufacturers/?store=DEFAULT&lang=en")
      )
  }

  def variants(cat: String = "50"): ChainBuilder = {
    exec(
      http("Get Category Variants")
        .get(shopizerBaseUrl + s"/api/v1/category/$cat/variants/?store=DEFAULT&lang=en")
        .silent
    )
      .exec(
        http("Options: Category Variants")
          .options(shopizerBaseUrl + s"/api/v1/category/$cat/variants/?store=DEFAULT&lang=en")
          .silent
      )
  }
}
