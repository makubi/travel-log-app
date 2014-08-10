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

  private final val apiImageUploadBase: String = s"http://${serverBaseUrl}/api/images/uploadImage"
  private final val imageFileFormKey: String = "file"
  private final val imageDateTimeFormKey: String = "dateTime"
  private final val imageNameFormKey: String = "name"

  private final val apiGpxDataUpload: String = s"http://${serverBaseUrl}/api/tracks/upload/"
  private final val trackTimeZoneFormKey: String = "timeZone"
  private final val trackActivityFormKey: String = "activity"

  private final val username: String = "admin"
  private final val password: String = "1234"

  val authHeader: HttpAuthentication = new HttpBasicAuthentication(username, password)
  val requestHeadersWithAuth = new HttpHeaders
  requestHeadersWithAuth.setAuthorization(authHeader)

  val restTemplate = new RestTemplate

  val messageConverters: java.util.List[HttpMessageConverter[_]] = restTemplate.getMessageConverters
  messageConverters.add(new ResourceHttpMessageConverter)
  messageConverters.add(new StringHttpMessageConverter)
  messageConverters.add(new FormHttpMessageConverter)

  private[travellogapp] def uploadImage(dateTime: LocalDateTime, timezone: String, name: String, imagePath: String) = {
    val parts: MultiValueMap[String, AnyRef] = new LinkedMultiValueMap[String, AnyRef]
    val url: String = apiImageUploadBase

    parts.add(imageDateTimeFormKey, getLocalDateTimeAsStringForImageUpload(dateTime)+timezone)
    parts.add(imageNameFormKey, name)

    putFileToUrlWithAuth(url, parts, imagePath)
  }

  private[travellogapp] def uploadGpxData(activity: String, timezone: String, pathToFile: String) = {
    val parts: MultiValueMap[String, AnyRef] = new LinkedMultiValueMap[String, AnyRef]
    val url: String = apiGpxDataUpload + "/" + timezone + "/" + activity

    parts.add(trackActivityFormKey, activity)
    parts.add(trackTimeZoneFormKey, trackTimeZoneFormKey)

    putFileToUrlWithAuth(url, parts, pathToFile)
  }

  private[travellogapp] def putFileToUrlWithAuth(url: String, parts: MultiValueMap[String,AnyRef], filePath: String): HttpStatus = {
    parts.add(imageFileFormKey, new FileSystemResource(filePath))

    val requestEntity: HttpEntity[_] = new HttpEntity[AnyRef](parts, requestHeadersWithAuth)
    Log.d("Network", "Putting file " + filePath + " to " + url)

    val response: ResponseEntity[Void] = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, classOf[Void])
    return response.getStatusCode
  }

  private def getLocalDateTimeAsStringForImageUpload(dateTime: LocalDateTime): String = {
    return DateTimeUtils.localDateTimeToIsoString(dateTime)
  }


}