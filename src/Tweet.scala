@serializable
class Tweet(
  var tweetID: String,
  var tweet: String,
  var userID: Int,
  var nextTweet: Tweet,
  var previousTweet: Tweet,
  var mentions: List[Int],
  var tweetType: String) {

  def printTweet() = {
    println("tweet ID is " + tweetID)
    println("this was tweeted by " + userID)
    println("tweet text is " + tweet)
    println("tweet type is " + tweetType)
    if (mentions != null) println("people mentioned in this tweet are " + mentions.mkString(" - "))
  }

}