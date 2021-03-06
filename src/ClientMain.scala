
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import scala.concurrent.duration._
import scala.util.Random.nextInt

object ClientMain extends App {

  
  val config = new Configuration;
  val system = ActorSystem("ClientMaster");
  val master = system.actorOf(Props[ClientActor], name = "clientactor");
  val remote = system.actorFor("akka.tcp://twitterserver@" + config.ipaddress +":5150/user/serverendpoint")
  //remote ! AddTweet(2, new Tweet("abc1234" + 2, "this is tweet from user " + 2, 2, null, null, null, "tweet"))
  //val numberOfActors = 20;
  //master ! addTweet;
  //master ! fetchTweet;
  val numberOfActors = 20

//spawn only twice as many client actors as many processors in ur system
for(i <- 1 until numberOfActors){
  
  // assign clients to HF
  if(i>=0 && i<=config.HFuser * numberOfActors ){
  val  myactor = system.actorOf(Props[ClientActor])
  myactor ! behaviour("HF",50, config, system); 
  }
  
  // assign clients to MF
  if(i>config.HFuser * numberOfActors && i<=(config.HFuser * numberOfActors + config.MFuser  * numberOfActors) ){
  val  myactor = system.actorOf(Props[ClientActor])
  myactor ! behaviour("MF",50, config , system); 
  }
  
  // assign clients to LF
  if(i>(config.HFuser * numberOfActors + config.MFuser  * numberOfActors) && i<=(config.MFuser * numberOfActors + config.LFuser  * numberOfActors) ){
  val  myactor = system.actorOf(Props[ClientActor])
  myactor ! behaviour("LF",50, config , system); 
  }
 
  
}

  
}

class ClientActor extends Actor {
    //Use system's dispatcher as ExecutionContext

  def receive = {
    
    case FetUpdatesResponse(tweets) => {
      //println("got something" + tweets.size)
    }
    
    case behaviour(msgtype, interval, config , system) =>{
      import system.dispatcher;
      //set this actors behaviour, and depending on dat behaviour send tweet messages(add and fetch)
      val remote = context.actorFor("akka.tcp://twitterserver@" + config.ipaddress +":5150/user/serverendpoint")
      var tweet: Tweet = null;
      var userID = 0;
      var recuradd = 0;
      var recurfetch = 0;
      if(msgtype == "HF"){
        userID = nextInt((config.HFuser * config.clientLoad).toInt );
        recuradd = interval*config.fetchHF;
        recurfetch = interval*config.addHF;
      }
      if(msgtype == "MF"){
        userID = nextInt((config.MFuser * config.clientLoad).toInt + 1 ) + (config.HFuser * config.clientLoad).toInt  ;
        recuradd = interval*config.fetchMF;
        recurfetch = interval*config.addMF;
      }
      if(msgtype == "LF"){
        userID = nextInt((config.LFuser * config.clientLoad).toInt + 1) + (config.HFuser * config.clientLoad).toInt + (config.MFuser * config.clientLoad).toInt;
        recuradd = interval*config.fetchLF;
        recurfetch = interval*config.addLF;
      }

         tweet = new Tweet("abc1234" + userID, "this is tweet from user " + userID, userID, null, null, null, "tweet");

         val cancellableAddTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 recuradd  milliseconds,
        				 remote,
        				 AddTweet(userID, tweet))
         
         val cancellableFetchTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 recurfetch  milliseconds,
        				 remote,
        				fetchUpdate(userID))
        				 
        //cancellableAddTweet .cancel()	
        //cancellableFetchTweet .cancel() 
    }
    case msg: String=>{
     print("got some unidentified msg")
    }
     
  }
  
}