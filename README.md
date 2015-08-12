# ConfluenceRESTClient

A Java client for interacting with the Confluence REST API.

### Example Usage:

```java
String url = "http://confluence.organisation.org";
String username = "Jono";
String password = "aPa$$w0rd";
 
ConfluenceClient client = ConfluenceClient.builder()
    .baseURL(url)
    .username(username)
    .password(password)
    .build();

// generate some report content to be presented on confluence.
String content = generateReport();

// configure page
Content page = new Content();
page.setType(Type.Page);
page.setSpace( new Space("DEV"));
page.setTitle("A page in DEV");                                      
page.setBody(new Body(new Storage(content, Storage.Representation.STORAGE)));

// post page to confluence.
client.postContent(page);

// search confluence instance by space key and title
// Note: this will contain our page from above
ContentResultList search 
        = confluenceClient.getContentBySpaceKeyAndTitle(
                "DEV", "A page in DEV");
```    

### Disclaimer

This library is currently still under active development, a MAVEN repository will be published when complete.
