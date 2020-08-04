package io.bluemoon.authorizationserver.config.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class CustomOAuthExceptionSerializer extends StdSerializer<CustomOAuthException> {

    public CustomOAuthExceptionSerializer() {
        super(CustomOAuthException.class);
    }

    @Override
    public void serialize(CustomOAuthException value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("code4444", value.getHttpErrorCode());
        gen.writeBooleanField("status", false);
        gen.writeObjectField("data", null);
        gen.writeObjectField("errors", Arrays.asList(value.getOAuth2ErrorCode(), value.getMessage()));

        if (value.getAdditionalInformation() != null) {
            for (Map.Entry<String, String> entry : value.getAdditionalInformation().entrySet()) {
                String key = entry.getKey();
                String add = entry.getValue();
                gen.writeStringField(key, add);
            }
        }
        gen.writeEndObject();
    }
}
