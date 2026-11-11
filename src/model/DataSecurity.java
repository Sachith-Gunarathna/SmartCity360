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
           return "Raw Stream";
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
            return wrappedStream.getSecurityStatus() + " + AES-256";
        }
    }
    
    public static class HmacAuthDecorator extends StreamDecorator{
        public HmacAuthDecorator(DataStream wrappedStream){
            super(wrappedStream);
        }

        @Override
        public String writeData(String rawPayload) {
            String data = wrappedStream.writeData(rawPayload);
            String sig = Integer.toHexString(data.hashCode()).toUpperCase();
            return "HMAC_SHA256(sig=)"+ sig + ")["+data+"]";
        }

        @Override
        public String getSecurityStatus() {
            return wrappedStream.getSecurityStatus() + " + HMAC";
        }
 
    }
    
    public static class GzipCompressionDecorder extends StreamDecorator{
        public GzipCompressionDecorder(DataStream wrappedStream){
            super(wrappedStream);
        }

        @Override
        public String writeData(String rawPayload) {
            String data = wrappedStream.writeData(rawPayload);
            return "GZIP_COMP(ratio=64%)["+ data +"]";
        }

        @Override
        public String getSecurityStatus() {
            return wrappedStream.getSecurityStatus() + " + GZIP";
        }
   
    }
}
