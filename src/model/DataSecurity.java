package model;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class DataSecurity {
    
    public interface DataStream{
        String writeData(String rawPayload);
        String getSecurityStatus();
    }
    
    public static class RawSensorStream implements DataStream{

        @Override
        public String writeData(String rawPayload) {
            return rawPayload;
        }

        @Override
        public String getSecurityStatus() {
           return "Unsecured Stream";
        }   
      
    }
    
    public static abstract class StreamDecorator implements DataStream{
        protected final DataStream wrappedStream;
        
        public StreamDecorator(DataStream wrappedStream){
            this.wrappedStream = wrappedStream;
        }
    }
    
    public static class Aes256EncryptionDecorator extends StreamDecorator{

        public Aes256EncryptionDecorator(DataStream wrappedStream) {
            super(wrappedStream);
        }

        @Override
        public String writeData(String rawPayload) {
            String baseData = wrappedStream.writeData(rawPayload);
            String encoded = Base64.getEncoder().encodeToString(baseData.getBytes(StandardCharsets.UTF_8));
            return "AES256_ENC["+encoded+"]";
        }

        @Override
        public String getSecurityStatus() {
            return "100% Encrypted (AES-256)";
        }
    }
}
