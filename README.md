<h1>Simple internet-market</h1>

[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8c0bd51bdba44e04bd2cbbfd7f643e9f)](https://www.codacy.com/manual/aleksey-lukyanets/market?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=aleksey-lukyanets/market&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/8c0bd51bdba44e04bd2cbbfd7f643e9f)](https://www.codacy.com/manual/aleksey-lukyanets/market?utm_source=github.com&utm_medium=referral&utm_content=aleksey-lukyanets/market&utm_campaign=Badge_Coverage)
![build](https://github.com/aleksey-lukyanets/market/workflows/build/badge.svg)

This project was started in 2014 as a study for Spring and related web technologies,
partly modernized in 2020-21. Since around 2014 professionally I am focused on backend development,
a modernization/maintenance of this project continues occasionally.
This is not a perfectly finished project, so it may contain bugs, imperfection and some mess.

<h2>Technologies</h2>

<ul class="discharged">
    <li>servlet: Spring MVC, Thymeleaf, jQuery, Spring Security</li>
    <li>data: H2, Spring Data JPA, Hibernate</li>
    <li>RESTful service: Spring MVC, Spring HATEOAS</li>
    <li>tests: JUnit, Hamcrest, Mockito, JSONPath</li>
</ul>

<h2>How to</h2>

<p>Java version: 11
<p>build: <code>package</code> Maven task
<p>main class: <code>src/main/java/market/Application.java</code>
<p>run: <code>java -jar target/market-0.1.1.jar</code>
<p>logs: <code>logs/market.log</code>, <code>logs/market.err</code>
<p>H2 console: <code>localhost:8080/h2-console/</code>
<p>Swagger UI: <code>localhost:8080/swagger-ui/#/</code>

<h2>Functionality</h2>

<ol class="discharged">
    <li>Visual representation of the product range</li>
    <li>Customer's shopping cart
        <ul>
            <li>selecting a product: add, delete, change a quantity</li>
            <li>viewing the contents of the cart</li>
            <li>placing an order</li>
            <li>storing in the database a shopping cart of a registered customer</li>
        </ul>
    </li>
    <li>Market control panel
        <ul>
            <li>products and categories: add, edit, delete</li>
            <li>viewing information about placed orders</li>
            <li>managing the availability of goods in stock</li>
            <li>transfer orders from the "in progress" state to the "executed" state</li>
        </ul>        
    </li>
    <li>Secured access to the application
        <ul>
            <li>registration and authorization of customers</li>
            <li>restricted access to the control panel</li>
        </ul>
    </li>
    <li>Double check of form content: client-side and server-side</li>
</ol>

<h2>Legacy branches</h2>

<p><code>good-old-2014</code> - status for 2014
<p><code>jsp-2021</code> - status with web views based on JSP and Apache Tiles

<h2>Links</h2>

For project description in Russian from good old 2014 refer to <code>README_RU.md</code>