[![Codacy Badge](https://api.codacy.com/project/badge/Grade/8c0bd51bdba44e04bd2cbbfd7f643e9f)](https://www.codacy.com/manual/aleksey-lukyanets/market?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=aleksey-lukyanets/market&amp;utm_campaign=Badge_Grade)
[![Codacy Badge](https://api.codacy.com/project/badge/Coverage/8c0bd51bdba44e04bd2cbbfd7f643e9f)](https://www.codacy.com/manual/aleksey-lukyanets/market?utm_source=github.com&utm_medium=referral&utm_content=aleksey-lukyanets/market&utm_campaign=Badge_Coverage)
![build](https://github.com/aleksey-lukyanets/market/workflows/build/badge.svg)

<h2>Технологии</h2>

<ul class="discharged">
    <li>сервлет: Spring MVC, JavaServer Pages, Arache Tiles</li>
    <li>авторизация пользователей: Spring Security</li>
    <li>доступ к данным: Hibernate, Spring Data JPA</li>
    <li>веб-служба в стиле REST</li>
    <li>тесты: Spring Test, JUnit, Hamcrest, JSONPath;</li>
    <li>веб-интерфейс: jQuery, jQuery Validate Plugin, Bootstrap</li>
    <li>база данных: PostgreSQL</li>
    <li>контейнер сервлетов: Apache Tomcat</li>
</ul>

<h2>Функционал магазина</h2>

<ol class="discharged">
    <li>Наглядное представление ассортимента товаров</li>
    <li>Корзина покупателя
        <ul>
            <li>выбор товаров: добавление, удаление, изменение количества</li>
            <li>просмотр содержимого корзины</li>
            <li>оформление заказа</li>
            <li>хранение корзины зарегистрированного покупателя в базе данных</li>
        </ul>
    </li>
    <li>Панель управления магазином
        <ul>
            <li>товары и категории: добавление, редактирование, удаление</li>
            <li>просмотр информации о размещённых заказах</li>
            <li>управление наличием товаров на складе</li>
            <li>перевод заказов из состояния "в исполнении" в состояние "исполнен"</li>
        </ul>        
    </li>
    <li>Безопасный доступ к приложению
        <ul>
            <li>регистрация и авторизация пользователей</li>
            <li>ограничение доступа к панели управления</li>
        </ul>
    </li>
    <li>Двойная проверка содержимого форм: на стороне клиента и на стороне сервера</li>
</ol>

<h2>Оформление заказа</h2>

<p>Ниже приведена диаграмма процесса оформления заказа, на которую нанесены
    элементы данных и доступные покупателю действия.</p>
<center>
    <a href="http://46.101.111.55:8080/resources/img/checkout-flow.png">
        <img src="http://46.101.111.55:8080/resources/img/checkout-flow.png"
             alt="процесс оформления заказа"/>
    </a>
</center>
<br>

<h2>Веб-служба REST</h2>

<p>Помимо гипертекстового интерфейса магазин представляет веб-службу, через которую
    доступен функционал магазина. Описание веб-службы находится на странице
    <a href="<c:url value='/rest-api' />">REST API</a>.</p>

<h2>Сортировка, фильтрация, разбивка на страницы</h2>

<p>Приложение предоставляет возможность просматривать ресурсы удобным пользователю способом.</p>
<p>Не нарушая принципов стиля REST, критерии передаются в параметрах URI. Например,
    в панели управления магазином так выглядит запрос отображения таблицы всех имеющихся
    в наличии товаров, с сортировкой по возрастанию цены и 5 товаров на странице:
    <code><b>/admin/storage</b> ? available=true &amp; direct=asc &amp; size=5</code>
    (без пробелов).</p>
<p>Операции сортировки, фильтрации и постраничного отображения выделены в отдельную
    иерархию классов в пакете <code>market.sorting</code>, которая объединяет
    методы изменения значений опций, формирования запроса к БД (<code>PageRequest</code>
    Spring Data) и добавления всех необходимых данных к модели <code>Model</code> Spring MVC.</p>

<h2>Валидация форм</h2>

<p>Проверка данных всех форм пользовательского и административного интерфейса выполняется
    дважды: на стороне пользователя и на стороне сервера.</p>
<ul class="discharged">
    <li>Проверка на стороне пользователя осуществляется с использованием jQuery Validate Plugin,
        который проверяет данные в момент ввода средствами JavaSript. Для посимвольной проверки строки применены
        регулярные выражения (regex). Визуализация дополнена классами Bootstrap.</li>
    <li>Проверка на стороне сервера выполняется с использованием пакетов <code>javax.validation</code> и
        <code>org.springframework.validation</code>.</li>
</ul>
<p>Такой подход к валидации форм делает процесс проверки данных комфортным
    для пользователя и вместе с тем гарантирует выполнение проверки при отключённом
    JavaScript в браузере пользователя.</p>

<h2>Обработка исключений</h2>

<p>В приложении реализована централизованная обработка исключений классом
    <code>market.controller.SpringExceptionHandler</code> с аннотацией
    <code>@ControllerAdvice</code>, предоставленной Spring.</p>
<p>Помимо объединения обработчиков в единый класс, такой подход позволяет
    вынести проверку авторизации клиентов веб-сервиса из реализации контроллеров:
    права пользователя проверяются перехватчиком
    <code>market.interceptor.RestUserCheckInterceptor</code> и в случае неудачной
    аутентификации исключение <code>RestNotAuthenticatedException</code> передаётся
    обработчику в обход контроллеров, с последующим возвратом клиенту HTTP-состояния
    <code>401 Unauthorized</code> (вместо перенаправления на страницу входа, как в случае
    гипертекстового интерфейса магазина). При удачной аутентификации запрос
    передаётся соответствующему контроллеру.</p>

<h2>Модель базы данных</h2>

<p>База данных приложения состоит из 13 связанных таблиц, отображаемых средствами Hibernate в 14 классов.</p>

<center>
    <a href="http://46.101.111.55:8080/resources/img/database-model.png">
        <img src="http://46.101.111.55:8080/resources/img/database-model.png"
             width="500" alt="схема базы данных"/>
    </a>
</center>
<br>
<p>Слой доступа к данным на первоначальном этапе разработки был представлен классами DAO,
    а с введением функций разбивки на страницы и сортировки реализован
    с помощью репозитория Spring Data JPA.</p>

<h2>Пользовательские классы Spring</h2>

<p>Функционал фреймворков Spring MVC и Spring Security расширен следующими классами:</p>
<ul class="discharged">
    <li><code>UserDetailsServiceImpl</code> реализует интерфейс <code>UserDetailsService</code>
        и обеспечивает извлечение профиля пользователя из базы данных;</li>
    <li><code>CustomAuthenticationSuccessHandler</code> реализует интерфейс
        <code>AuthenticationSuccessHandler</code> и обрабатывает событие успешной аутентификации пользователя;</li>
    <li><code>SpringExceptionHandler</code> осуществляет централизованную обработку исключений;</li>
    <li><code>SessionCartInterceptor</code> реализует интерфейс <code>HandlerInterceptorAdapter</code>
        и проверяет до обработки запроса контроллерами, существует ли в модели атрибут корзины покупателя;
        при отсутствии создатся новая корзина; такое решение позволяет централизовать создание корзины,
        в том числе для случая, когда браузер пользователя не принимает cookies и поэтому
        не поддерживает хранение параметров сессии;</li>
    <li><code>RestUserCheckInterceptor</code> реализует интерфейс <code>HandlerInterceptorAdapter</code>
        и используется для проверки прав пользователя при доступе к веб-службе.</li>
</ul>

<h1>Веб-служба REST</h1>

<p>REST-интерфейс приложения предоставляет доступ к ресурсам магазина: позволяет
    регистрировать покупателей, изменять их контактные данные, запрашивать
    сведения о товарах, добавлять товары в корзину и оформлять заказы.</p>
<p>Обмен данными между клиентом и веб-службой магазина осуществляется в формате JSON,
    аутентификация выполняется средствами Basic Authentication.</p>
<p>В соответствии со стилем REST, взаимодействие между клиентом и сервером лишено
    сеансового состояния, поэтому веб-служба магазина не предоставляет гостевую
    корзину: информация о товарах доступна всем клиентам, но заказ может быть
    собран и оформлен только зарегистрированным пользователем.</p>
<p>Ниже приводится интерфейс веб-службы и примеры взаимодействия.</p>

<h2>Взаимодействие с веб-службой</h2>

<p>Доступ к ресурсам магазина можно получить с использованием любого HTTP-клиента,
    поддерживающего Basic-аутентификацию, например RESTClient для Mozilla Firefox.</p>
<p>Так обращение к ресурсу <code>http://46.101.111.55:8080/rest/products</code>
    возвращает список всех товаров:</p>
<pre style="width: 95%;">[<br/>    {<br/>        "productId": 1,<br/>        "distillery": "Ardbeg",<br/>        "name": "Ten",<br/>        "price": 4030,<br/>        "links": [{<br/>                "rel": "self",<br/>                "href": "http://46.101.111.55:8080/rest/products/1"}]<br/>    },<br/>    {<br/>        "productId": 2,<br/>        "distillery": "Balvenie",<br/>        "name": "12 y.o. Doublewood",<br/>        "price": 4100,<br/>        "links": [{<br/>                "rel": "self",<br/>                "href": "http://46.101.111.55:8080/rest/products/2" }]<br/>    }<br/>    <br/>    ...<br/>]</pre>
<p>Сведения об отдельном товаре доступны по его идентификатору.
    Ресурс <code>/products/2</code>:</p>
<pre style="width: 85%;">{<br/>    "id": 2,<br/>    "distillery": "Balvenie",<br/>    "name": "12 y.o. Doublewood",<br/>    "price": 4100,<br/>    "age": 12,<br/>    "volume": 700,<br/>    "alcohol": 40,<br/>    "description": "Помимо стандартных бочек выдерживается в бочках<br/>            из-под хереса. Гармоничный аромат свежего мёда, ванили,<br/>            воска, дуба и цветочной пыльцы с сухой горчинкой.",<br/>    "inStock": true<br/>}</pre>

<br>
<h5>Регистрация покупателя</h5>

<p>Регистрация нового покупателя осуществляется отправкой запроса <code>POST /signup</code>:</p>
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
<pre style="width: 95%;">заголовок:   Status Code: 200 Ok<br>             Content-Type: application/json; charset=UTF-8<br>тело:        {<br>                "user": "ivan.petrov@yandex.ru",<br>                "items":<br>                [<br>                    {<br>                        "productId": 2,<br>                        "quantity": 1,<br>                        "links": [{<br>                            "rel": "self",<br>                            "href": "http://46.101.111.55:8080/rest/products/2" }]<br>                    }<br>                ],<br>                "totalItems": 1,<br>                "productsCost": 4100,<br>                "deliveryCost": 400,<br>                "deliveryIncluded": true,<br>                "totalCost": 4500,<br>                "links":<br>                [<br>                    {<br>                        "rel": "Customer contacts",<br>                        "href": "http://46.101.111.55:8080/rest/customer/contacts"<br>                    },<br>                    {<br>                        "rel": "Payment",<br>                        "href": "http://46.101.111.55:8080/rest/cart/payment"<br>                    }<br>                ]<br>             }</pre>
<p>Опция доставки может быть изменена запросом <code>PUT /cart/delivery/{boolean}</code>.</p>
<br>
<p>Для оформления заказа следует отправить номер банковской карты, с которой
    будет оплачен заказ, запросом <code>POST /cart/payment</code>:</p>
<pre style="width: 95%;">заголовок:   Content-Type: application/json; charset=UTF-8<br>             Authorization: Basic aXZhbi5wZXRyb3ZAeWFuZGV4LnJ10nBldHJvdg==<br>тело:        {"number": "4444 3333 2222 1111"}</pre>
<p>Приложение вернёт подтверждение об оплате и приёме заказа:</p>
<pre style="width: 95%;">заголовок:   Status Code: 201 Created<br>             Location: http://46.101.111.55:8080/rest/customer/orders/13<br>             Content-Type: application/json; charset=UTF-8<br>тело:        {<br>                  "id": 13,<br>                  "user": "ivan.petrov@yandex.ru",<br>                  "billNumber": 525553712,<br>                  "dateCreated": 1397559589798,<br>                  "productsCost": 4100,<br>                  "deliveryCost": 400,<br>                  "deliveryIncluded": true,<br>                  "totalCost": 4500,<br>                  "payed": true,<br>                  "executed": false<br>             }</pre>
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
