public class Main {
	public static void main(String[] args)
	{
		HTTPRequest request1 = HTTPRequest.getBuilder().withURL("spider.com").withMethod("POST").withHeader("content-type","application/json").withBody("{'id':1, 'name':'Nikhil Saini'}")
		                       .withTimeout(5).withQueryParameters("ICPC","WORLD Finalist").withQueryParameters("Meta Hacker Cup","Rank 1894")
		                       .build();
		request1.execute();

		System.out.println("===============================================================================================================================");

		HTTPRequest request2 = HTTPRequest.getBuilder().withURL("ycombinator.com").withMethod("POST").withHeader("content-type","application/json").withBody("{'id':1, 'name':'Nikhil Saini'}")
		                       .withTimeout(5).build();
		request2.execute();
	}
}
