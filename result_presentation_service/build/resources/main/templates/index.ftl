<!DOCTYPE html>
<html>
<head>
    <title>Time Complexity Analyzer</title>
</head>
<body>
    <h1>Enter Your Code</h1>
    <form action="/analyze" method="post">
        <textarea name="code" rows="10" cols="50"></textarea><br>
        <input type="submit" value="Analyze">
    </form>
    <#if error??>
        <p style="color:red;">Error: ${error}</p>
    </#if>
</body>
</html>
