

import akka.actor.ActorRef

sealed trait ClientMessage
case class addTweet() extends ClientMessage
case class fetchTweet() extends ClientMessage
case class behaviour(userType : String, rateTweet : Int, config : Configuration ) extends ClientMessage