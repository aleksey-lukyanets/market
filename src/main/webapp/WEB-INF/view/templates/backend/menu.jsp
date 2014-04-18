<%--
    Главное меню панели управления.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<nav class="navbar navbar-default" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="<c:url value="/admin/" />">
                Панель управления
            </a>
        </div>
        <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
            <ul class="nav navbar-nav">
                <li><a href="<c:url value="/admin/storage" />">Наличие товаров</a></li>
                <li><a href="<c:url value="/admin/orders" />">Заказы</a></li>
                <li class="dropdown">
                    <a href="#" class="dropdown-toggle" data-toggle="dropdown">Витрина <b class="caret"></b></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value="/admin/regions" />">Регионы</a></li>
                        <li><a href="<c:url value="/admin/distilleries" />">Винокурни</a></li>
                        <li><a href="<c:url value="/admin/products" />">Товары</a></li>
                    </ul>
                </li>
            </ul>
            <security:authorize access="isAuthenticated()">
                <ul class="nav navbar-nav navbar-right">
                    <li class="dropdown">
                        <a href="#" class="dropdown-toggle" data-toggle="dropdown">
                            <security:authentication property="principal.username" />
                            <b class="caret"></b>
                        </a>
                        <ul class="dropdown-menu">
                            <li><a href="<c:url value="${pageContext.request.contextPath}/j_spring_security_logout" />">Выйти</a></li>
                        </ul>
                    </li>
                </ul>
            </security:authorize>
        </div>
    </div>
</nav>
