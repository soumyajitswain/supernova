<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
User Page.<a href="logout">Logout</a>
<br/>
<c:url var="eventsUrl" value="/admin/" />
<sec:authorize url="/admin/">
Admin Section
</sec:authorize>