# Unit Converter

## Command-Line Installation
```
curl -s https://raw.githubusercontent.com/MichaelSnowden/unit_converter/master/install.sh | sh
```

## Command-Line Usage
```
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
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependency>
    <groupId>com.github.MichaelSnowden</groupId>
    <artifactId>unit_converter</artifactId>
    <version>4e0b67e</version>
</dependency>
```

Then start with the `Calculator` class.

```
Calculator calculator = new Calculator();
console.println(calculator.calculate(line).toString());
```

`com.michaelsnowden.unit_converter.REPL` is a good place to look for an example usage.

If you want to add units (I haven't added them all), then try constructing the `Calculator` with your own `UnitsProvider`.

Here is an example taken directly from a webiste I have which uses this repo.

```
Connection connection = null;
Connection connection = null;
try {
    connection = DatabaseUrl.extract().getConnection();
//                connection = DriverManager.getConnection("jdbc:sqlite:test.db");

    Statement stmt = connection.createStatement();
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS base (si TEXT, sym TEXT, scalar REAL)");
    stmt.executeUpdate("CREATE TABLE IF NOT EXISTS comp (deriv TEXT, base TEXT, n INT , d INT)");
    if (!stmt.executeQuery("SELECT * FROM base").next()) {
        stmt.executeUpdate("INSERT INTO base VALUES ('kg', 'g', " + 1 / 1000.0 + ")");
        stmt.executeUpdate("INSERT INTO base VALUES ('s', 'ms', " + 1 / 1000.0 + ")");
        stmt.executeUpdate("INSERT INTO base VALUES ('ml', 'l', " + 1 / 1000.0 + ")");
    }
    if (!stmt.executeQuery("SELECT * FROM comp").next()) {
        stmt.executeUpdate("INSERT INTO comp VALUES ('N', 'kg', 1, 1)");
        stmt.executeUpdate("INSERT INTO comp VALUES ('N', 'm', 1, 1)");
        stmt.executeUpdate("INSERT INTO comp VALUES ('N', 's', -2, 1)");
    }

    UnitsProvider unitsProvider = new UnitsProvider() {
        @Override
        public Map<String, Fraction> decompose(String s, Fraction fraction) {
            try {
                ResultSet resultSet;
                resultSet = stmt.executeQuery("SELECT * FROM comp WHERE deriv = '" + s + "'");
                if (resultSet.next()) {
                    Map<String, Fraction> decomposed = new HashMap<>();
                    resultSet = stmt.executeQuery("SELECT base, n, d FROM comp WHERE deriv = " +
                            "'" + s + "'");
                    while (resultSet.next()) {
                        String base = resultSet.getString("base");
                        int n = resultSet.getInt("n");
                        int d = resultSet.getInt("d");
                        decomposed.put(base, new Fraction(n, d));
                    }
                    return decomposed;
                } else {
                    return new HashMap<String, Fraction>() {{
                        put(s, fraction);
                    }};
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return new HashMap<String, Fraction>() {{
                    put(s, fraction);
                }};
            }
        }

        @Override
        public Double conversionFactor(String s, Fraction fraction) {
            try {
                ResultSet set;
                set = stmt.executeQuery("SELECT scalar FROM base WHERE sym = '" + s + "'");
                if (set.next()) {
                    return set.getDouble("scalar");
                } else {
                    return 1.0;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return 1.0;
            }
        }

        @Override
        public String conversionSymbol(String s) {
            try {
                ResultSet set;
                set = stmt.executeQuery("SELECT si FROM base WHERE sym = '" + s + "'");
                if (set.next()) {
                    return set.getString("si");
                } else {
                    return s;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return s;
            }
        }
    };

    String expression = req.queryParams("expression");
    Calculator calculator = new Calculator(unitsProvider);
    QualifiedNumber calculate = calculator.calculate(expression);
    return calculate.toString();
} catch (Exception ignored) {
    return ignored.getMessage();
} finally {
    if (connection != null) try {
        connection.close();
    } catch (SQLException ignored) {

    }
}
```

## REST API
You can also hit the REST API to compute results.

# Via cURL
```
$ curl https://michael-snowden.herokuapp.com/calculate?expression=2kg%20%2B%202g
2.002 kg
```

# Via jQuery (won't work on this page because of a security policy directive)
```
var expression = encodeURI("2kg * 3 N^2");
$.get("https://michael-snowden.herokuapp.com/calculate?expression=" + expression, function (data) {
    console.log(data);
});
```

## Future Ideas
API method for adding unit conversions
