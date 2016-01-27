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

Then start with the `Calculator` class.

```java
Calculator calculator = new Calculator();
console.println(calculator.calculate(line).toString());
```

[`com.michaelsnowden.unit_converter.REPL`](https://github.com/MichaelSnowden/unit_converter/blob/master/src/main/java/com/michaelsnowden/unit_converter/REPL.java) is a good place to look for an example usage.

If you want to add units (I haven't added them all), then try constructing the `Calculator` with your own `UnitsProvider`.

Here is an example taken directly from a webiste I have which uses this repo.

```java
get("/calculate", (req, res) -> {
    Connection connection = null;
    try {
        connection = getConnection();
        Statement statement = connection.createStatement();
        SymbolLookup symbolLookup = getSymbolLookup(statement);
        String expression = req.queryParams("expression");
        if (expression == null) {
            return "Missing parameter 'expression'";
        }
        Calculator calculator = new Calculator(symbolLookup);
        return calculator.calculate(expression);
    } catch (SQLException e) {
        e.printStackTrace();
        return "An error occurred: " + e.getMessage();
    }
});

private static SymbolLookup getSymbolLookup(final Statement statement) throws SQLException, IOException {
    return new AbstractSymbolLookup() {
        {
            ResultSet resultSet = statement.executeQuery("SELECT * FROM map");
            while (resultSet.next()) {
                String key = resultSet.getString("key");
                String value = resultSet.getString("value");
                Calculator calculator = new Calculator(this);
                Term term = calculator.calculate(value);
                if (!map.containsKey(key)) {
                    map.put(key, term);
                } else {
                    map.put(key, map.get(key).times(term));
                }
            }
        }
    };
}
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

## Future Ideas
API method for adding unit conversions
