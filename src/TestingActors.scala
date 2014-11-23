
import akka.actor._
import akka.pattern.{ after, ask, pipe }
import akka.util.Timeout
import scala.concurrent._
import scala.concurrent.Future
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits._
import scala.util.Success
import scala.util.Failure

sealed trait Msgs
case class Start() extends Msgs
case class StartClient(server: ActorRef) extends Msgs

object TestingActors extends App {
  val system = ActorSystem("loadtesting")
  val server = system.actorOf(Props[server])
  server ! Start()
  var numberOfClients = 500
  var client: ActorRef = null

  for (num <- 1 to 10) {
    client = system.actorOf(Props[client])
    client ! StartClient(server)
  }
}
class server extends Actor {
  var count: Int =0
  var time = System.currentTimeMillis()
  def receive  = {
    case msg: String => {
      
      count += 1
      //println("got one " + count)
      if(count % 1000 == 0){
        println("got 1000 " + " msg/sec " + count/((System.currentTimeMillis() - time)/1000))
      }
    }
    case start: Start => {
      println("starting server")
    }
  }
}


class client extends Actor {
  def receive = {
    case StartClient(server) => {
      
      while (1 == 1) {
        server ! " Hi"
      }
    } 
    case msg: String => {
      
    }
  }
}