package tv.migo.backend.device.inspector

import akka.actor.Actor
import spray.routing._
import spray.http._
import java.io._
import java.text.SimpleDateFormat
import java.util.{Date, Calendar}
import java.net.InetAddress
import java.util.Properties
import java.io.FileInputStream

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

  val config_file = if (System.getProperty("config") == null) "./config.properties" else System.getProperty("config")

  val prop = new Properties()
  prop.load(new FileInputStream(config_file))

  val dataDir = prop.getProperty("data.folder")
  new java.io.File(dataDir).mkdirs()

  val host = this.getHost()



  val user_agent = headerValueByName("user-agent")

  val myRoute = path("") {
    user_agent { d => complete {
        log_header("[" + DateTime.now + "] " + d)
        "[" + DateTime.now + "] " + d
      }
    }
  }

  private def logs = (new java.io.File(dataDir)).listFiles
  private def filesMatching(matcher: String ) =
    for (file <- logs; 
         if (  file.getName.endsWith(".dat") 
            && matcher.toString().compareTo(file.getName.substring(0,8)) >= 0 ) ) yield file
  

  def getHost(): String = InetAddress.getLocalHost().getHostName()

  def getNow(): String = {
    val today = Calendar.getInstance().getTime()
    getNowBy("yyyy",today)+getNowBy("MM",today)+getNowBy("dd",today)+getNowBy("hh",today)
  }

  def getNowBy(fm:String, today:Date): String = new SimpleDateFormat(fm).format( today )

  def log_header(ss: String) = {

    val today = Calendar.getInstance().getTime()
    val threeDayAgo = new Date(today.getTime - 3*24*60*60*1000)

    filesMatching( getNowBy("yyyy",threeDayAgo)
              + getNowBy("MM",threeDayAgo)
              + getNowBy("dd",threeDayAgo) )
              .foreach( _.delete() )
             //.foreach(d => println("DIR: " + d.toString) ) 

    val now  = this.getNow()
    val S    = this.writer(dataDir + "/" + now + "_" + host + ".dat")
    S.write(ss+"\n")
    S.flush()
    S.close()
  }

  def writer(file: String): BufferedWriter = {
    new BufferedWriter( new FileWriter(file, true) )
  }

}
