<%--
    Шаблон страницы панели управления.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE HTML>
<html lang="en">
    <head>
        <title>Магазин виски &ndash; Панель управления</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/bootstrap.min.css" media="screen">
        <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/market-styles.css" media="screen">
        <script src="${pageContext.request.contextPath}/resources/js/jquery-1.9.0.min.js" type="text/javascript"></script>
        <script src="${pageContext.request.contextPath}/resources/js/bootstrap.min.js" type="text/javascript"></script>
    </head>
    <body style="text-align: center;">
        <div id="backend-layout">
            <div class="row">
                <div id="backend-content" class="content-column">

                    <tiles:insertAttribute name="menu" />

                    <div class="posts">
                        <tiles:insertAttribute name="content" />
                    </div>

                    <div class="footer">
                        <ul class="list-inline">
                            <li><a href="<c:url value="/inside" />" class="btn btn-default btn-xs">как сделан магазин</a></li>
                            <li><a href="https://github.com/aleksej-lukyanets/market">исходные коды на github</a></li>
                            <li><a href="http://spb.hh.ru/resume/002aa1acff01bbb6c90039ed1f744c5a305658">резюме на hh.ru</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </body>
</html>
