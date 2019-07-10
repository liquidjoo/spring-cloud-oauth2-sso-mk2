package io.bluemoon.testservice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.bluemoon.testservice.domain.user.User;
import lombok.Getter;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

public class APIRequest {

    private static IRequestExecutor iRequestExecutor = new DefaultRequestExecutor();

    public static IRequestExecutor getIRequestExecutor() {
        return iRequestExecutor;
    }

    public interface IRequestExecutor {
        ResponseWrapper createOAuthUser(User user) throws IOException;
        ResponseWrapper updateOAuthUser(User user);

        ResponseWrapper createOAuthClientDetails();
        ResponseWrapper updateOAuthClientDetials();
    }

    public static class DefaultRequestExecutor implements IRequestExecutor {
        static okhttp3.OkHttpClient client = null;
        static void init() {
            client = new okhttp3.OkHttpClient();
        }
        static {
            init();
        }


        @Override
        public ResponseWrapper createOAuthUser(User user) throws IOException {
            String url = "http://localhost:8081/auth/createOAuthUser";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(user);

            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonString);

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .header("Content-type", "application/json")
                    .build();

            Call call = client.newCall(request);
            Response response = call.execute();
            ResponseWrapper result = new ResponseWrapper(response.body().string(), convertToString(response.headers()));

            return result;
        }

        @Override
        public ResponseWrapper updateOAuthUser(User user) {
            return null;
        }

        @Override
        public ResponseWrapper createOAuthClientDetails() {
            return null;
        }

        @Override
        public ResponseWrapper updateOAuthClientDetials() {
            return null;
        }
    }

    private static String convertToString(Object input) {
        if (input == null) {
            return "null";
        } else if (input instanceof Map) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .excludeFieldsWithModifiers(Modifier.PROTECTED)
                    .disableHtmlEscaping()
                    .create();
            return gson.toJson((Map)input);
        } else if (input instanceof List) {
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithModifiers(Modifier.STATIC)
                    .excludeFieldsWithModifiers(Modifier.PROTECTED)
                    .disableHtmlEscaping()
                    .create();
            return gson.toJson((List)input);
        } else {
            return input.toString();
        }
    }

    @Getter
    public static class ResponseWrapper {
        private String body;
        private String header;

        public ResponseWrapper(String body, String header) {
            this.body = body;
            this.header = header;
        }
    }

}
