package tv.migo.backend.device.inspector

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import spray.can.Http
import java.util.Properties
import java.io.FileInputStream

object Boot extends App {

  val config_file = if (System.getProperty("config") == null) "./config.properties" else System.getProperty("config")

  val prop = new Properties()
  prop.load(new FileInputStream(config_file) )

  // we need an ActorSystem to host our application in
  implicit val system = ActorSystem("on-spray-can")

  // create and start our service actor
  val service = system.actorOf(Props[MyServiceActor], "demo-service")

  // start a new HTTP server on port 8080 with our service actor as the handler
  IO(Http) ! Http.Bind(service, interface = prop.getProperty("server.listen"), port = prop.getProperty("server.port").toInt)
}
