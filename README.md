To run the server you need to install Maven command tool and add it into your bin path
cd ~/PriceComparatorProject
mvn install
mvn compile
mvn exec:java -Dexec.mainClass=com.pricecomparator.Server

OR you can just use terminal and run the shell script: ./run.sh

API's:
- shopping basket optimal sub-lists: GET http://localhost:8000/basket?ids=<id1>,<id2>,<id3>&date=<date>
date format required yyyy-mm-dd

Each API can be called either via a browser or Postman or cmd line utility(eg. wget)
Example of url:
http://localhost:8000/basket?ids=P001,P003&date=2025-05-01 --> Response: {"storeProducts":{"kaufland":[{"name":"iaurt grecesc","price":11.8,"currency":"RON"}],"lidl":[{"name":"lapte zuzu","price":8.909999,"currency":"RON"}]},"date":"2025-05-01","totalPrice":20.71,"currency":"RON"}