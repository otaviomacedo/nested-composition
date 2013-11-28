package otaviomacedo

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

case class User(name: String)
case class Address(zip: String)
case class LatLong(lat: Double, long: Double)
case class Weather(temperature: Double)

object Api {
  def getAddress: User => Future[Option[Address]] = _ => Future(Some(Address("1234567")))
  def getGeolocation: Address => Future[Option[LatLong]] = _ => Future(Some(LatLong(-23, -46)))
  def getCurrentWeather: LatLong => Future[Option[Weather]] = _ => Future(Some(Weather(31)))
}

