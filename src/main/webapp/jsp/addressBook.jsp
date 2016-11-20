<%-- 
    Document   : addressBook
    Created on : Oct 30, 2016, 6:06:29 PM
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
        <title>Address Book</title>
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/AddressBookCSS.css" rel="stylesheet">
    </head>
    <body>
        <div class='container'>
            <h1 class='text-center'>Address Book</h1>
            <div class='row text-center'>
                <form class='form-inline' action='searchForAddress' method='get'>
                    <a class='btn btn-primary form-control searchItems' href='${pageContext.request.contextPath}/displayAddAddress' role='button'>+ Add Contact</a>
                    <a class='btn btn-primary form-control searchItems' href='${pageContext.request.contextPath}/addressBook' role='button'>List All</a>
                    <select name='searchType' class='form-control searchItems' required>
                        <option value="" disabled selected>Search by</option>
                        <option value="name">Name</option>
                        <option value="city">City</option>
                        <option value="state">State</option>
                        <option value="zip">Zip</option>
                    </select>
                    <input type='text' class='form-control input- searchItems' name='searchTerm' id='searchBar' placeholder='Search...'/>
                    <input type='submit' class='btn btn-primary searchItems' value='Search'/> 
                </form>
            </div>
            <div class='row top15'>
                <div>
                    <table class='table table-striped'>
                        <tr>
                            <th>Select</th>
                            <th>First Name</th>
                            <th>Last Name</th>
                            <th>City</th>
                            <th>State</th>
                        </tr>
                        <form class='form-group' action='displayEditAddress' method='post'>
                        <c:forEach var='address' items='${addressList}'>
                            <tr>
                                <td><button type='submit' class='btn btn-primary' value='${address.id}' name='id'>Select</button></td>
                                <td><c:out value='${address.firstName}'/></td>
                                <td><c:out value='${address.lastName}'/></td>
                                <td><c:out value='${address.city.cityName}'/></td>
                                <td><c:out value='${address.state.stateInitials}'/></td>
                            </tr>
                        </c:forEach>
                        </form>
                    </table>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/jquery-2.2.4.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    </body>
</html>
