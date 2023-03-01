FROM openjdk:17
EXPOSE 8081
ADD target/ecommerce-shop.jar ecommerce-shop.jar
ENTRYPOINT ["java","-jar","/ecommerce-shop"]