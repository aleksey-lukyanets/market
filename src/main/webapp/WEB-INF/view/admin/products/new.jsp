<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1>Добавление товара</h1>

<br>

<s:url value="/admin/products" var="post_path" />
<sf:form id="productForm" class="form-horizontal"
         modelAttribute="product" commandName="product"
         action="${post_path}" method="post">
    
    <tiles:insertAttribute name="form-fields" />
    
    <div class="form-group">
        <div class="col-sm-offset-3 col-sm-8">
            <a href="<c:url value="/admin/products" />" class="btn btn-default">
                Вернуться
            </a>
            <button type="submit" class="btn btn-primary">
                Создать
            </button>
        </div>
    </div>
</sf:form>

