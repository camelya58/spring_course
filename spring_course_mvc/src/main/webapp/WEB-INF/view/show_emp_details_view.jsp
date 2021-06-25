<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<body>
<h2>Dear Employee, you are welcome!</h2>
<br>
<br>
Your name: ${employee.name}
<br>
Your surname: ${employee.surname}
<br>
Your salary: ${employee.salary}
<br>
Your department: ${employee.department}
<br>
Your car: ${employee.carBrand}
<br>
Language(s):
<ul>
    <c:forEach var="lang" items="${employee.languages}">
        <li> ${lang}</li>
    </c:forEach>
</ul>
<br>
Phone number: ${employee.phoneNumber}
<br>
Email: ${employee.email}
<br>
</body>
</html>
