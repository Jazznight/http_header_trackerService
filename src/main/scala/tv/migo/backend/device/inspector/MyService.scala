package tv.migo.backend.device.inspector

import akka.actor.Actor
import spray.routing._
import spray.http._
import java.io._
import java.text.SimpleDateFormat
import java.util.Calendar
import java.net.InetAddress

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class MyServiceActor extends Actor with MyService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait MyService extends HttpService {

  //val S = new PrintWriter("/tmp/dump_test.txt")
  val runDir = "./run"
  new java.io.File(runDir).mkdirs()

  val host = this.getHost()



  val user_agent = headerValueByName("user-agent")

  val myRoute = path("") {
    user_agent { d => complete {
        log_header("[" + DateTime.now + "] " + d)
        "[" + DateTime.now + "] " + d
      }
    }
  }

  def getHost(): String = InetAddress.getLocalHost().getHostName()

  def getNow(): String = {
    val today = Calendar.getInstance().getTime()
    val yF = new SimpleDateFormat("yyyy").format(today)
    val MF = new SimpleDateFormat("MM").format(today)
    val dF = new SimpleDateFormat("dd").format(today)
    val hF = new SimpleDateFormat("hh").format(today)

    yF+MF+dF+hF
  }

  def log_header(ss: String) = {
    val now  = this.getNow()
    val S    = this.writer(runDir + "/" + host + "_" + now + ".dat")
    S.write(ss+"\n")
    S.flush()
    S.close()
  }

  def writer(file: String): BufferedWriter = {
    new BufferedWriter( new FileWriter(file, true) )
  }

}