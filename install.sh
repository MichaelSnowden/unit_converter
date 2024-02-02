#!/usr/bin/env bash
set -e
git clone https://github.com/MichaelSnowden/unit_converter
cd unit_converter
mvn generate-sources
mvn clean compile assembly:single
echo "#!/usr/bin/env bash" >> unit_converter.sh
echo "java -jar $(pwd)/target/unit_converter.jar \$1" >> unit_converter.sh
sudo chmod 777 unit_converter.sh
sudo cp unit_converter.sh /usr/local/bin/unit_converter
echo "Installation complete!"
echo "Open the REPL with this command:"
echo "    unit_converter"
