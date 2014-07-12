package at.droelf.travellogapp

import android.util.Log
import org.joda.time.{DateTimeZone, LocalDateTime}
import org.springframework.core.io.FileSystemResource
import org.springframework.http.converter.{HttpMessageConverter, FormHttpMessageConverter, StringHttpMessageConverter, ResourceHttpMessageConverter}
import org.springframework.http._
import org.springframework.util.{LinkedMultiValueMap, MultiValueMap}
import org.springframework.web.client.RestTemplate

class NetworkClient {

    val authHeader: HttpAuthentication = new HttpBasicAuthentication(username, password)
    val requestHeadersWithAuth = new HttpHeaders
    requestHeadersWithAuth.setAuthorization(authHeader)
    val restTemplate = new RestTemplate
    val messageConverters: java.util.List[HttpMessageConverter[_]] = restTemplate.getMessageConverters
    messageConverters.add(new ResourceHttpMessageConverter)
    messageConverters.add(new StringHttpMessageConverter)
    messageConverters.add(new FormHttpMessageConverter)

  private[travellogapp] def uploadImage(dateTime: LocalDateTime, timezone: String, name: String, imagePath: String): Boolean = {
    val url: String = apiImageUploadBase + "/" + getLocalDateTimeAsStringForImageUpload(dateTime) + timezone + "/" + name
    return putFileToUrlWithAuth(url, imagePath)
  }

  private[travellogapp] def uploadGpxData(activity: String, timezone: String, pathToFile: String): Boolean = {
    val url: String = apiGpxDataUpload + "/" + timezone + "/" + activity
    return putFileToUrlWithAuth(url, pathToFile)
  }

  private[travellogapp] def putFileToUrlWithAuth(url: String, filePath: String): Boolean = {
    val parts: MultiValueMap[String, AnyRef] = new LinkedMultiValueMap[String, AnyRef]
    parts.add(imageFileFormKey, new FileSystemResource(filePath))
    val requestEntity: HttpEntity[_] = new HttpEntity[AnyRef](parts, requestHeadersWithAuth)
    Log.d("Network", "Putting file " + filePath + " to " + url)
    val response: ResponseEntity[Void] = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, classOf[Void])
    return response.getStatusCode eq HttpStatus.OK
  }

  private def getTimeZoneForGpxDataUpload(dateTimeZone: DateTimeZone): String = {
    return ""
  }

  private def getLocalDateTimeAsStringForImageUpload(dateTime: LocalDateTime): String = {
    return DateTimeUtils.localDateTimeToIsoString(dateTime)
  }

  private final val apiImageUploadBase: String = "http://i8:9000/api/images/uploadImage"
  private final val imageFileFormKey: String = "file"
  private final val apiGpxDataUpload: String = "http://i8:9000/api/tracks/upload/"
  private final val username: String = "admin"
  private final val password: String = "1234"
}