package computerdatabase

import org.apache.http.client.methods.HttpPost
import org.apache.http.impl.client.DefaultHttpClient

class AuthTokenFetcher {

  def fetchToken(): Unit = {
    val client = new DefaultHttpClient
    val authUrl = "";
    val post = new HttpPost(authUrl)
    val response = client.execute(post);
    println(response);
  }

}


