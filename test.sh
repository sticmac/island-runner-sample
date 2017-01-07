for i in 0{1..9} {10..12} {46..53}; do
	mvn clean compile exec:java -Dmap=map$i.json > /dev/null;
done
