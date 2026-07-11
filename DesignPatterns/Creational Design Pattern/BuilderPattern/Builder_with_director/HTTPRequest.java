package builderWithDirector;

import java.util.*;

public class HTTPRequest{
    private String url;
    private String method;
    private Map<String,String> headers;
    private Map<String,String> queryParameters;
    private String body;
    private int timeout;
    
    private HTTPRequest(){
        url = "";
        method = "";
        headers = new HashMap<>();
        queryParameters = new HashMap<>();
        body = "";
        timeout = -1;
    }
    
    static class HTTPRequestBuilder
    {
        private HTTPRequest req;
        
        public HTTPRequestBuilder(){
            req = new HTTPRequest();
        }
        public HTTPRequestBuilder withURL(String url)
        {
            req.url = url;
            return this;
        }
        
        public HTTPRequestBuilder withMethod(String method)
        {
            req.method = method;
            return this;
        }
        
        public HTTPRequestBuilder withHeader(String key, String value)
        {
            req.headers.put(key,value);
            return this;
        }
        
        public HTTPRequestBuilder withQueryParameters(String key, String value)
        {
            req.queryParameters.put(key,value);
            return this;
        }
        
        public HTTPRequestBuilder withBody(String body)
        {
            req.body = body;
            return this;
        }
        
        public HTTPRequestBuilder withTimeout(int timeout)
        {
            req.timeout = timeout;
            return this;
        }
        
        public HTTPRequest build()
        {
            // Do Validation Logic here if any
            if(req.url != null && req.url.length()==0)
            {
                throw new RuntimeException("URL cannot be empty");
            }
            
            if(req.method.length()==0)
            {
                throw new RuntimeException("URL cannot be empty");
            }
            
            if(req.timeout == -1)
            {
                throw new RuntimeException("URL cannot be empty");
            }
            
            // If all validation passes, then finally return the formed object
            return req;
        }
    }
    
    public void execute()
    {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParameters.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParameters.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }
        
        if (!headers.isEmpty()) {
        System.out.println("Headers:");
        for (Map.Entry<String, String> header : headers.entrySet()) {
            System.out.println("  " + header.getKey() + ": " + header.getValue());
         }
        }
        
        if (body != null && !body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }
}

class HTTPRequestDirector{
    static HTTPRequest createGetRequest(String url, int timeout){
        return new HTTPRequest.HTTPRequestBuilder().withURL(url).withMethod("GET").withTimeout(timeout).build();
    }
    
    static HTTPRequest createJsonPostRequest(String url, String jsonBody, int timeout)
    {
        return new HTTPRequest.HTTPRequestBuilder().withURL(url).withMethod("POST").withTimeout(timeout).withHeader("content-type","application/json").withHeader("accept","application/json").withBody(jsonBody).build();
    }
}

// ==================================================================================================================================================================================================
// IF IMMUTABILITY IS TO BE ENSURED

import java.util.*;

public class HTTPRequest {
    // 1. All fields are final to guarantee immutability
    private final String url;
    private final String method;
    private final Map<String, String> headers;
    private final Map<String, String> queryParameters;
    private final String body;
    private final int timeout;
    
    // 2. Private constructor maps the builder's state to the object
    private HTTPRequest(HTTPRequestBuilder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.body = builder.body;
        this.timeout = builder.timeout;
        // Make the maps unmodifiable so they can't be changed after creation
        this.headers = Collections.unmodifiableMap(new HashMap<>(builder.headers));
        this.queryParameters = Collections.unmodifiableMap(new HashMap<>(builder.queryParameters));
    }
    
    // 3. Builder class holds its OWN state
    public static class HTTPRequestBuilder {
        private String url;
        private String method;
        private Map<String, String> headers = new HashMap<>();
        private Map<String, String> queryParameters = new HashMap<>();
        private String body = "";
        private int timeout = -1;
        
        public HTTPRequestBuilder withURL(String url) {
            this.url = url;
            return this;
        }
        
        public HTTPRequestBuilder withMethod(String method) {
            this.method = method;
            return this;
        }
        
        public HTTPRequestBuilder withHeader(String key, String value) {
            this.headers.put(key, value);
            return this;
        }
        
        public HTTPRequestBuilder withQueryParameter(String key, String value) {
            this.queryParameters.put(key, value);
            return this;
        }
        
        public HTTPRequestBuilder withBody(String body) {
            this.body = body;
            return this;
        }
        
        public HTTPRequestBuilder withTimeout(int timeout) {
            this.timeout = timeout;
            return this;
        }
        
        public HTTPRequest build() {
            // 4. Fixed Validation Logic
            if (url == null || url.trim().isEmpty()) {
                throw new IllegalStateException("URL cannot be empty or null");
            }
            if (method == null || method.trim().isEmpty()) {
                throw new IllegalStateException("Method cannot be empty or null");
            }
            if (timeout <= 0) {
                throw new IllegalStateException("Timeout must be greater than 0");
            }
            
            // 5. Create the immutable object
            return new HTTPRequest(this);
        }
    }
    
    public void execute() {
        System.out.println("Executing " + method + " request to " + url);

        if (!queryParameters.isEmpty()) {
            System.out.println("Query Parameters:");
            for (Map.Entry<String, String> param : queryParameters.entrySet()) {
                System.out.println("  " + param.getKey() + "=" + param.getValue());
            }
        }
        
        if (!headers.isEmpty()) {
            System.out.println("Headers:");
            for (Map.Entry<String, String> header : headers.entrySet()) {
                System.out.println("  " + header.getKey() + ": " + header.getValue());
            }
        }
        
        if (body != null && !body.isEmpty()) {
            System.out.println("Body: " + body);
        }

        System.out.println("Timeout: " + timeout + " seconds");
        System.out.println("Request executed successfully!");
    }
}

class Main {
    public static void main(String[] args) {
        HTTPRequest request = new HTTPRequest.HTTPRequestBuilder()
            .withURL("https://api.hello.com")
            .withMethod("POST")
            .withHeader("Content-Type", "application/json")
            .withQueryParameter("user", "123")
            .withBody("{ \"message\": \"hello world\" }")
            .withTimeout(20)
            .build();
            
        request.execute();
    }
}
