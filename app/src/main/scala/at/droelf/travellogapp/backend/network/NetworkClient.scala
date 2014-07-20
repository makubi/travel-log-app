package at.droelf.travellogapp.backend.network

import android.util.Log
import at.droelf.travellogapp.DateTimeUtils
import org.joda.time.LocalDateTime
import org.springframework.core.io.FileSystemResource
import org.springframework.http._
import org.springframework.http.converter.{FormHttpMessageConverter, HttpMessageConverter, ResourceHttpMessageConverter, StringHttpMessageConverter}
import org.springframework.util.{LinkedMultiValueMap, MultiValueMap}
import org.springframework.web.client.RestTemplate

class NetworkClient(serverBaseUrl: String) {

    val authHeader: HttpAuthentication = new HttpBasicAuthentication(username, password)
    val requestHeadersWithAuth = new HttpHeaders
    requestHeadersWithAuth.setAuthorization(authHeader)

    val restTemplate = new RestTemplate

    val messageConverters: java.util.List[HttpMessageConverter[_]] = restTemplate.getMessageConverters
    messageConverters.add(new ResourceHttpMessageConverter)
    messageConverters.add(new StringHttpMessageConverter)
    messageConverters.add(new FormHttpMessageConverter)

  private[travellogapp] def uploadImage(dateTime: LocalDateTime, timezone: String, name: String, imagePath: String) = {
    val url: String = apiImageUploadBase + "/" + getLocalDateTimeAsStringForImageUpload(dateTime) + timezone + "/" + name
    putFileToUrlWithAuth(url, imagePath)
  }

  private[travellogapp] def uploadGpxData(activity: String, timezone: String, pathToFile: String) = {
    val url: String = apiGpxDataUpload + "/" + timezone + "/" + activity
    putFileToUrlWithAuth(url, pathToFile)
  }

  private[travellogapp] def putFileToUrlWithAuth(url: String, filePath: String): HttpStatus = {
    val parts: MultiValueMap[String, AnyRef] = new LinkedMultiValueMap[String, AnyRef]
    parts.add(imageFileFormKey, new FileSystemResource(filePath))

    val requestEntity: HttpEntity[_] = new HttpEntity[AnyRef](parts, requestHeadersWithAuth)
    Log.d("Network", "Putting file " + filePath + " to " + url)

    val response: ResponseEntity[Void] = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, classOf[Void])
    return response.getStatusCode
  }

  private def getLocalDateTimeAsStringForImageUpload(dateTime: LocalDateTime): String = {
    return DateTimeUtils.localDateTimeToIsoString(dateTime)
  }

  private final val apiImageUploadBase: String = s"http://${serverBaseUrl}/api/images/uploadImage"
  private final val imageFileFormKey: String = "file"
  private final val apiGpxDataUpload: String = s"http://${serverBaseUrl}/api/tracks/upload/"
  private final val username: String = "admin"
  private final val password: String = "1234"
}