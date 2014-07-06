package at.droelf.travellogapp;

import android.util.Log;

import org.joda.time.DateTimeZone;
import org.joda.time.LocalDateTime;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class NetworkClient {

    private final RestTemplate restTemplate;

    private final String apiImageUploadBase = "http://k2:9000/api/images/uploadImage";
    private final String imageFileFormKey = "file";

    private final String apiGpxDataUpload = "http://k2:9000/api/tracks/upload/";

    private final String username = "admin";
    private final String password = "1234";

    private final HttpHeaders requestHeadersWithAuth;

    public NetworkClient() {
        final HttpAuthentication authHeader = new HttpBasicAuthentication(username, password);

        requestHeadersWithAuth = new HttpHeaders();
        requestHeadersWithAuth.setAuthorization(authHeader);


        restTemplate = new RestTemplate();

        final List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        messageConverters.add(new ResourceHttpMessageConverter());
        messageConverters.add(new StringHttpMessageConverter());
        messageConverters.add(new FormHttpMessageConverter());
    }

    boolean uploadImage(LocalDateTime dateTime, String timezone, String name, String imagePath) {
        final String url = apiImageUploadBase + "/" + getLocalDateTimeAsStringForImageUpload(dateTime) + timezone + "/" + name;

        return putFileToUrlWithAuth(url, imagePath);
    }

    boolean uploadGpxData(String activity, String timezone, String pathToFile) {
        final String url = apiGpxDataUpload + "/" + timezone + "/" + activity;

        return putFileToUrlWithAuth(url, pathToFile);
    }

    boolean putFileToUrlWithAuth(String url, String filePath) {
        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add(imageFileFormKey, new FileSystemResource(filePath));

        final HttpEntity<?> requestEntity = new HttpEntity<Object>(parts, requestHeadersWithAuth);

        Log.d("Network", "Putting file " + filePath + " to " + url);

        ResponseEntity<Void> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
        return response.getStatusCode() == HttpStatus.OK;
    }

    private String getTimeZoneForGpxDataUpload(DateTimeZone dateTimeZone) {
        return "";
    }

    private String getLocalDateTimeAsStringForImageUpload(LocalDateTime dateTime) {
        return DateTimeUtils.localDateTimeToIsoString(dateTime);
    }
}
