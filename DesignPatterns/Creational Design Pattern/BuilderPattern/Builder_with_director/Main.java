package builderWithDirector;
  
public class Main{
    public static void main(String[] args)
    {
        HTTPRequest request = new HTTPRequest.HTTPRequestBuilder().withURL("hello.com").withTimeout(20).withBody("hello world").withMethod("POST").build();
        request.execute();
        System.out.println("======================================================================================");
        HTTPRequest request2 = HTTPRequestDirector.createGetRequest("PIBindia.com",5);
        request2.execute();
        System.out.println("======================================================================================");
        HTTPRequest request3 = HTTPRequestDirector.createJsonPostRequest("ycombinator.com", "{'id':111, 'course':'Intro to ML'}", 5);
        request3.execute();
    }
}
