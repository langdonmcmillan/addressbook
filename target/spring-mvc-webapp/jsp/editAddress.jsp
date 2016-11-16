<%-- 
    Document   : editContact
    Created on : Oct 30, 2016, 6:06:54 PM
    Author     : apprentice
--%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="s" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="sf" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Edit Contact</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/AddressBookCSS.css" rel="stylesheet">
    </head>
    <body>
        <div class='container'>
        <h1>Edit Address</h1>
        <sf:form class='form-horizontal' modelAttribute='address' action='editAddress'>
            <div class='form-group'>
                <div class='hidden'>
                    <sf:input type="text" class="form-control" path="id"/>
                </div>
                <label for="edit-first-name" class="col-md-4">First Name:</label>
                <div class="col-md-8">
                    <sf:input type="text" class="form-control" id="edit-first-name" path="firstName" placeholder="First Name"/>
                    <sf:errors path="firstName" cssClass="errors"></sf:errors>
                </div>
                <label for="edit-last-name" class="col-md-4">Last Name:</label>
                <div class="col-md-8">
                    <sf:input type="text" class="form-control" id="edit-last-name" path="lastName" placeholder="Last Name"/>
                    <sf:errors path="lastName" cssClass="errors"></sf:errors>
                </div>
                <label for="edit-street" class="col-md-4">Street:</label>
                <div class="col-md-8">
                    <sf:input type="text" class="form-control" id="edit-street" path="street" placeholder="Street"/>
                    <sf:errors path="street" cssClass="errors"></sf:errors>
                </div>
                <label for="edit-city" class="col-md-4">City:</label>
                <div class="col-md-8">
                    <sf:input type="text" class="form-control" id="edit-city" path="city" placeholder="City"/>
                    <sf:errors path="city" cssClass="errors"></sf:errors>
                </div>
                <label for="edit-state" class="col-md-4">State:</label>
                <div class="col-md-8">
                    <sf:input type="text" class="form-control" id="edit-state" path="state" placeholder="State"/>
                    <sf:errors path="state" cssClass="errors"></sf:errors>
                </div>
                <label for="edit-zip" class="col-md-4">Zip:</label>
                <div class="col-md-8">
                    <sf:input type="text" class="form-control" id="edit-zip" path="zip" placeholder="Zip"/>
                    <sf:errors path="zip" cssClass="errors"></sf:errors>
                </div>
            </div>
                <div class='form-group'>
                <input class='btn btn-primary' type='submit' value='Update Address'/>
                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#deleteModal">Delete</button>
                <a class='btn btn-primary' href='${pageContext.request.contextPath}/addressBook' role='button'>Back</a>
            </div>
        </sf:form>        
        </div>
        <form class='form-horizontal' action='deleteAddress' method='post'>
            <div class='hidden'>
                <input value='${address.id}' type='text' name='id'>
            </div>
        <div class="modal fade" id="deleteModal" tabindex="-1" role="dialog" aria-labelledby="deleteModal">
            <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                        <h4 class="modal-title" id="myModalLabel">Delete Address</h4>
                    </div>
                    <div class="modal-body">
                        This will delete this address permanently. Are you sure?
                    </div>
                    <div class="modal-footer">
                        <button type="submit" class="btn btn-primary">Yes, Delete</button>
                        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button> 
                    </div>
                </div>
            </div>
        </div>
        </form>
        <script src="${pageContext.request.contextPath}/js/jquery-2.2.4.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    </body>
</html>
