<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<h1 class="admin">Заказы</h1>

<tiles:insertAttribute name="filter" />

<div class="table-responsive">
    <table class="table table-small-text" width="100%">
        <thead>
            <tr>
                <th>№</th>
                <th>покупатель</th>
                <th>дата</th>
                <th>доставка</th>

                <th>выписан<br>счёт</th>
                <th>статус<br>оплаты</th>
                <th>статус&nbsp;заказа</th>
                <th width="50"></th>
            </tr>
        </thead>

        <c:forEach var="order" items="${page.content}" varStatus="iter">
            <tr>
                <td>
                    <a data-toggle="collapse" data-parent="#accordion" href="#collapse${iter.index}">
                        ${order.id}
                    </a>
                </td>
                <td>${order.userAccount.name}</td>
                <td>
                    <fmt:formatDate value="${order.dateCreated}" pattern="dd.MM.yyyy, HH:mm"/>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${order.deliveryIncluded}">включена</c:when>
                        <c:otherwise>самовывоз</c:otherwise>
                    </c:choose>
                </td>

                <td>#${order.bill.number}</td>
                <td>
                    <c:choose>
                        <c:when test="${order.bill.payed}">оплачен</c:when>
                        <c:otherwise>ожидает&nbsp;оплаты</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${order.executed}">исполнен</c:when>
                        <c:otherwise>в исполнении</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${! order.executed}">
                        <s:url value="/admin/orders/{orderId}" var="put_executed_url">
                            <s:param name="orderId" value="${order.id}" />
                        </s:url>
                        <sf:form method="put" action="${put_executed_url}">
                            <input type="hidden" name="executed" value="true">
                            <button type="submit" class="btn btn-success btn-xs">
                                <span class="glyphicon glyphicon-ok"></span>&nbsp;
                            </button>
                        </sf:form>
                    </c:if>
                </td>
            </tr>
            <tr class="nopadding">
                <td colspan="7">
                    <div class="panel-collapse collapse" id="collapse${iter.index}">
                        <div class="" style="width: 100%;">

                            <div style="position: relative; float: left; max-width: 170px;">
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Покупатель</b></div>
                                    <ul class="list-group">
                                        <li class="list-group-item">
                                            ${order.userAccount.name}<br>
                                            ${order.userAccount.contacts.phone}<br>
                                            ${order.userAccount.email}
                                        </li>
                                        <c:if test="${order.deliveryIncluded}">
                                            <li class="list-group-item">
                                                адрес доставки:<br>
                                                ${order.userAccount.contacts.address}
                                            </li>
                                        </c:if>
                                    </ul>
                                </div>
                            </div>

                            <div style="position: relative; float: left; max-width: 210px; padding-left: 10px;">
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Счёт</b></div>
                                    <ul class="list-group">
                                        <li class="list-group-item">
                                            <table>
                                                <tr><td align="right" style="padding-right: 7px;">номер:</td><td>${order.bill.number}</td></tr>
                                                <tr><td align="right" style="padding-right: 7px;">от:</td><td><fmt:formatDate value="${order.dateCreated}" pattern="dd.MM.yyyy, HH:mm"/></td></tr>
                                                <tr><td align="right" style="padding-right: 7px;">на сумму:</td><td>${order.bill.totalCost} руб.</td></tr>
                                            </table>
                                        </li>
                                        <li class="list-group-item">
                                            <c:choose>
                                                <c:when test="${order.bill.payed}">
                                                    оплачен картой:<br>
                                                     ${order.bill.ccNumber}
                                                </c:when>
                                                <c:otherwise>ожидает&nbsp;оплаты</c:otherwise>
                                            </c:choose>
                                        </li>
                                    </ul>
                                </div>
                            </div>

                            <div style="position: relative; float: left; padding-left: 10px;">
                                <div class="panel panel-default">
                                    <div class="panel-heading"><b>Позиции заказа</b></div>
                                    <table class="table">
                                        <thead>
                                            <tr>
                                                <th width="110">наименование</th>
                                                <th>цена за ед.</th>
                                                <th>кол-во</th>
                                                <th width="70">цена</th>
                                            </tr>
                                        </thead>
                                        <c:forEach var="orderedProduct" items="${orderedProductsMap[order.id]}">
                                            <tr>
                                                <td>
                                                    ${orderedProduct.product.distillery.title} ${orderedProduct.product.name}
                                                </td>
                                                <td>
                                                    ${orderedProduct.product.price} руб.
                                                </td>
                                                <td>
                                                    ${orderedProduct.quantity}
                                                </td>
                                                <td>
                                                    ${orderedProduct.quantity * orderedProduct.product.price} руб.
                                                </td>
                                            </tr>
                                        </c:forEach>
                                        <c:if test="${order.deliveryIncluded}">
                                            <tr>
                                                <td colspan="3" align="right">подитог:</td>
                                                <td>${order.productsCost} руб.</td>
                                            </tr>
                                            <tr>
                                                <td colspan="3" align="right">доставка по Петербургу:</td>
                                                <td>${order.deliveryСost} руб.</td>
                                            </tr>
                                        </c:if>
                                        <tr>
                                            <td colspan="3" align="right">итог:</td>
                                            <td>${order.productsCost + order.deliveryСost} руб.</td>
                                        </tr>
                                    </table>
                                </div>
                            </div>

                        </div>
                    </div>
                </td>
            </tr>
        </c:forEach>

    </table>
</div>

<tiles:insertAttribute name="pagination" />

<div id="userModal" class="modal fade bs-modal-sm"
     tabindex="-1" role="dialog" aria-labelledby="mySmallModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-sm">
        <div class="modal-content">
            <div class="modal-header">
                <h4 class="modal-title">Информация о покупателе</h4>
            </div>
            <div class="modal-body">
                <table>
                    <tr>
                        <td align="right">имя:</td>
                        <td><span id="user-name"></span></td>
                    </tr>
                    <tr>
                        <td align="right">эл. почта:</td>
                        <td><span id="user-email"></span></td>
                    </tr>
                    <tr>
                        <td align="right">телефон:</td>
                        <td><span id="user-phone"></span></td>
                    </tr>
                </table>
            </div>
        </div>
    </div>
</div>
