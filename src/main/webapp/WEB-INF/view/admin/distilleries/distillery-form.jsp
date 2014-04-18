<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form"%>

<script src="${pageContext.request.contextPath}/resources/js/jquery-validate.min.js" type="text/javascript"></script>

<div class="form-group">
    <label for="region" class="col-sm-3 control-label">
        регион
    </label>
    <div class="col-sm-6">
        <div class="btn-group">
            <sf:select multiple="single"
                path="region.id" class="form-control"
                name="region" id="region">
                <sf:options items="${regions}" itemValue="id" itemLabel="name" />
            </sf:select>
        </div>
    </div>
</div>

<div class="form-group">
    <label for="title" class="col-sm-3 control-label">
        название
    </label>
    <div class="col-sm-6 has-feedback">
        <sf:input
            path="title" size="19" maxlength="20" class="form-control"
            name="title" id="title" value="${distillery.title}"
            data-validate="title" data-description="title" data-describedby="title-desc"/>
        <span id="title" class="glyphicon form-control-feedback"></span>
        <span class="help-block">обязательное поле</span>
        <div id="title-desc"></div>
        <sf:errors path="title" cssClass="alert alert-danger" element="div"/>
    </div>
</div>

<div class="form-group">
    <label for="description" class="col-sm-3 control-label">
        описание
    </label>
    <div class="col-sm-6">
        <sf:textarea
            path="description" rows="3" maxlength="1000" class="form-control"
            name="description" id="description" value="${distillery.description}"/>
    </div>
</div>

<script type="text/javascript">
    //<![CDATA[
    $(document).ready(function() {
        $('#distilleryForm').validate({
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
                title: {
                    pattern: '<div class="alert alert-danger">Специальные символы недопустимы.</div>'
                }
            }
        });
        $.validateExtend({
            title: {
                required: true,
                pattern: /^[^#$%^*()']*$/
            }
        });
    });
    //]]>
</script>
