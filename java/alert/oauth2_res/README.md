# testing 

```$xslt

cd oauth2 & mvn spring-boot:run

curl -u eagleeye:thisissecret -i -H 'Accept:application/json' -d "grant_
type=password&scope=webclient&username=will&password=pass" -H "Content-Type: app
lication/x-www-form-urlencoded" -X POST http://localhost:8080/oauth/token

cd oauth2_res & mvn spring-boot:run

curl -d "Authorization=bearer 7b905277-a0c6-41a3-bf0b-08a7d09b2517" http://localhost:8080/res
   
```