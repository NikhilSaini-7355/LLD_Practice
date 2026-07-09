interface IReports{
    String getJsonData(String s);
}

class XmlDataProvider{
    String getXmlData(String s)
    {
        return "XML - [ " + s + " ]";
    }
}

class XmlDataProviderAdapter implements IReports{
    XmlDataProvider xml;
    public XmlDataProviderAdapter(XmlDataProvider xml)
    {
        this.xml = xml;
    }
    public String getJsonData(String s)
    {
        String xmldata = this.xml.getXmlData(s);
        String jsondata = "JSON - [ " + xmldata.substring(8,xmldata.length()-1) + " ]";
        return jsondata;
    }
}

class Client{
    public String getJsonReport(IReports report, String data)
    {
        String jsonReport = report.getJsonData(data);
        return jsonReport;
    }
}
public class Main{
    public static void main(String[] args){
    XmlDataProvider xml = new XmlDataProvider();
    IReports xmlToJsonReport = new XmlDataProviderAdapter(xml);
    Client client = new Client();
    String jsonReport = client.getJsonReport(xmlToJsonReport,"Hello World!");
    System.out.println(jsonReport);
}
}
