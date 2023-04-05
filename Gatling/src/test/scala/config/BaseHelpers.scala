package config

import io.gatling.core.Predef._
import io.gatling.core.structure._
import io.gatling.http.Predef.http
import io.gatling.http.protocol.HttpProtocolBuilder


object BaseHelpers {
  val shopizerBaseUrl = "http://localhost:8080"
  val productsCount = 4
  val catIdTables = "50"
  val catIdChairs = "51"


  def ThinkTimer : ChainBuilder={
    pause(System.getProperty("MinPause", "2").toInt, System.getProperty("MaxPause", "5").toInt)
  }

  val httpProtocol: HttpProtocolBuilder = http
    .acceptHeader("*/*")
    .acceptEncodingHeader("gzip, deflate, br")
    .acceptLanguageHeader("en-US,en;q=0.9")
    .upgradeInsecureRequestsHeader("1")
    .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36")

}
