<%--
    Шаблон страницы внешнего интерфейса.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE HTML>
<html lang="en">
    <head>
        <title>Магазин виски</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" media="screen"/>
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/market-styles.css" media="screen"/>
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body class="frontend">
        <div id="layout">
            <div class="row">
                <div class="col-md-4 col-sm-4 col-xs-12" id="sidebar">
                    <div class="clearfix">
                        <div class="btn-group pull-right" style="padding-right:1.5em;padding-bottom:1em;">
                            &nbsp;<a id="details" class="btn btn-danger">Как работает страница?</a>
                        </div>
                    </div>
                    <div class="header">
                        <div class="brand-title">
                            <a class="brand-title" href="<c:url value="/" />">Магазин виски</a>
                        </div>
                        <div class="brand-tagline">виски с доставкой по Петербургу</div>
                    </div>
                    <c:if test="${!empty regions}">
                        <tiles:insertAttribute name="menu" />
                    </c:if>
                </div>
                            
                <div class="col-md-8 col-sm-8 col-xs-12" id="content">
                    <div id="frontend-content" class="content-column">
                        
                        <div class="headerWidget">
                            <div class="pull-right">
                                <div class="btn-group btn-group-sm">
                                    <a href="<c:url value="/cart" />" class="btn btn-default btn-sm">
                                        корзина&nbsp;&nbsp;&nbsp;<span class="glyphicon glyphicon-shopping-cart" style="font-size:13px;"></span>
                                        <span id="cart-total-items">
                                            <c:if test="${cart.totalItems > 0}">
                                                <span class="badge">${cart.totalItems}</span>
                                            </c:if>
                                        </span>
                                    </a>
                                    <security:authorize access="isAuthenticated()">
                                    </div>
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-default btn-sm">
                                            <security:authentication property="principal.username" />
                                        </button>
                                        <button type="button" class="btn btn-default btn-sm dropdown-toggle" data-toggle="dropdown">
                                            <span class="caret"></span>
                                            <span class="sr-only">Toggle Dropdown</span>
                                        </button>
                                        <ul class="dropdown-menu dropdown-menu-right" role="menu">
                                            <li>
                                                <a href="<c:url value="customer/orders" />">история заказов</a>
                                            </li>
                                            <li>
                                                <a href="<c:url value="${pageContext.request.contextPath}/j_spring_security_logout" />">выйти</a>
                                            </li>
                                        </ul>
                                    </security:authorize>
                                    <security:authorize access="! isAuthenticated()">
                                        <a href="<c:url value="/login" />" class="btn btn-default btn-sm">вход</a>
                                    </security:authorize>
                                </div>
                            </div>
                        </div>

                        <div class="posts clearfix">
                            <tiles:insertAttribute name="content" />
                        </div>

                        <div class="footer">
                            <ul class="list-inline">
                                <li><a href="<c:url value="/inside" />" class="btn btn-danger btn-xs">как сделан магазин</a></li>
                                <li><a href="<c:url value="/rest-api" />" class="btn btn-danger btn-xs">rest api</a></li>
                                <li><a href="https://github.com/aleksej-lukyanets/market">исходные коды на github</a></li>
                                <li><a href="http://spb.hh.ru/resume/002aa1acff01bbb6c90039ed1f744c5a305658">резюме на hh.ru</a></li>
                            </ul>
                        </div>

                    </div>
                </div>

            </div>
        </div>
        <script type="text/javascript">
            $('body').delegate('#details', 'click', function() {
                $('#detailsModal').modal({show: true});
            });
        </script>
    </body>
</html>
