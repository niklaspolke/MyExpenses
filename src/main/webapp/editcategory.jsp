<%@page language="Java" contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<c:import url="header.jspf" charEncoding="UTF-8"/>

<h3>Edit Category</h3>

<form action="editcategory" method="post">
    <fieldset>
        <legend>Category</legend>
        <table>
            <tbody>
                <tr>
                    <th>
                        <label for="name">name</label>
                    </th>
                    <td>
                        <input
                            type="text"
                            name="name"
                            size="40"
                            maxlength="30"
                            title="name of category"
                            placeholder="category"
                            required="required"
                            value="${sessionScope.category.name}">
                    </td>
                </tr>
                <tr>
                    <td>
                        <input type="reset" value="Reset">
                    </td>
                    <td>
                        <input type="submit" value="Save Category">
                    </td>
                </tr>
            </tbody>
        </table>
    </fieldset>
</form>

<c:import url="footer.jspf" charEncoding="UTF-8"/>
