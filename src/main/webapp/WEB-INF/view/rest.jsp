<%--
    Страница "REST-интерфейс".
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>

<h1>Веб-служба REST</h1>

<p>REST-интерфейс приложения предоставляет доступ к ресурсам магазина: позволяет
    регистрировать покупателей, изменять их контактные данные, запрашивать
    сведения о товарах, добавлять товары в корзину и оформлять заказы.</p>
<p>Обмен данными между клиентом и веб-службой магазина осуществляется в формате JSON,
    аутентификация выполняется средствами Basic Authentication.
    Для упрощения навигации возвращённые сервисом объекты содержат гиперссылки.</p>
<p>В соответствии со стилем REST, взаимодействие между клиентом и сервером лишено
    сеансового состояния, поэтому веб-служба магазина не предоставляет гостевую
    корзину: информация о товарах доступна всем клиентам, но заказ может быть
    собран и оформлен только зарегистрированным пользователем.</p>
<p>Ниже приводится интерфейс веб-службы и примеры взаимодействия.</p>

<h2>Взаимодействие с веб-службой</h2>

<p>Доступ к ресурсам магазина можно получить с использованием любого HTTP-клиента,
    поддерживающего Basic-аутентификацию, например RESTClient для Mozilla Firefox.</p>
<p>Так обращение к ресурсу <code>http://market.jelasticloud.com/rest/products</code>
    возвращает список всех товаров:</p>
<pre style="width: 95%;">[<br/>    {<br/>        "productId": 1,<br/>        "distillery": "Ardbeg",<br/>        "name": "Ten",<br/>        "price": 4030,<br/>        "links": [{<br/>                "rel": "self",<br/>                "href": "http://goformalts.jelasticloud.com/rest/products/1" }]<br/>    },<br/>    {<br/>        "productId": 2,<br/>        "distillery": "Balvenie",<br/>        "name": "12 y.o. Doublewood",<br/>        "price": 4100,<br/>        "links": [{<br/>                "rel": "self",<br/>                "href": "http://goformalts.jelasticloud.com/rest/products/2" }]<br/>    }<br/>    <br/>    ...<br/>]</pre>
<p>Сведения об отдельном товаре доступны по его идентификатору.
    Ресурс <code>/products/2</code>:</p>
<pre style="width: 85%;">{<br/>    "id": 2,<br/>    "distillery": "Balvenie",<br/>    "name": "12 y.o. Doublewood",<br/>    "price": 4100,<br/>    "age": 12,<br/>    "volume": 700,<br/>    "alcohol": 40,<br/>    "description": "Помимо стандартных бочек выдерживается в бочках<br/>            из-под хереса. Гармоничный аромат свежего мёда, ванили,<br/>            воска, дуба и цветочной пыльцы с сухой горчинкой.",<br/>    "inStock": true<br/>}</pre>

<br>
<h5>Регистрация покупателя</h5>

<p>Регистрация нового покупателя выполняется отправкой запроса <code>POST /signup</code>:</p>
<pre style="width: 75%;">заголовок:   Content-Type: application/json; charset=UTF-8<br>тело:        {<br>                 "email": "ivan.petrov@yandex.ru",<br>                 "name": "Иван Петров",<br>                 "password": "Иван Петров",<br>                 "phone": "+7 123 456 67 89",<br>                 "address": "ул. Итальянская, д. 7"<br>             }</pre>
<p>Ответ приложения при успешном создании аккаунта: <code>200 Ok</code>.</p>
<p>Если покупатель с указанным адресом электронной почты уже существует в магазине,
    либо если возникли нарушения при валидации переданных данных, клиенту будет возвращено
    HTTP-состояние <code>406 Not Acceptable</code> с соответствующими пояснениями.</p>
<p>Например, об ошибке в имени <code>"name": "name@#$%^"</code>
    сервер уведомит ответом:</p>
<pre style="width: 95%;">{<br>    "fieldErrors":<br>    [{<br>        "field": "name",<br>        "message": "В имени допустимы только буквы, пробел, дефис и апостроф."<br>    }]<br>}</pre>
<p>Получить или изменить контактные данные можно обращением к ресурсу соответственно
    <code>/customer/contacts</code> запросами GET или PUT.</p>

<br>
<h5>Формирование заказа</h5>

<p>Зарегистрированный покупатель может добавить товар в корзину запросом <code>PUT /cart</code>:</p>
<pre style="width: 95%;">заголовок:   Content-Type: application/json; charset=UTF-8<br>             Authorization: Basic aXZhbi5wZXRyb3ZAeWFuZGV4LnJ10nBldHJvdg==<br>тело:        {"productId": 2, "quantity": 1}</pre>
<p>В ответе приложение вернёт изменённую корзину:</p>
<pre style="width: 95%;">заголовок:   Status Code: 200 Ok<br>             Content-Type: application/json; charset=UTF-8<br>тело:        {<br>                "user": "ivan.petrov@yandex.ru",<br>                "items":<br>                [<br>                    {<br>                        "productId": 2,<br>                        "quantity": 1,<br>                        "links": [{<br>                            "rel": "self",<br>                            "href": "http://goformalts.../rest/products/2" }]<br>                    }<br>                ],<br>                "totalItems": 1,<br>                "productsCost": 4100,<br>                "deliveryCost": 400,<br>                "deliveryIncluded": true,<br>                "totalCost": 4500,<br>                "links":<br>                [<br>                    {<br>                        "rel": "Customer contacts",<br>                        "href": "http://goformalts.../rest/customer/contacts"<br>                    },<br>                    {<br>                        "rel": "Payment",<br>                        "href": "http://goformalts.../rest/cart/payment"<br>                    }<br>                ]<br>             }</pre>
<p>Опция доставки может быть изменена запросом <code>PUT /cart/delivery/{boolean}</code>.</p>
<br>
<p>Для оформления заказа следует отправить номер банковской карты, с которой
    будет оплачен заказ, запросом <code>POST /cart/payment</code>:</p>
