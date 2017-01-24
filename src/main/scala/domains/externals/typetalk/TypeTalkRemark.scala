package domains.externals.typetalk

/**
 * Empty constructor needed by jackson (for AWS Lambda)
 *
 * @return
 */
case class TypeTalkRemark(
  topic: Topic,
  post: Post
)

case class Topic(
  id: Int,
  name: String,
  suggestion: String,
  createdAt: String,
  updatedAt: String
)

case class Post(
  id: Int,
  topicId: Int,
  replyTo: String,
  message: String,
  account: Account,
  mention: String,
  attachments: java.util.ArrayList[String],
  likes: java.util.ArrayList[String],
  talks: java.util.ArrayList[String],
  createdAt: String,
  updatedAt: String
)

case class Account(
  id: Int,
  name: String,
  fullName: String,
  suggestion: String,
  imageUrl: String,
  createdAt: String,
  updatedAt: String
)

/*
{
  "topic": {
    "id": 208,
    "name": "IT Peeps",
    "suggestion": "IT Peeps",
    "lastPostedAt": "2014-12-10T09:00:29Z",
    "createdAt": "2014-06-10T02:32:29Z",
    "updatedAt": "2014-06-10T02:32:29Z"
  },
  "post": {
    "id": 333,
    "topicId": 208,
    "replyTo": null,
    "message": "Let's party like it's 1999!",
    "account": {
      "id": 100,
      "name": "jessica",
      "fullName": "Jessica Fitzherbert",
      "suggestion": "Jessica Fitzherbert",
      "imageUrl": "https://typetalk.in/accounts/100/profile_image.png?t=1403577149000",
      "createdAt": "2014-06-24T02:32:29Z",
      "updatedAt": "2014-06-24T02:32:29Z"
    },
    "mention": null,
    "attachments": [],
    "likes": [],
    "talks": [],
    "links": [],
    "createdAt": "2014-12-10T09:00:29Z",
    "updatedAt": "2014-12-10T09:00:29Z"
  }
}
*/
