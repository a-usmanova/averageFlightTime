package service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDeserializer extends StdDeserializer<ZonedDateTime> {

        protected CustomDeserializer() {
            this(null);
        }

        protected CustomDeserializer(Class<?> vc) {
            super(vc);
        }

        @Override
        public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {

            String value = jsonParser.getText();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
            if (value.matches("[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\\ [0-9]\\:[0-9]{2}")) {
                formatter = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");
            }
            return LocalDateTime.parse(value, formatter).atZone(ZoneId.of("GMT"));

        }
}
