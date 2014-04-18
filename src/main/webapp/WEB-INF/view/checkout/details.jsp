<%--
    Страница подтверждения/обновления контактных данных (шаг 1 оформления заказа).
--%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>
<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-validate.min.js" type="text/javascript"></script>

<h1>Уточнение контактных данных</h1>

<p>На этой странице вы можете изменить адрес доставки и контактный номер телефона.</p>

<br>

<sf:form id="detailsForm" class="form-horizontal"
         modelAttribute="userContacts" commandName="userContacts" method="put">
    <div class="form-group">
        <label class="col-sm-4 control-label">
            использовать данные:
        </label>
        <div class="col-sm-4">
            <div class="radio">
                <label for="useExisting">
                    <input type="radio" id="useExisting" name="infoOption"
                           value="useExisting" checked="checked">
                    имеющиеся
                </label>
            </div>
            <div class="radio">
                <label for="useNew">
                    <input type="radio" id="useNew" name="infoOption"
                           value="useNew">
                    обновлённые
                </label>
            </div>
        </div>
    </div>
    <div class="form-group">
        <label for="phone" class="col-sm-4 control-label">
            телефон
        </label>
        <div class="col-sm-4 has-feedback">
            <sf:input path="phone" id="phone" name="phone"
                size="15" maxlength="18" class="form-control"
                data-required="false"
                data-validate="phone" data-description="phone" data-describedby="phone-desc"
                disabled="true"/>
            <span id="phone" class="glyphicon form-control-feedback"></span>
            <div id="phone-desc"></div>
            <sf:errors path="phone" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <label for="address" class="col-sm-4 control-label">
            адрес&nbsp;доставки
        </label>
        <div class="col-sm-6 has-feedback">
            <sf:input path="address" id="address" name="address"
                size="15" maxlength="100" class="form-control"
                data-required="false"
                data-validate="address" data-description="address" data-describedby="address-desc"
                disabled="true"/>
            <span id="address" class="glyphicon form-control-feedback"></span>
            <div id="address-desc"></div>
            <sf:errors path="address" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-4">
            <a href="<c:url value="/cart" />" class="btn btn-default pull-right" style="margin-right: -25px;">
                вернуться к корзине
            </a>
        </div>
        <div class="col-sm-8">
            <button type="submit" class="btn btn-primary">
                перейти к оплате
            </button>
        </div>
    </div>
</sf:form>

<script type="text/javascript">
    $('#detailsForm').delegate('input[name=infoOption]:checked', 'change', function() {
        var value = $('input[name=infoOption]:checked', '#detailsForm').val();
        if (value === "useExisting") {
            $('#phone').prop("disabled", true);
            $('#address').prop("disabled", true);
            //$('#cityRegion').prop("disabled", true);
            $('#phone').prop("data-required", false);
            $('#address').prop("data-required", false);
        } else {
            $('#phone').prop("disabled", false);
            $('#address').prop("disabled", false);
            //$('#cityRegion').prop("disabled", false);
            $('#phone').prop("data-required", true);
            $('#address').prop("data-required", true);
        }
    });
    $('#detailsForm').validate({
        onKeyup: true,
        onChange: true,
        submitHandler: function(form) {
            form.submit();
        },
        eachValidField: function() {
            var input = $(this).attr('id');
            $(this).closest('div.form-group').removeClass('has-error').addClass('has-success');
            $('span#' + input).removeClass('glyphicon-remove').addClass('glyphicon-ok');
        },
        eachInvalidField: function() {
            var input = $(this).attr('id');
            $(this).closest('div.form-group').removeClass('has-success').addClass('has-error');
            $('span#' + input).removeClass('glyphicon-ok').addClass('glyphicon-remove');
        },
        description: {
            phone: {
                required: '<div class="alert alert-danger">Обязательное поле.</div>',
                pattern: '<div class="alert alert-danger">Должен состоять из знака +, кода страны, кода региона (1-4 цифр) и номера (6-7 цифр).</div>'
            },
            address: {
                required: '<div class="alert alert-danger">Обязательное поле.</div>',
                pattern: '<div class="alert alert-danger">Специальные символы недопустимы.</div>'
            }
        }
    });
    $.validateExtend({
        phone: {
            pattern: /^\+[1-9][0-9]?[\s]*\(?\d{3}\)?[-\s]?\d{3}[-\s]?\d{2}[-\s]?\d{2}$/
        },
        address: {
            pattern: /^[^#$%^*()']*$/
        }
    });
</script>

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Уточнение контактных данных</h4>
            </div>
            <div class="modal-body">
                <p>Функции:</p>
                <ol type="1">
                    <li>проверка корректного вида введённых данных;</li>
                    <li>сохранение новых данных в профиль покупателя.</li>
                </ol>
                <hr/>
                <p>Детали реализации</p>
                <ol type="1" class="discharged list-unstyled">
                    <li>Проверка данных формы выполняется дважды: на стороне пользователя и на стороне сервера. Подробности в разделе <a href="<c:url value="/inside#forms" />">проверка форм</a>.</li>
                </ol>
            </div>
        </div>
    </div>
</div>
