
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.Actor
import scala.concurrent.duration._
import scala.util.Random.nextInt

object ClientMain extends App {

  
  val config = new Configuration;
  val system = ActorSystem("ClientMaster");
  val master = system.actorOf(Props[ClientActor], name = "client actor");
  
  val numberOfActors = 20;
  //master ! addTweet;
  //master ! fetchTweet;
  


//spawn only twice as many client actors as many processors in ur system
for(i <- 1 until numberOfActors ){
  
  // assign clients to HF
  if(i>=0 && i<=config.HFuser * numberOfActors ){
  val  myactor = system.actorOf(Props[ClientActor])
  myactor ! behaviour("HF",500, config, system); 
  }
  
  // assign clients to MF
  if(i>config.HFuser * numberOfActors && i<=(config.HFuser * numberOfActors + config.MFuser  * numberOfActors) ){
  val  myactor = system.actorOf(Props[ClientActor])
  myactor ! behaviour("MF",500, config , system); 
  }
  
  // assign clients to LF
  if(i>(config.HFuser * numberOfActors + config.MFuser  * numberOfActors) && i<=(config.MFuser * numberOfActors + config.LFuser  * numberOfActors) ){
  val  myactor = system.actorOf(Props[ClientActor])
  myactor ! behaviour("LF",500, config , system); 
  }
 
  
}

  
}

class ClientActor extends Actor {
    //Use system's dispatcher as ExecutionContext

  def receive = {

    case behaviour(msgtype, interval, config , system) =>{
      import system.dispatcher;
      //set this actors behaviour, and depending on dat behaviour send tweet messages(add and fetch)
      var tweet: Tweet = null;
      if(msgtype == "HF"){
         var userID = nextInt((config.HFuser * config.clientLoad).toInt );
         tweet = new Tweet("abc1234" + userID, "this is tweet from user " + userID, userID, null, null, null, "tweet");
         val remote = context.actorFor("akka.tcp://twitterserver@" + config.ipaddress +":5150/user/serverendpoint")
         //remote ! AddTweet(userID, tweet)
         val cancellableAddTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 interval*config.fetchHF  milliseconds,
        				 remote,
        				 AddTweet(userID, tweet))
         
         val cancellableFetchTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 interval*config.addHF  milliseconds,
        				 remote,
        				fetchUpdate(userID))
        				 
        //cancellableAddTweet .cancel()	
        //cancellableFetchTweet .cancel() 
         
      }
      if(msgtype == "MF"){
        var userID = nextInt((config.MFuser * config.clientLoad).toInt + 1 ) + (config.HFuser * config.clientLoad).toInt  ;
         tweet = new Tweet("abc1234" + userID, "this is tweet from user " + userID, userID, null, null, null, "tweet");
         val remote = context.actorFor("akka.tcp://twitterserver@" + config.ipaddress +":5150/user/serverendpoint")
         //remote ! AddTweet(userID, tweet)
         val cancellableAddTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 interval*config.fetchMF  milliseconds,
        				 remote,
        				 AddTweet(userID, tweet))
         
         val cancellableFetchTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 interval*config.addMF  milliseconds,
        				 remote,
        				 fetchUpdate(userID))
        				 
        //cancellableAddTweet .cancel()	
        //cancellableFetchTweet .cancel() 
        
      }
      if(msgtype == "LF"){
        var userID = nextInt((config.LFuser * config.clientLoad).toInt + 1) + (config.HFuser * config.clientLoad).toInt + (config.MFuser * config.clientLoad).toInt;
         tweet = new Tweet("abc1234" + userID, "this is tweet from user " + userID, userID, null, null, null, "tweet");
         val remote = context.actorFor("akka.tcp://twitterserver@" + config.ipaddress +":5150/user/serverendpoint")
         //remote ! AddTweet(userID, tweet)
         val cancellableAddTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 interval*config.fetchLF  milliseconds,
        				 remote,
        				 AddTweet(userID, tweet))
         
         val cancellableFetchTweet =
        		 		 system.scheduler.schedule(0 milliseconds,
        				 interval*config.addLF  milliseconds,
        				 remote,
        				 fetchUpdate(userID))
        				 
        //cancellableAddTweet .cancel()	
        //cancellableFetchTweet .cancel() 
        
      }
      
      
    }
     
  }
  
}