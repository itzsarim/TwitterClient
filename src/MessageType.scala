

import akka.actor.ActorRef
import akka.actor.ActorSystem

sealed trait TwitterServerMessages

case class addTweet() extends TwitterServerMessages
case class fetchTweet() extends TwitterServerMessages
case class behaviour(userType : String, rateTweet : Int, config : Configuration , system : ActorSystem) extends TwitterServerMessages
//client communications
case class AddTweet(userId: Int, tweet: Tweet) extends TwitterServerMessages
case class fetchUpdate(userId: Int) extends TwitterServerMessages