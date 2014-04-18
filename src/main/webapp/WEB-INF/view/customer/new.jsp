<%--
    Страница регистрации нового пользователя.
--%>

<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-validate.min.js" type="text/javascript"></script>

<h1>Регистрация пользователя</h1>

<p>Чтобы иметь возможность совершать покупки в магазине, пожалуйста, сообщите следующую информацию:</p>

<br>

<sf:form id="signupForm" class="form-horizontal"
         modelAttribute="userDTO" commandName="userDTO" method="post">
    <div class="form-group">
        <label for="name" class="col-sm-4 control-label">
            полное&nbsp;имя
        </label>
        <div class="col-sm-6 has-feedback">
            <sf:input
                path="name" size="19" maxlength="50"
                id="name" name="name" class="form-control"
                data-validate="name" data-description="name" data-describedby="name-desc"
                placeholder="Иван Петров"/>
            <span id="name" class="glyphicon form-control-feedback"></span>
            <span class="help-block">обязательное поле</span>
            <div id="name-desc"></div>
            <sf:errors path="name" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <label for="email" class="col-sm-4 control-label">
            электронная&nbsp;почта
        </label>
        <div class="col-sm-6 has-feedback">
            <sf:input
                path="email"
                size="19"
                maxlength="50"
                id="email"
                name="email"
                class="form-control"
                data-validate="email"
                data-description="email"
                data-describedby="email-desc"
                placeholder="petrov@some.domain"/>
            <span id="email" class="glyphicon form-control-feedback"></span>
            <span class="help-block">обязательное поле</span>
            <div id="email-desc"></div>
            <sf:errors path="email" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <label for="password" class="col-sm-4 control-label">
            пароль
        </label>
        <div class="col-sm-6 has-feedback">
            <sf:password
                path="password"
                showPassword="false"
                size="19"
                maxlength="50"
                id="password"
                name="password"
                class="form-control"
                data-validate="password"
                data-conditional="password"
                data-description="password"
                data-describedby="password-desc"
                placeholder="••••••"/>
            <span id="password" class="glyphicon form-control-feedback"></span>
            <span class="help-block">обязательное поле</span>
            <div id="password-desc"></div>
            <sf:errors path="password" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <label for="confirm" class="col-sm-4 control-label">
            подтверждение
        </label>
        <div class="col-sm-6 has-feedback">
            <input type="password"
                   size="19"
                   maxlength="50"
                   id="confirm"
                   name="confirm"
                   class="form-control"
                   data-validate="confirm"
                   data-conditional="confirm"
                   data-description="confirm"
                   data-describedby="confirm-desc"
                   placeholder="••••••"/>
            <span id="confirm" class="glyphicon form-control-feedback"></span>
            <span class="help-block">обязательное поле</span>
            <div id="confirm-desc"></div>
        </div>
    </div>
    <div class="form-group">
        <label for="phone" class="col-sm-4 control-label">
            номер&nbsp;телефона
        </label>
        <div class="col-sm-6 has-feedback">
            <sf:input
                path="phone"
                size="15"
                maxlength="20"
                id="phone"
                name="phone"
                class="form-control"
                data-validate="phone"
                data-description="phone"
                data-describedby="phone-desc"
                placeholder="+7 (123) 456 78 90"/>
            <span id="phone" class="glyphicon form-control-feedback"></span>
            <span class="help-block">обязательное поле</span>
            <div id="phone-desc"></div>
            <sf:errors path="phone" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <label for="address" class="col-sm-4 control-label">
            адрес&nbsp;доставки
        </label>
        <div class="col-sm-6 has-feedback">
            <sf:input
                path="address"
                size="15"
                maxlength="100"
                id="address"
                name="address"
                class="form-control"
                data-validate="address"
                data-description="address"
                data-describedby="address-desc"/>
            <span id="address" class="glyphicon form-control-feedback"></span>
            <span class="help-block">обязательное поле</span>
            <div id="address-desc"></div>
            <sf:errors path="address" cssClass="alert alert-danger" element="div"/>
        </div>
    </div>
    <div class="form-group">
        <div class="col-sm-offset-4 col-sm-8">
            <button type="submit" class="btn btn-success">Зарегистрироваться</button>
        </div>
    </div>
</sf:form>

<script type="text/javascript">
    $('#signupForm').validate({
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
        conditional: {
            email: function() {
                return ($(this).val().length >= 6);
            },
            password: function() {
                return ($(this).val().length >= 6);
            },
            confirm: function() {
                return ($(this).val() === $('#password').val());
            }
        },
        description: {
            name: {
                pattern: '<div class="alert alert-danger">Специальные символы недопустимы.</div>'
            },
            email: {
                conditional: '<div class="alert alert-danger">Длина должна составлять не менее 6 символов.</div>',
                pattern: '<div class="alert alert-danger">Значение поля должно иметь формат адреса электронной почты.</div>'
            },
            password: {
                conditional: '<div class="alert alert-danger">Длина должна составлять от 6 до 50 символов.</div>',
                pattern: '<div class="alert alert-danger">Пароль должен состоять из цифр и латинских букв.</div>'
            },
            confirm: {
                conditional: '<div class="alert alert-danger">Не совпадает с паролем.</div>',
                pattern: '<div class="alert alert-danger">Пароль должен состоять из цифр и латинских букв.</div>'
            },
            phone: {
                pattern: '<div class="alert alert-danger">Должен состоять из знака +, кода страны, кода региона (1-4 цифр) и номера (6-7 цифр).</div>'
            },
            address: {
                pattern: '<div class="alert alert-danger">Специальные символы недопустимы.</div>'
            }
        }
    });
    $.validateExtend({
        name: {
            required: true,
            pattern: /^[^#$%^*()']*$/
        },
        email: {
            required: true,
            pattern: /^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/
        },
        password: {
            required: true,
            pattern: /^[a-zA-Z0-9]+$/
        },
        confirm: {
            required: true
        },
        phone: {
            required: true,
            pattern: /^\+[1-9]\d{0,2}?[\s]*\(?\d{1,4}\)?[-\s]?\d{1,3}[-\s]?\d{2}[-\s]?\d{2}$/
        },
        address: {
            required: true,
            pattern: /^[^#$%^*()']*$/
        }
    });
</script>

<div id="detailsModal" class="modal" tabindex="-1" role="dialog" aria-labelledby="detailsModal" aria-hidden="true">
    <div class="modal-dialog" style="line-height:160%;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title">Регистрация пользователя</h4>
            </div>
            <div class="modal-body">
                <p>Функции</p>
                <ol type="1">
                    <li>создание учётной записи;</li>
                    <li>получение и сохранение данных о покупателе;</li>
                    <li>проверка корректного вида введённых данных.</li>
                </ol>
                <hr/>
                <p>Детали реализации</p>
                <ol type="1" class="discharged">
                    <li>Пароль учётной записи не хранится в виде текста, при регистрации вычисляется его хэш-функция, которая заносится в базу данных.</li>
                    <li>Указанные пользователем телефон и адрес доставки будут использоваться в качестве контактных данных по умолчанию. Позже они могут быть изменены в процессе оформления заказа.</li>
                    <li>Проверка данных формы выполняется дважды: на стороне пользователя и на стороне сервера. Подробности в разделе <a href="<c:url value="/inside#forms" />">проверка форм</a>.</li>
                </ol>
            </div>
        </div>
    </div>
</div>
