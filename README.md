# Unit Converter

## Command-Line Installation
```bash
curl -s https://raw.githubusercontent.com/MichaelSnowden/unit_converter/master/install.sh | sh
```

## Command-Line Usage
```bash
$ unit_converter
uc> 400 kg * 9.8 m/s^2
3920 kg m s^-2
uc> 3.9 N * 10 m
39 kg m^2 s^-2
uc> quit
$
```

## Maven Integration
Add this to your `pom.xml`
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.MichaelSnowden</groupId>
        <artifactId>unit_converter</artifactId>
        <version>4e0b67e</version>
    </dependency>
</dependencies>
```

Then use the `Calculator` class. Here is an example taken directly from a webiste I have which uses this repo.

```java
get("/calculate", (req, res) -> {
    try {
        String expression = req.queryParams("expression");
        if (expression == null) {
            throw new IllegalArgumentException("Missing parameter 'expression'");
        }
        Calculator calculator = new Calculator();
        return calculator.calculate(expression);
    } catch (Exception e) {
        e.printStackTrace();
        return e.getMessage();
    }
});
```

## REST API (cURL)
```javascript
$ curl https://michael-snowden.herokuapp.com/calculate?expression=2kg%20%2B%202g
2.002 kg
```

## REST API (jQuery)
*This won't work on the GitHub page because of a security policy directive*
```javascript
var expression = encodeURI("2kg * 3 N^2");
$.get("https://michael-snowden.herokuapp.com/calculate?expression=" + expression, function (data) {
    console.log(data);
});
```
