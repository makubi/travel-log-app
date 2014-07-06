package at.droelf.travellogapp;

import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class NetworkClient {

    final RestTemplate restTemplate;

    final String apiImageUploadBase = "http://k2:9000/api/images/uploadImage";

    final String username = "admin";
    final String password = "1234";

    final HttpHeaders requestHeaders;

    public NetworkClient() {
        HttpAuthentication authHeader = new HttpBasicAuthentication("admin", "1234");

        requestHeaders = new HttpHeaders();
        requestHeaders.setAuthorization(authHeader);


        restTemplate = new RestTemplate();

        List<HttpMessageConverter<?>> add = restTemplate.getMessageConverters();
        add.add(new ResourceHttpMessageConverter());
        add.add(new StringHttpMessageConverter());
        add.add(new FormHttpMessageConverter());
    }

    void uploadFile(UploadImage uploadImage) {
        final String dateString = getDateTimeAsStringForImageUpload(uploadImage.dateTime);
        final String url = apiImageUploadBase + "/" + dateString + "/" + uploadImage.name;
        final String imagePath = uploadImage.imagePath;

        final MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();
        parts.add("file", new FileSystemResource(imagePath));

        final HttpEntity<?> requestEntity = new HttpEntity<Object>(parts, requestHeaders);

        restTemplate.exchange(url, HttpMethod.PUT, requestEntity, Void.class);
    }

    private String getDateTimeAsStringForImageUpload(DateTime dateTime) {
        return DateTimeUtils.dateTimeToIsoString(dateTime);
    }
}
