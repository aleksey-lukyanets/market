<%--
    Главная страница магазина.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="carousel-wrapper" style="margin-top: 20px;">
    <div id="carousel-example-generic" class="carousel slide" data-ride="carousel" data-interval="10000">
        <!-- Indicators -->
        <ol class="carousel-indicators">
            <li data-target="#carousel-example-generic" data-slide-to="0" class="active"></li>
            <li data-target="#carousel-example-generic" data-slide-to="1"></li>
        </ol>

        <!-- Wrapper for slides -->
        <div class="carousel-inner">
            <div class="item active">
                <img src="${pageContext.request.contextPath}${initParam.imagesPath}carousel/Balvenie.jpg"/>
                <div class="carousel-caption">
                    <p>Винокурня The Balvenie, Даффтаун, Шотландия</p>
                </div>
            </div>
            <div class="item">
                <img src="${pageContext.request.contextPath}${initParam.imagesPath}carousel/Laphroaig.jpg"/>
                <div class="carousel-caption">
                    <p>Винокурня Laphroaig, остров Айла, Шотландия</p>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="region-preview-container">
    <c:forEach var="region" items="${regions}">
        <div class="regionBox">
            <a href="<c:url value='regions/${region.id}'/>">
                <span class="regionLabel scheme-${region.color}"></span>
                <span class="regionLabelText">
                    <span class="regionTitle">${region.name}</span>
                    <br>${region.subtitle}
                </span>
                <img class="regionImage img-responsive" alt="${region.name}"
                     src="${pageContext.request.contextPath}${initParam.regionImagePath}${region.name}-preview.jpg"/>
            </a>
        </div>
    </c:forEach>
</div>

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Добро пожаловать в магазин виски</h4>
            </div>
            <div class="modal-body">
                <p>Вы можете ознакомиться с ассортиментом, сделать заказ, просмотреть перечень уже сделанных заказов, а затем перейти к <a href="/admin/">панели управления</a> магазином.</p>
                <p>Приложение разработано на Java с использованием технологий:</p>
                <ul class="discharged">
                    <li>сервлет: Spring MVC, JavaServer Pages, Arache Tiles;</li>
                    <li>авторизация пользователей: Spring Security;</li>
                    <li>доступ к данным: Hibernate, Spring Data JPA;</li>
                    <li>тесты: Spring Test, JUnit, Hamcrest, JSONPath;</li>
                    <li>веб-интерфейс: jQuery, AJAX, jQuery Validate, Bootstrap Framework;</li>
                    <li>база данных: PostgreSQL;</li>
                    <li>контейнер сервлетов: Apache Tomcat.</li>
                </ul>
                <p>Подробнее приложении можно узнать на странице <a href="<c:url value='/inside'/>">как сделан магазин</a>. Детали реализации каждой из страниц магазина также приведены в заметках, доступных по кнопке <button type="button" class="btn btn-danger btn-xs">Как работает страница?</button>.</p>
            </div>
        </div>
    </div>
</div>
            