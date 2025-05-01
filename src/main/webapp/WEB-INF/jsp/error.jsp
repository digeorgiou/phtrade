<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h1>Error Occurred</h1>
    <p>${error}</p>

    <div>
        BalanceList is empty: ${empty balanceList}<br>
        BalanceList is null: ${balanceList == null}<br>
        BalanceList size: ${not empty balanceList ? balanceList.size() : 0}
    </div>

</body>
</html>