<pre style="width: 95%;">заголовок:   Content-Type: application/json; charset=UTF-8<br>             Authorization: Basic aXZhbi5wZXRyb3ZAeWFuZGV4LnJ10nBldHJvdg==<br>тело:        {"number": "4444 3333 2222 1111"}</pre>
<p>Приложение вернёт подтверждение об оплате и приёме заказа:</p>
<pre style="width: 100%;">заголовок:   Status Code: 201 Created<br>             Location: http://market.jelasticloud.com/rest/customer/orders/13<br>             Content-Type: application/json; charset=UTF-8<br>тело:        {<br>                  "id": 13,<br>                  "user": "ivan.petrov@yandex.ru",<br>                  "billNumber": 525553712,<br>                  "dateCreated": 1397559589798,<br>                  "productsCost": 4100,<br>                  "deliveryCost": 400,<br>                  "deliveryIncluded": true,<br>                  "totalCost": 4500,<br>                  "payed": true,<br>                  "executed": false,<br>                  "links": [{<br>                      "rel": "self",<br>                      "href": "http://goformalts.../rest/customer/orders/13" }]<br>             }</pre>
<p>Получить перечень всех оставленных покупателем заказов можно обратившись к ресурсу
    <code>/customer/orders</code>.</p>
<br>

<h2>Операции с ресурсами</h2>

<h4>Сведения о товарах</h4>
<table class="table table-marked">
    <thead>
        <tr>
            <th width="200">ресурс</th>
            <th width="170">описание</th>
            <th>статусы ответа</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>GET /products</td>
            <td>Возвращает перечень всех товаров магазина</td>
            <td>200</td>
        </tr>
        <tr>
            <td>GET /products/:id</td>
            <td>Возвращает товар с указанным id</td>
            <td>200 — товар возвращён в теле ответа,<br>
                404 — товара с таким id не существует</td>
        </tr>
    </tbody>
</table>

<h4>Корзина покупателя (требует авторизации)</h4>
<table class="table table-marked">
    <thead>
        <tr>
            <th width="200">ресурс</th>
            <th width="170">описание</th>
            <th>статусы ответа</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>GET /cart</td>
            <td>Возвращает корзину покупателя</td>
            <td>200</td>
        </tr>
        <tr>
            <td>PUT /cart</td>
            <td>Добавляет товар в корзину</td>
            <td>200 — товар добавлен, обновлённая корзина находится в теле ответа,<br>
                406 — товара с запрошенным id не существует; пояснения в теле ответа</td>
        </tr>
        <tr>
            <td>DELETE /cart</td>
            <td>Удаляет из корзины все товары</td>
            <td>200 — корзина очищена, обновлённая корзина находится в теле ответа</td>
        </tr>
        <tr>
            <td>PUT /cart/delivery/:boolean</td>
            <td>Включает в заказ доставку</td>
            <td>200 — опция доставки изменена, обновлённая корзина находится в теле ответа</td>
        </tr>
        <tr>
            <td>POST /cart/payment</td>
            <td>Подтверждает заказ и оплачивает его картой с указанным номером</td>
            <td>201 — заказ оплачен и принят, ссылка на заказ находится в заголовке, детали заказа — в теле ответа,<br>
                406 — некорректный формат номера карты, либо корзина пуста; пояснения в теле ответа</td>
        </tr>
    </tbody>
</table>

<h4>Регистрация нового покупателя</h4>
<table class="table table-marked">
    <thead>
        <tr>
            <th width="200">ресурс</th>
            <th width="170">описание</th>
            <th>статусы ответа</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>POST /signup</td>
            <td>Регистрирует нового покупателя</td>
            <td>200 — покупатель зарегистрирован и возвращён в теле ответа,<br>
                406 — некорректный формат полученных данных; пояснения в теле ответа</td>
        </tr>
    </tbody>
</table>

<h4>Профиль покупателя (требует авторизации)</h4>
<table class="table table-marked">
    <thead>
        <tr>
            <th width="200">ресурс</th>
            <th width="170">описание</th>
            <th>статусы ответа</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td>GET /customer/contacts</td>
            <td>Возвращает контактные данные покупателя</td>
            <td>200 — контактные данные возвращены в теле ответа</td>
        </tr>
        <tr>
            <td>PUT /customer/contacts</td>
            <td>Изменяет контактные данные покупателя</td>
            <td>200 — данные изменены, обновлённые возвращены в теле ответа,<br>
                406 — некорректный формат полученных данных; пояснения в теле ответа</td>
        </tr>
        <tr>
            <td>GET /customer/orders</td>
            <td>Возвращает перечень заказов покупателя</td>
            <td>200 — перечень заказов возвращён в теле ответа</td>
        </tr>
        <tr>
            <td>GET /customer/orders/:id</td>
            <td>Возвращает заказ с указанным id</td>
            <td>200 — заказ возвращён в теле ответа,<br>
                404 — заказ с таким id у авторизованного пользователя не существует</td>
        </tr>
    </tbody>
</table>

<script type="text/javascript">
    $(document).ready(function() {
        $('#details').hide();
    });
</script>
