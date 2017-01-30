mvn clean compile
echo "Executing with 15-16 year map"
for i in `ls src/main/resources/15/*.json` ; do
	j=`basename $i`;
	mvn exec:java -Dmap=15/$j | grep "Report: ";
done
echo "-/*/-/*/-/*/-/*/-/*/-/*/-/*/-"
echo "Executing with 16-17 year map"
for i in `ls src/main/resources/16/*.json` ; do
	j=`basename $i`;
	mvn exec:java -Dmap=16/$j | grep "Report: ";
done

