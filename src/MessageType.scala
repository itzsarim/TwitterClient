

import akka.actor.ActorRef
import akka.actor.ActorSystem

sealed trait TwitterServerMessages



case class behaviour(userType : String, rateTweet : Int, config : Configuration , system : ActorSystem) extends TwitterServerMessages
//client communications
case class AddTweet(userId: Int, tweet: Tweet) extends TwitterServerMessages
case class fetchUpdate(userId: Int) extends TwitterServerMessages
case class FetUpdatesResponse(tweets: List[Tweet]) extends TwitterServerMessages